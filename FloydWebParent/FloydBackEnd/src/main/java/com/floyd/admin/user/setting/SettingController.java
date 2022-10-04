package com.floyd.admin.user.setting;

import com.floyd.admin.user.FileUploadUtil;
import com.floyd.admin.user.currency.CurrencyService;
import com.floyd.common.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/settings")
    public String listAll(Model model) {
        var listCurrencies = currencyService.listAll();
        var listSettings = settingService.listAll();

        model.addAttribute("pageTitle", "Settings - Floyd Admin");
        model.addAttribute("listCurrencies", listCurrencies);
        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "/settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(
            @RequestParam("fileImage") MultipartFile multipartFile,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws IOException {
        var settingBag = settingService.getGeneralSettings();

        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(settingBag, request);
        updateSettingValuesFromForm(settingBag.list(), request);

        redirectAttributes.addFlashAttribute("message", "General settings have been saved!");
        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;

            settingBag.updateSiteLogo(value);

            String uploadDir = "../site-logo";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(GeneralSettingBag settingBag, HttpServletRequest request) {
        var currencyId = request.getParameter("CURRENCY_ID");
        var currency = currencyService.findById(Integer.valueOf(currencyId));
        if (currency.isPresent()) {
            settingBag.updateCurrencySymbol(currency.get().getSymbol());
        }
    }

    private void updateSettingValuesFromForm(List<Setting> settings, HttpServletRequest request) {
        for (Setting setting : settings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        settingService.saveAll(settings);
    }
}
