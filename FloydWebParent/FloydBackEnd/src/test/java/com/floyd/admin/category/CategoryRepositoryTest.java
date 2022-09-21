package com.floyd.admin.category;

import com.floyd.admin.user.FloydBackEndApplication;
import com.floyd.admin.user.category.CategoryRepository;
import com.floyd.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FloydBackEndApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics", "Electronics", "default.png");
        var savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory() {
        var category = categoryRepository.findById(8).orElse(null);
        Category subCategory = new Category("Galaxy 12", "Galaxy 12", "default.png", category);
        var savedCategory = categoryRepository.saveAll(List.of(subCategory));
    }

    @Test
    public void testGetCategory() {
        Category category = categoryRepository.findById(2).get();
        System.out.println("Parents name: " + category.getName());
        var children = category.getChildren();
        for (Category subCategory : children) {
            System.out.println("Child name: " + subCategory.getName());
        }
        assertThat(category.getChildren().size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories() {
        var categories = categoryRepository.findAll();
        for (Category category : categories) {
            if(category.getParent() == null) {
                System.out.println(category.getName());
                var children = category.getChildren();
                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        var children = parent.getChildren();
        for (Category subCategory : children) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListRootCategories() {
        var rootCategories = categoryRepository.listRootCategories();
        assertThat(rootCategories.size()).isEqualTo(1);
    }
}
