package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.templates.requests.ListTemplatesRequest;
import io.github.payabli.api.types.BoardingLinkApiResponse;
import io.github.payabli.api.types.PayabliApiResponseTemplateId;
import io.github.payabli.api.types.TemplateQueryRecord;
import io.github.payabli.api.types.TemplateQueryResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TemplatesWireTest {
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
    public void testDeleteTemplate() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3625,\"responseText\":\"Success\"}"));
        PayabliApiResponseTemplateId response = client.templates().deleteTemplate(80.0);
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
    public void testGetlinkTemplate() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"responseData\":\"34\",\"responseText\":\"Success\"}"));
        BoardingLinkApiResponse response = client.templates().getlinkTemplate(80.0, true);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                "" + "{\n" + "  \"responseData\": \"34\",\n" + "  \"responseText\": \"Success\"\n" + "}";
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
    public void testGetTemplate() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"addPrice\":true,\"boardingLinks\":[{\"acceptOauth\":false,\"acceptRegister\":false,\"entryAttributes\":\"entryAttributes\",\"id\":91,\"lastUpdated\":\"2022-07-01T15:00:01Z\",\"orgParentName\":\"PropertyManager Pro\",\"referenceName\":\"payabli-00710\",\"referenceTemplateId\":1830,\"templateCode\":\"templateCode\",\"templateName\":\"SMB\"}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"idTemplate\":1000000,\"isRoot\":false,\"orgParentName\":\"PropertyManager Pro\",\"recipientEmailNotification\":true,\"resumable\":false,\"templateCode\":\"templateCode\",\"templateContent\":{\"businessData\":{\"visible\":true},\"documentsData\":{\"minimumDocuments\":1,\"subFooter\":\"subFooter\",\"subHeader\":\"subHeader\",\"uploadDocuments\":true,\"visible\":true},\"ownershipData\":{\"multipleContacts\":true,\"multipleOwners\":true,\"subFooter\":\"subFooter\",\"subHeader\":\"subHeader\",\"visible\":true},\"processingData\":{\"subFooter\":\"subFooter\",\"subHeader\":\"subHeader\",\"visible\":true},\"salesData\":{\"salesCode\":\"salesCode\",\"salesCRM\":\"salesCRM\"},\"servicesData\":{\"subFooter\":\"subFooter\",\"subHeader\":\"subHeader\",\"visible\":true},\"underwritingData\":{\"method\":\"automatic\",\"policyId\":\"J-itEyD6A7y5S5yYFjxOrb\"}},\"templateDescription\":\"templateDescription\",\"templateTitle\":\"templateTitle\",\"usedBy\":1}"));
        TemplateQueryRecord response = client.templates().getTemplate(80.0);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"addPrice\": true,\n"
                + "  \"boardingLinks\": [\n"
                + "    {\n"
                + "      \"acceptOauth\": false,\n"
                + "      \"acceptRegister\": false,\n"
                + "      \"entryAttributes\": \"entryAttributes\",\n"
                + "      \"id\": 91,\n"
                + "      \"lastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"orgParentName\": \"PropertyManager Pro\",\n"
                + "      \"referenceName\": \"payabli-00710\",\n"
                + "      \"referenceTemplateId\": 1830,\n"
                + "      \"templateCode\": \"templateCode\",\n"
                + "      \"templateName\": \"SMB\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"idTemplate\": 1000000,\n"
                + "  \"isRoot\": false,\n"
                + "  \"orgParentName\": \"PropertyManager Pro\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"resumable\": false,\n"
                + "  \"templateCode\": \"templateCode\",\n"
                + "  \"templateContent\": {\n"
                + "    \"businessData\": {\n"
                + "      \"visible\": true\n"
                + "    },\n"
                + "    \"documentsData\": {\n"
                + "      \"minimumDocuments\": 1,\n"
                + "      \"subFooter\": \"subFooter\",\n"
                + "      \"subHeader\": \"subHeader\",\n"
                + "      \"uploadDocuments\": true,\n"
                + "      \"visible\": true\n"
                + "    },\n"
                + "    \"ownershipData\": {\n"
                + "      \"multipleContacts\": true,\n"
                + "      \"multipleOwners\": true,\n"
                + "      \"subFooter\": \"subFooter\",\n"
                + "      \"subHeader\": \"subHeader\",\n"
                + "      \"visible\": true\n"
                + "    },\n"
                + "    \"processingData\": {\n"
                + "      \"subFooter\": \"subFooter\",\n"
                + "      \"subHeader\": \"subHeader\",\n"
                + "      \"visible\": true\n"
                + "    },\n"
                + "    \"salesData\": {\n"
                + "      \"salesCode\": \"salesCode\",\n"
                + "      \"salesCRM\": \"salesCRM\"\n"
                + "    },\n"
                + "    \"servicesData\": {\n"
                + "      \"subFooter\": \"subFooter\",\n"
                + "      \"subHeader\": \"subHeader\",\n"
                + "      \"visible\": true\n"
                + "    },\n"
                + "    \"underwritingData\": {\n"
                + "      \"method\": \"automatic\",\n"
                + "      \"policyId\": \"J-itEyD6A7y5S5yYFjxOrb\"\n"
                + "    }\n"
                + "  },\n"
                + "  \"templateDescription\": \"templateDescription\",\n"
                + "  \"templateTitle\": \"templateTitle\",\n"
                + "  \"usedBy\": 1\n"
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
    public void testListTemplates() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"records\":[{\"addPrice\":true,\"boardingLinks\":[{\"id\":91}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"idTemplate\":1000000,\"isRoot\":false,\"orgParentName\":\"PropertyManager Pro\",\"recipientEmailNotification\":true,\"resumable\":false,\"templateCode\":\"templateCode\",\"templateDescription\":\"templateDescription\",\"templateTitle\":\"templateTitle\",\"usedBy\":1}],\"summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
        TemplateQueryResponse response = client.templates()
                .listTemplates(
                        123,
                        ListTemplatesRequest.builder()
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
                + "  \"records\": [\n"
                + "    {\n"
                + "      \"addPrice\": true,\n"
                + "      \"boardingLinks\": [\n"
                + "        {\n"
                + "          \"id\": 91\n"
                + "        }\n"
                + "      ],\n"
                + "      \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"idTemplate\": 1000000,\n"
                + "      \"isRoot\": false,\n"
                + "      \"orgParentName\": \"PropertyManager Pro\",\n"
                + "      \"recipientEmailNotification\": true,\n"
                + "      \"resumable\": false,\n"
                + "      \"templateCode\": \"templateCode\",\n"
                + "      \"templateDescription\": \"templateDescription\",\n"
                + "      \"templateTitle\": \"templateTitle\",\n"
                + "      \"usedBy\": 1\n"
                + "    }\n"
                + "  ],\n"
                + "  \"summary\": {\n"
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
