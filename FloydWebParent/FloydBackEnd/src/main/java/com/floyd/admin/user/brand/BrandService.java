package com.floyd.admin.user.brand;

import com.floyd.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    private static final int BRANDS_PER_PAGE = 5;

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
            if (duplicateBrand != null && duplicateBrand.getId() != id) {
                return "DuplicateName";
            }
        }
        return "OK";
    }

    public List<Brand> listByPage(BrandPageInfo brandPageInfo, int pageNum, String keyword, String sortDir) {
        Sort sort = Sort.by("name");
        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        }
        else {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);
        Page<Brand> pageBrands = null;
        if (keyword == null) {
            pageBrands = brandRepository.listBrands(pageable);
        }
        else {
            pageBrands = brandRepository.search(keyword, pageable);
        }
        brandPageInfo.setTotalPages(pageBrands.getTotalPages());
        brandPageInfo.setTotalElements(pageBrands.getTotalElements());
        return pageBrands.getContent();
    }
}
