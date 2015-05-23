package com.epam.it_company.domain;

/**
 * Contain parameter placed in previous request that came to server.
 */
public class PreviousRequestParameter {
    private String parameterName;
    private Object parameter;

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
