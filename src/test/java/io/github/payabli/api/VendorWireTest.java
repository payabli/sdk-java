package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.types.BankAccountHolderType;
import io.github.payabli.api.types.BillingData;
import io.github.payabli.api.types.Contacts;
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
                                .contacts(Optional.of(Arrays.asList(Contacts.builder()
                                        .contactEmail("example@email.com")
                                        .contactName("Herman Martinez")
                                        .contactPhone("3055550000")
                                        .contactTitle("Owner")
                                        .build())))
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
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"VendorNumber\":\"1234\",\"Name1\":\"Herman's Coatings\",\"Name2\":\"Herman's Coating Supply Company, LLC\",\"EIN\":\"123456789\",\"Phone\":\"212-555-1234\",\"Email\":\"example@email.com\",\"RemitEmail\":null,\"Address1\":\"123 Ocean Drive\",\"Address2\":\"Suite 400\",\"City\":\"Bristol\",\"State\":\"GA\",\"Zip\":\"31113\",\"Country\":\"US\",\"Mcc\":\"7777\",\"LocationCode\":\"LOC123\",\"Contacts\":{\"ContactEmail\":\"eric@martinezcoatings.com\",\"ContactName\":\"Eric Martinez\",\"ContactPhone\":\"5555555555\",\"ContactTitle\":\"Owner\"},\"BillingData\":{\"id\":123456,\"accountId\":\"bank-account-001\",\"nickname\":\"Main Checking Account\",\"bankName\":\"Example Bank\",\"routingAccount\":\"123456789\",\"accountNumber\":\"9876543210\",\"typeAccount\":\"Checking\",\"bankAccountHolderName\":\"John Doe\",\"bankAccountHolderType\":\"Business\",\"bankAccountFunction\":2,\"verified\":true,\"status\":1,\"services\":[],\"default\":true},\"PaymentMethod\":null,\"VendorStatus\":1,\"VendorId\":1,\"EnrollmentStatus\":null,\"Summary\":{\"ActiveBills\":2,\"PendingBills\":4,\"InTransitBills\":3,\"PaidBills\":18,\"OverdueBills\":1,\"ApprovedBills\":5,\"DisapprovedBills\":1,\"TotalBills\":34,\"ActiveBillsAmount\":1250.75,\"PendingBillsAmount\":2890.5,\"InTransitBillsAmount\":1675.25,\"PaidBillsAmount\":15420.8,\"OverdueBillsAmount\":425,\"ApprovedBillsAmount\":3240.9,\"DisapprovedBillsAmount\":180,\"TotalBillsAmount\":25083.2},\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"ParentOrgName\":\"PropertyManager Pro\",\"ParentOrgId\":1000,\"CreatedDate\":\"2022-07-01T15:00:01Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"remitAddress1\":\"123 Walnut Street\",\"remitAddress2\":\"Suite 900\",\"remitCity\":\"Miami\",\"remitState\":\"FL\",\"remitZip\":\"31113\",\"remitCountry\":\"US\",\"payeeName1\":\"payeeName1\",\"payeeName2\":\"payeeName2\",\"customField1\":\"\",\"customField2\":\"\",\"customerVendorAccount\":\"123-456\",\"InternalReferenceId\":1000000,\"additionalData\":null,\"externalPaypointID\":\"Paypoint-100\",\"StoredMethods\":[]}"));
        VendorQueryRecord response = client.vendor().getVendor(1);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"VendorNumber\": \"1234\",\n"
                + "  \"Name1\": \"Herman's Coatings\",\n"
                + "  \"Name2\": \"Herman's Coating Supply Company, LLC\",\n"
                + "  \"EIN\": \"123456789\",\n"
                + "  \"Phone\": \"212-555-1234\",\n"
                + "  \"Email\": \"example@email.com\",\n"
                + "  \"RemitEmail\": null,\n"
                + "  \"Address1\": \"123 Ocean Drive\",\n"
                + "  \"Address2\": \"Suite 400\",\n"
                + "  \"City\": \"Bristol\",\n"
                + "  \"State\": \"GA\",\n"
                + "  \"Zip\": \"31113\",\n"
                + "  \"Country\": \"US\",\n"
                + "  \"Mcc\": \"7777\",\n"
                + "  \"LocationCode\": \"LOC123\",\n"
                + "  \"Contacts\": {\n"
                + "    \"ContactEmail\": \"eric@martinezcoatings.com\",\n"
                + "    \"ContactName\": \"Eric Martinez\",\n"
                + "    \"ContactPhone\": \"5555555555\",\n"
                + "    \"ContactTitle\": \"Owner\"\n"
                + "  },\n"
                + "  \"BillingData\": {\n"
                + "    \"id\": 123456,\n"
                + "    \"accountId\": \"bank-account-001\",\n"
                + "    \"nickname\": \"Main Checking Account\",\n"
                + "    \"bankName\": \"Example Bank\",\n"
                + "    \"routingAccount\": \"123456789\",\n"
                + "    \"accountNumber\": \"9876543210\",\n"
                + "    \"typeAccount\": \"Checking\",\n"
                + "    \"bankAccountHolderName\": \"John Doe\",\n"
                + "    \"bankAccountHolderType\": \"Business\",\n"
                + "    \"bankAccountFunction\": 2,\n"
                + "    \"verified\": true,\n"
                + "    \"status\": 1,\n"
                + "    \"services\": [],\n"
                + "    \"default\": true\n"
                + "  },\n"
                + "  \"PaymentMethod\": null,\n"
                + "  \"VendorStatus\": 1,\n"
                + "  \"VendorId\": 1,\n"
                + "  \"EnrollmentStatus\": null,\n"
                + "  \"Summary\": {\n"
                + "    \"ActiveBills\": 2,\n"
                + "    \"PendingBills\": 4,\n"
                + "    \"InTransitBills\": 3,\n"
                + "    \"PaidBills\": 18,\n"
                + "    \"OverdueBills\": 1,\n"
                + "    \"ApprovedBills\": 5,\n"
                + "    \"DisapprovedBills\": 1,\n"
                + "    \"TotalBills\": 34,\n"
                + "    \"ActiveBillsAmount\": 1250.75,\n"
                + "    \"PendingBillsAmount\": 2890.5,\n"
                + "    \"InTransitBillsAmount\": 1675.25,\n"
                + "    \"PaidBillsAmount\": 15420.8,\n"
                + "    \"OverdueBillsAmount\": 425,\n"
                + "    \"ApprovedBillsAmount\": 3240.9,\n"
                + "    \"DisapprovedBillsAmount\": 180,\n"
                + "    \"TotalBillsAmount\": 25083.2\n"
                + "  },\n"
                + "  \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "  \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "  \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "  \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "  \"ParentOrgId\": 1000,\n"
                + "  \"CreatedDate\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"remitAddress1\": \"123 Walnut Street\",\n"
                + "  \"remitAddress2\": \"Suite 900\",\n"
                + "  \"remitCity\": \"Miami\",\n"
                + "  \"remitState\": \"FL\",\n"
                + "  \"remitZip\": \"31113\",\n"
                + "  \"remitCountry\": \"US\",\n"
                + "  \"payeeName1\": \"payeeName1\",\n"
                + "  \"payeeName2\": \"payeeName2\",\n"
                + "  \"customField1\": \"\",\n"
                + "  \"customField2\": \"\",\n"
                + "  \"customerVendorAccount\": \"123-456\",\n"
                + "  \"InternalReferenceId\": 1000000,\n"
                + "  \"additionalData\": null,\n"
                + "  \"externalPaypointID\": \"Paypoint-100\",\n"
                + "  \"StoredMethods\": []\n"
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
