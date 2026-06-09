package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.payoutsubscription.requests.RequestPayoutSchedule;
import io.github.payabli.api.resources.payoutsubscription.requests.UpdatePayoutSubscriptionBody;
import io.github.payabli.api.types.AddPayoutSubscriptionResponse;
import io.github.payabli.api.types.AuthorizePaymentMethod;
import io.github.payabli.api.types.BillPayOutDataRequest;
import io.github.payabli.api.types.DeletePayoutSubscriptionResponse;
import io.github.payabli.api.types.Frequency;
import io.github.payabli.api.types.GetPayoutSubscriptionResponse;
import io.github.payabli.api.types.PayoutPaymentDetail;
import io.github.payabli.api.types.PayoutScheduleDetail;
import io.github.payabli.api.types.RequestOutAuthorizeVendorData;
import io.github.payabli.api.types.UpdatePayoutSubscriptionResponse;
import java.util.Arrays;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PayoutSubscriptionWireTest {
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
    public void testCreatePayoutSubscription() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"responseText\":\"Success\",\"isSuccess\":true,\"responseData\":42,\"customerId\":4440}"));
        AddPayoutSubscriptionResponse response = client.payoutSubscription()
                .createPayoutSubscription(RequestPayoutSchedule.builder()
                        .entryPoint("8cfec329267")
                        .paymentMethod(AuthorizePaymentMethod.builder()
                                .method("ach")
                                .achHolder("Herman Coatings")
                                .achRouting("021000021")
                                .achAccount("3453445666")
                                .achAccountType("checking")
                                .build())
                        .vendorData(RequestOutAuthorizeVendorData.builder()
                                .vendorId(456)
                                .build())
                        .paymentDetails(PayoutPaymentDetail.builder()
                                .totalAmount(500.0)
                                .serviceFee(0.0)
                                .currency("USD")
                                .build())
                        .billData(Optional.of(Arrays.asList(BillPayOutDataRequest.builder()
                                .dueDate("2025-08-15")
                                .invoiceDate("2025-08-01")
                                .invoiceNumber("INV-2345")
                                .netAmount("500")
                                .build())))
                        .scheduleDetails(PayoutScheduleDetail.builder()
                                .startDate("09/01/2027")
                                .endDate("09/01/2026")
                                .frequency(Frequency.MONTHLY)
                                .build())
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"entryPoint\": \"8cfec329267\",\n"
                + "  \"paymentMethod\": {\n"
                + "    \"method\": \"ach\",\n"
                + "    \"achHolder\": \"Herman Coatings\",\n"
                + "    \"achRouting\": \"021000021\",\n"
                + "    \"achAccount\": \"3453445666\",\n"
                + "    \"achAccountType\": \"checking\"\n"
                + "  },\n"
                + "  \"paymentDetails\": {\n"
                + "    \"totalAmount\": 500,\n"
                + "    \"serviceFee\": 0,\n"
                + "    \"currency\": \"USD\"\n"
                + "  },\n"
                + "  \"vendorData\": {\n"
                + "    \"vendorId\": 456\n"
                + "  },\n"
                + "  \"billData\": [\n"
                + "    {\n"
                + "      \"invoiceNumber\": \"INV-2345\",\n"
                + "      \"netAmount\": \"500\",\n"
                + "      \"invoiceDate\": \"2025-08-01\",\n"
                + "      \"dueDate\": \"2025-08-15\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"scheduleDetails\": {\n"
                + "    \"startDate\": \"09/01/2027\",\n"
                + "    \"endDate\": \"09/01/2026\",\n"
                + "    \"frequency\": \"monthly\"\n"
                + "  }\n"
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
                + "  \"responseText\": \"Success\",\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": 42,\n"
                + "  \"customerId\": 4440\n"
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
    public void testGetPayoutSubscription() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/PayoutSubscriptionWireTest_testGetPayoutSubscription_response.json")));
        GetPayoutSubscriptionResponse response = client.payoutSubscription().getPayoutSubscription(42L);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = TestResources.loadResource(
                "/wire-tests/PayoutSubscriptionWireTest_testGetPayoutSubscription_response.json");
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
    public void testUpdatePayoutSubscription() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseText\":\"Success\",\"isSuccess\":true,\"responseData\":\"42 paused\",\"customerId\":4440}"));
        UpdatePayoutSubscriptionResponse response = client.payoutSubscription()
                .updatePayoutSubscription(
                        42L,
                        UpdatePayoutSubscriptionBody.builder().setPause(true).build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"setPause\": true\n" + "}";
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
                + "  \"responseText\": \"Success\",\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": \"42 paused\",\n"
                + "  \"customerId\": 4440\n"
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
    public void testDeletePayoutSubscription() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":\"42\",\"responseText\":\"Success\"}"));
        DeletePayoutSubscriptionResponse response = client.payoutSubscription().deletePayoutSubscription(42L);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": \"42\",\n"
                + "  \"responseText\": \"Success\"\n"
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
