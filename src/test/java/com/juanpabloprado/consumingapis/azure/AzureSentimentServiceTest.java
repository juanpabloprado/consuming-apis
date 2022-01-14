package com.juanpabloprado.consumingapis.azure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AzureSentimentServiceTest {

    @Autowired
    private AzureSentimentService sentimentService;

    @Test
    void testPositiveSentiment() throws IOException, InterruptedException {

        SentimentAnalysis analysis = sentimentService.requestSentimentAnalysis("I love wild animals and their habitat!", ENGLISH.getLanguage());
        assertNotNull(analysis);
        assertEquals("positive", analysis.sentiment());
    }

    @Test
    void testNegativeSentiment() throws IOException, InterruptedException {

        SentimentAnalysis analysis = sentimentService.requestSentimentAnalysis("Pollution is horrible, we need to stop it!", ENGLISH.getLanguage());
        assertNotNull(analysis);
        assertEquals("negative", analysis.sentiment());
    }
}
