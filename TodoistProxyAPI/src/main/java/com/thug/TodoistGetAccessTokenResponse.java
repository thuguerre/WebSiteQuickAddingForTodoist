package com.thug;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class TodoistGetAccessTokenResponse {

	private String access_token;

	private String token_type;

	public TodoistGetAccessTokenResponse() {
	}

	public TodoistGetAccessTokenResponse(String access_token, String bearer) {
		this.access_token = access_token;
		this.token_type = bearer;
	}

	public String getAccessToken() {
		return access_token;
	}

	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}

	public String getTokenType() {
		return token_type;
	}

	public void setTokenType(String token_type) {
		this.token_type = token_type;
	}
}
