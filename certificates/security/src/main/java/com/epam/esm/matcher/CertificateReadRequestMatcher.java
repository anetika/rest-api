package com.epam.esm.matcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class CertificateReadRequestMatcher implements RequestMatcher {

    private static final String GET_METHOD = "GET";
    private static final String READ_CERTIFICATES_ENDPOINT = "/certificates/certificates";

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if (method != null && method.equals(GET_METHOD)) {
            return requestURI.startsWith(READ_CERTIFICATES_ENDPOINT);
        }
        return false;
    }
}
