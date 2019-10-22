package com.thug;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.thug.model.GetConfigurationResponse;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class GetConfigurationTest {

    private static final Logger LOGGER = Logger.getLogger(GetConfigurationTest.class.getName());

    @Rule
    public final EnvironmentVariables envVars = new EnvironmentVariables();

    @Test
    public void getConfiguration() throws InternalServerErrorException {

        TodoistProxyAPI api = new TodoistProxyAPI();

        GetConfigurationResponse response = api.getConfiguration();

        Pattern credentialsPattern = Pattern.compile("^[0-9a-f]{32}$");

        Assert.assertNotNull(response);
        Assert.assertFalse("Todoist Client ID property is not found", response.getClientId() == null);
        Assert.assertFalse("Todoist Client ID property is not set", response.getClientId().length() == 0);
        Assert.assertTrue("Todoist Client ID does not match pattern", credentialsPattern.matcher(response.getClientId()).matches());
    }

    @Test
    public void getClientIdWithBadEnvVar() throws InternalServerErrorException {

        TodoistProxyAPI api = new TodoistProxyAPI();
        api.setClientId(null);

        try {

            GetConfigurationResponse response = api.getConfiguration();
            Assert.fail("Should have thrown an exception.");

        } catch (InternalServerErrorException ex) {

            Assert.assertEquals("Environment Variable TODOIST_CLIENT_ID is not set.", ex.getMessage());
        }
    }
}
