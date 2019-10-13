package com.thug;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

public class CredentialsTest {

	@Test
	public void isCredentialsPropertiesFilePresent() throws IOException {

		Assert.assertNotNull("Credentials.properties file does not exist.", getClass().getResourceAsStream("/credentials.properties"));
	}

	@Test
	public void areCredentialsPropertiesPresent() throws IOException {

		InputStream input = getClass().getResourceAsStream("/credentials.properties");

		Properties prop = new Properties();
		prop.load(input);

		String clientId = prop.getProperty(TodoistProxyAPI.TODOIST_CLIENT_ID_PROPERTY_ID);
		String clientSecret = prop.getProperty(TodoistProxyAPI.TODOIST_CLIENT_SECRET_PROPERTY_ID);

		Pattern credentialsPattern = Pattern.compile("^[0-9a-f]{32}$");

		Assert.assertFalse("Todoist Client ID property is not found", clientId == null);
		Assert.assertFalse("Todoist Client ID property is not found", clientId.length() == 0);
		Assert.assertTrue("Client ID does not match pattern", credentialsPattern.matcher(clientId).matches());

		Assert.assertFalse("Todoist Client Secret property is not found", clientSecret == null);
		Assert.assertFalse("Todoist Client Secret property is not found", clientSecret.length() == 0);
		Assert.assertTrue("Client Secret does not match pattern", credentialsPattern.matcher(clientSecret).matches());
	}
}
