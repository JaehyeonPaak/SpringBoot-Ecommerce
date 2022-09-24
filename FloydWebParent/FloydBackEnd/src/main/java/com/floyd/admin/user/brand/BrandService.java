package com.floyd.admin.user.brand;

import com.floyd.admin.user.category.CategoryNotFoundException;
import com.floyd.admin.user.user.UserNotFoundException;
import com.floyd.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAll() {
        return (List<Brand>) brandRepository.findAll();
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        }
        catch (NoSuchElementException e) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    public void deleteById(Integer id) throws BrandNotFoundException {
        Long count = brandRepository.countById(id);
        if(count == null | count == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
        brandRepository.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreating = (id == null || id == 0);
        var duplicateBrand = brandRepository.findByName(name);
        if (isCreating) {
            if (duplicateBrand != null) {
                return "DuplicateName";
            }
        }
        else {
            if (duplicateBrand != null) {
                return "DuplicateName";
            }
        }
        return "OK";
    }
}
