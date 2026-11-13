package com.winter.common.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginForm {
    @NotEmpty
    @Size(min = 3, max = 16, message="账号长度4-16位字符")
    private String userName;
    public void setUserName(String name){
        this.userName = name != null ? name.trim() : null;
    }
    @Size(min = 6, max = 16, message="密码长度6-16位字符")
    @NotEmpty
    @Pattern(regexp = "[0-9a-zA-Z~!@#$%^&*()_+\\-=]{6,16}", message="密码格式不正确")
    private String password;
    @Size(min = 1, max = 6, message="验证码不正确")
    @NotEmpty
    private String code;
    @NotEmpty
    private String key;
}
