package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.checkcapture.requests.CheckCaptureRequestBody;
import io.github.payabli.api.resources.checkcapture.types.CheckCaptureResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckCaptureWireTest {
    private MockWebServer server;
    private PayabliApiClient client;
    private ObjectMapper objectMapper = ObjectMappers.JSON_MAPPER;

    @BeforeEach
    public void setup() throws Exception {
        server = new MockWebServer();
        server.start();
        client = PayabliApiClient.builder()
                .url(server.url("/").toString())
                .apiKey("test-api-key")
                .build();
    }

    @AfterEach
    public void teardown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testCheckProcessing() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"id\":\"txn_abc123def456\",\"success\":true,\"processDate\":\"2025-04-10T04:17:09.875Z\",\"ocrMicr\":\"⑆123456789⑆ ⑈123456⑈ 0123\",\"ocrMicrStatus\":\"SUCCESS\",\"ocrMicrConfidence\":\"95\",\"ocrAccountNumber\":\"123456\",\"ocrRoutingNumber\":\"123456789\",\"ocrCheckNumber\":\"0123\",\"ocrCheckTranCode\":\"\",\"ocrAmount\":\"125.50\",\"ocrAmountStatus\":\"SUCCESS\",\"ocrAmountConfidence\":\"98\",\"amountDiscrepancyDetected\":false,\"endorsementDetected\":true,\"errors\":[],\"messages\":[\"Check processed successfully\"],\"carLarMatchConfidence\":\"97\",\"carLarMatchStatus\":\"MATCH\",\"checkType\":1,\"referenceNumber\":\"REF_XYZ789\",\"pageIdentifier\":null}"));
        CheckCaptureResponse response = client.checkCapture()
                .checkProcessing(CheckCaptureRequestBody.builder()
                        .entryPoint("47abcfea12")
                        .frontImage("/9j/4AAQSkZJRgABAQEASABIAAD...")
                        .rearImage("/9j/4AAQSkZJRgABAQEASABIAAD...")
                        .checkAmount(12550)
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"entryPoint\": \"47abcfea12\",\n"
                + "  \"frontImage\": \"/9j/4AAQSkZJRgABAQEASABIAAD...\",\n"
                + "  \"rearImage\": \"/9j/4AAQSkZJRgABAQEASABIAAD...\",\n"
                + "  \"checkAmount\": 12550\n"
                + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertTrue(jsonEquals(expectedJson, actualJson), "Request body structure does not match expected");
        if (actualJson.has("type") || actualJson.has("_type") || actualJson.has("kind")) {
            String discriminator = null;
            if (actualJson.has("type")) discriminator = actualJson.get("type").asText();
            else if (actualJson.has("_type"))
                discriminator = actualJson.get("_type").asText();
            else if (actualJson.has("kind"))
                discriminator = actualJson.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualJson.isNull()) {
            Assertions.assertTrue(
                    actualJson.isObject() || actualJson.isArray() || actualJson.isValueNode(),
                    "request should be a valid JSON value");
        }

        if (actualJson.isArray()) {
            Assertions.assertTrue(actualJson.size() >= 0, "Array should have valid size");
        }
        if (actualJson.isObject()) {
            Assertions.assertTrue(actualJson.size() >= 0, "Object should have valid field count");
        }

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"id\": \"txn_abc123def456\",\n"
                + "  \"success\": true,\n"
                + "  \"processDate\": \"2025-04-10T04:17:09.875Z\",\n"
                + "  \"ocrMicr\": \"⑆123456789⑆ ⑈123456⑈ 0123\",\n"
                + "  \"ocrMicrStatus\": \"SUCCESS\",\n"
                + "  \"ocrMicrConfidence\": \"95\",\n"
                + "  \"ocrAccountNumber\": \"123456\",\n"
                + "  \"ocrRoutingNumber\": \"123456789\",\n"
                + "  \"ocrCheckNumber\": \"0123\",\n"
                + "  \"ocrCheckTranCode\": \"\",\n"
                + "  \"ocrAmount\": \"125.50\",\n"
                + "  \"ocrAmountStatus\": \"SUCCESS\",\n"
                + "  \"ocrAmountConfidence\": \"98\",\n"
                + "  \"amountDiscrepancyDetected\": false,\n"
                + "  \"endorsementDetected\": true,\n"
                + "  \"errors\": [],\n"
                + "  \"messages\": [\n"
                + "    \"Check processed successfully\"\n"
                + "  ],\n"
                + "  \"carLarMatchConfidence\": \"97\",\n"
                + "  \"carLarMatchStatus\": \"MATCH\",\n"
                + "  \"checkType\": 1,\n"
                + "  \"referenceNumber\": \"REF_XYZ789\",\n"
                + "  \"pageIdentifier\": null\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    /**
     * Compares two JsonNodes with numeric equivalence and null safety.
     * For objects, checks that all fields in 'expected' exist in 'actual' with matching values.
     * Allows 'actual' to have extra fields (e.g., default values added during serialization).
     */
    private boolean jsonEquals(JsonNode expected, JsonNode actual) {
        if (expected == null && actual == null) return true;
        if (expected == null || actual == null) return false;
        if (expected.equals(actual)) return true;
        if (expected.isNumber() && actual.isNumber())
            return Math.abs(expected.doubleValue() - actual.doubleValue()) < 1e-10;
        if (expected.isObject() && actual.isObject()) {
            java.util.Iterator<java.util.Map.Entry<String, JsonNode>> iter = expected.fields();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonNode> entry = iter.next();
                JsonNode actualValue = actual.get(entry.getKey());
                if (actualValue == null || !jsonEquals(entry.getValue(), actualValue)) return false;
            }
            return true;
        }
        if (expected.isArray() && actual.isArray()) {
            if (expected.size() != actual.size()) return false;
            for (int i = 0; i < expected.size(); i++) {
                if (!jsonEquals(expected.get(i), actual.get(i))) return false;
            }
            return true;
        }
        return false;
    }
}
