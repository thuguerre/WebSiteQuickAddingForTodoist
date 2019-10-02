package com.thug;

import com.google.api.server.spi.config.*;

import java.util.HashMap;
import java.util.Map;

@Api(name = "todoistProxyAPI", version = "v1", namespace = @ApiNamespace(ownerDomain = "todoistquickwebsiteadd.appspot.com", ownerName = "todoistquickwebsiteadd.appspot.com", packagePath = ""), issuers = {
		@ApiIssuer(name = "firebase", issuer = "https://securetoken.google.com/todoistquickwebsiteadd", jwksUri = "https://www.googleapis.com/service_accounts/v1/metadata/x509/securetoken@system.gserviceaccount.com") })
public class TodoistProxyAPI {

	private Map<String, String> stateCodes = new HashMap<String, String>();

	@ApiMethod(path = "oauth-callback", httpMethod = ApiMethod.HttpMethod.GET)
	public Message oauthCallback(@Named("code") String code, @Named("state") String state) {
		stateCodes.put(state, code);

		Message msg = new Message();
		msg.setMessage("ok");
		return msg;
	}

	@ApiMethod(path = "access-token/{state}", httpMethod = ApiMethod.HttpMethod.GET)
	public Message accessToken(@Named("state") String state) {

		if (stateCodes.containsKey(state)) {

			String code = stateCodes.get(state);
			stateCodes.remove(state);

			Message msg = new Message();
			msg.setMessage(code);
			return msg;

		} else {

			Message msg = new Message();
			msg.setMessage("ko");
			return msg;
		}
	}
}
