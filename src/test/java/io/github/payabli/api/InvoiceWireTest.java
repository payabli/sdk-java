package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.invoice.requests.AddInvoiceRequest;
import io.github.payabli.api.resources.invoice.requests.EditInvoiceRequest;
import io.github.payabli.api.resources.invoice.requests.GetAttachedFileFromInvoiceRequest;
import io.github.payabli.api.resources.invoice.requests.ListInvoicesOrgRequest;
import io.github.payabli.api.resources.invoice.requests.ListInvoicesRequest;
import io.github.payabli.api.resources.invoice.requests.SendInvoiceRequest;
import io.github.payabli.api.resources.invoice.types.GetInvoiceRecord;
import io.github.payabli.api.resources.invoice.types.InvoiceDataRequest;
import io.github.payabli.api.resources.invoice.types.InvoiceNumberResponse;
import io.github.payabli.api.resources.invoice.types.InvoiceResponseWithoutData;
import io.github.payabli.api.resources.invoice.types.QueryInvoiceResponse;
import io.github.payabli.api.resources.invoice.types.SendInvoiceResponse;
import io.github.payabli.api.types.BillData;
import io.github.payabli.api.types.BillItem;
import io.github.payabli.api.types.FileContent;
import io.github.payabli.api.types.Frequency;
import io.github.payabli.api.types.PayorDataRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvoiceWireTest {
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
    public void testAddInvoice() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3625,\"responseText\":\"Success\",\"pageidentifier\":null,\"roomId\":0}"));
        InvoiceResponseWithoutData response = client.invoice()
                .addInvoice(
                        "8cfec329267",
                        AddInvoiceRequest.builder()
                                .body(InvoiceDataRequest.builder()
                                        .customerData(PayorDataRequest.builder()
                                                .customerNumber("3")
                                                .firstName("Tamara")
                                                .lastName("Bagratoni")
                                                .build())
                                        .invoiceData(BillData.builder()
                                                .discount(10.0)
                                                .frequency(Frequency.ONE_TIME)
                                                .invoiceAmount(982.37)
                                                .invoiceDate("2025-10-19")
                                                .invoiceNumber("INV-3")
                                                .invoiceStatus(1)
                                                .invoiceType(0)
                                                .items(Optional.of(Arrays.asList(
                                                        BillItem.builder()
                                                                .itemCost(100.0)
                                                                .itemDescription("Consultation for Georgian tours")
                                                                .itemMode(1)
                                                                .itemProductName("Adventure Consult")
                                                                .itemQty(1)
                                                                .build(),
                                                        BillItem.builder()
                                                                .itemCost(882.37)
                                                                .itemDescription("Deposit for trip planning")
                                                                .itemProductName("Deposit ")
                                                                .itemQty(1)
                                                                .build())))
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
                + "    \"firstName\": \"Tamara\",\n"
                + "    \"lastName\": \"Bagratoni\",\n"
                + "    \"customerNumber\": \"3\"\n"
                + "  },\n"
                + "  \"invoiceData\": {\n"
                + "    \"items\": [\n"
                + "      {\n"
                + "        \"itemProductName\": \"Adventure Consult\",\n"
                + "        \"itemDescription\": \"Consultation for Georgian tours\",\n"
                + "        \"itemCost\": 100,\n"
                + "        \"itemQty\": 1,\n"
                + "        \"itemMode\": 1\n"
                + "      },\n"
                + "      {\n"
                + "        \"itemProductName\": \"Deposit \",\n"
                + "        \"itemDescription\": \"Deposit for trip planning\",\n"
                + "        \"itemCost\": 882.37,\n"
                + "        \"itemQty\": 1\n"
                + "      }\n"
                + "    ],\n"
                + "    \"invoiceDate\": \"2025-10-19\",\n"
                + "    \"invoiceType\": 0,\n"
                + "    \"invoiceStatus\": 1,\n"
                + "    \"frequency\": \"one-time\",\n"
                + "    \"invoiceAmount\": 982.37,\n"
                + "    \"discount\": 10,\n"
                + "    \"invoiceNumber\": \"INV-3\"\n"
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
                + "  \"responseData\": 3625,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"pageidentifier\": null,\n"
                + "  \"roomId\": 0\n"
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
    public void testDeleteAttachedFromInvoice() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3625,\"responseText\":\"Success\",\"pageidentifier\":null,\"roomId\":0}"));
        InvoiceResponseWithoutData response = client.invoice().deleteAttachedFromInvoice(23548884, "0_Bill.pdf");
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
                + "  \"responseData\": 3625,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"pageidentifier\": null,\n"
                + "  \"roomId\": 0\n"
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
    public void testDeleteInvoice() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3625,\"responseText\":\"Success\",\"pageidentifier\":null,\"roomId\":0}"));
        InvoiceResponseWithoutData response = client.invoice().deleteInvoice(23548884);
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
                + "  \"responseData\": 3625,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"pageidentifier\": null,\n"
                + "  \"roomId\": 0\n"
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
    public void testEditInvoice() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":332,\"responseText\":\"Success\",\"pageidentifier\":null,\"roomId\":0}"));
        InvoiceResponseWithoutData response = client.invoice()
                .editInvoice(
                        332,
                        EditInvoiceRequest.builder()
                                .body(InvoiceDataRequest.builder()
                                        .invoiceData(BillData.builder()
                                                .invoiceAmount(982.37)
                                                .invoiceDate("2025-10-19")
                                                .invoiceNumber("INV-6")
                                                .items(Optional.of(Arrays.asList(BillItem.builder()
                                                        .itemCost(882.37)
                                                        .itemDescription("Deposit for trip planning")
                                                        .itemProductName("Deposit")
                                                        .itemQty(1)
                                                        .build())))
                                                .build())
                                        .build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"invoiceData\": {\n"
                + "    \"items\": [\n"
                + "      {\n"
                + "        \"itemProductName\": \"Deposit\",\n"
                + "        \"itemDescription\": \"Deposit for trip planning\",\n"
                + "        \"itemCost\": 882.37,\n"
                + "        \"itemQty\": 1\n"
                + "      }\n"
                + "    ],\n"
                + "    \"invoiceDate\": \"2025-10-19\",\n"
                + "    \"invoiceAmount\": 982.37,\n"
                + "    \"invoiceNumber\": \"INV-6\"\n"
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
                + "  \"responseData\": 332,\n"
                + "  \"responseText\": \"Success\",\n"
                + "  \"pageidentifier\": null,\n"
                + "  \"roomId\": 0\n"
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
    public void testGetAttachedFileFromInvoice() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"fContent\":\"fContent\",\"filename\":\"filename\",\"ftype\":\"pdf\",\"furl\":\"furl\"}"));
        FileContent response = client.invoice()
                .getAttachedFileFromInvoice(
                        1,
                        "filename",
                        GetAttachedFileFromInvoiceRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"fContent\": \"fContent\",\n"
                + "  \"filename\": \"filename\",\n"
                + "  \"ftype\": \"pdf\",\n"
                + "  \"furl\": \"furl\"\n"
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
    public void testGetInvoice() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"AdditionalData\":null,\"billEvents\":[{\"description\":\"TransferCreated\",\"eventTime\":\"2023-07-05T22:31:06Z\",\"extraData\":{\"key\":\"value\"},\"refData\":\"refData\",\"source\":\"api\"}],\"company\":\"Acme Inc\",\"createdAt\":\"2022-07-01T15:00:01Z\",\"Customer\":{\"AdditionalData\":null,\"BillingAddress1\":\"1111 West 1st Street\",\"BillingAddress2\":\"Suite 200\",\"BillingCity\":\"Miami\",\"BillingCountry\":\"US\",\"BillingEmail\":\"example@email.com\",\"BillingPhone\":\"5555555555\",\"BillingState\":\"FL\",\"BillingZip\":\"45567\",\"CompanyName\":\"Sunshine LLC\",\"customerId\":4440,\"CustomerNumber\":\"3456-7645A\",\"customerStatus\":1,\"FirstName\":\"John\",\"Identifiers\":[\"\\\\\\\"firstname\\\\\\\"\",\"\\\\\\\"lastname\\\\\\\"\",\"\\\\\\\"email\\\\\\\"\",\"\\\\\\\"customId\\\\\\\"\"],\"LastName\":\"Doe\",\"ShippingAddress1\":\"123 Walnut St\",\"ShippingAddress2\":\"STE 900\",\"ShippingCity\":\"Johnson City\",\"ShippingCountry\":\"US\",\"ShippingState\":\"TN\",\"ShippingZip\":\"37619\"},\"customerId\":4440,\"discount\":10,\"DocumentsRef\":{\"filelist\":[{}],\"zipfile\":\"zx45.zip\"},\"dutyAmount\":0,\"firstName\":\"firstName\",\"freightAmount\":10,\"frequency\":\"one-time\",\"invoiceAmount\":105,\"invoiceDate\":\"2025-07-01\",\"invoiceDueDate\":\"2025-07-01\",\"invoiceEndDate\":\"2025-07-01\",\"invoiceId\":236,\"invoiceNumber\":\"INV-2345\",\"invoicePaidAmount\":0,\"invoiceSentDate\":\"2025-10-19T00:00:00Z\",\"invoiceStatus\":1,\"invoiceType\":0,\"items\":[{\"itemCommodityCode\":\"010\",\"itemCost\":5,\"itemDescription\":\"Deposit for materials.\",\"itemMode\":0,\"itemProductCode\":\"M-DEPOSIT\",\"itemProductName\":\"Materials deposit\",\"itemQty\":1,\"itemTaxAmount\":7,\"itemTaxRate\":0.075,\"itemTotalAmount\":1.1,\"itemUnitOfMeasure\":\"SqFt\"}],\"lastName\":\"lastName\",\"lastPaymentDate\":\"2025-10-19T00:00:00Z\",\"notes\":null,\"ParentOrgName\":\"parentOrgName\",\"paylinkId\":\"paylinkId\",\"paymentTerms\":\"NET30\",\"PaypointDbaname\":\"Sinks Inc\",\"PaypointEntryname\":\"5789a30009s\",\"paypointId\":56,\"PaypointLegalname\":\"Sinks and Faucets LLC\",\"purchaseOrder\":\"PO-345\",\"scheduledOptions\":{\"includePaylink\":true,\"includePdf\":true},\"shippingAddress1\":\"123 Walnut St\",\"shippingAddress2\":\"STE 900\",\"shippingCity\":\"Johnson City\",\"shippingCountry\":\"US\",\"shippingEmail\":\"example@email.com\",\"shippingFromZip\":\"30040\",\"shippingPhone\":\"shippingPhone\",\"shippingState\":\"TN\",\"shippingZip\":\"37619\",\"summaryCommodityCode\":\"501718\",\"tax\":2.05,\"termsConditions\":\"termsConditions\"}"));
        GetInvoiceRecord response = client.invoice().getInvoice(23548884);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"AdditionalData\": null,\n"
                + "  \"billEvents\": [\n"
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
                + "  \"company\": \"Acme Inc\",\n"
                + "  \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
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
                + "  \"customerId\": 4440,\n"
                + "  \"discount\": 10,\n"
                + "  \"DocumentsRef\": {\n"
                + "    \"filelist\": [\n"
                + "      {}\n"
                + "    ],\n"
                + "    \"zipfile\": \"zx45.zip\"\n"
                + "  },\n"
                + "  \"dutyAmount\": 0,\n"
                + "  \"firstName\": \"firstName\",\n"
                + "  \"freightAmount\": 10,\n"
                + "  \"frequency\": \"one-time\",\n"
                + "  \"invoiceAmount\": 105,\n"
                + "  \"invoiceDate\": \"2025-07-01\",\n"
                + "  \"invoiceDueDate\": \"2025-07-01\",\n"
                + "  \"invoiceEndDate\": \"2025-07-01\",\n"
                + "  \"invoiceId\": 236,\n"
                + "  \"invoiceNumber\": \"INV-2345\",\n"
                + "  \"invoicePaidAmount\": 0,\n"
                + "  \"invoiceSentDate\": \"2025-10-19T00:00:00Z\",\n"
                + "  \"invoiceStatus\": 1,\n"
                + "  \"invoiceType\": 0,\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"itemCommodityCode\": \"010\",\n"
                + "      \"itemCost\": 5,\n"
                + "      \"itemDescription\": \"Deposit for materials.\",\n"
                + "      \"itemMode\": 0,\n"
                + "      \"itemProductCode\": \"M-DEPOSIT\",\n"
                + "      \"itemProductName\": \"Materials deposit\",\n"
                + "      \"itemQty\": 1,\n"
                + "      \"itemTaxAmount\": 7,\n"
                + "      \"itemTaxRate\": 0.075,\n"
                + "      \"itemTotalAmount\": 1.1,\n"
                + "      \"itemUnitOfMeasure\": \"SqFt\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"lastName\": \"lastName\",\n"
                + "  \"lastPaymentDate\": \"2025-10-19T00:00:00Z\",\n"
                + "  \"notes\": null,\n"
                + "  \"ParentOrgName\": \"parentOrgName\",\n"
                + "  \"paylinkId\": \"paylinkId\",\n"
                + "  \"paymentTerms\": \"NET30\",\n"
                + "  \"PaypointDbaname\": \"Sinks Inc\",\n"
                + "  \"PaypointEntryname\": \"5789a30009s\",\n"
                + "  \"paypointId\": 56,\n"
                + "  \"PaypointLegalname\": \"Sinks and Faucets LLC\",\n"
                + "  \"purchaseOrder\": \"PO-345\",\n"
                + "  \"scheduledOptions\": {\n"
                + "    \"includePaylink\": true,\n"
                + "    \"includePdf\": true\n"
                + "  },\n"
                + "  \"shippingAddress1\": \"123 Walnut St\",\n"
                + "  \"shippingAddress2\": \"STE 900\",\n"
                + "  \"shippingCity\": \"Johnson City\",\n"
                + "  \"shippingCountry\": \"US\",\n"
                + "  \"shippingEmail\": \"example@email.com\",\n"
                + "  \"shippingFromZip\": \"30040\",\n"
                + "  \"shippingPhone\": \"shippingPhone\",\n"
                + "  \"shippingState\": \"TN\",\n"
                + "  \"shippingZip\": \"37619\",\n"
                + "  \"summaryCommodityCode\": \"501718\",\n"
                + "  \"tax\": 2.05,\n"
                + "  \"termsConditions\": \"termsConditions\"\n"
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
    public void testGetInvoiceNumber() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"MyInvoice-114434565s32440\",\"responseText\":\"Success\"}"));
        InvoiceNumberResponse response = client.invoice().getInvoiceNumber("8cfec329267");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": \"MyInvoice-114434565s32440\",\n"
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
    public void testListInvoices() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"invoiceId\":3674,\"customerId\":1323,\"paypointId\":10,\"invoiceNumber\":\"QA-1709680125\",\"invoiceDate\":\"2025-03-05\",\"invoiceDueDate\":\"2025-03-05\",\"lastPaymentDate\":null,\"createdAt\":\"2024-03-05T18:08:45Z\",\"invoiceStatus\":1,\"invoiceType\":0,\"frequency\":\"one-time\",\"paymentTerms\":\"N30\",\"termsConditions\":null,\"notes\":null,\"tax\":0,\"discount\":0,\"invoiceAmount\":50,\"invoicePaidAmount\":0,\"freightAmount\":0,\"dutyAmount\":0,\"purchaseOrder\":null,\"firstName\":\"Amirah\",\"lastName\":\"Tan\",\"company\":null,\"shippingAddress1\":\"1234 Rainier Ave\",\"shippingAddress2\":\"Apt 567\",\"shippingCity\":\"Seattle\",\"shippingState\":\"WA\",\"shippingZip\":\"98101\",\"shippingFromZip\":\"\",\"shippingCountry\":\"US\",\"shippingEmail\":\"amirah.tan@example.com\",\"shippingPhone\":\"\",\"summaryCommodityCode\":null,\"items\":[{\"itemCost\":50,\"itemDescription\":\"service\",\"itemProductName\":\"Internet\",\"itemQty\":1}],\"Customer\":{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"BillingPhone\":\"1234567890\",\"customerId\":1323},\"paylinkId\":\"3674-cf15b881-f276-4b69-bdc8-841b2d123XXXXXX\",\"billEvents\":[{\"description\":\"Invoice created\",\"eventTime\":\"2024-03-05T23:08:45Z\",\"refData\":\"00-802fa578504a7af6f3dd890a3802f7ef-61b4bedXXXX1234\"}],\"scheduledOptions\":null,\"PaypointLegalname\":\"Emerald City LLC\",\"PaypointDbaname\":\"Emerald City Trading\",\"PaypointEntryname\":\"47a30009s\",\"ParentOrgId\":123,\"ParentOrgName\":\"Emerald Enterprises\",\"AdditionalData\":null,\"DocumentsRef\":null,\"externalPaypointID\":\"seattletrade01-10\",\"pageIdentifier\":null}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
        QueryInvoiceResponse response = client.invoice()
                .listInvoices(
                        "8cfec329267",
                        ListInvoicesRequest.builder()
                                .fromRecord(251)
                                .limitRecord(0)
                                .sortBy("desc(field_name)")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"invoiceId\": 3674,\n"
                + "      \"customerId\": 1323,\n"
                + "      \"paypointId\": 10,\n"
                + "      \"invoiceNumber\": \"QA-1709680125\",\n"
                + "      \"invoiceDate\": \"2025-03-05\",\n"
                + "      \"invoiceDueDate\": \"2025-03-05\",\n"
                + "      \"lastPaymentDate\": null,\n"
                + "      \"createdAt\": \"2024-03-05T18:08:45Z\",\n"
                + "      \"invoiceStatus\": 1,\n"
                + "      \"invoiceType\": 0,\n"
                + "      \"frequency\": \"one-time\",\n"
                + "      \"paymentTerms\": \"N30\",\n"
                + "      \"termsConditions\": null,\n"
                + "      \"notes\": null,\n"
                + "      \"tax\": 0,\n"
                + "      \"discount\": 0,\n"
                + "      \"invoiceAmount\": 50,\n"
                + "      \"invoicePaidAmount\": 0,\n"
                + "      \"freightAmount\": 0,\n"
                + "      \"dutyAmount\": 0,\n"
                + "      \"purchaseOrder\": null,\n"
                + "      \"firstName\": \"Amirah\",\n"
                + "      \"lastName\": \"Tan\",\n"
                + "      \"company\": null,\n"
                + "      \"shippingAddress1\": \"1234 Rainier Ave\",\n"
                + "      \"shippingAddress2\": \"Apt 567\",\n"
                + "      \"shippingCity\": \"Seattle\",\n"
                + "      \"shippingState\": \"WA\",\n"
                + "      \"shippingZip\": \"98101\",\n"
                + "      \"shippingFromZip\": \"\",\n"
                + "      \"shippingCountry\": \"US\",\n"
                + "      \"shippingEmail\": \"amirah.tan@example.com\",\n"
                + "      \"shippingPhone\": \"\",\n"
                + "      \"summaryCommodityCode\": null,\n"
                + "      \"items\": [\n"
                + "        {\n"
                + "          \"itemCost\": 50,\n"
                + "          \"itemDescription\": \"service\",\n"
                + "          \"itemProductName\": \"Internet\",\n"
                + "          \"itemQty\": 1\n"
                + "        }\n"
                + "      ],\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": {\n"
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
                + "        \"BillingPhone\": \"1234567890\",\n"
                + "        \"customerId\": 1323\n"
                + "      },\n"
                + "      \"paylinkId\": \"3674-cf15b881-f276-4b69-bdc8-841b2d123XXXXXX\",\n"
                + "      \"billEvents\": [\n"
                + "        {\n"
                + "          \"description\": \"Invoice created\",\n"
                + "          \"eventTime\": \"2024-03-05T23:08:45Z\",\n"
                + "          \"refData\": \"00-802fa578504a7af6f3dd890a3802f7ef-61b4bedXXXX1234\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"scheduledOptions\": null,\n"
                + "      \"PaypointLegalname\": \"Emerald City LLC\",\n"
                + "      \"PaypointDbaname\": \"Emerald City Trading\",\n"
                + "      \"PaypointEntryname\": \"47a30009s\",\n"
                + "      \"ParentOrgId\": 123,\n"
                + "      \"ParentOrgName\": \"Emerald Enterprises\",\n"
                + "      \"AdditionalData\": null,\n"
                + "      \"DocumentsRef\": null,\n"
                + "      \"externalPaypointID\": \"seattletrade01-10\",\n"
                + "      \"pageIdentifier\": null\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 77.22,\n"
                + "    \"totalNetAmount\": 77.22,\n"
                + "    \"totalPages\": 2,\n"
                + "    \"totalRecords\": 2\n"
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
    public void testListInvoicesOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"billEvents\":[{\"description\":\"Invoice created\",\"eventTime\":\"2024-03-05T23:08:45Z\",\"refData\":\"00-802fa578504a7af6f3dd890a3802f7ef-61b4bedXXXX1234\"}],\"createdAt\":\"2024-03-05T18:08:45Z\",\"Customer\":{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"BillingPhone\":\"1234567890\",\"customerId\":1323},\"customerId\":1323,\"discount\":0,\"dutyAmount\":0,\"externalPaypointID\":\"seattletrade01-10\",\"firstName\":\"Amirah\",\"freightAmount\":0,\"frequency\":\"one-time\",\"termsConditions\":null,\"notes\":null,\"invoiceAmount\":50,\"invoiceDate\":\"2025-03-05\",\"invoiceDueDate\":\"2025-03-05\",\"invoiceId\":3674,\"invoiceNumber\":\"QA-1709680125\",\"invoicePaidAmount\":0,\"invoiceStatus\":1,\"invoiceType\":0,\"items\":[{\"itemCost\":50,\"itemDescription\":\"service\",\"itemProductName\":\"Internet\",\"itemQty\":1}],\"lastName\":\"Tan\",\"ParentOrgName\":\"Emerald Enterprises\",\"ParentOrgId\":123,\"paylinkId\":\"3674-cf15b881-f276-4b69-bdc8-841b2d123XXXXXX\",\"paymentTerms\":\"N30\",\"PaypointDbaname\":\"Emerald City Trading\",\"PaypointEntryname\":\"47a30009s\",\"paypointId\":10,\"PaypointLegalname\":\"Emerald City LLC\",\"shippingAddress1\":\"1234 Rainier Ave\",\"shippingAddress2\":\"Apt 567\",\"shippingCity\":\"Seattle\",\"shippingCountry\":\"US\",\"shippingEmail\":\"amirah.tan@example.com\",\"shippingFromZip\":\"\",\"shippingPhone\":\"\",\"shippingState\":\"WA\",\"shippingZip\":\"98101\",\"tax\":0}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
        QueryInvoiceResponse response = client.invoice()
                .listInvoicesOrg(
                        123,
                        ListInvoicesOrgRequest.builder()
                                .fromRecord(251)
                                .limitRecord(0)
                                .sortBy("desc(field_name)")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"billEvents\": [\n"
                + "        {\n"
                + "          \"description\": \"Invoice created\",\n"
                + "          \"eventTime\": \"2024-03-05T23:08:45Z\",\n"
                + "          \"refData\": \"00-802fa578504a7af6f3dd890a3802f7ef-61b4bedXXXX1234\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"createdAt\": \"2024-03-05T18:08:45Z\",\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": {\n"
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
                + "        \"BillingPhone\": \"1234567890\",\n"
                + "        \"customerId\": 1323\n"
                + "      },\n"
                + "      \"customerId\": 1323,\n"
                + "      \"discount\": 0,\n"
                + "      \"dutyAmount\": 0,\n"
                + "      \"externalPaypointID\": \"seattletrade01-10\",\n"
                + "      \"firstName\": \"Amirah\",\n"
                + "      \"freightAmount\": 0,\n"
                + "      \"frequency\": \"one-time\",\n"
                + "      \"termsConditions\": null,\n"
                + "      \"notes\": null,\n"
                + "      \"invoiceAmount\": 50,\n"
                + "      \"invoiceDate\": \"2025-03-05\",\n"
                + "      \"invoiceDueDate\": \"2025-03-05\",\n"
                + "      \"invoiceId\": 3674,\n"
                + "      \"invoiceNumber\": \"QA-1709680125\",\n"
                + "      \"invoicePaidAmount\": 0,\n"
                + "      \"invoiceStatus\": 1,\n"
                + "      \"invoiceType\": 0,\n"
                + "      \"items\": [\n"
                + "        {\n"
                + "          \"itemCost\": 50,\n"
                + "          \"itemDescription\": \"service\",\n"
                + "          \"itemProductName\": \"Internet\",\n"
                + "          \"itemQty\": 1\n"
                + "        }\n"
                + "      ],\n"
                + "      \"lastName\": \"Tan\",\n"
                + "      \"ParentOrgName\": \"Emerald Enterprises\",\n"
                + "      \"ParentOrgId\": 123,\n"
                + "      \"paylinkId\": \"3674-cf15b881-f276-4b69-bdc8-841b2d123XXXXXX\",\n"
                + "      \"paymentTerms\": \"N30\",\n"
                + "      \"PaypointDbaname\": \"Emerald City Trading\",\n"
                + "      \"PaypointEntryname\": \"47a30009s\",\n"
                + "      \"paypointId\": 10,\n"
                + "      \"PaypointLegalname\": \"Emerald City LLC\",\n"
                + "      \"shippingAddress1\": \"1234 Rainier Ave\",\n"
                + "      \"shippingAddress2\": \"Apt 567\",\n"
                + "      \"shippingCity\": \"Seattle\",\n"
                + "      \"shippingCountry\": \"US\",\n"
                + "      \"shippingEmail\": \"amirah.tan@example.com\",\n"
                + "      \"shippingFromZip\": \"\",\n"
                + "      \"shippingPhone\": \"\",\n"
                + "      \"shippingState\": \"WA\",\n"
                + "      \"shippingZip\": \"98101\",\n"
                + "      \"tax\": 0\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 77.22,\n"
                + "    \"totalNetAmount\": 77.22,\n"
                + "    \"totalPages\": 2,\n"
                + "    \"totalRecords\": 2\n"
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
    public void testSendInvoice() throws Exception {
        server.enqueue(
                new MockResponse().setResponseCode(200).setBody("{\"isSuccess\":true,\"responseText\":\"Success\"}"));
        SendInvoiceResponse response = client.invoice()
                .sendInvoice(
                        23548884,
                        SendInvoiceRequest.builder()
                                .attachfile(true)
                                .mail2("tamara@example.com")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                "" + "{\n" + "  \"isSuccess\": true,\n" + "  \"responseText\": \"Success\"\n" + "}";
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
    public void testGetInvoicePdf() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"key\":\"value\"}"));
        Map<String, Object> response = client.invoice().getInvoicePdf(23548884);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = "" + "{\n" + "  \"key\": \"value\"\n" + "}";
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
