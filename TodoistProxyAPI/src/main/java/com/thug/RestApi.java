package com.thug;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RestApi {

	private static final String todoistGetAccessTokenApi = "https://todoist.com/oauth/access_token";

	private static final String todoistRevokeAccessTokenApi = "https://api.todoist.com/sync/v8/access_tokens/revoke";

	private static String CLIENT_ID;

	private static String CLIENT_SECRET;

	private static void getAccessToken(String code) {

		Client client = Client.create();
		WebResource webResource = client.resource(todoistGetAccessTokenApi);

		TodoistGetAccessTokenRequest request = new TodoistGetAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, code);

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, request);

		// Status 200 is successful.
		if (response.getStatus() != 200) {
			System.out.println("Failed with HTTP Error code: " + response.getStatus());
			String error = response.getEntity(String.class);
			System.out.println("Error: " + error);
			return;
		}

		TodoistGetAccessTokenResponse output = response.getEntity(TodoistGetAccessTokenResponse.class);
		System.out.println("Access Token : " + output.getAccessToken());
		System.out.println("Token Type : " + output.getTokenType());
	}

	private static void revokeAccessToken(String token) {

		Client client = Client.create();
		WebResource webResource = client.resource(todoistRevokeAccessTokenApi);

		TodoistRevokeAccessTokenRequest request = new TodoistRevokeAccessTokenRequest(CLIENT_ID, CLIENT_SECRET, token);

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, request);

		// Status 204 is successful.
		if (response.getStatus() != 204) {
			System.out.println("Failed with HTTP Error code: " + response.getStatus());
			String error = response.getEntity(String.class);
			System.out.println("Error: " + error);
			return;
		} else {
			System.out.println("Token Revoked : " + token);
		}
	}

	public static void main(String[] args) {

		try (InputStream input = new FileInputStream("src/main/resources/credentials.properties")) {

			Properties prop = new Properties();
			prop.load(input);
			CLIENT_ID = prop.getProperty("CLIENT_ID");
			CLIENT_SECRET = prop.getProperty("CLIENT_SECRET");

			getAccessToken(prop.getProperty("TEMP_CODE"));
			// revokeAccessToken(prop.getProperty("TEMP_TOKEN"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
