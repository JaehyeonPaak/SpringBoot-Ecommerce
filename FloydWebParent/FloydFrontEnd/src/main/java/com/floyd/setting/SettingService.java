package com.floyd.setting;

import com.floyd.common.entity.Setting;
import com.floyd.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> getGeneralSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }
}
