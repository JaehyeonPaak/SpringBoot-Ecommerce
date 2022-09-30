package com.floyd.category;

import com.floyd.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listNoChildrenCategories() {
        List<Category> listNoChildrenCategories = new ArrayList<>();
        var listEnabledCategories = categoryRepository.findAllEnabled();
        for (Category category : listEnabledCategories) {
            var children = category.getChildren();
            if (children == null || children.size() == 0) {
                listNoChildrenCategories.add(category);
            }
        }
        return listNoChildrenCategories;
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
        var category = categoryRepository.findByAliasEnabled(alias);
        if (category == null) {
            throw new CategoryNotFoundException("Could not find any category with alias " + alias);
        }
        return category;
    }

    public List<Category> getCategoryParents(Category child) {
        List<Category> listParents = new ArrayList<>();
        var parents = child.getParent();
        while (parents != null) {
            listParents.add(0, parents);
            parents = parents.getParent();
        }
        listParents.add(child);

        return listParents;
    }
}
