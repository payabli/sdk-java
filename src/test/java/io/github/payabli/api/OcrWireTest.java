package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.types.FileContentImageOnly;
import io.github.payabli.api.types.PayabliApiResponseOcr;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OcrWireTest {
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
    public void testOcrDocumentForm() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseText\":\"responseText\",\"responseCode\":1,\"responseData\":{\"resultData\":{\"billNumber\":\"billNumber\",\"netAmount\":1.1,\"billDate\":\"2024-01-15T09:30:00Z\",\"dueDate\":\"2024-01-15T09:30:00Z\",\"comments\":\"comments\",\"billItems\":[{}],\"mode\":1,\"accountingField1\":\"accountingField1\",\"accountingField2\":\"accountingField2\",\"endDate\":\"2024-01-15T09:30:00Z\",\"frequency\":\"frequency\",\"terms\":\"terms\",\"status\":1,\"lotNumber\":\"lotNumber\",\"attachments\":[{}]}}}"));
        PayabliApiResponseOcr response = client.ocr()
                .ocrDocumentForm("typeResult", FileContentImageOnly.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"isSuccess\": true,\n"
                + "  \"responseText\": \"responseText\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"resultData\": {\n"
                + "      \"billNumber\": \"billNumber\",\n"
                + "      \"netAmount\": 1.1,\n"
                + "      \"billDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"dueDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"comments\": \"comments\",\n"
                + "      \"billItems\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"mode\": 1,\n"
                + "      \"accountingField1\": \"accountingField1\",\n"
                + "      \"accountingField2\": \"accountingField2\",\n"
                + "      \"endDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"frequency\": \"frequency\",\n"
                + "      \"terms\": \"terms\",\n"
                + "      \"status\": 1,\n"
                + "      \"lotNumber\": \"lotNumber\",\n"
                + "      \"attachments\": [\n"
                + "        {}\n"
                + "      ]\n"
                + "    }\n"
                + "  }\n"
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

    @Test
    public void testOcrDocumentJson() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseText\":\"responseText\",\"responseCode\":1,\"responseData\":{\"resultData\":{\"billNumber\":\"billNumber\",\"netAmount\":1.1,\"billDate\":\"2024-01-15T09:30:00Z\",\"dueDate\":\"2024-01-15T09:30:00Z\",\"comments\":\"comments\",\"billItems\":[{}],\"mode\":1,\"accountingField1\":\"accountingField1\",\"accountingField2\":\"accountingField2\",\"endDate\":\"2024-01-15T09:30:00Z\",\"frequency\":\"frequency\",\"terms\":\"terms\",\"status\":1,\"lotNumber\":\"lotNumber\",\"attachments\":[{}]}}}"));
        PayabliApiResponseOcr response = client.ocr()
                .ocrDocumentJson("typeResult", FileContentImageOnly.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"isSuccess\": true,\n"
                + "  \"responseText\": \"responseText\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"resultData\": {\n"
                + "      \"billNumber\": \"billNumber\",\n"
                + "      \"netAmount\": 1.1,\n"
                + "      \"billDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"dueDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"comments\": \"comments\",\n"
                + "      \"billItems\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"mode\": 1,\n"
                + "      \"accountingField1\": \"accountingField1\",\n"
                + "      \"accountingField2\": \"accountingField2\",\n"
                + "      \"endDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"frequency\": \"frequency\",\n"
                + "      \"terms\": \"terms\",\n"
                + "      \"status\": 1,\n"
                + "      \"lotNumber\": \"lotNumber\",\n"
                + "      \"attachments\": [\n"
                + "        {}\n"
                + "      ]\n"
                + "    }\n"
                + "  }\n"
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
                if (actualValue == null) {
                    if (!entry.getValue().isNull()) return false;
                } else if (!jsonEquals(entry.getValue(), actualValue)) return false;
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
