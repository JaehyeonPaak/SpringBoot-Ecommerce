package com.floyd.common.entity;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SettingBag {

    private List<Setting> listSettings;

    public Setting get(String key) {
        int index = listSettings.indexOf(new Setting(key));
        if (index >= 0) {
            return listSettings.get(index);
        }
        return null;
    }

    public String getValue(String key) {
        var setting = get(key);
        if (setting != null) {
            return setting.getValue();
        }
        return null;
    }

    public void update(String key, String value) {
        var setting = get(key);
        if (setting != null) {
            setting.setValue(value);
        }
    }

    public List<Setting> list() {
        return listSettings;
    }
}
