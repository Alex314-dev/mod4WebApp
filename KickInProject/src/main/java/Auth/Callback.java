package Auth;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Callback {
	
	//env variables would be nice
	private static String AuthuUrl = "https://dev-63893740.okta.com/oauth2/default/v1/token";
	private static String CLIENT_ID = "0oaq4qsacgNHrCPaS5d6";
	private static String CLIENT_SECRET = "pxvseJYa-YBFLoznWXfMn-3HMsgiWeEZwW6qX_o3";
	private static String post_uri_path = "auth";
	
	
	public String retriveTokens(String code, String host) throws IOException, InterruptedException {
		
		String encodedAuth = "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID+":"+CLIENT_SECRET).getBytes());
		
		String requestBody = "grant_type="+"authorization_code"
							+"&redirect_uri="+host+post_uri_path
							+"&code="+code;
        
		System.out.println(host+post_uri_path);
		
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        		.header("Content-Type", "application/x-www-form-urlencoded")
        		.header("Authorization", encodedAuth)
                .uri(URI.create(AuthuUrl))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        
        System.out.println(response.statusCode());
        
        if (response.statusCode() >= 400) {
            System.out.println(response.body());
        	return "error";
        	
        }
        
        String responseBodyJson = response.body();
        
        
        return responseBodyJson;
	}
	
}
