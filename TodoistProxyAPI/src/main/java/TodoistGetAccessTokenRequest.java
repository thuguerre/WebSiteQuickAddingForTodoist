import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class TodoistGetAccessTokenRequest {

	private String client_id;

	private String client_secret;

	private String code;

	private String redirect_uri;

	public TodoistGetAccessTokenRequest() {
	}

	public TodoistGetAccessTokenRequest(String clientId, String clientSecret, String code) {
		this.client_id = clientId;
		this.client_secret = clientSecret;
		this.code = code;
	}

	public TodoistGetAccessTokenRequest(String clientId, String clientSecret, String code, String redirectURI) {
		this.client_id = clientId;
		this.client_secret = clientSecret;
		this.code = code;
		this.redirect_uri = redirectURI;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRedirectURI() {
		return redirect_uri;
	}

	public void setRedirectURI(String redirectURI) {
		this.redirect_uri = redirectURI;
	}
}
