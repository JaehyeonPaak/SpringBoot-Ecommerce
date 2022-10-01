package com.floyd.product;

import com.floyd.category.CategoryNotFoundException;
import com.floyd.category.CategoryService;
import com.floyd.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable(name = "category_alias") String alias, Model model) {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable(name = "category_alias") String alias, @PathVariable(name = "pageNum") int pageNum, Model model) {
        try {
            Category category = categoryService.getCategory(alias);
            var listCategoryParents = categoryService.getCategoryParents(category);
            var pageProducts = productService.listByCategory(pageNum, category.getId());
            var listProducts = pageProducts.getContent();

            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("category", category);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("totalItems", pageProducts.getTotalElements());

            return "product/products_by_category";
        } catch (CategoryNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable(name = "product_alias") String alias, Model model) {
        try {
            var product = productService.getProduct(alias);
            var listCategoryParents = categoryService.getCategoryParents(product.getCategory());
            model.addAttribute("pageTitle", product.getName());
            model.addAttribute("product", product);
            model.addAttribute("listCategoryParents", listCategoryParents);

            return "product/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@RequestParam("keyword") String keyword, Model model) {
        return searchByPage(1, keyword, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@PathVariable("pageNum") int pageNum, @RequestParam("keyword") String keyword, Model model) {
        var pageProducts = productService.search(keyword, pageNum);
        var listProducts = pageProducts.getContent();

        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Search Result");

        return "product/search_result";
    }
}
