package com.popdeluxe.maxhogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HogMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(HogMgmtApplication.class, args);
	}

	//@Bean
	//public RestTemplate restTemplate(RestTemplateBuilder builder) {
	//	return builder.build();
	//}


	@Bean(name = "getMaxHogMembers")
	public RestTemplate collectCentRestTemplate(RestTemplateBuilder builder) {
		//return builder.rootUri("https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/members")
		return builder
				.additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
					request.getHeaders().add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjZkYjRmYTIyLTA5MmQtNGIzNC04MzFlLTI2YzQzZmQ3NjJjMCIsImlhdCI6MTYxMDY3MjI5MSwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI2Ny4xOTEuMjM4LjQzIl0sInR5cGUiOiJjbGllbnQifV19.kUvajlSm2cpb-A3k-sxNh7F3SRCqFFeVPXKcyioNkfPEEjGbgRr4VLOUJqSFT1iITuy3rg9kwiOaJBIS9z3tSg");
					return execution.execute(request, body);
				}).build();
	}



}
