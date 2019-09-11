package teste.treports;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

		resource.setAccessTokenUri("http://treports.localhost:5009/totvs.rac/connect/token");
		resource.setClientId("treports_ro");
		resource.setClientSecret("totvs@123");
		//resource.setGrantType("password");
		resource.setScope(List.of("authorization_api"));

		resource.setUsername("admin");
		resource.setPassword("totvs@123");

		AccessTokenRequest atr = new DefaultAccessTokenRequest();

		OAuth2RestOperations restTemplateA = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(atr));

		OAuth2AccessToken token = restTemplateA.getAccessToken();

		System.out.println(token.getValue());
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token.getValue());
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		HttpEntity<String> request = new HttpEntity<>("{\n" + 
				"	\"GenerateParams\": {\n" + 
				"		\"IsView\": false,\n" + 
				"		\"StopExecutionOnError\": true,\n" + 
				"		\"Parameters\": []\n" + 
				"	},\n" + 
				"	\"ScheduleParams\": {\n" + 
				"		\"Type\": 0\n" + 
				"	},\n" + 
				"	\"User\": \"admin\"\n" + 
				"}", headers);
		
		ResponseEntity<ReportRequest> response = restTemplate.postForEntity("http://localhost:7017/api/trep/v1/reports/1a73b4c7-bd01-46d2-9992-c6b196e8bbae/execute", request,
				ReportRequest.class);
		
		System.out.println(response.getStatusCodeValue());
		System.out.println(response.getBody().uIdRequest);
		System.out.println(response.getBody().scheduleDate);
	}
}
