package com.floyd.customer;

import com.floyd.Utility;
import com.floyd.common.entity.Country;
import com.floyd.common.entity.Customer;
import com.floyd.setting.EmailSettingBag;
import com.floyd.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        var listCountries = customerService.listAllCountries();

        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());
        model.addAttribute("listCountries", listCountries);

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model, HttpServletRequest servletRequest) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(customer, servletRequest);

        model.addAttribute("pageTitle", "Registration Succeeded!");
        return "register/register_success";
    }

    private void sendVerificationEmail(Customer customer, HttpServletRequest servletRequest) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFormAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", customer.getFirstName() + " " + customer.getLastName());
        String verifyURL = Utility.getSiteURL(servletRequest) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam(name = "code") String verificationCode, Model model) {
        var verified = customerService.verify(verificationCode);
        String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed!";
        model.addAttribute("pageTitle", pageTitle);
        return "register/" + (verified ? "verify_success" : "verify_fail");
    }
}
