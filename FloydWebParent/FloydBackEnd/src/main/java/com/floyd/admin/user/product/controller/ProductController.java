package com.floyd.admin.user.product.controller;

import com.floyd.admin.user.FileUploadUtil;
import com.floyd.admin.user.brand.BrandService;
import com.floyd.admin.user.product.ProductNotFoundException;
import com.floyd.admin.user.product.ProductService;
import com.floyd.common.entity.Category;
import com.floyd.common.entity.Product;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;

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
    public String saveProduct(
            Product product,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "fileImage") MultipartFile mainImageMultipartFile,
            @RequestParam(name = "extraImage") MultipartFile[] extraImageMultipartFiles) throws IOException {
        if (!mainImageMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
            product.setMainImage(fileName);
        }
        if (extraImageMultipartFiles.length > 0) {
            for (MultipartFile multipartFile : extraImageMultipartFiles) {
                if (multipartFile.isEmpty()) {
                    continue;
                }
                String extraFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                product.addExtraImage(extraFileName);
            }
        }

        var savedProduct = productService.save(product);

        if (!mainImageMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
            String uploadDir = "../product-images/" + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipartFile);
        }
        if (extraImageMultipartFiles.length > 0) {
            String uploadExtraDir = "../product-images/" + savedProduct.getId() + "/extras";
            FileUploadUtil.cleanDir(uploadExtraDir);
            for (MultipartFile multipartFile : extraImageMultipartFiles) {
                if (multipartFile.isEmpty()) {
                    continue;
                }
                String extraFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadExtraDir, extraFileName, multipartFile);
            }
        }
        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully!");
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            String productExtraImagesDir = "../product-images/" + id + "/extras";
            String productImagesDir = "../product-images/" + id;
            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);

            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "The product has been deleted successfully!");
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, enabled);
        String message = (enabled ? "The product ID " + id + " has been enabled" : "The product ID " + id + " has been disabled");
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/products";
    }
}
