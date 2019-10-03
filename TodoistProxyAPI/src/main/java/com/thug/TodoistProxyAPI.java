package com.thug;

import com.google.api.server.spi.config.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.thug.todoist.TodoistGetAccessTokenRequest;
import com.thug.todoist.TodoistGetAccessTokenResponse;
import com.thug.todoist.TodoistRevokeAccessTokenRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Api(name = "todoistProxyAPI", version = "v1", namespace = @ApiNamespace(ownerDomain = "todoistquickwebsiteadd.appspot.com", ownerName = "todoistquickwebsiteadd.appspot.com", packagePath = ""), issuers = {
		@ApiIssuer(name = "firebase", issuer = "https://securetoken.google.com/todoistquickwebsiteadd", jwksUri = "https://www.googleapis.com/service_accounts/v1/metadata/x509/securetoken@system.gserviceaccount.com") })
public class TodoistProxyAPI {

	private static final String todoistGetAccessTokenApi = "https://todoist.com/oauth/access_token";

	private static final String todoistRevokeAccessTokenApi = "https://api.todoist.com/sync/v8/access_tokens/revoke";

	private static String CLIENT_ID;

	private static String CLIENT_SECRET;

	public TodoistProxyAPI() {

		try (InputStream input = getClass().getResourceAsStream("/credentials.properties")) {

			Properties prop = new Properties();
			prop.load(input);
			CLIENT_ID = prop.getProperty("CLIENT_ID");
			CLIENT_SECRET = prop.getProperty("CLIENT_SECRET");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@ApiMethod(path = "access-token", httpMethod = ApiMethod.HttpMethod.POST)
	public AccessTokenResponse accessToken(AccessTokenRequest request) {

		Client client = Client.create();
		WebResource webResource = client.resource(todoistGetAccessTokenApi);

		TodoistGetAccessTokenRequest todoistRequest = new TodoistGetAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, request.getCode());

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, todoistRequest);
		TodoistGetAccessTokenResponse output = response.getEntity(TodoistGetAccessTokenResponse.class);

		// returning a success message
		AccessTokenResponse msg = new AccessTokenResponse();
		msg.setAccessToken(output.getAccessToken());
		return msg;
	}

	@ApiMethod(path = "access-token/{token}", httpMethod = ApiMethod.HttpMethod.DELETE)
	public AccessTokenResponse accessTokenRevoke(@Named("token") String token) {

		Client client = Client.create();
		WebResource webResource = client.resource(todoistRevokeAccessTokenApi);

		TodoistRevokeAccessTokenRequest request = new TodoistRevokeAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, token);

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, request);

		// Status 204 is successful.
		if (response.getStatus() != 204) {
			// TODO throw exception
			// returning a success message
			AccessTokenResponse msg = new AccessTokenResponse();
			msg.setAccessToken("ko status=" + response.getStatus() + " error=" + response.getEntity(String.class));
			return msg;
		}

		// returning a success message
		AccessTokenResponse msg = new AccessTokenResponse();
		msg.setAccessToken("access token revoked");
		return msg;
	}
}
