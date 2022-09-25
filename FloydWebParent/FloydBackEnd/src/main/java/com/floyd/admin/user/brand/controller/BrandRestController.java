package com.floyd.admin.user.brand.controller;

import com.floyd.admin.user.brand.BrandNotFoundException;
import com.floyd.admin.user.brand.BrandNotFoundRestException;
import com.floyd.admin.user.brand.BrandService;
import com.floyd.admin.user.brand.CategoryDTO;
import com.floyd.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BrandRestController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return brandService.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer id) throws BrandNotFoundRestException {
        try {
            var brand = brandService.get(id);
            var categories = brand.getCategories();
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            for (Category category : categories) {
                CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
                categoryDTOS.add(categoryDTO);
            }
            return categoryDTOS;
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }
}
