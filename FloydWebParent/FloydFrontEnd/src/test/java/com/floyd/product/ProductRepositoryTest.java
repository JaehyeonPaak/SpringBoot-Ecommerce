package com.floyd.product;

import com.floyd.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByAlias() {
        String alias = "iphone_7";
        var product = productRepository.findByAlias(alias);
        System.out.println(product.getName());
        assertThat(product).isNotNull();
    }

    @Test
    public void testSearch() {
        String keyword = "iPhone";
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        var result = productRepository.search(keyword, pageable);
        for (Product product : result) {
            System.out.println(product.getName());
        }
    }
}
