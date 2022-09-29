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

    public Category getCategory(String alias) {
        return categoryRepository.findByAliasEnabled(alias);
    }

}
