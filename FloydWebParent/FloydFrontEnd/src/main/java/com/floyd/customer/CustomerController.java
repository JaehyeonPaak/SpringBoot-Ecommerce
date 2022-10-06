package com.floyd.customer;

import com.floyd.common.entity.Country;
import com.floyd.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        var listCountries = customerService.listAllCountries();

        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());
        model.addAttribute("listCountries", listCountries);

        return "register/register_form";
    }
}
