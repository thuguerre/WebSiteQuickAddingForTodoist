package com.thug;

import com.google.api.server.spi.response.InternalServerErrorException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CredentialsTest {

	private static final Logger LOGGER = Logger.getLogger(CredentialsTest.class.getName());

	@Test
	public void areCredentialsLoaded() throws InternalServerErrorException {

		TodoistProxyAPI api = new TodoistProxyAPI();

		String clientId = api.getClientId();
		String clientSecret = api.getClientSecret();

		Pattern credentialsPattern = Pattern.compile("^[0-9a-f]{32}$");

		Assert.assertFalse("Todoist Client ID property is not found", clientId == null);
		Assert.assertFalse("Todoist Client ID property is not set", clientId.length() == 0);
		Assert.assertTrue("Todoist Client ID does not match pattern", credentialsPattern.matcher(clientId).matches());

		Assert.assertFalse("Todoist Client Secret property is not found", clientSecret == null);
		Assert.assertFalse("Todoist Client Secret property is not set", clientSecret.length() == 0);
		Assert.assertTrue("Todoist Client Secret does not match pattern", credentialsPattern.matcher(clientSecret).matches());
	}

	@Ignore("not able to modiy env var for the moment")
	@Test(expected = InternalServerErrorException.class)
	public void isTodoistClientIdAbsenceWellManaged() throws InternalServerErrorException {

		System.getenv().put(TodoistProxyAPI.TODOIST_CLIENT_ID_ENV_VAR_ID, "");
		new TodoistProxyAPI();
	}
}
