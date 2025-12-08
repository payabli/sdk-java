package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.bill.requests.AddBillRequest;
import io.github.payabli.api.resources.bill.requests.DeleteAttachedFromBillRequest;
import io.github.payabli.api.resources.bill.requests.GetAttachedFromBillRequest;
import io.github.payabli.api.resources.bill.requests.ListBillsOrgRequest;
import io.github.payabli.api.resources.bill.requests.ListBillsRequest;
import io.github.payabli.api.resources.bill.requests.SendToApprovalBillRequest;
import io.github.payabli.api.resources.bill.requests.SetApprovedBillRequest;
import io.github.payabli.api.resources.bill.types.BillOutData;
import io.github.payabli.api.resources.bill.types.BillResponse;
import io.github.payabli.api.resources.bill.types.EditBillResponse;
import io.github.payabli.api.resources.bill.types.GetBillResponse;
import io.github.payabli.api.resources.bill.types.ModifyApprovalBillResponse;
import io.github.payabli.api.resources.bill.types.SetApprovedBillResponse;
import io.github.payabli.api.types.BillItem;
import io.github.payabli.api.types.BillQueryResponse;
import io.github.payabli.api.types.FileContent;
import io.github.payabli.api.types.FileContentFtype;
import io.github.payabli.api.types.Frequency;
import io.github.payabli.api.types.VendorData;
import java.util.Arrays;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BillWireTest {
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
    public void testAddBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":6101}"));
        BillResponse response = client.bill()
                .addBill(
                        "8cfec329267",
                        AddBillRequest.builder()
                                .body(BillOutData.builder()
                                        .accountingField1("MyInternalId")
                                        .attachments(Optional.of(Arrays.asList(FileContent.builder()
                                                .filename("my-doc.pdf")
                                                .ftype(FileContentFtype.PDF)
                                                .furl("https://mysite.com/my-doc.pdf")
                                                .build())))
                                        .billDate("2024-07-01")
                                        .billItems(Optional.of(Arrays.asList(BillItem.builder()
                                                .itemCost(5.0)
                                                .itemCategories(Optional.of(Arrays.asList(Optional.of("deposits"))))
                                                .itemCommodityCode("010")
                                                .itemDescription("Deposit for materials")
                                                .itemMode(0)
                                                .itemProductCode("M-DEPOSIT")
                                                .itemProductName("Materials deposit")
                                                .itemQty(1)
                                                .itemTaxAmount(7.0)
                                                .itemTaxRate(0.075)
                                                .itemTotalAmount(123.0)
                                                .itemUnitOfMeasure("SqFt")
                                                .build())))
                                        .billNumber("ABC-123")
                                        .comments("Deposit for materials")
                                        .dueDate("2024-07-01")
                                        .endDate("2024-07-01")
                                        .frequency(Frequency.MONTHLY)
                                        .mode(0)
                                        .netAmount(3762.87)
                                        .status(-99)
                                        .terms("NET30")
                                        .vendor(VendorData.builder()
                                                .vendorNumber("1234-A")
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
                + "  \"billNumber\": \"ABC-123\",\n"
                + "  \"netAmount\": 3762.87,\n"
                + "  \"billDate\": \"2024-07-01\",\n"
                + "  \"dueDate\": \"2024-07-01\",\n"
                + "  \"comments\": \"Deposit for materials\",\n"
                + "  \"billItems\": [\n"
                + "    {\n"
                + "      \"itemProductCode\": \"M-DEPOSIT\",\n"
                + "      \"itemProductName\": \"Materials deposit\",\n"
                + "      \"itemDescription\": \"Deposit for materials\",\n"
                + "      \"itemCommodityCode\": \"010\",\n"
                + "      \"itemUnitOfMeasure\": \"SqFt\",\n"
                + "      \"itemCost\": 5,\n"
                + "      \"itemQty\": 1,\n"
                + "      \"itemMode\": 0,\n"
                + "      \"itemCategories\": [\n"
                + "        \"deposits\"\n"
                + "      ],\n"
                + "      \"itemTotalAmount\": 123,\n"
                + "      \"itemTaxAmount\": 7,\n"
                + "      \"itemTaxRate\": 0.075\n"
                + "    }\n"
                + "  ],\n"
                + "  \"mode\": 0,\n"
                + "  \"accountingField1\": \"MyInternalId\",\n"
                + "  \"vendor\": {\n"
                + "    \"vendorNumber\": \"1234-A\"\n"
                + "  },\n"
                + "  \"endDate\": \"2024-07-01\",\n"
                + "  \"frequency\": \"monthly\",\n"
                + "  \"terms\": \"NET30\",\n"
                + "  \"status\": -99,\n"
                + "  \"attachments\": [\n"
                + "    {\n"
                + "      \"ftype\": \"pdf\",\n"
                + "      \"filename\": \"my-doc.pdf\",\n"
                + "      \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "    }\n"
                + "  ]\n"
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
                + "  \"responseData\": 6101\n"
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
    public void testDeleteAttachedFromBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":6101}"));
        BillResponse response = client.bill()
                .deleteAttachedFromBill(
                        285,
                        "0_Bill.pdf",
                        DeleteAttachedFromBillRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

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
                + "  \"responseData\": 6101\n"
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
    public void testDeleteBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":6101}"));
        BillResponse response = client.bill().deleteBill(285);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

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
                + "  \"responseData\": 6101\n"
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
    public void testEditBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":6101}"));
        EditBillResponse response = client.bill()
                .editBill(
                        285,
                        BillOutData.builder()
                                .billDate("2025-07-01")
                                .netAmount(3762.87)
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody =
                "" + "{\n" + "  \"netAmount\": 3762.87,\n" + "  \"billDate\": \"2025-07-01\"\n" + "}";
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
                + "  \"responseData\": 6101\n"
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
    public void testGetAttachedFromBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"}"));
        FileContent response = client.bill()
                .getAttachedFromBill(
                        285,
                        "0_Bill.pdf",
                        GetAttachedFromBillRequest.builder().returnObject(true).build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "  \"filename\": \"my-doc.pdf\",\n"
                + "  \"ftype\": \"pdf\",\n"
                + "  \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
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
    public void testGetBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":{\"IdBill\":285,\"BillNumber\":\"ABC-123\",\"NetAmount\":100,\"Discount\":null,\"TotalAmount\":100,\"BillDate\":\"2025-07-01\",\"DueDate\":\"2025-07-01\",\"Comments\":\"Deposit for materials\",\"BatchNumber\":null,\"BillItems\":[{\"itemTotalAmount\":123,\"itemTaxAmount\":7,\"itemTaxRate\":0.075,\"itemProductCode\":\"M-DEPOSIT\",\"itemProductName\":\"Materials deposit\",\"itemDescription\":\"Deposit for materials.\",\"itemCommodityCode\":\"010\",\"itemUnitOfMeasure\":\"SqFt\",\"itemCost\":5,\"itemQty\":1,\"itemMode\":0,\"itemCategories\":null}],\"Mode\":0,\"PaymentMethod\":null,\"PaymentId\":null,\"AccountingField1\":\"MyInternalId\",\"AccountingField2\":\"MyInternalId\",\"Terms\":null,\"Source\":null,\"AdditionalData\":null,\"Vendor\":{\"VendorNumber\":\"1234\",\"Name1\":\"Herman's Coatings and Masonry\",\"Name2\":\"\",\"EIN\":\"XXXX6789\",\"Phone\":\"5555555555\",\"Email\":\"contact@hermanscoatings.com\",\"RemitEmail\":null,\"Address1\":\"123 Ocean Drive\",\"Address2\":\"Suite 400\",\"City\":\"Miami\",\"State\":\"FL\",\"Zip\":\"33139\",\"Country\":\"US\",\"Mcc\":\"7777\",\"LocationCode\":\"MIA123\",\"Contacts\":[{\"ContactName\":\"Herman Martinez\",\"ContactEmail\":\"herman@hermanscoatings.com\",\"ContactTitle\":\"Owner\",\"ContactPhone\":\"3055550000\"}],\"BillingData\":{\"id\":123,\"accountId\":null,\"nickname\":\"Checking Account\",\"bankName\":\"Country Bank\",\"routingAccount\":\"123123123\",\"accountNumber\":\"1XXXXXX3123\",\"typeAccount\":\"Checking\",\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"bankAccountHolderType\":\"Business\",\"bankAccountFunction\":0,\"verified\":true,\"status\":1,\"services\":[],\"default\":true},\"PaymentMethod\":\"vcard\",\"VendorStatus\":1,\"VendorId\":1234,\"EnrollmentStatus\":null,\"Summary\":{\"ActiveBills\":5,\"PendingBills\":2,\"InTransitBills\":1,\"PaidBills\":10,\"OverdueBills\":0,\"ApprovedBills\":3,\"DisapprovedBills\":0,\"TotalBills\":21,\"ActiveBillsAmount\":1500,\"PendingBillsAmount\":500,\"InTransitBillsAmount\":200,\"PaidBillsAmount\":3000,\"OverdueBillsAmount\":0,\"ApprovedBillsAmount\":800,\"DisapprovedBillsAmount\":0,\"TotalBillsAmount\":6000},\"PaypointLegalname\":\"Gruzya Adventure Outfitters LLC\",\"PaypointDbaname\":\"Gruzya Adventure Outfitters\",\"PaypointEntryname\":\"41035afaa7\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":1232,\"CreatedDate\":\"2022-07-01T15:00:01Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"remitAddress1\":\"123 Walnut Street\",\"remitAddress2\":\"Suite 900\",\"remitCity\":\"Miami\",\"remitState\":\"FL\",\"remitZip\":\"31113\",\"remitCountry\":\"US\",\"payeeName1\":\"Herman Martinez\",\"payeeName2\":\"\",\"customField1\":\"\",\"customField2\":\"\",\"customerVendorAccount\":\"A-37622\",\"InternalReferenceId\":123,\"additionalData\":{\"customField\":\"Custom Value 1\",\"reference\":\"REF-12345\",\"notes\":\"Additional vendor information\"},\"externalPaypointID\":\"ext123\",\"StoredMethods\":[]},\"Status\":-99,\"CreatedAt\":\"2025-07-01T15:00:01Z\",\"EndDate\":null,\"LastUpdated\":\"2025-07-01T15:00:01Z\",\"Frequency\":null,\"billEvents\":[{\"description\":\"Created Bill\",\"eventTime\":\"2025-07-01T15:00:01Z\",\"refData\":\"REF-12345\",\"extraData\":null,\"source\":\"API\"}],\"billApprovals\":null,\"PaypointLegalname\":\"Gruzya Adventure Outfitters LLC\",\"PaypointDbaname\":\"Gruzya Adventure Outfitters\",\"ParentOrgId\":1232,\"ParentOrgName\":\"Pilgrim Planner\",\"PaypointEntryname\":\"41035afaa7\",\"paylinkId\":null,\"DocumentsRef\":{\"zipfile\":\"documents_285.zip\",\"filelist\":[{\"originalName\":\"invoice.pdf\",\"zipName\":\"0_invoice.pdf\",\"descriptor\":\"Invoice document\"}]},\"externalPaypointID\":null,\"LotNumber\":\"LOT-285\",\"EntityID\":null}}"));
        GetBillResponse response = client.bill().getBill(285);
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
                + "    \"IdBill\": 285,\n"
                + "    \"BillNumber\": \"ABC-123\",\n"
                + "    \"NetAmount\": 100,\n"
                + "    \"Discount\": null,\n"
                + "    \"TotalAmount\": 100,\n"
                + "    \"BillDate\": \"2025-07-01\",\n"
                + "    \"DueDate\": \"2025-07-01\",\n"
                + "    \"Comments\": \"Deposit for materials\",\n"
                + "    \"BatchNumber\": null,\n"
                + "    \"BillItems\": [\n"
                + "      {\n"
                + "        \"itemTotalAmount\": 123,\n"
                + "        \"itemTaxAmount\": 7,\n"
                + "        \"itemTaxRate\": 0.075,\n"
                + "        \"itemProductCode\": \"M-DEPOSIT\",\n"
                + "        \"itemProductName\": \"Materials deposit\",\n"
                + "        \"itemDescription\": \"Deposit for materials.\",\n"
                + "        \"itemCommodityCode\": \"010\",\n"
                + "        \"itemUnitOfMeasure\": \"SqFt\",\n"
                + "        \"itemCost\": 5,\n"
                + "        \"itemQty\": 1,\n"
                + "        \"itemMode\": 0,\n"
                + "        \"itemCategories\": null\n"
                + "      }\n"
                + "    ],\n"
                + "    \"Mode\": 0,\n"
                + "    \"PaymentMethod\": null,\n"
                + "    \"PaymentId\": null,\n"
                + "    \"AccountingField1\": \"MyInternalId\",\n"
                + "    \"AccountingField2\": \"MyInternalId\",\n"
                + "    \"Terms\": null,\n"
                + "    \"Source\": null,\n"
                + "    \"AdditionalData\": null,\n"
                + "    \"Vendor\": {\n"
                + "      \"VendorNumber\": \"1234\",\n"
                + "      \"Name1\": \"Herman's Coatings and Masonry\",\n"
                + "      \"Name2\": \"\",\n"
                + "      \"EIN\": \"XXXX6789\",\n"
                + "      \"Phone\": \"5555555555\",\n"
                + "      \"Email\": \"contact@hermanscoatings.com\",\n"
                + "      \"RemitEmail\": null,\n"
                + "      \"Address1\": \"123 Ocean Drive\",\n"
                + "      \"Address2\": \"Suite 400\",\n"
                + "      \"City\": \"Miami\",\n"
                + "      \"State\": \"FL\",\n"
                + "      \"Zip\": \"33139\",\n"
                + "      \"Country\": \"US\",\n"
                + "      \"Mcc\": \"7777\",\n"
                + "      \"LocationCode\": \"MIA123\",\n"
                + "      \"Contacts\": [\n"
                + "        {\n"
                + "          \"ContactName\": \"Herman Martinez\",\n"
                + "          \"ContactEmail\": \"herman@hermanscoatings.com\",\n"
                + "          \"ContactTitle\": \"Owner\",\n"
                + "          \"ContactPhone\": \"3055550000\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BillingData\": {\n"
                + "        \"id\": 123,\n"
                + "        \"accountId\": null,\n"
                + "        \"nickname\": \"Checking Account\",\n"
                + "        \"bankName\": \"Country Bank\",\n"
                + "        \"routingAccount\": \"123123123\",\n"
                + "        \"accountNumber\": \"1XXXXXX3123\",\n"
                + "        \"typeAccount\": \"Checking\",\n"
                + "        \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "        \"bankAccountHolderType\": \"Business\",\n"
                + "        \"bankAccountFunction\": 0,\n"
                + "        \"verified\": true,\n"
                + "        \"status\": 1,\n"
                + "        \"services\": [],\n"
                + "        \"default\": true\n"
                + "      },\n"
                + "      \"PaymentMethod\": \"vcard\",\n"
                + "      \"VendorStatus\": 1,\n"
                + "      \"VendorId\": 1234,\n"
                + "      \"EnrollmentStatus\": null,\n"
                + "      \"Summary\": {\n"
                + "        \"ActiveBills\": 5,\n"
                + "        \"PendingBills\": 2,\n"
                + "        \"InTransitBills\": 1,\n"
                + "        \"PaidBills\": 10,\n"
                + "        \"OverdueBills\": 0,\n"
                + "        \"ApprovedBills\": 3,\n"
                + "        \"DisapprovedBills\": 0,\n"
                + "        \"TotalBills\": 21,\n"
                + "        \"ActiveBillsAmount\": 1500,\n"
                + "        \"PendingBillsAmount\": 500,\n"
                + "        \"InTransitBillsAmount\": 200,\n"
                + "        \"PaidBillsAmount\": 3000,\n"
                + "        \"OverdueBillsAmount\": 0,\n"
                + "        \"ApprovedBillsAmount\": 800,\n"
                + "        \"DisapprovedBillsAmount\": 0,\n"
                + "        \"TotalBillsAmount\": 6000\n"
                + "      },\n"
                + "      \"PaypointLegalname\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "      \"PaypointDbaname\": \"Gruzya Adventure Outfitters\",\n"
                + "      \"PaypointEntryname\": \"41035afaa7\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 1232,\n"
                + "      \"CreatedDate\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"remitAddress1\": \"123 Walnut Street\",\n"
                + "      \"remitAddress2\": \"Suite 900\",\n"
                + "      \"remitCity\": \"Miami\",\n"
                + "      \"remitState\": \"FL\",\n"
                + "      \"remitZip\": \"31113\",\n"
                + "      \"remitCountry\": \"US\",\n"
                + "      \"payeeName1\": \"Herman Martinez\",\n"
                + "      \"payeeName2\": \"\",\n"
                + "      \"customField1\": \"\",\n"
                + "      \"customField2\": \"\",\n"
                + "      \"customerVendorAccount\": \"A-37622\",\n"
                + "      \"InternalReferenceId\": 123,\n"
                + "      \"additionalData\": {\n"
                + "        \"customField\": \"Custom Value 1\",\n"
                + "        \"reference\": \"REF-12345\",\n"
                + "        \"notes\": \"Additional vendor information\"\n"
                + "      },\n"
                + "      \"externalPaypointID\": \"ext123\",\n"
                + "      \"StoredMethods\": []\n"
                + "    },\n"
                + "    \"Status\": -99,\n"
                + "    \"CreatedAt\": \"2025-07-01T15:00:01Z\",\n"
                + "    \"EndDate\": null,\n"
                + "    \"LastUpdated\": \"2025-07-01T15:00:01Z\",\n"
                + "    \"Frequency\": null,\n"
                + "    \"billEvents\": [\n"
                + "      {\n"
                + "        \"description\": \"Created Bill\",\n"
                + "        \"eventTime\": \"2025-07-01T15:00:01Z\",\n"
                + "        \"refData\": \"REF-12345\",\n"
                + "        \"extraData\": null,\n"
                + "        \"source\": \"API\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"billApprovals\": null,\n"
                + "    \"PaypointLegalname\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "    \"PaypointDbaname\": \"Gruzya Adventure Outfitters\",\n"
                + "    \"ParentOrgId\": 1232,\n"
                + "    \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "    \"PaypointEntryname\": \"41035afaa7\",\n"
                + "    \"paylinkId\": null,\n"
                + "    \"DocumentsRef\": {\n"
                + "      \"zipfile\": \"documents_285.zip\",\n"
                + "      \"filelist\": [\n"
                + "        {\n"
                + "          \"originalName\": \"invoice.pdf\",\n"
                + "          \"zipName\": \"0_invoice.pdf\",\n"
                + "          \"descriptor\": \"Invoice document\"\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"externalPaypointID\": null,\n"
                + "    \"LotNumber\": \"LOT-285\",\n"
                + "    \"EntityID\": null\n"
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
    public void testListBills() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Summary\":{\"pageidentifier\":null,\"pageSize\":20,\"total2approval\":1,\"totalactive\":1,\"totalAmount\":1.1,\"totalamount2approval\":1.1,\"totalamountactive\":1.1,\"totalamountapproved\":1.1,\"totalamountcancel\":1.1,\"totalamountdisapproved\":1.1,\"totalamountintransit\":1.1,\"totalamountoverdue\":1.1,\"totalamountpaid\":1.1,\"totalamountsent2approval\":1.1,\"totalapproved\":1,\"totalcancel\":1,\"totaldisapproved\":1,\"totalintransit\":1,\"totaloverdue\":1,\"totalPages\":1,\"totalpaid\":1,\"totalRecords\":2,\"totalsent2approval\":1},\"Records\":[{\"AdditionalData\":null,\"billApprovals\":[{\"approved\":0,\"approvedTime\":\"2024-03-13T15:54:27Z\",\"email\":\"lisandra@example.com\",\"Id\":34},{\"approved\":0,\"approvedTime\":\"2024-03-13T15:54:27Z\",\"email\":\"jccastillo@example.com\",\"Id\":293}],\"BillDate\":\"2025-03-10\",\"billEvents\":[{\"description\":\"Created Bill\",\"eventTime\":\"2024-03-13T15:54:26Z\",\"refData\":\"00-45e1c2d8b53b72fafc4082f374e68753-ffea4ba4c2ce63ce-00\"},{\"description\":\"Sent to Approval\",\"eventTime\":\"2024-03-13T15:54:28Z\",\"refData\":\"00-086a951822211bc2eb1803ed64db9d4f-0f07e0e8c394e481-00\"}],\"BillItems\":[{\"itemCommodityCode\":\"Commod-MI-2024031926\",\"itemCost\":200,\"itemDescription\":\"Consultation price\",\"itemMode\":0,\"itemProductCode\":\"Prod-MI-2024031926\",\"itemProductName\":\"Consultation\",\"itemQty\":1,\"itemTaxAmount\":0,\"itemTaxRate\":0,\"itemTotalAmount\":200,\"itemUnitOfMeasure\":\"per each\"}],\"BillNumber\":\"MI-bill-2024031926\",\"Comments\":\"PAYBILL\",\"CreatedAt\":\"2024-03-13T15:54:26Z\",\"Discount\":0,\"DocumentsRef\":null,\"DueDate\":\"2025-03-10\",\"EndDate\":null,\"EntityID\":null,\"externalPaypointID\":\"micasa-10\",\"Frequency\":\"one-time\",\"IdBill\":6104,\"LastUpdated\":\"2024-03-13T10:54:26Z\",\"LotNumber\":\"LOT123\",\"Mode\":0,\"NetAmount\":200,\"ParentOrgId\":1001,\"ParentOrgName\":\"Fitness Hub\",\"PaymentId\":null,\"PaymentMethod\":null,\"paylinkId\":null,\"PaypointDbaname\":\"MiCasa Sports\",\"PaypointEntryname\":\"micasa\",\"PaypointLegalname\":\"MiCasa Sports LLC\",\"Source\":\"web\",\"Status\":2,\"Terms\":\"Net30\",\"TotalAmount\":200,\"Transaction\":null,\"Vendor\":{\"Address1\":\"1234 Liberdad St.\",\"Address2\":\"Suite 100\",\"BillingData\":{\"accountNumber\":\"12345XXXX\",\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Elena Gomez\",\"bankAccountHolderType\":\"Business\",\"bankName\":\"Michigan Savings Bank\",\"id\":0,\"routingAccount\":\"072000326\",\"typeAccount\":\"Checking\"},\"City\":\"Detroit\",\"Country\":\"US\",\"EIN\":\"XXXXX6789\",\"Email\":\"elenag@industriesexample.com\",\"InternalReferenceId\":1215,\"Mcc\":\"700\",\"Name1\":\"Gomez-Radulescu Industries\",\"Name2\":\"Elena\",\"Phone\":\"517-555-0123\",\"State\":\"MI\",\"VendorId\":8723,\"VendorNumber\":\"MI-vendor-2024031926\",\"VendorStatus\":1,\"Zip\":\"48201\"}}]}"));
        BillQueryResponse response = client.bill()
                .listBills(
                        "8cfec329267",
                        ListBillsRequest.builder()
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
                + "  \"Summary\": {\n"
                + "    \"pageidentifier\": null,\n"
                + "    \"pageSize\": 20,\n"
                + "    \"total2approval\": 1,\n"
                + "    \"totalactive\": 1,\n"
                + "    \"totalAmount\": 1.1,\n"
                + "    \"totalamount2approval\": 1.1,\n"
                + "    \"totalamountactive\": 1.1,\n"
                + "    \"totalamountapproved\": 1.1,\n"
                + "    \"totalamountcancel\": 1.1,\n"
                + "    \"totalamountdisapproved\": 1.1,\n"
                + "    \"totalamountintransit\": 1.1,\n"
                + "    \"totalamountoverdue\": 1.1,\n"
                + "    \"totalamountpaid\": 1.1,\n"
                + "    \"totalamountsent2approval\": 1.1,\n"
                + "    \"totalapproved\": 1,\n"
                + "    \"totalcancel\": 1,\n"
                + "    \"totaldisapproved\": 1,\n"
                + "    \"totalintransit\": 1,\n"
                + "    \"totaloverdue\": 1,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalpaid\": 1,\n"
                + "    \"totalRecords\": 2,\n"
                + "    \"totalsent2approval\": 1\n"
                + "  },\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AdditionalData\": null,\n"
                + "      \"billApprovals\": [\n"
                + "        {\n"
                + "          \"approved\": 0,\n"
                + "          \"approvedTime\": \"2024-03-13T15:54:27Z\",\n"
                + "          \"email\": \"lisandra@example.com\",\n"
                + "          \"Id\": 34\n"
                + "        },\n"
                + "        {\n"
                + "          \"approved\": 0,\n"
                + "          \"approvedTime\": \"2024-03-13T15:54:27Z\",\n"
                + "          \"email\": \"jccastillo@example.com\",\n"
                + "          \"Id\": 293\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BillDate\": \"2025-03-10\",\n"
                + "      \"billEvents\": [\n"
                + "        {\n"
                + "          \"description\": \"Created Bill\",\n"
                + "          \"eventTime\": \"2024-03-13T15:54:26Z\",\n"
                + "          \"refData\": \"00-45e1c2d8b53b72fafc4082f374e68753-ffea4ba4c2ce63ce-00\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"description\": \"Sent to Approval\",\n"
                + "          \"eventTime\": \"2024-03-13T15:54:28Z\",\n"
                + "          \"refData\": \"00-086a951822211bc2eb1803ed64db9d4f-0f07e0e8c394e481-00\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BillItems\": [\n"
                + "        {\n"
                + "          \"itemCommodityCode\": \"Commod-MI-2024031926\",\n"
                + "          \"itemCost\": 200,\n"
                + "          \"itemDescription\": \"Consultation price\",\n"
                + "          \"itemMode\": 0,\n"
                + "          \"itemProductCode\": \"Prod-MI-2024031926\",\n"
                + "          \"itemProductName\": \"Consultation\",\n"
                + "          \"itemQty\": 1,\n"
                + "          \"itemTaxAmount\": 0,\n"
                + "          \"itemTaxRate\": 0,\n"
                + "          \"itemTotalAmount\": 200,\n"
                + "          \"itemUnitOfMeasure\": \"per each\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BillNumber\": \"MI-bill-2024031926\",\n"
                + "      \"Comments\": \"PAYBILL\",\n"
                + "      \"CreatedAt\": \"2024-03-13T15:54:26Z\",\n"
                + "      \"Discount\": 0,\n"
                + "      \"DocumentsRef\": null,\n"
                + "      \"DueDate\": \"2025-03-10\",\n"
                + "      \"EndDate\": null,\n"
                + "      \"EntityID\": null,\n"
                + "      \"externalPaypointID\": \"micasa-10\",\n"
                + "      \"Frequency\": \"one-time\",\n"
                + "      \"IdBill\": 6104,\n"
                + "      \"LastUpdated\": \"2024-03-13T10:54:26Z\",\n"
                + "      \"LotNumber\": \"LOT123\",\n"
                + "      \"Mode\": 0,\n"
                + "      \"NetAmount\": 200,\n"
                + "      \"ParentOrgId\": 1001,\n"
                + "      \"ParentOrgName\": \"Fitness Hub\",\n"
                + "      \"PaymentId\": null,\n"
                + "      \"PaymentMethod\": null,\n"
                + "      \"paylinkId\": null,\n"
                + "      \"PaypointDbaname\": \"MiCasa Sports\",\n"
                + "      \"PaypointEntryname\": \"micasa\",\n"
                + "      \"PaypointLegalname\": \"MiCasa Sports LLC\",\n"
                + "      \"Source\": \"web\",\n"
                + "      \"Status\": 2,\n"
                + "      \"Terms\": \"Net30\",\n"
                + "      \"TotalAmount\": 200,\n"
                + "      \"Transaction\": null,\n"
                + "      \"Vendor\": {\n"
                + "        \"Address1\": \"1234 Liberdad St.\",\n"
                + "        \"Address2\": \"Suite 100\",\n"
                + "        \"BillingData\": {\n"
                + "          \"accountNumber\": \"12345XXXX\",\n"
                + "          \"bankAccountFunction\": 0,\n"
                + "          \"bankAccountHolderName\": \"Elena Gomez\",\n"
                + "          \"bankAccountHolderType\": \"Business\",\n"
                + "          \"bankName\": \"Michigan Savings Bank\",\n"
                + "          \"id\": 0,\n"
                + "          \"routingAccount\": \"072000326\",\n"
                + "          \"typeAccount\": \"Checking\"\n"
                + "        },\n"
                + "        \"City\": \"Detroit\",\n"
                + "        \"Country\": \"US\",\n"
                + "        \"EIN\": \"XXXXX6789\",\n"
                + "        \"Email\": \"elenag@industriesexample.com\",\n"
                + "        \"InternalReferenceId\": 1215,\n"
                + "        \"Mcc\": \"700\",\n"
                + "        \"Name1\": \"Gomez-Radulescu Industries\",\n"
                + "        \"Name2\": \"Elena\",\n"
                + "        \"Phone\": \"517-555-0123\",\n"
                + "        \"State\": \"MI\",\n"
                + "        \"VendorId\": 8723,\n"
                + "        \"VendorNumber\": \"MI-vendor-2024031926\",\n"
                + "        \"VendorStatus\": 1,\n"
                + "        \"Zip\": \"48201\"\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
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
    public void testListBillsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Summary\":{\"pageidentifier\":null,\"pageSize\":20,\"total2approval\":1,\"totalactive\":1,\"totalAmount\":1.1,\"totalamount2approval\":1.1,\"totalamountactive\":1.1,\"totalamountapproved\":1.1,\"totalamountcancel\":1.1,\"totalamountdisapproved\":1.1,\"totalamountintransit\":1.1,\"totalamountoverdue\":1.1,\"totalamountpaid\":1.1,\"totalamountsent2approval\":1.1,\"totalapproved\":1,\"totalcancel\":1,\"totaldisapproved\":1,\"totalintransit\":1,\"totaloverdue\":1,\"totalPages\":1,\"totalpaid\":1,\"totalRecords\":2,\"totalsent2approval\":1},\"Records\":[{\"AdditionalData\":null,\"billApprovals\":[{\"approved\":0,\"approvedTime\":\"2024-03-13T15:54:27Z\",\"email\":\"lisandra@example.com\",\"Id\":34},{\"approved\":0,\"approvedTime\":\"2024-03-13T15:54:27Z\",\"email\":\"jccastillo@example.com\",\"Id\":293}],\"BillDate\":\"2025-03-10\",\"billEvents\":[{\"description\":\"Created Bill\",\"eventTime\":\"2024-03-13T15:54:26Z\",\"refData\":\"00-45e1c2d8b53b72fafc4082f374e68753-ffea4ba4c2ce63ce-00\"},{\"description\":\"Sent to Approval\",\"eventTime\":\"2024-03-13T15:54:28Z\",\"refData\":\"00-086a951822211bc2eb1803ed64db9d4f-0f07e0e8c394e481-00\"}],\"BillItems\":[{\"itemCommodityCode\":\"Commod-MI-2024031926\",\"itemCost\":200,\"itemDescription\":\"Consultation price\",\"itemMode\":0,\"itemProductCode\":\"Prod-MI-2024031926\",\"itemProductName\":\"Consultation\",\"itemQty\":1,\"itemTaxAmount\":0,\"itemTaxRate\":0,\"itemTotalAmount\":200,\"itemUnitOfMeasure\":\"per each\"}],\"BillNumber\":\"MI-bill-2024031926\",\"Comments\":\"PAYBILL\",\"CreatedAt\":\"2024-03-13T15:54:26Z\",\"Discount\":0,\"DocumentsRef\":null,\"DueDate\":\"2025-03-10\",\"EndDate\":null,\"EntityID\":null,\"externalPaypointID\":\"micasa-10\",\"Frequency\":\"one-time\",\"IdBill\":6104,\"LastUpdated\":\"2024-03-13T10:54:26Z\",\"LotNumber\":\"LOT123\",\"Mode\":0,\"NetAmount\":200,\"ParentOrgId\":1001,\"ParentOrgName\":\"Fitness Hub\",\"PaymentId\":null,\"PaymentMethod\":null,\"paylinkId\":null,\"PaypointDbaname\":\"MiCasa Sports\",\"PaypointEntryname\":\"micasa\",\"PaypointLegalname\":\"MiCasa Sports LLC\",\"Source\":\"web\",\"Status\":2,\"Terms\":\"Net30\",\"TotalAmount\":200,\"Transaction\":null,\"Vendor\":{\"Address1\":\"1234 Liberdad St.\",\"Address2\":\"Suite 100\",\"BillingData\":{\"accountNumber\":\"12345XXXX\",\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Elena Gomez\",\"bankAccountHolderType\":\"Business\",\"bankName\":\"Michigan Savings Bank\",\"id\":0,\"routingAccount\":\"072000326\",\"typeAccount\":\"Checking\"},\"City\":\"Detroit\",\"Country\":\"US\",\"EIN\":\"XXXXX6789\",\"Email\":\"elenag@industriesexample.com\",\"InternalReferenceId\":1215,\"Mcc\":\"700\",\"Name1\":\"Gomez-Radulescu Industries\",\"Name2\":\"Elena\",\"Phone\":\"517-555-0123\",\"State\":\"MI\",\"VendorId\":8723,\"VendorNumber\":\"MI-vendor-2024031926\",\"VendorStatus\":1,\"Zip\":\"48201\"}}]}"));
        BillQueryResponse response = client.bill()
                .listBillsOrg(
                        123,
                        ListBillsOrgRequest.builder()
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
                + "  \"Summary\": {\n"
                + "    \"pageidentifier\": null,\n"
                + "    \"pageSize\": 20,\n"
                + "    \"total2approval\": 1,\n"
                + "    \"totalactive\": 1,\n"
                + "    \"totalAmount\": 1.1,\n"
                + "    \"totalamount2approval\": 1.1,\n"
                + "    \"totalamountactive\": 1.1,\n"
                + "    \"totalamountapproved\": 1.1,\n"
                + "    \"totalamountcancel\": 1.1,\n"
                + "    \"totalamountdisapproved\": 1.1,\n"
                + "    \"totalamountintransit\": 1.1,\n"
                + "    \"totalamountoverdue\": 1.1,\n"
                + "    \"totalamountpaid\": 1.1,\n"
                + "    \"totalamountsent2approval\": 1.1,\n"
                + "    \"totalapproved\": 1,\n"
                + "    \"totalcancel\": 1,\n"
                + "    \"totaldisapproved\": 1,\n"
                + "    \"totalintransit\": 1,\n"
                + "    \"totaloverdue\": 1,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalpaid\": 1,\n"
                + "    \"totalRecords\": 2,\n"
                + "    \"totalsent2approval\": 1\n"
                + "  },\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AdditionalData\": null,\n"
                + "      \"billApprovals\": [\n"
                + "        {\n"
                + "          \"approved\": 0,\n"
                + "          \"approvedTime\": \"2024-03-13T15:54:27Z\",\n"
                + "          \"email\": \"lisandra@example.com\",\n"
                + "          \"Id\": 34\n"
                + "        },\n"
                + "        {\n"
                + "          \"approved\": 0,\n"
                + "          \"approvedTime\": \"2024-03-13T15:54:27Z\",\n"
                + "          \"email\": \"jccastillo@example.com\",\n"
                + "          \"Id\": 293\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BillDate\": \"2025-03-10\",\n"
                + "      \"billEvents\": [\n"
                + "        {\n"
                + "          \"description\": \"Created Bill\",\n"
                + "          \"eventTime\": \"2024-03-13T15:54:26Z\",\n"
                + "          \"refData\": \"00-45e1c2d8b53b72fafc4082f374e68753-ffea4ba4c2ce63ce-00\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"description\": \"Sent to Approval\",\n"
                + "          \"eventTime\": \"2024-03-13T15:54:28Z\",\n"
                + "          \"refData\": \"00-086a951822211bc2eb1803ed64db9d4f-0f07e0e8c394e481-00\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BillItems\": [\n"
                + "        {\n"
                + "          \"itemCommodityCode\": \"Commod-MI-2024031926\",\n"
                + "          \"itemCost\": 200,\n"
                + "          \"itemDescription\": \"Consultation price\",\n"
                + "          \"itemMode\": 0,\n"
                + "          \"itemProductCode\": \"Prod-MI-2024031926\",\n"
                + "          \"itemProductName\": \"Consultation\",\n"
                + "          \"itemQty\": 1,\n"
                + "          \"itemTaxAmount\": 0,\n"
                + "          \"itemTaxRate\": 0,\n"
                + "          \"itemTotalAmount\": 200,\n"
                + "          \"itemUnitOfMeasure\": \"per each\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BillNumber\": \"MI-bill-2024031926\",\n"
                + "      \"Comments\": \"PAYBILL\",\n"
                + "      \"CreatedAt\": \"2024-03-13T15:54:26Z\",\n"
                + "      \"Discount\": 0,\n"
                + "      \"DocumentsRef\": null,\n"
                + "      \"DueDate\": \"2025-03-10\",\n"
                + "      \"EndDate\": null,\n"
                + "      \"EntityID\": null,\n"
                + "      \"externalPaypointID\": \"micasa-10\",\n"
                + "      \"Frequency\": \"one-time\",\n"
                + "      \"IdBill\": 6104,\n"
                + "      \"LastUpdated\": \"2024-03-13T10:54:26Z\",\n"
                + "      \"LotNumber\": \"LOT123\",\n"
                + "      \"Mode\": 0,\n"
                + "      \"NetAmount\": 200,\n"
                + "      \"ParentOrgId\": 1001,\n"
                + "      \"ParentOrgName\": \"Fitness Hub\",\n"
                + "      \"PaymentId\": null,\n"
                + "      \"PaymentMethod\": null,\n"
                + "      \"paylinkId\": null,\n"
                + "      \"PaypointDbaname\": \"MiCasa Sports\",\n"
                + "      \"PaypointEntryname\": \"micasa\",\n"
                + "      \"PaypointLegalname\": \"MiCasa Sports LLC\",\n"
                + "      \"Source\": \"web\",\n"
                + "      \"Status\": 2,\n"
                + "      \"Terms\": \"Net30\",\n"
                + "      \"TotalAmount\": 200,\n"
                + "      \"Transaction\": null,\n"
                + "      \"Vendor\": {\n"
                + "        \"Address1\": \"1234 Liberdad St.\",\n"
                + "        \"Address2\": \"Suite 100\",\n"
                + "        \"BillingData\": {\n"
                + "          \"accountNumber\": \"12345XXXX\",\n"
                + "          \"bankAccountFunction\": 0,\n"
                + "          \"bankAccountHolderName\": \"Elena Gomez\",\n"
                + "          \"bankAccountHolderType\": \"Business\",\n"
                + "          \"bankName\": \"Michigan Savings Bank\",\n"
                + "          \"id\": 0,\n"
                + "          \"routingAccount\": \"072000326\",\n"
                + "          \"typeAccount\": \"Checking\"\n"
                + "        },\n"
                + "        \"City\": \"Detroit\",\n"
                + "        \"Country\": \"US\",\n"
                + "        \"EIN\": \"XXXXX6789\",\n"
                + "        \"Email\": \"elenag@industriesexample.com\",\n"
                + "        \"InternalReferenceId\": 1215,\n"
                + "        \"Mcc\": \"700\",\n"
                + "        \"Name1\": \"Gomez-Radulescu Industries\",\n"
                + "        \"Name2\": \"Elena\",\n"
                + "        \"Phone\": \"517-555-0123\",\n"
                + "        \"State\": \"MI\",\n"
                + "        \"VendorId\": 8723,\n"
                + "        \"VendorNumber\": \"MI-vendor-2024031926\",\n"
                + "        \"VendorStatus\": 1,\n"
                + "        \"Zip\": \"48201\"\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
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
    public void testModifyApprovalBill() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":6101,\"responseText\":\"Success\"}"));
        ModifyApprovalBillResponse response = client.bill().modifyApprovalBill(285, Arrays.asList("string"));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "[\n" + "  \"string\"\n" + "]";
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
                + "  \"responseData\": 6101,\n"
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
    public void testSendToApprovalBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"responseCode\":1,\"pageIdentifier\":null,\"roomId\":0,\"isSuccess\":true,\"responseText\":\"Success\",\"responseData\":6101}"));
        BillResponse response = client.bill()
                .sendToApprovalBill(
                        285,
                        SendToApprovalBillRequest.builder()
                                .body(Arrays.asList("string"))
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
        String expectedRequestBody = "" + "[\n" + "  \"string\"\n" + "]";
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
                + "  \"responseData\": 6101\n"
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
    public void testSetApprovedBill() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":6101,\"responseText\":\"Success\"}"));
        SetApprovedBillResponse response = client.bill()
                .setApprovedBill(285, "true", SetApprovedBillRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": 6101,\n"
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
