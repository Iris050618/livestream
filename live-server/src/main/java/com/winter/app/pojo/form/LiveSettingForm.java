package com.winter.app.pojo.form;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LiveSettingForm {
    private Integer groupId;
    private String title;
    private String banner;
}
