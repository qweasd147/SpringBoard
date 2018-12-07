package com.joo.utils;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

public class CookieUtils {

    public static void setCookie(String key, String value, int maxAge){
        setCookie(key, value, "/", maxAge);
    }

    public static void setCookie(String key, String value, String path, int maxAge){
        Cookie cookie = new Cookie(key, value);

        cookie.setPath(path);
        cookie.setMaxAge(maxAge);

        HttpServletResponse resp = WebUtil.getResponse();

        resp.addCookie(cookie);
    }

    public static Cookie getCookie(String key){

        Cookie[] cookies = WebUtil.getRequest().getCookies();

        return Arrays.stream(cookies)
                .filter(Objects::nonNull)
                .filter(cookie -> key.equals(cookie.getName()))
                .findFirst()
                .orElse(null);
    }

    public static String getCookieVal(String key){
        return getCookie(key).getValue();
    }

    public static boolean removeCookie(String key){
        Cookie cookie = getCookie(key);

        if(cookie == null)  return false;
        HttpServletResponse resp = WebUtil.getResponse();

        cookie.setMaxAge(0);
        resp.addCookie(cookie);

        return true;
    }
}
