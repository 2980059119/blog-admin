package com.blog.modules.security.controller;

import cn.hutool.core.util.IdUtil;
import com.blog.annotation.rest.AnonymousDeleteMapping;
import com.blog.annotation.rest.AnonymousGetMapping;
import com.blog.annotation.rest.AnonymousPostMapping;
import com.blog.commom.redis.service.RedisService;
import com.blog.config.RsaProperties;
import com.blog.exception.BadRequestException;
import com.blog.modules.logging.annotation.Log;
import com.blog.modules.security.config.bean.LoginCodeEnum;
import com.blog.modules.security.config.bean.LoginProperties;
import com.blog.modules.security.config.bean.SecurityProperties;
import com.blog.modules.security.security.TokenProvider;
import com.blog.modules.security.service.OnlineUserService;
import com.blog.modules.security.service.dto.AuthUserDto;
import com.blog.modules.security.service.dto.JwtUserDto;
import com.blog.utils.RsaUtils;
import com.blog.utils.SecurityUtils;
import com.blog.utils.StringUtils;
import com.wf.captcha.base.Captcha;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 授权、根据token获取用户详细信息
 *
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final SecurityProperties properties;
    private final RedisService redisService;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Resource
    private LoginProperties loginProperties;

    @Log("用户登录")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 开启验证码
        if(loginProperties.getLoginCode().getEnabled()) {
            // 查询验证码
            String code = (String) redisService.get(authUser.getUuid());
            // 清除验证码
            redisService.del(authUser.getUuid());
            if (StringUtils.isBlank(code)) {
                throw new BadRequestException("验证码不存在或已过期");
            }
            if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
                throw new BadRequestException("验证码错误");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return ResponseEntity.ok(authInfo);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        return ResponseEntity.ok(SecurityUtils.getCurrentUser());
    }

    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        if (!loginProperties.getLoginCode().getEnabled()) {
            // 验证码信息
            Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
                put("enabled", 0);
            }};
            return ResponseEntity.ok(imgResult);
        }
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisService.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<>(2) {{
            put("enabled", 1);
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
