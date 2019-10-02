package com.thug;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class TodoistRevokeAccessTokenRequest {

	private String client_id;

	private String client_secret;

	private String access_token;

	public TodoistRevokeAccessTokenRequest() {
	}

	public TodoistRevokeAccessTokenRequest(String clientId, String clientSecret, String token) {
		this.client_id = clientId;
		this.client_secret = clientSecret;
		this.access_token = token;
	}

	public String getClientId() {
		return client_id;
	}

	public void setClientId(String clientId) {
		this.client_id = clientId;
	}

	public String getClientSecret() {
		return client_secret;
	}

	public void setClientSecret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getAccessToken() {
		return access_token;
	}

	public void setAccessToken(String code) {
		this.access_token = code;
	}

}
