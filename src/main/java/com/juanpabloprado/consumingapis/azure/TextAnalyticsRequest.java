package com.juanpabloprado.consumingapis.azure;

import java.util.List;

public record TextAnalyticsRequest(List<TextDocument> documents) {
}
