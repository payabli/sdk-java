package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.notificationlogs.requests.SearchNotificationLogsRequest;
import io.github.payabli.api.resources.notificationlogs.types.NotificationLog;
import io.github.payabli.api.resources.notificationlogs.types.NotificationLogDetail;
import io.github.payabli.api.resources.notificationlogs.types.NotificationLogSearchRequest;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NotificationlogsWireTest {
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
    public void testSearchNotificationLogs() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"id\":\"550e8400-e29b-41d4-a716-446655440000\",\"orgId\":12345,\"paypointId\":67890,\"notificationEvent\":\"ActivatedMerchant\",\"target\":\"https://webhook.example.com/payments\",\"responseStatus\":\"200\",\"success\":true,\"jobData\":\"{\\\"transactionId\\\":\\\"txn_123\\\"}\",\"createdDate\":\"2024-01-15T10:30:00Z\",\"successDate\":\"2024-01-15T10:30:05Z\",\"lastFailedDate\":null,\"isInProgress\":false}]"));
        List<NotificationLog> response = client.notificationlogs()
                .searchNotificationLogs(SearchNotificationLogsRequest.builder()
                        .body(NotificationLogSearchRequest.builder()
                                .startDate(OffsetDateTime.parse("2024-01-01T00:00:00Z"))
                                .endDate(OffsetDateTime.parse("2024-01-31T23:59:59Z"))
                                .notificationEvent("ActivatedMerchant")
                                .succeeded(true)
                                .orgId(12345L)
                                .build())
                        .pageSize(20)
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"startDate\": \"2024-01-01T00:00:00Z\",\n"
                + "  \"endDate\": \"2024-01-31T23:59:59Z\",\n"
                + "  \"orgId\": 12345,\n"
                + "  \"notificationEvent\": \"ActivatedMerchant\",\n"
                + "  \"succeeded\": true\n"
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
                + "[\n"
                + "  {\n"
                + "    \"id\": \"550e8400-e29b-41d4-a716-446655440000\",\n"
                + "    \"orgId\": 12345,\n"
                + "    \"paypointId\": 67890,\n"
                + "    \"notificationEvent\": \"ActivatedMerchant\",\n"
                + "    \"target\": \"https://webhook.example.com/payments\",\n"
                + "    \"responseStatus\": \"200\",\n"
                + "    \"success\": true,\n"
                + "    \"jobData\": \"{\\\"transactionId\\\":\\\"txn_123\\\"}\",\n"
                + "    \"createdDate\": \"2024-01-15T10:30:00Z\",\n"
                + "    \"successDate\": \"2024-01-15T10:30:05Z\",\n"
                + "    \"lastFailedDate\": null,\n"
                + "    \"isInProgress\": false\n"
                + "  }\n"
                + "]";
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
    public void testGetNotificationLog() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"id\":\"550e8400-e29b-41d4-a716-446655440000\",\"orgId\":12345,\"paypointId\":67890,\"notificationEvent\":\"ActivatedMerchant\",\"target\":\"https://webhook.example.com/payments\",\"responseStatus\":\"200\",\"success\":true,\"jobData\":\"{\\\"transactionId\\\":\\\"txn_123\\\"}\",\"createdDate\":\"2024-01-15T10:30:00Z\",\"successDate\":\"2024-01-15T10:30:05Z\",\"lastFailedDate\":null,\"isInProgress\":false,\"webHeaders\":[{\"key\":\"Content-Type\",\"value\":\"application/json\"},{\"key\":\"User-Agent\",\"value\":\"PaymentSystem/1.0\"}],\"responseHeaders\":[{\"key\":\"Content-Type\",\"value\":[\"application/json\"]},{\"key\":\"X-Request-ID\",\"value\":[\"req_abc123\"]}],\"responseContent\":\"{\\\"status\\\":\\\"received\\\",\\\"id\\\":\\\"wh_123\\\"}\"}"));
        NotificationLogDetail response =
                client.notificationlogs().getNotificationLog(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"id\": \"550e8400-e29b-41d4-a716-446655440000\",\n"
                + "  \"orgId\": 12345,\n"
                + "  \"paypointId\": 67890,\n"
                + "  \"notificationEvent\": \"ActivatedMerchant\",\n"
                + "  \"target\": \"https://webhook.example.com/payments\",\n"
                + "  \"responseStatus\": \"200\",\n"
                + "  \"success\": true,\n"
                + "  \"jobData\": \"{\\\"transactionId\\\":\\\"txn_123\\\"}\",\n"
                + "  \"createdDate\": \"2024-01-15T10:30:00Z\",\n"
                + "  \"successDate\": \"2024-01-15T10:30:05Z\",\n"
                + "  \"lastFailedDate\": null,\n"
                + "  \"isInProgress\": false,\n"
                + "  \"webHeaders\": [\n"
                + "    {\n"
                + "      \"key\": \"Content-Type\",\n"
                + "      \"value\": \"application/json\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"User-Agent\",\n"
                + "      \"value\": \"PaymentSystem/1.0\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"responseHeaders\": [\n"
                + "    {\n"
                + "      \"key\": \"Content-Type\",\n"
                + "      \"value\": [\n"
                + "        \"application/json\"\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"X-Request-ID\",\n"
                + "      \"value\": [\n"
                + "        \"req_abc123\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"responseContent\": \"{\\\"status\\\":\\\"received\\\",\\\"id\\\":\\\"wh_123\\\"}\"\n"
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
    public void testRetryNotificationLog() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"id\":\"550e8400-e29b-41d4-a716-446655440000\",\"orgId\":12345,\"paypointId\":67890,\"notificationEvent\":\"ActivatedMerchant\",\"target\":\"https://webhook.example.com/payments\",\"responseStatus\":\"200\",\"success\":true,\"jobData\":\"{\\\"transactionId\\\":\\\"txn_123\\\"}\",\"createdDate\":\"2024-01-15T10:30:00Z\",\"successDate\":\"2024-01-15T10:30:05Z\",\"lastFailedDate\":null,\"isInProgress\":false,\"webHeaders\":[{\"key\":\"Content-Type\",\"value\":\"application/json\"}],\"responseHeaders\":[{\"key\":\"Content-Type\",\"value\":[\"application/json\"]}],\"responseContent\":\"{\\\"status\\\":\\\"received\\\",\\\"id\\\":\\\"wh_123\\\"}\"}"));
        NotificationLogDetail response =
                client.notificationlogs().retryNotificationLog(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"id\": \"550e8400-e29b-41d4-a716-446655440000\",\n"
                + "  \"orgId\": 12345,\n"
                + "  \"paypointId\": 67890,\n"
                + "  \"notificationEvent\": \"ActivatedMerchant\",\n"
                + "  \"target\": \"https://webhook.example.com/payments\",\n"
                + "  \"responseStatus\": \"200\",\n"
                + "  \"success\": true,\n"
                + "  \"jobData\": \"{\\\"transactionId\\\":\\\"txn_123\\\"}\",\n"
                + "  \"createdDate\": \"2024-01-15T10:30:00Z\",\n"
                + "  \"successDate\": \"2024-01-15T10:30:05Z\",\n"
                + "  \"lastFailedDate\": null,\n"
                + "  \"isInProgress\": false,\n"
                + "  \"webHeaders\": [\n"
                + "    {\n"
                + "      \"key\": \"Content-Type\",\n"
                + "      \"value\": \"application/json\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"responseHeaders\": [\n"
                + "    {\n"
                + "      \"key\": \"Content-Type\",\n"
                + "      \"value\": [\n"
                + "        \"application/json\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"responseContent\": \"{\\\"status\\\":\\\"received\\\",\\\"id\\\":\\\"wh_123\\\"}\"\n"
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
    public void testBulkRetryNotificationLogs() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.notificationlogs()
                .bulkRetryNotificationLogs(Arrays.asList(
                        UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                        UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                        UUID.fromString("550e8400-e29b-41d4-a716-446655440002")));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "[\n"
                + "  \"550e8400-e29b-41d4-a716-446655440000\",\n"
                + "  \"550e8400-e29b-41d4-a716-446655440001\",\n"
                + "  \"550e8400-e29b-41d4-a716-446655440002\"\n"
                + "]";
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
