package com.thug;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.ConflictException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.thug.model.GetAccessTokenRequest;
import com.thug.model.GetAccessTokenResponse;
import com.thug.model.GoneException;
import com.thug.model.RevokeAccessTokenRequest;
import com.thug.todoist.TodoistGetAccessTokenRequest;
import com.thug.todoist.TodoistGetAccessTokenResponse;
import com.thug.todoist.TodoistRevokeAccessTokenRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

@Api(name = "todoistProxyAPI", version = "v1", namespace = @ApiNamespace(ownerDomain = "${endpoints.project.id}.appspot.com", ownerName = "${endpoints.project.id}.appspot.com", packagePath = ""), issuers = {
        @ApiIssuer(name = "firebase", issuer = "https://securetoken.google.com/${endpoints.project.id}", jwksUri = "https://www.googleapis.com/service_accounts/v1/metadata/x509/securetoken@system.gserviceaccount.com")})
public class TodoistProxyAPI {

    private static final String TODOIST_GET_ACCESS_TOKEN_API = "https://todoist.com/oauth/access_token";

    private static final String TODOIST_REVOKE_ACCESS_TOKEN_API = "https://api.todoist.com/sync/v8/access_tokens/revoke";

    private static final Logger LOGGER = Logger.getLogger(TodoistProxyAPI.class.getName());

    private String clientId;

    private String clientSecret;

    public TodoistProxyAPI() {
        loadConfiguration();
    }

    private void loadConfiguration() {

        if (clientId == null || clientSecret == null) {

            try (InputStream input = getClass().getResourceAsStream("/credentials.properties")) {

                Properties prop = new Properties();
                prop.load(input);
                clientId = prop.getProperty("TODOIST_CLIENT_ID");
                clientSecret = prop.getProperty("TODOIST_CLIENT_SECRET");

                LOGGER.info("configuration loaded.");

            } catch (IOException ex) {

                LOGGER.severe("configuration loading error.");
                ex.printStackTrace();
            }

        } else {

            LOGGER.info("configuration already loaded. Do nothing.");
        }
    }

    @ApiMethod(path = "access-token", httpMethod = ApiMethod.HttpMethod.POST)
    public GetAccessTokenResponse accessToken(GetAccessTokenRequest request) throws BadRequestException {

        loadConfiguration();

        /**** Parameters verification ****/

        if (request.getCode() == null) {
            throw new BadRequestException("code cannot be null");
        }

        if (request.getState() == null) {
            throw new BadRequestException("state cannot be null");
        }

        /**** Payload ****/

        Client client = Client.create();
        WebResource webResource = client.resource(TODOIST_GET_ACCESS_TOKEN_API);

        TodoistGetAccessTokenRequest todoistRequest = new TodoistGetAccessTokenRequest(clientId, this.clientSecret, request.getCode());

        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, todoistRequest);
        TodoistGetAccessTokenResponse output = response.getEntity(TodoistGetAccessTokenResponse.class);

        // returning a success message
        GetAccessTokenResponse msg = new GetAccessTokenResponse();
        msg.setAccessToken(output.getAccessToken());
        return msg;
    }

    @ApiMethod(path = "access-token", httpMethod = ApiMethod.HttpMethod.DELETE)
    public GetAccessTokenResponse accessTokenRevoke(RevokeAccessTokenRequest request) throws BadRequestException, ConflictException, GoneException {

        loadConfiguration();

        /**** Parameters verification ****/

        if (request.getAccessToken() == null) {
            throw new BadRequestException("token cannot be null");
        }

        /**** Payload ****/

        Client client = Client.create();
        WebResource webResource = client.resource(TODOIST_REVOKE_ACCESS_TOKEN_API);

        TodoistRevokeAccessTokenRequest todoistRequest = new TodoistRevokeAccessTokenRequest(clientId, this.clientSecret, request.getAccessToken());

        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, todoistRequest);

        if (response.getStatus() == 204) {

            // In Todoist documentation, status 204 is successful.
            GetAccessTokenResponse msg = new GetAccessTokenResponse();
            msg.setAccessToken("access token revoked");
            return msg;

        } else if (response.getStatus() == 400) {

            // Todoist has sent a 400=unauthorized exception. The access token is unknown from Todoist
            throw new GoneException("access token has expired or is wrong.");

        } else {

            // we do not know what happened
            throw new ConflictException("an error occurred : " + response.getEntity(String.class));
        }
    }
}
