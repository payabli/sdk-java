package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.moneyin.requests.RequestCredit;
import io.github.payabli.api.resources.moneyin.requests.RequestPayment;
import io.github.payabli.api.resources.moneyin.requests.RequestPaymentAuthorize;
import io.github.payabli.api.resources.moneyin.requests.RequestPaymentValidate;
import io.github.payabli.api.resources.moneyin.requests.RequestRefund;
import io.github.payabli.api.resources.moneyin.requests.SendReceipt2TransRequest;
import io.github.payabli.api.resources.moneyin.types.AuthResponse;
import io.github.payabli.api.resources.moneyin.types.CapturePaymentDetails;
import io.github.payabli.api.resources.moneyin.types.CaptureRequest;
import io.github.payabli.api.resources.moneyin.types.CaptureResponse;
import io.github.payabli.api.resources.moneyin.types.PayabliApiResponseGetPaid;
import io.github.payabli.api.resources.moneyin.types.ReceiptResponse;
import io.github.payabli.api.resources.moneyin.types.RefundResponse;
import io.github.payabli.api.resources.moneyin.types.RefundWithInstructionsResponse;
import io.github.payabli.api.resources.moneyin.types.RequestCreditPaymentMethod;
import io.github.payabli.api.resources.moneyin.types.RequestPaymentValidatePaymentMethod;
import io.github.payabli.api.resources.moneyin.types.RequestPaymentValidatePaymentMethodMethod;
import io.github.payabli.api.resources.moneyin.types.ReverseResponse;
import io.github.payabli.api.resources.moneyin.types.TransRequestBody;
import io.github.payabli.api.resources.moneyin.types.ValidateResponse;
import io.github.payabli.api.resources.moneyin.types.VoidResponse;
import io.github.payabli.api.types.Achaccounttype;
import io.github.payabli.api.types.PayMethodCredit;
import io.github.payabli.api.types.PayabliApiResponse;
import io.github.payabli.api.types.PayabliApiResponse0;
import io.github.payabli.api.types.PaymentDetail;
import io.github.payabli.api.types.PaymentDetailCredit;
import io.github.payabli.api.types.PaymentMethod;
import io.github.payabli.api.types.PayorDataRequest;
import io.github.payabli.api.types.RefundDetail;
import io.github.payabli.api.types.SplitFundingRefundContent;
import io.github.payabli.api.types.TransactionQueryRecordsCustomer;
import java.util.Arrays;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoneyInWireTest {
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
    public void testAuthorize() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseText\":\"Success\",\"isSuccess\":true,\"pageIdentifier\":null,\"responseData\":{\"authCode\":\"123456\",\"referenceId\":\"10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13\",\"resultCode\":1,\"resultText\":\"Authorized\",\"avsResponseText\":\"No address or ZIP match only\",\"cvvResponseText\":\"CVV2/CVC2 no match\",\"customerId\":4440,\"methodReferenceId\":null}}"));
        AuthResponse response = client.moneyIn()
                .authorize(RequestPaymentAuthorize.builder()
                        .body(TransRequestBody.builder()
                                .paymentDetails(PaymentDetail.builder()
                                        .totalAmount(100.0)
                                        .serviceFee(0.0)
                                        .build())
                                .paymentMethod(PaymentMethod.of(PayMethodCredit.builder()
                                        .cardexp("02/27")
                                        .cardnumber("4111111111111111")
                                        .method("card")
                                        .cardcvv("999")
                                        .cardHolder("John Cassian")
                                        .cardzip("12345")
                                        .initiator("payor")
                                        .build()))
                                .customerData(PayorDataRequest.builder()
                                        .customerId(4440L)
                                        .build())
                                .entryPoint("f743aed24a")
                                .ipaddress("255.255.255.255")
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
                + "  \"ipaddress\": \"255.255.255.255\",\n"
                + "  \"paymentDetails\": {\n"
                + "    \"serviceFee\": 0,\n"
                + "    \"totalAmount\": 100\n"
                + "  },\n"
                + "  \"paymentMethod\": {\n"
                + "    \"cardcvv\": \"999\",\n"
                + "    \"cardexp\": \"02/27\",\n"
                + "    \"cardHolder\": \"John Cassian\",\n"
                + "    \"cardnumber\": \"4111111111111111\",\n"
                + "    \"cardzip\": \"12345\",\n"
                + "    \"initiator\": \"payor\",\n"
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
                + "  \"responseText\": \"Success\",\n"
                + "  \"isSuccess\": true,\n"
                + "  \"pageIdentifier\": null,\n"
                + "  \"responseData\": {\n"
                + "    \"authCode\": \"123456\",\n"
                + "    \"referenceId\": \"10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Authorized\",\n"
                + "    \"avsResponseText\": \"No address or ZIP match only\",\n"
                + "    \"cvvResponseText\": \"CVV2/CVC2 no match\",\n"
                + "    \"customerId\": 4440,\n"
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
    public void testCapture() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":{\"authCode\":\"123456\",\"referenceId\":\"10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13\",\"resultCode\":1,\"resultText\":\"SUCCESS\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":null,\"methodReferenceId\":null}}"));
        CaptureResponse response = client.moneyIn().capture("10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13", 0.0);
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
                + "    \"authCode\": \"123456\",\n"
                + "    \"referenceId\": \"10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"SUCCESS\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": null,\n"
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
    public void testCaptureAuth() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":{\"authCode\":\"123456\",\"referenceId\":\"10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13\",\"resultCode\":1,\"resultText\":\"SUCCESS\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":null,\"methodReferenceId\":null}}"));
        CaptureResponse response = client.moneyIn()
                .captureAuth(
                        "10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13",
                        CaptureRequest.builder()
                                .paymentDetails(CapturePaymentDetails.builder()
                                        .totalAmount(105.0)
                                        .serviceFee(5.0)
                                        .build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"paymentDetails\": {\n"
                + "    \"totalAmount\": 105,\n"
                + "    \"serviceFee\": 5\n"
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
                + "    \"authCode\": \"123456\",\n"
                + "    \"referenceId\": \"10-7d9cd67d-2d5d-4cd7-a1b7-72b8b201ec13\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"SUCCESS\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": null,\n"
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
    public void testCredit() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseData\":{\"AuthCode\":\"AuthCode\",\"CustomerId\":4440,\"methodReferenceId\":null,\"ReferenceId\":\"45-erre-324\",\"ResultCode\":1,\"ResultText\":\"Approved\"},\"responseText\":\"Success\"}"));
        PayabliApiResponse0 response = client.moneyIn()
                .credit(RequestCredit.builder()
                        .customerData(PayorDataRequest.builder()
                                .billingAddress1("5127 Linkwood ave")
                                .customerNumber("100")
                                .build())
                        .paymentDetails(PaymentDetailCredit.builder()
                                .totalAmount(1.0)
                                .serviceFee(0.0)
                                .build())
                        .paymentMethod(RequestCreditPaymentMethod.builder()
                                .achAccount("88354454")
                                .achAccountType(Achaccounttype.CHECKING)
                                .achHolder("John Smith")
                                .achRouting("021000021")
                                .build())
                        .idempotencyKey("6B29FC40-CA47-1067-B31D-00DD010662DA")
                        .entrypoint("my-entrypoint")
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
        String expectedRequestBody = ""
                + "{\n"
                + "  \"customerData\": {\n"
                + "    \"billingAddress1\": \"5127 Linkwood ave\",\n"
                + "    \"customerNumber\": \"100\"\n"
                + "  },\n"
                + "  \"entrypoint\": \"my-entrypoint\",\n"
                + "  \"paymentDetails\": {\n"
                + "    \"serviceFee\": 0,\n"
                + "    \"totalAmount\": 1\n"
                + "  },\n"
                + "  \"paymentMethod\": {\n"
                + "    \"achAccount\": \"88354454\",\n"
                + "    \"achAccountType\": \"Checking\",\n"
                + "    \"achHolder\": \"John Smith\",\n"
                + "    \"achRouting\": \"021000021\",\n"
                + "    \"method\": \"ach\"\n"
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
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"responseData\": {\n"
                + "    \"AuthCode\": \"AuthCode\",\n"
                + "    \"CustomerId\": 4440,\n"
                + "    \"methodReferenceId\": null,\n"
                + "    \"ReferenceId\": \"45-erre-324\",\n"
                + "    \"ResultCode\": 1,\n"
                + "    \"ResultText\": \"Approved\"\n"
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
    public void testDetails() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"BatchAmount\":10050.75,\"BatchNumber\":\"BN987654321\",\"CfeeTransactions\":[{\"cFeeTransid\":\"cFeeTransid\",\"feeAmount\":1.1,\"operation\":\"operation\",\"refundId\":4440,\"responseData\":{\"key\":\"value\"},\"settlementStatus\":1,\"transactionTime\":\"2024-01-15T09:30:00Z\",\"transStatus\":1}],\"Customer\":{\"AdditionalData\":{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"},\"BillingAddress1\":\"123 Willow Lane\",\"BillingAddress2\":\"Unit 101\",\"BillingCity\":\"Greenfield\",\"BillingCountry\":\"USA\",\"BillingEmail\":\"elizabeta.marion@email.com\",\"BillingPhone\":\"831-555-0123\",\"BillingState\":\"CA\",\"BillingZip\":\"93927\",\"customerId\":4440,\"CustomerNumber\":\"CUST12345\",\"customerStatus\":1,\"FirstName\":\"Elizabeta\",\"Identifiers\":[\"\\\\\\\"firstname\\\\\\\"\",\"\\\\\\\"lastname\\\\\\\"\",\"\\\\\\\"email\\\\\\\"\",\"\\\\\\\"customId\\\\\\\"\"],\"LastName\":\"Marion\",\"ShippingAddress1\":\"123 Willow Lane\",\"ShippingAddress2\":\"Unit 101\",\"ShippingCity\":\"Greenfield\",\"ShippingCountry\":\"USA\",\"ShippingState\":\"CA\",\"ShippingZip\":\"93927\"},\"EntrypageId\":0,\"ExternalProcessorInformation\":\"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\"FeeAmount\":5,\"GatewayTransId\":\"GT12345678\",\"InvoiceData\":{\"AdditionalData\":\"AdditionalData\",\"company\":\"Wind in the Willows Neighborhood Association, LLC\",\"discount\":0,\"dutyAmount\":0,\"firstName\":\"Elizabeta\",\"freightAmount\":0,\"frequency\":\"one-time\",\"invoiceAmount\":1000.5,\"invoiceDate\":\"2025-02-15\",\"invoiceDueDate\":\"2025-03-15\",\"invoiceEndDate\":\"2025-04-15\",\"invoiceNumber\":\"INV-20242401\",\"invoiceStatus\":1,\"invoiceType\":1,\"items\":[{\"itemCategories\":[\"HOA Dues\",\"Annual Service\"],\"itemCommodityCode\":\"200300\",\"itemCost\":1000.5,\"itemDescription\":\"Annual dues for Wind in the Willows HOA.\",\"itemMode\":1,\"itemProductCode\":\"HOADUES2024\",\"itemProductName\":\"HOA Annual Dues\",\"itemQty\":1,\"itemTaxAmount\":0,\"itemTaxRate\":0,\"itemTotalAmount\":1000.5,\"itemUnitOfMeasure\":\"service\"}],\"lastName\":\"Marion\",\"notes\":\"Annual HOA dues for Wind in the Willows Neighborhood.\",\"paymentTerms\":\"NET30\",\"purchaseOrder\":\"PO-4321ABC\",\"shippingAddress1\":\"123 Willow Lane\",\"shippingAddress2\":\"Unit 101\",\"shippingCity\":\"Greenfield\",\"shippingCountry\":\"USA\",\"shippingEmail\":\"elizabeta.marion@email.com\",\"shippingFromZip\":\"93926\",\"shippingPhone\":\"831-555-0123\",\"shippingState\":\"CA\",\"shippingZip\":\"93927\",\"summaryCommodityCode\":\"HOA2024\",\"tax\":0,\"termsConditions\":\"Full payment of HOA dues required within 30 days.\"},\"Method\":\"online\",\"NetAmount\":995.5,\"Operation\":\"Sale\",\"OrderId\":\"DUES-123\",\"OrgId\":500,\"ParentOrgName\":\"HOAManager Pro\",\"PaymentData\":{\"AccountExp\":\"11/29\",\"AccountType\":\"visa\",\"AccountZip\":\"90210\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"HolderName\":\"Elizabeta Marion\",\"Initiator\":\"merchant\",\"MaskedAccount\":\"5xxxxxxxxxxx4321\",\"orderDescription\":\"Annual HOA Dues for Wind in the Willows\",\"paymentDetails\":{\"categories\":[{\"amount\":1000,\"label\":\"Deposit\"},{\"amount\":1000,\"label\":\"Deposit\"}],\"currency\":\"USD\",\"serviceFee\":5,\"splitFunding\":[{}],\"totalAmount\":1000.5},\"Sequence\":\"first\",\"SignatureData\":\"image/png;base64,\",\"StoredMethodUsageType\":\"unscheduled\"},\"PaymentTransId\":\"12345-67890abcd\",\"PayorId\":98765,\"PaypointDbaname\":\"Wind in the Willows\",\"PaypointEntryname\":\"72aeon12\",\"PaypointId\":12345,\"PaypointLegalname\":\"Wind in the Willows Neighborhood Association, LLC\",\"PendingFeeAmount\":0,\"RefundId\":0,\"ResponseData\":{\"authcode\":\"123456\",\"avsresponse\":\"\",\"avsresponse_text\":\"\",\"cvvresponse\":\"\",\"cvvresponse_text\":\"\",\"orderid\":\"DUES-123\",\"response_code\":\"200\",\"response_code_text\":\"Transaction processed successfully.\",\"responsetext\":\"Completed\",\"transactionid\":\"TX987654321\"},\"ReturnedId\":0,\"ScheduleReference\":0,\"SettlementStatus\":1,\"Source\":\"web\",\"TotalAmount\":1000.5,\"TransactionEvents\":[{\"EventTime\":\"2024-01-23T00:46:05Z\",\"TransEvent\":\"Created\"},{\"EventData\":\"response=1&responsetext=Approved&authcode=123456&transactionid=9144440&avsresponse=&cvvresponse=&orderid=434-38aXXXX8ae4cd496db737200000000&type=sale&response_code=100&verification_method=&emv_application_id=A0000000031000&emv_application_label=VISA&emv_application_preferred_name=&emv_application_pan_sequence_number=00&transaction_status_information=&masked_merchant_number=xxxxxxxx9100&masked_terminal_number=xx01\",\"EventTime\":\"2024-01-23T00:46:17Z\",\"TransEvent\":\"Approved\"},{\"EventData\":{\"action_type\":\"settle\",\"amount\":1000.5,\"api_method\":\"\",\"batch_id\":\"68031555\",\"date\":\"20240123013414\",\"device_license_number\":\"\",\"device_nickname\":\"\",\"ip_address\":\"100.100.100.100\",\"processor_batch_id\":\"680317555\",\"processor_response_code\":\"\",\"processor_response_text\":\"\",\"requested_amountSpecified\":false,\"response_code\":\"100\",\"response_text\":\"SUCCESS\",\"source\":\"internal\",\"success\":1,\"username\":\" \"},\"EventTime\":\"2024-01-23T01:34:14Z\",\"TransEvent\":\"Settled\"}],\"TransactionTime\":\"2024-02-15T10:30:00Z\",\"TransStatus\":2}"));
        TransactionQueryRecordsCustomer response = client.moneyIn().details("45-as456777hhhhhhhhhh77777777-324");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"BatchAmount\": 10050.75,\n"
                + "  \"BatchNumber\": \"BN987654321\",\n"
                + "  \"CfeeTransactions\": [\n"
                + "    {\n"
                + "      \"cFeeTransid\": \"cFeeTransid\",\n"
                + "      \"feeAmount\": 1.1,\n"
                + "      \"operation\": \"operation\",\n"
                + "      \"refundId\": 4440,\n"
                + "      \"responseData\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"settlementStatus\": 1,\n"
                + "      \"transactionTime\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"transStatus\": 1\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Customer\": {\n"
                + "    \"AdditionalData\": {\n"
                + "      \"key1\": \"value1\",\n"
                + "      \"key2\": \"value2\",\n"
                + "      \"key3\": \"value3\"\n"
                + "    },\n"
                + "    \"BillingAddress1\": \"123 Willow Lane\",\n"
                + "    \"BillingAddress2\": \"Unit 101\",\n"
                + "    \"BillingCity\": \"Greenfield\",\n"
                + "    \"BillingCountry\": \"USA\",\n"
                + "    \"BillingEmail\": \"elizabeta.marion@email.com\",\n"
                + "    \"BillingPhone\": \"831-555-0123\",\n"
                + "    \"BillingState\": \"CA\",\n"
                + "    \"BillingZip\": \"93927\",\n"
                + "    \"customerId\": 4440,\n"
                + "    \"CustomerNumber\": \"CUST12345\",\n"
                + "    \"customerStatus\": 1,\n"
                + "    \"FirstName\": \"Elizabeta\",\n"
                + "    \"Identifiers\": [\n"
                + "      \"\\\\\\\"firstname\\\\\\\"\",\n"
                + "      \"\\\\\\\"lastname\\\\\\\"\",\n"
                + "      \"\\\\\\\"email\\\\\\\"\",\n"
                + "      \"\\\\\\\"customId\\\\\\\"\"\n"
                + "    ],\n"
                + "    \"LastName\": \"Marion\",\n"
                + "    \"ShippingAddress1\": \"123 Willow Lane\",\n"
                + "    \"ShippingAddress2\": \"Unit 101\",\n"
                + "    \"ShippingCity\": \"Greenfield\",\n"
                + "    \"ShippingCountry\": \"USA\",\n"
                + "    \"ShippingState\": \"CA\",\n"
                + "    \"ShippingZip\": \"93927\"\n"
                + "  },\n"
                + "  \"EntrypageId\": 0,\n"
                + "  \"ExternalProcessorInformation\": \"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\n"
                + "  \"FeeAmount\": 5,\n"
                + "  \"GatewayTransId\": \"GT12345678\",\n"
                + "  \"InvoiceData\": {\n"
                + "    \"AdditionalData\": \"AdditionalData\",\n"
                + "    \"company\": \"Wind in the Willows Neighborhood Association, LLC\",\n"
                + "    \"discount\": 0,\n"
                + "    \"dutyAmount\": 0,\n"
                + "    \"firstName\": \"Elizabeta\",\n"
                + "    \"freightAmount\": 0,\n"
                + "    \"frequency\": \"one-time\",\n"
                + "    \"invoiceAmount\": 1000.5,\n"
                + "    \"invoiceDate\": \"2025-02-15\",\n"
                + "    \"invoiceDueDate\": \"2025-03-15\",\n"
                + "    \"invoiceEndDate\": \"2025-04-15\",\n"
                + "    \"invoiceNumber\": \"INV-20242401\",\n"
                + "    \"invoiceStatus\": 1,\n"
                + "    \"invoiceType\": 1,\n"
                + "    \"items\": [\n"
                + "      {\n"
                + "        \"itemCategories\": [\n"
                + "          \"HOA Dues\",\n"
                + "          \"Annual Service\"\n"
                + "        ],\n"
                + "        \"itemCommodityCode\": \"200300\",\n"
                + "        \"itemCost\": 1000.5,\n"
                + "        \"itemDescription\": \"Annual dues for Wind in the Willows HOA.\",\n"
                + "        \"itemMode\": 1,\n"
                + "        \"itemProductCode\": \"HOADUES2024\",\n"
                + "        \"itemProductName\": \"HOA Annual Dues\",\n"
                + "        \"itemQty\": 1,\n"
                + "        \"itemTaxAmount\": 0,\n"
                + "        \"itemTaxRate\": 0,\n"
                + "        \"itemTotalAmount\": 1000.5,\n"
                + "        \"itemUnitOfMeasure\": \"service\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"lastName\": \"Marion\",\n"
                + "    \"notes\": \"Annual HOA dues for Wind in the Willows Neighborhood.\",\n"
                + "    \"paymentTerms\": \"NET30\",\n"
                + "    \"purchaseOrder\": \"PO-4321ABC\",\n"
                + "    \"shippingAddress1\": \"123 Willow Lane\",\n"
                + "    \"shippingAddress2\": \"Unit 101\",\n"
                + "    \"shippingCity\": \"Greenfield\",\n"
                + "    \"shippingCountry\": \"USA\",\n"
                + "    \"shippingEmail\": \"elizabeta.marion@email.com\",\n"
                + "    \"shippingFromZip\": \"93926\",\n"
                + "    \"shippingPhone\": \"831-555-0123\",\n"
                + "    \"shippingState\": \"CA\",\n"
                + "    \"shippingZip\": \"93927\",\n"
                + "    \"summaryCommodityCode\": \"HOA2024\",\n"
                + "    \"tax\": 0,\n"
                + "    \"termsConditions\": \"Full payment of HOA dues required within 30 days.\"\n"
                + "  },\n"
                + "  \"Method\": \"online\",\n"
                + "  \"NetAmount\": 995.5,\n"
                + "  \"Operation\": \"Sale\",\n"
                + "  \"OrderId\": \"DUES-123\",\n"
                + "  \"OrgId\": 500,\n"
                + "  \"ParentOrgName\": \"HOAManager Pro\",\n"
                + "  \"PaymentData\": {\n"
                + "    \"AccountExp\": \"11/29\",\n"
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
                + "    \"HolderName\": \"Elizabeta Marion\",\n"
                + "    \"Initiator\": \"merchant\",\n"
                + "    \"MaskedAccount\": \"5xxxxxxxxxxx4321\",\n"
                + "    \"orderDescription\": \"Annual HOA Dues for Wind in the Willows\",\n"
                + "    \"paymentDetails\": {\n"
                + "      \"categories\": [\n"
                + "        {\n"
                + "          \"amount\": 1000,\n"
                + "          \"label\": \"Deposit\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"amount\": 1000,\n"
                + "          \"label\": \"Deposit\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"currency\": \"USD\",\n"
                + "      \"serviceFee\": 5,\n"
                + "      \"splitFunding\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"totalAmount\": 1000.5\n"
                + "    },\n"
                + "    \"Sequence\": \"first\",\n"
                + "    \"SignatureData\": \"image/png;base64,\",\n"
                + "    \"StoredMethodUsageType\": \"unscheduled\"\n"
                + "  },\n"
                + "  \"PaymentTransId\": \"12345-67890abcd\",\n"
                + "  \"PayorId\": 98765,\n"
                + "  \"PaypointDbaname\": \"Wind in the Willows\",\n"
                + "  \"PaypointEntryname\": \"72aeon12\",\n"
                + "  \"PaypointId\": 12345,\n"
                + "  \"PaypointLegalname\": \"Wind in the Willows Neighborhood Association, LLC\",\n"
                + "  \"PendingFeeAmount\": 0,\n"
                + "  \"RefundId\": 0,\n"
                + "  \"ResponseData\": {\n"
                + "    \"authcode\": \"123456\",\n"
                + "    \"avsresponse\": \"\",\n"
                + "    \"avsresponse_text\": \"\",\n"
                + "    \"cvvresponse\": \"\",\n"
                + "    \"cvvresponse_text\": \"\",\n"
                + "    \"orderid\": \"DUES-123\",\n"
                + "    \"response_code\": \"200\",\n"
                + "    \"response_code_text\": \"Transaction processed successfully.\",\n"
                + "    \"responsetext\": \"Completed\",\n"
                + "    \"transactionid\": \"TX987654321\"\n"
                + "  },\n"
                + "  \"ReturnedId\": 0,\n"
                + "  \"ScheduleReference\": 0,\n"
                + "  \"SettlementStatus\": 1,\n"
                + "  \"Source\": \"web\",\n"
                + "  \"TotalAmount\": 1000.5,\n"
                + "  \"TransactionEvents\": [\n"
                + "    {\n"
                + "      \"EventTime\": \"2024-01-23T00:46:05Z\",\n"
                + "      \"TransEvent\": \"Created\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"EventData\": \"response=1&responsetext=Approved&authcode=123456&transactionid=9144440&avsresponse=&cvvresponse=&orderid=434-38aXXXX8ae4cd496db737200000000&type=sale&response_code=100&verification_method=&emv_application_id=A0000000031000&emv_application_label=VISA&emv_application_preferred_name=&emv_application_pan_sequence_number=00&transaction_status_information=&masked_merchant_number=xxxxxxxx9100&masked_terminal_number=xx01\",\n"
                + "      \"EventTime\": \"2024-01-23T00:46:17Z\",\n"
                + "      \"TransEvent\": \"Approved\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"EventData\": {\n"
                + "        \"action_type\": \"settle\",\n"
                + "        \"amount\": 1000.5,\n"
                + "        \"api_method\": \"\",\n"
                + "        \"batch_id\": \"68031555\",\n"
                + "        \"date\": \"20240123013414\",\n"
                + "        \"device_license_number\": \"\",\n"
                + "        \"device_nickname\": \"\",\n"
                + "        \"ip_address\": \"100.100.100.100\",\n"
                + "        \"processor_batch_id\": \"680317555\",\n"
                + "        \"processor_response_code\": \"\",\n"
                + "        \"processor_response_text\": \"\",\n"
                + "        \"requested_amountSpecified\": false,\n"
                + "        \"response_code\": \"100\",\n"
                + "        \"response_text\": \"SUCCESS\",\n"
                + "        \"source\": \"internal\",\n"
                + "        \"success\": 1,\n"
                + "        \"username\": \" \"\n"
                + "      },\n"
                + "      \"EventTime\": \"2024-01-23T01:34:14Z\",\n"
                + "      \"TransEvent\": \"Settled\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"TransactionTime\": \"2024-02-15T10:30:00Z\",\n"
                + "  \"TransStatus\": 2\n"
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
    public void testGetpaid() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseText\":\"Success\",\"isSuccess\":true,\"pageIdentifier\":null,\"responseData\":{\"authCode\":\"VTLMC1\",\"referenceId\":\"575-c490247af7ed403d86ba583507be61b0\",\"resultCode\":1,\"resultText\":\"Approved\",\"avsResponseText\":\"Exact match, Street address and 5-digit ZIP code both match\",\"cvvResponseText\":\"Not processed. Indicates that the expiration date was not provided with the request, or that the card does not have a valid CVV2 code. If the expiration date was not included with the request, resubmit the request with the expiration date.\",\"customerId\":41892,\"methodReferenceId\":null}}"));
        PayabliApiResponseGetPaid response = client.moneyIn()
                .getpaid(RequestPayment.builder()
                        .body(TransRequestBody.builder()
                                .paymentDetails(PaymentDetail.builder()
                                        .totalAmount(100.0)
                                        .serviceFee(0.0)
                                        .build())
                                .paymentMethod(PaymentMethod.of(PayMethodCredit.builder()
                                        .cardexp("02/27")
                                        .cardnumber("4111111111111111")
                                        .method("card")
                                        .cardcvv("999")
                                        .cardHolder("John Cassian")
                                        .cardzip("12345")
                                        .initiator("payor")
                                        .build()))
                                .customerData(PayorDataRequest.builder()
                                        .customerId(4440L)
                                        .build())
                                .entryPoint("f743aed24a")
                                .ipaddress("255.255.255.255")
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
                + "  \"ipaddress\": \"255.255.255.255\",\n"
                + "  \"paymentDetails\": {\n"
                + "    \"serviceFee\": 0,\n"
                + "    \"totalAmount\": 100\n"
                + "  },\n"
                + "  \"paymentMethod\": {\n"
                + "    \"cardcvv\": \"999\",\n"
                + "    \"cardexp\": \"02/27\",\n"
                + "    \"cardHolder\": \"John Cassian\",\n"
                + "    \"cardnumber\": \"4111111111111111\",\n"
                + "    \"cardzip\": \"12345\",\n"
                + "    \"initiator\": \"payor\",\n"
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
                + "  \"responseText\": \"Success\",\n"
                + "  \"isSuccess\": true,\n"
                + "  \"pageIdentifier\": null,\n"
                + "  \"responseData\": {\n"
                + "    \"authCode\": \"VTLMC1\",\n"
                + "    \"referenceId\": \"575-c490247af7ed403d86ba583507be61b0\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Approved\",\n"
                + "    \"avsResponseText\": \"Exact match, Street address and 5-digit ZIP code both match\",\n"
                + "    \"cvvResponseText\": \"Not processed. Indicates that the expiration date was not provided with the request, or that the card does not have a valid CVV2 code. If the expiration date was not included with the request, resubmit the request with the expiration date.\",\n"
                + "    \"customerId\": 41892,\n"
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
    public void testReverse() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":{\"authCode\":\"A0000\",\"referenceId\":\"255-fb61db4171334aa79224b019f090e4c5\",\"resultCode\":1,\"resultText\":\"REVERSED\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":null,\"methodReferenceId\":null}}"));
        ReverseResponse response = client.moneyIn().reverse("10-3ffa27df-b171-44e0-b251-e95fbfc7a723", 0.0);
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
                + "    \"authCode\": \"A0000\",\n"
                + "    \"referenceId\": \"255-fb61db4171334aa79224b019f090e4c5\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"REVERSED\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": null,\n"
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
    public void testRefund() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseText\":\"Success\",\"isSuccess\":true,\"responseData\":{\"authCode\":\"A0000\",\"expectedProcessingDateTime\":\"2025-02-15T10:30:00Z\",\"referenceId\":\"10-3ffa27df-b171-44e0-b251-e95fbfc7a723\",\"resultCode\":10,\"resultText\":\"INITIATED\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":null,\"methodReferenceId\":null},\"pageidentifier\":null}"));
        RefundResponse response = client.moneyIn().refund("10-3ffa27df-b171-44e0-b251-e95fbfc7a723", 0.0);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": {\n"
                + "    \"authCode\": \"A0000\",\n"
                + "    \"expectedProcessingDateTime\": \"2025-02-15T10:30:00Z\",\n"
                + "    \"referenceId\": \"10-3ffa27df-b171-44e0-b251-e95fbfc7a723\",\n"
                + "    \"resultCode\": 10,\n"
                + "    \"resultText\": \"INITIATED\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": null,\n"
                + "    \"methodReferenceId\": null\n"
                + "  },\n"
                + "  \"pageidentifier\": null\n"
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
    public void testRefundWithInstructions() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseText\":\"Success\",\"isSuccess\":true,\"responseData\":{\"authCode\":\"\",\"referenceId\":\"288-a1192b75-99e9-4d43-8af1-7ae9ab7da4f4\",\"resultCode\":1,\"resultText\":\"CAPTURED\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":null,\"methodReferenceId\":null},\"pageidentifier\":null}"));
        RefundWithInstructionsResponse response = client.moneyIn()
                .refundWithInstructions(
                        "10-3ffa27df-b171-44e0-b251-e95fbfc7a723",
                        RequestRefund.builder()
                                .idempotencyKey("8A29FC40-CA47-1067-B31D-00DD010662DB")
                                .amount(100.0)
                                .orderDescription("Materials deposit")
                                .refundDetails(RefundDetail.builder()
                                        .splitRefunding(Optional.of(Arrays.asList(
                                                SplitFundingRefundContent.builder()
                                                        .accountId("187-342")
                                                        .amount(60.0)
                                                        .description("Refunding undelivered materials")
                                                        .originationEntryPoint("7f1a381696")
                                                        .build(),
                                                SplitFundingRefundContent.builder()
                                                        .accountId("187-343")
                                                        .amount(40.0)
                                                        .description("Refunding deposit for undelivered materials")
                                                        .originationEntryPoint("7f1a381696")
                                                        .build())))
                                        .build())
                                .source("api")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());

        // Validate headers
        Assertions.assertEquals(
                "8A29FC40-CA47-1067-B31D-00DD010662DB",
                request.getHeader("idempotencyKey"),
                "Header 'idempotencyKey' should match expected value");
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"source\": \"api\",\n"
                + "  \"orderDescription\": \"Materials deposit\",\n"
                + "  \"amount\": 100,\n"
                + "  \"refundDetails\": {\n"
                + "    \"splitRefunding\": [\n"
                + "      {\n"
                + "        \"originationEntryPoint\": \"7f1a381696\",\n"
                + "        \"accountId\": \"187-342\",\n"
                + "        \"description\": \"Refunding undelivered materials\",\n"
                + "        \"amount\": 60\n"
                + "      },\n"
                + "      {\n"
                + "        \"originationEntryPoint\": \"7f1a381696\",\n"
                + "        \"accountId\": \"187-343\",\n"
                + "        \"description\": \"Refunding deposit for undelivered materials\",\n"
                + "        \"amount\": 40\n"
                + "      }\n"
                + "    ]\n"
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
                + "  \"responseData\": {\n"
                + "    \"authCode\": \"\",\n"
                + "    \"referenceId\": \"288-a1192b75-99e9-4d43-8af1-7ae9ab7da4f4\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"CAPTURED\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": null,\n"
                + "    \"methodReferenceId\": null\n"
                + "  },\n"
                + "  \"pageidentifier\": null\n"
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
    public void testReverseCredit() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"authCode\":null,\"avsResponseText\":null,\"customerId\":4440,\"cvvResponseText\":null,\"referenceId\":\"148-7e1528b9b7ab56d0bf3b837237b84479\",\"resultCode\":1,\"resultText\":\"transaction processed.\"},\"responseText\":\"Success\"}"));
        PayabliApiResponse response = client.moneyIn().reverseCredit("45-as456777hhhhhhhhhh77777777-324");
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
                + "    \"authCode\": null,\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"customerId\": 4440,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"referenceId\": \"148-7e1528b9b7ab56d0bf3b837237b84479\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"transaction processed.\"\n"
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
    public void testSendReceipt2Trans() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseText\":\"Success\"}"));
        ReceiptResponse response = client.moneyIn()
                .sendReceipt2Trans(
                        "45-as456777hhhhhhhhhh77777777-324",
                        SendReceipt2TransRequest.builder()
                                .email("example@email.com")
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
                + "  \"pageIdentifier\": \"null\",\n"
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
    public void testValidate() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"authCode\":\"\",\"referenceId\":\"\",\"resultCode\":1,\"resultText\":\"Validated\",\"avsResponseText\":\"Zip code provided\",\"cvvResponseText\":\"\",\"customerId\":0,\"methodReferenceId\":null},\"responseText\":\"Success\"}"));
        ValidateResponse response = client.moneyIn()
                .validate(RequestPaymentValidate.builder()
                        .entryPoint("entry132")
                        .paymentMethod(RequestPaymentValidatePaymentMethod.builder()
                                .method(RequestPaymentValidatePaymentMethodMethod.CARD)
                                .cardnumber("4360000001000005")
                                .cardexp("12/29")
                                .cardzip("14602-8328")
                                .cardHolder("Dianne Becker-Smith")
                                .build())
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
        String expectedRequestBody = ""
                + "{\n"
                + "  \"entryPoint\": \"entry132\",\n"
                + "  \"paymentMethod\": {\n"
                + "    \"method\": \"card\",\n"
                + "    \"cardnumber\": \"4360000001000005\",\n"
                + "    \"cardexp\": \"12/29\",\n"
                + "    \"cardzip\": \"14602-8328\",\n"
                + "    \"cardHolder\": \"Dianne Becker-Smith\"\n"
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
                + "    \"authCode\": \"\",\n"
                + "    \"referenceId\": \"\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Validated\",\n"
                + "    \"avsResponseText\": \"Zip code provided\",\n"
                + "    \"cvvResponseText\": \"\",\n"
                + "    \"customerId\": 0,\n"
                + "    \"methodReferenceId\": null\n"
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
    public void testVoid() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":{\"authCode\":\"123456\",\"referenceId\":\"132-9eab3dfe958146639944aebcab3e9e28\",\"resultCode\":1,\"resultText\":\"Transaction Void Successful\",\"avsResponseText\":null,\"cvvResponseText\":null,\"customerId\":null,\"methodReferenceId\":null}}"));
        VoidResponse response = client.moneyIn().void_("10-3ffa27df-b171-44e0-b251-e95fbfc7a723");
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
                + "    \"authCode\": \"123456\",\n"
                + "    \"referenceId\": \"132-9eab3dfe958146639944aebcab3e9e28\",\n"
                + "    \"resultCode\": 1,\n"
                + "    \"resultText\": \"Transaction Void Successful\",\n"
                + "    \"avsResponseText\": null,\n"
                + "    \"cvvResponseText\": null,\n"
                + "    \"customerId\": null,\n"
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
