package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.paymentmethoddomain.requests.AddPaymentMethodDomainRequest;
import io.github.payabli.api.resources.paymentmethoddomain.requests.ListPaymentMethodDomainsRequest;
import io.github.payabli.api.resources.paymentmethoddomain.requests.UpdatePaymentMethodDomainRequest;
import io.github.payabli.api.resources.paymentmethoddomain.types.AddPaymentMethodDomainRequestApplePay;
import io.github.payabli.api.resources.paymentmethoddomain.types.AddPaymentMethodDomainRequestGooglePay;
import io.github.payabli.api.resources.paymentmethoddomain.types.DeletePaymentMethodDomainResponse;
import io.github.payabli.api.resources.paymentmethoddomain.types.ListPaymentMethodDomainsResponse;
import io.github.payabli.api.resources.paymentmethoddomain.types.UpdatePaymentMethodDomainRequestWallet;
import io.github.payabli.api.types.AddPaymentMethodDomainApiResponse;
import io.github.payabli.api.types.PaymentMethodDomainApiResponse;
import io.github.payabli.api.types.PaymentMethodDomainGeneralResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentMethodDomainWireTest {
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
    public void testAddPaymentMethodDomain() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageidentifier\":\"pageidentifier\",\"responseData\":{\"id\":\"pmd_a4c7e39d15f24b8c8d6259f174e3d081\",\"type\":\"PaymentMethodDomains\",\"entityId\":109,\"entityType\":\"paypoint\",\"domainName\":\"checkout.example.com\",\"applePay\":{\"isEnabled\":true,\"data\":null},\"googlePay\":{\"isEnabled\":true,\"data\":null},\"ownerEntityId\":109,\"ownerEntityType\":\"paypoint\",\"cascades\":[{\"jobId\":\"1030398\",\"jobStatus\":\"completed\",\"jobErrorMessage\":null,\"createdAt\":\"2025-04-25T15:37:28.685Z\",\"updatedAt\":\"2025-04-25T15:37:33.228Z\"},{\"jobId\":\"611502\",\"jobStatus\":\"completed\",\"jobErrorMessage\":null,\"createdAt\":\"2026-09-26T22:25:45.095Z\",\"updatedAt\":\"2026-09-26T22:25:46.187Z\"},{\"jobId\":\"611172\",\"jobStatus\":\"completed\",\"jobErrorMessage\":null,\"createdAt\":\"2026-09-26T19:46:40.075Z\",\"updatedAt\":\"2026-09-26T19:47:13.548Z\"}],\"createdAt\":\"2025-04-25T15:44:17.016Z\",\"updatedAt\":\"2025-04-25T15:44:17.016Z\"},\"responseText\":\"Success\"}"));
        AddPaymentMethodDomainApiResponse response = client.paymentMethodDomain()
                .addPaymentMethodDomain(AddPaymentMethodDomainRequest.builder()
                        .applePay(AddPaymentMethodDomainRequestApplePay.builder()
                                .isEnabled(true)
                                .build())
                        .googlePay(AddPaymentMethodDomainRequestGooglePay.builder()
                                .isEnabled(true)
                                .build())
                        .domainName("checkout.example.com")
                        .entityId(109L)
                        .entityType("paypoint")
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"domainName\": \"checkout.example.com\",\n"
                + "  \"entityId\": 109,\n"
                + "  \"entityType\": \"paypoint\",\n"
                + "  \"applePay\": {\n"
                + "    \"isEnabled\": true\n"
                + "  },\n"
                + "  \"googlePay\": {\n"
                + "    \"isEnabled\": true\n"
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
                + "  \"isSuccess\": true,\n"
                + "  \"pageidentifier\": \"pageidentifier\",\n"
                + "  \"responseData\": {\n"
                + "    \"id\": \"pmd_a4c7e39d15f24b8c8d6259f174e3d081\",\n"
                + "    \"type\": \"PaymentMethodDomains\",\n"
                + "    \"entityId\": 109,\n"
                + "    \"entityType\": \"paypoint\",\n"
                + "    \"domainName\": \"checkout.example.com\",\n"
                + "    \"applePay\": {\n"
                + "      \"isEnabled\": true,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"googlePay\": {\n"
                + "      \"isEnabled\": true,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"ownerEntityId\": 109,\n"
                + "    \"ownerEntityType\": \"paypoint\",\n"
                + "    \"cascades\": [\n"
                + "      {\n"
                + "        \"jobId\": \"1030398\",\n"
                + "        \"jobStatus\": \"completed\",\n"
                + "        \"jobErrorMessage\": null,\n"
                + "        \"createdAt\": \"2025-04-25T15:37:28.685Z\",\n"
                + "        \"updatedAt\": \"2025-04-25T15:37:33.228Z\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"jobId\": \"611502\",\n"
                + "        \"jobStatus\": \"completed\",\n"
                + "        \"jobErrorMessage\": null,\n"
                + "        \"createdAt\": \"2026-09-26T22:25:45.095Z\",\n"
                + "        \"updatedAt\": \"2026-09-26T22:25:46.187Z\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"jobId\": \"611172\",\n"
                + "        \"jobStatus\": \"completed\",\n"
                + "        \"jobErrorMessage\": null,\n"
                + "        \"createdAt\": \"2026-09-26T19:46:40.075Z\",\n"
                + "        \"updatedAt\": \"2026-09-26T19:47:13.548Z\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"createdAt\": \"2025-04-25T15:44:17.016Z\",\n"
                + "    \"updatedAt\": \"2025-04-25T15:44:17.016Z\"\n"
                + "  },\n"
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

    @Test
    public void testCascadePaymentMethodDomain() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageidentifier\":\"null\",\"responseData\":{\"id\":\"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\"type\":\"PaymentMethodDomains\",\"entityId\":78,\"entityType\":\"organization\",\"domainName\":\"checkout.example.com\",\"applePay\":{\"isEnabled\":true,\"data\":null},\"googlePay\":{\"isEnabled\":true,\"data\":null},\"ownerEntityId\":78,\"ownerEntityType\":\"organization\",\"cascades\":[{\"jobId\":\"1245697\",\"jobStatus\":\"completed\",\"jobErrorMessage\":null,\"createdAt\":\"2025-04-25T15:37:28.685Z\",\"updatedAt\":\"2025-04-25T15:37:33.228Z\"}],\"createdAt\":\"2025-03-15T10:24:36.207Z\",\"updatedAt\":\"2025-04-25T15:38:46.804Z\"},\"responseText\":\"Success\"}"));
        PaymentMethodDomainGeneralResponse response =
                client.paymentMethodDomain().cascadePaymentMethodDomain("pmd_b8237fa45c964d8a9ef27160cd42b8c5");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"pageidentifier\": \"null\",\n"
                + "  \"responseData\": {\n"
                + "    \"id\": \"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\n"
                + "    \"type\": \"PaymentMethodDomains\",\n"
                + "    \"entityId\": 78,\n"
                + "    \"entityType\": \"organization\",\n"
                + "    \"domainName\": \"checkout.example.com\",\n"
                + "    \"applePay\": {\n"
                + "      \"isEnabled\": true,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"googlePay\": {\n"
                + "      \"isEnabled\": true,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"ownerEntityId\": 78,\n"
                + "    \"ownerEntityType\": \"organization\",\n"
                + "    \"cascades\": [\n"
                + "      {\n"
                + "        \"jobId\": \"1245697\",\n"
                + "        \"jobStatus\": \"completed\",\n"
                + "        \"jobErrorMessage\": null,\n"
                + "        \"createdAt\": \"2025-04-25T15:37:28.685Z\",\n"
                + "        \"updatedAt\": \"2025-04-25T15:37:33.228Z\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"createdAt\": \"2025-03-15T10:24:36.207Z\",\n"
                + "    \"updatedAt\": \"2025-04-25T15:38:46.804Z\"\n"
                + "  },\n"
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

    @Test
    public void testDeletePaymentMethodDomain() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseData\":\"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\"responseText\":\"Success\"}"));
        DeletePaymentMethodDomainResponse response =
                client.paymentMethodDomain().deletePaymentMethodDomain("pmd_b8237fa45c964d8a9ef27160cd42b8c5");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"responseData\": \"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\n"
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

    @Test
    public void testGetPaymentMethodDomain() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"id\":\"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\"type\":\"PaymentMethodDomains\",\"entityId\":78,\"entityType\":\"organization\",\"domainName\":\"checkout.example.com\",\"applePay\":{\"isEnabled\":true,\"data\":null},\"googlePay\":{\"isEnabled\":true,\"data\":null},\"ownerEntityId\":78,\"ownerEntityType\":\"organization\",\"cascades\":[{\"jobId\":\"1245697\",\"jobStatus\":\"completed\",\"createdAt\":\"2025-04-25T15:37:28.685Z\",\"updatedAt\":\"2025-04-25T15:37:33.228Z\"}],\"createdAt\":\"2025-03-15T10:24:36.207Z\",\"updatedAt\":\"2025-04-25T15:38:46.804Z\"}"));
        PaymentMethodDomainApiResponse response =
                client.paymentMethodDomain().getPaymentMethodDomain("pmd_b8237fa45c964d8a9ef27160cd42b8c5");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"id\": \"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\n"
                + "  \"type\": \"PaymentMethodDomains\",\n"
                + "  \"entityId\": 78,\n"
                + "  \"entityType\": \"organization\",\n"
                + "  \"domainName\": \"checkout.example.com\",\n"
                + "  \"applePay\": {\n"
                + "    \"isEnabled\": true,\n"
                + "    \"data\": null\n"
                + "  },\n"
                + "  \"googlePay\": {\n"
                + "    \"isEnabled\": true,\n"
                + "    \"data\": null\n"
                + "  },\n"
                + "  \"ownerEntityId\": 78,\n"
                + "  \"ownerEntityType\": \"organization\",\n"
                + "  \"cascades\": [\n"
                + "    {\n"
                + "      \"jobId\": \"1245697\",\n"
                + "      \"jobStatus\": \"completed\",\n"
                + "      \"createdAt\": \"2025-04-25T15:37:28.685Z\",\n"
                + "      \"updatedAt\": \"2025-04-25T15:37:33.228Z\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"createdAt\": \"2025-03-15T10:24:36.207Z\",\n"
                + "  \"updatedAt\": \"2025-04-25T15:38:46.804Z\"\n"
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
    public void testListPaymentMethodDomains() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"records\":[{\"id\":\"pmd_7b4b3ca0f6b74f02853dfcee5ec090a3\",\"type\":\"PaymentMethodDomains\",\"entityId\":1147,\"entityType\":\"paypoint\",\"domainName\":\"payment.example.com\",\"applePay\":{\"isEnabled\":true},\"googlePay\":{\"isEnabled\":true},\"ownerEntityId\":1147,\"ownerEntityType\":\"paypoint\",\"createdAt\":\"2025-02-13T18:31:07.023Z\",\"updatedAt\":\"2025-03-18T13:48:39.056Z\"},{\"id\":\"pmd_1f799c8ab7dd432dbc2052ce332c101c\",\"type\":\"PaymentMethodDomains\",\"entityId\":1147,\"entityType\":\"paypoint\",\"domainName\":\"checkout.example.com\",\"applePay\":{\"isEnabled\":true},\"googlePay\":{\"isEnabled\":true},\"ownerEntityId\":1147,\"ownerEntityType\":\"paypoint\",\"createdAt\":\"2025-02-13T18:04:50.207Z\",\"updatedAt\":\"2025-02-13T18:04:50.207Z\"},{\"id\":\"pmd_135ac1be6fab4a97850aadbbba77ce1b\",\"type\":\"PaymentMethodDomains\",\"entityId\":1147,\"entityType\":\"paypoint\",\"domainName\":\"pay.example.com\",\"applePay\":{\"isEnabled\":true},\"googlePay\":{\"isEnabled\":false},\"ownerEntityId\":1147,\"ownerEntityType\":\"paypoint\",\"createdAt\":\"2026-09-06T03:55:32.213Z\",\"updatedAt\":\"2026-09-06T03:55:47.586Z\"}],\"summary\":{\"pageIdentifier\":\"t.wlbQ4YZ3/JJkaP2/muAxibhlwdVz1Ve89QtI40H9KPhf...\",\"pageSize\":20,\"totalPages\":1,\"totalRecords\":17}}"));
        ListPaymentMethodDomainsResponse response = client.paymentMethodDomain()
                .listPaymentMethodDomains(ListPaymentMethodDomainsRequest.builder()
                        .entityId(1147L)
                        .entityType("paypoint")
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"records\": [\n"
                + "    {\n"
                + "      \"id\": \"pmd_7b4b3ca0f6b74f02853dfcee5ec090a3\",\n"
                + "      \"type\": \"PaymentMethodDomains\",\n"
                + "      \"entityId\": 1147,\n"
                + "      \"entityType\": \"paypoint\",\n"
                + "      \"domainName\": \"payment.example.com\",\n"
                + "      \"applePay\": {\n"
                + "        \"isEnabled\": true\n"
                + "      },\n"
                + "      \"googlePay\": {\n"
                + "        \"isEnabled\": true\n"
                + "      },\n"
                + "      \"ownerEntityId\": 1147,\n"
                + "      \"ownerEntityType\": \"paypoint\",\n"
                + "      \"createdAt\": \"2025-02-13T18:31:07.023Z\",\n"
                + "      \"updatedAt\": \"2025-03-18T13:48:39.056Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"pmd_1f799c8ab7dd432dbc2052ce332c101c\",\n"
                + "      \"type\": \"PaymentMethodDomains\",\n"
                + "      \"entityId\": 1147,\n"
                + "      \"entityType\": \"paypoint\",\n"
                + "      \"domainName\": \"checkout.example.com\",\n"
                + "      \"applePay\": {\n"
                + "        \"isEnabled\": true\n"
                + "      },\n"
                + "      \"googlePay\": {\n"
                + "        \"isEnabled\": true\n"
                + "      },\n"
                + "      \"ownerEntityId\": 1147,\n"
                + "      \"ownerEntityType\": \"paypoint\",\n"
                + "      \"createdAt\": \"2025-02-13T18:04:50.207Z\",\n"
                + "      \"updatedAt\": \"2025-02-13T18:04:50.207Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"pmd_135ac1be6fab4a97850aadbbba77ce1b\",\n"
                + "      \"type\": \"PaymentMethodDomains\",\n"
                + "      \"entityId\": 1147,\n"
                + "      \"entityType\": \"paypoint\",\n"
                + "      \"domainName\": \"pay.example.com\",\n"
                + "      \"applePay\": {\n"
                + "        \"isEnabled\": true\n"
                + "      },\n"
                + "      \"googlePay\": {\n"
                + "        \"isEnabled\": false\n"
                + "      },\n"
                + "      \"ownerEntityId\": 1147,\n"
                + "      \"ownerEntityType\": \"paypoint\",\n"
                + "      \"createdAt\": \"2026-09-06T03:55:32.213Z\",\n"
                + "      \"updatedAt\": \"2026-09-06T03:55:47.586Z\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"summary\": {\n"
                + "    \"pageIdentifier\": \"t.wlbQ4YZ3/JJkaP2/muAxibhlwdVz1Ve89QtI40H9KPhf...\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 17\n"
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
    public void testUpdatePaymentMethodDomain() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageidentifier\":\"null\",\"responseData\":{\"id\":\"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\"type\":\"PaymentMethodDomains\",\"entityId\":78,\"entityType\":\"organization\",\"domainName\":\"checkout.example.com\",\"applePay\":{\"isEnabled\":false,\"data\":null},\"googlePay\":{\"isEnabled\":false,\"data\":null},\"ownerEntityId\":78,\"ownerEntityType\":\"organization\",\"cascades\":[{\"jobId\":\"1245697\",\"jobStatus\":\"completed\",\"jobErrorMessage\":null,\"createdAt\":\"2025-04-25T15:37:28.685Z\",\"updatedAt\":\"2025-04-25T15:37:33.228Z\"}],\"createdAt\":\"2025-03-15T10:24:36.207Z\",\"updatedAt\":\"2025-04-25T16:05:12.345Z\"},\"responseText\":\"Success\"}"));
        PaymentMethodDomainGeneralResponse response = client.paymentMethodDomain()
                .updatePaymentMethodDomain(
                        "pmd_b8237fa45c964d8a9ef27160cd42b8c5",
                        UpdatePaymentMethodDomainRequest.builder()
                                .applePay(UpdatePaymentMethodDomainRequestWallet.builder()
                                        .isEnabled(false)
                                        .build())
                                .googlePay(UpdatePaymentMethodDomainRequestWallet.builder()
                                        .isEnabled(false)
                                        .build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"applePay\": {\n"
                + "    \"isEnabled\": false\n"
                + "  },\n"
                + "  \"googlePay\": {\n"
                + "    \"isEnabled\": false\n"
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
                + "  \"isSuccess\": true,\n"
                + "  \"pageidentifier\": \"null\",\n"
                + "  \"responseData\": {\n"
                + "    \"id\": \"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\n"
                + "    \"type\": \"PaymentMethodDomains\",\n"
                + "    \"entityId\": 78,\n"
                + "    \"entityType\": \"organization\",\n"
                + "    \"domainName\": \"checkout.example.com\",\n"
                + "    \"applePay\": {\n"
                + "      \"isEnabled\": false,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"googlePay\": {\n"
                + "      \"isEnabled\": false,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"ownerEntityId\": 78,\n"
                + "    \"ownerEntityType\": \"organization\",\n"
                + "    \"cascades\": [\n"
                + "      {\n"
                + "        \"jobId\": \"1245697\",\n"
                + "        \"jobStatus\": \"completed\",\n"
                + "        \"jobErrorMessage\": null,\n"
                + "        \"createdAt\": \"2025-04-25T15:37:28.685Z\",\n"
                + "        \"updatedAt\": \"2025-04-25T15:37:33.228Z\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"createdAt\": \"2025-03-15T10:24:36.207Z\",\n"
                + "    \"updatedAt\": \"2025-04-25T16:05:12.345Z\"\n"
                + "  },\n"
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

    @Test
    public void testVerifyPaymentMethodDomain() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageidentifier\":\"null\",\"responseData\":{\"id\":\"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\"type\":\"PaymentMethodDomains\",\"entityId\":78,\"entityType\":\"organization\",\"domainName\":\"checkout.example.com\",\"applePay\":{\"isEnabled\":true,\"data\":null},\"googlePay\":{\"isEnabled\":true,\"data\":null},\"ownerEntityId\":78,\"ownerEntityType\":\"organization\",\"cascades\":[{\"jobId\":\"1245697\",\"jobStatus\":\"completed\",\"jobErrorMessage\":null,\"createdAt\":\"2025-04-25T15:37:28.685Z\",\"updatedAt\":\"2025-04-25T15:37:33.228Z\"}],\"createdAt\":\"2025-03-15T10:24:36.207Z\",\"updatedAt\":\"2025-04-25T15:45:21.517Z\"},\"responseText\":\"Success\"}"));
        PaymentMethodDomainGeneralResponse response =
                client.paymentMethodDomain().verifyPaymentMethodDomain("pmd_b8237fa45c964d8a9ef27160cd42b8c5");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"pageidentifier\": \"null\",\n"
                + "  \"responseData\": {\n"
                + "    \"id\": \"pmd_b8237fa45c964d8a9ef27160cd42b8c5\",\n"
                + "    \"type\": \"PaymentMethodDomains\",\n"
                + "    \"entityId\": 78,\n"
                + "    \"entityType\": \"organization\",\n"
                + "    \"domainName\": \"checkout.example.com\",\n"
                + "    \"applePay\": {\n"
                + "      \"isEnabled\": true,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"googlePay\": {\n"
                + "      \"isEnabled\": true,\n"
                + "      \"data\": null\n"
                + "    },\n"
                + "    \"ownerEntityId\": 78,\n"
                + "    \"ownerEntityType\": \"organization\",\n"
                + "    \"cascades\": [\n"
                + "      {\n"
                + "        \"jobId\": \"1245697\",\n"
                + "        \"jobStatus\": \"completed\",\n"
                + "        \"jobErrorMessage\": null,\n"
                + "        \"createdAt\": \"2025-04-25T15:37:28.685Z\",\n"
                + "        \"updatedAt\": \"2025-04-25T15:37:33.228Z\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"createdAt\": \"2025-03-15T10:24:36.207Z\",\n"
                + "    \"updatedAt\": \"2025-04-25T15:45:21.517Z\"\n"
                + "  },\n"
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
