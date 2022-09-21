package com.floyd.admin.user.user.controller;

import com.floyd.admin.user.FileUploadUtil;
import com.floyd.admin.user.security.FloydUserDetails;
import com.floyd.admin.user.user.UserService;
import com.floyd.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal FloydUserDetails loggedInUser, Model model) {
        var email = loggedInUser.getUsername();
        var user = userService.getByEmail(email);

        model.addAttribute("user", user);
        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String updateDetails(User user, RedirectAttributes redirectAttributes, @AuthenticationPrincipal FloydUserDetails loggedInUser, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            var filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(filename);
            var savedUser = userService.updateUserDetails(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
        }
        else {
            if (user.getPhotos() == null) {
                user.setPhotos(null);
            }
            userService.updateUserDetails(user);
        }
        loggedInUser.setFirstName(user.getFirstName() );
        loggedInUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "Your account details have been updated!");
        return "redirect:/account";
    }
}
