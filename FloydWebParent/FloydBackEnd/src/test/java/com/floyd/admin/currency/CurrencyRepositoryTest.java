package com.floyd.admin.currency;

import com.floyd.admin.FloydBackEndApplication;
import com.floyd.common.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(classes = FloydBackEndApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void testCreateCurrencies() {
        List<Currency> listCurrencies = List.of(
                new Currency("Dollar", "$", "USD"),
                new Currency("Won", "₩", "KRW"),
                new Currency("Euro", "€", "EUR"),
                new Currency("Yen", "￥", "JPY")
        );

        var savedCurrencies = currencyRepository.saveAll(listCurrencies);
        assertThat(savedCurrencies).isNotNull();
    }

    @Test
    public void testListAllOrderByNameAsc() {
        var currencies = currencyRepository.findAllByOrderByNameAsc();
        for (Currency currency : currencies) {
            System.out.println(currency.getName());
        }
        assertThat(currencies).size().isGreaterThan(0);
    }
}
