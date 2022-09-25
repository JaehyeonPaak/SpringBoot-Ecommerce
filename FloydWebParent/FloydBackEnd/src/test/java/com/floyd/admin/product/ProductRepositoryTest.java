package com.floyd.admin.product;

import com.floyd.admin.user.FloydBackEndApplication;
import com.floyd.admin.user.product.ProductRepository;
import com.floyd.common.entity.Brand;
import com.floyd.common.entity.Category;
import com.floyd.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FloydBackEndApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCreateProduct() {
        var brand = entityManager.find(Brand.class, 7);
        var category = entityManager.find(Category.class, 1);

        Product product1 = new Product();
        product1.setName("Samsung Galaxy 20");
        product1.setAlias("samsung_galaxy_20");
        product1.setShortDescription("A new released phone from Samsung");
        product1.setFullDescription("Full description for Samsung Galaxy 20");
        product1.setCreatedTime(new Date());
        product1.setUpdatedTime(new Date());
        product1.setPrice(1200000);
        product1.setBrand(brand);
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setName("Samsung Galaxy 18 Max");
        product2.setAlias("samsung_galaxy_18_max");
        product2.setShortDescription("A new released phone from Samsung");
        product2.setFullDescription("Full description for Samsung Galaxy 18 Max");
        product2.setCreatedTime(new Date());
        product2.setUpdatedTime(new Date());
        product2.setPrice(900000);
        product2.setBrand(brand);
        product2.setCategory(category);

        Product product3 = new Product();
        product3.setName("iPhone 13 Pro Max");
        product3.setAlias("iphone_13_pro_max");
        product3.setShortDescription("A new released phone from Apple");
        product3.setFullDescription("Full description for iPhone 13 Pro Max");
        product3.setCreatedTime(new Date());
        product3.setUpdatedTime(new Date());
        product3.setPrice(1500000);
        product3.setBrand(brand);
        product3.setCategory(category);

        var savedProduct = (List<Product>) productRepository.saveAll(List.of(product1, product2, product3));

        assertThat(savedProduct.size()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts() {
        var listProducts = (List<Product>) productRepository.findAll();
        assertThat(listProducts.size()).isEqualTo(3);
    }

    @Test
    public void testGetProduct() {
        Integer id = 1;
        var product = productRepository.findById(id).get();
        assertThat(product.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 5;
        var product = productRepository.findById(id).get();
        product.setShortDescription("The most competitive phone from Samsung");
        product.setUpdatedTime(new Date());
        product.setPrice(950000);
        var updatedProduct = productRepository.save(product);
        assertThat(updatedProduct.getPrice()).isEqualTo(950000);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 3;
        productRepository.deleteById(id);
        var product = productRepository.findById(id).orElse(null);
        assertThat(product).isNull();
    }
}
