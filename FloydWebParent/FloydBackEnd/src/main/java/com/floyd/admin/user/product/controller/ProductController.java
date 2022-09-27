package com.floyd.admin.user.product.controller;

import com.floyd.admin.user.FileUploadUtil;
import com.floyd.admin.user.brand.BrandService;
import com.floyd.admin.user.product.ProductNotFoundException;
import com.floyd.admin.user.product.ProductService;
import com.floyd.common.entity.Category;
import com.floyd.common.entity.Product;
import com.floyd.common.entity.ProductImage;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

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
        model.addAttribute("numberOfExcistingExtraImages", 0);

        return "/products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(
            Product product,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "fileImage") MultipartFile mainImageMultipartFile,
            @RequestParam(name = "extraImage") MultipartFile[] extraImageMultipartFiles,
            @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
            @RequestParam(name = "detailNames", required = false) String[] detailNames,
            @RequestParam(name = "detailValues", required = false) String[] detailValues,
            @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
            @RequestParam(name = "imageNames", required = false) String[] imageNames) throws IOException {
        if (imageIDs != null && imageNames.length != 0) {
            Set<ProductImage> images = new HashSet<>();
            for (int count = 0; count < imageIDs.length; count++) {
                Integer id = Integer.parseInt(imageIDs[count]);
                String name = imageNames[count];
                if (id != null && !name.isEmpty()) {
                    images.add(new ProductImage(id, name, product));
                }
                product.setImages(images);
            }
        }
        if (detailNames != null && detailNames.length != 0) {
            for (int count = 0; count < detailNames.length; count++) {
                String name = detailNames[count];
                String value = detailValues[count];
                Integer id = Integer.parseInt(detailIDs[count]);
                if (id != 0) {
                    product.addDetails(id, name, value);
                }
                else if (!name.isEmpty() && !value.isEmpty()) {
                    product.addDetails(name, value);
                }
            }
        }
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
                if (!product.containsImageName(extraFileName)) {
                    product.addExtraImage(extraFileName);
                }
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

        String extraImageDir = "../product-images" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String filename = file.toFile().getName();
                if (!product.containsImageName(filename)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("Deleted extra image: " + filename);
                    }
                    catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + filename);
                    }
                }
            });
        }
        catch (IOException e) {
            LOGGER.error("Could not list directory: " + dirPath);
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

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var product = productService.get(id);
            var listBrands = brandService.findAll();
            Integer numberOfExcistingExtraImages = 0;
            if (product.getImages() != null) {
                numberOfExcistingExtraImages = product.getImages().size();
            }
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("numberOfExcistingExtraImages", numberOfExcistingExtraImages);

            return "products/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }

    }
}
