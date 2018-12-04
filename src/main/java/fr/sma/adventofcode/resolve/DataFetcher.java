package fr.sma.adventofcode.resolve;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DataFetcher {
	
	@Value("${cookie}")
	private String cookie;
	
	public String fetch(int day) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder()
				.build();
		
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://adventofcode.com/2018/day/" + day + "/input"))
				.header("Cookie", cookie)
				.GET()
				.build();
		
		HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		
		return response.body();
	}
}
