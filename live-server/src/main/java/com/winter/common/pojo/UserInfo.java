package com.winter.common.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfo {
    private Integer id;
    private String userName;
    private String name;
    private String avatar;
}
