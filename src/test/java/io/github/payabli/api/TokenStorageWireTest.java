package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.tokenstorage.requests.AddMethodRequest;
import io.github.payabli.api.resources.tokenstorage.requests.GetMethodRequest;
import io.github.payabli.api.resources.tokenstorage.requests.UpdateMethodRequest;
import io.github.payabli.api.resources.tokenstorage.types.AddMethodResponse;
import io.github.payabli.api.resources.tokenstorage.types.GetMethodResponse;
import io.github.payabli.api.resources.tokenstorage.types.RequestTokenStorage;
import io.github.payabli.api.resources.tokenstorage.types.RequestTokenStoragePaymentMethod;
import io.github.payabli.api.resources.tokenstorage.types.TokenizeCard;
import io.github.payabli.api.types.PayabliApiResponsePaymethodDelete;
import io.github.payabli.api.types.PayorDataRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenStorageWireTest {
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
    public void testAddMethod() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"customerId\":4400,\"methodReferenceId\":\"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\"referenceId\":\"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\"resultCode\":1,\"resultText\":\"Approved\"},\"responseText\":\"Success\"}"));
        AddMethodResponse response = client.tokenStorage()
                .addMethod(AddMethodRequest.builder()
                        .body(RequestTokenStorage.builder()
                                .customerData(PayorDataRequest.builder()
                                        .customerId(4440L)
                                        .build())
                                .entryPoint("f743aed24a")
                                .fallbackAuth(true)
                                .paymentMethod(RequestTokenStoragePaymentMethod.of(TokenizeCard.builder()
                                        .method("card")
                                        .cardexp("02/25")
                                        .cardHolder("John Doe")
                                        .cardnumber("4111111111111111")
                                        .cardcvv("123")
                                        .cardzip("12345")
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
                + "  \"customerData\": {\n"
                + "    \"customerId\": 4440\n"
                + "  },\n"
                + "  \"entryPoint\": \"f743aed24a\",\n"
                + "  \"fallbackAuth\": true,\n"
                + "  \"paymentMethod\": {\n"
                + "    \"cardcvv\": \"123\",\n"
                + "    \"cardexp\": \"02/25\",\n"
                + "    \"cardHolder\": \"John Doe\",\n"
                + "    \"cardnumber\": \"4111111111111111\",\n"
                + "    \"cardzip\": \"12345\",\n"
                + "    \"method\": \"card\"\n"
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
                + "  \"responseData\": {\n"
                + "    \"customerId\": 4400,\n"
                + "    \"methodReferenceId\": \"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\n"
                + "    \"referenceId\": \"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Approved\"\n"
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
    public void testGetMethod() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"aba\":\"\",\"achHolderType\":\"personal\",\"achSecCode\":\"achSecCode\",\"bin\":\"401288\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"customers\":[{\"additionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"balance\":250,\"billingPhone\":\"1234567890\",\"company\":\"Bluesky Tech Inc\",\"created\":\"2023-06-01T14:30:00Z\",\"customerId\":1456,\"customerNumber\":\"CS789\",\"customerStatus\":1,\"customerUsername\":\"Marcus\",\"identifierFields\":[\"firstname\",\"email\"],\"lastUpdated\":\"2024-12-15T09:45:32Z\",\"mfa\":true,\"mfaMode\":1,\"parentOrgId\":5,\"parentOrgName\":\"TechCorp\",\"paypointDbaname\":\"Bluesky Tech\",\"paypointEntryname\":\"45782932fcc\",\"paypointLegalname\":\"Bluesky Technologies LLC\",\"shippingAddress1\":\"Suite 500\",\"shippingCity\":\"San Francisco\",\"shippingCountry\":\"US\",\"shippingState\":\"CA\",\"shippingZip\":\"94105\",\"timeZone\":-8}],\"descriptor\":\"visa\",\"expDate\":\"0926\",\"holderName\":\"Marcus Chen\",\"idPmethod\":\"81f7fde1-dd8b-4892-b2e1-cd60dd91f6b4-XXXC\",\"lastUpdated\":\"2025-01-15T16:30:22Z\",\"maskedAccount\":\"4XXXXXXX2345\",\"method\":\"card\",\"methodType\":\"Single Merchant\",\"postalCode\":\"94105\"},\"responseText\":\"Success\"}"));
        GetMethodResponse response = client.tokenStorage()
                .getMethod(
                        "32-8877drt00045632-678",
                        GetMethodRequest.builder()
                                .cardExpirationFormat(1)
                                .includeTemporary(false)
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": {\n"
                + "    \"aba\": \"\",\n"
                + "    \"achHolderType\": \"personal\",\n"
                + "    \"achSecCode\": \"achSecCode\",\n"
                + "    \"bin\": \"401288\",\n"
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
                + "    \"customers\": [\n"
                + "      {\n"
                + "        \"additionalData\": {\n"
                + "          \"key1\": {\n"
                + "            \"key\": \"value\"\n"
                + "          },\n"
                + "          \"key2\": {\n"
                + "            \"key\": \"value\"\n"
                + "          },\n"
                + "          \"key3\": {\n"
                + "            \"key\": \"value\"\n"
                + "          }\n"
                + "        },\n"
                + "        \"balance\": 250,\n"
                + "        \"billingPhone\": \"1234567890\",\n"
                + "        \"company\": \"Bluesky Tech Inc\",\n"
                + "        \"created\": \"2023-06-01T14:30:00Z\",\n"
                + "        \"customerId\": 1456,\n"
                + "        \"customerNumber\": \"CS789\",\n"
                + "        \"customerStatus\": 1,\n"
                + "        \"customerUsername\": \"Marcus\",\n"
                + "        \"identifierFields\": [\n"
                + "          \"firstname\",\n"
                + "          \"email\"\n"
                + "        ],\n"
                + "        \"lastUpdated\": \"2024-12-15T09:45:32Z\",\n"
                + "        \"mfa\": true,\n"
                + "        \"mfaMode\": 1,\n"
                + "        \"parentOrgId\": 5,\n"
                + "        \"parentOrgName\": \"TechCorp\",\n"
                + "        \"paypointDbaname\": \"Bluesky Tech\",\n"
                + "        \"paypointEntryname\": \"45782932fcc\",\n"
                + "        \"paypointLegalname\": \"Bluesky Technologies LLC\",\n"
                + "        \"shippingAddress1\": \"Suite 500\",\n"
                + "        \"shippingCity\": \"San Francisco\",\n"
                + "        \"shippingCountry\": \"US\",\n"
                + "        \"shippingState\": \"CA\",\n"
                + "        \"shippingZip\": \"94105\",\n"
                + "        \"timeZone\": -8\n"
                + "      }\n"
                + "    ],\n"
                + "    \"descriptor\": \"visa\",\n"
                + "    \"expDate\": \"0926\",\n"
                + "    \"holderName\": \"Marcus Chen\",\n"
                + "    \"idPmethod\": \"81f7fde1-dd8b-4892-b2e1-cd60dd91f6b4-XXXC\",\n"
                + "    \"lastUpdated\": \"2025-01-15T16:30:22Z\",\n"
                + "    \"maskedAccount\": \"4XXXXXXX2345\",\n"
                + "    \"method\": \"card\",\n"
                + "    \"methodType\": \"Single Merchant\",\n"
                + "    \"postalCode\": \"94105\"\n"
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
    public void testRemoveMethod() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"referenceId\":\"32-8877drt65345632-678\",\"resultCode\":1,\"resultText\":\"Removed\"},\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymethodDelete response = client.tokenStorage().removeMethod("32-8877drt00045632-678");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": {\n"
                + "    \"referenceId\": \"32-8877drt65345632-678\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Removed\"\n"
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
    public void testUpdateMethod() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"referenceId\":\"1b502b79-e319-4159-8c29-a9f8d9f105c8-1323\",\"resultCode\":1,\"resultText\":\"Updated\"},\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymethodDelete response = client.tokenStorage()
                .updateMethod(
                        "32-8877drt00045632-678",
                        UpdateMethodRequest.builder()
                                .body(RequestTokenStorage.builder()
                                        .customerData(PayorDataRequest.builder()
                                                .customerId(4440L)
                                                .build())
                                        .entryPoint("f743aed24a")
                                        .fallbackAuth(true)
                                        .paymentMethod(RequestTokenStoragePaymentMethod.of(TokenizeCard.builder()
                                                .method("card")
                                                .cardexp("02/25")
                                                .cardHolder("John Doe")
                                                .cardnumber("4111111111111111")
                                                .cardcvv("123")
                                                .cardzip("12345")
                                                .build()))
                                        .build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"customerData\": {\n"
                + "    \"customerId\": 4440\n"
                + "  },\n"
                + "  \"entryPoint\": \"f743aed24a\",\n"
                + "  \"fallbackAuth\": true,\n"
                + "  \"paymentMethod\": {\n"
                + "    \"cardcvv\": \"123\",\n"
                + "    \"cardexp\": \"02/25\",\n"
                + "    \"cardHolder\": \"John Doe\",\n"
                + "    \"cardnumber\": \"4111111111111111\",\n"
                + "    \"cardzip\": \"12345\",\n"
                + "    \"method\": \"card\"\n"
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
                + "  \"responseData\": {\n"
                + "    \"referenceId\": \"1b502b79-e319-4159-8c29-a9f8d9f105c8-1323\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Updated\"\n"
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
