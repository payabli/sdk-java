package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.statistic.requests.BasicStatsRequest;
import io.github.payabli.api.resources.statistic.requests.CustomerBasicStatsRequest;
import io.github.payabli.api.resources.statistic.requests.SubStatsRequest;
import io.github.payabli.api.resources.statistic.requests.VendorBasicStatsRequest;
import io.github.payabli.api.resources.statistic.types.StatBasicExtendedQueryRecord;
import io.github.payabli.api.resources.statistic.types.StatBasicQueryRecord;
import io.github.payabli.api.resources.statistic.types.StatisticsVendorQueryRecord;
import io.github.payabli.api.resources.statistic.types.SubscriptionStatsQueryRecord;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatisticWireTest {
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
    public void testBasicStats() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"outCustomers\":18196,\"outNewCustomers\":1089,\"outTransactions\":3319,\"outSubscriptionsPaid\":0,\"outCardTransactions\":0,\"outVCardTransactions\":0,\"outACHTransactions\":0,\"outCheckTransactions\":0,\"outPendingMethodTransactions\":22,\"outTransactionsVolume\":13111741.78,\"outSubscriptionsPaidVolume\":0,\"outCardVolume\":0,\"outVCardVolume\":0,\"outACHVolume\":0,\"outCheckVolume\":0,\"outPendingMethodVolume\":82,\"statX\":\"2025-11\",\"inTransactions\":168204,\"inSubscriptionsPaid\":311,\"inCustomers\":2561522,\"inNewCustomers\":44846,\"inCardTransactions\":115059,\"inACHTransactions\":53153,\"inCheckTransactions\":0,\"inCashTransactions\":15,\"inWalletTransactions\":0,\"inCardChargeBacks\":17,\"inACHReturns\":0,\"inTransactionsVolume\":104795896.94,\"inSubscriptionsPaidVolume\":81569.32,\"inCardVolume\":41085285.13,\"inACHVolume\":63706101.81,\"inCheckVolume\":0,\"inCashVolume\":4510,\"inWalletVolume\":0,\"inCardChargeBackVolume\":15455.75,\"inACHReturnsVolume\":0}]"));
        List<StatBasicExtendedQueryRecord> response = client.statistic()
                .basicStats(
                        "ytd",
                        "m",
                        1,
                        1000000L,
                        BasicStatsRequest.builder()
                                .endDate("2025-11-01")
                                .startDate("2025-11-30")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"outCustomers\": 18196,\n"
                + "    \"outNewCustomers\": 1089,\n"
                + "    \"outTransactions\": 3319,\n"
                + "    \"outSubscriptionsPaid\": 0,\n"
                + "    \"outCardTransactions\": 0,\n"
                + "    \"outVCardTransactions\": 0,\n"
                + "    \"outACHTransactions\": 0,\n"
                + "    \"outCheckTransactions\": 0,\n"
                + "    \"outPendingMethodTransactions\": 22,\n"
                + "    \"outTransactionsVolume\": 13111741.78,\n"
                + "    \"outSubscriptionsPaidVolume\": 0,\n"
                + "    \"outCardVolume\": 0,\n"
                + "    \"outVCardVolume\": 0,\n"
                + "    \"outACHVolume\": 0,\n"
                + "    \"outCheckVolume\": 0,\n"
                + "    \"outPendingMethodVolume\": 82,\n"
                + "    \"statX\": \"2025-11\",\n"
                + "    \"inTransactions\": 168204,\n"
                + "    \"inSubscriptionsPaid\": 311,\n"
                + "    \"inCustomers\": 2561522,\n"
                + "    \"inNewCustomers\": 44846,\n"
                + "    \"inCardTransactions\": 115059,\n"
                + "    \"inACHTransactions\": 53153,\n"
                + "    \"inCheckTransactions\": 0,\n"
                + "    \"inCashTransactions\": 15,\n"
                + "    \"inWalletTransactions\": 0,\n"
                + "    \"inCardChargeBacks\": 17,\n"
                + "    \"inACHReturns\": 0,\n"
                + "    \"inTransactionsVolume\": 104795896.94,\n"
                + "    \"inSubscriptionsPaidVolume\": 81569.32,\n"
                + "    \"inCardVolume\": 41085285.13,\n"
                + "    \"inACHVolume\": 63706101.81,\n"
                + "    \"inCheckVolume\": 0,\n"
                + "    \"inCashVolume\": 4510,\n"
                + "    \"inWalletVolume\": 0,\n"
                + "    \"inCardChargeBackVolume\": 15455.75,\n"
                + "    \"inACHReturnsVolume\": 0\n"
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
    public void testCustomerBasicStats() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[{\"interval\":\"2023-03\",\"count\":45,\"volume\":12500.75}]"));
        List<SubscriptionStatsQueryRecord> response = client.statistic()
                .customerBasicStats(
                        "ytd", "m", 998, CustomerBasicStatsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"interval\": \"2023-03\",\n"
                + "    \"count\": 45,\n"
                + "    \"volume\": 12500.75\n"
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
    public void testSubStats() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"statX\":\"2023-03\",\"inTransactions\":150,\"inTransactionsVolume\":25000.5,\"inWalletTransactions\":10,\"inWalletVolume\":1000.5}]"));
        List<StatBasicQueryRecord> response = client.statistic()
                .subStats("30", 1, 1000000L, SubStatsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"statX\": \"2023-03\",\n"
                + "    \"inTransactions\": 150,\n"
                + "    \"inTransactionsVolume\": 25000.5,\n"
                + "    \"inWalletTransactions\": 10,\n"
                + "    \"inWalletVolume\": 1000.5\n"
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
    public void testVendorBasicStats() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"statX\":\"2023-03\",\"active\":25,\"activeVolume\":5000.25,\"sentToApproval\":10,\"sentToApprovalVolume\":2500.75,\"toApproval\":8,\"toApprovalVolume\":1800.5,\"approved\":20,\"approvedVolume\":4200,\"disapproved\":3,\"disapprovedVolume\":600.25,\"cancelled\":2,\"cancelledVolume\":400,\"inTransit\":5,\"inTransitVolume\":1250.75,\"paid\":18,\"paidVolume\":3800.5}]"));
        List<StatisticsVendorQueryRecord> response = client.statistic()
                .vendorBasicStats(
                        "ytd", "m", 1, VendorBasicStatsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"statX\": \"2023-03\",\n"
                + "    \"active\": 25,\n"
                + "    \"activeVolume\": 5000.25,\n"
                + "    \"sentToApproval\": 10,\n"
                + "    \"sentToApprovalVolume\": 2500.75,\n"
                + "    \"toApproval\": 8,\n"
                + "    \"toApprovalVolume\": 1800.5,\n"
                + "    \"approved\": 20,\n"
                + "    \"approvedVolume\": 4200,\n"
                + "    \"disapproved\": 3,\n"
                + "    \"disapprovedVolume\": 600.25,\n"
                + "    \"cancelled\": 2,\n"
                + "    \"cancelledVolume\": 400,\n"
                + "    \"inTransit\": 5,\n"
                + "    \"inTransitVolume\": 1250.75,\n"
                + "    \"paid\": 18,\n"
                + "    \"paidVolume\": 3800.5\n"
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
