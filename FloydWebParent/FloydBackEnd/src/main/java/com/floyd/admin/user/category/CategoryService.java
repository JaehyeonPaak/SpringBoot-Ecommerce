package com.floyd.admin.user.category;

import com.floyd.admin.user.user.UserNotFoundException;
import com.floyd.common.entity.Category;
import com.floyd.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listCategories() {
        var rootCategories = categoryRepository.listRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();
        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(rootCategory);
            var children = rootCategory.getChildren();
            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                subCategory.setName(name);
                hierarchicalCategories.add(subCategory);

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
        var children = parent.getChildren();
        int newSubLevel = subLevel + 1;
        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            subCategory.setName(name);
            hierarchicalCategories.add(subCategory);
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
        }
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        var categoriesInDB = categoryRepository.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(category);
                var children = category.getChildren();
                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();
                    subCategory.setName(name);
                    categoriesUsedInForm.add(subCategory);
                    listChildren(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        var children = parent.getChildren();
        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            categoriesUsedInForm.add(new Category(name));
            listChildren(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        }
        catch (NoSuchElementException e) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingCategory = (id == null || id == 0);
        var categoryByName = categoryRepository.findByName(name);
        var categoryByAlias = categoryRepository.findByAlias(alias);

        if (isCreatingCategory) { // when create new category...
            if (categoryByName != null) {
                return "DuplicateName";
            }
            else {
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        }
        else { // when edit category details...
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicateName";
            }
            else {
                if (categoryByAlias != null && categoryByAlias.getId() != id) {
                    return "DuplicateAlias";
                }
            }
        }
        return "OK";
    }
}
