package com.github.konstantin.suspitsyn.ecommercebackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class StaticServices {

    public static final String APPLICATION_JSON = "application/json";

    public static void mapResponse(HttpServletResponse httpServletResponse, Map<String, String> map) throws IOException {
        httpServletResponse.setContentType(APPLICATION_JSON);
        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), map);
    }

}
