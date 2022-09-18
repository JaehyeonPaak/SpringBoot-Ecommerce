package com.floyd.admin.user;

import com.floyd.admin.FileUploadUtil;
import com.floyd.common.entity.User;
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

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return listByPage(1, "firstName", "asc", null, model);
    }

    @GetMapping("/users/new")
    public String createNewUser(Model model) {
        User user = new User(); // created with id = null...
        user.setEnabled(true);

        var roles = userService.listRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", "Create New User");

        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveNewUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            var filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(filename);
            var savedUser = userService.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
        }
        else {
            if (user.getPhotos() == null) {
                user.setPhotos(null);
            }
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");
        //userService.save(user);

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var user = userService.get(id);
            var roles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");

            return "user_form";
        }
        catch(UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully!");
        }
        catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);

        String message = (enabled ? "The user ID " + id + " has been enabled" : "The user ID " + id + " has been disabled");
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }

    // localhost:8080/users/page/1/sortField=firstName&sortDir=asc&keyword=conor
    @GetMapping("/users/page/{pageNumber}")
    public String listByPage(
            @PathVariable(name = "pageNumber") int pageNumber,
            @RequestParam(name = "sortField") String sortField,
            @RequestParam(name = "sortDir") String sortDir,
            @RequestParam(name = "keyword") String keyword,
            Model model) {
        var page = userService.listByPage(pageNumber, sortField, sortDir, keyword);
        var listUsers = page.getContent();
        var reverseDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("listUsers", listUsers);
        model.addAttribute("usersPerPage", userService.USERS_PER_PAGE);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseDir", reverseDir);
        model.addAttribute("keyword", keyword);

        return "users";
    }

}
