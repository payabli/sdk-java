package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.subscription.requests.RequestSchedule;
import io.github.payabli.api.resources.subscription.requests.RequestUpdateSchedule;
import io.github.payabli.api.resources.subscription.types.AddSubscriptionResponse;
import io.github.payabli.api.resources.subscription.types.RemoveSubscriptionResponse;
import io.github.payabli.api.resources.subscription.types.RequestSchedulePaymentMethod;
import io.github.payabli.api.resources.subscription.types.SubscriptionRequestBody;
import io.github.payabli.api.resources.subscription.types.UpdateSubscriptionResponse;
import io.github.payabli.api.types.Frequency;
import io.github.payabli.api.types.PayMethodCredit;
import io.github.payabli.api.types.PaymentDetail;
import io.github.payabli.api.types.PayorDataRequest;
import io.github.payabli.api.types.ScheduleDetail;
import io.github.payabli.api.types.SubscriptionQueryRecords;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscriptionWireTest {
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
    public void testGetSubscription() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"CreatedAt\":\"2022-07-01T15:00:01Z\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"1111 West 1st Street\",\"BillingAddress2\":\"Suite 200\",\"BillingCity\":\"Miami\",\"BillingCountry\":\"US\",\"BillingEmail\":\"example@email.com\",\"BillingPhone\":\"5555555555\",\"BillingState\":\"FL\",\"BillingZip\":\"45567\",\"CompanyName\":\"Sunshine LLC\",\"customerId\":4440,\"CustomerNumber\":\"3456-7645A\",\"customerStatus\":1,\"FirstName\":\"John\",\"Identifiers\":[\"\\\\\\\"firstname\\\\\\\"\",\"\\\\\\\"lastname\\\\\\\"\",\"\\\\\\\"email\\\\\\\"\",\"\\\\\\\"customId\\\\\\\"\"],\"LastName\":\"Doe\",\"ShippingAddress1\":\"123 Walnut St\",\"ShippingAddress2\":\"STE 900\",\"ShippingCity\":\"Johnson City\",\"ShippingCountry\":\"US\",\"ShippingState\":\"TN\",\"ShippingZip\":\"37619\"},\"EndDate\":\"2025-10-19T00:00:00Z\",\"EntrypageId\":0,\"ExternalPaypointID\":\"Paypoint-100\",\"FeeAmount\":3,\"Frequency\":\"monthly\",\"IdSub\":396,\"InvoiceData\":{\"AdditionalData\":\"AdditionalData\",\"attachments\":[{}],\"company\":\"ACME, INC\",\"discount\":10,\"dutyAmount\":0,\"firstName\":\"Chad\",\"freightAmount\":10,\"frequency\":\"one-time\",\"invoiceAmount\":105,\"invoiceDate\":\"2025-07-01\",\"invoiceDueDate\":\"2025-07-01\",\"invoiceEndDate\":\"2025-07-01\",\"invoiceNumber\":\"INV-2345\",\"invoiceStatus\":1,\"invoiceType\":0,\"items\":[{\"itemCost\":5,\"itemProductName\":\"Materials deposit\",\"itemQty\":1}],\"lastName\":\"Mercia\",\"notes\":\"Example notes.\",\"paymentTerms\":\"PIA\",\"purchaseOrder\":\"PO-345\",\"shippingAddress1\":\"123 Walnut St\",\"shippingAddress2\":\"STE 900\",\"shippingCity\":\"Johnson City\",\"shippingCountry\":\"US\",\"shippingEmail\":\"example@email.com\",\"shippingFromZip\":\"30040\",\"shippingPhone\":\"5555555555\",\"shippingState\":\"TN\",\"shippingZip\":\"37619\",\"summaryCommodityCode\":\"501718\",\"tax\":2.05,\"termsConditions\":\"Must be paid before work scheduled.\"},\"LastRun\":\"2025-10-19T00:00:00Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"LeftCycles\":15,\"Method\":\"card\",\"NetAmount\":3762.87,\"NextDate\":\"2025-10-19T00:00:00Z\",\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"AccountExp\":\"11/29\",\"accountId\":\"accountId\",\"AccountType\":\"visa\",\"AccountZip\":\"90210\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"HolderName\":\"Chad Mercia\",\"Initiator\":\"payor\",\"MaskedAccount\":\"4XXXXXXXX1111\",\"orderDescription\":\"Depost for materials for 123 Walnut St\",\"paymentDetails\":{\"categories\":[{\"amount\":1000,\"label\":\"Deposit\"}],\"checkImage\":{\"key\":\"value\"},\"checkNumber\":\"107\",\"currency\":\"USD\",\"serviceFee\":0,\"splitFunding\":[{}],\"totalAmount\":100},\"Sequence\":\"subsequent\",\"SignatureData\":\"SignatureData\",\"StoredId\":\"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\"StoredMethodUsageType\":\"subscription\"},\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"PaypointId\":255,\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PlanId\":0,\"Source\":\"api\",\"StartDate\":\"2025-10-19T00:00:00Z\",\"SubEvents\":[{\"description\":\"TransferCreated\",\"eventTime\":\"2023-07-05T22:31:06Z\",\"extraData\":{\"key\":\"value\"},\"refData\":\"refData\",\"source\":\"api\"}],\"SubStatus\":1,\"TotalAmount\":103,\"TotalCycles\":24,\"UntilCancelled\":true}"));
        SubscriptionQueryRecords response = client.subscription().getSubscription(263);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"CreatedAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"Customer\": {\n"
                + "    \"AdditionalData\": \"AdditionalData\",\n"
                + "    \"BillingAddress1\": \"1111 West 1st Street\",\n"
                + "    \"BillingAddress2\": \"Suite 200\",\n"
                + "    \"BillingCity\": \"Miami\",\n"
                + "    \"BillingCountry\": \"US\",\n"
                + "    \"BillingEmail\": \"example@email.com\",\n"
                + "    \"BillingPhone\": \"5555555555\",\n"
                + "    \"BillingState\": \"FL\",\n"
                + "    \"BillingZip\": \"45567\",\n"
                + "    \"CompanyName\": \"Sunshine LLC\",\n"
                + "    \"customerId\": 4440,\n"
                + "    \"CustomerNumber\": \"3456-7645A\",\n"
                + "    \"customerStatus\": 1,\n"
                + "    \"FirstName\": \"John\",\n"
                + "    \"Identifiers\": [\n"
                + "      \"\\\\\\\"firstname\\\\\\\"\",\n"
                + "      \"\\\\\\\"lastname\\\\\\\"\",\n"
                + "      \"\\\\\\\"email\\\\\\\"\",\n"
                + "      \"\\\\\\\"customId\\\\\\\"\"\n"
                + "    ],\n"
                + "    \"LastName\": \"Doe\",\n"
                + "    \"ShippingAddress1\": \"123 Walnut St\",\n"
                + "    \"ShippingAddress2\": \"STE 900\",\n"
                + "    \"ShippingCity\": \"Johnson City\",\n"
                + "    \"ShippingCountry\": \"US\",\n"
                + "    \"ShippingState\": \"TN\",\n"
                + "    \"ShippingZip\": \"37619\"\n"
                + "  },\n"
                + "  \"EndDate\": \"2025-10-19T00:00:00Z\",\n"
                + "  \"EntrypageId\": 0,\n"
                + "  \"ExternalPaypointID\": \"Paypoint-100\",\n"
                + "  \"FeeAmount\": 3,\n"
                + "  \"Frequency\": \"monthly\",\n"
                + "  \"IdSub\": 396,\n"
                + "  \"InvoiceData\": {\n"
                + "    \"AdditionalData\": \"AdditionalData\",\n"
                + "    \"attachments\": [\n"
                + "      {}\n"
                + "    ],\n"
                + "    \"company\": \"ACME, INC\",\n"
                + "    \"discount\": 10,\n"
                + "    \"dutyAmount\": 0,\n"
                + "    \"firstName\": \"Chad\",\n"
                + "    \"freightAmount\": 10,\n"
                + "    \"frequency\": \"one-time\",\n"
                + "    \"invoiceAmount\": 105,\n"
                + "    \"invoiceDate\": \"2025-07-01\",\n"
                + "    \"invoiceDueDate\": \"2025-07-01\",\n"
                + "    \"invoiceEndDate\": \"2025-07-01\",\n"
                + "    \"invoiceNumber\": \"INV-2345\",\n"
                + "    \"invoiceStatus\": 1,\n"
                + "    \"invoiceType\": 0,\n"
                + "    \"items\": [\n"
                + "      {\n"
                + "        \"itemCost\": 5,\n"
                + "        \"itemProductName\": \"Materials deposit\",\n"
                + "        \"itemQty\": 1\n"
                + "      }\n"
                + "    ],\n"
                + "    \"lastName\": \"Mercia\",\n"
                + "    \"notes\": \"Example notes.\",\n"
                + "    \"paymentTerms\": \"PIA\",\n"
                + "    \"purchaseOrder\": \"PO-345\",\n"
                + "    \"shippingAddress1\": \"123 Walnut St\",\n"
                + "    \"shippingAddress2\": \"STE 900\",\n"
                + "    \"shippingCity\": \"Johnson City\",\n"
                + "    \"shippingCountry\": \"US\",\n"
                + "    \"shippingEmail\": \"example@email.com\",\n"
                + "    \"shippingFromZip\": \"30040\",\n"
                + "    \"shippingPhone\": \"5555555555\",\n"
                + "    \"shippingState\": \"TN\",\n"
                + "    \"shippingZip\": \"37619\",\n"
                + "    \"summaryCommodityCode\": \"501718\",\n"
                + "    \"tax\": 2.05,\n"
                + "    \"termsConditions\": \"Must be paid before work scheduled.\"\n"
                + "  },\n"
                + "  \"LastRun\": \"2025-10-19T00:00:00Z\",\n"
                + "  \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"LeftCycles\": 15,\n"
                + "  \"Method\": \"card\",\n"
                + "  \"NetAmount\": 3762.87,\n"
                + "  \"NextDate\": \"2025-10-19T00:00:00Z\",\n"
                + "  \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "  \"PaymentData\": {\n"
                + "    \"AccountExp\": \"11/29\",\n"
                + "    \"accountId\": \"accountId\",\n"
                + "    \"AccountType\": \"visa\",\n"
                + "    \"AccountZip\": \"90210\",\n"
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
                + "    \"HolderName\": \"Chad Mercia\",\n"
                + "    \"Initiator\": \"payor\",\n"
                + "    \"MaskedAccount\": \"4XXXXXXXX1111\",\n"
                + "    \"orderDescription\": \"Depost for materials for 123 Walnut St\",\n"
                + "    \"paymentDetails\": {\n"
                + "      \"categories\": [\n"
                + "        {\n"
                + "          \"amount\": 1000,\n"
                + "          \"label\": \"Deposit\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"checkImage\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"checkNumber\": \"107\",\n"
                + "      \"currency\": \"USD\",\n"
                + "      \"serviceFee\": 0,\n"
                + "      \"splitFunding\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"totalAmount\": 100\n"
                + "    },\n"
                + "    \"Sequence\": \"subsequent\",\n"
                + "    \"SignatureData\": \"SignatureData\",\n"
                + "    \"StoredId\": \"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\n"
                + "    \"StoredMethodUsageType\": \"subscription\"\n"
                + "  },\n"
                + "  \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "  \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "  \"PaypointId\": 255,\n"
                + "  \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "  \"PlanId\": 0,\n"
                + "  \"Source\": \"api\",\n"
                + "  \"StartDate\": \"2025-10-19T00:00:00Z\",\n"
                + "  \"SubEvents\": [\n"
                + "    {\n"
                + "      \"description\": \"TransferCreated\",\n"
                + "      \"eventTime\": \"2023-07-05T22:31:06Z\",\n"
                + "      \"extraData\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"refData\": \"refData\",\n"
                + "      \"source\": \"api\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"SubStatus\": 1,\n"
                + "  \"TotalAmount\": 103,\n"
                + "  \"TotalCycles\": 24,\n"
                + "  \"UntilCancelled\": true\n"
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
    public void testNewSubscription() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"responseText\":\"Success\",\"isSuccess\":true,\"responseData\":396,\"customerId\":4440}"));
        AddSubscriptionResponse response = client.subscription()
                .newSubscription(RequestSchedule.builder()
                        .body(SubscriptionRequestBody.builder()
                                .customerData(PayorDataRequest.builder()
                                        .customerId(4440L)
                                        .build())
                                .entryPoint("f743aed24a")
                                .paymentDetails(PaymentDetail.builder()
                                        .totalAmount(100.0)
                                        .serviceFee(0.0)
                                        .build())
                                .paymentMethod(RequestSchedulePaymentMethod.of(PayMethodCredit.builder()
                                        .cardexp("02/25")
                                        .cardnumber("4111111111111111")
                                        .method("card")
                                        .cardcvv("123")
                                        .cardHolder("John Cassian")
                                        .cardzip("37615")
                                        .initiator("payor")
                                        .build()))
                                .scheduleDetails(ScheduleDetail.builder()
                                        .endDate("03-20-2025")
                                        .frequency(Frequency.WEEKLY)
                                        .planId(1)
                                        .startDate("09-20-2024")
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
                + "  \"customerData\": {\n"
                + "    \"customerId\": 4440\n"
                + "  },\n"
                + "  \"entryPoint\": \"f743aed24a\",\n"
                + "  \"paymentDetails\": {\n"
                + "    \"serviceFee\": 0,\n"
                + "    \"totalAmount\": 100\n"
                + "  },\n"
                + "  \"paymentMethod\": {\n"
                + "    \"cardcvv\": \"123\",\n"
                + "    \"cardexp\": \"02/25\",\n"
                + "    \"cardHolder\": \"John Cassian\",\n"
                + "    \"cardnumber\": \"4111111111111111\",\n"
                + "    \"cardzip\": \"37615\",\n"
                + "    \"initiator\": \"payor\",\n"
                + "    \"method\": \"card\"\n"
                + "  },\n"
                + "  \"scheduleDetails\": {\n"
                + "    \"endDate\": \"03-20-2025\",\n"
                + "    \"frequency\": \"weekly\",\n"
                + "    \"planId\": 1,\n"
                + "    \"startDate\": \"09-20-2024\"\n"
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
                + "  \"responseData\": 396,\n"
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
    public void testRemoveSubscription() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":\"396\",\"responseText\":\"Success\"}"));
        RemoveSubscriptionResponse response = client.subscription().removeSubscription(396);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": \"396\",\n"
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
    public void testUpdateSubscription() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseText\":\"Success\",\"isSuccess\":true,\"responseData\":\"396 paused\",\"customerId\":4440}"));
        UpdateSubscriptionResponse response = client.subscription()
                .updateSubscription(
                        231, RequestUpdateSchedule.builder().setPause(true).build());
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
                + "  \"responseData\": \"396 paused\",\n"
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
