package com.joo.utils;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WebUtil {

    public static <T> void setSession(String key, T obj) {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        setSessionAttribute(req, key, obj);
    }

    public static <T> void setSessionAttribute(HttpServletRequest req, String key, T obj) {

        HttpSession session = getSession(req);

        if ( session.getAttribute(key) != null ){
            removeSessionAttribute(req, key);	// 기존값을 제거해 준다.
        }

        session.setAttribute(key, obj);
    }

    public static void removeSessionAttribute(String sessionKey) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        removeSessionAttribute(req, sessionKey);
    }

    public static void removeSessionAttribute(HttpServletRequest req, String sessionKey) {
        HttpSession session = getSession(req);

        if ( session.getAttribute(sessionKey) != null ){
            session.removeAttribute(sessionKey);	// 기존값을 제거해 준다.
        }
    }

    public static <T> T getSessionAttribute(String key) {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getSessionAttribute(req, key);
    }

    public static <T> T getSessionAttribute(HttpServletRequest req, String key) {

        HttpSession session = req.getSession();

        return (T) session.getAttribute(key);
    }

    public static <T> T getBean(String beanName) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getBean(req, beanName);
    }

    public static <T> T getBean(HttpServletRequest req, String beanName) {

        HttpSession session = req.getSession();
        ServletContext context = session.getServletContext();
        WebApplicationContext wContext = WebApplicationContextUtils.getWebApplicationContext(context);

        return (T) wContext.getBean(beanName);
    }

    public static HttpSession getSession() {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getSession(req);
    }

    public static String getSessionID(){
        return RequestContextHolder.getRequestAttributes().getSessionId();
    }

    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}