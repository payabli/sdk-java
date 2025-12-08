package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.moneyout.requests.CaptureAllOutRequest;
import io.github.payabli.api.resources.moneyout.requests.CaptureOutRequest;
import io.github.payabli.api.resources.moneyout.requests.MoneyOutTypesRequestOutAuthorize;
import io.github.payabli.api.resources.moneyout.requests.SendVCardLinkRequest;
import io.github.payabli.api.resources.moneyouttypes.types.AuthCapturePayoutResponse;
import io.github.payabli.api.resources.moneyouttypes.types.AuthorizePaymentMethod;
import io.github.payabli.api.resources.moneyouttypes.types.AuthorizePayoutBody;
import io.github.payabli.api.resources.moneyouttypes.types.CaptureAllOutResponse;
import io.github.payabli.api.resources.moneyouttypes.types.OperationResult;
import io.github.payabli.api.resources.moneyouttypes.types.RequestOutAuthorizeInvoiceData;
import io.github.payabli.api.resources.moneyouttypes.types.RequestOutAuthorizePaymentDetails;
import io.github.payabli.api.resources.moneyouttypes.types.RequestOutAuthorizeVendorData;
import io.github.payabli.api.resources.moneyouttypes.types.VCardGetResponse;
import io.github.payabli.api.types.BillDetailResponse;
import io.github.payabli.api.types.PayabliApiResponse0000;
import java.util.Arrays;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoneyOutWireTest {
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
    public void testAuthorizeOut() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":{\"authCode\":null,\"referenceId\":\"129-219\",\"resultCode\":1,\"resultText\":\"Authorized\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":0,\"methodReferenceId\":null}}"));
        AuthCapturePayoutResponse response = client.moneyOut()
                .authorizeOut(MoneyOutTypesRequestOutAuthorize.builder()
                        .body(AuthorizePayoutBody.builder()
                                .entryPoint("48acde49")
                                .paymentMethod(AuthorizePaymentMethod.builder()
                                        .method("managed")
                                        .build())
                                .paymentDetails(RequestOutAuthorizePaymentDetails.builder()
                                        .totalAmount(47.0)
                                        .unbundled(false)
                                        .build())
                                .vendorData(RequestOutAuthorizeVendorData.builder()
                                        .vendorNumber("7895433")
                                        .build())
                                .orderDescription("Window Painting")
                                .invoiceData(Arrays.asList(RequestOutAuthorizeInvoiceData.builder()
                                        .billId(54323L)
                                        .build()))
                                .build())
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"entryPoint\": \"48acde49\",\n"
                + "  \"invoiceData\": [\n"
                + "    {\n"
                + "      \"billId\": 54323\n"
                + "    }\n"
                + "  ],\n"
                + "  \"orderDescription\": \"Window Painting\",\n"
                + "  \"paymentDetails\": {\n"
                + "    \"totalAmount\": 47,\n"
                + "    \"unbundled\": false\n"
                + "  },\n"
                + "  \"paymentMethod\": {\n"
                + "    \"method\": \"managed\"\n"
                + "  },\n"
                + "  \"vendorData\": {\n"
                + "    \"vendorNumber\": \"7895433\"\n"
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
                + "  \"responseCode\": 1,\n"
                + "  \"pageIdentifier\": null,\n"
                + "  \"roomId\": 0,\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"responseData\": {\n"
                + "    \"authCode\": null,\n"
                + "    \"referenceId\": \"129-219\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Authorized\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": 0,\n"
                + "    \"methodReferenceId\": null\n"
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
    public void testCancelAllOut() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseCode\":1,\"responseData\":[{\"CustomerId\":1000000,\"ReferenceId\":\"129-230\",\"ResultCode\":1,\"ResultText\":\"Cancelled\"},{\"CustomerId\":1000000,\"ReferenceId\":\"129-219\",\"ResultCode\":1,\"ResultText\":\"Cancelled\"}],\"responseText\":\"Success\"}"));
        CaptureAllOutResponse response = client.moneyOut().cancelAllOut(Arrays.asList("2-29", "2-28", "2-27"));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "[\n" + "  \"2-29\",\n" + "  \"2-28\",\n" + "  \"2-27\"\n" + "]";
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
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": [\n"
                + "    {\n"
                + "      \"CustomerId\": 1000000,\n"
                + "      \"ReferenceId\": \"129-230\",\n"
                + "      \"ResultCode\": 1,\n"
                + "      \"ResultText\": \"Cancelled\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"CustomerId\": 1000000,\n"
                + "      \"ReferenceId\": \"129-219\",\n"
                + "      \"ResultCode\": 1,\n"
                + "      \"ResultText\": \"Cancelled\"\n"
                + "    }\n"
                + "  ],\n"
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
    public void testCancelOutGet() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseText\":\"Success\",\"pageIdentifier\":null,\"responseData\":{\"ReferenceId\":\"129-219\",\"ResultCode\":1,\"ResultText\":\"Approved\",\"CustomerId\":0,\"AuthCode\":null,\"cvvResponseText\":null,\"avsResponseText\":null,\"methodReferenceId\":null}}"));
        PayabliApiResponse0000 response = client.moneyOut().cancelOutGet("129-219");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"pageIdentifier\": null,\n"
                + "  \"responseData\": {\n"
                + "    \"ReferenceId\": \"129-219\",\n"
                + "    \"ResultCode\": 1,\n"
                + "    \"ResultText\": \"Approved\",\n"
                + "    \"CustomerId\": 0,\n"
                + "    \"AuthCode\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"methodReferenceId\": null\n"
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
    public void testCancelOutDelete() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseText\":\"Success\",\"pageIdentifier\":null,\"responseData\":{\"ReferenceId\":\"129-219\",\"ResultCode\":1,\"ResultText\":\"Approved\",\"CustomerId\":0,\"AuthCode\":null,\"cvvResponseText\":null,\"avsResponseText\":null,\"methodReferenceId\":null}}"));
        PayabliApiResponse0000 response = client.moneyOut().cancelOutDelete("129-219");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"pageIdentifier\": null,\n"
                + "  \"responseData\": {\n"
                + "    \"ReferenceId\": \"129-219\",\n"
                + "    \"ResultCode\": 1,\n"
                + "    \"ResultText\": \"Approved\",\n"
                + "    \"CustomerId\": 0,\n"
                + "    \"AuthCode\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"methodReferenceId\": null\n"
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
    public void testCaptureAllOut() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseCode\":1,\"responseData\":[{\"CustomerId\":1000000,\"ReferenceId\":\"129-230\",\"ResultCode\":1,\"ResultText\":\"Captured\"},{\"CustomerId\":1000000,\"ReferenceId\":\"129-219\",\"ResultCode\":1,\"ResultText\":\"Captured\"}],\"responseText\":\"Success\"}"));
        CaptureAllOutResponse response = client.moneyOut()
                .captureAllOut(CaptureAllOutRequest.builder()
                        .body(Arrays.asList("2-29", "2-28", "2-27"))
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "[\n" + "  \"2-29\",\n" + "  \"2-28\",\n" + "  \"2-27\"\n" + "]";
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
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": [\n"
                + "    {\n"
                + "      \"CustomerId\": 1000000,\n"
                + "      \"ReferenceId\": \"129-230\",\n"
                + "      \"ResultCode\": 1,\n"
                + "      \"ResultText\": \"Captured\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"CustomerId\": 1000000,\n"
                + "      \"ReferenceId\": \"129-219\",\n"
                + "      \"ResultCode\": 1,\n"
                + "      \"ResultText\": \"Captured\"\n"
                + "    }\n"
                + "  ],\n"
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
    public void testCaptureOut() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":{\"authCode\":null,\"referenceId\":\"129-219\",\"resultCode\":1,\"resultText\":\"Authorized\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":0,\"methodReferenceId\":null}}"));
        AuthCapturePayoutResponse response = client.moneyOut()
                .captureOut("129-219", CaptureOutRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"responseCode\": 1,\n"
                + "  \"pageIdentifier\": null,\n"
                + "  \"roomId\": 0,\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"responseData\": {\n"
                + "    \"authCode\": null,\n"
                + "    \"referenceId\": \"129-219\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Authorized\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": 0,\n"
                + "    \"methodReferenceId\": null\n"
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
    public void testPayoutDetails() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Bills\":[{\"invoiceNumber\":\"123B\",\"netAmount\":\"8800.00\"}],\"CheckData\":null,\"CheckNumber\":null,\"Comments\":\"testing\",\"CreatedDate\":\"2022-07-01T15:00:01Z\",\"Events\":[{\"EventTime\":\"2023-04-24T09:17:49Z\",\"TransEvent\":\"Authorized\"}],\"FeeAmount\":0,\"Gateway\":\"TSYS\",\"IdOut\":350,\"LastUpdated\":\"2023-04-23T17:00:00Z\",\"NetAmount\":8800,\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"AccountType\":\"\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"HolderName\":\"\",\"Initiator\":\"payor\",\"MaskedAccount\":\"\",\"Sequence\":\"subsequent\",\"SignatureData\":\"SignatureData\",\"StoredMethodUsageType\":\"subscription\"},\"PaymentGroup\":\"2345667-ddd-fff\",\"PaymentId\":\"12345678910\",\"PaymentMethod\":\"managed\",\"PaymentStatus\":\"Authorized\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointLegalname\":\"Sunshine Services, LLC\",\"Source\":\"api\",\"Status\":11,\"StatusText\":\"Captured\",\"TotalAmount\":8800,\"Vendor\":{\"VendorNumber\":\"1234\",\"Name1\":\"Herman's Coatings\",\"Name2\":\"Herman's Coating Supply Company, LLC\",\"EIN\":\"123456789\",\"Phone\":\"212-555-1234\",\"Email\":\"example@email.com\",\"RemitEmail\":null,\"Address1\":\"123 Ocean Drive\",\"Address2\":\"Suite 400\",\"City\":\"Bristol\",\"State\":\"GA\",\"Zip\":\"31113\",\"Country\":\"US\",\"Mcc\":\"7777\",\"LocationCode\":\"LOC123\",\"Contacts\":{\"ContactEmail\":\"eric@martinezcoatings.com\",\"ContactName\":\"Eric Martinez\",\"ContactPhone\":\"5555555555\",\"ContactTitle\":\"Owner\"},\"BillingData\":{\"id\":123456,\"accountId\":\"bank-account-001\",\"nickname\":\"Main Checking Account\",\"bankName\":\"Example Bank\",\"routingAccount\":\"123456789\",\"accountNumber\":\"9876543210\",\"typeAccount\":\"Checking\",\"bankAccountHolderName\":\"John Doe\",\"bankAccountHolderType\":\"Business\",\"bankAccountFunction\":2,\"verified\":true,\"status\":1,\"services\":[],\"default\":true},\"PaymentMethod\":null,\"VendorStatus\":1,\"VendorId\":1,\"EnrollmentStatus\":null,\"Summary\":{\"ActiveBills\":2,\"PendingBills\":4,\"InTransitBills\":3,\"PaidBills\":18,\"OverdueBills\":1,\"ApprovedBills\":5,\"DisapprovedBills\":1,\"TotalBills\":34,\"ActiveBillsAmount\":1250.75,\"PendingBillsAmount\":2890.5,\"InTransitBillsAmount\":1675.25,\"PaidBillsAmount\":15420.8,\"OverdueBillsAmount\":425,\"ApprovedBillsAmount\":3240.9,\"DisapprovedBillsAmount\":180,\"TotalBillsAmount\":25083.2},\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"ParentOrgName\":\"PropertyManager Pro\",\"ParentOrgId\":1000,\"CreatedDate\":\"2022-07-01T15:00:01Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"remitAddress1\":\"123 Walnut Street\",\"remitAddress2\":\"Suite 900\",\"remitCity\":\"Miami\",\"remitState\":\"FL\",\"remitZip\":\"31113\",\"remitCountry\":\"US\",\"payeeName1\":\"payeeName1\",\"payeeName2\":\"payeeName2\",\"customField1\":\"\",\"customField2\":\"\",\"customerVendorAccount\":\"123-456\",\"InternalReferenceId\":1000000,\"additionalData\":null,\"externalPaypointID\":\"Paypoint-100\",\"StoredMethods\":[]},\"CreatedAt\":null,\"ParentOrgId\":null,\"externalPaypointID\":null,\"EntryName\":null,\"BatchId\":null,\"HasVcardTransactions\":false,\"IsSameDayACH\":false,\"ScheduleId\":null,\"SettlementStatus\":null,\"RiskFlagged\":false,\"RiskFlaggedOn\":null,\"RiskStatus\":null,\"RiskReason\":null,\"RiskAction\":null,\"RiskActionCode\":null}"));
        BillDetailResponse response = client.moneyOut().payoutDetails("45-as456777hhhhhhhhhh77777777-324");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Bills\": [\n"
                + "    {\n"
                + "      \"invoiceNumber\": \"123B\",\n"
                + "      \"netAmount\": \"8800.00\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"CheckData\": null,\n"
                + "  \"CheckNumber\": null,\n"
                + "  \"Comments\": \"testing\",\n"
                + "  \"CreatedDate\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"Events\": [\n"
                + "    {\n"
                + "      \"EventTime\": \"2023-04-24T09:17:49Z\",\n"
                + "      \"TransEvent\": \"Authorized\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"FeeAmount\": 0,\n"
                + "  \"Gateway\": \"TSYS\",\n"
                + "  \"IdOut\": 350,\n"
                + "  \"LastUpdated\": \"2023-04-23T17:00:00Z\",\n"
                + "  \"NetAmount\": 8800,\n"
                + "  \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "  \"PaymentData\": {\n"
                + "    \"AccountType\": \"\",\n"
                + "    \"binData\": {\n"
                + "      \"binMatchedLength\": \"6\",\n"
                + "      \"binCardBrand\": \"Visa\",\n"
                + "      \"binCardType\": \"Credit\",\n"
                + "      \"binCardCategory\": \"PLATINUM\",\n"
                + "      \"binCardIssuer\": \"Bank of Example\",\n"
                + "      \"binCardIssuerCountry\": \"United States\",\n"
                + "      \"binCardIssuerCountryCodeA2\": \"US\",\n"
                + "      \"binCardIssuerCountryNumber\": \"840\",\n"
                + "      \"binCardIsRegulated\": \"false\",\n"
                + "      \"binCardUseCategory\": \"Consumer\",\n"
                + "      \"binCardIssuerCountryCodeA3\": \"USA\"\n"
                + "    },\n"
                + "    \"HolderName\": \"\",\n"
                + "    \"Initiator\": \"payor\",\n"
                + "    \"MaskedAccount\": \"\",\n"
                + "    \"Sequence\": \"subsequent\",\n"
                + "    \"SignatureData\": \"SignatureData\",\n"
                + "    \"StoredMethodUsageType\": \"subscription\"\n"
                + "  },\n"
                + "  \"PaymentGroup\": \"2345667-ddd-fff\",\n"
                + "  \"PaymentId\": \"12345678910\",\n"
                + "  \"PaymentMethod\": \"managed\",\n"
                + "  \"PaymentStatus\": \"Authorized\",\n"
                + "  \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "  \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "  \"Source\": \"api\",\n"
                + "  \"Status\": 11,\n"
                + "  \"StatusText\": \"Captured\",\n"
                + "  \"TotalAmount\": 8800,\n"
                + "  \"Vendor\": {\n"
                + "    \"VendorNumber\": \"1234\",\n"
                + "    \"Name1\": \"Herman's Coatings\",\n"
                + "    \"Name2\": \"Herman's Coating Supply Company, LLC\",\n"
                + "    \"EIN\": \"123456789\",\n"
                + "    \"Phone\": \"212-555-1234\",\n"
                + "    \"Email\": \"example@email.com\",\n"
                + "    \"RemitEmail\": null,\n"
                + "    \"Address1\": \"123 Ocean Drive\",\n"
                + "    \"Address2\": \"Suite 400\",\n"
                + "    \"City\": \"Bristol\",\n"
                + "    \"State\": \"GA\",\n"
                + "    \"Zip\": \"31113\",\n"
                + "    \"Country\": \"US\",\n"
                + "    \"Mcc\": \"7777\",\n"
                + "    \"LocationCode\": \"LOC123\",\n"
                + "    \"Contacts\": {\n"
                + "      \"ContactEmail\": \"eric@martinezcoatings.com\",\n"
                + "      \"ContactName\": \"Eric Martinez\",\n"
                + "      \"ContactPhone\": \"5555555555\",\n"
                + "      \"ContactTitle\": \"Owner\"\n"
                + "    },\n"
                + "    \"BillingData\": {\n"
                + "      \"id\": 123456,\n"
                + "      \"accountId\": \"bank-account-001\",\n"
                + "      \"nickname\": \"Main Checking Account\",\n"
                + "      \"bankName\": \"Example Bank\",\n"
                + "      \"routingAccount\": \"123456789\",\n"
                + "      \"accountNumber\": \"9876543210\",\n"
                + "      \"typeAccount\": \"Checking\",\n"
                + "      \"bankAccountHolderName\": \"John Doe\",\n"
                + "      \"bankAccountHolderType\": \"Business\",\n"
                + "      \"bankAccountFunction\": 2,\n"
                + "      \"verified\": true,\n"
                + "      \"status\": 1,\n"
                + "      \"services\": [],\n"
                + "      \"default\": true\n"
                + "    },\n"
                + "    \"PaymentMethod\": null,\n"
                + "    \"VendorStatus\": 1,\n"
                + "    \"VendorId\": 1,\n"
                + "    \"EnrollmentStatus\": null,\n"
                + "    \"Summary\": {\n"
                + "      \"ActiveBills\": 2,\n"
                + "      \"PendingBills\": 4,\n"
                + "      \"InTransitBills\": 3,\n"
                + "      \"PaidBills\": 18,\n"
                + "      \"OverdueBills\": 1,\n"
                + "      \"ApprovedBills\": 5,\n"
                + "      \"DisapprovedBills\": 1,\n"
                + "      \"TotalBills\": 34,\n"
                + "      \"ActiveBillsAmount\": 1250.75,\n"
                + "      \"PendingBillsAmount\": 2890.5,\n"
                + "      \"InTransitBillsAmount\": 1675.25,\n"
                + "      \"PaidBillsAmount\": 15420.8,\n"
                + "      \"OverdueBillsAmount\": 425,\n"
                + "      \"ApprovedBillsAmount\": 3240.9,\n"
                + "      \"DisapprovedBillsAmount\": 180,\n"
                + "      \"TotalBillsAmount\": 25083.2\n"
                + "    },\n"
                + "    \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "    \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "    \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "    \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "    \"ParentOrgId\": 1000,\n"
                + "    \"CreatedDate\": \"2022-07-01T15:00:01Z\",\n"
                + "    \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "    \"remitAddress1\": \"123 Walnut Street\",\n"
                + "    \"remitAddress2\": \"Suite 900\",\n"
                + "    \"remitCity\": \"Miami\",\n"
                + "    \"remitState\": \"FL\",\n"
                + "    \"remitZip\": \"31113\",\n"
                + "    \"remitCountry\": \"US\",\n"
                + "    \"payeeName1\": \"payeeName1\",\n"
                + "    \"payeeName2\": \"payeeName2\",\n"
                + "    \"customField1\": \"\",\n"
                + "    \"customField2\": \"\",\n"
                + "    \"customerVendorAccount\": \"123-456\",\n"
                + "    \"InternalReferenceId\": 1000000,\n"
                + "    \"additionalData\": null,\n"
                + "    \"externalPaypointID\": \"Paypoint-100\",\n"
                + "    \"StoredMethods\": []\n"
                + "  },\n"
                + "  \"CreatedAt\": null,\n"
                + "  \"ParentOrgId\": null,\n"
                + "  \"externalPaypointID\": null,\n"
                + "  \"EntryName\": null,\n"
                + "  \"BatchId\": null,\n"
                + "  \"HasVcardTransactions\": false,\n"
                + "  \"IsSameDayACH\": false,\n"
                + "  \"ScheduleId\": null,\n"
                + "  \"SettlementStatus\": null,\n"
                + "  \"RiskFlagged\": false,\n"
                + "  \"RiskFlaggedOn\": null,\n"
                + "  \"RiskStatus\": null,\n"
                + "  \"RiskReason\": null,\n"
                + "  \"RiskAction\": null,\n"
                + "  \"RiskActionCode\": null\n"
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
    public void testVCardGet() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"vcardSent\":false,\"cardToken\":\"20231206142225226104\",\"cardNumber\":\"553232XXXXXX3179\",\"cvc\":\"XXX\",\"expirationDate\":\"2025-05-01\",\"status\":null,\"amount\":120,\"currentBalance\":120,\"expenseLimit\":20,\"expenseLimitPeriod\":null,\"maxNumberOfUses\":1,\"currentNumberOfUses\":0,\"exactAmount\":true,\"mcc\":null,\"tcc\":null,\"misc1\":null,\"misc2\":null,\"dateCreated\":\"2023-12-06T20:25:31.077\",\"dateModified\":\"2023-12-06T00:00:00\",\"associatedVendor\":{\"VendorNumber\":\"VENDOR123456\",\"Name1\":\"Smith Industries\",\"Name2\":\"John Smith\",\"EIN\":\"12-3456789\",\"Phone\":\"555-123-4567\",\"Email\":\"contact@smithindustries.com\",\"RemitEmail\":null,\"Address1\":\"1234 Main Street\",\"Address2\":\"Suite 200\",\"City\":\"New York\",\"State\":\"NY\",\"Zip\":\"10001\",\"Country\":\"USA\",\"Mcc\":\"5411\",\"LocationCode\":null,\"Contacts\":[{\"ContactName\":\"Herman Martinez\",\"ContactEmail\":\"herman@hermanscoatings.com\",\"ContactTitle\":\"Owner\",\"ContactPhone\":\"3055550000\"}],\"BillingData\":{\"id\":123,\"accountId\":null,\"nickname\":\"Checking Account\",\"bankName\":\"Chase Bank\",\"routingAccount\":\"021000021\",\"accountNumber\":\"3XXXXXX8888\",\"typeAccount\":\"Checking\",\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"bankAccountHolderType\":\"Business\",\"bankAccountFunction\":0,\"verified\":true,\"status\":1,\"services\":[],\"default\":true},\"PaymentMethod\":\"vcard\",\"VendorStatus\":1,\"VendorId\":339,\"EnrollmentStatus\":null,\"Summary\":{\"ActiveBills\":1,\"ActiveBillsAmount\":1.1,\"ApprovedBills\":1,\"ApprovedBillsAmount\":1.1,\"DisapprovedBills\":1,\"DisapprovedBillsAmount\":1.1,\"InTransitBills\":0,\"InTransitBillsAmount\":0,\"OverdueBills\":1,\"OverdueBillsAmount\":100,\"PaidBills\":0,\"PaidBillsAmount\":0,\"PendingBills\":1,\"PendingBillsAmount\":100,\"TotalBills\":1,\"TotalBillsAmount\":100},\"PaypointLegalname\":\"Athlete Factory LLC\",\"PaypointDbaname\":\"Athlete Factory LLC\",\"PaypointEntryname\":\"PaypointEntryname\",\"ParentOrgName\":\"HOA Manager Pro\",\"ParentOrgId\":1232,\"CreatedDate\":\"2022-07-01T15:00:01Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"remitAddress1\":\"123 Walnut Street\",\"remitAddress2\":\"Suite 900\",\"remitCity\":\"Miami\",\"remitState\":\"FL\",\"remitZip\":\"31113\",\"remitCountry\":\"US\",\"payeeName1\":null,\"payeeName2\":null,\"customField1\":\"customField1\",\"customField2\":\"customField2\",\"customerVendorAccount\":null,\"InternalReferenceId\":27,\"additionalData\":null,\"externalPaypointID\":null,\"StoredMethods\":null},\"associatedCustomer\":null,\"ParentOrgName\":\"HOA Manager Pro\",\"PaypointDbaname\":\"Athlete Factory LLC\",\"PaypointLegalname\":\"Athlete Factory LLC\",\"PaypointEntryname\":\"47acde49\",\"externalPaypointID\":null,\"paypointId\":12345}"));
        VCardGetResponse response = client.moneyOut().vCardGet("20230403315245421165");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"vcardSent\": false,\n"
                + "  \"cardToken\": \"20231206142225226104\",\n"
                + "  \"cardNumber\": \"553232XXXXXX3179\",\n"
                + "  \"cvc\": \"XXX\",\n"
                + "  \"expirationDate\": \"2025-05-01\",\n"
                + "  \"status\": null,\n"
                + "  \"amount\": 120,\n"
                + "  \"currentBalance\": 120,\n"
                + "  \"expenseLimit\": 20,\n"
                + "  \"expenseLimitPeriod\": null,\n"
                + "  \"maxNumberOfUses\": 1,\n"
                + "  \"currentNumberOfUses\": 0,\n"
                + "  \"exactAmount\": true,\n"
                + "  \"mcc\": null,\n"
                + "  \"tcc\": null,\n"
                + "  \"misc1\": null,\n"
                + "  \"misc2\": null,\n"
                + "  \"dateCreated\": \"2023-12-06T20:25:31.077\",\n"
                + "  \"dateModified\": \"2023-12-06T00:00:00\",\n"
                + "  \"associatedVendor\": {\n"
                + "    \"VendorNumber\": \"VENDOR123456\",\n"
                + "    \"Name1\": \"Smith Industries\",\n"
                + "    \"Name2\": \"John Smith\",\n"
                + "    \"EIN\": \"12-3456789\",\n"
                + "    \"Phone\": \"555-123-4567\",\n"
                + "    \"Email\": \"contact@smithindustries.com\",\n"
                + "    \"RemitEmail\": null,\n"
                + "    \"Address1\": \"1234 Main Street\",\n"
                + "    \"Address2\": \"Suite 200\",\n"
                + "    \"City\": \"New York\",\n"
                + "    \"State\": \"NY\",\n"
                + "    \"Zip\": \"10001\",\n"
                + "    \"Country\": \"USA\",\n"
                + "    \"Mcc\": \"5411\",\n"
                + "    \"LocationCode\": null,\n"
                + "    \"Contacts\": [\n"
                + "      {\n"
                + "        \"ContactName\": \"Herman Martinez\",\n"
                + "        \"ContactEmail\": \"herman@hermanscoatings.com\",\n"
                + "        \"ContactTitle\": \"Owner\",\n"
                + "        \"ContactPhone\": \"3055550000\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"BillingData\": {\n"
                + "      \"id\": 123,\n"
                + "      \"accountId\": null,\n"
                + "      \"nickname\": \"Checking Account\",\n"
                + "      \"bankName\": \"Chase Bank\",\n"
                + "      \"routingAccount\": \"021000021\",\n"
                + "      \"accountNumber\": \"3XXXXXX8888\",\n"
                + "      \"typeAccount\": \"Checking\",\n"
                + "      \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "      \"bankAccountHolderType\": \"Business\",\n"
                + "      \"bankAccountFunction\": 0,\n"
                + "      \"verified\": true,\n"
                + "      \"status\": 1,\n"
                + "      \"services\": [],\n"
                + "      \"default\": true\n"
                + "    },\n"
                + "    \"PaymentMethod\": \"vcard\",\n"
                + "    \"VendorStatus\": 1,\n"
                + "    \"VendorId\": 339,\n"
                + "    \"EnrollmentStatus\": null,\n"
                + "    \"Summary\": {\n"
                + "      \"ActiveBills\": 1,\n"
                + "      \"ActiveBillsAmount\": 1.1,\n"
                + "      \"ApprovedBills\": 1,\n"
                + "      \"ApprovedBillsAmount\": 1.1,\n"
                + "      \"DisapprovedBills\": 1,\n"
                + "      \"DisapprovedBillsAmount\": 1.1,\n"
                + "      \"InTransitBills\": 0,\n"
                + "      \"InTransitBillsAmount\": 0,\n"
                + "      \"OverdueBills\": 1,\n"
                + "      \"OverdueBillsAmount\": 100,\n"
                + "      \"PaidBills\": 0,\n"
                + "      \"PaidBillsAmount\": 0,\n"
                + "      \"PendingBills\": 1,\n"
                + "      \"PendingBillsAmount\": 100,\n"
                + "      \"TotalBills\": 1,\n"
                + "      \"TotalBillsAmount\": 100\n"
                + "    },\n"
                + "    \"PaypointLegalname\": \"Athlete Factory LLC\",\n"
                + "    \"PaypointDbaname\": \"Athlete Factory LLC\",\n"
                + "    \"PaypointEntryname\": \"PaypointEntryname\",\n"
                + "    \"ParentOrgName\": \"HOA Manager Pro\",\n"
                + "    \"ParentOrgId\": 1232,\n"
                + "    \"CreatedDate\": \"2022-07-01T15:00:01Z\",\n"
                + "    \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "    \"remitAddress1\": \"123 Walnut Street\",\n"
                + "    \"remitAddress2\": \"Suite 900\",\n"
                + "    \"remitCity\": \"Miami\",\n"
                + "    \"remitState\": \"FL\",\n"
                + "    \"remitZip\": \"31113\",\n"
                + "    \"remitCountry\": \"US\",\n"
                + "    \"payeeName1\": null,\n"
                + "    \"payeeName2\": null,\n"
                + "    \"customField1\": \"customField1\",\n"
                + "    \"customField2\": \"customField2\",\n"
                + "    \"customerVendorAccount\": null,\n"
                + "    \"InternalReferenceId\": 27,\n"
                + "    \"additionalData\": null,\n"
                + "    \"externalPaypointID\": null,\n"
                + "    \"StoredMethods\": null\n"
                + "  },\n"
                + "  \"associatedCustomer\": null,\n"
                + "  \"ParentOrgName\": \"HOA Manager Pro\",\n"
                + "  \"PaypointDbaname\": \"Athlete Factory LLC\",\n"
                + "  \"PaypointLegalname\": \"Athlete Factory LLC\",\n"
                + "  \"PaypointEntryname\": \"47acde49\",\n"
                + "  \"externalPaypointID\": null,\n"
                + "  \"paypointId\": 12345\n"
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
    public void testSendVCardLink() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\":\"Successfully sent email to: vendor@vendor.com\",\"success\":true}"));
        OperationResult response = client.moneyOut()
                .sendVCardLink(SendVCardLinkRequest.builder()
                        .transId("01K33Z6YQZ6GD5QVKZ856MJBSC")
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"transId\": \"01K33Z6YQZ6GD5QVKZ856MJBSC\"\n" + "}";
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
                + "  \"message\": \"Successfully sent email to: vendor@vendor.com\",\n"
                + "  \"success\": true\n"
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
    public void testGetCheckImage() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "\"%PDF-1.7\\n%����\\n123 0 obj\\n<</Linearized 1/L 123456/O 125/E 78901/N 1/T 123450/H [ 800 200]>>\\nendobj\\n\\n124 0 obj\\n<</DecodeParms<</Columns 4/Predictor 12>>/Filter/FlateDecode/ID[<AB123C4567EF890123456789ABCDEF01><12345678ABCDEF9876543210FEDCBA98>]/Index[123 100]/Info 122 0 R/Length 128/Prev 123450/Root 125 0 R/Size 223/Type/XRef/W[1 3 1]>>stream\\nh�bbd```b``�\\n\\\"x�a7�r�H~�����A�D���2����m�f��L`v6�H����D���J[@����H8�I��)0��q� XD��`��a���P�`�`��\\\"�A$������r���p�$�Ip������a� �\""));
        String response = client.moneyOut().getCheckImage("check133832686289732320_01JKBNZ5P32JPTZY8XXXX000000.pdf");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "\"%PDF-1.7\\n%����\\n123 0 obj\\n<</Linearized 1/L 123456/O 125/E 78901/N 1/T 123450/H [ 800 200]>>\\nendobj\\n\\n124 0 obj\\n<</DecodeParms<</Columns 4/Predictor 12>>/Filter/FlateDecode/ID[<AB123C4567EF890123456789ABCDEF01><12345678ABCDEF9876543210FEDCBA98>]/Index[123 100]/Info 122 0 R/Length 128/Prev 123450/Root 125 0 R/Size 223/Type/XRef/W[1 3 1]>>stream\\nh�bbd```b``�\\n\\\"x�a7�r�H~�����A�D���2����m�f��L`v6�H����D���J[@����H8�I��)0��q� XD��`��a���P�`�`��\\\"�A$������r���p�$�Ip������a� �\"";
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
