package Auth;

public class Tokens {
	
	private String access_token;
	private String id_token;
	
	Tokens (String access_token, String id_token) {
		this.access_token = access_token;
		this.id_token = id_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getId_token() {
		return id_token;
	}

	public void setId_token(String id_token) {
		this.id_token = id_token;
	}
	
}