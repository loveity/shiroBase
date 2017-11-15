package com.test.shiro.filter;

import org.apache.shiro.authc.*;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Service
public class MyFormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
    public static final String DEFAULT_MESSAGE_PARAM = "message";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
    private String messageParam = DEFAULT_MESSAGE_PARAM;


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        String username = getUsername(request);
        String password = getPassword(request);
        boolean rememberMe = isRememberMe(request);

//        String host = StringUtils.getRemoteAddr((HttpServletRequest) request);
        String host = "";
        boolean mobile = isMobileLogin(request);
        return this.createToken(username, password, request, response);
        //end
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    public String getMobileLoginParam() {
        return mobileLoginParam;
    }

    protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }

    public String getMessageParam() {
        return messageParam;
    }

    /**
     * 登录成功之后跳转URL
     */
    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request,
                                        ServletResponse response) throws Exception {
//        Principal p = UserUtils.getPrincipal();
//        if (p != null && !p.isMobileLogin()){
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
//        }else{
//            super.issueSuccessRedirect(request, response);
//        }
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName(), message = "";
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)) {
            message = "用户或密码错误, 请重试.";
        } else if (e.getMessage() != null && org.apache.commons.lang3.StringUtils.startsWith(e.getMessage(), "msg:")) {
            message = org.apache.commons.lang3.StringUtils.replace(e.getMessage(), "msg:", "");
        } else {
            message = "系统出现点问题，请稍后再试！";
            e.printStackTrace(); // 输出到控制台
        }
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
    }

}