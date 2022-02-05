package com.epam.esm.matcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class OrderCreateRequestMatcher implements RequestMatcher {

    private static final String POST_METHOD = "POST";
    private static final String CREATE_ORDER_ENDPOINT = "/orders";

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if (method != null && method.equals(POST_METHOD)) {
            return requestURI.startsWith(CREATE_ORDER_ENDPOINT);
        }
        return false;
    }
}
