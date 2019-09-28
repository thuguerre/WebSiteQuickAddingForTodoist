import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestApi {

	private static final String todoistGetAccessTokenApi = "https://todoist.com/oauth/access_token";

	private static final String todoistRevokeAccessTokenApi = "https://api.todoist.com/sync/v8/access_tokens/revoke";

	private static final String CLIENT_ID = "51ba8ae54b9146be839bd0561002f081";

	// the true value of our application OAUTH Client Secret must not be committed publicly
	private static final String CLIENT_SECRET = "NOT_TO_COMMIT";

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
		getAccessToken("9a4d424dceb2112a28a784e673d998596612a6c2");
		//revokeAccessToken("NOT_TO_COMMIT");
	}
}
