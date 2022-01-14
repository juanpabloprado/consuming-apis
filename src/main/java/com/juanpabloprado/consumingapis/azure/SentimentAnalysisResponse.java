package com.juanpabloprado.consumingapis.azure;

import java.util.List;

public record SentimentAnalysisResponse(List<TextDocumentScore> documents) {
    record TextDocumentScore(String id, String sentiment) {
    }
}
