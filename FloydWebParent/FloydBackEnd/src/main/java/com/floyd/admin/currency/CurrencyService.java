package com.floyd.admin.currency;

import com.floyd.common.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> listAll() {
        return currencyRepository.findAllByOrderByNameAsc();
    }

    public Optional<Currency> findById(Integer id) {
        return currencyRepository.findById(id);
    }
}
