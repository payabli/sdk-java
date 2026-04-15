package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.payoutsubscription.types.QueryPayoutSubscriptionResponse;
import io.github.payabli.api.resources.query.requests.ListBatchDetailsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListBatchDetailsRequest;
import io.github.payabli.api.resources.query.requests.ListBatchesOrgRequest;
import io.github.payabli.api.resources.query.requests.ListBatchesOutOrgRequest;
import io.github.payabli.api.resources.query.requests.ListBatchesOutRequest;
import io.github.payabli.api.resources.query.requests.ListBatchesRequest;
import io.github.payabli.api.resources.query.requests.ListChargebacksOrgRequest;
import io.github.payabli.api.resources.query.requests.ListChargebacksRequest;
import io.github.payabli.api.resources.query.requests.ListCustomersOrgRequest;
import io.github.payabli.api.resources.query.requests.ListCustomersRequest;
import io.github.payabli.api.resources.query.requests.ListNotificationReportsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListNotificationReportsRequest;
import io.github.payabli.api.resources.query.requests.ListNotificationsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListNotificationsRequest;
import io.github.payabli.api.resources.query.requests.ListOrganizationsRequest;
import io.github.payabli.api.resources.query.requests.ListPayoutOrgRequest;
import io.github.payabli.api.resources.query.requests.ListPayoutRequest;
import io.github.payabli.api.resources.query.requests.ListPayoutSubscriptionsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListPayoutSubscriptionsRequest;
import io.github.payabli.api.resources.query.requests.ListPaypointsRequest;
import io.github.payabli.api.resources.query.requests.ListSettlementsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListSettlementsRequest;
import io.github.payabli.api.resources.query.requests.ListSubscriptionsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListSubscriptionsRequest;
import io.github.payabli.api.resources.query.requests.ListTransactionsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListTransactionsRequest;
import io.github.payabli.api.resources.query.requests.ListTransferDetailsOutRequest;
import io.github.payabli.api.resources.query.requests.ListTransfersOutOrgRequest;
import io.github.payabli.api.resources.query.requests.ListTransfersOutPaypointRequest;
import io.github.payabli.api.resources.query.requests.ListTransfersPaypointRequest;
import io.github.payabli.api.resources.query.requests.ListTransfersRequest;
import io.github.payabli.api.resources.query.requests.ListTransfersRequestOrg;
import io.github.payabli.api.resources.query.requests.ListUsersOrgRequest;
import io.github.payabli.api.resources.query.requests.ListUsersPaypointRequest;
import io.github.payabli.api.resources.query.requests.ListVcardsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListVcardsRequest;
import io.github.payabli.api.resources.query.requests.ListVendorsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListVendorsRequest;
import io.github.payabli.api.resources.querytypes.types.ListOrganizationsResponse;
import io.github.payabli.api.resources.querytypes.types.QueryBatchesDetailResponse;
import io.github.payabli.api.resources.querytypes.types.QueryBatchesResponse;
import io.github.payabli.api.resources.querytypes.types.QueryTransferDetailResponse;
import io.github.payabli.api.resources.querytypes.types.TransferOutDetailQueryResponse;
import io.github.payabli.api.resources.querytypes.types.TransferOutQueryResponse;
import io.github.payabli.api.types.QueryBatchesOutResponse;
import io.github.payabli.api.types.QueryChargebacksResponse;
import io.github.payabli.api.types.QueryCustomerResponse;
import io.github.payabli.api.types.QueryEntrypointResponse;
import io.github.payabli.api.types.QueryPayoutTransaction;
import io.github.payabli.api.types.QueryResponseNotificationReports;
import io.github.payabli.api.types.QueryResponseNotifications;
import io.github.payabli.api.types.QueryResponseSettlements;
import io.github.payabli.api.types.QueryResponseTransactions;
import io.github.payabli.api.types.QueryResponseVendors;
import io.github.payabli.api.types.QuerySubscriptionResponse;
import io.github.payabli.api.types.QueryUserResponse;
import io.github.payabli.api.types.TransferQueryResponse;
import io.github.payabli.api.types.VCardQueryResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryWireTest {
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
    public void testListBatchDetails() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchDetails_response.json")));
        QueryBatchesDetailResponse response = client.query()
                .listBatchDetails(
                        "8cfec329267",
                        ListBatchDetailsRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchDetails_response.json");
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
    public void testListBatchDetailsOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(
                        TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchDetailsOrg_response.json")));
        QueryResponseSettlements response = client.query()
                .listBatchDetailsOrg(
                        123,
                        ListBatchDetailsOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchDetailsOrg_response.json");
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
    public void testListBatches() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListBatches_response.json")));
        QueryBatchesResponse response = client.query()
                .listBatches(
                        "8cfec329267",
                        ListBatchesRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListBatches_response.json");
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
    public void testListBatchesOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchesOrg_response.json")));
        QueryBatchesResponse response = client.query()
                .listBatchesOrg(
                        123,
                        ListBatchesOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchesOrg_response.json");
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
    public void testListBatchesOut() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchesOut_response.json")));
        QueryBatchesOutResponse response = client.query()
                .listBatchesOut(
                        "8cfec329267",
                        ListBatchesOutRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchesOut_response.json");
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
    public void testListBatchesOutOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchesOutOrg_response.json")));
        QueryBatchesOutResponse response = client.query()
                .listBatchesOutOrg(
                        123,
                        ListBatchesOutOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListBatchesOutOrg_response.json");
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
    public void testListChargebacks() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListChargebacks_response.json")));
        QueryChargebacksResponse response = client.query()
                .listChargebacks(
                        "8cfec329267",
                        ListChargebacksRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListChargebacks_response.json");
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
    public void testListChargebacksOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListChargebacksOrg_response.json")));
        QueryChargebacksResponse response = client.query()
                .listChargebacksOrg(
                        123,
                        ListChargebacksOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListChargebacksOrg_response.json");
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
    public void testListCustomers() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListCustomers_response.json")));
        QueryCustomerResponse response = client.query()
                .listCustomers(
                        "8cfec329267",
                        ListCustomersRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListCustomers_response.json");
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
    public void testListCustomersOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListCustomersOrg_response.json")));
        QueryCustomerResponse response = client.query()
                .listCustomersOrg(
                        123,
                        ListCustomersOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListCustomersOrg_response.json");
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
    public void testListNotificationReports() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"createdAt\":\"2024-02-20T01:48:04Z\",\"id\":4881,\"isDownloadable\":true,\"reportName\":\"Transaction-2024-02-20-000000-0-2.csv\"}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":1}}"));
        QueryResponseNotificationReports response = client.query()
                .listNotificationReports(
                        "8cfec329267",
                        ListNotificationReportsRequest.builder()
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
                + "      \"createdAt\": \"2024-02-20T01:48:04Z\",\n"
                + "      \"id\": 4881,\n"
                + "      \"isDownloadable\": true,\n"
                + "      \"reportName\": \"Transaction-2024-02-20-000000-0-2.csv\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 1\n"
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
    public void testListNotificationReportsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"createdAt\":\"2024-02-20T01:48:04Z\",\"id\":4881,\"isDownloadable\":true,\"reportName\":\"Transaction-2024-02-20-000000-0-2.csv\"}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":1}}"));
        QueryResponseNotificationReports response = client.query()
                .listNotificationReportsOrg(
                        123,
                        ListNotificationReportsOrgRequest.builder()
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
                + "      \"createdAt\": \"2024-02-20T01:48:04Z\",\n"
                + "      \"id\": 4881,\n"
                + "      \"isDownloadable\": true,\n"
                + "      \"reportName\": \"Transaction-2024-02-20-000000-0-2.csv\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 1\n"
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
    public void testListNotifications() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"content\":{\"eventType\":\"ReceivedChargeBack\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88976,\"ownerId\":123,\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"},{\"content\":{\"eventType\":\"ReceivedAchReturn\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88975,\"ownerId\":123,\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"}],\"Summary\":{\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":2}}"));
        QueryResponseNotifications response = client.query()
                .listNotifications(
                        "8cfec329267",
                        ListNotificationsRequest.builder()
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
                + "      \"content\": {\n"
                + "        \"eventType\": \"ReceivedChargeBack\",\n"
                + "        \"timeZone\": 0\n"
                + "      },\n"
                + "      \"createdAt\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"frequency\": \"untilcancelled\",\n"
                + "      \"lastUpdated\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"method\": \"email\",\n"
                + "      \"notificationId\": 88976,\n"
                + "      \"ownerId\": 123,\n"
                + "      \"ownerName\": \"Pilgrim Planner\",\n"
                + "      \"ownerType\": 2,\n"
                + "      \"source\": \"api\",\n"
                + "      \"status\": 1,\n"
                + "      \"target\": \"example@example.com\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"content\": {\n"
                + "        \"eventType\": \"ReceivedAchReturn\",\n"
                + "        \"timeZone\": 0\n"
                + "      },\n"
                + "      \"createdAt\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"frequency\": \"untilcancelled\",\n"
                + "      \"lastUpdated\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"method\": \"email\",\n"
                + "      \"notificationId\": 88975,\n"
                + "      \"ownerId\": 123,\n"
                + "      \"ownerName\": \"Pilgrim Planner\",\n"
                + "      \"ownerType\": 2,\n"
                + "      \"source\": \"api\",\n"
                + "      \"status\": 1,\n"
                + "      \"target\": \"example@example.com\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 1,\n"
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
    public void testListNotificationsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"content\":{\"eventType\":\"ReceivedChargeBack\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88976,\"ownerId\":123,\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"},{\"content\":{\"eventType\":\"ReceivedAchReturn\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88975,\"ownerId\":123,\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"}],\"Summary\":{\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":2}}"));
        QueryResponseNotifications response = client.query()
                .listNotificationsOrg(
                        123,
                        ListNotificationsOrgRequest.builder()
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
                + "      \"content\": {\n"
                + "        \"eventType\": \"ReceivedChargeBack\",\n"
                + "        \"timeZone\": 0\n"
                + "      },\n"
                + "      \"createdAt\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"frequency\": \"untilcancelled\",\n"
                + "      \"lastUpdated\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"method\": \"email\",\n"
                + "      \"notificationId\": 88976,\n"
                + "      \"ownerId\": 123,\n"
                + "      \"ownerName\": \"Pilgrim Planner\",\n"
                + "      \"ownerType\": 2,\n"
                + "      \"source\": \"api\",\n"
                + "      \"status\": 1,\n"
                + "      \"target\": \"example@example.com\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"content\": {\n"
                + "        \"eventType\": \"ReceivedAchReturn\",\n"
                + "        \"timeZone\": 0\n"
                + "      },\n"
                + "      \"createdAt\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"frequency\": \"untilcancelled\",\n"
                + "      \"lastUpdated\": \"2022-06-07T05:00:00Z\",\n"
                + "      \"method\": \"email\",\n"
                + "      \"notificationId\": 88975,\n"
                + "      \"ownerId\": 123,\n"
                + "      \"ownerName\": \"Pilgrim Planner\",\n"
                + "      \"ownerType\": 2,\n"
                + "      \"source\": \"api\",\n"
                + "      \"status\": 1,\n"
                + "      \"target\": \"example@example.com\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 1,\n"
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
    public void testListOrganizations() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"services\":[{}],\"contacts\":[{}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"hasBilling\":true,\"hasResidual\":true,\"idOrg\":123,\"isRoot\":false,\"orgAddress\":\"123 Walnut Street\",\"orgCity\":\"Johnson City\",\"orgCountry\":\"US\",\"orgEntryName\":\"pilgrim-planner\",\"orgId\":\"I-123\",\"orgName\":\"Pilgrim Planner\",\"orgParentId\":236,\"orgParentName\":\"PropertyManager Pro\",\"orgState\":\"TN\",\"orgTimezone\":-5,\"orgType\":0,\"orgWebsite\":\"www.pilgrimageplanner.com\",\"orgZip\":\"orgZip\",\"recipientEmailNotification\":true,\"replyToEmail\":\"example@email.com\",\"resumable\":false,\"users\":[{\"createdAt\":\"2022-07-01T15:00:01Z\",\"UsrMFAMode\":0}]}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
        ListOrganizationsResponse response = client.query()
                .listOrganizations(
                        123,
                        ListOrganizationsRequest.builder()
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
                + "      \"services\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"contacts\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"hasBilling\": true,\n"
                + "      \"hasResidual\": true,\n"
                + "      \"idOrg\": 123,\n"
                + "      \"isRoot\": false,\n"
                + "      \"orgAddress\": \"123 Walnut Street\",\n"
                + "      \"orgCity\": \"Johnson City\",\n"
                + "      \"orgCountry\": \"US\",\n"
                + "      \"orgEntryName\": \"pilgrim-planner\",\n"
                + "      \"orgId\": \"I-123\",\n"
                + "      \"orgName\": \"Pilgrim Planner\",\n"
                + "      \"orgParentId\": 236,\n"
                + "      \"orgParentName\": \"PropertyManager Pro\",\n"
                + "      \"orgState\": \"TN\",\n"
                + "      \"orgTimezone\": -5,\n"
                + "      \"orgType\": 0,\n"
                + "      \"orgWebsite\": \"www.pilgrimageplanner.com\",\n"
                + "      \"orgZip\": \"orgZip\",\n"
                + "      \"recipientEmailNotification\": true,\n"
                + "      \"replyToEmail\": \"example@email.com\",\n"
                + "      \"resumable\": false,\n"
                + "      \"users\": [\n"
                + "        {\n"
                + "          \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "          \"UsrMFAMode\": 0\n"
                + "        }\n"
                + "      ]\n"
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
    public void testListPayout() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListPayout_response.json")));
        QueryPayoutTransaction response = client.query()
                .listPayout(
                        "8cfec329267",
                        ListPayoutRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListPayout_response.json");
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
    public void testListPayoutOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListPayoutOrg_response.json")));
        QueryPayoutTransaction response = client.query()
                .listPayoutOrg(
                        123,
                        ListPayoutOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListPayoutOrg_response.json");
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
    public void testListPaypoints() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListPaypoints_response.json")));
        QueryEntrypointResponse response = client.query()
                .listPaypoints(
                        123,
                        ListPaypointsRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListPaypoints_response.json");
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
    public void testListSettlements() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListSettlements_response.json")));
        QueryResponseSettlements response = client.query()
                .listSettlements(
                        "8cfec329267",
                        ListSettlementsRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListSettlements_response.json");
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
    public void testListSettlementsOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListSettlementsOrg_response.json")));
        QueryResponseSettlements response = client.query()
                .listSettlementsOrg(
                        123,
                        ListSettlementsOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListSettlementsOrg_response.json");
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
    public void testListSubscriptions() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListSubscriptions_response.json")));
        QuerySubscriptionResponse response = client.query()
                .listSubscriptions(
                        "8cfec329267",
                        ListSubscriptionsRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListSubscriptions_response.json");
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
    public void testListSubscriptionsOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/QueryWireTest_testListSubscriptionsOrg_response.json")));
        QuerySubscriptionResponse response = client.query()
                .listSubscriptionsOrg(
                        123,
                        ListSubscriptionsOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListSubscriptionsOrg_response.json");
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
    public void testListPayoutSubscriptions() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/QueryWireTest_testListPayoutSubscriptions_response.json")));
        QueryPayoutSubscriptionResponse response = client.query()
                .listPayoutSubscriptions(
                        "8cfec329267",
                        ListPayoutSubscriptionsRequest.builder()
                                .fromRecord(0)
                                .limitRecord(20)
                                .sortBy("desc(field_name)")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListPayoutSubscriptions_response.json");
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
    public void testListPayoutSubscriptionsOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/QueryWireTest_testListPayoutSubscriptionsOrg_response.json")));
        QueryPayoutSubscriptionResponse response = client.query()
                .listPayoutSubscriptionsOrg(
                        123,
                        ListPayoutSubscriptionsOrgRequest.builder()
                                .fromRecord(0)
                                .limitRecord(20)
                                .sortBy("desc(field_name)")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListPayoutSubscriptionsOrg_response.json");
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
    public void testListTransactions() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListTransactions_response.json")));
        QueryResponseTransactions response = client.query()
                .listTransactions(
                        "8cfec329267",
                        ListTransactionsRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransactions_response.json");
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
    public void testListTransactionsOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(
                        TestResources.loadResource("/wire-tests/QueryWireTest_testListTransactionsOrg_response.json")));
        QueryResponseTransactions response = client.query()
                .listTransactionsOrg(
                        123,
                        ListTransactionsOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransactionsOrg_response.json");
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
    public void testListTransferDetails() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(
                        TestResources.loadResource("/wire-tests/QueryWireTest_testListTransferDetails_response.json")));
        QueryTransferDetailResponse response = client.query()
                .listTransferDetails(
                        "47862acd",
                        123456,
                        ListTransfersPaypointRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransferDetails_response.json");
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
    public void testListTransfers() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListTransfers_response.json")));
        TransferQueryResponse response = client.query()
                .listTransfers(
                        "47862acd",
                        ListTransfersRequest.builder()
                                .fromRecord(0)
                                .limitRecord(20)
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransfers_response.json");
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
    public void testListTransfersOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListTransfersOrg_response.json")));
        TransferQueryResponse response = client.query()
                .listTransfersOrg(
                        123L,
                        ListTransfersRequestOrg.builder()
                                .fromRecord(0)
                                .limitRecord(20)
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransfersOrg_response.json");
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
    public void testListTransfersOutOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(
                        TestResources.loadResource("/wire-tests/QueryWireTest_testListTransfersOutOrg_response.json")));
        TransferOutQueryResponse response = client.query()
                .listTransfersOutOrg(
                        77,
                        ListTransfersOutOrgRequest.builder()
                                .fromRecord(0)
                                .limitRecord(20)
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransfersOutOrg_response.json");
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
    public void testListTransfersOutPaypoint() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/QueryWireTest_testListTransfersOutPaypoint_response.json")));
        TransferOutQueryResponse response = client.query()
                .listTransfersOutPaypoint(
                        "47cade237",
                        ListTransfersOutPaypointRequest.builder()
                                .fromRecord(0)
                                .limitRecord(20)
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransfersOutPaypoint_response.json");
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
    public void testListTransferDetailsOut() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/QueryWireTest_testListTransferDetailsOut_response.json")));
        TransferOutDetailQueryResponse response = client.query()
                .listTransferDetailsOut(
                        "47ace2b25",
                        4521,
                        ListTransferDetailsOutRequest.builder()
                                .fromRecord(0)
                                .limitRecord(20)
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListTransferDetailsOut_response.json");
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
    public void testListUsersOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"Access\":[{\"roleValue\":true}],\"AdditionalData\":\"AdditionalData\",\"createdAt\":\"2022-07-01T15:00:01Z\",\"Email\":\"example@email.com\",\"language\":\"en\",\"lastAccess\":\"2022-07-01T15:00:01Z\",\"Name\":\"Sean Smith\",\"Phone\":\"5555555555\",\"Scope\":[{\"orgType\":0}],\"snData\":\"snData\",\"snIdentifier\":\"snIdentifier\",\"snProvider\":\"google\",\"timeZone\":-5,\"userId\":1000000,\"UsrMFA\":false,\"UsrMFAMode\":0,\"UsrStatus\":1}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
        QueryUserResponse response = client.query()
                .listUsersOrg(
                        123,
                        ListUsersOrgRequest.builder()
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
    public void testListUsersPaypoint() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"Access\":[{\"roleValue\":true}],\"AdditionalData\":\"AdditionalData\",\"createdAt\":\"2022-07-01T15:00:01Z\",\"Email\":\"example@email.com\",\"language\":\"en\",\"lastAccess\":\"2022-07-01T15:00:01Z\",\"Name\":\"Sean Smith\",\"Phone\":\"5555555555\",\"Scope\":[{\"orgType\":0}],\"snData\":\"snData\",\"snIdentifier\":\"snIdentifier\",\"snProvider\":\"google\",\"timeZone\":-5,\"userId\":1000000,\"UsrMFA\":false,\"UsrMFAMode\":0,\"UsrStatus\":1}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
        QueryUserResponse response = client.query()
                .listUsersPaypoint(
                        "8cfec329267",
                        ListUsersPaypointRequest.builder()
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
    public void testListVendors() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListVendors_response.json")));
        QueryResponseVendors response = client.query()
                .listVendors(
                        "8cfec329267",
                        ListVendorsRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListVendors_response.json");
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
    public void testListVendorsOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListVendorsOrg_response.json")));
        QueryResponseVendors response = client.query()
                .listVendorsOrg(
                        123,
                        ListVendorsOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListVendorsOrg_response.json");
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
    public void testListVcards() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListVcards_response.json")));
        VCardQueryResponse response = client.query()
                .listVcards(
                        "8cfec329267",
                        ListVcardsRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListVcards_response.json");
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
    public void testListVcardsOrg() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/QueryWireTest_testListVcardsOrg_response.json")));
        VCardQueryResponse response = client.query()
                .listVcardsOrg(
                        123,
                        ListVcardsOrgRequest.builder()
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/QueryWireTest_testListVcardsOrg_response.json");
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
                if (actualValue == null) {
                    if (!entry.getValue().isNull()) return false;
                } else if (!jsonEquals(entry.getValue(), actualValue)) return false;
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
