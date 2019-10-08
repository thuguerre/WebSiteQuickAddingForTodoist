package com.thug;

import com.google.api.server.spi.config.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.thug.model.GetAccessTokenRequest;
import com.thug.model.GetAccessTokenResponse;
import com.thug.model.RevokeAccessTokenRequest;
import com.thug.todoist.TodoistGetAccessTokenRequest;
import com.thug.todoist.TodoistGetAccessTokenResponse;
import com.thug.todoist.TodoistRevokeAccessTokenRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Api(name = "todoistProxyAPI", version = "v1", namespace = @ApiNamespace(ownerDomain = "websitequickadding4todoisttest.appspot.com", ownerName = "websitequickadding4todoisttest.appspot.com", packagePath = ""), issuers = {
		@ApiIssuer(name = "firebase", issuer = "https://securetoken.google.com/websitequickadding4todoisttest", jwksUri = "https://www.googleapis.com/service_accounts/v1/metadata/x509/securetoken@system.gserviceaccount.com") })
public class TodoistProxyAPI {

	private static final String TODOIST_GET_ACCESS_TOKEN_API = "https://todoist.com/oauth/access_token";

	private static final String TODOIST_REVOKE_ACCESS_TOKEN_API = "https://api.todoist.com/sync/v8/access_tokens/revoke";

	private static String CLIENT_ID;

	private static String CLIENT_SECRET;

	public TodoistProxyAPI() {

		try (InputStream input = getClass().getResourceAsStream("/credentials.properties")) {

			Properties prop = new Properties();
			prop.load(input);
			CLIENT_ID = prop.getProperty("TODOIST_CLIENT_ID");
			CLIENT_SECRET = prop.getProperty("TODOIST_CLIENT_SECRET");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@ApiMethod(path = "access-token", httpMethod = ApiMethod.HttpMethod.POST)
	public GetAccessTokenResponse accessToken(GetAccessTokenRequest request) {

		Client client = Client.create();
		WebResource webResource = client.resource(TODOIST_GET_ACCESS_TOKEN_API);

		TodoistGetAccessTokenRequest todoistRequest = new TodoistGetAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, request.getCode());

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, todoistRequest);
		TodoistGetAccessTokenResponse output = response.getEntity(TodoistGetAccessTokenResponse.class);

		// returning a success message
		GetAccessTokenResponse msg = new GetAccessTokenResponse();
		msg.setAccessToken(output.getAccessToken());
		return msg;
	}

	@ApiMethod(path = "access-token", httpMethod = ApiMethod.HttpMethod.DELETE)
	public GetAccessTokenResponse accessTokenRevoke(RevokeAccessTokenRequest request) {

		Client client = Client.create();
		WebResource webResource = client.resource(TODOIST_REVOKE_ACCESS_TOKEN_API);

		TodoistRevokeAccessTokenRequest todoistRequest = new TodoistRevokeAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, request.getAccessToken());

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, todoistRequest);

		// Status 204 is successful.
		if (response.getStatus() != 204) {
			// TODO throw exception
			// returning a success message
			GetAccessTokenResponse msg = new GetAccessTokenResponse();
			msg.setAccessToken("ko status=" + response.getStatus() + " error=" + response.getEntity(String.class));
			return msg;
		}

		// returning a success message
		GetAccessTokenResponse msg = new GetAccessTokenResponse();
		msg.setAccessToken("access token revoked");
		return msg;
	}
}
