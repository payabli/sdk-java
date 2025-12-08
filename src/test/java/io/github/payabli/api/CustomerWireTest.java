package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.customer.requests.AddCustomerRequest;
import io.github.payabli.api.types.CustomerData;
import io.github.payabli.api.types.CustomerQueryRecords;
import io.github.payabli.api.types.PayabliApiResponse00Responsedatanonobject;
import io.github.payabli.api.types.PayabliApiResponseCustomerQuery;
import java.util.Arrays;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerWireTest {
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
    public void testAddCustomer() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"customerId\":17264,\"customerNumber\":\"12356ACB\",\"customerUsername\":null,\"customerStatus\":0,\"Company\":null,\"Firstname\":\"Irene\",\"Lastname\":\"Canizales\",\"Phone\":null,\"Email\":\"irene@canizalesconcrete.com\",\"Address\":null,\"Address1\":\"123 Bishop's Trail\",\"City\":\"Mountain City\",\"State\":\"TN\",\"Zip\":\"37612\",\"Country\":\"US\",\"ShippingAddress\":null,\"ShippingAddress1\":null,\"ShippingCity\":null,\"ShippingState\":null,\"ShippingZip\":null,\"ShippingCountry\":null,\"Balance\":0,\"TimeZone\":-5,\"MFA\":false,\"MFAMode\":0,\"snProvider\":null,\"snIdentifier\":null,\"snData\":null,\"LastUpdated\":\"2024-03-13T12:49:56Z\",\"Created\":\"2024-03-13T12:49:56Z\",\"AdditionalFields\":{\"key\":\"value\"},\"IdentifierFields\":[\"email\"],\"Subscriptions\":null,\"StoredMethods\":null,\"customerSummary\":{\"numberofTransactions\":30,\"recentTransactions\":[{\"EntrypageId\":0,\"FeeAmount\":1,\"PayorId\":1551,\"PaypointId\":226,\"SettlementStatus\":2,\"TotalAmount\":30.22,\"TransStatus\":1}],\"totalAmountTransactions\":1500,\"totalNetAmountTransactions\":1500},\"PaypointLegalname\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointDbaname\":\"Gruzya Adventure Outfitters\",\"ParentOrgName\":\"The Pilgrim Planner\",\"ParentOrgId\":123,\"PaypointEntryname\":\"41035afaa7\",\"pageidentifier\":\"null\",\"externalPaypointID\":null,\"customerConsent\":null},\"responseText\":\"Success\"}"));
        PayabliApiResponseCustomerQuery response = client.customer()
                .addCustomer(
                        "8cfec329267",
                        AddCustomerRequest.builder()
                                .body(CustomerData.builder()
                                        .customerNumber("12356ACB")
                                        .firstname("Irene")
                                        .lastname("Canizales")
                                        .email("irene@canizalesconcrete.com")
                                        .address1("123 Bishop's Trail")
                                        .city("Mountain City")
                                        .state("TN")
                                        .zip("37612")
                                        .country("US")
                                        .timeZone(-5)
                                        .identifierFields(Arrays.asList(Optional.of("email")))
                                        .build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"customerNumber\": \"12356ACB\",\n"
                + "  \"firstname\": \"Irene\",\n"
                + "  \"lastname\": \"Canizales\",\n"
                + "  \"address1\": \"123 Bishop's Trail\",\n"
                + "  \"city\": \"Mountain City\",\n"
                + "  \"state\": \"TN\",\n"
                + "  \"zip\": \"37612\",\n"
                + "  \"country\": \"US\",\n"
                + "  \"email\": \"irene@canizalesconcrete.com\",\n"
                + "  \"identifierFields\": [\n"
                + "    \"email\"\n"
                + "  ],\n"
                + "  \"timeZone\": -5\n"
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
                + "    \"customerId\": 17264,\n"
                + "    \"customerNumber\": \"12356ACB\",\n"
                + "    \"customerUsername\": null,\n"
                + "    \"customerStatus\": 0,\n"
                + "    \"Company\": null,\n"
                + "    \"Firstname\": \"Irene\",\n"
                + "    \"Lastname\": \"Canizales\",\n"
                + "    \"Phone\": null,\n"
                + "    \"Email\": \"irene@canizalesconcrete.com\",\n"
                + "    \"Address\": null,\n"
                + "    \"Address1\": \"123 Bishop's Trail\",\n"
                + "    \"City\": \"Mountain City\",\n"
                + "    \"State\": \"TN\",\n"
                + "    \"Zip\": \"37612\",\n"
                + "    \"Country\": \"US\",\n"
                + "    \"ShippingAddress\": null,\n"
                + "    \"ShippingAddress1\": null,\n"
                + "    \"ShippingCity\": null,\n"
                + "    \"ShippingState\": null,\n"
                + "    \"ShippingZip\": null,\n"
                + "    \"ShippingCountry\": null,\n"
                + "    \"Balance\": 0,\n"
                + "    \"TimeZone\": -5,\n"
                + "    \"MFA\": false,\n"
                + "    \"MFAMode\": 0,\n"
                + "    \"snProvider\": null,\n"
                + "    \"snIdentifier\": null,\n"
                + "    \"snData\": null,\n"
                + "    \"LastUpdated\": \"2024-03-13T12:49:56Z\",\n"
                + "    \"Created\": \"2024-03-13T12:49:56Z\",\n"
                + "    \"AdditionalFields\": {\n"
                + "      \"key\": \"value\"\n"
                + "    },\n"
                + "    \"IdentifierFields\": [\n"
                + "      \"email\"\n"
                + "    ],\n"
                + "    \"Subscriptions\": null,\n"
                + "    \"StoredMethods\": null,\n"
                + "    \"customerSummary\": {\n"
                + "      \"numberofTransactions\": 30,\n"
                + "      \"recentTransactions\": [\n"
                + "        {\n"
                + "          \"EntrypageId\": 0,\n"
                + "          \"FeeAmount\": 1,\n"
                + "          \"PayorId\": 1551,\n"
                + "          \"PaypointId\": 226,\n"
                + "          \"SettlementStatus\": 2,\n"
                + "          \"TotalAmount\": 30.22,\n"
                + "          \"TransStatus\": 1\n"
                + "        }\n"
                + "      ],\n"
                + "      \"totalAmountTransactions\": 1500,\n"
                + "      \"totalNetAmountTransactions\": 1500\n"
                + "    },\n"
                + "    \"PaypointLegalname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "    \"PaypointDbaname\": \"Gruzya Adventure Outfitters\",\n"
                + "    \"ParentOrgName\": \"The Pilgrim Planner\",\n"
                + "    \"ParentOrgId\": 123,\n"
                + "    \"PaypointEntryname\": \"41035afaa7\",\n"
                + "    \"pageidentifier\": \"null\",\n"
                + "    \"externalPaypointID\": null,\n"
                + "    \"customerConsent\": null\n"
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
    public void testDeleteCustomer() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"isSuccess\":true,\"pageIdentifier\":\"null\",\"roomId\":0,\"responseData\":\" \",\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.customer().deleteCustomer(998);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"responseCode\": 1,\n"
                + "  \"isSuccess\": true,\n"
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"roomId\": 0,\n"
                + "  \"responseData\": \" \",\n"
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
    public void testGetCustomer() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"customerId\":4440,\"customerNumber\":\"3456-7645A\",\"customerUsername\":\"myusername\",\"customerStatus\":1,\"Company\":\"AA LLC\",\"Firstname\":\"John\",\"Lastname\":\"Smith\",\"Phone\":\"1234567890\",\"Email\":\"example@email.com\",\"Address\":\"3245 Main St\",\"Address1\":\"STE 900\",\"City\":\"Miami\",\"State\":\"FL\",\"Zip\":\"77777\",\"Country\":\"US\",\"ShippingAddress\":\"123 Walnut St\",\"ShippingAddress1\":\"STE 900\",\"ShippingCity\":\"Johnson City\",\"ShippingState\":\"TN\",\"ShippingZip\":\"37619\",\"ShippingCountry\":\"US\",\"Balance\":1.1,\"TimeZone\":-5,\"MFA\":false,\"MFAMode\":0,\"snProvider\":\"facebook\",\"snIdentifier\":\"6677fgttyudd999\",\"snData\":\"\",\"LastUpdated\":\"2021-06-16T05:00:00Z\",\"Created\":\"2021-06-10T05:00:00Z\",\"AdditionalFields\":{\"property1\":\"string\",\"property2\":\"string\"},\"IdentifierFields\":[\"email\"],\"Subscriptions\":[{\"CreatedAt\":\"2022-07-01T15:00:01Z\",\"EndDate\":\"2025-10-19T00:00:00Z\",\"EntrypageId\":0,\"ExternalPaypointID\":\"Paypoint-100\",\"FeeAmount\":3,\"Frequency\":\"monthly\",\"IdSub\":396,\"LastRun\":\"2025-10-19T00:00:00Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"LeftCycles\":15,\"Method\":\"card\",\"NetAmount\":3762.87,\"NextDate\":\"2025-10-19T00:00:00Z\",\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"paymentDetails\":{\"totalAmount\":100}},\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"PaypointId\":255,\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PlanId\":0,\"Source\":\"api\",\"StartDate\":\"2025-10-19T00:00:00Z\",\"SubEvents\":[{\"description\":\"TransferCreated\",\"eventTime\":\"2023-07-05T22:31:06Z\"}],\"SubStatus\":1,\"TotalAmount\":103,\"TotalCycles\":24,\"UntilCancelled\":true}],\"StoredMethods\":[{\"bin\":\"411111\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"descriptor\":\"visa\",\"expDate\":\"1227\",\"holderName\":\"Chad Mercia\",\"idPmethod\":\"6edcbb56-9c0e-4003-b3d1-99abf149ba0e\",\"lastUpdated\":\"2022-07-01T15:00:01Z\",\"maskedAccount\":\"4XXXXXXXX1111\",\"method\":\"card\"}],\"customerSummary\":{\"numberofTransactions\":30,\"recentTransactions\":[{\"EntrypageId\":0,\"FeeAmount\":1,\"PayorId\":1551,\"PaypointId\":226,\"SettlementStatus\":2,\"TotalAmount\":30.22,\"TransStatus\":1}],\"totalAmountTransactions\":1500,\"totalNetAmountTransactions\":1500},\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PaypointDbaname\":\"Sunshine Gutters\",\"ParentOrgName\":\"PropertyManager Pro\",\"ParentOrgId\":123,\"PaypointEntryname\":\"d193cf9a46\",\"pageidentifier\":\"null\",\"externalPaypointID\":\"Paypoint-100\",\"customerConsent\":{\"eCommunication\":{\"status\":1,\"updatedAt\":\"2022-07-01T15:00:01Z\"},\"sms\":{\"status\":1,\"updatedAt\":\"2022-07-01T15:00:01Z\"}}}"));
        CustomerQueryRecords response = client.customer().getCustomer(998);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"customerId\": 4440,\n"
                + "  \"customerNumber\": \"3456-7645A\",\n"
                + "  \"customerUsername\": \"myusername\",\n"
                + "  \"customerStatus\": 1,\n"
                + "  \"Company\": \"AA LLC\",\n"
                + "  \"Firstname\": \"John\",\n"
                + "  \"Lastname\": \"Smith\",\n"
                + "  \"Phone\": \"1234567890\",\n"
                + "  \"Email\": \"example@email.com\",\n"
                + "  \"Address\": \"3245 Main St\",\n"
                + "  \"Address1\": \"STE 900\",\n"
                + "  \"City\": \"Miami\",\n"
                + "  \"State\": \"FL\",\n"
                + "  \"Zip\": \"77777\",\n"
                + "  \"Country\": \"US\",\n"
                + "  \"ShippingAddress\": \"123 Walnut St\",\n"
                + "  \"ShippingAddress1\": \"STE 900\",\n"
                + "  \"ShippingCity\": \"Johnson City\",\n"
                + "  \"ShippingState\": \"TN\",\n"
                + "  \"ShippingZip\": \"37619\",\n"
                + "  \"ShippingCountry\": \"US\",\n"
                + "  \"Balance\": 1.1,\n"
                + "  \"TimeZone\": -5,\n"
                + "  \"MFA\": false,\n"
                + "  \"MFAMode\": 0,\n"
                + "  \"snProvider\": \"facebook\",\n"
                + "  \"snIdentifier\": \"6677fgttyudd999\",\n"
                + "  \"snData\": \"\",\n"
                + "  \"LastUpdated\": \"2021-06-16T05:00:00Z\",\n"
                + "  \"Created\": \"2021-06-10T05:00:00Z\",\n"
                + "  \"AdditionalFields\": {\n"
                + "    \"property1\": \"string\",\n"
                + "    \"property2\": \"string\"\n"
                + "  },\n"
                + "  \"IdentifierFields\": [\n"
                + "    \"email\"\n"
                + "  ],\n"
                + "  \"Subscriptions\": [\n"
                + "    {\n"
                + "      \"CreatedAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"EndDate\": \"2025-10-19T00:00:00Z\",\n"
                + "      \"EntrypageId\": 0,\n"
                + "      \"ExternalPaypointID\": \"Paypoint-100\",\n"
                + "      \"FeeAmount\": 3,\n"
                + "      \"Frequency\": \"monthly\",\n"
                + "      \"IdSub\": 396,\n"
                + "      \"LastRun\": \"2025-10-19T00:00:00Z\",\n"
                + "      \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"LeftCycles\": 15,\n"
                + "      \"Method\": \"card\",\n"
                + "      \"NetAmount\": 3762.87,\n"
                + "      \"NextDate\": \"2025-10-19T00:00:00Z\",\n"
                + "      \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"paymentDetails\": {\n"
                + "          \"totalAmount\": 100\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "      \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "      \"PaypointId\": 255,\n"
                + "      \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "      \"PlanId\": 0,\n"
                + "      \"Source\": \"api\",\n"
                + "      \"StartDate\": \"2025-10-19T00:00:00Z\",\n"
                + "      \"SubEvents\": [\n"
                + "        {\n"
                + "          \"description\": \"TransferCreated\",\n"
                + "          \"eventTime\": \"2023-07-05T22:31:06Z\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"SubStatus\": 1,\n"
                + "      \"TotalAmount\": 103,\n"
                + "      \"TotalCycles\": 24,\n"
                + "      \"UntilCancelled\": true\n"
                + "    }\n"
                + "  ],\n"
                + "  \"StoredMethods\": [\n"
                + "    {\n"
                + "      \"bin\": \"411111\",\n"
                + "      \"binData\": {\n"
                + "        \"binMatchedLength\": \"6\",\n"
                + "        \"binCardBrand\": \"Visa\",\n"
                + "        \"binCardType\": \"Credit\",\n"
                + "        \"binCardCategory\": \"PLATINUM\",\n"
                + "        \"binCardIssuer\": \"Bank of Example\",\n"
                + "        \"binCardIssuerCountry\": \"United States\",\n"
                + "        \"binCardIssuerCountryCodeA2\": \"US\",\n"
                + "        \"binCardIssuerCountryNumber\": \"840\",\n"
                + "        \"binCardIsRegulated\": \"false\",\n"
                + "        \"binCardUseCategory\": \"Consumer\",\n"
                + "        \"binCardIssuerCountryCodeA3\": \"USA\"\n"
                + "      },\n"
                + "      \"descriptor\": \"visa\",\n"
                + "      \"expDate\": \"1227\",\n"
                + "      \"holderName\": \"Chad Mercia\",\n"
                + "      \"idPmethod\": \"6edcbb56-9c0e-4003-b3d1-99abf149ba0e\",\n"
                + "      \"lastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"maskedAccount\": \"4XXXXXXXX1111\",\n"
                + "      \"method\": \"card\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"customerSummary\": {\n"
                + "    \"numberofTransactions\": 30,\n"
                + "    \"recentTransactions\": [\n"
                + "      {\n"
                + "        \"EntrypageId\": 0,\n"
                + "        \"FeeAmount\": 1,\n"
                + "        \"PayorId\": 1551,\n"
                + "        \"PaypointId\": 226,\n"
                + "        \"SettlementStatus\": 2,\n"
                + "        \"TotalAmount\": 30.22,\n"
                + "        \"TransStatus\": 1\n"
                + "      }\n"
                + "    ],\n"
                + "    \"totalAmountTransactions\": 1500,\n"
                + "    \"totalNetAmountTransactions\": 1500\n"
                + "  },\n"
                + "  \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "  \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "  \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "  \"ParentOrgId\": 123,\n"
                + "  \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "  \"pageidentifier\": \"null\",\n"
                + "  \"externalPaypointID\": \"Paypoint-100\",\n"
                + "  \"customerConsent\": {\n"
                + "    \"eCommunication\": {\n"
                + "      \"status\": 1,\n"
                + "      \"updatedAt\": \"2022-07-01T15:00:01Z\"\n"
                + "    },\n"
                + "    \"sms\": {\n"
                + "      \"status\": 1,\n"
                + "      \"updatedAt\": \"2022-07-01T15:00:01Z\"\n"
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
    public void testLinkCustomerTransaction() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":\"null\",\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":\" \"}"));
        PayabliApiResponse00Responsedatanonobject response =
                client.customer().linkCustomerTransaction(998, "45-as456777hhhhhhhhhh77777777-324");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"responseCode\": 1,\n"
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"roomId\": 0,\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"responseData\": \" \"\n"
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
    public void testRequestConsent() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseCode\":1,\"responseData\":\" \",\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.customer().requestConsent(998);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": \" \",\n"
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
    public void testUpdateCustomer() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(
                        "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":\" \",\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.customer()
                .updateCustomer(
                        998,
                        CustomerData.builder()
                                .firstname("Irene")
                                .lastname("Canizales")
                                .address1("145 Bishop's Trail")
                                .city("Mountain City")
                                .state("TN")
                                .zip("37612")
                                .country("US")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"firstname\": \"Irene\",\n"
                + "  \"lastname\": \"Canizales\",\n"
                + "  \"address1\": \"145 Bishop's Trail\",\n"
                + "  \"city\": \"Mountain City\",\n"
                + "  \"state\": \"TN\",\n"
                + "  \"zip\": \"37612\",\n"
                + "  \"country\": \"US\"\n"
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
                + "  \"responseData\": \" \",\n"
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
