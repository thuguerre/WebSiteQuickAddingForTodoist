package com.thug;

import com.google.api.server.spi.response.InternalServerErrorException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CredentialsTest {

	private static final Logger LOGGER = Logger.getLogger(CredentialsTest.class.getName());

	@Rule
	public final EnvironmentVariables envVars = new EnvironmentVariables();

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

	@Test
	public void isTodoistClientIdAbsenceWellManaged() {

		envVars.clear(TodoistProxyAPI.TODOIST_CLIENT_ID_ENV_VAR_ID);

		try {

			new TodoistProxyAPI();
			Assert.fail("Should have thrown an exception.");

		} catch (InternalServerErrorException ex) {

			Assert.assertTrue("Not the right exception", ex.getMessage().contains("TODOIST_CLIENT_ID is not set."));
		}
	}

	@Test
	public void isTodoistClientSecretAbsenceWellManaged() {

		envVars.clear(TodoistProxyAPI.TODOIST_CLIENT_SECRET_ENV_VAR_ID);

		try {

			new TodoistProxyAPI();
			Assert.fail("Should have thrown an exception.");

		} catch (InternalServerErrorException ex) {

			Assert.assertTrue("Not the right exception", ex.getMessage().contains("TODOIST_CLIENT_SECRET is not set."));
		}
	}

	@Test
	public void isTodoistClientIdLenght0WellManaged() {

		envVars.set(TodoistProxyAPI.TODOIST_CLIENT_ID_ENV_VAR_ID, "");

		try {

			new TodoistProxyAPI();
			Assert.fail("Should have thrown an exception.");

		} catch (InternalServerErrorException ex) {

			Assert.assertTrue("Not the right exception", ex.getMessage().contains("TODOIST_CLIENT_ID is not set."));
		}
	}

	@Test
	public void isTodoistClientSecretLenght0WellManaged() {

		envVars.set(TodoistProxyAPI.TODOIST_CLIENT_SECRET_ENV_VAR_ID, "");

		try {

			new TodoistProxyAPI();
			Assert.fail("Should have thrown an exception.");

		} catch (InternalServerErrorException ex) {

			Assert.assertTrue("Not the right exception", ex.getMessage().contains("TODOIST_CLIENT_SECRET is not set."));
		}
	}
}
