package com.floyd.admin.user.product.controller;

import com.floyd.admin.user.brand.BrandService;
import com.floyd.admin.user.product.ProductService;
import com.floyd.common.entity.Category;
import com.floyd.common.entity.Product;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/products")
    public String listAll(Model model) {
        var listProducts = productService.listAll();
        model.addAttribute("listProducts", listProducts);
        return "/products/products";
    }

    @GetMapping("/products/new")
    public String createNewProduct(Model model) {
        var listBrands = brandService.findAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);

        return "/products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
        System.out.println(product.getName());
        System.out.println(product.getBrand().getName());
        System.out.println(product.getCategory().getName());

        return "redirect:/products";
    }

}
