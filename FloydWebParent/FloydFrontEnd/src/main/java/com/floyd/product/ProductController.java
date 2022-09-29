package com.floyd.product;

import com.floyd.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        var category = categoryService.getCategory(alias);
        if (category == null) {
            return "error/404";
        }
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

        return "products_by_category";
    }
}
