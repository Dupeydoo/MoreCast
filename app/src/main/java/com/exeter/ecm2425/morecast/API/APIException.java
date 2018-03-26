package com.exeter.ecm2425.morecast.API;

/**
 * An Exception class used by the APIService and classes
 * relating to encapsulate the failure of the API.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class APIException extends Exception {

    /**
     * Constructor allowing a message to be supplied.
     * @param message The message for the APIException.
     */
    public APIException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }
}
