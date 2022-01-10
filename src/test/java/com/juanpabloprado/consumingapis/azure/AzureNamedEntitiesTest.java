package com.juanpabloprado.consumingapis.azure;

import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AzureNamedEntitiesTest {

    @Value("${AZURE_API_KEY}")
    private String azureApiKey;

    private static final String AZURE_ENDPOINT = "https://landon-hotel-feedback-jprado2.cognitiveservices.azure.com";

    private static final String AZURE_ENDPOINT_PATH = "/text/analytics/v3.0/entities/recognition/general";

    private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";

    private static final String EXAMPLE_JSON = """
            {
              "documents": [
                {
                  "language": "en",
                  "id": "1",
                  "text": "The Landon Hotel was found in 1952 London by Arthur Landon after World War II."
                }
              ]
            }
            """;

    @Test
    public void getEntities() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(API_KEY_HEADER_NAME, azureApiKey)
                .uri(URI.create(AZURE_ENDPOINT + AZURE_ENDPOINT_PATH))
                .POST(BodyPublishers.ofString(EXAMPLE_JSON))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }


}
