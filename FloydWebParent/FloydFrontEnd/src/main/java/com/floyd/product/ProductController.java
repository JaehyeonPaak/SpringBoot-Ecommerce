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
    public String viewCategory(@PathVariable(name = "category_alias") String alias, Model model) {
        var category = categoryService.getCategory(alias);
        if (category == null) {
            return "error/404";
        }
        var listCategoryParents = categoryService.getCategoryParents(category);

        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("listCategoryParents", listCategoryParents);

        return "products_by_category";
    }
}
