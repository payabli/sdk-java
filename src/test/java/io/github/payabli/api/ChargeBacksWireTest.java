package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.chargebacks.requests.ResponseChargeBack;
import io.github.payabli.api.resources.chargebacks.types.AddResponseResponse;
import io.github.payabli.api.resources.chargebacks.types.ChargebackQueryRecords;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChargeBacksWireTest {
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
    public void testAddResponse() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":126,\"responseText\":\"Success\"}"));
        AddResponseResponse response = client.chargeBacks()
                .addResponse(
                        1000000L,
                        ResponseChargeBack.builder()
                                .idempotencyKey("6B29FC40-CA47-1067-B31D-00DD010662DA")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());

        // Validate headers
        Assertions.assertEquals(
                "6B29FC40-CA47-1067-B31D-00DD010662DA",
                request.getHeader("idempotencyKey"),
                "Header 'idempotencyKey' should match expected value");
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"responseData\": 126,\n"
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
    public void testGetChargeback() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Id\":201,\"ChargebackDate\":\"2022-06-25T00:00:00Z\",\"CaseNumber\":\"TZ45678\",\"ReasonCode\":\"reasonCode\",\"Reason\":\"Buyer dispute\",\"ReferenceNumber\":\"referenceNumber\",\"LastFour\":\"6789\",\"AccountType\":\"visa\",\"Status\":1,\"Method\":\"card\",\"CreatedAt\":\"2022-07-01T15:00:01Z\",\"ReplyBy\":\"2022-07-15T23:59:59Z\",\"PaymentTransId\":\"226-fe55ec0348e34702bd91b4be198ce7ec\",\"ScheduleReference\":0,\"OrderId\":\"O-5140\",\"NetAmount\":3762.87,\"TransactionTime\":\"2024-01-15T09:30:00Z\",\"Customer\":{\"AdditionalData\":null,\"BillingAddress1\":\"1111 West 1st Street\",\"BillingAddress2\":\"Suite 200\",\"BillingCity\":\"Miami\",\"BillingCountry\":\"US\",\"BillingEmail\":\"example@email.com\",\"BillingPhone\":\"5555555555\",\"BillingState\":\"FL\",\"BillingZip\":\"45567\",\"CompanyName\":\"Sunshine LLC\",\"customerId\":4440,\"CustomerNumber\":\"3456-7645A\",\"customerStatus\":1,\"FirstName\":\"John\",\"Identifiers\":[\"firstname\",\"lastname\",\"email\",\"customId\"],\"LastName\":\"Doe\",\"ShippingAddress1\":\"123 Walnut St\",\"ShippingAddress2\":\"STE 900\",\"ShippingCity\":\"Johnson City\",\"ShippingCountry\":\"US\",\"ShippingState\":\"TN\",\"ShippingZip\":\"37619\"},\"PaymentData\":{\"AccountExp\":\"11/29\",\"accountId\":\"accountId\",\"AccountType\":\"visa\",\"AccountZip\":\"90210\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"HolderName\":\"Chad Mercia\",\"Initiator\":\"payor\",\"MaskedAccount\":\"4XXXXXXXX1111\",\"orderDescription\":\"Depost for materials for 123 Walnut St\",\"paymentDetails\":{\"categories\":[{\"amount\":1000,\"label\":\"Deposit\"}],\"checkImage\":{\"key\":\"value\"},\"checkNumber\":\"107\",\"currency\":\"USD\",\"serviceFee\":0,\"splitFunding\":[{}],\"totalAmount\":100},\"Sequence\":\"subsequent\",\"SignatureData\":\"SignatureData\",\"StoredId\":\"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\"StoredMethodUsageType\":\"subscription\"},\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PaypointDbaname\":\"Sunshine Gutters\",\"ParentOrgName\":\"PropertyManager Pro\",\"ParentOrgId\":123,\"PaypointEntryname\":\"d193cf9a46\",\"Responses\":[{\"contactEmail\":\"example@email.com\",\"contactName\":\"John Doe\",\"createdAt\":\"2022-07-01T15:00:01Z\",\"id\":672,\"notes\":\"any note here\"}],\"Transaction\":{\"AchHolderType\":\"personal\",\"AchSecCode\":\"AchSecCode\",\"BatchAmount\":30.22,\"BatchNumber\":\"batch_226_ach_12-30-2023\",\"CfeeTransactions\":[{\"transactionTime\":\"2024-01-15T09:30:00Z\"}],\"ConnectorName\":\"gp\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"1111 West 1st Street\",\"BillingAddress2\":\"Suite 200\",\"BillingCity\":\"Miami\",\"BillingCountry\":\"US\",\"BillingEmail\":\"example@email.com\",\"BillingPhone\":\"5555555555\",\"BillingState\":\"FL\",\"BillingZip\":\"45567\",\"CompanyName\":\"Sunshine LLC\",\"customerId\":4440,\"CustomerNumber\":\"3456-7645A\",\"customerStatus\":1,\"FirstName\":\"John\",\"Identifiers\":[\"firstname\",\"lastname\",\"email\",\"customId\"],\"LastName\":\"Doe\",\"ShippingAddress1\":\"123 Walnut St\",\"ShippingAddress2\":\"STE 900\",\"ShippingCity\":\"Johnson City\",\"ShippingCountry\":\"US\",\"ShippingState\":\"TN\",\"ShippingZip\":\"37619\"},\"DeviceId\":\"6c361c7d-674c-44cc-b790-382b75d1xxx\",\"EntrypageId\":0,\"ExternalProcessorInformation\":\"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\"FeeAmount\":1,\"GatewayTransId\":\"TRN_xwCAjQorWAYX1nAhAoHZVfN8iYHbI0\",\"InvoiceData\":{\"AdditionalData\":\"AdditionalData\",\"attachments\":[{}],\"company\":\"ACME, INC\",\"discount\":10,\"dutyAmount\":0,\"firstName\":\"Chad\",\"freightAmount\":10,\"frequency\":\"one-time\",\"invoiceAmount\":105,\"invoiceDate\":\"2025-07-01\",\"invoiceDueDate\":\"2025-07-01\",\"invoiceEndDate\":\"2025-07-01\",\"invoiceNumber\":\"INV-2345\",\"invoiceStatus\":1,\"invoiceType\":0,\"items\":[{\"itemCost\":5,\"itemProductName\":\"Materials deposit\",\"itemQty\":1}],\"lastName\":\"Mercia\",\"notes\":\"Example notes.\",\"paymentTerms\":\"PIA\",\"purchaseOrder\":\"PO-345\",\"shippingAddress1\":\"123 Walnut St\",\"shippingAddress2\":\"STE 900\",\"shippingCity\":\"Johnson City\",\"shippingCountry\":\"US\",\"shippingEmail\":\"example@email.com\",\"shippingFromZip\":\"30040\",\"shippingPhone\":\"5555555555\",\"shippingState\":\"TN\",\"shippingZip\":\"37619\",\"summaryCommodityCode\":\"501718\",\"tax\":2.05,\"termsConditions\":\"Must be paid before work scheduled.\"},\"Method\":\"ach\",\"NetAmount\":3762.87,\"Operation\":\"Sale\",\"OrderId\":\"O-5140\",\"OrgId\":123,\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"AccountExp\":\"11/29\",\"accountId\":\"accountId\",\"AccountType\":\"visa\",\"AccountZip\":\"90210\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"HolderName\":\"Chad Mercia\",\"Initiator\":\"payor\",\"MaskedAccount\":\"4XXXXXXXX1111\",\"orderDescription\":\"Depost for materials for 123 Walnut St\",\"paymentDetails\":{\"totalAmount\":100},\"Sequence\":\"subsequent\",\"SignatureData\":\"SignatureData\",\"StoredId\":\"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\"StoredMethodUsageType\":\"subscription\"},\"PaymentTransId\":\"226-fe55ec0348e34702bd91b4be198ce7ec\",\"PayorId\":1551,\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"PaypointId\":226,\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PendingFeeAmount\":2,\"RefundId\":0,\"ResponseData\":{\"authcode\":\"authcode\",\"avsresponse\":\"avsresponse\",\"avsresponse_text\":\"avsresponse_text\",\"cvvresponse\":\"cvvresponse\",\"cvvresponse_text\":\"cvvresponse_text\",\"emv_auth_response_data\":\"emv_auth_response_data\",\"orderid\":\"O-5140\",\"response\":\"response\",\"response_code\":\"XXX\",\"response_code_text\":\"Transaction was approved.\",\"responsetext\":\"CAPTURED\",\"transactionid\":\"TRN_XXXXXGOa87juzW\",\"type\":\"type\"},\"ReturnedId\":0,\"ScheduleReference\":0,\"SettlementStatus\":2,\"Source\":\"api\",\"splitFundingInstructions\":[{}],\"TotalAmount\":30.22,\"TransactionEvents\":[{}],\"TransactionTime\":\"2025-10-19T00:00:00Z\",\"TransAdditionalData\":{\"key\":\"value\"},\"TransStatus\":1},\"externalPaypointID\":null,\"pageidentifier\":null,\"messages\":[{\"Id\":1,\"RoomId\":100,\"UserId\":555,\"UserName\":\"John Admin\",\"Content\":\"Chargeback initiated by customer\",\"CreatedAt\":\"2022-06-25T10:30:00Z\",\"MessageType\":1,\"MessageProperties\":{\"status\":\"initial\"}}],\"ServiceGroup\":\" \",\"DisputeType\":\"chargeback\",\"ProcessorName\":\"Global Payments\"}"));
        ChargebackQueryRecords response = client.chargeBacks().getChargeback(1000000L);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Id\": 201,\n"
                + "  \"ChargebackDate\": \"2022-06-25T00:00:00Z\",\n"
                + "  \"CaseNumber\": \"TZ45678\",\n"
                + "  \"ReasonCode\": \"reasonCode\",\n"
                + "  \"Reason\": \"Buyer dispute\",\n"
                + "  \"ReferenceNumber\": \"referenceNumber\",\n"
                + "  \"LastFour\": \"6789\",\n"
                + "  \"AccountType\": \"visa\",\n"
                + "  \"Status\": 1,\n"
                + "  \"Method\": \"card\",\n"
                + "  \"CreatedAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"ReplyBy\": \"2022-07-15T23:59:59Z\",\n"
                + "  \"PaymentTransId\": \"226-fe55ec0348e34702bd91b4be198ce7ec\",\n"
                + "  \"ScheduleReference\": 0,\n"
                + "  \"OrderId\": \"O-5140\",\n"
                + "  \"NetAmount\": 3762.87,\n"
                + "  \"TransactionTime\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"Customer\": {\n"
                + "    \"AdditionalData\": null,\n"
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
                + "      \"firstname\",\n"
                + "      \"lastname\",\n"
                + "      \"email\",\n"
                + "      \"customId\"\n"
                + "    ],\n"
                + "    \"LastName\": \"Doe\",\n"
                + "    \"ShippingAddress1\": \"123 Walnut St\",\n"
                + "    \"ShippingAddress2\": \"STE 900\",\n"
                + "    \"ShippingCity\": \"Johnson City\",\n"
                + "    \"ShippingCountry\": \"US\",\n"
                + "    \"ShippingState\": \"TN\",\n"
                + "    \"ShippingZip\": \"37619\"\n"
                + "  },\n"
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
                + "  \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "  \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "  \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "  \"ParentOrgId\": 123,\n"
                + "  \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "  \"Responses\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"example@email.com\",\n"
                + "      \"contactName\": \"John Doe\",\n"
                + "      \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"id\": 672,\n"
                + "      \"notes\": \"any note here\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Transaction\": {\n"
                + "    \"AchHolderType\": \"personal\",\n"
                + "    \"AchSecCode\": \"AchSecCode\",\n"
                + "    \"BatchAmount\": 30.22,\n"
                + "    \"BatchNumber\": \"batch_226_ach_12-30-2023\",\n"
                + "    \"CfeeTransactions\": [\n"
                + "      {\n"
                + "        \"transactionTime\": \"2024-01-15T09:30:00Z\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"ConnectorName\": \"gp\",\n"
                + "    \"Customer\": {\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"BillingAddress1\": \"1111 West 1st Street\",\n"
                + "      \"BillingAddress2\": \"Suite 200\",\n"
                + "      \"BillingCity\": \"Miami\",\n"
                + "      \"BillingCountry\": \"US\",\n"
                + "      \"BillingEmail\": \"example@email.com\",\n"
                + "      \"BillingPhone\": \"5555555555\",\n"
                + "      \"BillingState\": \"FL\",\n"
                + "      \"BillingZip\": \"45567\",\n"
                + "      \"CompanyName\": \"Sunshine LLC\",\n"
                + "      \"customerId\": 4440,\n"
                + "      \"CustomerNumber\": \"3456-7645A\",\n"
                + "      \"customerStatus\": 1,\n"
                + "      \"FirstName\": \"John\",\n"
                + "      \"Identifiers\": [\n"
                + "        \"firstname\",\n"
                + "        \"lastname\",\n"
                + "        \"email\",\n"
                + "        \"customId\"\n"
                + "      ],\n"
                + "      \"LastName\": \"Doe\",\n"
                + "      \"ShippingAddress1\": \"123 Walnut St\",\n"
                + "      \"ShippingAddress2\": \"STE 900\",\n"
                + "      \"ShippingCity\": \"Johnson City\",\n"
                + "      \"ShippingCountry\": \"US\",\n"
                + "      \"ShippingState\": \"TN\",\n"
                + "      \"ShippingZip\": \"37619\"\n"
                + "    },\n"
                + "    \"DeviceId\": \"6c361c7d-674c-44cc-b790-382b75d1xxx\",\n"
                + "    \"EntrypageId\": 0,\n"
                + "    \"ExternalProcessorInformation\": \"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\n"
                + "    \"FeeAmount\": 1,\n"
                + "    \"GatewayTransId\": \"TRN_xwCAjQorWAYX1nAhAoHZVfN8iYHbI0\",\n"
                + "    \"InvoiceData\": {\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"attachments\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"company\": \"ACME, INC\",\n"
                + "      \"discount\": 10,\n"
                + "      \"dutyAmount\": 0,\n"
                + "      \"firstName\": \"Chad\",\n"
                + "      \"freightAmount\": 10,\n"
                + "      \"frequency\": \"one-time\",\n"
                + "      \"invoiceAmount\": 105,\n"
                + "      \"invoiceDate\": \"2025-07-01\",\n"
                + "      \"invoiceDueDate\": \"2025-07-01\",\n"
                + "      \"invoiceEndDate\": \"2025-07-01\",\n"
                + "      \"invoiceNumber\": \"INV-2345\",\n"
                + "      \"invoiceStatus\": 1,\n"
                + "      \"invoiceType\": 0,\n"
                + "      \"items\": [\n"
                + "        {\n"
                + "          \"itemCost\": 5,\n"
                + "          \"itemProductName\": \"Materials deposit\",\n"
                + "          \"itemQty\": 1\n"
                + "        }\n"
                + "      ],\n"
                + "      \"lastName\": \"Mercia\",\n"
                + "      \"notes\": \"Example notes.\",\n"
                + "      \"paymentTerms\": \"PIA\",\n"
                + "      \"purchaseOrder\": \"PO-345\",\n"
                + "      \"shippingAddress1\": \"123 Walnut St\",\n"
                + "      \"shippingAddress2\": \"STE 900\",\n"
                + "      \"shippingCity\": \"Johnson City\",\n"
                + "      \"shippingCountry\": \"US\",\n"
                + "      \"shippingEmail\": \"example@email.com\",\n"
                + "      \"shippingFromZip\": \"30040\",\n"
                + "      \"shippingPhone\": \"5555555555\",\n"
                + "      \"shippingState\": \"TN\",\n"
                + "      \"shippingZip\": \"37619\",\n"
                + "      \"summaryCommodityCode\": \"501718\",\n"
                + "      \"tax\": 2.05,\n"
                + "      \"termsConditions\": \"Must be paid before work scheduled.\"\n"
                + "    },\n"
                + "    \"Method\": \"ach\",\n"
                + "    \"NetAmount\": 3762.87,\n"
                + "    \"Operation\": \"Sale\",\n"
                + "    \"OrderId\": \"O-5140\",\n"
                + "    \"OrgId\": 123,\n"
                + "    \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "    \"PaymentData\": {\n"
                + "      \"AccountExp\": \"11/29\",\n"
                + "      \"accountId\": \"accountId\",\n"
                + "      \"AccountType\": \"visa\",\n"
                + "      \"AccountZip\": \"90210\",\n"
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
                + "      \"HolderName\": \"Chad Mercia\",\n"
                + "      \"Initiator\": \"payor\",\n"
                + "      \"MaskedAccount\": \"4XXXXXXXX1111\",\n"
                + "      \"orderDescription\": \"Depost for materials for 123 Walnut St\",\n"
                + "      \"paymentDetails\": {\n"
                + "        \"totalAmount\": 100\n"
                + "      },\n"
                + "      \"Sequence\": \"subsequent\",\n"
                + "      \"SignatureData\": \"SignatureData\",\n"
                + "      \"StoredId\": \"1ec55af9-7b5a-4ff0-81ed-c12d2f95e135-4440\",\n"
                + "      \"StoredMethodUsageType\": \"subscription\"\n"
                + "    },\n"
                + "    \"PaymentTransId\": \"226-fe55ec0348e34702bd91b4be198ce7ec\",\n"
                + "    \"PayorId\": 1551,\n"
                + "    \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "    \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "    \"PaypointId\": 226,\n"
                + "    \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "    \"PendingFeeAmount\": 2,\n"
                + "    \"RefundId\": 0,\n"
                + "    \"ResponseData\": {\n"
                + "      \"authcode\": \"authcode\",\n"
                + "      \"avsresponse\": \"avsresponse\",\n"
                + "      \"avsresponse_text\": \"avsresponse_text\",\n"
                + "      \"cvvresponse\": \"cvvresponse\",\n"
                + "      \"cvvresponse_text\": \"cvvresponse_text\",\n"
                + "      \"emv_auth_response_data\": \"emv_auth_response_data\",\n"
                + "      \"orderid\": \"O-5140\",\n"
                + "      \"response\": \"response\",\n"
                + "      \"response_code\": \"XXX\",\n"
                + "      \"response_code_text\": \"Transaction was approved.\",\n"
                + "      \"responsetext\": \"CAPTURED\",\n"
                + "      \"transactionid\": \"TRN_XXXXXGOa87juzW\",\n"
                + "      \"type\": \"type\"\n"
                + "    },\n"
                + "    \"ReturnedId\": 0,\n"
                + "    \"ScheduleReference\": 0,\n"
                + "    \"SettlementStatus\": 2,\n"
                + "    \"Source\": \"api\",\n"
                + "    \"splitFundingInstructions\": [\n"
                + "      {}\n"
                + "    ],\n"
                + "    \"TotalAmount\": 30.22,\n"
                + "    \"TransactionEvents\": [\n"
                + "      {}\n"
                + "    ],\n"
                + "    \"TransactionTime\": \"2025-10-19T00:00:00Z\",\n"
                + "    \"TransAdditionalData\": {\n"
                + "      \"key\": \"value\"\n"
                + "    },\n"
                + "    \"TransStatus\": 1\n"
                + "  },\n"
                + "  \"externalPaypointID\": null,\n"
                + "  \"pageidentifier\": null,\n"
                + "  \"messages\": [\n"
                + "    {\n"
                + "      \"Id\": 1,\n"
                + "      \"RoomId\": 100,\n"
                + "      \"UserId\": 555,\n"
                + "      \"UserName\": \"John Admin\",\n"
                + "      \"Content\": \"Chargeback initiated by customer\",\n"
                + "      \"CreatedAt\": \"2022-06-25T10:30:00Z\",\n"
                + "      \"MessageType\": 1,\n"
                + "      \"MessageProperties\": {\n"
                + "        \"status\": \"initial\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"ServiceGroup\": \" \",\n"
                + "  \"DisputeType\": \"chargeback\",\n"
                + "  \"ProcessorName\": \"Global Payments\"\n"
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
