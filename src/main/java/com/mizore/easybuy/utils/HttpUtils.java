package com.mizore.easybuy.utils;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@UtilityClass
public class HttpUtils {

    public HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    public HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    private ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
    }

}
