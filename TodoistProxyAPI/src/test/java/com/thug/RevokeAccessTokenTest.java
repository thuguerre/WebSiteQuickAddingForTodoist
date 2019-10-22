package com.thug;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.thug.model.GoneException;
import com.thug.model.RevokeAccessTokenRequest;
import org.junit.Test;

import java.util.logging.Logger;

public class RevokeAccessTokenTest {

    private static final Logger LOGGER = Logger.getLogger(RevokeAccessTokenTest.class.getName());

    @Test(expected = BadRequestException.class)
    public void nullRequest() throws InternalServerErrorException, ConflictException, BadRequestException, GoneException {

        TodoistProxyAPI api = new TodoistProxyAPI();
        api.accessTokenRevoke(null);
    }


    @Test(expected = BadRequestException.class)
    public void nullAccessToken() throws InternalServerErrorException, ConflictException, BadRequestException, GoneException {

        TodoistProxyAPI api = new TodoistProxyAPI();

        RevokeAccessTokenRequest request = new RevokeAccessTokenRequest();
        request.setAccessToken(null);

        api.accessTokenRevoke(request);
    }

    @Test(expected = GoneException.class)
    public void wrongAccessToken() throws InternalServerErrorException, ConflictException, BadRequestException, GoneException {

        TodoistProxyAPI api = new TodoistProxyAPI();

        RevokeAccessTokenRequest request = new RevokeAccessTokenRequest();
        request.setAccessToken("wrong-access-token");

        api.accessTokenRevoke(request);
    }
}
