package com.thug;

import com.google.api.server.spi.config.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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

	private Map<String, String> stateAccessTokens;

	public TodoistProxyAPI() {

		stateAccessTokens = new HashMap<String, String>();

		try (InputStream input = getClass().getResourceAsStream("/credentials.properties")) {

			Properties prop = new Properties();
			prop.load(input);
			CLIENT_ID = prop.getProperty("CLIENT_ID");
			CLIENT_SECRET = prop.getProperty("CLIENT_SECRET");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@ApiMethod(path = "oauth-callback", httpMethod = ApiMethod.HttpMethod.GET)
	public Message oauthCallback(@Named("code") String code, @Named("state") String state) {

		Client client = Client.create();
		WebResource webResource = client.resource(todoistGetAccessTokenApi);

		TodoistGetAccessTokenRequest request = new TodoistGetAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, code);

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, request);

		// Status 200 is successful.
		if (response.getStatus() != 200) {
			// TODO throw exception
			// returning a success message
			Message msg = new Message();
			msg.setMessage("ko status=" + response.getStatus() + " error=" + response.getEntity(String.class));
			return msg;

		} else {

			TodoistGetAccessTokenResponse output = response.getEntity(TodoistGetAccessTokenResponse.class);

			// storing access token in memory, ready to be retrieved by extension
			stateAccessTokens.put(state, output.getAccessToken());

			// returning a success message
			Message msg = new Message();
			msg.setMessage("access token ready");
			return msg;
		}
	}

	@ApiMethod(path = "access-token/{state}", httpMethod = ApiMethod.HttpMethod.GET)
	public Message accessToken(@Named("state") String state) {

		if (stateAccessTokens.containsKey(state)) {

			String accessToken = stateAccessTokens.get(state);
			stateAccessTokens.remove(state);

			Message msg = new Message();
			msg.setMessage(accessToken);
			return msg;

		} else {

			Message msg = new Message();
			msg.setMessage("ko");
			return msg;
		}
	}

	@ApiMethod(path = "access-token/{token}", httpMethod = ApiMethod.HttpMethod.DELETE)
	public Message accessTokenRevoke(@Named("token") String token) {

		Client client = Client.create();
		WebResource webResource = client.resource(todoistRevokeAccessTokenApi);

		TodoistRevokeAccessTokenRequest request = new TodoistRevokeAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, token);

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, request);

		// Status 204 is successful.
		if (response.getStatus() != 204) {
			// TODO throw exception
			// returning a success message
			Message msg = new Message();
			msg.setMessage("ko status=" + response.getStatus() + " error=" + response.getEntity(String.class));
			return msg;
		}

		// returning a success message
		Message msg = new Message();
		msg.setMessage("access token revoked");
		return msg;
	}
}
