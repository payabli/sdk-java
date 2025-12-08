package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.hostedpaymentpages.requests.NewPageRequest;
import io.github.payabli.api.types.PayabliApiResponse00Responsedatanonobject;
import io.github.payabli.api.types.PayabliPages;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostedPaymentPagesWireTest {
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
    public void testLoadPage() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"credentials\":[{\"accountId\":\"accountId\",\"cfeeFix\":1.1,\"cfeeFloat\":1.1,\"cfeeMax\":1.1,\"cfeeMin\":1.1,\"maxticket\":1.1,\"minticket\":1.1,\"mode\":1,\"referenceId\":1000000,\"service\":\"service\"}],\"lastAccess\":\"2022-06-30T15:01:00Z\",\"pageContent\":{\"amount\":{\"categories\":[{}],\"enabled\":true,\"order\":1},\"autopay\":{\"enabled\":true,\"frequencySelected\":\"frequencySelected\",\"header\":\"header\",\"order\":1,\"startDate\":\"1, 5-10\"},\"contactUs\":{\"emailLabel\":\"emailLabel\",\"enabled\":true,\"header\":\"header\",\"order\":1,\"paymentIcons\":true,\"phoneLabel\":\"phoneLabel\"},\"entry\":\"entry\",\"invoices\":{\"enabled\":true,\"invoiceLink\":{\"enabled\":true},\"order\":1,\"viewInvoiceDetails\":{\"enabled\":true}},\"logo\":{\"enabled\":true,\"order\":1},\"messageBeforePaying\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"name\":\"name\",\"notes\":{\"enabled\":true,\"header\":\"header\",\"order\":1,\"placeholder\":\"placeholder\",\"value\":\"value\"},\"page\":{\"description\":\"description\",\"enabled\":true,\"header\":\"header\",\"order\":1},\"paymentButton\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"paymentMethods\":{\"allMethodsChecked\":true,\"enabled\":true,\"header\":\"header\",\"methods\":{\"amex\":true,\"applePay\":true,\"discover\":false,\"eCheck\":false,\"mastercard\":true,\"visa\":true},\"order\":1},\"payor\":{\"enabled\":true,\"fields\":[{}],\"header\":\"header\",\"order\":1},\"review\":{\"enabled\":true,\"header\":\"header\",\"order\":1},\"subdomain\":\"mypage-1\"},\"pageIdentifier\":\"null\",\"pageSettings\":{\"color\":\"color\",\"customCssUrl\":\"customCssUrl\",\"language\":\"language\",\"pageLogo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"paymentButton\":{\"label\":\"label\",\"size\":\"sm\"},\"redirectAfterApprove\":true,\"redirectAfterApproveUrl\":\"redirectAfterApproveUrl\"},\"published\":1,\"receiptContent\":{\"amount\":{\"enabled\":true,\"order\":1},\"contactUs\":{\"enabled\":true,\"order\":1},\"details\":{\"enabled\":true,\"order\":1},\"logo\":{\"enabled\":true,\"order\":1},\"messageBeforeButton\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"page\":{\"description\":\"description\",\"enabled\":true,\"header\":\"header\",\"order\":1},\"paymentButton\":{\"enabled\":true,\"label\":\"label\",\"order\":1},\"paymentInformation\":{\"enabled\":true,\"order\":1},\"settings\":{\"enabled\":true,\"fields\":[{}],\"order\":1,\"sendAuto\":true,\"sendManual\":true}},\"subdomain\":\"mypage-1\",\"totalAmount\":1.1,\"validationCode\":\"validationCode\"}"));
        PayabliPages response = client.hostedPaymentPages().loadPage("8cfec329267", "pay-your-fees-1");
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
    public void testNewPage() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"pageIdentifier\":\"null\",\"responseCode\":1,\"responseData\":\"responseData\",\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.hostedPaymentPages()
                .newPage(
                        "8cfec329267",
                        NewPageRequest.builder()
                                .body(PayabliPages.builder().build())
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
    public void testSavePage() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":\"string\",\"responseText\":\"Updated\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.hostedPaymentPages()
                .savePage(
                        "8cfec329267", "pay-your-fees-1", PayabliPages.builder().build());
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
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": \"string\",\n"
                + "  \"responseText\": \"Updated\"\n"
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
