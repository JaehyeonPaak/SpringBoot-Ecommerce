package com.floyd.admin.user;

import com.floyd.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model) {
        var listUsers = userService.listUsers();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/users/new")
    public String createNewUser(Model model) {
        User user = new User();
        user.setEnabled(true);

        var roles = userService.listRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveNewUser(User user) {
        userService.save(user);
        return "redirect:/users";
    }
}
