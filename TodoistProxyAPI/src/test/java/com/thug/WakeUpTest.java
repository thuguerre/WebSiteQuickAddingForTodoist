package com.thug;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.thug.model.WakeUpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.logging.Logger;

public class WakeUpTest {

    private static final Logger LOGGER = Logger.getLogger(WakeUpTest.class.getName());

    @Test
    public void wakeup() throws InternalServerErrorException {

        TodoistProxyAPI api = new TodoistProxyAPI();
        WakeUpResponse response = api.wakeUp();

        Assert.assertNotNull("Wake Up Service's response must not be null", response);
        Assert.assertNotNull("Wake Up Service's response message must not be null", response.getMessage());
        Assert.assertEquals("Wake Up Service's response message is not the one expected", TodoistProxyAPI.WAKE_UP_RESPONSE, response.getMessage());
    }
}
