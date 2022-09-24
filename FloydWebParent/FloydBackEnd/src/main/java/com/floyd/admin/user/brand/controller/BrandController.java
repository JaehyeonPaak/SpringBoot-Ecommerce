package com.floyd.admin.user.brand.controller;

import com.floyd.admin.user.FileUploadUtil;
import com.floyd.admin.user.brand.BrandNotFoundException;
import com.floyd.admin.user.brand.BrandService;
import com.floyd.admin.user.category.CategoryNotFoundException;
import com.floyd.admin.user.category.CategoryService;
import com.floyd.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

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
    public String saveBrand(Brand brand, RedirectAttributes redirectAttributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            var filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(filename);
            var savedBrand = brandService.save(brand);
            String uploadDir = "../brand-logos/" + savedBrand.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
        }
        else {
            if (brand.getLogo() == null) {
                brand.setLogo(null);
            }
            brandService.save(brand);
        }
        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully!");
        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var brand = brandService.get(id);
            var listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

            return "brands/brand_form";
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            brandService.deleteById(id);
            String uploadDir = "../brand-logos/" + id;
            FileUploadUtil.removeDir(uploadDir);
            redirectAttributes.addFlashAttribute("message", "The brand has been deleted successfully!");
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/brands";
    }
}
