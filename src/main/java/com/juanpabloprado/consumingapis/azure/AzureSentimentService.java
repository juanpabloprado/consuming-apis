package com.juanpabloprado.consumingapis.azure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class AzureSentimentService {

	@Value("${AZURE_API_KEY}")
	private String azureApiKey;
		
	private final ObjectMapper mapper;
	
	private static final String AZURE_ENDPOINT = "https://landon-hotel-feedback-jprado2.cognitiveservices.azure.com";

	private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";


	public AzureSentimentService(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public SentimentAnalysis requestSentimentAnalysis(String text, String language) throws IOException, InterruptedException {

		TextDocument document = new TextDocument("1",text,language);
		TextAnalyticsRequest requestBody = new TextAnalyticsRequest(new ArrayList<>());
		requestBody.documents().add(document);
		
		String endpoint = AZURE_ENDPOINT + "/text/analytics/v3.0/sentiment";

		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_2)
				.proxy(ProxySelector.getDefault())
				.connectTimeout(Duration.ofSeconds(5))
				.build();
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endpoint))
				.header(API_KEY_HEADER_NAME, azureApiKey)
				.header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
				.POST(BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
				.timeout(Duration.ofSeconds(5))
				.build();
		
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		
		if(response.statusCode() != 200) {
			System.out.println(response.body());
			throw new RuntimeException("An issue occurred making the API call");
		}
	
		String sentimentValue = this.mapper
				.readValue(response.body(), SentimentAnalysisResponse.class)
				.documents()
				.get(0)
				.sentiment();

		return new SentimentAnalysis(document, sentimentValue);

	}
}
