package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.paypoint.requests.GetEntryConfigRequest;
import io.github.payabli.api.resources.paypoint.types.GetBasicEntryByIdResponse;
import io.github.payabli.api.resources.paypoint.types.GetBasicEntryResponse;
import io.github.payabli.api.resources.paypoint.types.GetEntryConfigResponse;
import io.github.payabli.api.resources.paypoint.types.MigratePaypointResponse;
import io.github.payabli.api.resources.paypoint.types.NotificationRequest;
import io.github.payabli.api.resources.paypoint.types.PaypointMoveRequest;
import io.github.payabli.api.resources.paypoint.types.WebHeaderParameter;
import io.github.payabli.api.types.FileContent;
import io.github.payabli.api.types.PayabliApiResponse00Responsedatanonobject;
import io.github.payabli.api.types.PayabliApiResponseGeneric2Part;
import io.github.payabli.api.types.PayabliPages;
import io.github.payabli.api.types.SettingsQueryRecord;
import java.util.Arrays;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaypointWireTest {
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
    public void testGetBasicEntry() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":{\"EntryName\":\"abc123def\",\"EntryPages\":[{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}}}],\"IdEntry\":11111,\"Paypoint\":{\"address1\":\"123 Ocean Drive\",\"address2\":\"Suite 400\",\"bankData\":[{\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"nickname\":\"Business Checking 1234\"}],\"boardingId\":340,\"city\":\"Bristol\",\"contacts\":[{}],\"country\":\"US\",\"credentials\":[{}],\"dbaName\":\"Sunshine Gutters\",\"externalPaypointID\":\"\",\"fax\":\"5555555555\",\"idPaypoint\":1000000,\"legalName\":\"Sunshine Services, LLC\",\"parentOrg\":{\"orgName\":\"Pilgrim Planner\",\"orgStatus\":1,\"orgType\":0},\"paypointStatus\":1,\"phone\":\"5555555555\",\"state\":\"GA\",\"summary\":{\"amountSubs\":0,\"amountTx\":0,\"countSubs\":0,\"countTx\":0,\"customers\":1},\"timeZone\":-5,\"websiteAddress\":\"www.example.com\",\"zip\":\"31113\"}},\"responseText\":\"Success\"}"));
        GetBasicEntryResponse response = client.paypoint().getBasicEntry("8cfec329267");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"EntryName\": \"abc123def\",\n"
                + "    \"EntryPages\": [\n"
                + "      {\n"
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
                + "        }\n"
                + "      }\n"
                + "    ],\n"
                + "    \"IdEntry\": 11111,\n"
                + "    \"Paypoint\": {\n"
                + "      \"address1\": \"123 Ocean Drive\",\n"
                + "      \"address2\": \"Suite 400\",\n"
                + "      \"bankData\": [\n"
                + "        {\n"
                + "          \"bankAccountFunction\": 0,\n"
                + "          \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "          \"nickname\": \"Business Checking 1234\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"boardingId\": 340,\n"
                + "      \"city\": \"Bristol\",\n"
                + "      \"contacts\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"country\": \"US\",\n"
                + "      \"credentials\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"dbaName\": \"Sunshine Gutters\",\n"
                + "      \"externalPaypointID\": \"\",\n"
                + "      \"fax\": \"5555555555\",\n"
                + "      \"idPaypoint\": 1000000,\n"
                + "      \"legalName\": \"Sunshine Services, LLC\",\n"
                + "      \"parentOrg\": {\n"
                + "        \"orgName\": \"Pilgrim Planner\",\n"
                + "        \"orgStatus\": 1,\n"
                + "        \"orgType\": 0\n"
                + "      },\n"
                + "      \"paypointStatus\": 1,\n"
                + "      \"phone\": \"5555555555\",\n"
                + "      \"state\": \"GA\",\n"
                + "      \"summary\": {\n"
                + "        \"amountSubs\": 0,\n"
                + "        \"amountTx\": 0,\n"
                + "        \"countSubs\": 0,\n"
                + "        \"countTx\": 0,\n"
                + "        \"customers\": 1\n"
                + "      },\n"
                + "      \"timeZone\": -5,\n"
                + "      \"websiteAddress\": \"www.example.com\",\n"
                + "      \"zip\": \"31113\"\n"
                + "    }\n"
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
    public void testGetBasicEntryById() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":{\"EntryName\":\"abc123def\",\"EntryPages\":[{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}}}],\"IdEntry\":11111,\"Paypoint\":{\"address1\":\"123 Ocean Drive\",\"address2\":\"Suite 400\",\"bankData\":[{\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"nickname\":\"Business Checking 1234\"}],\"boardingId\":340,\"city\":\"Bristol\",\"contacts\":[{}],\"country\":\"US\",\"credentials\":[{}],\"dbaName\":\"Sunshine Gutters\",\"externalPaypointID\":\"\",\"fax\":\"5555555555\",\"idPaypoint\":1000000,\"legalName\":\"Sunshine Services, LLC\",\"parentOrg\":{\"orgName\":\"Pilgrim Planner\",\"orgStatus\":1,\"orgType\":0},\"paypointStatus\":1,\"phone\":\"5555555555\",\"state\":\"GA\",\"summary\":{\"amountSubs\":0,\"amountTx\":0,\"countSubs\":0,\"countTx\":0,\"customers\":1},\"timeZone\":-5,\"websiteAddress\":\"www.example.com\",\"zip\":\"31113\"}},\"responseText\":\"Success\"}"));
        GetBasicEntryByIdResponse response = client.paypoint().getBasicEntryById("198");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"EntryName\": \"abc123def\",\n"
                + "    \"EntryPages\": [\n"
                + "      {\n"
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
                + "        }\n"
                + "      }\n"
                + "    ],\n"
                + "    \"IdEntry\": 11111,\n"
                + "    \"Paypoint\": {\n"
                + "      \"address1\": \"123 Ocean Drive\",\n"
                + "      \"address2\": \"Suite 400\",\n"
                + "      \"bankData\": [\n"
                + "        {\n"
                + "          \"bankAccountFunction\": 0,\n"
                + "          \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "          \"nickname\": \"Business Checking 1234\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"boardingId\": 340,\n"
                + "      \"city\": \"Bristol\",\n"
                + "      \"contacts\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"country\": \"US\",\n"
                + "      \"credentials\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"dbaName\": \"Sunshine Gutters\",\n"
                + "      \"externalPaypointID\": \"\",\n"
                + "      \"fax\": \"5555555555\",\n"
                + "      \"idPaypoint\": 1000000,\n"
                + "      \"legalName\": \"Sunshine Services, LLC\",\n"
                + "      \"parentOrg\": {\n"
                + "        \"orgName\": \"Pilgrim Planner\",\n"
                + "        \"orgStatus\": 1,\n"
                + "        \"orgType\": 0\n"
                + "      },\n"
                + "      \"paypointStatus\": 1,\n"
                + "      \"phone\": \"5555555555\",\n"
                + "      \"state\": \"GA\",\n"
                + "      \"summary\": {\n"
                + "        \"amountSubs\": 0,\n"
                + "        \"amountTx\": 0,\n"
                + "        \"countSubs\": 0,\n"
                + "        \"countTx\": 0,\n"
                + "        \"customers\": 1\n"
                + "      },\n"
                + "      \"timeZone\": -5,\n"
                + "      \"websiteAddress\": \"www.example.com\",\n"
                + "      \"zip\": \"31113\"\n"
                + "    }\n"
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
    public void testGetEntryConfig() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":{\"EntryName\":\"abc123def\",\"EntryPages\":[{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}}}],\"IdEntry\":11111,\"Paypoint\":{\"address1\":\"123 Ocean Drive\",\"address2\":\"Suite 400\",\"bankData\":[{\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"nickname\":\"Business Checking 1234\"}],\"boardingId\":340,\"city\":\"Bristol\",\"contacts\":[{}],\"country\":\"US\",\"credentials\":[{}],\"dbaName\":\"Sunshine Gutters\",\"externalPaypointID\":\"\",\"fax\":\"5555555555\",\"idPaypoint\":1000000,\"legalName\":\"Sunshine Services, LLC\",\"parentOrg\":{\"orgName\":\"Pilgrim Planner\",\"orgStatus\":1,\"orgType\":0},\"paypointStatus\":1,\"phone\":\"5555555555\",\"state\":\"GA\",\"summary\":{\"amountSubs\":0,\"amountTx\":0,\"countSubs\":0,\"countTx\":0,\"customers\":1},\"timeZone\":-5,\"websiteAddress\":\"www.example.com\",\"zip\":\"31113\"}},\"responseText\":\"Success\"}"));
        GetEntryConfigResponse response = client.paypoint()
                .getEntryConfig("8cfec329267", GetEntryConfigRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"EntryName\": \"abc123def\",\n"
                + "    \"EntryPages\": [\n"
                + "      {\n"
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
                + "        }\n"
                + "      }\n"
                + "    ],\n"
                + "    \"IdEntry\": 11111,\n"
                + "    \"Paypoint\": {\n"
                + "      \"address1\": \"123 Ocean Drive\",\n"
                + "      \"address2\": \"Suite 400\",\n"
                + "      \"bankData\": [\n"
                + "        {\n"
                + "          \"bankAccountFunction\": 0,\n"
                + "          \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "          \"nickname\": \"Business Checking 1234\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"boardingId\": 340,\n"
                + "      \"city\": \"Bristol\",\n"
                + "      \"contacts\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"country\": \"US\",\n"
                + "      \"credentials\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"dbaName\": \"Sunshine Gutters\",\n"
                + "      \"externalPaypointID\": \"\",\n"
                + "      \"fax\": \"5555555555\",\n"
                + "      \"idPaypoint\": 1000000,\n"
                + "      \"legalName\": \"Sunshine Services, LLC\",\n"
                + "      \"parentOrg\": {\n"
                + "        \"orgName\": \"Pilgrim Planner\",\n"
                + "        \"orgStatus\": 1,\n"
                + "        \"orgType\": 0\n"
                + "      },\n"
                + "      \"paypointStatus\": 1,\n"
                + "      \"phone\": \"5555555555\",\n"
                + "      \"state\": \"GA\",\n"
                + "      \"summary\": {\n"
                + "        \"amountSubs\": 0,\n"
                + "        \"amountTx\": 0,\n"
                + "        \"countSubs\": 0,\n"
                + "        \"countTx\": 0,\n"
                + "        \"customers\": 1\n"
                + "      },\n"
                + "      \"timeZone\": -5,\n"
                + "      \"websiteAddress\": \"www.example.com\",\n"
                + "      \"zip\": \"31113\"\n"
                + "    }\n"
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
    public void testGetPage() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"credentials\":[{\"accountId\":\"accountId\",\"cfeeFix\":1.1,\"cfeeFloat\":1.1,\"cfeeMax\":1.1,\"cfeeMin\":1.1,\"maxticket\":1.1,\"minticket\":1.1,\"mode\":1,\"referenceId\":1000000,\"service\":\"service\"}],\"lastAccess\":\"2022-06-30T15:01:00Z\",\"pageContent\":{\"amount\":{\"categories\":[{}],\"enabled\":true,\"order\":1},\"autopay\":{\"enabled\":true,\"frequencySelected\":\"frequencySelected\",\"header\":\"header\",\"order\":1,\"startDate\":\"1, 5-10\"},\"contactUs\":{\"emailLabel\":\"emailLabel\",\"enabled\":true,\"header\":\"header\",\"order\":1,\"paymentIcons\":true,\"phoneLabel\":\"phoneLabel\"},\"entry\":\"entry\",\"invoices\":{\"enabled\":true,\"invoiceLink\":{\"enabled\":true},\"order\":1,\"viewInvoiceDetails\":{\"enabled\":true}},\"logo\":{\"enabled\":true,\"order\":1},\"messageBeforePaying\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"name\":\"name\",\"notes\":{\"enabled\":true,\"header\":\"header\",\"order\":1,\"placeholder\":\"placeholder\",\"value\":\"value\"},\"page\":{\"description\":\"description\",\"enabled\":true,\"header\":\"header\",\"order\":1},\"paymentButton\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"paymentMethods\":{\"allMethodsChecked\":true,\"enabled\":true,\"header\":\"header\",\"methods\":{\"amex\":true,\"applePay\":true,\"discover\":false,\"eCheck\":false,\"mastercard\":true,\"visa\":true},\"order\":1},\"payor\":{\"enabled\":true,\"fields\":[{}],\"header\":\"header\",\"order\":1},\"review\":{\"enabled\":true,\"header\":\"header\",\"order\":1},\"subdomain\":\"mypage-1\"},\"pageIdentifier\":\"null\",\"pageSettings\":{\"color\":\"color\",\"customCssUrl\":\"customCssUrl\",\"language\":\"language\",\"pageLogo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"paymentButton\":{\"label\":\"label\",\"size\":\"sm\"},\"redirectAfterApprove\":true,\"redirectAfterApproveUrl\":\"redirectAfterApproveUrl\"},\"published\":1,\"receiptContent\":{\"amount\":{\"enabled\":true,\"order\":1},\"contactUs\":{\"enabled\":true,\"order\":1},\"details\":{\"enabled\":true,\"order\":1},\"logo\":{\"enabled\":true,\"order\":1},\"messageBeforeButton\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"page\":{\"description\":\"description\",\"enabled\":true,\"header\":\"header\",\"order\":1},\"paymentButton\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"paymentInformation\":{\"enabled\":true,\"order\":1},\"settings\":{\"enabled\":true,\"fields\":[{}],\"order\":1,\"sendAuto\":true,\"sendManual\":true}},\"subdomain\":\"mypage-1\",\"totalAmount\":1.1,\"validationCode\":\"validationCode\"}"));
        PayabliPages response = client.paypoint().getPage("8cfec329267", "pay-your-fees-1");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"AdditionalData\": {\n"
                + "    \"key1\": {\n"
                + "      \"key\": \"value\"\n"
                + "    },\n"
                + "    \"key2\": {\n"
                + "      \"key\": \"value\"\n"
                + "    },\n"
                + "    \"key3\": {\n"
                + "      \"key\": \"value\"\n"
                + "    }\n"
                + "  },\n"
                + "  \"credentials\": [\n"
                + "    {\n"
                + "      \"accountId\": \"accountId\",\n"
                + "      \"cfeeFix\": 1.1,\n"
                + "      \"cfeeFloat\": 1.1,\n"
                + "      \"cfeeMax\": 1.1,\n"
                + "      \"cfeeMin\": 1.1,\n"
                + "      \"maxticket\": 1.1,\n"
                + "      \"minticket\": 1.1,\n"
                + "      \"mode\": 1,\n"
                + "      \"referenceId\": 1000000,\n"
                + "      \"service\": \"service\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"lastAccess\": \"2022-06-30T15:01:00Z\",\n"
                + "  \"pageContent\": {\n"
                + "    \"amount\": {\n"
                + "      \"categories\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"enabled\": true,\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"autopay\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"frequencySelected\": \"frequencySelected\",\n"
                + "      \"header\": \"header\",\n"
                + "      \"order\": 1,\n"
                + "      \"startDate\": \"1, 5-10\"\n"
                + "    },\n"
                + "    \"contactUs\": {\n"
                + "      \"emailLabel\": \"emailLabel\",\n"
                + "      \"enabled\": true,\n"
                + "      \"header\": \"header\",\n"
                + "      \"order\": 1,\n"
                + "      \"paymentIcons\": true,\n"
                + "      \"phoneLabel\": \"phoneLabel\"\n"
                + "    },\n"
                + "    \"entry\": \"entry\",\n"
                + "    \"invoices\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"invoiceLink\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"order\": 1,\n"
                + "      \"viewInvoiceDetails\": {\n"
                + "        \"enabled\": true\n"
                + "      }\n"
                + "    },\n"
                + "    \"logo\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"messageBeforePaying\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"label\": \"label\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"name\": \"name\",\n"
                + "    \"notes\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"header\": \"header\",\n"
                + "      \"order\": 1,\n"
                + "      \"placeholder\": \"placeholder\",\n"
                + "      \"value\": \"value\"\n"
                + "    },\n"
                + "    \"page\": {\n"
                + "      \"description\": \"description\",\n"
                + "      \"enabled\": true,\n"
                + "      \"header\": \"header\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"paymentButton\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"label\": \"label\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"paymentMethods\": {\n"
                + "      \"allMethodsChecked\": true,\n"
                + "      \"enabled\": true,\n"
                + "      \"header\": \"header\",\n"
                + "      \"methods\": {\n"
                + "        \"amex\": true,\n"
                + "        \"applePay\": true,\n"
                + "        \"discover\": false,\n"
                + "        \"eCheck\": false,\n"
                + "        \"mastercard\": true,\n"
                + "        \"visa\": true\n"
                + "      },\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"payor\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"fields\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"header\": \"header\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"review\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"header\": \"header\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"subdomain\": \"mypage-1\"\n"
                + "  },\n"
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"pageSettings\": {\n"
                + "    \"color\": \"color\",\n"
                + "    \"customCssUrl\": \"customCssUrl\",\n"
                + "    \"language\": \"language\",\n"
                + "    \"pageLogo\": {\n"
                + "      \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "      \"filename\": \"my-doc.pdf\",\n"
                + "      \"ftype\": \"pdf\",\n"
                + "      \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "    },\n"
                + "    \"paymentButton\": {\n"
                + "      \"label\": \"label\",\n"
                + "      \"size\": \"sm\"\n"
                + "    },\n"
                + "    \"redirectAfterApprove\": true,\n"
                + "    \"redirectAfterApproveUrl\": \"redirectAfterApproveUrl\"\n"
                + "  },\n"
                + "  \"published\": 1,\n"
                + "  \"receiptContent\": {\n"
                + "    \"amount\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"contactUs\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"details\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"logo\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"messageBeforeButton\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"label\": \"label\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"page\": {\n"
                + "      \"description\": \"description\",\n"
                + "      \"enabled\": true,\n"
                + "      \"header\": \"header\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"paymentButton\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"label\": \"label\",\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"paymentInformation\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"order\": 1\n"
                + "    },\n"
                + "    \"settings\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"fields\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"order\": 1,\n"
                + "      \"sendAuto\": true,\n"
                + "      \"sendManual\": true\n"
                + "    }\n"
                + "  },\n"
                + "  \"subdomain\": \"mypage-1\",\n"
                + "  \"totalAmount\": 1.1,\n"
                + "  \"validationCode\": \"validationCode\"\n"
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
    public void testRemovePage() throws Exception {
        server.enqueue(
                new MockResponse().setResponseCode(200).setBody("{\"isSuccess\":true,\"responseText\":\"Success\"}"));
        PayabliApiResponseGeneric2Part response = client.paypoint().removePage("8cfec329267", "pay-your-fees-1");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

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
    public void testSaveLogo() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseCode\":1,\"responseData\":\"responseData\",\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response =
                client.paypoint().saveLogo("8cfec329267", FileContent.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
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
                + "  \"pageIdentifier\": \"null\",\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": \"responseData\",\n"
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
    public void testSettingsPage() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"customFields\":[{\"key\":\"customerID\",\"readOnly\":false,\"value\":\"\"},{\"key\":\"test\",\"readOnly\":false,\"value\":\"\"},{\"key\":\"newfield\",\"readOnly\":false,\"value\":\"\"},{\"key\":\"testfield\",\"readOnly\":false,\"value\":\"\"}],\"forInvoices\":[{\"key\":\"brandColor\",\"value\":\"#0594fad1\"},{\"key\":\"requiredInvoiceNumber\",\"value\":\"true\"},{\"key\":\"invoicePrefix\",\"value\":\"INV-\"},{\"key\":\"invoiceNumber\",\"value\":\"1\"},{\"key\":\"dueDate\",\"value\":\"NET30\"},{\"key\":\"dueDateCustom\",\"value\":\"Invalid Date\"},{\"key\":\"memoNote\",\"value\":\"Thank you for your business!\"},{\"key\":\"footerNote\",\"value\":\"default footer testing\"},{\"key\":\"includePaymentLink\",\"value\":\"true\"},{\"key\":\"paylinkHeader\",\"value\":\"Gruzya Adventure Outfitters\"},{\"key\":\"paylinkDescription\",\"value\":\"Pay Invoice\"},{\"key\":\"contactUsText\",\"value\":\"Contact Us\"},{\"key\":\"contactUsEmail\",\"value\":\"support@gruzyaadventureoutfitters.com\"},{\"key\":\"contactUsPhone\",\"value\":\"5551234567\"},{\"key\":\"invoiceNumbering\",\"value\":\"autoapply\"},{\"key\":\"paymentVisa\",\"value\":\"true\"},{\"key\":\"paymentMastercard\",\"value\":\"true\"},{\"key\":\"paymentDiscover\",\"value\":\"true\"},{\"key\":\"paymentAmex\",\"value\":\"true\"},{\"key\":\"paymentAch\",\"value\":\"true\"},{\"key\":\"paymentApplePay\",\"value\":\"false\"},{\"key\":\"paymentGooglePay\",\"value\":\"false\"},{\"key\":\"requireInvoiceNumber\",\"value\":\"false\"},{\"key\":\"autoCreateInvoice\",\"value\":\"true\"},{\"key\":\"\",\"value\":\"\"}],\"forPayOuts\":[{\"key\":\"autoCapture\",\"readOnly\":false,\"value\":\"true\"},{\"key\":\"autoPay\",\"readOnly\":false,\"value\":\"true\"},{\"key\":\"autoDownloadCheckImage\",\"readOnly\":false,\"value\":\"false\"}],\"forWallets\":[{\"key\":\"isApplePayEnabled\",\"readOnly\":false,\"value\":\"false\"}],\"general\":[{\"key\":\"currency\",\"readOnly\":true,\"value\":\"USD\"},{\"key\":\"disableAutoReceipt\",\"readOnly\":true,\"value\":\"true\"},{\"key\":\"sendApprovedReceipt\",\"readOnly\":false,\"value\":\"true\"},{\"key\":\"sendDeclinedReceipt\",\"readOnly\":false,\"value\":\"true\"},{\"key\":\"\",\"readOnly\":true,\"value\":\"\"}],\"identifiers\":[{\"key\":\"key\",\"readOnly\":false,\"value\":\"value\"}]}"));
        SettingsQueryRecord response = client.paypoint().settingsPage("8cfec329267");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"customFields\": [\n"
                + "    {\n"
                + "      \"key\": \"customerID\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"test\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"newfield\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"testfield\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"forInvoices\": [\n"
                + "    {\n"
                + "      \"key\": \"brandColor\",\n"
                + "      \"value\": \"#0594fad1\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"requiredInvoiceNumber\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"invoicePrefix\",\n"
                + "      \"value\": \"INV-\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"invoiceNumber\",\n"
                + "      \"value\": \"1\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"dueDate\",\n"
                + "      \"value\": \"NET30\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"dueDateCustom\",\n"
                + "      \"value\": \"Invalid Date\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"memoNote\",\n"
                + "      \"value\": \"Thank you for your business!\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"footerNote\",\n"
                + "      \"value\": \"default footer testing\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"includePaymentLink\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paylinkHeader\",\n"
                + "      \"value\": \"Gruzya Adventure Outfitters\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paylinkDescription\",\n"
                + "      \"value\": \"Pay Invoice\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"contactUsText\",\n"
                + "      \"value\": \"Contact Us\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"contactUsEmail\",\n"
                + "      \"value\": \"support@gruzyaadventureoutfitters.com\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"contactUsPhone\",\n"
                + "      \"value\": \"5551234567\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"invoiceNumbering\",\n"
                + "      \"value\": \"autoapply\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paymentVisa\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paymentMastercard\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paymentDiscover\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paymentAmex\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paymentAch\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paymentApplePay\",\n"
                + "      \"value\": \"false\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"paymentGooglePay\",\n"
                + "      \"value\": \"false\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"requireInvoiceNumber\",\n"
                + "      \"value\": \"false\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"autoCreateInvoice\",\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"\",\n"
                + "      \"value\": \"\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"forPayOuts\": [\n"
                + "    {\n"
                + "      \"key\": \"autoCapture\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"autoPay\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"autoDownloadCheckImage\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"false\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"forWallets\": [\n"
                + "    {\n"
                + "      \"key\": \"isApplePayEnabled\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"false\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"general\": [\n"
                + "    {\n"
                + "      \"key\": \"currency\",\n"
                + "      \"readOnly\": true,\n"
                + "      \"value\": \"USD\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"disableAutoReceipt\",\n"
                + "      \"readOnly\": true,\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"sendApprovedReceipt\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"sendDeclinedReceipt\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"true\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"\",\n"
                + "      \"readOnly\": true,\n"
                + "      \"value\": \"\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"identifiers\": [\n"
                + "    {\n"
                + "      \"key\": \"key\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"value\"\n"
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
    public void testMigrate() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseText\":\"Success\"}"));
        MigratePaypointResponse response = client.paypoint()
                .migrate(PaypointMoveRequest.builder()
                        .entryPoint("473abc123def")
                        .newParentOrganizationId(123)
                        .notificationRequest(NotificationRequest.builder()
                                .notificationUrl("https://webhook-test.yoursie.com")
                                .webHeaderParameters(Optional.of(Arrays.asList(WebHeaderParameter.builder()
                                        .key("testheader")
                                        .value("1234567890")
                                        .build())))
                                .build())
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"entryPoint\": \"473abc123def\",\n"
                + "  \"newParentOrganizationId\": 123,\n"
                + "  \"notificationRequest\": {\n"
                + "    \"notificationUrl\": \"https://webhook-test.yoursie.com\",\n"
                + "    \"webHeaderParameters\": [\n"
                + "      {\n"
                + "        \"key\": \"testheader\",\n"
                + "        \"value\": \"1234567890\"\n"
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
                + "  \"isSuccess\": true,\n"
                + "  \"responseCode\": 1,\n"
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
