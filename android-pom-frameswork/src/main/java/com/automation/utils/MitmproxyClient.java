package com.automation.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MitmproxyClient {

    private static final String MITM_API = ConfigReader.get("mitmproxyApi"); 
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Polls mitmproxy API for the latest response body containing a keyword.
     *
     * @param keyword        the substring we expect (e.g. "Incorrect password")
     * @param timeoutSeconds how long to wait before giving up
     * @return the response content if found, else null
     */
    public static String waitForMessage(String keyword, int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < endTime) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(MITM_API + "/flows"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode root = mapper.readTree(response.body());

                JsonNode flows = root.path("flows");
                if (flows.isArray()) {
                    for (int i = flows.size() - 1; i >= 0; i--) { // newest first
                        JsonNode flow = flows.get(i);
                        String content = flow.path("response").path("content").path("text").asText();
                        if (content != null && content.contains(keyword)) {
                            return content;
                        }
                    }
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            // short wait before retry
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }

        return null; // not found within timeout
    }
}
