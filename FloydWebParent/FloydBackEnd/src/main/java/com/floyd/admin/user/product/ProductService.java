package com.floyd.admin.user.product;

import com.floyd.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Product save(Product product) {
        boolean isCreating = product.getId() == null;
        if (isCreating) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().toLowerCase().replaceAll(" ", "_");
            product.setAlias(defaultAlias);
        }
        else {
            product.setAlias(product.getAlias().toLowerCase().replaceAll(" ", "_"));
        }
        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }

    public void deleteById(Integer id) throws ProductNotFoundException {
        Long products = productRepository.countById(id);
        if (products == null || products == 0) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
        productRepository.deleteById(id);
    }
}
