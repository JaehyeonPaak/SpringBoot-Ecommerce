package com.floyd.admin.user.brand.controller;

import com.floyd.admin.user.brand.BrandService;
import com.floyd.admin.user.category.CategoryService;
import com.floyd.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listAll(Model model) {
        var listBrands = brandService.findAll();
        model.addAttribute("listBrands", listBrands);

        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String createNewBrand(Model model) {
        Brand brand = new Brand();
        var listCategories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("pageTitle", "Create New Brand");
        model.addAttribute("brand", brand);
        model.addAttribute("listCategories", listCategories);

        return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand() {
        return "redirect:/brands";
    }
}
