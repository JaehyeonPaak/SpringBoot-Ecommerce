package com.floyd.admin.product;

import com.floyd.admin.user.product.ProductRepository;
import com.floyd.admin.user.product.ProductService;
import com.floyd.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    public void testListByPage() {
        var page = productService.listByPage(1, "name", "asc", null);
        var products = page.getContent();
        for (Product product : products) {
            System.out.println(product.getName());
        }
    }
}
