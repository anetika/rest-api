package com.epam.esm.matcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class AllEntityReadRequestMatcher implements RequestMatcher {

    private static final String GET_METHOD = "GET";
    private static final String READ_CERTIFICATES_ENDPOINT = "/certificates/certificates";
    private static final String READ_TAGS_ENDPOINT = "/tags";
    private static final String READ_ORDERS_ENDPOINT = "/orders";
    private static final String READ_USERS_ENDPOINT = "/users";

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if (method != null && method.equals(GET_METHOD)) {
            return requestURI.startsWith(READ_CERTIFICATES_ENDPOINT)
                    || requestURI.startsWith(READ_TAGS_ENDPOINT)
                    || requestURI.startsWith(READ_ORDERS_ENDPOINT)
                    || requestURI.startsWith(READ_USERS_ENDPOINT);
        }
        return false;
    }
}
