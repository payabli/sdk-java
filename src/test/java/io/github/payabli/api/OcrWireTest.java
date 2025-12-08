package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.ocr.types.FileContentImageOnly;
import io.github.payabli.api.resources.ocr.types.PayabliApiResponseOcr;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OcrWireTest {
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
    public void testOcrDocumentForm() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseText\":\"responseText\",\"responseCode\":1,\"responseData\":{\"resultData\":{\"billNumber\":\"billNumber\",\"netAmount\":1.1,\"billDate\":\"2024-01-15T09:30:00Z\",\"dueDate\":\"2024-01-15T09:30:00Z\",\"comments\":\"comments\",\"billItems\":[{\"itemTotalAmount\":1.1,\"itemTaxAmount\":1.1,\"itemTaxRate\":1.1,\"itemProductCode\":\"itemProductCode\",\"itemProductName\":\"itemProductName\",\"itemDescription\":\"itemDescription\",\"itemCommodityCode\":\"itemCommodityCode\",\"itemUnitOfMeasure\":\"itemUnitOfMeasure\",\"itemCost\":1.1,\"itemQty\":1,\"itemMode\":1,\"itemCategories\":[\"itemCategories\",\"itemCategories\"]},{\"itemTotalAmount\":1.1,\"itemTaxAmount\":1.1,\"itemTaxRate\":1.1,\"itemProductCode\":\"itemProductCode\",\"itemProductName\":\"itemProductName\",\"itemDescription\":\"itemDescription\",\"itemCommodityCode\":\"itemCommodityCode\",\"itemUnitOfMeasure\":\"itemUnitOfMeasure\",\"itemCost\":1.1,\"itemQty\":1,\"itemMode\":1,\"itemCategories\":[\"itemCategories\",\"itemCategories\"]}],\"mode\":1,\"accountingField1\":\"accountingField1\",\"accountingField2\":\"accountingField2\",\"additionalData\":{\"category\":\"category\",\"currency_code\":\"currency_code\",\"type\":\"type\",\"reference_number\":\"reference_number\"},\"vendor\":{\"vendorNumber\":\"vendorNumber\",\"name1\":\"name1\",\"name2\":\"name2\",\"ein\":\"ein\",\"phone\":\"phone\",\"email\":\"email\",\"address1\":\"address1\",\"address2\":\"address2\",\"city\":\"city\",\"state\":\"state\",\"zip\":\"zip\",\"country\":\"country\",\"mcc\":\"mcc\",\"locationCode\":\"locationCode\",\"contacts\":[{},{}],\"billingData\":{\"id\":1,\"bankName\":\"bankName\",\"routingAccount\":\"routingAccount\",\"accountNumber\":\"accountNumber\",\"typeAccount\":\"typeAccount\",\"bankAccountHolderName\":\"bankAccountHolderName\",\"bankAccountHolderType\":\"bankAccountHolderType\",\"bankAccountFunction\":1},\"paymentMethod\":\"paymentMethod\",\"vendorStatus\":1,\"remitAddress1\":\"remitAddress1\",\"remitAddress2\":\"remitAddress2\",\"remitCity\":\"remitCity\",\"remitState\":\"remitState\",\"remitZip\":\"remitZip\",\"remitCountry\":\"remitCountry\",\"payeeName1\":\"payeeName1\",\"payeeName2\":\"payeeName2\",\"customerVendorAccount\":\"customerVendorAccount\",\"internalReferenceId\":1000000,\"customField1\":\"customField1\",\"customField2\":\"customField2\",\"additionalData\":{\"web\":\"web\"}},\"endDate\":\"2024-01-15T09:30:00Z\",\"frequency\":\"frequency\",\"terms\":\"terms\",\"status\":1,\"lotNumber\":\"lotNumber\",\"attachments\":[{\"ftype\":\"ftype\",\"filename\":\"filename\",\"fileDescriptor\":\"fileDescriptor\",\"furl\":\"furl\",\"fContent\":\"fContent\"},{\"ftype\":\"ftype\",\"filename\":\"filename\",\"fileDescriptor\":\"fileDescriptor\",\"furl\":\"furl\",\"fContent\":\"fContent\"}]}}}"));
        PayabliApiResponseOcr response = client.ocr()
                .ocrDocumentForm("typeResult", FileContentImageOnly.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
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
                + "  \"responseText\": \"responseText\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"resultData\": {\n"
                + "      \"billNumber\": \"billNumber\",\n"
                + "      \"netAmount\": 1.1,\n"
                + "      \"billDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"dueDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"comments\": \"comments\",\n"
                + "      \"billItems\": [\n"
                + "        {\n"
                + "          \"itemTotalAmount\": 1.1,\n"
                + "          \"itemTaxAmount\": 1.1,\n"
                + "          \"itemTaxRate\": 1.1,\n"
                + "          \"itemProductCode\": \"itemProductCode\",\n"
                + "          \"itemProductName\": \"itemProductName\",\n"
                + "          \"itemDescription\": \"itemDescription\",\n"
                + "          \"itemCommodityCode\": \"itemCommodityCode\",\n"
                + "          \"itemUnitOfMeasure\": \"itemUnitOfMeasure\",\n"
                + "          \"itemCost\": 1.1,\n"
                + "          \"itemQty\": 1,\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemCategories\": [\n"
                + "            \"itemCategories\",\n"
                + "            \"itemCategories\"\n"
                + "          ]\n"
                + "        },\n"
                + "        {\n"
                + "          \"itemTotalAmount\": 1.1,\n"
                + "          \"itemTaxAmount\": 1.1,\n"
                + "          \"itemTaxRate\": 1.1,\n"
                + "          \"itemProductCode\": \"itemProductCode\",\n"
                + "          \"itemProductName\": \"itemProductName\",\n"
                + "          \"itemDescription\": \"itemDescription\",\n"
                + "          \"itemCommodityCode\": \"itemCommodityCode\",\n"
                + "          \"itemUnitOfMeasure\": \"itemUnitOfMeasure\",\n"
                + "          \"itemCost\": 1.1,\n"
                + "          \"itemQty\": 1,\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemCategories\": [\n"
                + "            \"itemCategories\",\n"
                + "            \"itemCategories\"\n"
                + "          ]\n"
                + "        }\n"
                + "      ],\n"
                + "      \"mode\": 1,\n"
                + "      \"accountingField1\": \"accountingField1\",\n"
                + "      \"accountingField2\": \"accountingField2\",\n"
                + "      \"additionalData\": {\n"
                + "        \"category\": \"category\",\n"
                + "        \"currency_code\": \"currency_code\",\n"
                + "        \"type\": \"type\",\n"
                + "        \"reference_number\": \"reference_number\"\n"
                + "      },\n"
                + "      \"vendor\": {\n"
                + "        \"vendorNumber\": \"vendorNumber\",\n"
                + "        \"name1\": \"name1\",\n"
                + "        \"name2\": \"name2\",\n"
                + "        \"ein\": \"ein\",\n"
                + "        \"phone\": \"phone\",\n"
                + "        \"email\": \"email\",\n"
                + "        \"address1\": \"address1\",\n"
                + "        \"address2\": \"address2\",\n"
                + "        \"city\": \"city\",\n"
                + "        \"state\": \"state\",\n"
                + "        \"zip\": \"zip\",\n"
                + "        \"country\": \"country\",\n"
                + "        \"mcc\": \"mcc\",\n"
                + "        \"locationCode\": \"locationCode\",\n"
                + "        \"contacts\": [\n"
                + "          {},\n"
                + "          {}\n"
                + "        ],\n"
                + "        \"billingData\": {\n"
                + "          \"id\": 1,\n"
                + "          \"bankName\": \"bankName\",\n"
                + "          \"routingAccount\": \"routingAccount\",\n"
                + "          \"accountNumber\": \"accountNumber\",\n"
                + "          \"typeAccount\": \"typeAccount\",\n"
                + "          \"bankAccountHolderName\": \"bankAccountHolderName\",\n"
                + "          \"bankAccountHolderType\": \"bankAccountHolderType\",\n"
                + "          \"bankAccountFunction\": 1\n"
                + "        },\n"
                + "        \"paymentMethod\": \"paymentMethod\",\n"
                + "        \"vendorStatus\": 1,\n"
                + "        \"remitAddress1\": \"remitAddress1\",\n"
                + "        \"remitAddress2\": \"remitAddress2\",\n"
                + "        \"remitCity\": \"remitCity\",\n"
                + "        \"remitState\": \"remitState\",\n"
                + "        \"remitZip\": \"remitZip\",\n"
                + "        \"remitCountry\": \"remitCountry\",\n"
                + "        \"payeeName1\": \"payeeName1\",\n"
                + "        \"payeeName2\": \"payeeName2\",\n"
                + "        \"customerVendorAccount\": \"customerVendorAccount\",\n"
                + "        \"internalReferenceId\": 1000000,\n"
                + "        \"customField1\": \"customField1\",\n"
                + "        \"customField2\": \"customField2\",\n"
                + "        \"additionalData\": {\n"
                + "          \"web\": \"web\"\n"
                + "        }\n"
                + "      },\n"
                + "      \"endDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"frequency\": \"frequency\",\n"
                + "      \"terms\": \"terms\",\n"
                + "      \"status\": 1,\n"
                + "      \"lotNumber\": \"lotNumber\",\n"
                + "      \"attachments\": [\n"
                + "        {\n"
                + "          \"ftype\": \"ftype\",\n"
                + "          \"filename\": \"filename\",\n"
                + "          \"fileDescriptor\": \"fileDescriptor\",\n"
                + "          \"furl\": \"furl\",\n"
                + "          \"fContent\": \"fContent\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"ftype\": \"ftype\",\n"
                + "          \"filename\": \"filename\",\n"
                + "          \"fileDescriptor\": \"fileDescriptor\",\n"
                + "          \"furl\": \"furl\",\n"
                + "          \"fContent\": \"fContent\"\n"
                + "        }\n"
                + "      ]\n"
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
    public void testOcrDocumentJson() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseText\":\"responseText\",\"responseCode\":1,\"responseData\":{\"resultData\":{\"billNumber\":\"billNumber\",\"netAmount\":1.1,\"billDate\":\"2024-01-15T09:30:00Z\",\"dueDate\":\"2024-01-15T09:30:00Z\",\"comments\":\"comments\",\"billItems\":[{\"itemTotalAmount\":1.1,\"itemTaxAmount\":1.1,\"itemTaxRate\":1.1,\"itemProductCode\":\"itemProductCode\",\"itemProductName\":\"itemProductName\",\"itemDescription\":\"itemDescription\",\"itemCommodityCode\":\"itemCommodityCode\",\"itemUnitOfMeasure\":\"itemUnitOfMeasure\",\"itemCost\":1.1,\"itemQty\":1,\"itemMode\":1,\"itemCategories\":[\"itemCategories\",\"itemCategories\"]},{\"itemTotalAmount\":1.1,\"itemTaxAmount\":1.1,\"itemTaxRate\":1.1,\"itemProductCode\":\"itemProductCode\",\"itemProductName\":\"itemProductName\",\"itemDescription\":\"itemDescription\",\"itemCommodityCode\":\"itemCommodityCode\",\"itemUnitOfMeasure\":\"itemUnitOfMeasure\",\"itemCost\":1.1,\"itemQty\":1,\"itemMode\":1,\"itemCategories\":[\"itemCategories\",\"itemCategories\"]}],\"mode\":1,\"accountingField1\":\"accountingField1\",\"accountingField2\":\"accountingField2\",\"additionalData\":{\"category\":\"category\",\"currency_code\":\"currency_code\",\"type\":\"type\",\"reference_number\":\"reference_number\"},\"vendor\":{\"vendorNumber\":\"vendorNumber\",\"name1\":\"name1\",\"name2\":\"name2\",\"ein\":\"ein\",\"phone\":\"phone\",\"email\":\"email\",\"address1\":\"address1\",\"address2\":\"address2\",\"city\":\"city\",\"state\":\"state\",\"zip\":\"zip\",\"country\":\"country\",\"mcc\":\"mcc\",\"locationCode\":\"locationCode\",\"contacts\":[{},{}],\"billingData\":{\"id\":1,\"bankName\":\"bankName\",\"routingAccount\":\"routingAccount\",\"accountNumber\":\"accountNumber\",\"typeAccount\":\"typeAccount\",\"bankAccountHolderName\":\"bankAccountHolderName\",\"bankAccountHolderType\":\"bankAccountHolderType\",\"bankAccountFunction\":1},\"paymentMethod\":\"paymentMethod\",\"vendorStatus\":1,\"remitAddress1\":\"remitAddress1\",\"remitAddress2\":\"remitAddress2\",\"remitCity\":\"remitCity\",\"remitState\":\"remitState\",\"remitZip\":\"remitZip\",\"remitCountry\":\"remitCountry\",\"payeeName1\":\"payeeName1\",\"payeeName2\":\"payeeName2\",\"customerVendorAccount\":\"customerVendorAccount\",\"internalReferenceId\":1000000,\"customField1\":\"customField1\",\"customField2\":\"customField2\",\"additionalData\":{\"web\":\"web\"}},\"endDate\":\"2024-01-15T09:30:00Z\",\"frequency\":\"frequency\",\"terms\":\"terms\",\"status\":1,\"lotNumber\":\"lotNumber\",\"attachments\":[{\"ftype\":\"ftype\",\"filename\":\"filename\",\"fileDescriptor\":\"fileDescriptor\",\"furl\":\"furl\",\"fContent\":\"fContent\"},{\"ftype\":\"ftype\",\"filename\":\"filename\",\"fileDescriptor\":\"fileDescriptor\",\"furl\":\"furl\",\"fContent\":\"fContent\"}]}}}"));
        PayabliApiResponseOcr response = client.ocr()
                .ocrDocumentJson("typeResult", FileContentImageOnly.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
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
                + "  \"responseText\": \"responseText\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"resultData\": {\n"
                + "      \"billNumber\": \"billNumber\",\n"
                + "      \"netAmount\": 1.1,\n"
                + "      \"billDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"dueDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"comments\": \"comments\",\n"
                + "      \"billItems\": [\n"
                + "        {\n"
                + "          \"itemTotalAmount\": 1.1,\n"
                + "          \"itemTaxAmount\": 1.1,\n"
                + "          \"itemTaxRate\": 1.1,\n"
                + "          \"itemProductCode\": \"itemProductCode\",\n"
                + "          \"itemProductName\": \"itemProductName\",\n"
                + "          \"itemDescription\": \"itemDescription\",\n"
                + "          \"itemCommodityCode\": \"itemCommodityCode\",\n"
                + "          \"itemUnitOfMeasure\": \"itemUnitOfMeasure\",\n"
                + "          \"itemCost\": 1.1,\n"
                + "          \"itemQty\": 1,\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemCategories\": [\n"
                + "            \"itemCategories\",\n"
                + "            \"itemCategories\"\n"
                + "          ]\n"
                + "        },\n"
                + "        {\n"
                + "          \"itemTotalAmount\": 1.1,\n"
                + "          \"itemTaxAmount\": 1.1,\n"
                + "          \"itemTaxRate\": 1.1,\n"
                + "          \"itemProductCode\": \"itemProductCode\",\n"
                + "          \"itemProductName\": \"itemProductName\",\n"
                + "          \"itemDescription\": \"itemDescription\",\n"
                + "          \"itemCommodityCode\": \"itemCommodityCode\",\n"
                + "          \"itemUnitOfMeasure\": \"itemUnitOfMeasure\",\n"
                + "          \"itemCost\": 1.1,\n"
                + "          \"itemQty\": 1,\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemCategories\": [\n"
                + "            \"itemCategories\",\n"
                + "            \"itemCategories\"\n"
                + "          ]\n"
                + "        }\n"
                + "      ],\n"
                + "      \"mode\": 1,\n"
                + "      \"accountingField1\": \"accountingField1\",\n"
                + "      \"accountingField2\": \"accountingField2\",\n"
                + "      \"additionalData\": {\n"
                + "        \"category\": \"category\",\n"
                + "        \"currency_code\": \"currency_code\",\n"
                + "        \"type\": \"type\",\n"
                + "        \"reference_number\": \"reference_number\"\n"
                + "      },\n"
                + "      \"vendor\": {\n"
                + "        \"vendorNumber\": \"vendorNumber\",\n"
                + "        \"name1\": \"name1\",\n"
                + "        \"name2\": \"name2\",\n"
                + "        \"ein\": \"ein\",\n"
                + "        \"phone\": \"phone\",\n"
                + "        \"email\": \"email\",\n"
                + "        \"address1\": \"address1\",\n"
                + "        \"address2\": \"address2\",\n"
                + "        \"city\": \"city\",\n"
                + "        \"state\": \"state\",\n"
                + "        \"zip\": \"zip\",\n"
                + "        \"country\": \"country\",\n"
                + "        \"mcc\": \"mcc\",\n"
                + "        \"locationCode\": \"locationCode\",\n"
                + "        \"contacts\": [\n"
                + "          {},\n"
                + "          {}\n"
                + "        ],\n"
                + "        \"billingData\": {\n"
                + "          \"id\": 1,\n"
                + "          \"bankName\": \"bankName\",\n"
                + "          \"routingAccount\": \"routingAccount\",\n"
                + "          \"accountNumber\": \"accountNumber\",\n"
                + "          \"typeAccount\": \"typeAccount\",\n"
                + "          \"bankAccountHolderName\": \"bankAccountHolderName\",\n"
                + "          \"bankAccountHolderType\": \"bankAccountHolderType\",\n"
                + "          \"bankAccountFunction\": 1\n"
                + "        },\n"
                + "        \"paymentMethod\": \"paymentMethod\",\n"
                + "        \"vendorStatus\": 1,\n"
                + "        \"remitAddress1\": \"remitAddress1\",\n"
                + "        \"remitAddress2\": \"remitAddress2\",\n"
                + "        \"remitCity\": \"remitCity\",\n"
                + "        \"remitState\": \"remitState\",\n"
                + "        \"remitZip\": \"remitZip\",\n"
                + "        \"remitCountry\": \"remitCountry\",\n"
                + "        \"payeeName1\": \"payeeName1\",\n"
                + "        \"payeeName2\": \"payeeName2\",\n"
                + "        \"customerVendorAccount\": \"customerVendorAccount\",\n"
                + "        \"internalReferenceId\": 1000000,\n"
                + "        \"customField1\": \"customField1\",\n"
                + "        \"customField2\": \"customField2\",\n"
                + "        \"additionalData\": {\n"
                + "          \"web\": \"web\"\n"
                + "        }\n"
                + "      },\n"
                + "      \"endDate\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"frequency\": \"frequency\",\n"
                + "      \"terms\": \"terms\",\n"
                + "      \"status\": 1,\n"
                + "      \"lotNumber\": \"lotNumber\",\n"
                + "      \"attachments\": [\n"
                + "        {\n"
                + "          \"ftype\": \"ftype\",\n"
                + "          \"filename\": \"filename\",\n"
                + "          \"fileDescriptor\": \"fileDescriptor\",\n"
                + "          \"furl\": \"furl\",\n"
                + "          \"fContent\": \"fContent\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"ftype\": \"ftype\",\n"
                + "          \"filename\": \"filename\",\n"
                + "          \"fileDescriptor\": \"fileDescriptor\",\n"
                + "          \"furl\": \"furl\",\n"
                + "          \"fContent\": \"fContent\"\n"
                + "        }\n"
                + "      ]\n"
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
