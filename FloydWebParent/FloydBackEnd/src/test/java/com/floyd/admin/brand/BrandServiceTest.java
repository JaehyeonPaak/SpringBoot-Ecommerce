package com.floyd.admin.brand;

import com.floyd.admin.user.brand.BrandPageInfo;
import com.floyd.admin.user.brand.BrandRepository;
import com.floyd.admin.user.brand.BrandService;
import com.floyd.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTest {

    @MockBean
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName() {
        Integer id = null;
        String name = "EA sports";

        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        var result = brandService.checkUnique(id, name);
        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK() {
        Integer id = null;
        String name = "Nike";

        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        var result = brandService.checkUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName() {
        Integer id = 1;
        String name = "Adidas";

        Brand brand = new Brand();
        brand.setId(2);
        brand.setName(name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        var result = brandService.checkUnique(id, name);
        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK() {
        Integer id = 1;
        String name = "Puma";

        Brand brand = new Brand();
        brand.setId(2);
        brand.setName(name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        var result = brandService.checkUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }
}
