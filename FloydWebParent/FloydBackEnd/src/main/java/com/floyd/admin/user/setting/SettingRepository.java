package com.floyd.admin.user.setting;

import com.floyd.common.entity.Setting;
import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository<Setting, String> {
}
