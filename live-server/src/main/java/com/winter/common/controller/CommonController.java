package com.winter.common.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.winter.common.pojo.*;
import com.winter.entity.User;
import com.winter.utils.AesEncryptUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.winter.utils.JWTUtil;
import com.winter.utils.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class CommonController {
//    private JSONObject data;

//    public CommonController() throws IOException {
//        InputStream inputStream = this.getClass().getResourceAsStream("/data.json");
//        this.data = JSON.parseObject(inputStream, JSONObject.class);
//        System.out.println(this.data);
//    }

    @GetMapping("/401")
    public Response notAuth() {
        return Response.failed(ResponseType.UNAUTHORIZED);
    }

    @GetMapping("/404")
    public Response notFound() {
        return Response.failed(ResponseType.NOT_FOUND);
    }

    @GetMapping("/notlogin")
    public Response notlogin() {
        return Response.failed(ResponseType.NOT_LOGIN);
    }

    @GetMapping("/code")
    public Response getCode() throws Exception {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 40);
        captcha.getArithmeticString();
        // 获取运算的结果
        String result = captcha.text();
        String content = result + "_" + System.currentTimeMillis();
        String key = AesEncryptUtil.encrypt(content);
        CodeVO codeVO = new CodeVO(captcha.toBase64(), key);
        return Response.success(codeVO);
    }

    /**
     * 用户登录
     * @param loginForm
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public Response<String> Login(@Validated @RequestBody LoginForm loginForm) throws Exception {
        String userName = loginForm.getUserName();
        String password = loginForm.getPassword();
        String key = loginForm.getKey();
        String code = loginForm.getCode();
        String result = AesEncryptUtil.decrypt(key);
        String[] list = result.split("_");
        String realCode = list[0];
        long time = Long.parseLong(list[1]);
        long now = System.currentTimeMillis();
        if (!"000000".equals(code)) {
            // 万能验证码直接跳过
            if (now - time > 60 * 1000) {
                // 验证码一分钟过期
                return Response.failed("验证码已过期");
            }
            if (!code.equals(realCode)) {
                return Response.failed("验证码错误");
            }
        }
        User user = new User();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        queryWrapper.select("id", "user_name", "password", "name", "salt").last("limit 1");
        user = user.selectOne(queryWrapper);
        if(user == null){
            return Response.failed("账号或密码有误");
        }
        if (!LoginUtil.equals(password, user.getPassword(), user.getSalt())) {
            return Response.failed("账号或密码有误");
        }
        UserToken userToken = BeanUtil.copyProperties(user, UserToken.class);
        userToken.setPort(PortType.WEB);
        String token = JWTUtil.sign(userToken);
        return Response.success(token);
    }

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @GetMapping("/user/info")
    public Response<UserInfo> getUserInfo(HttpServletRequest request){
        UserToken userToken = (UserToken) request.getAttribute("user");
        Integer userId = userToken.getId();
        User user = new User();
        user.setId(userId);
        user = user.selectById();
        UserInfo info = BeanUtil.copyProperties(user, UserInfo.class);
        return Response.success(info);
    }
}
