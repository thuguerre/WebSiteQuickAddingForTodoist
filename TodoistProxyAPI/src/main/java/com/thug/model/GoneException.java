package com.thug.model;

import com.google.api.server.spi.ServiceException;

public class GoneException extends ServiceException {
    public GoneException(String message) {
        super(410, message);
    }
}
