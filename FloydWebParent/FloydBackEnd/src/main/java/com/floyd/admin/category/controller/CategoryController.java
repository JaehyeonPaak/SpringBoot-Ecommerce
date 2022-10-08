package com.floyd.admin.category.controller;

import com.floyd.admin.category.CategoryService;
import com.floyd.admin.FileUploadUtil;
import com.floyd.admin.category.CategoryNotFoundException;
import com.floyd.admin.category.CategoryPageInfo;
import com.floyd.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
        if (sortDir == null) {
            sortDir = "asc";
        }
        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
        var listCategories = categoryService.listByPage(categoryPageInfo, pageNum, sortDir, keyword);
        var reverseDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseDir", reverseDir);
        model.addAttribute("totalItems", categoryPageInfo.getTotalElements());
        model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("categoriesPerPage", categoryService.ROOT_CATEGORIES_PER_PAGE);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortDir", sortDir);

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String createNewCategory(Model model) {
        var categories = categoryService.listCategoriesUsedInForm();
        Category category = new Category();
        category.setEnabled(true);

        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, RedirectAttributes redirectAttributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            var filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(filename);
            var savedCategory = categoryService.save(category);
            String uploadDir = "../category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
        }
        else {
            if (category.getImage() == null) {
                category.setImage(null);
            }
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully!");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var category = categoryService.get(id);
            var categories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("categories", categories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

            return "categories/category_form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);
        String message = (enabled ? "The category ID " + id + " has been enabled" : "The category ID " + id + " has been disabled");
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            String uploadDir = "../category-images/" + id;
            FileUploadUtil.removeDir(uploadDir);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully!");
        }
        catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }

}
