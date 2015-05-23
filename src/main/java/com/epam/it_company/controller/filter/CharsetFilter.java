package com.epam.it_company.controller.filter;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class CharsetFilter implements Filter {
    private final Logger logger = LogManager.getLogger(CharsetFilter.class);

    private String encoding;
    private static final String CHARSET_INIT_PARAMETER =
            "characterEncoding";

    public void destroy() {
        logger.info("Destroying charset filter");
    }
    public void doFilter(final ServletRequest request,final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }
    public void init(FilterConfig fConfig) throws ServletException {
        encoding = fConfig.getInitParameter(CHARSET_INIT_PARAMETER);
    }
}
