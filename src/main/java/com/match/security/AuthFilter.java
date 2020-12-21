package com.match.security;

import com.google.common.base.Strings;
import com.match.base.LoginUserUtil;
import com.match.entity.User;
import com.match.service.ISmsService;
import com.match.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISmsService smsService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String name = obtainUsername(request);
        if (!Strings.isNullOrEmpty(name)){
            request.setAttribute("username",name);
            return super.attemptAuthentication(request,response);
        }
        String telephone = request.getParameter("telephone");
        if (Strings.isNullOrEmpty(telephone) || !LoginUserUtil.checkTelephone(telephone)){
            throw new BadCredentialsException("Wrong telephone number");
        }

        User user = userService.findUserByTelephone(telephone);
        String inputCode = request.getParameter("smsCode");
        String sessionCode = smsService.getSmsCode(telephone);
        if (Objects.equals(inputCode,sessionCode)){
            if (user == null){
                //如果用户第一次用手机登录则自动注册改用户
                user = userService.addUserByPhone(telephone);
            }
            return new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        } else {
            throw new BadCredentialsException("smsCodeError");
        }

    }
}
