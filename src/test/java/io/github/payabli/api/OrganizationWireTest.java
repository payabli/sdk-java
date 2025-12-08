package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.organization.requests.AddOrganizationRequest;
import io.github.payabli.api.resources.organization.requests.OrganizationData;
import io.github.payabli.api.resources.organization.types.AddOrganizationResponse;
import io.github.payabli.api.resources.organization.types.DeleteOrganizationResponse;
import io.github.payabli.api.resources.organization.types.EditOrganizationResponse;
import io.github.payabli.api.types.Contacts;
import io.github.payabli.api.types.FileContent;
import io.github.payabli.api.types.FileContentFtype;
import io.github.payabli.api.types.Instrument;
import io.github.payabli.api.types.OrganizationQueryRecord;
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

public class OrganizationWireTest {
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
    public void testAddOrganization() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":245,\"responseText\":\"Success\"}"));
        AddOrganizationResponse response = client.organization()
                .addOrganization(AddOrganizationRequest.builder()
                        .orgName("Pilgrim Planner")
                        .orgType(0)
                        .replyToEmail("email@example.com")
                        .idempotencyKey("6B29FC40-CA47-1067-B31D-00DD010662DA")
                        .billingInfo(Instrument.builder()
                                .achAccount("123123123")
                                .achRouting("123123123")
                                .billingAddress("123 Walnut Street")
                                .billingCity("Johnson City")
                                .billingCountry("US")
                                .billingState("TN")
                                .billingZip("37615")
                                .build())
                        .contacts(Optional.of(Arrays.asList(Contacts.builder()
                                .contactEmail("herman@hermanscoatings.com")
                                .contactName("Herman Martinez")
                                .contactPhone("3055550000")
                                .contactTitle("Owner")
                                .build())))
                        .hasBilling(true)
                        .hasResidual(true)
                        .orgAddress("123 Walnut Street")
                        .orgCity("Johnson City")
                        .orgCountry("US")
                        .orgEntryName("pilgrim-planner")
                        .orgId("123")
                        .orgLogo(FileContent.builder()
                                .fContent("TXkgdGVzdCBmaWxlHJ==...")
                                .filename("my-doc.pdf")
                                .ftype(FileContentFtype.PDF)
                                .furl("https://mysite.com/my-doc.pdf")
                                .build())
                        .orgParentId(236L)
                        .orgState("TN")
                        .orgTimezone(-5)
                        .orgWebsite("www.pilgrimageplanner.com")
                        .orgZip("37615")
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
                + "  \"billingInfo\": {\n"
                + "    \"achAccount\": \"123123123\",\n"
                + "    \"achRouting\": \"123123123\",\n"
                + "    \"billingAddress\": \"123 Walnut Street\",\n"
                + "    \"billingCity\": \"Johnson City\",\n"
                + "    \"billingCountry\": \"US\",\n"
                + "    \"billingState\": \"TN\",\n"
                + "    \"billingZip\": \"37615\"\n"
                + "  },\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"herman@hermanscoatings.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"hasBilling\": true,\n"
                + "  \"hasResidual\": true,\n"
                + "  \"orgAddress\": \"123 Walnut Street\",\n"
                + "  \"orgCity\": \"Johnson City\",\n"
                + "  \"orgCountry\": \"US\",\n"
                + "  \"orgEntryName\": \"pilgrim-planner\",\n"
                + "  \"orgId\": \"123\",\n"
                + "  \"orgLogo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"orgName\": \"Pilgrim Planner\",\n"
                + "  \"orgParentId\": 236,\n"
                + "  \"orgState\": \"TN\",\n"
                + "  \"orgTimezone\": -5,\n"
                + "  \"orgType\": 0,\n"
                + "  \"orgWebsite\": \"www.pilgrimageplanner.com\",\n"
                + "  \"orgZip\": \"37615\",\n"
                + "  \"replyToEmail\": \"email@example.com\"\n"
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
                + "  \"responseData\": 245,\n"
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
    public void testDeleteOrganization() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":245,\"responseText\":\"Success\"}"));
        DeleteOrganizationResponse response = client.organization().deleteOrganization(123);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": 245,\n"
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
    public void testEditOrganization() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":2442,\"responseText\":\"Success\"}"));
        EditOrganizationResponse response = client.organization()
                .editOrganization(
                        123,
                        OrganizationData.builder()
                                .contacts(Optional.of(Arrays.asList(Contacts.builder()
                                        .contactEmail("herman@hermanscoatings.com")
                                        .contactName("Herman Martinez")
                                        .contactPhone("3055550000")
                                        .contactTitle("Owner")
                                        .build())))
                                .orgAddress("123 Walnut Street")
                                .orgCity("Johnson City")
                                .orgCountry("US")
                                .orgEntryName("pilgrim-planner")
                                .organizationDataOrgId("123")
                                .orgName("Pilgrim Planner")
                                .orgState("TN")
                                .orgTimezone(-5)
                                .orgType(0)
                                .orgWebsite("www.pilgrimageplanner.com")
                                .orgZip("37615")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"herman@hermanscoatings.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"orgAddress\": \"123 Walnut Street\",\n"
                + "  \"orgCity\": \"Johnson City\",\n"
                + "  \"orgCountry\": \"US\",\n"
                + "  \"orgEntryName\": \"pilgrim-planner\",\n"
                + "  \"orgId\": \"123\",\n"
                + "  \"orgName\": \"Pilgrim Planner\",\n"
                + "  \"orgState\": \"TN\",\n"
                + "  \"orgTimezone\": -5,\n"
                + "  \"orgType\": 0,\n"
                + "  \"orgWebsite\": \"www.pilgrimageplanner.com\",\n"
                + "  \"orgZip\": \"37615\"\n"
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
                + "  \"responseData\": 2442,\n"
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
    public void testGetBasicOrganization() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"services\":[{\"description\":\"description\",\"enabled\":true,\"monthlyCost\":1.1,\"name\":\"name\",\"reseller\":true,\"setupCost\":1.1,\"txCost\":1.1,\"txPercentCost\":1.1}],\"billingInfo\":{\"achAccount\":\"123123123\",\"achRouting\":\"123123123\",\"billingAddress\":\"123 Walnut Street\",\"billingCity\":\"Johnson City\",\"billingCountry\":\"US\",\"billingState\":\"TN\",\"billingZip\":\"37615\"},\"contacts\":[{\"contactEmail\":\"example@email.com\",\"contactName\":\"Herman Martinez\",\"contactPhone\":\"3055550000\",\"contactTitle\":\"Owner\"}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"hasBilling\":true,\"hasResidual\":true,\"idOrg\":123,\"isRoot\":false,\"orgAddress\":\"123 Walnut Street\",\"orgCity\":\"Johnson City\",\"orgCountry\":\"US\",\"orgEntryName\":\"pilgrim-planner\",\"orgId\":\"I-123\",\"orgLogo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"orgName\":\"Pilgrim Planner\",\"orgParentId\":236,\"orgParentName\":\"PropertyManager Pro\",\"orgState\":\"TN\",\"orgTimezone\":-5,\"orgType\":0,\"orgWebsite\":\"www.pilgrimageplanner.com\",\"orgZip\":\"orgZip\",\"recipientEmailNotification\":true,\"replyToEmail\":\"example@email.com\",\"resumable\":false,\"summary\":{\"amountSubs\":1.1,\"amountTx\":1.1,\"childOrgs\":1,\"childPaypoints\":1,\"countSubs\":1,\"countTx\":1},\"users\":[{\"Access\":[{\"roleValue\":true}],\"AdditionalData\":\"AdditionalData\",\"createdAt\":\"2022-07-01T15:00:01Z\",\"Email\":\"example@email.com\",\"language\":\"en\",\"lastAccess\":\"2022-07-01T15:00:01Z\",\"Name\":\"Sean Smith\",\"Phone\":\"5555555555\",\"Scope\":[{\"orgType\":0}],\"snData\":\"snData\",\"snIdentifier\":\"snIdentifier\",\"snProvider\":\"google\",\"timeZone\":-5,\"userId\":1000000,\"UsrMFA\":false,\"UsrMFAMode\":0,\"UsrStatus\":1}]}"));
        OrganizationQueryRecord response = client.organization().getBasicOrganization("8cfec329267");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"services\": [\n"
                + "    {\n"
                + "      \"description\": \"description\",\n"
                + "      \"enabled\": true,\n"
                + "      \"monthlyCost\": 1.1,\n"
                + "      \"name\": \"name\",\n"
                + "      \"reseller\": true,\n"
                + "      \"setupCost\": 1.1,\n"
                + "      \"txCost\": 1.1,\n"
                + "      \"txPercentCost\": 1.1\n"
                + "    }\n"
                + "  ],\n"
                + "  \"billingInfo\": {\n"
                + "    \"achAccount\": \"123123123\",\n"
                + "    \"achRouting\": \"123123123\",\n"
                + "    \"billingAddress\": \"123 Walnut Street\",\n"
                + "    \"billingCity\": \"Johnson City\",\n"
                + "    \"billingCountry\": \"US\",\n"
                + "    \"billingState\": \"TN\",\n"
                + "    \"billingZip\": \"37615\"\n"
                + "  },\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"example@email.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"hasBilling\": true,\n"
                + "  \"hasResidual\": true,\n"
                + "  \"idOrg\": 123,\n"
                + "  \"isRoot\": false,\n"
                + "  \"orgAddress\": \"123 Walnut Street\",\n"
                + "  \"orgCity\": \"Johnson City\",\n"
                + "  \"orgCountry\": \"US\",\n"
                + "  \"orgEntryName\": \"pilgrim-planner\",\n"
                + "  \"orgId\": \"I-123\",\n"
                + "  \"orgLogo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"orgName\": \"Pilgrim Planner\",\n"
                + "  \"orgParentId\": 236,\n"
                + "  \"orgParentName\": \"PropertyManager Pro\",\n"
                + "  \"orgState\": \"TN\",\n"
                + "  \"orgTimezone\": -5,\n"
                + "  \"orgType\": 0,\n"
                + "  \"orgWebsite\": \"www.pilgrimageplanner.com\",\n"
                + "  \"orgZip\": \"orgZip\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"replyToEmail\": \"example@email.com\",\n"
                + "  \"resumable\": false,\n"
                + "  \"summary\": {\n"
                + "    \"amountSubs\": 1.1,\n"
                + "    \"amountTx\": 1.1,\n"
                + "    \"childOrgs\": 1,\n"
                + "    \"childPaypoints\": 1,\n"
                + "    \"countSubs\": 1,\n"
                + "    \"countTx\": 1\n"
                + "  },\n"
                + "  \"users\": [\n"
                + "    {\n"
                + "      \"Access\": [\n"
                + "        {\n"
                + "          \"roleValue\": true\n"
                + "        }\n"
                + "      ],\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"Email\": \"example@email.com\",\n"
                + "      \"language\": \"en\",\n"
                + "      \"lastAccess\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"Name\": \"Sean Smith\",\n"
                + "      \"Phone\": \"5555555555\",\n"
                + "      \"Scope\": [\n"
                + "        {\n"
                + "          \"orgType\": 0\n"
                + "        }\n"
                + "      ],\n"
                + "      \"snData\": \"snData\",\n"
                + "      \"snIdentifier\": \"snIdentifier\",\n"
                + "      \"snProvider\": \"google\",\n"
                + "      \"timeZone\": -5,\n"
                + "      \"userId\": 1000000,\n"
                + "      \"UsrMFA\": false,\n"
                + "      \"UsrMFAMode\": 0,\n"
                + "      \"UsrStatus\": 1\n"
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
    public void testGetBasicOrganizationById() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"services\":[{\"description\":\"description\",\"enabled\":true,\"monthlyCost\":1.1,\"name\":\"name\",\"reseller\":true,\"setupCost\":1.1,\"txCost\":1.1,\"txPercentCost\":1.1}],\"billingInfo\":{\"achAccount\":\"123123123\",\"achRouting\":\"123123123\",\"billingAddress\":\"123 Walnut Street\",\"billingCity\":\"Johnson City\",\"billingCountry\":\"US\",\"billingState\":\"TN\",\"billingZip\":\"37615\"},\"contacts\":[{\"contactEmail\":\"example@email.com\",\"contactName\":\"Herman Martinez\",\"contactPhone\":\"3055550000\",\"contactTitle\":\"Owner\"}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"hasBilling\":true,\"hasResidual\":true,\"idOrg\":123,\"isRoot\":false,\"orgAddress\":\"123 Walnut Street\",\"orgCity\":\"Johnson City\",\"orgCountry\":\"US\",\"orgEntryName\":\"pilgrim-planner\",\"orgId\":\"I-123\",\"orgLogo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"orgName\":\"Pilgrim Planner\",\"orgParentId\":236,\"orgParentName\":\"PropertyManager Pro\",\"orgState\":\"TN\",\"orgTimezone\":-5,\"orgType\":0,\"orgWebsite\":\"www.pilgrimageplanner.com\",\"orgZip\":\"orgZip\",\"recipientEmailNotification\":true,\"replyToEmail\":\"example@email.com\",\"resumable\":false,\"summary\":{\"amountSubs\":1.1,\"amountTx\":1.1,\"childOrgs\":1,\"childPaypoints\":1,\"countSubs\":1,\"countTx\":1},\"users\":[{\"Access\":[{\"roleValue\":true}],\"AdditionalData\":\"AdditionalData\",\"createdAt\":\"2022-07-01T15:00:01Z\",\"Email\":\"example@email.com\",\"language\":\"en\",\"lastAccess\":\"2022-07-01T15:00:01Z\",\"Name\":\"Sean Smith\",\"Phone\":\"5555555555\",\"Scope\":[{\"orgType\":0}],\"snData\":\"snData\",\"snIdentifier\":\"snIdentifier\",\"snProvider\":\"google\",\"timeZone\":-5,\"userId\":1000000,\"UsrMFA\":false,\"UsrMFAMode\":0,\"UsrStatus\":1}]}"));
        OrganizationQueryRecord response = client.organization().getBasicOrganizationById(123);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"services\": [\n"
                + "    {\n"
                + "      \"description\": \"description\",\n"
                + "      \"enabled\": true,\n"
                + "      \"monthlyCost\": 1.1,\n"
                + "      \"name\": \"name\",\n"
                + "      \"reseller\": true,\n"
                + "      \"setupCost\": 1.1,\n"
                + "      \"txCost\": 1.1,\n"
                + "      \"txPercentCost\": 1.1\n"
                + "    }\n"
                + "  ],\n"
                + "  \"billingInfo\": {\n"
                + "    \"achAccount\": \"123123123\",\n"
                + "    \"achRouting\": \"123123123\",\n"
                + "    \"billingAddress\": \"123 Walnut Street\",\n"
                + "    \"billingCity\": \"Johnson City\",\n"
                + "    \"billingCountry\": \"US\",\n"
                + "    \"billingState\": \"TN\",\n"
                + "    \"billingZip\": \"37615\"\n"
                + "  },\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"example@email.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"hasBilling\": true,\n"
                + "  \"hasResidual\": true,\n"
                + "  \"idOrg\": 123,\n"
                + "  \"isRoot\": false,\n"
                + "  \"orgAddress\": \"123 Walnut Street\",\n"
                + "  \"orgCity\": \"Johnson City\",\n"
                + "  \"orgCountry\": \"US\",\n"
                + "  \"orgEntryName\": \"pilgrim-planner\",\n"
                + "  \"orgId\": \"I-123\",\n"
                + "  \"orgLogo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"orgName\": \"Pilgrim Planner\",\n"
                + "  \"orgParentId\": 236,\n"
                + "  \"orgParentName\": \"PropertyManager Pro\",\n"
                + "  \"orgState\": \"TN\",\n"
                + "  \"orgTimezone\": -5,\n"
                + "  \"orgType\": 0,\n"
                + "  \"orgWebsite\": \"www.pilgrimageplanner.com\",\n"
                + "  \"orgZip\": \"orgZip\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"replyToEmail\": \"example@email.com\",\n"
                + "  \"resumable\": false,\n"
                + "  \"summary\": {\n"
                + "    \"amountSubs\": 1.1,\n"
                + "    \"amountTx\": 1.1,\n"
                + "    \"childOrgs\": 1,\n"
                + "    \"childPaypoints\": 1,\n"
                + "    \"countSubs\": 1,\n"
                + "    \"countTx\": 1\n"
                + "  },\n"
                + "  \"users\": [\n"
                + "    {\n"
                + "      \"Access\": [\n"
                + "        {\n"
                + "          \"roleValue\": true\n"
                + "        }\n"
                + "      ],\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"Email\": \"example@email.com\",\n"
                + "      \"language\": \"en\",\n"
                + "      \"lastAccess\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"Name\": \"Sean Smith\",\n"
                + "      \"Phone\": \"5555555555\",\n"
                + "      \"Scope\": [\n"
                + "        {\n"
                + "          \"orgType\": 0\n"
                + "        }\n"
                + "      ],\n"
                + "      \"snData\": \"snData\",\n"
                + "      \"snIdentifier\": \"snIdentifier\",\n"
                + "      \"snProvider\": \"google\",\n"
                + "      \"timeZone\": -5,\n"
                + "      \"userId\": 1000000,\n"
                + "      \"UsrMFA\": false,\n"
                + "      \"UsrMFAMode\": 0,\n"
                + "      \"UsrStatus\": 1\n"
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
    public void testGetOrganization() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"services\":[{\"description\":\"description\",\"enabled\":true,\"monthlyCost\":1.1,\"name\":\"name\",\"reseller\":true,\"setupCost\":1.1,\"txCost\":1.1,\"txPercentCost\":1.1}],\"billingInfo\":{\"achAccount\":\"123123123\",\"achRouting\":\"123123123\",\"billingAddress\":\"123 Walnut Street\",\"billingCity\":\"Johnson City\",\"billingCountry\":\"US\",\"billingState\":\"TN\",\"billingZip\":\"37615\"},\"contacts\":[{\"contactEmail\":\"example@email.com\",\"contactName\":\"Herman Martinez\",\"contactPhone\":\"3055550000\",\"contactTitle\":\"Owner\"}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"hasBilling\":true,\"hasResidual\":true,\"idOrg\":123,\"isRoot\":false,\"orgAddress\":\"123 Walnut Street\",\"orgCity\":\"Johnson City\",\"orgCountry\":\"US\",\"orgEntryName\":\"pilgrim-planner\",\"orgId\":\"I-123\",\"orgLogo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"orgName\":\"Pilgrim Planner\",\"orgParentId\":236,\"orgParentName\":\"PropertyManager Pro\",\"orgState\":\"TN\",\"orgTimezone\":-5,\"orgType\":0,\"orgWebsite\":\"www.pilgrimageplanner.com\",\"orgZip\":\"orgZip\",\"recipientEmailNotification\":true,\"replyToEmail\":\"example@email.com\",\"resumable\":false,\"summary\":{\"amountSubs\":1.1,\"amountTx\":1.1,\"childOrgs\":1,\"childPaypoints\":1,\"countSubs\":1,\"countTx\":1},\"users\":[{\"Access\":[{\"roleValue\":true}],\"AdditionalData\":\"AdditionalData\",\"createdAt\":\"2022-07-01T15:00:01Z\",\"Email\":\"example@email.com\",\"language\":\"en\",\"lastAccess\":\"2022-07-01T15:00:01Z\",\"Name\":\"Sean Smith\",\"Phone\":\"5555555555\",\"Scope\":[{\"orgType\":0}],\"snData\":\"snData\",\"snIdentifier\":\"snIdentifier\",\"snProvider\":\"google\",\"timeZone\":-5,\"userId\":1000000,\"UsrMFA\":false,\"UsrMFAMode\":0,\"UsrStatus\":1}]}"));
        OrganizationQueryRecord response = client.organization().getOrganization(123);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"services\": [\n"
                + "    {\n"
                + "      \"description\": \"description\",\n"
                + "      \"enabled\": true,\n"
                + "      \"monthlyCost\": 1.1,\n"
                + "      \"name\": \"name\",\n"
                + "      \"reseller\": true,\n"
                + "      \"setupCost\": 1.1,\n"
                + "      \"txCost\": 1.1,\n"
                + "      \"txPercentCost\": 1.1\n"
                + "    }\n"
                + "  ],\n"
                + "  \"billingInfo\": {\n"
                + "    \"achAccount\": \"123123123\",\n"
                + "    \"achRouting\": \"123123123\",\n"
                + "    \"billingAddress\": \"123 Walnut Street\",\n"
                + "    \"billingCity\": \"Johnson City\",\n"
                + "    \"billingCountry\": \"US\",\n"
                + "    \"billingState\": \"TN\",\n"
                + "    \"billingZip\": \"37615\"\n"
                + "  },\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"example@email.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"hasBilling\": true,\n"
                + "  \"hasResidual\": true,\n"
                + "  \"idOrg\": 123,\n"
                + "  \"isRoot\": false,\n"
                + "  \"orgAddress\": \"123 Walnut Street\",\n"
                + "  \"orgCity\": \"Johnson City\",\n"
                + "  \"orgCountry\": \"US\",\n"
                + "  \"orgEntryName\": \"pilgrim-planner\",\n"
                + "  \"orgId\": \"I-123\",\n"
                + "  \"orgLogo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"orgName\": \"Pilgrim Planner\",\n"
                + "  \"orgParentId\": 236,\n"
                + "  \"orgParentName\": \"PropertyManager Pro\",\n"
                + "  \"orgState\": \"TN\",\n"
                + "  \"orgTimezone\": -5,\n"
                + "  \"orgType\": 0,\n"
                + "  \"orgWebsite\": \"www.pilgrimageplanner.com\",\n"
                + "  \"orgZip\": \"orgZip\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"replyToEmail\": \"example@email.com\",\n"
                + "  \"resumable\": false,\n"
                + "  \"summary\": {\n"
                + "    \"amountSubs\": 1.1,\n"
                + "    \"amountTx\": 1.1,\n"
                + "    \"childOrgs\": 1,\n"
                + "    \"childPaypoints\": 1,\n"
                + "    \"countSubs\": 1,\n"
                + "    \"countTx\": 1\n"
                + "  },\n"
                + "  \"users\": [\n"
                + "    {\n"
                + "      \"Access\": [\n"
                + "        {\n"
                + "          \"roleValue\": true\n"
                + "        }\n"
                + "      ],\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"Email\": \"example@email.com\",\n"
                + "      \"language\": \"en\",\n"
                + "      \"lastAccess\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"Name\": \"Sean Smith\",\n"
                + "      \"Phone\": \"5555555555\",\n"
                + "      \"Scope\": [\n"
                + "        {\n"
                + "          \"orgType\": 0\n"
                + "        }\n"
                + "      ],\n"
                + "      \"snData\": \"snData\",\n"
                + "      \"snIdentifier\": \"snIdentifier\",\n"
                + "      \"snProvider\": \"google\",\n"
                + "      \"timeZone\": -5,\n"
                + "      \"userId\": 1000000,\n"
                + "      \"UsrMFA\": false,\n"
                + "      \"UsrMFAMode\": 0,\n"
                + "      \"UsrStatus\": 1\n"
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
    public void testGetSettingsOrganization() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"customFields\":[{\"key\":\"key\",\"readOnly\":false,\"value\":\"value\"}],\"forInvoices\":[{\"key\":\"key\",\"readOnly\":false,\"value\":\"value\"}],\"forPayOuts\":[{\"key\":\"key\",\"readOnly\":false,\"value\":\"value\"}],\"forWallets\":[{\"key\":\"isGooglePayEnabled\",\"readOnly\":false,\"value\":\"true\"}],\"general\":[{\"key\":\"key\",\"readOnly\":false,\"value\":\"value\"}],\"identifiers\":[{\"key\":\"key\",\"readOnly\":false,\"value\":\"value\"}]}"));
        SettingsQueryRecord response = client.organization().getSettingsOrganization(123);
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
                + "      \"key\": \"key\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"value\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"forInvoices\": [\n"
                + "    {\n"
                + "      \"key\": \"key\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"value\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"forPayOuts\": [\n"
                + "    {\n"
                + "      \"key\": \"key\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"value\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"forWallets\": [\n"
                + "    {\n"
                + "      \"key\": \"isGooglePayEnabled\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"true\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"general\": [\n"
                + "    {\n"
                + "      \"key\": \"key\",\n"
                + "      \"readOnly\": false,\n"
                + "      \"value\": \"value\"\n"
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
