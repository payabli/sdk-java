package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.moneyout.requests.CaptureAllOutRequest;
import io.github.payabli.api.resources.moneyout.requests.CaptureOutRequest;
import io.github.payabli.api.resources.moneyout.requests.MoneyOutTypesRequestOutAuthorize;
import io.github.payabli.api.resources.moneyout.requests.ReissueOutRequest;
import io.github.payabli.api.resources.moneyout.requests.SendVCardLinkRequest;
import io.github.payabli.api.resources.moneyouttypes.types.AllowedCheckPaymentStatus;
import io.github.payabli.api.resources.moneyouttypes.types.AuthCapturePayoutResponse;
import io.github.payabli.api.resources.moneyouttypes.types.AuthorizePaymentMethod;
import io.github.payabli.api.resources.moneyouttypes.types.AuthorizePayoutBody;
import io.github.payabli.api.resources.moneyouttypes.types.CaptureAllOutResponse;
import io.github.payabli.api.resources.moneyouttypes.types.OperationResult;
import io.github.payabli.api.resources.moneyouttypes.types.ReissuePaymentMethod;
import io.github.payabli.api.resources.moneyouttypes.types.ReissuePayoutBody;
import io.github.payabli.api.resources.moneyouttypes.types.ReissuePayoutResponse;
import io.github.payabli.api.resources.moneyouttypes.types.RequestOutAuthorizeInvoiceData;
import io.github.payabli.api.resources.moneyouttypes.types.RequestOutAuthorizePaymentDetails;
import io.github.payabli.api.resources.moneyouttypes.types.RequestOutAuthorizeVendorData;
import io.github.payabli.api.resources.moneyouttypes.types.VCardGetResponse;
import io.github.payabli.api.types.AchHolderType;
import io.github.payabli.api.types.BillDetailResponse;
import io.github.payabli.api.types.PayabliApiResponse0000;
import io.github.payabli.api.types.PayabliApiResponse00Responsedatanonobject;
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
                                .autoCapture(true)
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
                + "  \"autoCapture\": true,\n"
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
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/MoneyOutWireTest_testPayoutDetails_response.json")));
        BillDetailResponse response = client.moneyOut().payoutDetails("45-as456777hhhhhhhhhh77777777-324");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/MoneyOutWireTest_testPayoutDetails_response.json");
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
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/MoneyOutWireTest_testVCardGet_response.json")));
        VCardGetResponse response = client.moneyOut().vCardGet("20230403315245421165");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/MoneyOutWireTest_testVCardGet_response.json");
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

    @Test
    public void testUpdateCheckPaymentStatus() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":\"TRANS123456\"}"));
        PayabliApiResponse00Responsedatanonobject response =
                client.moneyOut().updateCheckPaymentStatus("TRANS123456", AllowedCheckPaymentStatus.PAID);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());

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
                + "  \"responseData\": \"TRANS123456\"\n"
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
    public void testReissueOut() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseText\":\"Success\",\"responseData\":{\"transactionId\":\"130-220\",\"status\":\"Authorized\",\"originalTransactionId\":\"129-219\"}}"));
        ReissuePayoutResponse response = client.moneyOut()
                .reissueOut(ReissueOutRequest.builder()
                        .transId("129-219")
                        .body(ReissuePayoutBody.builder()
                                .paymentMethod(ReissuePaymentMethod.builder()
                                        .method("ach")
                                        .achHolder("Acme Corp")
                                        .achRouting("021000021")
                                        .achAccount("9876543210")
                                        .achAccountType("savings")
                                        .achHolderType(AchHolderType.BUSINESS)
                                        .build())
                                .build())
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"paymentMethod\": {\n"
                + "    \"method\": \"ach\",\n"
                + "    \"achAccount\": \"9876543210\",\n"
                + "    \"achAccountType\": \"savings\",\n"
                + "    \"achRouting\": \"021000021\",\n"
                + "    \"achHolder\": \"Acme Corp\",\n"
                + "    \"achHolderType\": \"business\"\n"
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
                + "  \"responseCode\": 1,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"responseData\": {\n"
                + "    \"transactionId\": \"130-220\",\n"
                + "    \"status\": \"Authorized\",\n"
                + "    \"originalTransactionId\": \"129-219\"\n"
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
