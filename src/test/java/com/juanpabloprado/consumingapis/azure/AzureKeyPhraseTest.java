package com.juanpabloprado.consumingapis.azure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AzureKeyPhraseTest {

    @Value("${AZURE_API_KEY}")
    private String azureApiKey;

    private static final String AZURE_ENDPOINT = "https://landon-hotel-feedback-jprado2.cognitiveservices.azure.com";

    private static final String AZURE_ENDPOINT_PATH = "/text/analytics/v3.0/keyPhrases";

    private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";

    private static final String EXAMPLE_JSON = """
            {
              "documents": [
                {
                  "language": "en",
                  "id": "1",
                  "text": "In an e360 interview, Carlos Nobre, Brazil’s leading expert on the Amazon and climate change, discusses the key perils facing the world’s largest rainforest, where a record number of fires are now raging, and lays out what can be done to stave off a ruinous transformation of the region."
                }
              ]
            }
            """;

    private static final String textForAnalysis = "In an e360 interview, Carlos Nobre, Brazil’s leading expert on the Amazon and climate change, discusses the key perils facing the world’s largest rainforest, where a record number of fires are now raging, and lays out what can be done to stave off a ruinous transformation of the region.";

    @Autowired
    public ObjectMapper mapper;

    @Test
    public void getKeyPhrases() throws IOException, InterruptedException {

        TextDocument document = new TextDocument("1", textForAnalysis, ENGLISH.getLanguage());
        TextAnalyticsRequest requestBody = new TextAnalyticsRequest(new ArrayList<>());
        requestBody.documents().add(document);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AZURE_ENDPOINT + AZURE_ENDPOINT_PATH))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(API_KEY_HEADER_NAME, this.azureApiKey)
                .POST(BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        System.out.println(response.body());

    }


}
