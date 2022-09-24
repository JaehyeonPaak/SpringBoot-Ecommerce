package com.floyd.admin.brand;

import com.floyd.admin.user.FloydBackEndApplication;
import com.floyd.admin.user.brand.BrandRepository;
import com.floyd.admin.user.category.CategoryRepository;
import com.floyd.common.entity.Brand;
import com.floyd.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FloydBackEndApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCreateBrand() {
        var category = entityManager.find(Category.class, 1);
        Brand brand = new Brand();
        brand.setName("Hwawei");
        brand.setLogo("default.png");
        brand.getCategories().add(category);

        Brand savedBrand = brandRepository.save(brand);

        assertThat(savedBrand.getName()).isEqualTo("Hwawei");
    }

    @Test
    public void testCreateBrand2() {
        var iphone7 = entityManager.find(Category.class, 10);
        var iphone14 = entityManager.find(Category.class, 11);

        Brand brand = new Brand();
        brand.setName("Apple");
        brand.setLogo("default.png");
        brand.getCategories().addAll(List.of(iphone7, iphone14));

        Brand savedBrand = brandRepository.save(brand);

        assertThat(savedBrand.getCategories().size()).isEqualTo(2);
    }

    @Test
    public void testFindAll() {
        var brands = brandRepository.findAll();
        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetById() {
        var brand = brandRepository.findById(1).get();
        assertThat(brand.getName()).isEqualTo("Samsung");
    }

    @Test
    public void testUpdateName() {
        String name = "Samsung Electronics";
        var brand = brandRepository.findById(1).get();
        brand.setName(name);
        assertThat(brand.getName()).isEqualTo(name);
    }

    @Test
    public void testDelete() {
        Integer id = 6;
        var brand = brandRepository.findById(id).get();
        brandRepository.deleteById(id);
        var deletedBrand = brandRepository.findById(id).orElse(null);
        assertThat(deletedBrand).isNull();
    }

    @Test
    public void testListBrandsAscending() {
        int pageNum = 1;
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNum - 1, 5, sort);
        var listBrands = brandRepository.listBrands(pageable);
        var brands = listBrands.getContent();
        for (Brand brand : brands) {
            System.out.println(brand.getName());
        }
    }

    @Test
    public void testListBrandDescending() {
        int pageNum = 1;
        Sort sort = Sort.by("name").descending();
        Pageable pageable = PageRequest.of(pageNum - 1, 5, sort);
        var listBrands = brandRepository.listBrands(pageable);
        var brands = listBrands.getContent();
        for (Brand brand : brands) {
            System.out.println(brand.getName());
        }
    }

    @Test
    public void testSearch() {
        String keyword = "Apple";
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(0, 5, sort);
        var pageBrands = brandRepository.search(keyword, pageable);
        var result = pageBrands.getContent();
        assertThat(result.size()).isEqualTo(1);
    }
}
