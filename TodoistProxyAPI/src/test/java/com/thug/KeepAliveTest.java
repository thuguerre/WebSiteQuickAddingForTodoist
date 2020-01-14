package com.thug;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.thug.model.KeepAliveResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.logging.Logger;

public class KeepAliveTest {

    private static final Logger LOGGER = Logger.getLogger(KeepAliveTest.class.getName());

    @Test
    public void keepAlive() throws InternalServerErrorException {

        TodoistProxyAPI api = new TodoistProxyAPI();
        KeepAliveResponse response = api.keepAlive();

        Assert.assertNotNull("Keep Alive Service's response must not be null", response);
        Assert.assertNotNull("Keep Alive Service's response message must not be null", response.getMessage());
        Assert.assertEquals("Keep Alive Service's response message is not the one expected", TodoistProxyAPI.KEEP_ALIVE_RESPONSE, response.getMessage());
    }
}
