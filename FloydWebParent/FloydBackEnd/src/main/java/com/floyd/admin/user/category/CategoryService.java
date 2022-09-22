package com.floyd.admin.user.category;

import com.floyd.admin.user.user.UserNotFoundException;
import com.floyd.common.entity.Category;
import com.floyd.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listCategories(String sortDir) {
        Sort sort = Sort.by("name");
        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        }
        else {
            sort = sort.descending();
        }
        var rootCategories = categoryRepository.listRootCategories(sort);

        return listHierarchicalCategories(rootCategories, sortDir);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();
        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(rootCategory);
            var children = sortDir == null ? sortSubCategories(rootCategory.getChildren()) : sortSubCategories(rootCategory.getChildren(), sortDir);
            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                subCategory.setName(name);
                hierarchicalCategories.add(subCategory);

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
        var children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;
        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            subCategory.setName(name);
            hierarchicalCategories.add(subCategory);
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        var categoriesInDB = categoryRepository.findAll(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(category);
                var children = sortSubCategories(category.getChildren());
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
        var children = sortSubCategories(parent.getChildren());
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

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category category1, Category category2) {
                if (sortDir.equals("asc")) {
                    return category1.getName().compareTo(category2.getName());
                }
                else {
                    return category1.getName().compareTo(category2.getName()) * (-1);
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }
}
