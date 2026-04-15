package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.vendor.types.VendorEnrichRequest;
import io.github.payabli.api.resources.vendor.types.VendorEnrichResponse;
import io.github.payabli.api.types.BankAccountHolderType;
import io.github.payabli.api.types.BillingData;
import io.github.payabli.api.types.Contacts;
import io.github.payabli.api.types.FileContent;
import io.github.payabli.api.types.FileContentFtype;
import io.github.payabli.api.types.PayabliApiResponseVendors;
import io.github.payabli.api.types.TypeAccount;
import io.github.payabli.api.types.VendorData;
import io.github.payabli.api.types.VendorQueryRecord;
import java.util.Arrays;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendorWireTest {
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
    public void testAddVendor() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3890,\"responseText\":\"Success\"}"));
        PayabliApiResponseVendors response = client.vendor()
                .addVendor(
                        "8cfec329267",
                        VendorData.builder()
                                .vendorNumber("1234")
                                .address1("123 Ocean Drive")
                                .address2("Suite 400")
                                .billingData(BillingData.builder()
                                        .accountNumber("123123123")
                                        .bankAccountFunction(0)
                                        .bankAccountHolderName("Gruzya Adventure Outfitters LLC")
                                        .bankAccountHolderType(BankAccountHolderType.BUSINESS)
                                        .bankName("Country Bank")
                                        .id(123)
                                        .routingAccount("123123123")
                                        .typeAccount(TypeAccount.CHECKING)
                                        .build())
                                .city("Miami")
                                .contacts(Arrays.asList(Contacts.builder()
                                        .contactEmail("example@email.com")
                                        .contactName("Herman Martinez")
                                        .contactPhone("3055550000")
                                        .contactTitle("Owner")
                                        .build()))
                                .country("US")
                                .customerVendorAccount("A-37622")
                                .ein("12-3456789")
                                .email("example@email.com")
                                .internalReferenceId(123L)
                                .locationCode("MIA123")
                                .mcc("7777")
                                .name1("Herman's Coatings and Masonry")
                                .name2("<string>")
                                .payeeName1("<string>")
                                .payeeName2("<string>")
                                .paymentMethod("managed")
                                .phone("5555555555")
                                .remitAddress1("123 Walnut Street")
                                .remitAddress2("Suite 900")
                                .remitCity("Miami")
                                .remitCountry("US")
                                .remitState("FL")
                                .remitZip("31113")
                                .state("FL")
                                .vendorStatus(1)
                                .zip("33139")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"vendorNumber\": \"1234\",\n"
                + "  \"name1\": \"Herman's Coatings and Masonry\",\n"
                + "  \"name2\": \"<string>\",\n"
                + "  \"ein\": \"12-3456789\",\n"
                + "  \"phone\": \"5555555555\",\n"
                + "  \"email\": \"example@email.com\",\n"
                + "  \"address1\": \"123 Ocean Drive\",\n"
                + "  \"address2\": \"Suite 400\",\n"
                + "  \"city\": \"Miami\",\n"
                + "  \"state\": \"FL\",\n"
                + "  \"zip\": \"33139\",\n"
                + "  \"country\": \"US\",\n"
                + "  \"mcc\": \"7777\",\n"
                + "  \"locationCode\": \"MIA123\",\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactEmail\": \"example@email.com\",\n"
                + "      \"contactTitle\": \"Owner\",\n"
                + "      \"contactPhone\": \"3055550000\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"billingData\": {\n"
                + "    \"id\": 123,\n"
                + "    \"bankName\": \"Country Bank\",\n"
                + "    \"routingAccount\": \"123123123\",\n"
                + "    \"accountNumber\": \"123123123\",\n"
                + "    \"typeAccount\": \"Checking\",\n"
                + "    \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "    \"bankAccountHolderType\": \"Business\",\n"
                + "    \"bankAccountFunction\": 0\n"
                + "  },\n"
                + "  \"paymentMethod\": \"managed\",\n"
                + "  \"vendorStatus\": 1,\n"
                + "  \"remitAddress1\": \"123 Walnut Street\",\n"
                + "  \"remitAddress2\": \"Suite 900\",\n"
                + "  \"remitCity\": \"Miami\",\n"
                + "  \"remitState\": \"FL\",\n"
                + "  \"remitZip\": \"31113\",\n"
                + "  \"remitCountry\": \"US\",\n"
                + "  \"payeeName1\": \"<string>\",\n"
                + "  \"payeeName2\": \"<string>\",\n"
                + "  \"customerVendorAccount\": \"A-37622\",\n"
                + "  \"internalReferenceId\": 123\n"
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
                + "  \"responseData\": 3890,\n"
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
    public void testDeleteVendor() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3890,\"responseText\":\"Success\"}"));
        PayabliApiResponseVendors response = client.vendor().deleteVendor(1);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": 3890,\n"
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
    public void testEditVendor() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3890,\"responseText\":\"Success\"}"));
        PayabliApiResponseVendors response = client.vendor()
                .editVendor(
                        1, VendorData.builder().name1("Theodore's Janitorial").build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"name1\": \"Theodore's Janitorial\"\n" + "}";
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
                + "  \"responseData\": 3890,\n"
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
    public void testGetVendor() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/VendorWireTest_testGetVendor_response.json")));
        VendorQueryRecord response = client.vendor().getVendor(1);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/VendorWireTest_testGetVendor_response.json");
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
    public void testEnrichVendor() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseText\":\"Success\",\"responseData\":{\"enrichmentId\":\"enrich-3890-a1b2c3d4\",\"status\":\"insufficient\",\"stagesTriggered\":[\"invoice_scan\"],\"vendorPayoutReady\":false,\"enrichmentData\":{\"invoiceScan\":{\"vendorName\":\"Greenfield Landscaping\",\"street\":\"456 Commerce Blvd\",\"city\":\"Indianapolis\",\"state\":\"IN\",\"zipCode\":\"46201\",\"country\":\"US\",\"phone\":\"5555550100\",\"email\":null,\"paymentLink\":null,\"cardAccepted\":\"unable to determine\",\"achAccepted\":\"unable to determine\",\"checkAccepted\":\"yes\",\"invoiceNumber\":\"INV-70683\",\"amountDue\":390.5,\"dueDate\":\"2026-01-31\"},\"webSearch\":null}}}"));
        VendorEnrichResponse response = client.vendor()
                .enrichVendor(
                        "8cfec329267",
                        VendorEnrichRequest.builder()
                                .vendorId(3890L)
                                .scope(Optional.of(Arrays.asList("invoice_scan")))
                                .applyEnrichmentData(false)
                                .invoiceFile(FileContent.builder()
                                        .fContent("<base64-encoded-pdf>")
                                        .filename("invoice-2026-001.pdf")
                                        .ftype(FileContentFtype.PDF)
                                        .build())
                                .fallbackMethod("check")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"vendorId\": 3890,\n"
                + "  \"scope\": [\n"
                + "    \"invoice_scan\"\n"
                + "  ],\n"
                + "  \"applyEnrichmentData\": false,\n"
                + "  \"fallbackMethod\": \"check\",\n"
                + "  \"invoiceFile\": {\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"filename\": \"invoice-2026-001.pdf\",\n"
                + "    \"fContent\": \"<base64-encoded-pdf>\"\n"
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
                + "    \"enrichmentId\": \"enrich-3890-a1b2c3d4\",\n"
                + "    \"status\": \"insufficient\",\n"
                + "    \"stagesTriggered\": [\n"
                + "      \"invoice_scan\"\n"
                + "    ],\n"
                + "    \"vendorPayoutReady\": false,\n"
                + "    \"enrichmentData\": {\n"
                + "      \"invoiceScan\": {\n"
                + "        \"vendorName\": \"Greenfield Landscaping\",\n"
                + "        \"street\": \"456 Commerce Blvd\",\n"
                + "        \"city\": \"Indianapolis\",\n"
                + "        \"state\": \"IN\",\n"
                + "        \"zipCode\": \"46201\",\n"
                + "        \"country\": \"US\",\n"
                + "        \"phone\": \"5555550100\",\n"
                + "        \"email\": null,\n"
                + "        \"paymentLink\": null,\n"
                + "        \"cardAccepted\": \"unable to determine\",\n"
                + "        \"achAccepted\": \"unable to determine\",\n"
                + "        \"checkAccepted\": \"yes\",\n"
                + "        \"invoiceNumber\": \"INV-70683\",\n"
                + "        \"amountDue\": 390.5,\n"
                + "        \"dueDate\": \"2026-01-31\"\n"
                + "      },\n"
                + "      \"webSearch\": null\n"
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
