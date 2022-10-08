package com.floyd.admin.customer;

import com.floyd.admin.country.CountryRepository;
import com.floyd.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/customers")
    public String listFirstPage(Model model) {
        return listByPage(1, "firstName", "asc", null, model);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum,
            @RequestParam(name = "sortField") String sortField,
            @RequestParam(name = "sortDir") String sortDir,
            @RequestParam(name = "keyword") String keyword,
            Model model) {
        var page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
        var listCustomers = page.getContent();
        var reverseDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("customersPerPage", customerService.CUSTOMERS_PER_PAGE);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseDir", reverseDir);
        model.addAttribute("keyword", keyword);

        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "status") boolean enabled,
            RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnabledStatus(id, enabled);
        String message = "The customer ID " + id + " has been " + (enabled ? "enabled" : "disabled");
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var customer = customerService.get(id);
            model.addAttribute("customer", customer);
            return "customers/customer_detail_modal";
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The customer ID " + id + " has been deleted successfully!");
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var customer = customerService.get(id);
            var listCountries = countryRepository.findAllByOrderByNameAsc();
            model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
            model.addAttribute("customer", customer);
            model.addAttribute("listCountries", listCountries);

            return "customers/customer_form";
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes) {
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "The customer ID " + customer.getId() + " has been edited successfully!");
        return "redirect:/customers";
    }
}
