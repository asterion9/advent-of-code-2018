package fr.sma.adventofcode.resolve;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

@Service
public class DataFetcher {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${cookie}")
	private String cookie;
	
	public String fetch(int day) throws IOException, InterruptedException {
		String fileName = Hashing.sha256().newHasher().putInt(day).putUnencodedChars(cookie).hash().toString();
		
		File inputFile = Paths.get("target", "inputs", fileName).toFile();
		
		if(inputFile.exists()) {
			logger.info("loading input from cached file");
			return Files.asCharSource(inputFile, Charsets.UTF_8).read();
		} else {
			logger.info("loading input from url");
			inputFile.getParentFile().mkdirs();
		}
		
		HttpClient httpClient = HttpClient.newBuilder()
				.build();
		
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://adventofcode.com/2018/day/" + day + "/input"))
				.header("Cookie", cookie)
				.GET()
				.build();
		
		HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		
		Files.asCharSink(inputFile, Charsets.UTF_8).write(response.body());
		
		return response.body();
	}
}
