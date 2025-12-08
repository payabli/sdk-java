package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
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
import io.github.payabli.api.resources.query.requests.ListPaypointsRequest;
import io.github.payabli.api.resources.query.requests.ListSettlementsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListSettlementsRequest;
import io.github.payabli.api.resources.query.requests.ListSubscriptionsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListSubscriptionsRequest;
import io.github.payabli.api.resources.query.requests.ListTransactionsOrgRequest;
import io.github.payabli.api.resources.query.requests.ListTransactionsRequest;
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
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"Id\":25048,\"Method\":\"ach\",\"WalletType\":null,\"SettledAmount\":0.5,\"Type\":\"credit\",\"BatchNumber\":\"batch-100-20-2024\",\"BatchAmount\":32,\"PaymentTransId\":\"245-9e4072eef77e45979ea0e49f680000X\",\"PaymentTransStatus\":1,\"ScheduleReference\":0,\"GatewayTransId\":\"TRN_XXXXX\",\"OrderId\":\"\",\"TransMethod\":\"ach\",\"PaymentData\":{\"AccountType\":\"Checking\",\"HolderName\":\"Lydia Marshall\",\"MaskedAccount\":\"1XXXXXX5678\",\"paymentDetails\":{\"categories\":[{\"amount\":1000,\"label\":\"Deposit\"}],\"currency\":\"USD\",\"serviceFee\":0,\"splitFunding\":[{}],\"totalAmount\":2}},\"NetAmount\":2,\"Operation\":\"Sale\",\"Category\":\"auth\",\"Source\":\"api\",\"Status\":1,\"TransactionTime\":\"2024-11-19T15:58:01Z\",\"Customer\":{\"AdditionalData\":null,\"BillingAddress1\":\"100 Golden Ridge Drive\",\"BillingAddress2\":\"STE 100\",\"BillingCity\":\"Mendota\",\"BillingCountry\":\"US\",\"BillingEmail\":\"lydia@example.com\",\"BillingPhone\":\"+12345678\",\"BillingState\":\"VA\",\"BillingZip\":\"20147\",\"customerId\":2707,\"CustomerNumber\":\"901102\",\"customerStatus\":1,\"FirstName\":\"Lydia\",\"LastName\":\"Marshall\"},\"SettlementDate\":\"2024-11-20T00:00:00Z\",\"PaymentSettlementStatus\":1,\"BatchStatus\":1,\"DepositDate\":\"2024-11-22T00:00:00Z\",\"ExpectedDepositDate\":\"2024-11-22T00:00:00Z\",\"MaskedAccount\":\"1XXXXXX5678\",\"CreatedAt\":\"2024-11-19T15:58:01Z\",\"PaypointLegalname\":\"Gruzya Adventure Outfitters, LLC\",\"ResponseData\":{\"authcode\":\"\",\"avsresponse_text\":\"\",\"cvvresponse_text\":\"\",\"response_code\":\"100\",\"response_code_text\":\"Operation successful.\",\"responsetext\":\"CAPTURED\",\"transactionid\":\"TRN_XXXXX\"},\"PaypointDbaname\":\"Gruzya Adventure Outfitters, LLC\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":123,\"PaypointEntryname\":\"7f1a3816XX\",\"DeviceId\":\"\",\"RetrievalId\":0,\"ChargebackId\":0,\"AchHolderType\":\"personal\",\"AchSecCode\":\"PPD\",\"ConnectorName\":\"DefaultConnector\",\"EntrypageId\":0,\"FeeAmount\":0,\"OrgId\":123,\"PayorId\":2707,\"PaypointId\":123,\"PendingFeeAmount\":0,\"RefundId\":0,\"ReturnedId\":0,\"splitFundingInstructions\":[],\"TotalAmount\":2,\"CfeeTransactions\":[],\"invoiceData\":null,\"TransactionEvents\":[{\"EventTime\":\"2024-11-19T15:57:40Z\",\"TransEvent\":\"Created\"},{\"EventData\":{\"account_id\":\"TRA_XXXXX\",\"account_name\":\"123456\",\"action\":{\"app_id\":\"XXXXX\",\"app_name\":\"PayAbli\",\"id\":\"ACT_XXXXX\",\"result_code\":\"SUCCESS\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"AUTHORIZE\"},\"amount\":\"200\",\"batch_id\":\"\",\"capture_mode\":\"AUTO\",\"channel\":\"CNP\",\"country\":\"US\",\"currency\":\"USD\",\"fees\":{\"amount\":\"0\",\"rate\":\"0.00\",\"total_amount\":\"0\"},\"id\":\"TRN_XXXXX\",\"merchant_amount\":\"200\",\"merchant_id\":\"MER_XXXXX\",\"merchant_name\":\"Henriette97\",\"order\":{\"reference\":\"\"},\"payment_method\":{\"bank_transfer\":{\"account_type\":\"CHECKING\",\"bank\":{\"name\":\"\"},\"masked_account_number_last4\":\"XXXX5678\"},\"entry_mode\":\"ECOM\",\"message\":\"Success\",\"narrative\":\"Lydia Marshall\",\"result\":\"00\"},\"reference\":\"245-XXXXX\",\"status\":\"CAPTURED\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"SALE\"},\"EventTime\":\"2024-11-19T20:58:01Z\",\"TransEvent\":\"Approved\"},{\"EventTime\":\"2024-11-20T03:05:10Z\",\"TransEvent\":\"ClosedBatch\"}],\"externalPaypointID\":\"ext-paypoint-123\",\"isHold\":0}],\"Summary\":{\"serviceFees\":852.48,\"transferAmount\":0,\"refunds\":-3521.85,\"heldAmount\":3.7,\"totalRecords\":21872,\"totalAmount\":61645.74,\"totalNetAmount\":61645.74,\"totalPages\":21872,\"pageSize\":0,\"pageidentifier\":null}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"Id\": 25048,\n"
                + "      \"Method\": \"ach\",\n"
                + "      \"WalletType\": null,\n"
                + "      \"SettledAmount\": 0.5,\n"
                + "      \"Type\": \"credit\",\n"
                + "      \"BatchNumber\": \"batch-100-20-2024\",\n"
                + "      \"BatchAmount\": 32,\n"
                + "      \"PaymentTransId\": \"245-9e4072eef77e45979ea0e49f680000X\",\n"
                + "      \"PaymentTransStatus\": 1,\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"GatewayTransId\": \"TRN_XXXXX\",\n"
                + "      \"OrderId\": \"\",\n"
                + "      \"TransMethod\": \"ach\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"AccountType\": \"Checking\",\n"
                + "        \"HolderName\": \"Lydia Marshall\",\n"
                + "        \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "        \"paymentDetails\": {\n"
                + "          \"categories\": [\n"
                + "            {\n"
                + "              \"amount\": 1000,\n"
                + "              \"label\": \"Deposit\"\n"
                + "            }\n"
                + "          ],\n"
                + "          \"currency\": \"USD\",\n"
                + "          \"serviceFee\": 0,\n"
                + "          \"splitFunding\": [\n"
                + "            {}\n"
                + "          ],\n"
                + "          \"totalAmount\": 2\n"
                + "        }\n"
                + "      },\n"
                + "      \"NetAmount\": 2,\n"
                + "      \"Operation\": \"Sale\",\n"
                + "      \"Category\": \"auth\",\n"
                + "      \"Source\": \"api\",\n"
                + "      \"Status\": 1,\n"
                + "      \"TransactionTime\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": null,\n"
                + "        \"BillingAddress1\": \"100 Golden Ridge Drive\",\n"
                + "        \"BillingAddress2\": \"STE 100\",\n"
                + "        \"BillingCity\": \"Mendota\",\n"
                + "        \"BillingCountry\": \"US\",\n"
                + "        \"BillingEmail\": \"lydia@example.com\",\n"
                + "        \"BillingPhone\": \"+12345678\",\n"
                + "        \"BillingState\": \"VA\",\n"
                + "        \"BillingZip\": \"20147\",\n"
                + "        \"customerId\": 2707,\n"
                + "        \"CustomerNumber\": \"901102\",\n"
                + "        \"customerStatus\": 1,\n"
                + "        \"FirstName\": \"Lydia\",\n"
                + "        \"LastName\": \"Marshall\"\n"
                + "      },\n"
                + "      \"SettlementDate\": \"2024-11-20T00:00:00Z\",\n"
                + "      \"PaymentSettlementStatus\": 1,\n"
                + "      \"BatchStatus\": 1,\n"
                + "      \"DepositDate\": \"2024-11-22T00:00:00Z\",\n"
                + "      \"ExpectedDepositDate\": \"2024-11-22T00:00:00Z\",\n"
                + "      \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "      \"CreatedAt\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"PaypointLegalname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"ResponseData\": {\n"
                + "        \"authcode\": \"\",\n"
                + "        \"avsresponse_text\": \"\",\n"
                + "        \"cvvresponse_text\": \"\",\n"
                + "        \"response_code\": \"100\",\n"
                + "        \"response_code_text\": \"Operation successful.\",\n"
                + "        \"responsetext\": \"CAPTURED\",\n"
                + "        \"transactionid\": \"TRN_XXXXX\"\n"
                + "      },\n"
                + "      \"PaypointDbaname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 123,\n"
                + "      \"PaypointEntryname\": \"7f1a3816XX\",\n"
                + "      \"DeviceId\": \"\",\n"
                + "      \"RetrievalId\": 0,\n"
                + "      \"ChargebackId\": 0,\n"
                + "      \"AchHolderType\": \"personal\",\n"
                + "      \"AchSecCode\": \"PPD\",\n"
                + "      \"ConnectorName\": \"DefaultConnector\",\n"
                + "      \"EntrypageId\": 0,\n"
                + "      \"FeeAmount\": 0,\n"
                + "      \"OrgId\": 123,\n"
                + "      \"PayorId\": 2707,\n"
                + "      \"PaypointId\": 123,\n"
                + "      \"PendingFeeAmount\": 0,\n"
                + "      \"RefundId\": 0,\n"
                + "      \"ReturnedId\": 0,\n"
                + "      \"splitFundingInstructions\": [],\n"
                + "      \"TotalAmount\": 2,\n"
                + "      \"CfeeTransactions\": [],\n"
                + "      \"invoiceData\": null,\n"
                + "      \"TransactionEvents\": [\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-19T15:57:40Z\",\n"
                + "          \"TransEvent\": \"Created\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventData\": {\n"
                + "            \"account_id\": \"TRA_XXXXX\",\n"
                + "            \"account_name\": \"123456\",\n"
                + "            \"action\": {\n"
                + "              \"app_id\": \"XXXXX\",\n"
                + "              \"app_name\": \"PayAbli\",\n"
                + "              \"id\": \"ACT_XXXXX\",\n"
                + "              \"result_code\": \"SUCCESS\",\n"
                + "              \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "              \"type\": \"AUTHORIZE\"\n"
                + "            },\n"
                + "            \"amount\": \"200\",\n"
                + "            \"batch_id\": \"\",\n"
                + "            \"capture_mode\": \"AUTO\",\n"
                + "            \"channel\": \"CNP\",\n"
                + "            \"country\": \"US\",\n"
                + "            \"currency\": \"USD\",\n"
                + "            \"fees\": {\n"
                + "              \"amount\": \"0\",\n"
                + "              \"rate\": \"0.00\",\n"
                + "              \"total_amount\": \"0\"\n"
                + "            },\n"
                + "            \"id\": \"TRN_XXXXX\",\n"
                + "            \"merchant_amount\": \"200\",\n"
                + "            \"merchant_id\": \"MER_XXXXX\",\n"
                + "            \"merchant_name\": \"Henriette97\",\n"
                + "            \"order\": {\n"
                + "              \"reference\": \"\"\n"
                + "            },\n"
                + "            \"payment_method\": {\n"
                + "              \"bank_transfer\": {\n"
                + "                \"account_type\": \"CHECKING\",\n"
                + "                \"bank\": {\n"
                + "                  \"name\": \"\"\n"
                + "                },\n"
                + "                \"masked_account_number_last4\": \"XXXX5678\"\n"
                + "              },\n"
                + "              \"entry_mode\": \"ECOM\",\n"
                + "              \"message\": \"Success\",\n"
                + "              \"narrative\": \"Lydia Marshall\",\n"
                + "              \"result\": \"00\"\n"
                + "            },\n"
                + "            \"reference\": \"245-XXXXX\",\n"
                + "            \"status\": \"CAPTURED\",\n"
                + "            \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "            \"type\": \"SALE\"\n"
                + "          },\n"
                + "          \"EventTime\": \"2024-11-19T20:58:01Z\",\n"
                + "          \"TransEvent\": \"Approved\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-20T03:05:10Z\",\n"
                + "          \"TransEvent\": \"ClosedBatch\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"externalPaypointID\": \"ext-paypoint-123\",\n"
                + "      \"isHold\": 0\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"serviceFees\": 852.48,\n"
                + "    \"transferAmount\": 0,\n"
                + "    \"refunds\": -3521.85,\n"
                + "    \"heldAmount\": 3.7,\n"
                + "    \"totalRecords\": 21872,\n"
                + "    \"totalAmount\": 61645.74,\n"
                + "    \"totalNetAmount\": 61645.74,\n"
                + "    \"totalPages\": 21872,\n"
                + "    \"pageSize\": 0,\n"
                + "    \"pageidentifier\": null\n"
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
    public void testListBatchDetailsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"BatchAmount\":32,\"BatchNumber\":\"batch-100-20-2024\",\"Category\":\"auth\",\"CreatedAt\":\"2024-11-19T15:58:01Z\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"100 Golden Ridge Drive\",\"BillingAddress2\":\"STE 100\",\"BillingCity\":\"Mendota\",\"BillingCountry\":\"US\",\"BillingEmail\":\"lydia@example.com\",\"BillingPhone\":\"+12345678\",\"BillingState\":\"VA\",\"BillingZip\":\"20147\",\"customerId\":2707,\"CustomerNumber\":\"901102\",\"customerStatus\":1,\"FirstName\":\"Lydia\",\"LastName\":\"Marshall\"},\"ExpectedDepositDate\":\"2024-11-22T00:00:00Z\",\"GatewayTransId\":\"TRN_XXXXX\",\"Id\":25048,\"isHold\":0,\"MaskedAccount\":\"1XXXXXX5678\",\"Method\":\"ach\",\"NetAmount\":2,\"Operation\":\"Sale\",\"OrderId\":\"\",\"ParentOrgName\":\"Pilgrim Planner\",\"PaymentData\":{\"AccountType\":\"Checking\",\"HolderName\":\"Lydia Marshall\",\"MaskedAccount\":\"1XXXXXX5678\",\"paymentDetails\":{\"categories\":[{\"amount\":1000,\"label\":\"Deposit\"}],\"currency\":\"USD\",\"serviceFee\":0,\"splitFunding\":[{}],\"totalAmount\":2}},\"PaymentTransId\":\"245-9e4072eef77e45979ea0e49f680000X\",\"PaymentTransStatus\":1,\"PaypointDbaname\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointEntryname\":\"7f1a3816XX\",\"PaypointLegalname\":\"Gruzya Adventure Outfitters, LLC\",\"ResponseData\":{\"authcode\":\"\",\"avsresponse_text\":\"\",\"cvvresponse_text\":\"\",\"response_code\":\"100\",\"response_code_text\":\"Operation successful.\",\"responsetext\":\"CAPTURED\",\"transactionid\":\"TRN_XXXXX\"},\"ScheduleReference\":0,\"SettledAmount\":0.5,\"SettlementDate\":\"2024-11-20T00:00:00Z\",\"Source\":\"api\",\"Status\":1,\"TransactionEvents\":[{\"EventTime\":\"2024-11-19T15:57:40Z\",\"TransEvent\":\"Created\"},{\"EventData\":{\"account_id\":\"TRA_XXXXX\",\"account_name\":\"123456\",\"action\":{\"app_id\":\"XXXXX\",\"app_name\":\"PayAbli\",\"id\":\"ACT_XXXXX\",\"result_code\":\"SUCCESS\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"AUTHORIZE\"},\"amount\":\"200\",\"batch_id\":\"\",\"capture_mode\":\"AUTO\",\"channel\":\"CNP\",\"country\":\"US\",\"currency\":\"USD\",\"fees\":{\"amount\":\"0\",\"rate\":\"0.00\",\"total_amount\":\"0\"},\"id\":\"TRN_XXXXX\",\"merchant_amount\":\"200\",\"merchant_id\":\"MER_XXXXX\",\"merchant_name\":\"Henriette97\",\"order\":{\"reference\":\"\"},\"payment_method\":{\"bank_transfer\":{\"account_type\":\"CHECKING\",\"bank\":{\"name\":\"\"},\"masked_account_number_last4\":\"XXXX5678\"},\"entry_mode\":\"ECOM\",\"message\":\"Success\",\"narrative\":\"Lydia Marshall\",\"result\":\"00\"},\"reference\":\"245-XXXXX\",\"status\":\"CAPTURED\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"SALE\"},\"EventTime\":\"2024-11-19T20:58:01Z\",\"TransEvent\":\"Approved\"},{\"EventTime\":\"2024-11-20T03:05:10Z\",\"TransEvent\":\"ClosedBatch\"}],\"TransactionTime\":\"2024-11-19T15:58:01Z\",\"TransMethod\":\"ach\",\"Type\":\"credit\"}],\"Summary\":{\"heldAmount\":3.7,\"pageSize\":0,\"refunds\":-3521.85,\"serviceFees\":852.48,\"totalAmount\":61645.74,\"totalNetAmount\":61645.74,\"totalPages\":21872,\"totalRecords\":21872,\"transferAmount\":0}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"BatchAmount\": 32,\n"
                + "      \"BatchNumber\": \"batch-100-20-2024\",\n"
                + "      \"Category\": \"auth\",\n"
                + "      \"CreatedAt\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": \"AdditionalData\",\n"
                + "        \"BillingAddress1\": \"100 Golden Ridge Drive\",\n"
                + "        \"BillingAddress2\": \"STE 100\",\n"
                + "        \"BillingCity\": \"Mendota\",\n"
                + "        \"BillingCountry\": \"US\",\n"
                + "        \"BillingEmail\": \"lydia@example.com\",\n"
                + "        \"BillingPhone\": \"+12345678\",\n"
                + "        \"BillingState\": \"VA\",\n"
                + "        \"BillingZip\": \"20147\",\n"
                + "        \"customerId\": 2707,\n"
                + "        \"CustomerNumber\": \"901102\",\n"
                + "        \"customerStatus\": 1,\n"
                + "        \"FirstName\": \"Lydia\",\n"
                + "        \"LastName\": \"Marshall\"\n"
                + "      },\n"
                + "      \"ExpectedDepositDate\": \"2024-11-22T00:00:00Z\",\n"
                + "      \"GatewayTransId\": \"TRN_XXXXX\",\n"
                + "      \"Id\": 25048,\n"
                + "      \"isHold\": 0,\n"
                + "      \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "      \"Method\": \"ach\",\n"
                + "      \"NetAmount\": 2,\n"
                + "      \"Operation\": \"Sale\",\n"
                + "      \"OrderId\": \"\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"AccountType\": \"Checking\",\n"
                + "        \"HolderName\": \"Lydia Marshall\",\n"
                + "        \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "        \"paymentDetails\": {\n"
                + "          \"categories\": [\n"
                + "            {\n"
                + "              \"amount\": 1000,\n"
                + "              \"label\": \"Deposit\"\n"
                + "            }\n"
                + "          ],\n"
                + "          \"currency\": \"USD\",\n"
                + "          \"serviceFee\": 0,\n"
                + "          \"splitFunding\": [\n"
                + "            {}\n"
                + "          ],\n"
                + "          \"totalAmount\": 2\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaymentTransId\": \"245-9e4072eef77e45979ea0e49f680000X\",\n"
                + "      \"PaymentTransStatus\": 1,\n"
                + "      \"PaypointDbaname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointEntryname\": \"7f1a3816XX\",\n"
                + "      \"PaypointLegalname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"ResponseData\": {\n"
                + "        \"authcode\": \"\",\n"
                + "        \"avsresponse_text\": \"\",\n"
                + "        \"cvvresponse_text\": \"\",\n"
                + "        \"response_code\": \"100\",\n"
                + "        \"response_code_text\": \"Operation successful.\",\n"
                + "        \"responsetext\": \"CAPTURED\",\n"
                + "        \"transactionid\": \"TRN_XXXXX\"\n"
                + "      },\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"SettledAmount\": 0.5,\n"
                + "      \"SettlementDate\": \"2024-11-20T00:00:00Z\",\n"
                + "      \"Source\": \"api\",\n"
                + "      \"Status\": 1,\n"
                + "      \"TransactionEvents\": [\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-19T15:57:40Z\",\n"
                + "          \"TransEvent\": \"Created\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventData\": {\n"
                + "            \"account_id\": \"TRA_XXXXX\",\n"
                + "            \"account_name\": \"123456\",\n"
                + "            \"action\": {\n"
                + "              \"app_id\": \"XXXXX\",\n"
                + "              \"app_name\": \"PayAbli\",\n"
                + "              \"id\": \"ACT_XXXXX\",\n"
                + "              \"result_code\": \"SUCCESS\",\n"
                + "              \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "              \"type\": \"AUTHORIZE\"\n"
                + "            },\n"
                + "            \"amount\": \"200\",\n"
                + "            \"batch_id\": \"\",\n"
                + "            \"capture_mode\": \"AUTO\",\n"
                + "            \"channel\": \"CNP\",\n"
                + "            \"country\": \"US\",\n"
                + "            \"currency\": \"USD\",\n"
                + "            \"fees\": {\n"
                + "              \"amount\": \"0\",\n"
                + "              \"rate\": \"0.00\",\n"
                + "              \"total_amount\": \"0\"\n"
                + "            },\n"
                + "            \"id\": \"TRN_XXXXX\",\n"
                + "            \"merchant_amount\": \"200\",\n"
                + "            \"merchant_id\": \"MER_XXXXX\",\n"
                + "            \"merchant_name\": \"Henriette97\",\n"
                + "            \"order\": {\n"
                + "              \"reference\": \"\"\n"
                + "            },\n"
                + "            \"payment_method\": {\n"
                + "              \"bank_transfer\": {\n"
                + "                \"account_type\": \"CHECKING\",\n"
                + "                \"bank\": {\n"
                + "                  \"name\": \"\"\n"
                + "                },\n"
                + "                \"masked_account_number_last4\": \"XXXX5678\"\n"
                + "              },\n"
                + "              \"entry_mode\": \"ECOM\",\n"
                + "              \"message\": \"Success\",\n"
                + "              \"narrative\": \"Lydia Marshall\",\n"
                + "              \"result\": \"00\"\n"
                + "            },\n"
                + "            \"reference\": \"245-XXXXX\",\n"
                + "            \"status\": \"CAPTURED\",\n"
                + "            \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "            \"type\": \"SALE\"\n"
                + "          },\n"
                + "          \"EventTime\": \"2024-11-19T20:58:01Z\",\n"
                + "          \"TransEvent\": \"Approved\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-20T03:05:10Z\",\n"
                + "          \"TransEvent\": \"ClosedBatch\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"TransactionTime\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"TransMethod\": \"ach\",\n"
                + "      \"Type\": \"credit\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"heldAmount\": 3.7,\n"
                + "    \"pageSize\": 0,\n"
                + "    \"refunds\": -3521.85,\n"
                + "    \"serviceFees\": 852.48,\n"
                + "    \"totalAmount\": 61645.74,\n"
                + "    \"totalNetAmount\": 61645.74,\n"
                + "    \"totalPages\": 21872,\n"
                + "    \"totalRecords\": 21872,\n"
                + "    \"transferAmount\": 0\n"
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
    public void testListBatches() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Summary\":{\"pageidentifier\":null,\"pageSize\":20,\"totalAmount\":54049.71,\"totalNetAmount\":0,\"totalPages\":3,\"totalRecords\":3},\"Records\":[{\"IdBatch\":1049,\"BatchNumber\":\"batch_2857_combined_08-26-2025_001\",\"TransferIdentifier\":null,\"EventsData\":[{\"description\":\"Created\",\"eventTime\":\"2025-08-25T03:19:27.6190027-04:00\",\"refData\":null,\"extraData\":null,\"source\":\"api\"}],\"ConnectorName\":\"GP\",\"BatchDate\":\"2025-08-25T20:00:00\",\"BatchAmount\":0,\"BatchFeesAmount\":0,\"BatchAuthAmount\":0,\"BatchReleasedAmount\":0,\"BatchHoldAmount\":0,\"BatchReturnedAmount\":0,\"BatchRefundAmount\":0,\"BatchSplitAmount\":0,\"BatchStatus\":2,\"BatchRecords\":2,\"PaypointId\":187,\"PaypointName\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointDba\":\"Gruzya Adventure Outfitters\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":105,\"externalPaypointID\":null,\"EntryName\":\"47f4f8c7e1\",\"BankName\":null,\"BatchType\":0,\"Method\":\"combined\",\"ExpectedDepositDate\":\"2025-08-26T00:00:00Z\",\"DepositDate\":null,\"TransferDate\":\"2025-08-26T00:00:00Z\",\"Transfer\":null},{\"IdBatch\":1043,\"BatchNumber\":\"BT-2023041817-187\",\"TransferIdentifier\":null,\"EventsData\":null,\"ConnectorName\":null,\"BatchDate\":\"2023-04-18T17:01:03Z\",\"BatchAmount\":219.02,\"BatchFeesAmount\":0,\"BatchAuthAmount\":0,\"BatchReleasedAmount\":0,\"BatchHoldAmount\":0,\"BatchReturnedAmount\":0,\"BatchRefundAmount\":0,\"BatchSplitAmount\":0,\"BatchStatus\":2,\"BatchRecords\":1,\"PaypointId\":187,\"PaypointName\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointDba\":\"Gruzya Adventure Outfitters\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":105,\"externalPaypointID\":null,\"EntryName\":\"d193cf9a46\",\"BankName\":null,\"BatchType\":0,\"Method\":\"card\",\"ExpectedDepositDate\":\"2023-04-19T00:00:00Z\",\"DepositDate\":null,\"TransferDate\":\"2025-09-02T00:00:00Z\",\"Transfer\":null},{\"IdBatch\":1012,\"BatchNumber\":\"BT-2023041421-187\",\"TransferIdentifier\":\"ec310c3d-d4bf-4670-9524-00fcc4ab6a2a\",\"EventsData\":[{\"description\":\"Created\",\"eventTime\":\"2023-04-14T21:01:03Z\",\"refData\":null,\"extraData\":null,\"source\":\"api\"},{\"description\":\"Closed\",\"eventTime\":\"2023-04-15T03:05:10Z\",\"refData\":\"batchId: 1012\",\"extraData\":null,\"source\":\"worker\"}],\"ConnectorName\":\"GP\",\"BatchDate\":\"2023-04-14T21:01:03Z\",\"BatchAmount\":1080.44,\"BatchFeesAmount\":0,\"BatchAuthAmount\":1080.44,\"BatchReleasedAmount\":0,\"BatchHoldAmount\":0,\"BatchReturnedAmount\":0,\"BatchRefundAmount\":0,\"BatchSplitAmount\":0,\"BatchStatus\":2,\"BatchRecords\":4,\"PaypointId\":187,\"PaypointName\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointDba\":\"Gruzya Adventure Outfitters\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":105,\"externalPaypointID\":null,\"EntryName\":\"d193cf9a46\",\"BankName\":null,\"BatchType\":0,\"Method\":\"card\",\"ExpectedDepositDate\":\"2023-04-15T00:00:00Z\",\"DepositDate\":null,\"TransferDate\":\"2025-09-02T00:00:00Z\",\"Transfer\":{\"TransferId\":5998,\"TransferDate\":\"2025-09-02T00:00:00Z\",\"Processor\":\"gp\",\"TransferStatus\":1,\"GrossAmount\":1080.44,\"ChargeBackAmount\":0,\"ReturnedAmount\":0,\"RefundAmount\":0,\"HoldAmount\":0,\"ReleasedAmount\":0,\"BillingFeesAmount\":0,\"ThirdPartyPaidAmount\":0,\"AdjustmentsAmount\":0,\"NetFundedAmount\":1080.44}}]}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Summary\": {\n"
                + "    \"pageidentifier\": null,\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 54049.71,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 3,\n"
                + "    \"totalRecords\": 3\n"
                + "  },\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"IdBatch\": 1049,\n"
                + "      \"BatchNumber\": \"batch_2857_combined_08-26-2025_001\",\n"
                + "      \"TransferIdentifier\": null,\n"
                + "      \"EventsData\": [\n"
                + "        {\n"
                + "          \"description\": \"Created\",\n"
                + "          \"eventTime\": \"2025-08-25T03:19:27.6190027-04:00\",\n"
                + "          \"refData\": null,\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"api\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"ConnectorName\": \"GP\",\n"
                + "      \"BatchDate\": \"2025-08-25T20:00:00\",\n"
                + "      \"BatchAmount\": 0,\n"
                + "      \"BatchFeesAmount\": 0,\n"
                + "      \"BatchAuthAmount\": 0,\n"
                + "      \"BatchReleasedAmount\": 0,\n"
                + "      \"BatchHoldAmount\": 0,\n"
                + "      \"BatchReturnedAmount\": 0,\n"
                + "      \"BatchRefundAmount\": 0,\n"
                + "      \"BatchSplitAmount\": 0,\n"
                + "      \"BatchStatus\": 2,\n"
                + "      \"BatchRecords\": 2,\n"
                + "      \"PaypointId\": 187,\n"
                + "      \"PaypointName\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointDba\": \"Gruzya Adventure Outfitters\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 105,\n"
                + "      \"externalPaypointID\": null,\n"
                + "      \"EntryName\": \"47f4f8c7e1\",\n"
                + "      \"BankName\": null,\n"
                + "      \"BatchType\": 0,\n"
                + "      \"Method\": \"combined\",\n"
                + "      \"ExpectedDepositDate\": \"2025-08-26T00:00:00Z\",\n"
                + "      \"DepositDate\": null,\n"
                + "      \"TransferDate\": \"2025-08-26T00:00:00Z\",\n"
                + "      \"Transfer\": null\n"
                + "    },\n"
                + "    {\n"
                + "      \"IdBatch\": 1043,\n"
                + "      \"BatchNumber\": \"BT-2023041817-187\",\n"
                + "      \"TransferIdentifier\": null,\n"
                + "      \"EventsData\": null,\n"
                + "      \"ConnectorName\": null,\n"
                + "      \"BatchDate\": \"2023-04-18T17:01:03Z\",\n"
                + "      \"BatchAmount\": 219.02,\n"
                + "      \"BatchFeesAmount\": 0,\n"
                + "      \"BatchAuthAmount\": 0,\n"
                + "      \"BatchReleasedAmount\": 0,\n"
                + "      \"BatchHoldAmount\": 0,\n"
                + "      \"BatchReturnedAmount\": 0,\n"
                + "      \"BatchRefundAmount\": 0,\n"
                + "      \"BatchSplitAmount\": 0,\n"
                + "      \"BatchStatus\": 2,\n"
                + "      \"BatchRecords\": 1,\n"
                + "      \"PaypointId\": 187,\n"
                + "      \"PaypointName\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointDba\": \"Gruzya Adventure Outfitters\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 105,\n"
                + "      \"externalPaypointID\": null,\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"BankName\": null,\n"
                + "      \"BatchType\": 0,\n"
                + "      \"Method\": \"card\",\n"
                + "      \"ExpectedDepositDate\": \"2023-04-19T00:00:00Z\",\n"
                + "      \"DepositDate\": null,\n"
                + "      \"TransferDate\": \"2025-09-02T00:00:00Z\",\n"
                + "      \"Transfer\": null\n"
                + "    },\n"
                + "    {\n"
                + "      \"IdBatch\": 1012,\n"
                + "      \"BatchNumber\": \"BT-2023041421-187\",\n"
                + "      \"TransferIdentifier\": \"ec310c3d-d4bf-4670-9524-00fcc4ab6a2a\",\n"
                + "      \"EventsData\": [\n"
                + "        {\n"
                + "          \"description\": \"Created\",\n"
                + "          \"eventTime\": \"2023-04-14T21:01:03Z\",\n"
                + "          \"refData\": null,\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"api\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"description\": \"Closed\",\n"
                + "          \"eventTime\": \"2023-04-15T03:05:10Z\",\n"
                + "          \"refData\": \"batchId: 1012\",\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"worker\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"ConnectorName\": \"GP\",\n"
                + "      \"BatchDate\": \"2023-04-14T21:01:03Z\",\n"
                + "      \"BatchAmount\": 1080.44,\n"
                + "      \"BatchFeesAmount\": 0,\n"
                + "      \"BatchAuthAmount\": 1080.44,\n"
                + "      \"BatchReleasedAmount\": 0,\n"
                + "      \"BatchHoldAmount\": 0,\n"
                + "      \"BatchReturnedAmount\": 0,\n"
                + "      \"BatchRefundAmount\": 0,\n"
                + "      \"BatchSplitAmount\": 0,\n"
                + "      \"BatchStatus\": 2,\n"
                + "      \"BatchRecords\": 4,\n"
                + "      \"PaypointId\": 187,\n"
                + "      \"PaypointName\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointDba\": \"Gruzya Adventure Outfitters\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 105,\n"
                + "      \"externalPaypointID\": null,\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"BankName\": null,\n"
                + "      \"BatchType\": 0,\n"
                + "      \"Method\": \"card\",\n"
                + "      \"ExpectedDepositDate\": \"2023-04-15T00:00:00Z\",\n"
                + "      \"DepositDate\": null,\n"
                + "      \"TransferDate\": \"2025-09-02T00:00:00Z\",\n"
                + "      \"Transfer\": {\n"
                + "        \"TransferId\": 5998,\n"
                + "        \"TransferDate\": \"2025-09-02T00:00:00Z\",\n"
                + "        \"Processor\": \"gp\",\n"
                + "        \"TransferStatus\": 1,\n"
                + "        \"GrossAmount\": 1080.44,\n"
                + "        \"ChargeBackAmount\": 0,\n"
                + "        \"ReturnedAmount\": 0,\n"
                + "        \"RefundAmount\": 0,\n"
                + "        \"HoldAmount\": 0,\n"
                + "        \"ReleasedAmount\": 0,\n"
                + "        \"BillingFeesAmount\": 0,\n"
                + "        \"ThirdPartyPaidAmount\": 0,\n"
                + "        \"AdjustmentsAmount\": 0,\n"
                + "        \"NetFundedAmount\": 1080.44\n"
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
    public void testListBatchesOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Summary\":{\"pageidentifier\":null,\"pageSize\":20,\"totalAmount\":54049.71,\"totalNetAmount\":0,\"totalPages\":3,\"totalRecords\":3},\"Records\":[{\"IdBatch\":1049,\"BatchNumber\":\"batch_2857_combined_08-26-2025_001\",\"TransferIdentifier\":null,\"EventsData\":[{\"description\":\"Created\",\"eventTime\":\"2025-08-25T03:19:27.6190027-04:00\",\"refData\":null,\"extraData\":null,\"source\":\"api\"}],\"ConnectorName\":\"GP\",\"BatchDate\":\"2025-08-25T20:00:00\",\"BatchAmount\":0,\"BatchFeesAmount\":0,\"BatchAuthAmount\":0,\"BatchReleasedAmount\":0,\"BatchHoldAmount\":0,\"BatchReturnedAmount\":0,\"BatchRefundAmount\":0,\"BatchSplitAmount\":0,\"BatchStatus\":2,\"BatchRecords\":2,\"PaypointId\":187,\"PaypointName\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointDba\":\"Gruzya Adventure Outfitters\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":105,\"externalPaypointID\":null,\"EntryName\":\"47f4f8c7e1\",\"BankName\":null,\"BatchType\":0,\"Method\":\"combined\",\"ExpectedDepositDate\":\"2025-08-26T00:00:00Z\",\"DepositDate\":null,\"TransferDate\":\"2025-08-26T00:00:00Z\",\"Transfer\":null},{\"IdBatch\":1043,\"BatchNumber\":\"BT-2023041817-187\",\"TransferIdentifier\":null,\"EventsData\":null,\"ConnectorName\":null,\"BatchDate\":\"2023-04-18T17:01:03Z\",\"BatchAmount\":219.02,\"BatchFeesAmount\":0,\"BatchAuthAmount\":0,\"BatchReleasedAmount\":0,\"BatchHoldAmount\":0,\"BatchReturnedAmount\":0,\"BatchRefundAmount\":0,\"BatchSplitAmount\":0,\"BatchStatus\":2,\"BatchRecords\":1,\"PaypointId\":187,\"PaypointName\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointDba\":\"Gruzya Adventure Outfitters\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":105,\"externalPaypointID\":null,\"EntryName\":\"d193cf9a46\",\"BankName\":null,\"BatchType\":0,\"Method\":\"card\",\"ExpectedDepositDate\":\"2023-04-19T00:00:00Z\",\"DepositDate\":null,\"TransferDate\":\"2025-09-02T00:00:00Z\",\"Transfer\":null},{\"IdBatch\":1012,\"BatchNumber\":\"BT-2023041421-187\",\"TransferIdentifier\":\"ec310c3d-d4bf-4670-9524-00fcc4ab6a2a\",\"EventsData\":[{\"description\":\"Created\",\"eventTime\":\"2023-04-14T21:01:03Z\",\"refData\":null,\"extraData\":null,\"source\":\"api\"},{\"description\":\"Closed\",\"eventTime\":\"2023-04-15T03:05:10Z\",\"refData\":\"batchId: 1012\",\"extraData\":null,\"source\":\"worker\"}],\"ConnectorName\":\"GP\",\"BatchDate\":\"2023-04-14T21:01:03Z\",\"BatchAmount\":1080.44,\"BatchFeesAmount\":0,\"BatchAuthAmount\":1080.44,\"BatchReleasedAmount\":0,\"BatchHoldAmount\":0,\"BatchReturnedAmount\":0,\"BatchRefundAmount\":0,\"BatchSplitAmount\":0,\"BatchStatus\":2,\"BatchRecords\":4,\"PaypointId\":187,\"PaypointName\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointDba\":\"Gruzya Adventure Outfitters\",\"ParentOrgName\":\"Pilgrim Planner\",\"ParentOrgId\":105,\"externalPaypointID\":null,\"EntryName\":\"d193cf9a46\",\"BankName\":null,\"BatchType\":0,\"Method\":\"card\",\"ExpectedDepositDate\":\"2023-04-15T00:00:00Z\",\"DepositDate\":null,\"TransferDate\":\"2025-09-02T00:00:00Z\",\"Transfer\":{\"TransferId\":5998,\"TransferDate\":\"2025-09-02T00:00:00Z\",\"Processor\":\"gp\",\"TransferStatus\":1,\"GrossAmount\":1080.44,\"ChargeBackAmount\":0,\"ReturnedAmount\":0,\"RefundAmount\":0,\"HoldAmount\":0,\"ReleasedAmount\":0,\"BillingFeesAmount\":0,\"ThirdPartyPaidAmount\":0,\"AdjustmentsAmount\":0,\"NetFundedAmount\":1080.44}}]}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Summary\": {\n"
                + "    \"pageidentifier\": null,\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 54049.71,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 3,\n"
                + "    \"totalRecords\": 3\n"
                + "  },\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"IdBatch\": 1049,\n"
                + "      \"BatchNumber\": \"batch_2857_combined_08-26-2025_001\",\n"
                + "      \"TransferIdentifier\": null,\n"
                + "      \"EventsData\": [\n"
                + "        {\n"
                + "          \"description\": \"Created\",\n"
                + "          \"eventTime\": \"2025-08-25T03:19:27.6190027-04:00\",\n"
                + "          \"refData\": null,\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"api\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"ConnectorName\": \"GP\",\n"
                + "      \"BatchDate\": \"2025-08-25T20:00:00\",\n"
                + "      \"BatchAmount\": 0,\n"
                + "      \"BatchFeesAmount\": 0,\n"
                + "      \"BatchAuthAmount\": 0,\n"
                + "      \"BatchReleasedAmount\": 0,\n"
                + "      \"BatchHoldAmount\": 0,\n"
                + "      \"BatchReturnedAmount\": 0,\n"
                + "      \"BatchRefundAmount\": 0,\n"
                + "      \"BatchSplitAmount\": 0,\n"
                + "      \"BatchStatus\": 2,\n"
                + "      \"BatchRecords\": 2,\n"
                + "      \"PaypointId\": 187,\n"
                + "      \"PaypointName\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointDba\": \"Gruzya Adventure Outfitters\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 105,\n"
                + "      \"externalPaypointID\": null,\n"
                + "      \"EntryName\": \"47f4f8c7e1\",\n"
                + "      \"BankName\": null,\n"
                + "      \"BatchType\": 0,\n"
                + "      \"Method\": \"combined\",\n"
                + "      \"ExpectedDepositDate\": \"2025-08-26T00:00:00Z\",\n"
                + "      \"DepositDate\": null,\n"
                + "      \"TransferDate\": \"2025-08-26T00:00:00Z\",\n"
                + "      \"Transfer\": null\n"
                + "    },\n"
                + "    {\n"
                + "      \"IdBatch\": 1043,\n"
                + "      \"BatchNumber\": \"BT-2023041817-187\",\n"
                + "      \"TransferIdentifier\": null,\n"
                + "      \"EventsData\": null,\n"
                + "      \"ConnectorName\": null,\n"
                + "      \"BatchDate\": \"2023-04-18T17:01:03Z\",\n"
                + "      \"BatchAmount\": 219.02,\n"
                + "      \"BatchFeesAmount\": 0,\n"
                + "      \"BatchAuthAmount\": 0,\n"
                + "      \"BatchReleasedAmount\": 0,\n"
                + "      \"BatchHoldAmount\": 0,\n"
                + "      \"BatchReturnedAmount\": 0,\n"
                + "      \"BatchRefundAmount\": 0,\n"
                + "      \"BatchSplitAmount\": 0,\n"
                + "      \"BatchStatus\": 2,\n"
                + "      \"BatchRecords\": 1,\n"
                + "      \"PaypointId\": 187,\n"
                + "      \"PaypointName\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointDba\": \"Gruzya Adventure Outfitters\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 105,\n"
                + "      \"externalPaypointID\": null,\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"BankName\": null,\n"
                + "      \"BatchType\": 0,\n"
                + "      \"Method\": \"card\",\n"
                + "      \"ExpectedDepositDate\": \"2023-04-19T00:00:00Z\",\n"
                + "      \"DepositDate\": null,\n"
                + "      \"TransferDate\": \"2025-09-02T00:00:00Z\",\n"
                + "      \"Transfer\": null\n"
                + "    },\n"
                + "    {\n"
                + "      \"IdBatch\": 1012,\n"
                + "      \"BatchNumber\": \"BT-2023041421-187\",\n"
                + "      \"TransferIdentifier\": \"ec310c3d-d4bf-4670-9524-00fcc4ab6a2a\",\n"
                + "      \"EventsData\": [\n"
                + "        {\n"
                + "          \"description\": \"Created\",\n"
                + "          \"eventTime\": \"2023-04-14T21:01:03Z\",\n"
                + "          \"refData\": null,\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"api\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"description\": \"Closed\",\n"
                + "          \"eventTime\": \"2023-04-15T03:05:10Z\",\n"
                + "          \"refData\": \"batchId: 1012\",\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"worker\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"ConnectorName\": \"GP\",\n"
                + "      \"BatchDate\": \"2023-04-14T21:01:03Z\",\n"
                + "      \"BatchAmount\": 1080.44,\n"
                + "      \"BatchFeesAmount\": 0,\n"
                + "      \"BatchAuthAmount\": 1080.44,\n"
                + "      \"BatchReleasedAmount\": 0,\n"
                + "      \"BatchHoldAmount\": 0,\n"
                + "      \"BatchReturnedAmount\": 0,\n"
                + "      \"BatchRefundAmount\": 0,\n"
                + "      \"BatchSplitAmount\": 0,\n"
                + "      \"BatchStatus\": 2,\n"
                + "      \"BatchRecords\": 4,\n"
                + "      \"PaypointId\": 187,\n"
                + "      \"PaypointName\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointDba\": \"Gruzya Adventure Outfitters\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"ParentOrgId\": 105,\n"
                + "      \"externalPaypointID\": null,\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"BankName\": null,\n"
                + "      \"BatchType\": 0,\n"
                + "      \"Method\": \"card\",\n"
                + "      \"ExpectedDepositDate\": \"2023-04-15T00:00:00Z\",\n"
                + "      \"DepositDate\": null,\n"
                + "      \"TransferDate\": \"2025-09-02T00:00:00Z\",\n"
                + "      \"Transfer\": {\n"
                + "        \"TransferId\": 5998,\n"
                + "        \"TransferDate\": \"2025-09-02T00:00:00Z\",\n"
                + "        \"Processor\": \"gp\",\n"
                + "        \"TransferStatus\": 1,\n"
                + "        \"GrossAmount\": 1080.44,\n"
                + "        \"ChargeBackAmount\": 0,\n"
                + "        \"ReturnedAmount\": 0,\n"
                + "        \"RefundAmount\": 0,\n"
                + "        \"HoldAmount\": 0,\n"
                + "        \"ReleasedAmount\": 0,\n"
                + "        \"BillingFeesAmount\": 0,\n"
                + "        \"ThirdPartyPaidAmount\": 0,\n"
                + "        \"AdjustmentsAmount\": 0,\n"
                + "        \"NetFundedAmount\": 1080.44\n"
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
    public void testListBatchesOut() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"AchAmount\":0,\"AchRecords\":0,\"AchStatus\":0,\"AchStatusText\":\"AchStatusText\",\"BatchAmount\":4,\"BatchCancelledAmount\":0,\"BatchCancelledRecords\":0,\"BatchDate\":\"2024-01-01T00:00:00Z\",\"BatchNumber\":\"10-20240101-PAYABLITST\",\"BatchPaidAmount\":0,\"BatchPaidRecords\":0,\"BatchProcessedAmount\":0,\"BatchProcessedRecords\":0,\"BatchProcessingAmount\":0,\"BatchProcessingRecords\":0,\"BatchRecords\":1,\"BatchStatus\":1,\"BatchStatusText\":\"Waiting Funds\",\"CardAmount\":0,\"CardRecords\":0,\"CardStatus\":0,\"CardStatusText\":\"CardStatusText\",\"CheckAmount\":0,\"CheckRecords\":0,\"CheckStatus\":0,\"CheckStatusText\":\"CheckStatusText\",\"EntryName\":\"d193cf9a46\",\"externalPaypointID\":\"Paypoint-100\",\"IdBatch\":239,\"ParentOrgName\":\"FitnessManager\",\"PaypointDba\":\"Athlete Factory LLC\",\"PaypointId\":123,\"PaypointName\":\"Athlete Factory LLC\",\"VcardAmount\":0,\"VcardRecords\":0,\"VcardStatus\":0,\"VcardStatusText\":\"VcardStatusText\",\"WireAmount\":0,\"WireRecords\":0,\"WireStatus\":0,\"WireStatusText\":\"WireStatusText\"}],\"Summary\":{\"pageidentifier\":\"null\",\"pageSize\":20,\"totalAmount\":0.01,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":46}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AchAmount\": 0,\n"
                + "      \"AchRecords\": 0,\n"
                + "      \"AchStatus\": 0,\n"
                + "      \"AchStatusText\": \"AchStatusText\",\n"
                + "      \"BatchAmount\": 4,\n"
                + "      \"BatchCancelledAmount\": 0,\n"
                + "      \"BatchCancelledRecords\": 0,\n"
                + "      \"BatchDate\": \"2024-01-01T00:00:00Z\",\n"
                + "      \"BatchNumber\": \"10-20240101-PAYABLITST\",\n"
                + "      \"BatchPaidAmount\": 0,\n"
                + "      \"BatchPaidRecords\": 0,\n"
                + "      \"BatchProcessedAmount\": 0,\n"
                + "      \"BatchProcessedRecords\": 0,\n"
                + "      \"BatchProcessingAmount\": 0,\n"
                + "      \"BatchProcessingRecords\": 0,\n"
                + "      \"BatchRecords\": 1,\n"
                + "      \"BatchStatus\": 1,\n"
                + "      \"BatchStatusText\": \"Waiting Funds\",\n"
                + "      \"CardAmount\": 0,\n"
                + "      \"CardRecords\": 0,\n"
                + "      \"CardStatus\": 0,\n"
                + "      \"CardStatusText\": \"CardStatusText\",\n"
                + "      \"CheckAmount\": 0,\n"
                + "      \"CheckRecords\": 0,\n"
                + "      \"CheckStatus\": 0,\n"
                + "      \"CheckStatusText\": \"CheckStatusText\",\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"IdBatch\": 239,\n"
                + "      \"ParentOrgName\": \"FitnessManager\",\n"
                + "      \"PaypointDba\": \"Athlete Factory LLC\",\n"
                + "      \"PaypointId\": 123,\n"
                + "      \"PaypointName\": \"Athlete Factory LLC\",\n"
                + "      \"VcardAmount\": 0,\n"
                + "      \"VcardRecords\": 0,\n"
                + "      \"VcardStatus\": 0,\n"
                + "      \"VcardStatusText\": \"VcardStatusText\",\n"
                + "      \"WireAmount\": 0,\n"
                + "      \"WireRecords\": 0,\n"
                + "      \"WireStatus\": 0,\n"
                + "      \"WireStatusText\": \"WireStatusText\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageidentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0.01,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 46\n"
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
    public void testListBatchesOutOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"AchAmount\":0,\"AchRecords\":0,\"AchStatus\":0,\"AchStatusText\":\"AchStatusText\",\"BatchAmount\":4,\"BatchCancelledAmount\":0,\"BatchCancelledRecords\":0,\"BatchDate\":\"2024-01-01T00:00:00Z\",\"BatchNumber\":\"10-20240101-PAYABLITST\",\"BatchPaidAmount\":0,\"BatchPaidRecords\":0,\"BatchProcessedAmount\":0,\"BatchProcessedRecords\":0,\"BatchProcessingAmount\":0,\"BatchProcessingRecords\":0,\"BatchRecords\":1,\"BatchStatus\":1,\"BatchStatusText\":\"Waiting Funds\",\"CardAmount\":0,\"CardRecords\":0,\"CardStatus\":0,\"CardStatusText\":\"CardStatusText\",\"CheckAmount\":0,\"CheckRecords\":0,\"CheckStatus\":0,\"CheckStatusText\":\"CheckStatusText\",\"EntryName\":\"d193cf9a46\",\"externalPaypointID\":\"Paypoint-100\",\"IdBatch\":239,\"ParentOrgName\":\"FitnessManager\",\"PaypointDba\":\"Athlete Factory LLC\",\"PaypointId\":123,\"PaypointName\":\"Athlete Factory LLC\",\"VcardAmount\":0,\"VcardRecords\":0,\"VcardStatus\":0,\"VcardStatusText\":\"VcardStatusText\",\"WireAmount\":0,\"WireRecords\":0,\"WireStatus\":0,\"WireStatusText\":\"WireStatusText\"}],\"Summary\":{\"pageidentifier\":\"null\",\"pageSize\":20,\"totalAmount\":0.01,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":46}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AchAmount\": 0,\n"
                + "      \"AchRecords\": 0,\n"
                + "      \"AchStatus\": 0,\n"
                + "      \"AchStatusText\": \"AchStatusText\",\n"
                + "      \"BatchAmount\": 4,\n"
                + "      \"BatchCancelledAmount\": 0,\n"
                + "      \"BatchCancelledRecords\": 0,\n"
                + "      \"BatchDate\": \"2024-01-01T00:00:00Z\",\n"
                + "      \"BatchNumber\": \"10-20240101-PAYABLITST\",\n"
                + "      \"BatchPaidAmount\": 0,\n"
                + "      \"BatchPaidRecords\": 0,\n"
                + "      \"BatchProcessedAmount\": 0,\n"
                + "      \"BatchProcessedRecords\": 0,\n"
                + "      \"BatchProcessingAmount\": 0,\n"
                + "      \"BatchProcessingRecords\": 0,\n"
                + "      \"BatchRecords\": 1,\n"
                + "      \"BatchStatus\": 1,\n"
                + "      \"BatchStatusText\": \"Waiting Funds\",\n"
                + "      \"CardAmount\": 0,\n"
                + "      \"CardRecords\": 0,\n"
                + "      \"CardStatus\": 0,\n"
                + "      \"CardStatusText\": \"CardStatusText\",\n"
                + "      \"CheckAmount\": 0,\n"
                + "      \"CheckRecords\": 0,\n"
                + "      \"CheckStatus\": 0,\n"
                + "      \"CheckStatusText\": \"CheckStatusText\",\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"IdBatch\": 239,\n"
                + "      \"ParentOrgName\": \"FitnessManager\",\n"
                + "      \"PaypointDba\": \"Athlete Factory LLC\",\n"
                + "      \"PaypointId\": 123,\n"
                + "      \"PaypointName\": \"Athlete Factory LLC\",\n"
                + "      \"VcardAmount\": 0,\n"
                + "      \"VcardRecords\": 0,\n"
                + "      \"VcardStatus\": 0,\n"
                + "      \"VcardStatusText\": \"VcardStatusText\",\n"
                + "      \"WireAmount\": 0,\n"
                + "      \"WireRecords\": 0,\n"
                + "      \"WireStatus\": 0,\n"
                + "      \"WireStatusText\": \"WireStatusText\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageidentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0.01,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 46\n"
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
    public void testListChargebacks() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"AccountType\":\"4XXXXXX0003\",\"CaseNumber\":\"00001\",\"ChargebackDate\":\"2023-02-08T00:00:00Z\",\"CreatedAt\":\"2023-02-20T15:36:49Z\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"321 Big Sky Road\",\"BillingAddress2\":\"\",\"BillingCity\":\"Helena\",\"BillingCountry\":\"US\",\"BillingEmail\":\"janis.berzins@example.com\",\"BillingPhone\":\"406-555-0123\",\"BillingState\":\"MT\",\"BillingZip\":\"59601\",\"CompanyName\":\"Big Sky Imports\",\"customerId\":1324,\"CustomerNumber\":\"12345\",\"customerStatus\":1,\"FirstName\":\"Janis\",\"Identifiers\":[\"firstname\",\"email\"],\"LastName\":\"Berzins\",\"ShippingAddress1\":\"321 Big Sky Road\",\"ShippingAddress2\":\"\",\"ShippingCity\":\"Helena\",\"ShippingCountry\":\"US\",\"ShippingState\":\"MT\",\"ShippingZip\":\"59601\"},\"externalPaypointID\":\"f743aed24a-10\",\"Id\":578,\"LastFour\":\"4XXXXXX0003\",\"Method\":\"card\",\"NetAmount\":1.5,\"OrderId\":\"\",\"ParentOrgName\":\"Par\",\"PaymentData\":{\"AccountExp\":\"0330\",\"AccountType\":\"visa\",\"HolderName\":\"Janis Berzins\",\"MaskedAccount\":\"4XXXXXX0003\",\"paymentDetails\":{\"totalAmount\":100},\"StoredId\":\"stored-id-123\"},\"PaymentTransId\":\"10-bfcd5a17861d4a8690ca53c00000X\",\"PaypointDbaname\":\"Global Factory LLC\",\"PaypointEntryname\":\"f743aed24a\",\"PaypointLegalname\":\"Global Factory LLC\",\"Reason\":\"Testing\",\"ReasonCode\":\"00001\",\"ReferenceNumber\":\"10-bfcd5a17861d4a8690ca53c00000X\",\"ReplyBy\":\"2023-03-02T15:36:49Z\",\"ScheduleReference\":0,\"Status\":3,\"Transaction\":{\"BatchAmount\":0,\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"321 Big Sky Road\",\"BillingAddress2\":\"\",\"BillingCity\":\"Helena\",\"BillingCountry\":\"US\",\"BillingEmail\":\"janis.berzins@example.com\",\"BillingPhone\":\"406-555-0123\",\"BillingState\":\"MT\",\"BillingZip\":\"59601\",\"CompanyName\":\"Big Sky Imports\",\"customerId\":1324,\"CustomerNumber\":\"12345\",\"customerStatus\":1,\"FirstName\":\"Janis\",\"Identifiers\":[\"firstname\",\"email\"],\"LastName\":\"Berzins\",\"ShippingAddress1\":\"321 Big Sky Road\",\"ShippingAddress2\":\"\",\"ShippingCity\":\"Helena\",\"ShippingCountry\":\"US\",\"ShippingState\":\"MT\",\"ShippingZip\":\"59601\"},\"EntrypageId\":0,\"FeeAmount\":0.06,\"GatewayTransId\":\"8082800000\",\"Method\":\"card\",\"NetAmount\":1.5,\"Operation\":\"Sale\",\"OrderId\":\"\",\"OrgId\":0,\"PaymentData\":{\"AccountExp\":\"0330\",\"AccountType\":\"visa\",\"HolderName\":\"Lisandra Sosa\",\"MaskedAccount\":\"4XXXXXX0003\",\"paymentDetails\":{\"currency\":\"USD\",\"serviceFee\":0.06,\"totalAmount\":1.56}},\"PaymentTransId\":\"10-bfcd5a17861d4a8690ca53c00000X\",\"PayorId\":0,\"PaypointId\":0,\"RefundId\":0,\"ResponseData\":{\"authcode\":\"123456\",\"avsresponse\":\"N\",\"avsresponse_text\":\"No address or ZIP match only\",\"cvvresponse\":\"N\",\"cvvresponse_text\":\"CVV2/CVC2 no match\",\"orderid\":\"10-bfcd5a17861d4a8690ca53c00000X\",\"response_code\":\"100\",\"response_code_text\":\"Transaction was approved.\",\"responsetext\":\"SUCCESS\",\"transactionid\":\"8082800000\"},\"ReturnedId\":0,\"ScheduleReference\":0,\"SettlementStatus\":0,\"Source\":\"api\",\"TotalAmount\":1.56,\"TransactionEvents\":[{\"EventTime\":\"2023-02-20T15:36:47Z\",\"TransEvent\":\"created\"},{\"EventData\":\"response=1&responsetext=SUCCESS&authcode=123456&transactionid=8082800000&avsresponse=N&cvvresponse=N&orderid=10-bfcd5a17861d4a8690ca53c00000X&type=sale&response_code=100\",\"EventTime\":\"2023-02-20T15:36:49Z\",\"TransEvent\":\"Approved\"}],\"TransactionTime\":\"2023-02-20T15:36:47Z\",\"TransStatus\":1},\"TransactionTime\":\"2023-02-20T15:36:47Z\"}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":1.56,\"totalNetAmount\":1.5,\"totalPages\":1,\"totalRecords\":1}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AccountType\": \"4XXXXXX0003\",\n"
                + "      \"CaseNumber\": \"00001\",\n"
                + "      \"ChargebackDate\": \"2023-02-08T00:00:00Z\",\n"
                + "      \"CreatedAt\": \"2023-02-20T15:36:49Z\",\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": \"AdditionalData\",\n"
                + "        \"BillingAddress1\": \"321 Big Sky Road\",\n"
                + "        \"BillingAddress2\": \"\",\n"
                + "        \"BillingCity\": \"Helena\",\n"
                + "        \"BillingCountry\": \"US\",\n"
                + "        \"BillingEmail\": \"janis.berzins@example.com\",\n"
                + "        \"BillingPhone\": \"406-555-0123\",\n"
                + "        \"BillingState\": \"MT\",\n"
                + "        \"BillingZip\": \"59601\",\n"
                + "        \"CompanyName\": \"Big Sky Imports\",\n"
                + "        \"customerId\": 1324,\n"
                + "        \"CustomerNumber\": \"12345\",\n"
                + "        \"customerStatus\": 1,\n"
                + "        \"FirstName\": \"Janis\",\n"
                + "        \"Identifiers\": [\n"
                + "          \"firstname\",\n"
                + "          \"email\"\n"
                + "        ],\n"
                + "        \"LastName\": \"Berzins\",\n"
                + "        \"ShippingAddress1\": \"321 Big Sky Road\",\n"
                + "        \"ShippingAddress2\": \"\",\n"
                + "        \"ShippingCity\": \"Helena\",\n"
                + "        \"ShippingCountry\": \"US\",\n"
                + "        \"ShippingState\": \"MT\",\n"
                + "        \"ShippingZip\": \"59601\"\n"
                + "      },\n"
                + "      \"externalPaypointID\": \"f743aed24a-10\",\n"
                + "      \"Id\": 578,\n"
                + "      \"LastFour\": \"4XXXXXX0003\",\n"
                + "      \"Method\": \"card\",\n"
                + "      \"NetAmount\": 1.5,\n"
                + "      \"OrderId\": \"\",\n"
                + "      \"ParentOrgName\": \"Par\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"AccountExp\": \"0330\",\n"
                + "        \"AccountType\": \"visa\",\n"
                + "        \"HolderName\": \"Janis Berzins\",\n"
                + "        \"MaskedAccount\": \"4XXXXXX0003\",\n"
                + "        \"paymentDetails\": {\n"
                + "          \"totalAmount\": 100\n"
                + "        },\n"
                + "        \"StoredId\": \"stored-id-123\"\n"
                + "      },\n"
                + "      \"PaymentTransId\": \"10-bfcd5a17861d4a8690ca53c00000X\",\n"
                + "      \"PaypointDbaname\": \"Global Factory LLC\",\n"
                + "      \"PaypointEntryname\": \"f743aed24a\",\n"
                + "      \"PaypointLegalname\": \"Global Factory LLC\",\n"
                + "      \"Reason\": \"Testing\",\n"
                + "      \"ReasonCode\": \"00001\",\n"
                + "      \"ReferenceNumber\": \"10-bfcd5a17861d4a8690ca53c00000X\",\n"
                + "      \"ReplyBy\": \"2023-03-02T15:36:49Z\",\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"Status\": 3,\n"
                + "      \"Transaction\": {\n"
                + "        \"BatchAmount\": 0,\n"
                + "        \"Customer\": {\n"
                + "          \"AdditionalData\": \"AdditionalData\",\n"
                + "          \"BillingAddress1\": \"321 Big Sky Road\",\n"
                + "          \"BillingAddress2\": \"\",\n"
                + "          \"BillingCity\": \"Helena\",\n"
                + "          \"BillingCountry\": \"US\",\n"
                + "          \"BillingEmail\": \"janis.berzins@example.com\",\n"
                + "          \"BillingPhone\": \"406-555-0123\",\n"
                + "          \"BillingState\": \"MT\",\n"
                + "          \"BillingZip\": \"59601\",\n"
                + "          \"CompanyName\": \"Big Sky Imports\",\n"
                + "          \"customerId\": 1324,\n"
                + "          \"CustomerNumber\": \"12345\",\n"
                + "          \"customerStatus\": 1,\n"
                + "          \"FirstName\": \"Janis\",\n"
                + "          \"Identifiers\": [\n"
                + "            \"firstname\",\n"
                + "            \"email\"\n"
                + "          ],\n"
                + "          \"LastName\": \"Berzins\",\n"
                + "          \"ShippingAddress1\": \"321 Big Sky Road\",\n"
                + "          \"ShippingAddress2\": \"\",\n"
                + "          \"ShippingCity\": \"Helena\",\n"
                + "          \"ShippingCountry\": \"US\",\n"
                + "          \"ShippingState\": \"MT\",\n"
                + "          \"ShippingZip\": \"59601\"\n"
                + "        },\n"
                + "        \"EntrypageId\": 0,\n"
                + "        \"FeeAmount\": 0.06,\n"
                + "        \"GatewayTransId\": \"8082800000\",\n"
                + "        \"Method\": \"card\",\n"
                + "        \"NetAmount\": 1.5,\n"
                + "        \"Operation\": \"Sale\",\n"
                + "        \"OrderId\": \"\",\n"
                + "        \"OrgId\": 0,\n"
                + "        \"PaymentData\": {\n"
                + "          \"AccountExp\": \"0330\",\n"
                + "          \"AccountType\": \"visa\",\n"
                + "          \"HolderName\": \"Lisandra Sosa\",\n"
                + "          \"MaskedAccount\": \"4XXXXXX0003\",\n"
                + "          \"paymentDetails\": {\n"
                + "            \"currency\": \"USD\",\n"
                + "            \"serviceFee\": 0.06,\n"
                + "            \"totalAmount\": 1.56\n"
                + "          }\n"
                + "        },\n"
                + "        \"PaymentTransId\": \"10-bfcd5a17861d4a8690ca53c00000X\",\n"
                + "        \"PayorId\": 0,\n"
                + "        \"PaypointId\": 0,\n"
                + "        \"RefundId\": 0,\n"
                + "        \"ResponseData\": {\n"
                + "          \"authcode\": \"123456\",\n"
                + "          \"avsresponse\": \"N\",\n"
                + "          \"avsresponse_text\": \"No address or ZIP match only\",\n"
                + "          \"cvvresponse\": \"N\",\n"
                + "          \"cvvresponse_text\": \"CVV2/CVC2 no match\",\n"
                + "          \"orderid\": \"10-bfcd5a17861d4a8690ca53c00000X\",\n"
                + "          \"response_code\": \"100\",\n"
                + "          \"response_code_text\": \"Transaction was approved.\",\n"
                + "          \"responsetext\": \"SUCCESS\",\n"
                + "          \"transactionid\": \"8082800000\"\n"
                + "        },\n"
                + "        \"ReturnedId\": 0,\n"
                + "        \"ScheduleReference\": 0,\n"
                + "        \"SettlementStatus\": 0,\n"
                + "        \"Source\": \"api\",\n"
                + "        \"TotalAmount\": 1.56,\n"
                + "        \"TransactionEvents\": [\n"
                + "          {\n"
                + "            \"EventTime\": \"2023-02-20T15:36:47Z\",\n"
                + "            \"TransEvent\": \"created\"\n"
                + "          },\n"
                + "          {\n"
                + "            \"EventData\": \"response=1&responsetext=SUCCESS&authcode=123456&transactionid=8082800000&avsresponse=N&cvvresponse=N&orderid=10-bfcd5a17861d4a8690ca53c00000X&type=sale&response_code=100\",\n"
                + "            \"EventTime\": \"2023-02-20T15:36:49Z\",\n"
                + "            \"TransEvent\": \"Approved\"\n"
                + "          }\n"
                + "        ],\n"
                + "        \"TransactionTime\": \"2023-02-20T15:36:47Z\",\n"
                + "        \"TransStatus\": 1\n"
                + "      },\n"
                + "      \"TransactionTime\": \"2023-02-20T15:36:47Z\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 1.56,\n"
                + "    \"totalNetAmount\": 1.5,\n"
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
    public void testListChargebacksOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"AccountType\":\"4XXXXXX0003\",\"CaseNumber\":\"00001\",\"ChargebackDate\":\"2023-02-08T00:00:00Z\",\"CreatedAt\":\"2022-07-01T15:00:01Z\",\"externalPaypointID\":\"Paypoint-100\",\"Id\":578,\"LastFour\":\"4XXXXXX0003\",\"Method\":\"card\",\"NetAmount\":1.5,\"OrderId\":\"O-5140\",\"pageidentifier\":\"null\",\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"paymentDetails\":{\"totalAmount\":100}},\"PaymentTransId\":\"10-bfcd5a17861d4a8690ca53c142ca3810\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"PaypointLegalname\":\"Sunshine Services, LLC\",\"Reason\":\"Testing\",\"ReasonCode\":\"00001\",\"ReferenceNumber\":\"10-bfcd5a17861d4a8690ca53c142ca3810\",\"ReplyBy\":\"2022-07-11T15:00:01Z\",\"Responses\":\"Responses\",\"ScheduleReference\":0,\"Status\":3,\"Transaction\":{\"EntrypageId\":0,\"FeeAmount\":1,\"PayorId\":1551,\"PaypointId\":226,\"SettlementStatus\":2,\"TotalAmount\":30.22,\"TransStatus\":1},\"TransactionTime\":\"2024-01-15T09:30:00Z\"}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AccountType\": \"4XXXXXX0003\",\n"
                + "      \"CaseNumber\": \"00001\",\n"
                + "      \"ChargebackDate\": \"2023-02-08T00:00:00Z\",\n"
                + "      \"CreatedAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"Id\": 578,\n"
                + "      \"LastFour\": \"4XXXXXX0003\",\n"
                + "      \"Method\": \"card\",\n"
                + "      \"NetAmount\": 1.5,\n"
                + "      \"OrderId\": \"O-5140\",\n"
                + "      \"pageidentifier\": \"null\",\n"
                + "      \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"paymentDetails\": {\n"
                + "          \"totalAmount\": 100\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaymentTransId\": \"10-bfcd5a17861d4a8690ca53c142ca3810\",\n"
                + "      \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "      \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "      \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "      \"Reason\": \"Testing\",\n"
                + "      \"ReasonCode\": \"00001\",\n"
                + "      \"ReferenceNumber\": \"10-bfcd5a17861d4a8690ca53c142ca3810\",\n"
                + "      \"ReplyBy\": \"2022-07-11T15:00:01Z\",\n"
                + "      \"Responses\": \"Responses\",\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"Status\": 3,\n"
                + "      \"Transaction\": {\n"
                + "        \"EntrypageId\": 0,\n"
                + "        \"FeeAmount\": 1,\n"
                + "        \"PayorId\": 1551,\n"
                + "        \"PaypointId\": 226,\n"
                + "        \"SettlementStatus\": 2,\n"
                + "        \"TotalAmount\": 30.22,\n"
                + "        \"TransStatus\": 1\n"
                + "      },\n"
                + "      \"TransactionTime\": \"2024-01-15T09:30:00Z\"\n"
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
    public void testListCustomers() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"Address\":\"1234 Bayou Road\",\"Address1\":\"Suite 2\",\"Balance\":0,\"City\":\"Lafayette\",\"Company\":\"Boudreaux's Shop\",\"Country\":\"US\",\"Created\":\"2023-12-20T13:07:48Z\",\"customerId\":2876,\"customerNumber\":\"425436530000\",\"customerStatus\":0,\"customerSummary\":{\"numberofTransactions\":30,\"totalAmountTransactions\":1500,\"totalNetAmountTransactions\":1500},\"Email\":\"thibodeaux.hebert@bayoumail.com\",\"externalPaypointID\":\"pay-10\",\"Firstname\":\"Thibodeaux\",\"IdentifierFields\":[\"email\"],\"Lastname\":\"Hebert\",\"LastUpdated\":\"2023-12-20T13:07:48Z\",\"MFA\":false,\"MFAMode\":0,\"ParentOrgName\":\"SupplyPro\",\"PaypointDbaname\":\"Global Factory LLC\",\"PaypointEntryname\":\"4872acb376a\",\"PaypointLegalname\":\"Global Factory LLC\",\"Phone\":\"(504) 823-4566\",\"ShippingAddress\":\"1234 Bayou Road\",\"ShippingAddress1\":\"Suite 2\",\"ShippingCity\":\"Lafayette\",\"ShippingCountry\":\"US\",\"ShippingState\":\"LA\",\"ShippingZip\":\"70501\",\"State\":\"LA\",\"TimeZone\":0,\"Zip\":\"70501\"}],\"Summary\":{\"pageIdentifier\":\"XXXXXXXXXXXXXX\",\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":26,\"totalRecords\":510}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"Address\": \"1234 Bayou Road\",\n"
                + "      \"Address1\": \"Suite 2\",\n"
                + "      \"Balance\": 0,\n"
                + "      \"City\": \"Lafayette\",\n"
                + "      \"Company\": \"Boudreaux's Shop\",\n"
                + "      \"Country\": \"US\",\n"
                + "      \"Created\": \"2023-12-20T13:07:48Z\",\n"
                + "      \"customerId\": 2876,\n"
                + "      \"customerNumber\": \"425436530000\",\n"
                + "      \"customerStatus\": 0,\n"
                + "      \"customerSummary\": {\n"
                + "        \"numberofTransactions\": 30,\n"
                + "        \"totalAmountTransactions\": 1500,\n"
                + "        \"totalNetAmountTransactions\": 1500\n"
                + "      },\n"
                + "      \"Email\": \"thibodeaux.hebert@bayoumail.com\",\n"
                + "      \"externalPaypointID\": \"pay-10\",\n"
                + "      \"Firstname\": \"Thibodeaux\",\n"
                + "      \"IdentifierFields\": [\n"
                + "        \"email\"\n"
                + "      ],\n"
                + "      \"Lastname\": \"Hebert\",\n"
                + "      \"LastUpdated\": \"2023-12-20T13:07:48Z\",\n"
                + "      \"MFA\": false,\n"
                + "      \"MFAMode\": 0,\n"
                + "      \"ParentOrgName\": \"SupplyPro\",\n"
                + "      \"PaypointDbaname\": \"Global Factory LLC\",\n"
                + "      \"PaypointEntryname\": \"4872acb376a\",\n"
                + "      \"PaypointLegalname\": \"Global Factory LLC\",\n"
                + "      \"Phone\": \"(504) 823-4566\",\n"
                + "      \"ShippingAddress\": \"1234 Bayou Road\",\n"
                + "      \"ShippingAddress1\": \"Suite 2\",\n"
                + "      \"ShippingCity\": \"Lafayette\",\n"
                + "      \"ShippingCountry\": \"US\",\n"
                + "      \"ShippingState\": \"LA\",\n"
                + "      \"ShippingZip\": \"70501\",\n"
                + "      \"State\": \"LA\",\n"
                + "      \"TimeZone\": 0,\n"
                + "      \"Zip\": \"70501\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"XXXXXXXXXXXXXX\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 26,\n"
                + "    \"totalRecords\": 510\n"
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
    public void testListCustomersOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"Address\":\"1234 Bayou Road\",\"Address1\":\"Suite 2\",\"Balance\":0,\"City\":\"Lafayette\",\"Company\":\"Boudreaux's Shop\",\"Country\":\"US\",\"Created\":\"2023-12-20T13:07:48Z\",\"customerId\":2876,\"customerNumber\":\"425436530000\",\"customerStatus\":0,\"customerSummary\":{\"numberofTransactions\":30,\"totalAmountTransactions\":1500,\"totalNetAmountTransactions\":1500},\"Email\":\"thibodeaux.hebert@bayoumail.com\",\"externalPaypointID\":\"pay-10\",\"Firstname\":\"Thibodeaux\",\"IdentifierFields\":[\"email\"],\"Lastname\":\"Hebert\",\"LastUpdated\":\"2023-12-20T13:07:48Z\",\"MFA\":false,\"MFAMode\":0,\"ParentOrgName\":\"SupplyPro\",\"PaypointDbaname\":\"Global Factory LLC\",\"PaypointEntryname\":\"4872acb376a\",\"PaypointLegalname\":\"Global Factory LLC\",\"Phone\":\"(504) 823-4566\",\"ShippingAddress\":\"1234 Bayou Road\",\"ShippingAddress1\":\"Suite 2\",\"ShippingCity\":\"Lafayette\",\"ShippingCountry\":\"US\",\"ShippingState\":\"LA\",\"ShippingZip\":\"70501\",\"State\":\"LA\",\"TimeZone\":0,\"Zip\":\"70501\"}],\"Summary\":{\"pageIdentifier\":\"XXXXXXXXXXXXXX\",\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":26,\"totalRecords\":510}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"Address\": \"1234 Bayou Road\",\n"
                + "      \"Address1\": \"Suite 2\",\n"
                + "      \"Balance\": 0,\n"
                + "      \"City\": \"Lafayette\",\n"
                + "      \"Company\": \"Boudreaux's Shop\",\n"
                + "      \"Country\": \"US\",\n"
                + "      \"Created\": \"2023-12-20T13:07:48Z\",\n"
                + "      \"customerId\": 2876,\n"
                + "      \"customerNumber\": \"425436530000\",\n"
                + "      \"customerStatus\": 0,\n"
                + "      \"customerSummary\": {\n"
                + "        \"numberofTransactions\": 30,\n"
                + "        \"totalAmountTransactions\": 1500,\n"
                + "        \"totalNetAmountTransactions\": 1500\n"
                + "      },\n"
                + "      \"Email\": \"thibodeaux.hebert@bayoumail.com\",\n"
                + "      \"externalPaypointID\": \"pay-10\",\n"
                + "      \"Firstname\": \"Thibodeaux\",\n"
                + "      \"IdentifierFields\": [\n"
                + "        \"email\"\n"
                + "      ],\n"
                + "      \"Lastname\": \"Hebert\",\n"
                + "      \"LastUpdated\": \"2023-12-20T13:07:48Z\",\n"
                + "      \"MFA\": false,\n"
                + "      \"MFAMode\": 0,\n"
                + "      \"ParentOrgName\": \"SupplyPro\",\n"
                + "      \"PaypointDbaname\": \"Global Factory LLC\",\n"
                + "      \"PaypointEntryname\": \"4872acb376a\",\n"
                + "      \"PaypointLegalname\": \"Global Factory LLC\",\n"
                + "      \"Phone\": \"(504) 823-4566\",\n"
                + "      \"ShippingAddress\": \"1234 Bayou Road\",\n"
                + "      \"ShippingAddress1\": \"Suite 2\",\n"
                + "      \"ShippingCity\": \"Lafayette\",\n"
                + "      \"ShippingCountry\": \"US\",\n"
                + "      \"ShippingState\": \"LA\",\n"
                + "      \"ShippingZip\": \"70501\",\n"
                + "      \"State\": \"LA\",\n"
                + "      \"TimeZone\": 0,\n"
                + "      \"Zip\": \"70501\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"XXXXXXXXXXXXXX\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 0,\n"
                + "    \"totalNetAmount\": 0,\n"
                + "    \"totalPages\": 26,\n"
                + "    \"totalRecords\": 510\n"
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
                                "{\"Records\":[{\"content\":{\"eventType\":\"ReceivedChargeBack\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88976,\"ownerId\":\"123\",\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"},{\"content\":{\"eventType\":\"ReceivedAchReturn\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88975,\"ownerId\":\"123\",\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"}],\"Summary\":{\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":2}}"));
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
                + "      \"ownerId\": \"123\",\n"
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
                + "      \"ownerId\": \"123\",\n"
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
                                "{\"Records\":[{\"content\":{\"eventType\":\"ReceivedChargeBack\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88976,\"ownerId\":\"123\",\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"},{\"content\":{\"eventType\":\"ReceivedAchReturn\",\"timeZone\":0},\"createdAt\":\"2022-06-07T05:00:00Z\",\"frequency\":\"untilcancelled\",\"lastUpdated\":\"2022-06-07T05:00:00Z\",\"method\":\"email\",\"notificationId\":88975,\"ownerId\":\"123\",\"ownerName\":\"Pilgrim Planner\",\"ownerType\":2,\"source\":\"api\",\"status\":1,\"target\":\"example@example.com\"}],\"Summary\":{\"pageSize\":20,\"totalAmount\":0,\"totalNetAmount\":0,\"totalPages\":1,\"totalRecords\":2}}"));
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
                + "      \"ownerId\": \"123\",\n"
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
                + "      \"ownerId\": \"123\",\n"
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
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"BatchNumber\":\"BT-2024321\",\"Bills\":[{}],\"CardToken\":\"CardToken\",\"CheckNumber\":\"12345\",\"Comments\":\"Deposit for materials\",\"CreatedAt\":\"2022-07-01T15:00:01Z\",\"EntryName\":\"d193cf9a46\",\"Events\":[{}],\"externalPaypointID\":\"Paypoint-100\",\"FeeAmount\":10.25,\"Gateway\":\"TSYS\",\"IdOut\":236,\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"NetAmount\":3762.87,\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"paymentDetails\":{\"totalAmount\":100}},\"PaymentId\":\"12345678910\",\"PaymentMethod\":\"ach\",\"PaymentStatus\":\"Processed\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointLegalname\":\"Sunshine Services, LLC\",\"Source\":\"api\",\"Status\":1,\"TotalAmount\":110.25,\"Vendor\":{\"additionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"CreatedDate\":\"2022-07-01T15:00:01Z\"}}],\"Summary\":{\"totalPages\":391,\"totalRecords\":7803,\"totalAmount\":21435.95,\"totalNetAmount\":21435.95,\"totalPaid\":1,\"totalPaidAmount\":4,\"totalCanceled\":1743,\"totalCanceledAmount\":4515,\"totalCaptured\":138,\"totalCapturedAmount\":542,\"totalAuthorized\":4139,\"totalAuthorizedAmount\":11712.35,\"totalProcessing\":1780,\"totalProcessingAmount\":4660.6,\"totalOpen\":2,\"totalOpenAmount\":2,\"totalOnHold\":0,\"totalOnHoldAmount\":0,\"pageSize\":20}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"BatchNumber\": \"BT-2024321\",\n"
                + "      \"Bills\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"CardToken\": \"CardToken\",\n"
                + "      \"CheckNumber\": \"12345\",\n"
                + "      \"Comments\": \"Deposit for materials\",\n"
                + "      \"CreatedAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"Events\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"FeeAmount\": 10.25,\n"
                + "      \"Gateway\": \"TSYS\",\n"
                + "      \"IdOut\": 236,\n"
                + "      \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"NetAmount\": 3762.87,\n"
                + "      \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"paymentDetails\": {\n"
                + "          \"totalAmount\": 100\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaymentId\": \"12345678910\",\n"
                + "      \"PaymentMethod\": \"ach\",\n"
                + "      \"PaymentStatus\": \"Processed\",\n"
                + "      \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "      \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "      \"Source\": \"api\",\n"
                + "      \"Status\": 1,\n"
                + "      \"TotalAmount\": 110.25,\n"
                + "      \"Vendor\": {\n"
                + "        \"additionalData\": {\n"
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
                + "        \"CreatedDate\": \"2022-07-01T15:00:01Z\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"totalPages\": 391,\n"
                + "    \"totalRecords\": 7803,\n"
                + "    \"totalAmount\": 21435.95,\n"
                + "    \"totalNetAmount\": 21435.95,\n"
                + "    \"totalPaid\": 1,\n"
                + "    \"totalPaidAmount\": 4,\n"
                + "    \"totalCanceled\": 1743,\n"
                + "    \"totalCanceledAmount\": 4515,\n"
                + "    \"totalCaptured\": 138,\n"
                + "    \"totalCapturedAmount\": 542,\n"
                + "    \"totalAuthorized\": 4139,\n"
                + "    \"totalAuthorizedAmount\": 11712.35,\n"
                + "    \"totalProcessing\": 1780,\n"
                + "    \"totalProcessingAmount\": 4660.6,\n"
                + "    \"totalOpen\": 2,\n"
                + "    \"totalOpenAmount\": 2,\n"
                + "    \"totalOnHold\": 0,\n"
                + "    \"totalOnHoldAmount\": 0,\n"
                + "    \"pageSize\": 20\n"
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
    public void testListPayoutOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"BatchNumber\":\"BT-2024321\",\"Bills\":[{}],\"CardToken\":\"CardToken\",\"CheckNumber\":\"12345\",\"Comments\":\"Deposit for materials\",\"CreatedAt\":\"2022-07-01T15:00:01Z\",\"EntryName\":\"d193cf9a46\",\"Events\":[{}],\"externalPaypointID\":\"Paypoint-100\",\"FeeAmount\":10.25,\"Gateway\":\"TSYS\",\"IdOut\":236,\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"NetAmount\":3762.87,\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"paymentDetails\":{\"totalAmount\":100}},\"PaymentId\":\"12345678910\",\"PaymentMethod\":\"ach\",\"PaymentStatus\":\"Processed\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointLegalname\":\"Sunshine Services, LLC\",\"Source\":\"api\",\"Status\":1,\"TotalAmount\":110.25,\"Vendor\":{\"additionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"CreatedDate\":\"2022-07-01T15:00:01Z\"}}],\"Summary\":{\"totalPages\":391,\"totalRecords\":7803,\"totalAmount\":21435.95,\"totalNetAmount\":21435.95,\"totalPaid\":1,\"totalPaidAmount\":4,\"totalCanceled\":1743,\"totalCanceledAmount\":4515,\"totalCaptured\":138,\"totalCapturedAmount\":542,\"totalAuthorized\":4139,\"totalAuthorizedAmount\":11712.35,\"totalProcessing\":1780,\"totalProcessingAmount\":4660.6,\"totalOpen\":2,\"totalOpenAmount\":2,\"totalOnHold\":0,\"totalOnHoldAmount\":0,\"pageSize\":20}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"BatchNumber\": \"BT-2024321\",\n"
                + "      \"Bills\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"CardToken\": \"CardToken\",\n"
                + "      \"CheckNumber\": \"12345\",\n"
                + "      \"Comments\": \"Deposit for materials\",\n"
                + "      \"CreatedAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"EntryName\": \"d193cf9a46\",\n"
                + "      \"Events\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"FeeAmount\": 10.25,\n"
                + "      \"Gateway\": \"TSYS\",\n"
                + "      \"IdOut\": 236,\n"
                + "      \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"NetAmount\": 3762.87,\n"
                + "      \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"paymentDetails\": {\n"
                + "          \"totalAmount\": 100\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaymentId\": \"12345678910\",\n"
                + "      \"PaymentMethod\": \"ach\",\n"
                + "      \"PaymentStatus\": \"Processed\",\n"
                + "      \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "      \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "      \"Source\": \"api\",\n"
                + "      \"Status\": 1,\n"
                + "      \"TotalAmount\": 110.25,\n"
                + "      \"Vendor\": {\n"
                + "        \"additionalData\": {\n"
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
                + "        \"CreatedDate\": \"2022-07-01T15:00:01Z\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"totalPages\": 391,\n"
                + "    \"totalRecords\": 7803,\n"
                + "    \"totalAmount\": 21435.95,\n"
                + "    \"totalNetAmount\": 21435.95,\n"
                + "    \"totalPaid\": 1,\n"
                + "    \"totalPaidAmount\": 4,\n"
                + "    \"totalCanceled\": 1743,\n"
                + "    \"totalCanceledAmount\": 4515,\n"
                + "    \"totalCaptured\": 138,\n"
                + "    \"totalCapturedAmount\": 542,\n"
                + "    \"totalAuthorized\": 4139,\n"
                + "    \"totalAuthorizedAmount\": 11712.35,\n"
                + "    \"totalProcessing\": 1780,\n"
                + "    \"totalProcessingAmount\": 4660.6,\n"
                + "    \"totalOpen\": 2,\n"
                + "    \"totalOpenAmount\": 2,\n"
                + "    \"totalOnHold\": 0,\n"
                + "    \"totalOnHoldAmount\": 0,\n"
                + "    \"pageSize\": 20\n"
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
    public void testListPaypoints() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"AverageMonthlyVolume\":1000,\"AverageTicketAmount\":1000,\"BAddress1\":\"123 Walnut Street\",\"BAddress2\":\"Suite 103\",\"BankData\":[{\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"nickname\":\"Business Checking 1234\"}],\"BCity\":\"New Vegas\",\"BCountry\":\"US\",\"BFax\":\"5551234567\",\"BinPerson\":60,\"BinPhone\":20,\"BinWeb\":20,\"BoardingId\":340,\"BPhone\":\"5551234567\",\"BStartdate\":\"01/01/1990\",\"BState\":\"FL\",\"BSummary\":\"Brick and mortar store that sells office supplies\",\"BTimeZone\":-5,\"BZip\":\"33000\",\"ContactData\":[{}],\"CreatedAt\":\"2022-07-01T15:00:01Z\",\"DbaName\":\"Sunshine Gutters\",\"DocumentsRef\":\"DocumentsRef\",\"Ein\":\"123456789\",\"EntryPoints\":[{}],\"externalPaypointID\":\"Paypoint-100\",\"ExternalProcessorInformation\":\"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\"HighTicketAmount\":1000,\"IdPaypoint\":1000000,\"LastModified\":\"2022-07-01T15:00:01Z\",\"LegalName\":\"Sunshine Services, LLC\",\"License\":\"2222222FFG\",\"LicenseState\":\"CA\",\"MAddress1\":\"123 Walnut Street\",\"MAddress2\":\"STE 900\",\"Mccid\":\"Mccid\",\"MCity\":\"Johnson City\",\"MCountry\":\"US\",\"MState\":\"TN\",\"MZip\":\"37615\",\"OrgId\":123,\"OrgParentName\":\"PropertyManager Pro\",\"OwnerData\":[{}],\"OwnType\":\"Limited Liability Company\",\"PaypointStatus\":1,\"SalesCode\":\"SalesCode\",\"Taxfillname\":\"Sunshine LLC\",\"TemplateId\":22,\"WebsiteAddress\":\"www.example.com\",\"Whencharged\":\"When Service Provided\",\"Whendelivered\":\"0-7 Days\",\"Whenprovided\":\"30 Days or Less\",\"Whenrefund\":\"Exchange Only\"}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AverageMonthlyVolume\": 1000,\n"
                + "      \"AverageTicketAmount\": 1000,\n"
                + "      \"BAddress1\": \"123 Walnut Street\",\n"
                + "      \"BAddress2\": \"Suite 103\",\n"
                + "      \"BankData\": [\n"
                + "        {\n"
                + "          \"bankAccountFunction\": 0,\n"
                + "          \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "          \"nickname\": \"Business Checking 1234\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"BCity\": \"New Vegas\",\n"
                + "      \"BCountry\": \"US\",\n"
                + "      \"BFax\": \"5551234567\",\n"
                + "      \"BinPerson\": 60,\n"
                + "      \"BinPhone\": 20,\n"
                + "      \"BinWeb\": 20,\n"
                + "      \"BoardingId\": 340,\n"
                + "      \"BPhone\": \"5551234567\",\n"
                + "      \"BStartdate\": \"01/01/1990\",\n"
                + "      \"BState\": \"FL\",\n"
                + "      \"BSummary\": \"Brick and mortar store that sells office supplies\",\n"
                + "      \"BTimeZone\": -5,\n"
                + "      \"BZip\": \"33000\",\n"
                + "      \"ContactData\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"CreatedAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"DbaName\": \"Sunshine Gutters\",\n"
                + "      \"DocumentsRef\": \"DocumentsRef\",\n"
                + "      \"Ein\": \"123456789\",\n"
                + "      \"EntryPoints\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"ExternalProcessorInformation\": \"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\n"
                + "      \"HighTicketAmount\": 1000,\n"
                + "      \"IdPaypoint\": 1000000,\n"
                + "      \"LastModified\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"LegalName\": \"Sunshine Services, LLC\",\n"
                + "      \"License\": \"2222222FFG\",\n"
                + "      \"LicenseState\": \"CA\",\n"
                + "      \"MAddress1\": \"123 Walnut Street\",\n"
                + "      \"MAddress2\": \"STE 900\",\n"
                + "      \"Mccid\": \"Mccid\",\n"
                + "      \"MCity\": \"Johnson City\",\n"
                + "      \"MCountry\": \"US\",\n"
                + "      \"MState\": \"TN\",\n"
                + "      \"MZip\": \"37615\",\n"
                + "      \"OrgId\": 123,\n"
                + "      \"OrgParentName\": \"PropertyManager Pro\",\n"
                + "      \"OwnerData\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"OwnType\": \"Limited Liability Company\",\n"
                + "      \"PaypointStatus\": 1,\n"
                + "      \"SalesCode\": \"SalesCode\",\n"
                + "      \"Taxfillname\": \"Sunshine LLC\",\n"
                + "      \"TemplateId\": 22,\n"
                + "      \"WebsiteAddress\": \"www.example.com\",\n"
                + "      \"Whencharged\": \"When Service Provided\",\n"
                + "      \"Whendelivered\": \"0-7 Days\",\n"
                + "      \"Whenprovided\": \"30 Days or Less\",\n"
                + "      \"Whenrefund\": \"Exchange Only\"\n"
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
    public void testListSettlements() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"BatchAmount\":32,\"BatchNumber\":\"batch-100-20-2024\",\"Category\":\"auth\",\"CreatedAt\":\"2024-11-19T15:58:01Z\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"100 Golden Ridge Drive\",\"BillingAddress2\":\"STE 100\",\"BillingCity\":\"Mendota\",\"BillingCountry\":\"US\",\"BillingEmail\":\"lydia@example.com\",\"BillingPhone\":\"+12345678\",\"BillingState\":\"VA\",\"BillingZip\":\"20147\",\"customerId\":2707,\"CustomerNumber\":\"901102\",\"customerStatus\":1,\"FirstName\":\"Lydia\",\"LastName\":\"Marshall\"},\"ExpectedDepositDate\":\"2024-11-22T00:00:00Z\",\"GatewayTransId\":\"TRN_XXXXX\",\"Id\":25048,\"isHold\":0,\"MaskedAccount\":\"1XXXXXX5678\",\"Method\":\"ach\",\"NetAmount\":2,\"Operation\":\"Sale\",\"OrderId\":\"\",\"ParentOrgName\":\"Pilgrim Planner\",\"PaymentData\":{\"AccountType\":\"Checking\",\"HolderName\":\"Lydia Marshall\",\"MaskedAccount\":\"1XXXXXX5678\",\"paymentDetails\":{\"categories\":[{\"amount\":1000,\"label\":\"Deposit\"}],\"currency\":\"USD\",\"serviceFee\":0,\"splitFunding\":[{}],\"totalAmount\":2}},\"PaymentTransId\":\"245-9e4072eef77e45979ea0e49f680000X\",\"PaymentTransStatus\":1,\"PaypointDbaname\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointEntryname\":\"7f1a3816XX\",\"PaypointLegalname\":\"Gruzya Adventure Outfitters, LLC\",\"ResponseData\":{\"authcode\":\"\",\"avsresponse_text\":\"\",\"cvvresponse_text\":\"\",\"response_code\":\"100\",\"response_code_text\":\"Operation successful.\",\"responsetext\":\"CAPTURED\",\"transactionid\":\"TRN_XXXXX\"},\"ScheduleReference\":0,\"SettledAmount\":0.5,\"SettlementDate\":\"2024-11-20T00:00:00Z\",\"Source\":\"api\",\"Status\":1,\"TransactionEvents\":[{\"EventTime\":\"2024-11-19T15:57:40Z\",\"TransEvent\":\"Created\"},{\"EventData\":{\"account_id\":\"TRA_XXXXX\",\"account_name\":\"123456\",\"action\":{\"app_id\":\"XXXXX\",\"app_name\":\"PayAbli\",\"id\":\"ACT_XXXXX\",\"result_code\":\"SUCCESS\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"AUTHORIZE\"},\"amount\":\"200\",\"batch_id\":\"\",\"capture_mode\":\"AUTO\",\"channel\":\"CNP\",\"country\":\"US\",\"currency\":\"USD\",\"fees\":{\"amount\":\"0\",\"rate\":\"0.00\",\"total_amount\":\"0\"},\"id\":\"TRN_XXXXX\",\"merchant_amount\":\"200\",\"merchant_id\":\"MER_XXXXX\",\"merchant_name\":\"Henriette97\",\"order\":{\"reference\":\"\"},\"payment_method\":{\"bank_transfer\":{\"account_type\":\"CHECKING\",\"bank\":{\"name\":\"\"},\"masked_account_number_last4\":\"XXXX5678\"},\"entry_mode\":\"ECOM\",\"message\":\"Success\",\"narrative\":\"Lydia Marshall\",\"result\":\"00\"},\"reference\":\"245-XXXXX\",\"status\":\"CAPTURED\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"SALE\"},\"EventTime\":\"2024-11-19T20:58:01Z\",\"TransEvent\":\"Approved\"},{\"EventTime\":\"2024-11-20T03:05:10Z\",\"TransEvent\":\"ClosedBatch\"}],\"TransactionTime\":\"2024-11-19T15:58:01Z\",\"TransMethod\":\"ach\",\"Type\":\"credit\"}],\"Summary\":{\"heldAmount\":3.7,\"pageSize\":0,\"refunds\":-3521.85,\"serviceFees\":852.48,\"totalAmount\":61645.74,\"totalNetAmount\":61645.74,\"totalPages\":21872,\"totalRecords\":21872,\"transferAmount\":0}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"BatchAmount\": 32,\n"
                + "      \"BatchNumber\": \"batch-100-20-2024\",\n"
                + "      \"Category\": \"auth\",\n"
                + "      \"CreatedAt\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": \"AdditionalData\",\n"
                + "        \"BillingAddress1\": \"100 Golden Ridge Drive\",\n"
                + "        \"BillingAddress2\": \"STE 100\",\n"
                + "        \"BillingCity\": \"Mendota\",\n"
                + "        \"BillingCountry\": \"US\",\n"
                + "        \"BillingEmail\": \"lydia@example.com\",\n"
                + "        \"BillingPhone\": \"+12345678\",\n"
                + "        \"BillingState\": \"VA\",\n"
                + "        \"BillingZip\": \"20147\",\n"
                + "        \"customerId\": 2707,\n"
                + "        \"CustomerNumber\": \"901102\",\n"
                + "        \"customerStatus\": 1,\n"
                + "        \"FirstName\": \"Lydia\",\n"
                + "        \"LastName\": \"Marshall\"\n"
                + "      },\n"
                + "      \"ExpectedDepositDate\": \"2024-11-22T00:00:00Z\",\n"
                + "      \"GatewayTransId\": \"TRN_XXXXX\",\n"
                + "      \"Id\": 25048,\n"
                + "      \"isHold\": 0,\n"
                + "      \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "      \"Method\": \"ach\",\n"
                + "      \"NetAmount\": 2,\n"
                + "      \"Operation\": \"Sale\",\n"
                + "      \"OrderId\": \"\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"AccountType\": \"Checking\",\n"
                + "        \"HolderName\": \"Lydia Marshall\",\n"
                + "        \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "        \"paymentDetails\": {\n"
                + "          \"categories\": [\n"
                + "            {\n"
                + "              \"amount\": 1000,\n"
                + "              \"label\": \"Deposit\"\n"
                + "            }\n"
                + "          ],\n"
                + "          \"currency\": \"USD\",\n"
                + "          \"serviceFee\": 0,\n"
                + "          \"splitFunding\": [\n"
                + "            {}\n"
                + "          ],\n"
                + "          \"totalAmount\": 2\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaymentTransId\": \"245-9e4072eef77e45979ea0e49f680000X\",\n"
                + "      \"PaymentTransStatus\": 1,\n"
                + "      \"PaypointDbaname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointEntryname\": \"7f1a3816XX\",\n"
                + "      \"PaypointLegalname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"ResponseData\": {\n"
                + "        \"authcode\": \"\",\n"
                + "        \"avsresponse_text\": \"\",\n"
                + "        \"cvvresponse_text\": \"\",\n"
                + "        \"response_code\": \"100\",\n"
                + "        \"response_code_text\": \"Operation successful.\",\n"
                + "        \"responsetext\": \"CAPTURED\",\n"
                + "        \"transactionid\": \"TRN_XXXXX\"\n"
                + "      },\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"SettledAmount\": 0.5,\n"
                + "      \"SettlementDate\": \"2024-11-20T00:00:00Z\",\n"
                + "      \"Source\": \"api\",\n"
                + "      \"Status\": 1,\n"
                + "      \"TransactionEvents\": [\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-19T15:57:40Z\",\n"
                + "          \"TransEvent\": \"Created\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventData\": {\n"
                + "            \"account_id\": \"TRA_XXXXX\",\n"
                + "            \"account_name\": \"123456\",\n"
                + "            \"action\": {\n"
                + "              \"app_id\": \"XXXXX\",\n"
                + "              \"app_name\": \"PayAbli\",\n"
                + "              \"id\": \"ACT_XXXXX\",\n"
                + "              \"result_code\": \"SUCCESS\",\n"
                + "              \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "              \"type\": \"AUTHORIZE\"\n"
                + "            },\n"
                + "            \"amount\": \"200\",\n"
                + "            \"batch_id\": \"\",\n"
                + "            \"capture_mode\": \"AUTO\",\n"
                + "            \"channel\": \"CNP\",\n"
                + "            \"country\": \"US\",\n"
                + "            \"currency\": \"USD\",\n"
                + "            \"fees\": {\n"
                + "              \"amount\": \"0\",\n"
                + "              \"rate\": \"0.00\",\n"
                + "              \"total_amount\": \"0\"\n"
                + "            },\n"
                + "            \"id\": \"TRN_XXXXX\",\n"
                + "            \"merchant_amount\": \"200\",\n"
                + "            \"merchant_id\": \"MER_XXXXX\",\n"
                + "            \"merchant_name\": \"Henriette97\",\n"
                + "            \"order\": {\n"
                + "              \"reference\": \"\"\n"
                + "            },\n"
                + "            \"payment_method\": {\n"
                + "              \"bank_transfer\": {\n"
                + "                \"account_type\": \"CHECKING\",\n"
                + "                \"bank\": {\n"
                + "                  \"name\": \"\"\n"
                + "                },\n"
                + "                \"masked_account_number_last4\": \"XXXX5678\"\n"
                + "              },\n"
                + "              \"entry_mode\": \"ECOM\",\n"
                + "              \"message\": \"Success\",\n"
                + "              \"narrative\": \"Lydia Marshall\",\n"
                + "              \"result\": \"00\"\n"
                + "            },\n"
                + "            \"reference\": \"245-XXXXX\",\n"
                + "            \"status\": \"CAPTURED\",\n"
                + "            \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "            \"type\": \"SALE\"\n"
                + "          },\n"
                + "          \"EventTime\": \"2024-11-19T20:58:01Z\",\n"
                + "          \"TransEvent\": \"Approved\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-20T03:05:10Z\",\n"
                + "          \"TransEvent\": \"ClosedBatch\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"TransactionTime\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"TransMethod\": \"ach\",\n"
                + "      \"Type\": \"credit\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"heldAmount\": 3.7,\n"
                + "    \"pageSize\": 0,\n"
                + "    \"refunds\": -3521.85,\n"
                + "    \"serviceFees\": 852.48,\n"
                + "    \"totalAmount\": 61645.74,\n"
                + "    \"totalNetAmount\": 61645.74,\n"
                + "    \"totalPages\": 21872,\n"
                + "    \"totalRecords\": 21872,\n"
                + "    \"transferAmount\": 0\n"
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
    public void testListSettlementsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"BatchAmount\":32,\"BatchNumber\":\"batch-100-20-2024\",\"Category\":\"auth\",\"CreatedAt\":\"2024-11-19T15:58:01Z\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"100 Golden Ridge Drive\",\"BillingAddress2\":\"STE 100\",\"BillingCity\":\"Mendota\",\"BillingCountry\":\"US\",\"BillingEmail\":\"lydia@example.com\",\"BillingPhone\":\"+12345678\",\"BillingState\":\"VA\",\"BillingZip\":\"20147\",\"customerId\":2707,\"CustomerNumber\":\"901102\",\"customerStatus\":1,\"FirstName\":\"Lydia\",\"LastName\":\"Marshall\"},\"ExpectedDepositDate\":\"2024-11-22T00:00:00Z\",\"GatewayTransId\":\"TRN_XXXXX\",\"Id\":25048,\"isHold\":0,\"MaskedAccount\":\"1XXXXXX5678\",\"Method\":\"ach\",\"NetAmount\":2,\"Operation\":\"Sale\",\"OrderId\":\"\",\"ParentOrgName\":\"Pilgrim Planner\",\"PaymentData\":{\"AccountType\":\"Checking\",\"HolderName\":\"Lydia Marshall\",\"MaskedAccount\":\"1XXXXXX5678\",\"paymentDetails\":{\"categories\":[{\"amount\":1000,\"label\":\"Deposit\"}],\"currency\":\"USD\",\"serviceFee\":0,\"splitFunding\":[{}],\"totalAmount\":2}},\"PaymentTransId\":\"245-9e4072eef77e45979ea0e49f680000X\",\"PaymentTransStatus\":1,\"PaypointDbaname\":\"Gruzya Adventure Outfitters, LLC\",\"PaypointEntryname\":\"7f1a3816XX\",\"PaypointLegalname\":\"Gruzya Adventure Outfitters, LLC\",\"ResponseData\":{\"authcode\":\"\",\"avsresponse_text\":\"\",\"cvvresponse_text\":\"\",\"response_code\":\"100\",\"response_code_text\":\"Operation successful.\",\"responsetext\":\"CAPTURED\",\"transactionid\":\"TRN_XXXXX\"},\"ScheduleReference\":0,\"SettledAmount\":0.5,\"SettlementDate\":\"2024-11-20T00:00:00Z\",\"Source\":\"api\",\"Status\":1,\"TransactionEvents\":[{\"EventTime\":\"2024-11-19T15:57:40Z\",\"TransEvent\":\"Created\"},{\"EventData\":{\"account_id\":\"TRA_XXXXX\",\"account_name\":\"123456\",\"action\":{\"app_id\":\"XXXXX\",\"app_name\":\"PayAbli\",\"id\":\"ACT_XXXXX\",\"result_code\":\"SUCCESS\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"AUTHORIZE\"},\"amount\":\"200\",\"batch_id\":\"\",\"capture_mode\":\"AUTO\",\"channel\":\"CNP\",\"country\":\"US\",\"currency\":\"USD\",\"fees\":{\"amount\":\"0\",\"rate\":\"0.00\",\"total_amount\":\"0\"},\"id\":\"TRN_XXXXX\",\"merchant_amount\":\"200\",\"merchant_id\":\"MER_XXXXX\",\"merchant_name\":\"Henriette97\",\"order\":{\"reference\":\"\"},\"payment_method\":{\"bank_transfer\":{\"account_type\":\"CHECKING\",\"bank\":{\"name\":\"\"},\"masked_account_number_last4\":\"XXXX5678\"},\"entry_mode\":\"ECOM\",\"message\":\"Success\",\"narrative\":\"Lydia Marshall\",\"result\":\"00\"},\"reference\":\"245-XXXXX\",\"status\":\"CAPTURED\",\"time_created\":\"2024-11-19T20:58:01.583Z\",\"type\":\"SALE\"},\"EventTime\":\"2024-11-19T20:58:01Z\",\"TransEvent\":\"Approved\"},{\"EventTime\":\"2024-11-20T03:05:10Z\",\"TransEvent\":\"ClosedBatch\"}],\"TransactionTime\":\"2024-11-19T15:58:01Z\",\"TransMethod\":\"ach\",\"Type\":\"credit\"}],\"Summary\":{\"heldAmount\":3.7,\"pageSize\":0,\"refunds\":-3521.85,\"serviceFees\":852.48,\"totalAmount\":61645.74,\"totalNetAmount\":61645.74,\"totalPages\":21872,\"totalRecords\":21872,\"transferAmount\":0}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"BatchAmount\": 32,\n"
                + "      \"BatchNumber\": \"batch-100-20-2024\",\n"
                + "      \"Category\": \"auth\",\n"
                + "      \"CreatedAt\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": \"AdditionalData\",\n"
                + "        \"BillingAddress1\": \"100 Golden Ridge Drive\",\n"
                + "        \"BillingAddress2\": \"STE 100\",\n"
                + "        \"BillingCity\": \"Mendota\",\n"
                + "        \"BillingCountry\": \"US\",\n"
                + "        \"BillingEmail\": \"lydia@example.com\",\n"
                + "        \"BillingPhone\": \"+12345678\",\n"
                + "        \"BillingState\": \"VA\",\n"
                + "        \"BillingZip\": \"20147\",\n"
                + "        \"customerId\": 2707,\n"
                + "        \"CustomerNumber\": \"901102\",\n"
                + "        \"customerStatus\": 1,\n"
                + "        \"FirstName\": \"Lydia\",\n"
                + "        \"LastName\": \"Marshall\"\n"
                + "      },\n"
                + "      \"ExpectedDepositDate\": \"2024-11-22T00:00:00Z\",\n"
                + "      \"GatewayTransId\": \"TRN_XXXXX\",\n"
                + "      \"Id\": 25048,\n"
                + "      \"isHold\": 0,\n"
                + "      \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "      \"Method\": \"ach\",\n"
                + "      \"NetAmount\": 2,\n"
                + "      \"Operation\": \"Sale\",\n"
                + "      \"OrderId\": \"\",\n"
                + "      \"ParentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"AccountType\": \"Checking\",\n"
                + "        \"HolderName\": \"Lydia Marshall\",\n"
                + "        \"MaskedAccount\": \"1XXXXXX5678\",\n"
                + "        \"paymentDetails\": {\n"
                + "          \"categories\": [\n"
                + "            {\n"
                + "              \"amount\": 1000,\n"
                + "              \"label\": \"Deposit\"\n"
                + "            }\n"
                + "          ],\n"
                + "          \"currency\": \"USD\",\n"
                + "          \"serviceFee\": 0,\n"
                + "          \"splitFunding\": [\n"
                + "            {}\n"
                + "          ],\n"
                + "          \"totalAmount\": 2\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaymentTransId\": \"245-9e4072eef77e45979ea0e49f680000X\",\n"
                + "      \"PaymentTransStatus\": 1,\n"
                + "      \"PaypointDbaname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"PaypointEntryname\": \"7f1a3816XX\",\n"
                + "      \"PaypointLegalname\": \"Gruzya Adventure Outfitters, LLC\",\n"
                + "      \"ResponseData\": {\n"
                + "        \"authcode\": \"\",\n"
                + "        \"avsresponse_text\": \"\",\n"
                + "        \"cvvresponse_text\": \"\",\n"
                + "        \"response_code\": \"100\",\n"
                + "        \"response_code_text\": \"Operation successful.\",\n"
                + "        \"responsetext\": \"CAPTURED\",\n"
                + "        \"transactionid\": \"TRN_XXXXX\"\n"
                + "      },\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"SettledAmount\": 0.5,\n"
                + "      \"SettlementDate\": \"2024-11-20T00:00:00Z\",\n"
                + "      \"Source\": \"api\",\n"
                + "      \"Status\": 1,\n"
                + "      \"TransactionEvents\": [\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-19T15:57:40Z\",\n"
                + "          \"TransEvent\": \"Created\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventData\": {\n"
                + "            \"account_id\": \"TRA_XXXXX\",\n"
                + "            \"account_name\": \"123456\",\n"
                + "            \"action\": {\n"
                + "              \"app_id\": \"XXXXX\",\n"
                + "              \"app_name\": \"PayAbli\",\n"
                + "              \"id\": \"ACT_XXXXX\",\n"
                + "              \"result_code\": \"SUCCESS\",\n"
                + "              \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "              \"type\": \"AUTHORIZE\"\n"
                + "            },\n"
                + "            \"amount\": \"200\",\n"
                + "            \"batch_id\": \"\",\n"
                + "            \"capture_mode\": \"AUTO\",\n"
                + "            \"channel\": \"CNP\",\n"
                + "            \"country\": \"US\",\n"
                + "            \"currency\": \"USD\",\n"
                + "            \"fees\": {\n"
                + "              \"amount\": \"0\",\n"
                + "              \"rate\": \"0.00\",\n"
                + "              \"total_amount\": \"0\"\n"
                + "            },\n"
                + "            \"id\": \"TRN_XXXXX\",\n"
                + "            \"merchant_amount\": \"200\",\n"
                + "            \"merchant_id\": \"MER_XXXXX\",\n"
                + "            \"merchant_name\": \"Henriette97\",\n"
                + "            \"order\": {\n"
                + "              \"reference\": \"\"\n"
                + "            },\n"
                + "            \"payment_method\": {\n"
                + "              \"bank_transfer\": {\n"
                + "                \"account_type\": \"CHECKING\",\n"
                + "                \"bank\": {\n"
                + "                  \"name\": \"\"\n"
                + "                },\n"
                + "                \"masked_account_number_last4\": \"XXXX5678\"\n"
                + "              },\n"
                + "              \"entry_mode\": \"ECOM\",\n"
                + "              \"message\": \"Success\",\n"
                + "              \"narrative\": \"Lydia Marshall\",\n"
                + "              \"result\": \"00\"\n"
                + "            },\n"
                + "            \"reference\": \"245-XXXXX\",\n"
                + "            \"status\": \"CAPTURED\",\n"
                + "            \"time_created\": \"2024-11-19T20:58:01.583Z\",\n"
                + "            \"type\": \"SALE\"\n"
                + "          },\n"
                + "          \"EventTime\": \"2024-11-19T20:58:01Z\",\n"
                + "          \"TransEvent\": \"Approved\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"EventTime\": \"2024-11-20T03:05:10Z\",\n"
                + "          \"TransEvent\": \"ClosedBatch\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"TransactionTime\": \"2024-11-19T15:58:01Z\",\n"
                + "      \"TransMethod\": \"ach\",\n"
                + "      \"Type\": \"credit\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"heldAmount\": 3.7,\n"
                + "    \"pageSize\": 0,\n"
                + "    \"refunds\": -3521.85,\n"
                + "    \"serviceFees\": 852.48,\n"
                + "    \"totalAmount\": 61645.74,\n"
                + "    \"totalNetAmount\": 61645.74,\n"
                + "    \"totalPages\": 21872,\n"
                + "    \"totalRecords\": 21872,\n"
                + "    \"transferAmount\": 0\n"
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
    public void testListSubscriptions() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":{\"CreatedAt\":\"2023-12-14T08:51:10Z\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"68 Golden Drive\",\"BillingAddress2\":\"\",\"BillingCity\":\"Johnson City\",\"BillingCountry\":\"US\",\"BillingEmail\":\"company@payabli.com\",\"BillingPhone\":\"\",\"BillingState\":\"TN\",\"BillingZip\":\"37612\",\"CompanyName\":\"Sunshine LLC\",\"customerId\":1323,\"CustomerNumber\":\"1234\",\"customerStatus\":1,\"FirstName\":\"Lisandra\",\"Identifiers\":[\"\\\\\\\"firstname\\\\\\\"\",\"\\\\\\\"lastname\\\\\\\"\",\"\\\\\\\"email\\\\\\\"\"],\"LastName\":\"Smitch\",\"ShippingAddress1\":\"68 Golden Drive\",\"ShippingCity\":\"Johnson City\",\"ShippingCountry\":\"US\",\"ShippingState\":\"TN\",\"ShippingZip\":\"37612\"},\"EndDate\":\"2026-03-20T00:00:00Z\",\"EntrypageId\":0,\"ExternalPaypointID\":\"f743aed24a-10\",\"FeeAmount\":0,\"Frequency\":\"monthly\",\"IdSub\":580,\"InvoiceData\":{\"AdditionalData\":\"AdditionalData\",\"frequency\":\"one-time\",\"invoiceAmount\":100,\"invoiceNumber\":\"QA-1702561870\",\"invoiceStatus\":1,\"invoiceType\":1,\"items\":[{\"itemCost\":10,\"itemDescription\":\"service\",\"itemMode\":1,\"itemProductName\":\"Mat replacement\",\"itemQty\":5,\"itemTotalAmount\":50},{\"itemCost\":5,\"itemDescription\":\"service\",\"itemMode\":1,\"itemProductName\":\"Mat clean\",\"itemQty\":10,\"itemTotalAmount\":50}]},\"LastRun\":\"2024-01-02T14:32:11Z\",\"LastUpdated\":\"2023-12-14T08:51:10Z\",\"LeftCycles\":20,\"Method\":\"card\",\"NetAmount\":10,\"NextDate\":\"2024-07-20T00:00:00Z\",\"ParentOrgName\":\"FitnessManager\",\"PaymentData\":{\"AccountExp\":\"0924\",\"AccountType\":\"unknown\",\"AccountZip\":\"90210\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"HolderName\":\"Mr. Michael Abernathy\",\"Initiator\":\"payor\",\"MaskedAccount\":\"2222 4XXXXXX0010\",\"paymentDetails\":{\"currency\":\"USD\",\"serviceFee\":0,\"totalAmount\":100},\"Sequence\":\"subsequent\",\"StoredMethodUsageType\":\"subscription\"},\"PaypointDbaname\":\"Athlete Factory LLC\",\"PaypointEntryname\":\"473ac58b0\",\"PaypointId\":10,\"PaypointLegalname\":\"Athlete Factory LLC\",\"PlanId\":1,\"StartDate\":\"2024-07-20T00:00:00Z\",\"SubEvents\":[{\"description\":\"created\",\"eventTime\":\"2023-12-14T13:51:10Z\",\"refData\":\"00-3470dfe2658b492811630255602f3fb5-d06fe0f72110000-00\"},{\"description\":\"updated\",\"eventTime\":\"2023-12-15T10:30:00Z\",\"refData\":\"01-1234abcde6789fghij4567klmnopqr89-abcdefghi12345678-01\",\"source\":\"web app\"}],\"SubStatus\":1,\"TotalAmount\":100,\"TotalCycles\":20,\"UntilCancelled\":false},\"Summary\":{\"pageIdentifier\":\"XXXXXXXXXXXXXXXXXXX\",\"pageSize\":20,\"totalAmount\":150.22,\"totalNetAmount\":150.22,\"totalPages\":1,\"totalRecords\":2}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": {\n"
                + "    \"CreatedAt\": \"2023-12-14T08:51:10Z\",\n"
                + "    \"Customer\": {\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"BillingAddress1\": \"68 Golden Drive\",\n"
                + "      \"BillingAddress2\": \"\",\n"
                + "      \"BillingCity\": \"Johnson City\",\n"
                + "      \"BillingCountry\": \"US\",\n"
                + "      \"BillingEmail\": \"company@payabli.com\",\n"
                + "      \"BillingPhone\": \"\",\n"
                + "      \"BillingState\": \"TN\",\n"
                + "      \"BillingZip\": \"37612\",\n"
                + "      \"CompanyName\": \"Sunshine LLC\",\n"
                + "      \"customerId\": 1323,\n"
                + "      \"CustomerNumber\": \"1234\",\n"
                + "      \"customerStatus\": 1,\n"
                + "      \"FirstName\": \"Lisandra\",\n"
                + "      \"Identifiers\": [\n"
                + "        \"\\\\\\\"firstname\\\\\\\"\",\n"
                + "        \"\\\\\\\"lastname\\\\\\\"\",\n"
                + "        \"\\\\\\\"email\\\\\\\"\"\n"
                + "      ],\n"
                + "      \"LastName\": \"Smitch\",\n"
                + "      \"ShippingAddress1\": \"68 Golden Drive\",\n"
                + "      \"ShippingCity\": \"Johnson City\",\n"
                + "      \"ShippingCountry\": \"US\",\n"
                + "      \"ShippingState\": \"TN\",\n"
                + "      \"ShippingZip\": \"37612\"\n"
                + "    },\n"
                + "    \"EndDate\": \"2026-03-20T00:00:00Z\",\n"
                + "    \"EntrypageId\": 0,\n"
                + "    \"ExternalPaypointID\": \"f743aed24a-10\",\n"
                + "    \"FeeAmount\": 0,\n"
                + "    \"Frequency\": \"monthly\",\n"
                + "    \"IdSub\": 580,\n"
                + "    \"InvoiceData\": {\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"frequency\": \"one-time\",\n"
                + "      \"invoiceAmount\": 100,\n"
                + "      \"invoiceNumber\": \"QA-1702561870\",\n"
                + "      \"invoiceStatus\": 1,\n"
                + "      \"invoiceType\": 1,\n"
                + "      \"items\": [\n"
                + "        {\n"
                + "          \"itemCost\": 10,\n"
                + "          \"itemDescription\": \"service\",\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemProductName\": \"Mat replacement\",\n"
                + "          \"itemQty\": 5,\n"
                + "          \"itemTotalAmount\": 50\n"
                + "        },\n"
                + "        {\n"
                + "          \"itemCost\": 5,\n"
                + "          \"itemDescription\": \"service\",\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemProductName\": \"Mat clean\",\n"
                + "          \"itemQty\": 10,\n"
                + "          \"itemTotalAmount\": 50\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"LastRun\": \"2024-01-02T14:32:11Z\",\n"
                + "    \"LastUpdated\": \"2023-12-14T08:51:10Z\",\n"
                + "    \"LeftCycles\": 20,\n"
                + "    \"Method\": \"card\",\n"
                + "    \"NetAmount\": 10,\n"
                + "    \"NextDate\": \"2024-07-20T00:00:00Z\",\n"
                + "    \"ParentOrgName\": \"FitnessManager\",\n"
                + "    \"PaymentData\": {\n"
                + "      \"AccountExp\": \"0924\",\n"
                + "      \"AccountType\": \"unknown\",\n"
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
                + "      \"HolderName\": \"Mr. Michael Abernathy\",\n"
                + "      \"Initiator\": \"payor\",\n"
                + "      \"MaskedAccount\": \"2222 4XXXXXX0010\",\n"
                + "      \"paymentDetails\": {\n"
                + "        \"currency\": \"USD\",\n"
                + "        \"serviceFee\": 0,\n"
                + "        \"totalAmount\": 100\n"
                + "      },\n"
                + "      \"Sequence\": \"subsequent\",\n"
                + "      \"StoredMethodUsageType\": \"subscription\"\n"
                + "    },\n"
                + "    \"PaypointDbaname\": \"Athlete Factory LLC\",\n"
                + "    \"PaypointEntryname\": \"473ac58b0\",\n"
                + "    \"PaypointId\": 10,\n"
                + "    \"PaypointLegalname\": \"Athlete Factory LLC\",\n"
                + "    \"PlanId\": 1,\n"
                + "    \"StartDate\": \"2024-07-20T00:00:00Z\",\n"
                + "    \"SubEvents\": [\n"
                + "      {\n"
                + "        \"description\": \"created\",\n"
                + "        \"eventTime\": \"2023-12-14T13:51:10Z\",\n"
                + "        \"refData\": \"00-3470dfe2658b492811630255602f3fb5-d06fe0f72110000-00\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"description\": \"updated\",\n"
                + "        \"eventTime\": \"2023-12-15T10:30:00Z\",\n"
                + "        \"refData\": \"01-1234abcde6789fghij4567klmnopqr89-abcdefghi12345678-01\",\n"
                + "        \"source\": \"web app\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"SubStatus\": 1,\n"
                + "    \"TotalAmount\": 100,\n"
                + "    \"TotalCycles\": 20,\n"
                + "    \"UntilCancelled\": false\n"
                + "  },\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"XXXXXXXXXXXXXXXXXXX\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 150.22,\n"
                + "    \"totalNetAmount\": 150.22,\n"
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
    public void testListSubscriptionsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":{\"CreatedAt\":\"2023-12-14T08:51:10Z\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"68 Golden Drive\",\"BillingAddress2\":\"\",\"BillingCity\":\"Johnson City\",\"BillingCountry\":\"US\",\"BillingEmail\":\"company@payabli.com\",\"BillingPhone\":\"\",\"BillingState\":\"TN\",\"BillingZip\":\"37612\",\"CompanyName\":\"Sunshine LLC\",\"customerId\":1323,\"CustomerNumber\":\"1234\",\"customerStatus\":1,\"FirstName\":\"Lisandra\",\"Identifiers\":[\"\\\\\\\"firstname\\\\\\\"\",\"\\\\\\\"lastname\\\\\\\"\",\"\\\\\\\"email\\\\\\\"\"],\"LastName\":\"Smitch\",\"ShippingAddress1\":\"68 Golden Drive\",\"ShippingCity\":\"Johnson City\",\"ShippingCountry\":\"US\",\"ShippingState\":\"TN\",\"ShippingZip\":\"37612\"},\"EndDate\":\"2026-03-20T00:00:00Z\",\"EntrypageId\":0,\"ExternalPaypointID\":\"f743aed24a-10\",\"FeeAmount\":0,\"Frequency\":\"monthly\",\"IdSub\":580,\"InvoiceData\":{\"AdditionalData\":\"AdditionalData\",\"frequency\":\"one-time\",\"invoiceAmount\":100,\"invoiceNumber\":\"QA-1702561870\",\"invoiceStatus\":1,\"invoiceType\":1,\"items\":[{\"itemCost\":10,\"itemDescription\":\"service\",\"itemMode\":1,\"itemProductName\":\"Mat replacement\",\"itemQty\":5,\"itemTotalAmount\":50},{\"itemCost\":5,\"itemDescription\":\"service\",\"itemMode\":1,\"itemProductName\":\"Mat clean\",\"itemQty\":10,\"itemTotalAmount\":50}]},\"LastRun\":\"2024-01-02T14:32:11Z\",\"LastUpdated\":\"2023-12-14T08:51:10Z\",\"LeftCycles\":20,\"Method\":\"card\",\"NetAmount\":10,\"NextDate\":\"2024-07-20T00:00:00Z\",\"ParentOrgName\":\"FitnessManager\",\"PaymentData\":{\"AccountExp\":\"0924\",\"AccountType\":\"unknow\",\"AccountZip\":\"90210\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"HolderName\":\"Mr. Michael Abernathy\",\"Initiator\":\"payor\",\"MaskedAccount\":\"2222 4XXXXXX0010\",\"paymentDetails\":{\"currency\":\"USD\",\"serviceFee\":0,\"totalAmount\":100},\"Sequence\":\"subsequent\",\"StoredMethodUsageType\":\"subscription\"},\"PaypointDbaname\":\"Athlete Factory LLC\",\"PaypointEntryname\":\"473ac58b0\",\"PaypointId\":10,\"PaypointLegalname\":\"Athlete Factory LLC\",\"PlanId\":1,\"StartDate\":\"2024-07-20T00:00:00Z\",\"SubEvents\":[{\"description\":\"created\",\"eventTime\":\"2023-12-14T13:51:10Z\",\"refData\":\"00-3470dfe2658b492811630255602f3fb5-d06fe0f72110000-00\"},{\"description\":\"updated\",\"eventTime\":\"2023-12-15T10:30:00Z\",\"refData\":\"01-1234abcde6789fghij4567klmnopqr89-abcdefghi12345678-01\",\"source\":\"web app\"}],\"SubStatus\":1,\"TotalAmount\":100,\"TotalCycles\":20,\"UntilCancelled\":false},\"Summary\":{\"pageIdentifier\":\"XXXXXXXXXXXXXXXXXXX\",\"pageSize\":20,\"totalAmount\":150.22,\"totalNetAmount\":150.22,\"totalPages\":1,\"totalRecords\":2}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": {\n"
                + "    \"CreatedAt\": \"2023-12-14T08:51:10Z\",\n"
                + "    \"Customer\": {\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"BillingAddress1\": \"68 Golden Drive\",\n"
                + "      \"BillingAddress2\": \"\",\n"
                + "      \"BillingCity\": \"Johnson City\",\n"
                + "      \"BillingCountry\": \"US\",\n"
                + "      \"BillingEmail\": \"company@payabli.com\",\n"
                + "      \"BillingPhone\": \"\",\n"
                + "      \"BillingState\": \"TN\",\n"
                + "      \"BillingZip\": \"37612\",\n"
                + "      \"CompanyName\": \"Sunshine LLC\",\n"
                + "      \"customerId\": 1323,\n"
                + "      \"CustomerNumber\": \"1234\",\n"
                + "      \"customerStatus\": 1,\n"
                + "      \"FirstName\": \"Lisandra\",\n"
                + "      \"Identifiers\": [\n"
                + "        \"\\\\\\\"firstname\\\\\\\"\",\n"
                + "        \"\\\\\\\"lastname\\\\\\\"\",\n"
                + "        \"\\\\\\\"email\\\\\\\"\"\n"
                + "      ],\n"
                + "      \"LastName\": \"Smitch\",\n"
                + "      \"ShippingAddress1\": \"68 Golden Drive\",\n"
                + "      \"ShippingCity\": \"Johnson City\",\n"
                + "      \"ShippingCountry\": \"US\",\n"
                + "      \"ShippingState\": \"TN\",\n"
                + "      \"ShippingZip\": \"37612\"\n"
                + "    },\n"
                + "    \"EndDate\": \"2026-03-20T00:00:00Z\",\n"
                + "    \"EntrypageId\": 0,\n"
                + "    \"ExternalPaypointID\": \"f743aed24a-10\",\n"
                + "    \"FeeAmount\": 0,\n"
                + "    \"Frequency\": \"monthly\",\n"
                + "    \"IdSub\": 580,\n"
                + "    \"InvoiceData\": {\n"
                + "      \"AdditionalData\": \"AdditionalData\",\n"
                + "      \"frequency\": \"one-time\",\n"
                + "      \"invoiceAmount\": 100,\n"
                + "      \"invoiceNumber\": \"QA-1702561870\",\n"
                + "      \"invoiceStatus\": 1,\n"
                + "      \"invoiceType\": 1,\n"
                + "      \"items\": [\n"
                + "        {\n"
                + "          \"itemCost\": 10,\n"
                + "          \"itemDescription\": \"service\",\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemProductName\": \"Mat replacement\",\n"
                + "          \"itemQty\": 5,\n"
                + "          \"itemTotalAmount\": 50\n"
                + "        },\n"
                + "        {\n"
                + "          \"itemCost\": 5,\n"
                + "          \"itemDescription\": \"service\",\n"
                + "          \"itemMode\": 1,\n"
                + "          \"itemProductName\": \"Mat clean\",\n"
                + "          \"itemQty\": 10,\n"
                + "          \"itemTotalAmount\": 50\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"LastRun\": \"2024-01-02T14:32:11Z\",\n"
                + "    \"LastUpdated\": \"2023-12-14T08:51:10Z\",\n"
                + "    \"LeftCycles\": 20,\n"
                + "    \"Method\": \"card\",\n"
                + "    \"NetAmount\": 10,\n"
                + "    \"NextDate\": \"2024-07-20T00:00:00Z\",\n"
                + "    \"ParentOrgName\": \"FitnessManager\",\n"
                + "    \"PaymentData\": {\n"
                + "      \"AccountExp\": \"0924\",\n"
                + "      \"AccountType\": \"unknow\",\n"
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
                + "      \"HolderName\": \"Mr. Michael Abernathy\",\n"
                + "      \"Initiator\": \"payor\",\n"
                + "      \"MaskedAccount\": \"2222 4XXXXXX0010\",\n"
                + "      \"paymentDetails\": {\n"
                + "        \"currency\": \"USD\",\n"
                + "        \"serviceFee\": 0,\n"
                + "        \"totalAmount\": 100\n"
                + "      },\n"
                + "      \"Sequence\": \"subsequent\",\n"
                + "      \"StoredMethodUsageType\": \"subscription\"\n"
                + "    },\n"
                + "    \"PaypointDbaname\": \"Athlete Factory LLC\",\n"
                + "    \"PaypointEntryname\": \"473ac58b0\",\n"
                + "    \"PaypointId\": 10,\n"
                + "    \"PaypointLegalname\": \"Athlete Factory LLC\",\n"
                + "    \"PlanId\": 1,\n"
                + "    \"StartDate\": \"2024-07-20T00:00:00Z\",\n"
                + "    \"SubEvents\": [\n"
                + "      {\n"
                + "        \"description\": \"created\",\n"
                + "        \"eventTime\": \"2023-12-14T13:51:10Z\",\n"
                + "        \"refData\": \"00-3470dfe2658b492811630255602f3fb5-d06fe0f72110000-00\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"description\": \"updated\",\n"
                + "        \"eventTime\": \"2023-12-15T10:30:00Z\",\n"
                + "        \"refData\": \"01-1234abcde6789fghij4567klmnopqr89-abcdefghi12345678-01\",\n"
                + "        \"source\": \"web app\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"SubStatus\": 1,\n"
                + "    \"TotalAmount\": 100,\n"
                + "    \"TotalCycles\": 20,\n"
                + "    \"UntilCancelled\": false\n"
                + "  },\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"XXXXXXXXXXXXXXXXXXX\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 150.22,\n"
                + "    \"totalNetAmount\": 150.22,\n"
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
    public void testListTransactions() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"BatchAmount\":30.22,\"BatchNumber\":\"1234567\",\"CfeeTransactions\":[{\"cFeeTransid\":\"string\",\"feeAmount\":0,\"operation\":\"string\",\"refundId\":0,\"settlementStatus\":0,\"transactionTime\":\"2019-08-24T14:15:22Z\",\"transStatus\":0}],\"ConnectorName\":\"gp\",\"Customer\":{\"AdditionalData\":\"AdditionalData\",\"BillingAddress1\":\"1111 street\",\"BillingAddress2\":\"string\",\"BillingCity\":\"myCity\",\"BillingCountry\":\"US\",\"BillingEmail\":\"customer@mail.com\",\"BillingPhone\":\"1234567890\",\"BillingState\":\"CA\",\"BillingZip\":\"45567\",\"CompanyName\":\"Sunshine LLC\",\"customerId\":34,\"CustomerNumber\":\"3456-7645A\",\"FirstName\":\"John\",\"LastName\":\"Doe\",\"ShippingAddress1\":\"string\",\"ShippingAddress2\":\"string\",\"ShippingCity\":\"string\",\"ShippingCountry\":\"string\",\"ShippingState\":\"string\",\"ShippingZip\":\"string\"},\"DeviceId\":\"6c361c7d-674c-44cc-b790-382b75d1xxx\",\"EntrypageId\":0,\"ExternalProcessorInformation\":\" \",\"FeeAmount\":10.25,\"GatewayTransId\":\"string\",\"InvoiceData\":{\"attachments\":[{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"}],\"company\":\"string\",\"discount\":0,\"dutyAmount\":0,\"firstName\":\"string\",\"freightAmount\":10,\"frequency\":\"one-time\",\"invoiceAmount\":105,\"invoiceDate\":\"2026-01-01\",\"invoiceDueDate\":\"2026-01-15\",\"invoiceEndDate\":\"2026-01-15\",\"invoiceNumber\":\"INV-2345\",\"invoiceStatus\":0,\"invoiceType\":0,\"items\":[{\"itemCategories\":[\"string\"],\"itemCommodityCode\":\"string\",\"itemCost\":1,\"itemDescription\":\"string\",\"itemMode\":0,\"itemProductCode\":\"string\",\"itemProductName\":\"product 01\",\"itemQty\":1,\"itemTaxAmount\":0,\"itemTaxRate\":0,\"itemTotalAmount\":0,\"itemUnitOfMeasure\":\"U\"}],\"lastName\":\"string\",\"notes\":\"string\",\"paymentTerms\":\"PIA\",\"purchaseOrder\":\"string\",\"shippingAddress1\":\"string\",\"shippingAddress2\":\"string\",\"shippingCity\":\"string\",\"shippingCountry\":\"string\",\"shippingEmail\":\"string\",\"shippingFromZip\":\"string\",\"shippingPhone\":\"string\",\"shippingState\":\"string\",\"shippingZip\":\"string\",\"summaryCommodityCode\":\"string\",\"tax\":2.05,\"termsConditions\":\"string\"},\"Method\":\"card\",\"NetAmount\":100,\"Operation\":\"Sale\",\"OrderId\":\"9876543\",\"OrgId\":2,\"ParentOrgName\":\"Payabli\",\"PaymentData\":{\"AccountExp\":\"0426\",\"AccountType\":\"visa\",\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"VISA\",\"binCardType\":\"DEBIT\",\"binCardCategory\":\"CLASSIC\",\"binCardIssuer\":\"CONOTOXIA SP. Z O.O\",\"binCardIssuerCountry\":\"POLAND\",\"binCardIssuerCountryCodeA2\":\"PL\",\"binCardIssuerCountryNumber\":\"616\",\"binCardIsRegulated\":\"true\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"POL\"},\"HolderName\":\"Billy J Franks\",\"Initiator\":\"payor\",\"MaskedAccount\":\"411111XXXXXX1111\",\"paymentDetails\":{\"totalAmount\":100},\"Sequence\":\"first\",\"StoredId\":\"675b43c1-9867-463c-8dc7-3d6ddb68554b-12812\",\"StoredMethodUsageType\":\"unscheduled\"},\"PaymentTransId\":\"2345667-ddd-fff\",\"PayorId\":55,\"PaypointDbaname\":\"Sunshine LLC\",\"PaypointEntryname\":\"7acda8200\",\"PaypointId\":2,\"PaypointLegalname\":\"Sunshine LLC\",\"PendingFeeAmount\":2,\"RefundId\":0,\"ResponseData\":{\"authcode\":\"00\",\"response_code\":\"100\",\"response_code_text\":\"Transaction was approved.\",\"responsetext\":\"CAPTURED\",\"transactionid\":\"TRN_xwCAjQorWAYX1nAhAoHZVfN8iYHbI0\"},\"ReturnedId\":0,\"ScheduleReference\":0,\"SettlementStatus\":0,\"Source\":\"vterminal\",\"splitFundingInstructions\":[{}],\"TotalAmount\":110.25,\"TransactionEvents\":[{}],\"TransactionTime\":\"2019-08-24T14:15:22Z\",\"TransStatus\":1}],\"Summary\":{\"pageIdentifier\":\"XXXXXXXXXXXXXXXXXXX\",\"pageSize\":20,\"totalAmount\":177.22,\"totalNetAmount\":177.22,\"totalPages\":1,\"totalRecords\":2}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"BatchAmount\": 30.22,\n"
                + "      \"BatchNumber\": \"1234567\",\n"
                + "      \"CfeeTransactions\": [\n"
                + "        {\n"
                + "          \"cFeeTransid\": \"string\",\n"
                + "          \"feeAmount\": 0,\n"
                + "          \"operation\": \"string\",\n"
                + "          \"refundId\": 0,\n"
                + "          \"settlementStatus\": 0,\n"
                + "          \"transactionTime\": \"2019-08-24T14:15:22Z\",\n"
                + "          \"transStatus\": 0\n"
                + "        }\n"
                + "      ],\n"
                + "      \"ConnectorName\": \"gp\",\n"
                + "      \"Customer\": {\n"
                + "        \"AdditionalData\": \"AdditionalData\",\n"
                + "        \"BillingAddress1\": \"1111 street\",\n"
                + "        \"BillingAddress2\": \"string\",\n"
                + "        \"BillingCity\": \"myCity\",\n"
                + "        \"BillingCountry\": \"US\",\n"
                + "        \"BillingEmail\": \"customer@mail.com\",\n"
                + "        \"BillingPhone\": \"1234567890\",\n"
                + "        \"BillingState\": \"CA\",\n"
                + "        \"BillingZip\": \"45567\",\n"
                + "        \"CompanyName\": \"Sunshine LLC\",\n"
                + "        \"customerId\": 34,\n"
                + "        \"CustomerNumber\": \"3456-7645A\",\n"
                + "        \"FirstName\": \"John\",\n"
                + "        \"LastName\": \"Doe\",\n"
                + "        \"ShippingAddress1\": \"string\",\n"
                + "        \"ShippingAddress2\": \"string\",\n"
                + "        \"ShippingCity\": \"string\",\n"
                + "        \"ShippingCountry\": \"string\",\n"
                + "        \"ShippingState\": \"string\",\n"
                + "        \"ShippingZip\": \"string\"\n"
                + "      },\n"
                + "      \"DeviceId\": \"6c361c7d-674c-44cc-b790-382b75d1xxx\",\n"
                + "      \"EntrypageId\": 0,\n"
                + "      \"ExternalProcessorInformation\": \" \",\n"
                + "      \"FeeAmount\": 10.25,\n"
                + "      \"GatewayTransId\": \"string\",\n"
                + "      \"InvoiceData\": {\n"
                + "        \"attachments\": [\n"
                + "          {\n"
                + "            \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "            \"filename\": \"my-doc.pdf\",\n"
                + "            \"ftype\": \"pdf\",\n"
                + "            \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "          }\n"
                + "        ],\n"
                + "        \"company\": \"string\",\n"
                + "        \"discount\": 0,\n"
                + "        \"dutyAmount\": 0,\n"
                + "        \"firstName\": \"string\",\n"
                + "        \"freightAmount\": 10,\n"
                + "        \"frequency\": \"one-time\",\n"
                + "        \"invoiceAmount\": 105,\n"
                + "        \"invoiceDate\": \"2026-01-01\",\n"
                + "        \"invoiceDueDate\": \"2026-01-15\",\n"
                + "        \"invoiceEndDate\": \"2026-01-15\",\n"
                + "        \"invoiceNumber\": \"INV-2345\",\n"
                + "        \"invoiceStatus\": 0,\n"
                + "        \"invoiceType\": 0,\n"
                + "        \"items\": [\n"
                + "          {\n"
                + "            \"itemCategories\": [\n"
                + "              \"string\"\n"
                + "            ],\n"
                + "            \"itemCommodityCode\": \"string\",\n"
                + "            \"itemCost\": 1,\n"
                + "            \"itemDescription\": \"string\",\n"
                + "            \"itemMode\": 0,\n"
                + "            \"itemProductCode\": \"string\",\n"
                + "            \"itemProductName\": \"product 01\",\n"
                + "            \"itemQty\": 1,\n"
                + "            \"itemTaxAmount\": 0,\n"
                + "            \"itemTaxRate\": 0,\n"
                + "            \"itemTotalAmount\": 0,\n"
                + "            \"itemUnitOfMeasure\": \"U\"\n"
                + "          }\n"
                + "        ],\n"
                + "        \"lastName\": \"string\",\n"
                + "        \"notes\": \"string\",\n"
                + "        \"paymentTerms\": \"PIA\",\n"
                + "        \"purchaseOrder\": \"string\",\n"
                + "        \"shippingAddress1\": \"string\",\n"
                + "        \"shippingAddress2\": \"string\",\n"
                + "        \"shippingCity\": \"string\",\n"
                + "        \"shippingCountry\": \"string\",\n"
                + "        \"shippingEmail\": \"string\",\n"
                + "        \"shippingFromZip\": \"string\",\n"
                + "        \"shippingPhone\": \"string\",\n"
                + "        \"shippingState\": \"string\",\n"
                + "        \"shippingZip\": \"string\",\n"
                + "        \"summaryCommodityCode\": \"string\",\n"
                + "        \"tax\": 2.05,\n"
                + "        \"termsConditions\": \"string\"\n"
                + "      },\n"
                + "      \"Method\": \"card\",\n"
                + "      \"NetAmount\": 100,\n"
                + "      \"Operation\": \"Sale\",\n"
                + "      \"OrderId\": \"9876543\",\n"
                + "      \"OrgId\": 2,\n"
                + "      \"ParentOrgName\": \"Payabli\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"AccountExp\": \"0426\",\n"
                + "        \"AccountType\": \"visa\",\n"
                + "        \"binData\": {\n"
                + "          \"binMatchedLength\": \"6\",\n"
                + "          \"binCardBrand\": \"VISA\",\n"
                + "          \"binCardType\": \"DEBIT\",\n"
                + "          \"binCardCategory\": \"CLASSIC\",\n"
                + "          \"binCardIssuer\": \"CONOTOXIA SP. Z O.O\",\n"
                + "          \"binCardIssuerCountry\": \"POLAND\",\n"
                + "          \"binCardIssuerCountryCodeA2\": \"PL\",\n"
                + "          \"binCardIssuerCountryNumber\": \"616\",\n"
                + "          \"binCardIsRegulated\": \"true\",\n"
                + "          \"binCardUseCategory\": \"Consumer\",\n"
                + "          \"binCardIssuerCountryCodeA3\": \"POL\"\n"
                + "        },\n"
                + "        \"HolderName\": \"Billy J Franks\",\n"
                + "        \"Initiator\": \"payor\",\n"
                + "        \"MaskedAccount\": \"411111XXXXXX1111\",\n"
                + "        \"paymentDetails\": {\n"
                + "          \"totalAmount\": 100\n"
                + "        },\n"
                + "        \"Sequence\": \"first\",\n"
                + "        \"StoredId\": \"675b43c1-9867-463c-8dc7-3d6ddb68554b-12812\",\n"
                + "        \"StoredMethodUsageType\": \"unscheduled\"\n"
                + "      },\n"
                + "      \"PaymentTransId\": \"2345667-ddd-fff\",\n"
                + "      \"PayorId\": 55,\n"
                + "      \"PaypointDbaname\": \"Sunshine LLC\",\n"
                + "      \"PaypointEntryname\": \"7acda8200\",\n"
                + "      \"PaypointId\": 2,\n"
                + "      \"PaypointLegalname\": \"Sunshine LLC\",\n"
                + "      \"PendingFeeAmount\": 2,\n"
                + "      \"RefundId\": 0,\n"
                + "      \"ResponseData\": {\n"
                + "        \"authcode\": \"00\",\n"
                + "        \"response_code\": \"100\",\n"
                + "        \"response_code_text\": \"Transaction was approved.\",\n"
                + "        \"responsetext\": \"CAPTURED\",\n"
                + "        \"transactionid\": \"TRN_xwCAjQorWAYX1nAhAoHZVfN8iYHbI0\"\n"
                + "      },\n"
                + "      \"ReturnedId\": 0,\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"SettlementStatus\": 0,\n"
                + "      \"Source\": \"vterminal\",\n"
                + "      \"splitFundingInstructions\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"TotalAmount\": 110.25,\n"
                + "      \"TransactionEvents\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"TransactionTime\": \"2019-08-24T14:15:22Z\",\n"
                + "      \"TransStatus\": 1\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"XXXXXXXXXXXXXXXXXXX\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 177.22,\n"
                + "    \"totalNetAmount\": 177.22,\n"
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
    public void testListTransactionsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"AchHolderType\":\"personal\",\"AchSecCode\":\"AchSecCode\",\"BatchAmount\":30.22,\"BatchNumber\":\"batch_226_ach_12-30-2023\",\"CfeeTransactions\":[{\"transactionTime\":\"2024-01-15T09:30:00Z\"}],\"ConnectorName\":\"gp\",\"DeviceId\":\"6c361c7d-674c-44cc-b790-382b75d1xxx\",\"EntrypageId\":0,\"ExternalProcessorInformation\":\"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\"FeeAmount\":1,\"GatewayTransId\":\"TRN_xwCAjQorWAYX1nAhAoHZVfN8iYHbI0\",\"Method\":\"ach\",\"NetAmount\":3762.87,\"Operation\":\"Sale\",\"OrderId\":\"O-5140\",\"OrgId\":123,\"ParentOrgName\":\"PropertyManager Pro\",\"PaymentData\":{\"paymentDetails\":{\"totalAmount\":100}},\"PaymentTransId\":\"226-fe55ec0348e34702bd91b4be198ce7ec\",\"PayorId\":1551,\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"PaypointId\":226,\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PendingFeeAmount\":2,\"RefundId\":0,\"ResponseData\":{\"response_code\":\"XXX\",\"response_code_text\":\"Transaction was approved.\",\"responsetext\":\"CAPTURED\"},\"ReturnedId\":0,\"ScheduleReference\":0,\"SettlementStatus\":2,\"Source\":\"api\",\"splitFundingInstructions\":[{}],\"TotalAmount\":30.22,\"TransactionEvents\":[{}],\"TransactionTime\":\"2025-10-19T00:00:00Z\",\"TransAdditionalData\":{\"key\":\"value\"},\"TransStatus\":1}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"AchHolderType\": \"personal\",\n"
                + "      \"AchSecCode\": \"AchSecCode\",\n"
                + "      \"BatchAmount\": 30.22,\n"
                + "      \"BatchNumber\": \"batch_226_ach_12-30-2023\",\n"
                + "      \"CfeeTransactions\": [\n"
                + "        {\n"
                + "          \"transactionTime\": \"2024-01-15T09:30:00Z\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"ConnectorName\": \"gp\",\n"
                + "      \"DeviceId\": \"6c361c7d-674c-44cc-b790-382b75d1xxx\",\n"
                + "      \"EntrypageId\": 0,\n"
                + "      \"ExternalProcessorInformation\": \"[MER_xxxxxxxxxxxxxx]/[NNNNNNNNN]\",\n"
                + "      \"FeeAmount\": 1,\n"
                + "      \"GatewayTransId\": \"TRN_xwCAjQorWAYX1nAhAoHZVfN8iYHbI0\",\n"
                + "      \"Method\": \"ach\",\n"
                + "      \"NetAmount\": 3762.87,\n"
                + "      \"Operation\": \"Sale\",\n"
                + "      \"OrderId\": \"O-5140\",\n"
                + "      \"OrgId\": 123,\n"
                + "      \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "      \"PaymentData\": {\n"
                + "        \"paymentDetails\": {\n"
                + "          \"totalAmount\": 100\n"
                + "        }\n"
                + "      },\n"
                + "      \"PaymentTransId\": \"226-fe55ec0348e34702bd91b4be198ce7ec\",\n"
                + "      \"PayorId\": 1551,\n"
                + "      \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "      \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "      \"PaypointId\": 226,\n"
                + "      \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "      \"PendingFeeAmount\": 2,\n"
                + "      \"RefundId\": 0,\n"
                + "      \"ResponseData\": {\n"
                + "        \"response_code\": \"XXX\",\n"
                + "        \"response_code_text\": \"Transaction was approved.\",\n"
                + "        \"responsetext\": \"CAPTURED\"\n"
                + "      },\n"
                + "      \"ReturnedId\": 0,\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"SettlementStatus\": 2,\n"
                + "      \"Source\": \"api\",\n"
                + "      \"splitFundingInstructions\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"TotalAmount\": 30.22,\n"
                + "      \"TransactionEvents\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"TransactionTime\": \"2025-10-19T00:00:00Z\",\n"
                + "      \"TransAdditionalData\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"TransStatus\": 1\n"
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
    public void testListTransferDetails() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Summary\":{\"achReturns\":-50,\"adjustments\":10,\"billingFees\":25,\"chargebacks\":0,\"grossTransferAmount\":1000,\"releaseAmount\":0,\"thirdPartyPaid\":0,\"totalNetAmountTransfer\":935,\"serviceFees\":30,\"splitAmount\":650.22,\"transferAmount\":935,\"refunds\":-20,\"heldAmount\":0,\"totalRecords\":1,\"totalAmount\":1000,\"totalNetAmount\":935,\"netBatchAmount\":935,\"totalPages\":1,\"pageSize\":20,\"pageidentifier\":\"XYZ123ABC456\"},\"Records\":[{\"transferDetailId\":654321,\"transferId\":12345,\"transactionId\":\"txn-4321hg6543fe\",\"type\":\"credit\",\"category\":\"sale\",\"grossAmount\":1000,\"chargeBackAmount\":0,\"returnedAmount\":0,\"refundAmount\":20,\"holdAmount\":0,\"releasedAmount\":0,\"billingFeesAmount\":25,\"thirdPartyPaidAmount\":0,\"adjustmentsAmount\":10,\"netTransferAmount\":935,\"splitFundingAmount\":0,\"ParentOrgName\":\"GadgetPro\",\"PaypointDbaname\":\"Global Gadgets\",\"PaypointLegalname\":\"Global Gadgets, LLC\",\"PaypointEntryname\":\"48ae10920\",\"PaymentTransId\":\"txn-4321hg6543fe\",\"ConnectorName\":\"gp\",\"GatewayTransId\":\"TRN_K6Nz3JxrNKkaPTF4ExCqfO4UwMW4CM\",\"OrderId\":\"order789\",\"Method\":\"ach\",\"BatchNumber\":\"batch_226_ach_12-30-2023\",\"BatchAmount\":30.22,\"PayorId\":1551,\"PaymentData\":{\"MaskedAccount\":\"411812XXXXXX2357\",\"AccountType\":\"visa\",\"AccountExp\":\"08/28\",\"HolderName\":\"Ara Karapetyan\",\"orderDescription\":\"Electronics Purchase\",\"StoredId\":null,\"Initiator\":null,\"StoredMethodUsageType\":null,\"Sequence\":null,\"accountId\":null,\"SignatureData\":null,\"binData\":{\"binMatchedLength\":\"6\",\"binCardBrand\":\"Visa\",\"binCardType\":\"Credit\",\"binCardCategory\":\"PLATINUM\",\"binCardIssuer\":\"Bank of Example\",\"binCardIssuerCountry\":\"United States\",\"binCardIssuerCountryCodeA2\":\"US\",\"binCardIssuerCountryNumber\":\"840\",\"binCardIsRegulated\":\"false\",\"binCardUseCategory\":\"Consumer\",\"binCardIssuerCountryCodeA3\":\"USA\"},\"paymentDetails\":null},\"TransStatus\":1,\"TotalAmount\":1000,\"NetAmount\":935,\"FeeAmount\":1,\"SettlementStatus\":2,\"Operation\":\"Sale\",\"ResponseData\":{\"response\":\"Approved\",\"responsetext\":\"Transaction successful\",\"authcode\":\"123456\",\"transactionid\":\"TRN_K6Nz3JxrNKkaPTF4ExCqfO4OOOOOX\",\"response_code\":\"100\",\"response_code_text\":\"Operation successful.\"},\"Source\":\"web\",\"ScheduleReference\":0,\"OrgId\":9876,\"RefundId\":0,\"ReturnedId\":0,\"TransactionTime\":\"2024-01-05T12:15:30.11\",\"Customer\":{\"Identifiers\":[\"customerId\",\"email\"],\"FirstName\":\"Ara\",\"LastName\":\"Karapetyan\",\"CompanyName\":\"Ara's Electronics\",\"BillingAddress1\":\"7890 Tech Park Drive\",\"BillingCity\":\"Baltimore\",\"BillingState\":\"MD\",\"BillingZip\":\"21230\",\"BillingCountry\":\"US\",\"BillingEmail\":\"ara.karapetyan@electronics.com\",\"CustomerNumber\":\"0010\",\"customerId\":7890},\"transactionNumber\":null,\"billingFeesDetails\":[],\"ExternalProcessorInformation\":null,\"PaypointId\":1357,\"ChargebackId\":null,\"RetrievalId\":null,\"TransAdditionalData\":null,\"invoiceData\":null,\"EntrypageId\":null,\"externalPaypointID\":null,\"IsValidatedACH\":true,\"splitFundingInstructions\":[],\"CfeeTransactions\":[],\"TransactionEvents\":[],\"PendingFeeAmount\":0,\"RiskFlagged\":false,\"RiskFlaggedOn\":null,\"RiskStatus\":\"approved\",\"RiskReason\":null,\"RiskAction\":null,\"RiskActionCode\":null,\"DeviceId\":null,\"AchSecCode\":\"PPD\",\"AchHolderType\":\"personal\",\"IpAddress\":\"192.100.1.100\",\"IsSameDayACH\":false,\"WalletType\":null}]}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Summary\": {\n"
                + "    \"achReturns\": -50,\n"
                + "    \"adjustments\": 10,\n"
                + "    \"billingFees\": 25,\n"
                + "    \"chargebacks\": 0,\n"
                + "    \"grossTransferAmount\": 1000,\n"
                + "    \"releaseAmount\": 0,\n"
                + "    \"thirdPartyPaid\": 0,\n"
                + "    \"totalNetAmountTransfer\": 935,\n"
                + "    \"serviceFees\": 30,\n"
                + "    \"splitAmount\": 650.22,\n"
                + "    \"transferAmount\": 935,\n"
                + "    \"refunds\": -20,\n"
                + "    \"heldAmount\": 0,\n"
                + "    \"totalRecords\": 1,\n"
                + "    \"totalAmount\": 1000,\n"
                + "    \"totalNetAmount\": 935,\n"
                + "    \"netBatchAmount\": 935,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"pageSize\": 20,\n"
                + "    \"pageidentifier\": \"XYZ123ABC456\"\n"
                + "  },\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"transferDetailId\": 654321,\n"
                + "      \"transferId\": 12345,\n"
                + "      \"transactionId\": \"txn-4321hg6543fe\",\n"
                + "      \"type\": \"credit\",\n"
                + "      \"category\": \"sale\",\n"
                + "      \"grossAmount\": 1000,\n"
                + "      \"chargeBackAmount\": 0,\n"
                + "      \"returnedAmount\": 0,\n"
                + "      \"refundAmount\": 20,\n"
                + "      \"holdAmount\": 0,\n"
                + "      \"releasedAmount\": 0,\n"
                + "      \"billingFeesAmount\": 25,\n"
                + "      \"thirdPartyPaidAmount\": 0,\n"
                + "      \"adjustmentsAmount\": 10,\n"
                + "      \"netTransferAmount\": 935,\n"
                + "      \"splitFundingAmount\": 0,\n"
                + "      \"ParentOrgName\": \"GadgetPro\",\n"
                + "      \"PaypointDbaname\": \"Global Gadgets\",\n"
                + "      \"PaypointLegalname\": \"Global Gadgets, LLC\",\n"
                + "      \"PaypointEntryname\": \"48ae10920\",\n"
                + "      \"PaymentTransId\": \"txn-4321hg6543fe\",\n"
                + "      \"ConnectorName\": \"gp\",\n"
                + "      \"GatewayTransId\": \"TRN_K6Nz3JxrNKkaPTF4ExCqfO4UwMW4CM\",\n"
                + "      \"OrderId\": \"order789\",\n"
                + "      \"Method\": \"ach\",\n"
                + "      \"BatchNumber\": \"batch_226_ach_12-30-2023\",\n"
                + "      \"BatchAmount\": 30.22,\n"
                + "      \"PayorId\": 1551,\n"
                + "      \"PaymentData\": {\n"
                + "        \"MaskedAccount\": \"411812XXXXXX2357\",\n"
                + "        \"AccountType\": \"visa\",\n"
                + "        \"AccountExp\": \"08/28\",\n"
                + "        \"HolderName\": \"Ara Karapetyan\",\n"
                + "        \"orderDescription\": \"Electronics Purchase\",\n"
                + "        \"StoredId\": null,\n"
                + "        \"Initiator\": null,\n"
                + "        \"StoredMethodUsageType\": null,\n"
                + "        \"Sequence\": null,\n"
                + "        \"accountId\": null,\n"
                + "        \"SignatureData\": null,\n"
                + "        \"binData\": {\n"
                + "          \"binMatchedLength\": \"6\",\n"
                + "          \"binCardBrand\": \"Visa\",\n"
                + "          \"binCardType\": \"Credit\",\n"
                + "          \"binCardCategory\": \"PLATINUM\",\n"
                + "          \"binCardIssuer\": \"Bank of Example\",\n"
                + "          \"binCardIssuerCountry\": \"United States\",\n"
                + "          \"binCardIssuerCountryCodeA2\": \"US\",\n"
                + "          \"binCardIssuerCountryNumber\": \"840\",\n"
                + "          \"binCardIsRegulated\": \"false\",\n"
                + "          \"binCardUseCategory\": \"Consumer\",\n"
                + "          \"binCardIssuerCountryCodeA3\": \"USA\"\n"
                + "        },\n"
                + "        \"paymentDetails\": null\n"
                + "      },\n"
                + "      \"TransStatus\": 1,\n"
                + "      \"TotalAmount\": 1000,\n"
                + "      \"NetAmount\": 935,\n"
                + "      \"FeeAmount\": 1,\n"
                + "      \"SettlementStatus\": 2,\n"
                + "      \"Operation\": \"Sale\",\n"
                + "      \"ResponseData\": {\n"
                + "        \"response\": \"Approved\",\n"
                + "        \"responsetext\": \"Transaction successful\",\n"
                + "        \"authcode\": \"123456\",\n"
                + "        \"transactionid\": \"TRN_K6Nz3JxrNKkaPTF4ExCqfO4OOOOOX\",\n"
                + "        \"response_code\": \"100\",\n"
                + "        \"response_code_text\": \"Operation successful.\"\n"
                + "      },\n"
                + "      \"Source\": \"web\",\n"
                + "      \"ScheduleReference\": 0,\n"
                + "      \"OrgId\": 9876,\n"
                + "      \"RefundId\": 0,\n"
                + "      \"ReturnedId\": 0,\n"
                + "      \"TransactionTime\": \"2024-01-05T12:15:30.11\",\n"
                + "      \"Customer\": {\n"
                + "        \"Identifiers\": [\n"
                + "          \"customerId\",\n"
                + "          \"email\"\n"
                + "        ],\n"
                + "        \"FirstName\": \"Ara\",\n"
                + "        \"LastName\": \"Karapetyan\",\n"
                + "        \"CompanyName\": \"Ara's Electronics\",\n"
                + "        \"BillingAddress1\": \"7890 Tech Park Drive\",\n"
                + "        \"BillingCity\": \"Baltimore\",\n"
                + "        \"BillingState\": \"MD\",\n"
                + "        \"BillingZip\": \"21230\",\n"
                + "        \"BillingCountry\": \"US\",\n"
                + "        \"BillingEmail\": \"ara.karapetyan@electronics.com\",\n"
                + "        \"CustomerNumber\": \"0010\",\n"
                + "        \"customerId\": 7890\n"
                + "      },\n"
                + "      \"transactionNumber\": null,\n"
                + "      \"billingFeesDetails\": [],\n"
                + "      \"ExternalProcessorInformation\": null,\n"
                + "      \"PaypointId\": 1357,\n"
                + "      \"ChargebackId\": null,\n"
                + "      \"RetrievalId\": null,\n"
                + "      \"TransAdditionalData\": null,\n"
                + "      \"invoiceData\": null,\n"
                + "      \"EntrypageId\": null,\n"
                + "      \"externalPaypointID\": null,\n"
                + "      \"IsValidatedACH\": true,\n"
                + "      \"splitFundingInstructions\": [],\n"
                + "      \"CfeeTransactions\": [],\n"
                + "      \"TransactionEvents\": [],\n"
                + "      \"PendingFeeAmount\": 0,\n"
                + "      \"RiskFlagged\": false,\n"
                + "      \"RiskFlaggedOn\": null,\n"
                + "      \"RiskStatus\": \"approved\",\n"
                + "      \"RiskReason\": null,\n"
                + "      \"RiskAction\": null,\n"
                + "      \"RiskActionCode\": null,\n"
                + "      \"DeviceId\": null,\n"
                + "      \"AchSecCode\": \"PPD\",\n"
                + "      \"AchHolderType\": \"personal\",\n"
                + "      \"IpAddress\": \"192.100.1.100\",\n"
                + "      \"IsSameDayACH\": false,\n"
                + "      \"WalletType\": null\n"
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
    public void testListTransfers() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"transferId\":79851,\"paypointId\":705,\"batchNumber\":\"split_705_gp_11-16-2024\",\"batchCurrency\":\"USD\",\"batchRecords\":1,\"transferIdentifier\":\"bbcbfed7-e535-45fe-8d62-000000\",\"batchId\":111430,\"paypointEntryName\":\"47ae3de37\",\"paypointLegalName\":\"Gruzya Outdoor Outfitters LLC\",\"paypointDbaName\":\"Gruzya Outdoor Outfitters\",\"paypointLogo\":\"https://example.com/logo.png\",\"parentOrgName\":\"Pilgrim Planner\",\"parentOrgId\":12345,\"parentOrgEntryName\":\"43aebc000\",\"parentOrgLogo\":\"https://example.com/parent-logo.png\",\"externalPaypointID\":\"ext-12345\",\"bankAccount\":{\"accountNumber\":\"****1234\",\"routingNumber\":\"123456789\"},\"transferDate\":\"2024-11-17T08:20:07.288+00:00\",\"processor\":\"gp\",\"transferStatus\":2,\"grossAmount\":1029,\"chargeBackAmount\":25,\"returnedAmount\":0,\"holdAmount\":0,\"releasedAmount\":0,\"billingFeesAmount\":0,\"thirdPartyPaidAmount\":0,\"adjustmentsAmount\":0,\"netTransferAmount\":1004,\"splitAmount\":650.22,\"eventsData\":[{\"description\":\"Transfer Created\",\"eventTime\":\"2024-11-16T08:15:33.4364067Z\",\"refData\":null,\"extraData\":null,\"source\":\"worker\"}],\"messages\":[]}],\"Summary\":{\"totalPages\":1,\"totalRecords\":2,\"pageSize\":20}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"transferId\": 79851,\n"
                + "      \"paypointId\": 705,\n"
                + "      \"batchNumber\": \"split_705_gp_11-16-2024\",\n"
                + "      \"batchCurrency\": \"USD\",\n"
                + "      \"batchRecords\": 1,\n"
                + "      \"transferIdentifier\": \"bbcbfed7-e535-45fe-8d62-000000\",\n"
                + "      \"batchId\": 111430,\n"
                + "      \"paypointEntryName\": \"47ae3de37\",\n"
                + "      \"paypointLegalName\": \"Gruzya Outdoor Outfitters LLC\",\n"
                + "      \"paypointDbaName\": \"Gruzya Outdoor Outfitters\",\n"
                + "      \"paypointLogo\": \"https://example.com/logo.png\",\n"
                + "      \"parentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"parentOrgId\": 12345,\n"
                + "      \"parentOrgEntryName\": \"43aebc000\",\n"
                + "      \"parentOrgLogo\": \"https://example.com/parent-logo.png\",\n"
                + "      \"externalPaypointID\": \"ext-12345\",\n"
                + "      \"bankAccount\": {\n"
                + "        \"accountNumber\": \"****1234\",\n"
                + "        \"routingNumber\": \"123456789\"\n"
                + "      },\n"
                + "      \"transferDate\": \"2024-11-17T08:20:07.288+00:00\",\n"
                + "      \"processor\": \"gp\",\n"
                + "      \"transferStatus\": 2,\n"
                + "      \"grossAmount\": 1029,\n"
                + "      \"chargeBackAmount\": 25,\n"
                + "      \"returnedAmount\": 0,\n"
                + "      \"holdAmount\": 0,\n"
                + "      \"releasedAmount\": 0,\n"
                + "      \"billingFeesAmount\": 0,\n"
                + "      \"thirdPartyPaidAmount\": 0,\n"
                + "      \"adjustmentsAmount\": 0,\n"
                + "      \"netTransferAmount\": 1004,\n"
                + "      \"splitAmount\": 650.22,\n"
                + "      \"eventsData\": [\n"
                + "        {\n"
                + "          \"description\": \"Transfer Created\",\n"
                + "          \"eventTime\": \"2024-11-16T08:15:33.4364067Z\",\n"
                + "          \"refData\": null,\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"worker\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"messages\": []\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 2,\n"
                + "    \"pageSize\": 20\n"
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
    public void testListTransfersOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"transferId\":79851,\"paypointId\":705,\"batchNumber\":\"split_705_gp_11-16-2024\",\"batchCurrency\":\"USD\",\"batchRecords\":1,\"transferIdentifier\":\"bbcbfed7-e535-45fe-8d62-000000\",\"batchId\":111430,\"paypointEntryName\":\"47ae3de37\",\"paypointLegalName\":\"Gruzya Outdoor Outfitters LLC\",\"paypointDbaName\":\"Gruzya Outdoor Outfitters\",\"paypointLogo\":\"https://example.com/logo.png\",\"parentOrgName\":\"Pilgrim Planner\",\"parentOrgId\":12345,\"parentOrgEntryName\":\"43aebc000\",\"parentOrgLogo\":\"https://example.com/parent-logo.png\",\"externalPaypointID\":\"ext-12345\",\"bankAccount\":{\"accountNumber\":\"****1234\",\"routingNumber\":\"123456789\"},\"transferDate\":\"2024-11-17T08:20:07.288+00:00\",\"processor\":\"gp\",\"transferStatus\":2,\"grossAmount\":1029,\"chargeBackAmount\":25,\"returnedAmount\":0,\"holdAmount\":0,\"releasedAmount\":0,\"billingFeesAmount\":0,\"thirdPartyPaidAmount\":0,\"adjustmentsAmount\":0,\"netTransferAmount\":1004,\"splitAmount\":650.22,\"eventsData\":[{\"description\":\"Transfer Created\",\"eventTime\":\"2024-11-16T08:15:33.4364067Z\",\"refData\":null,\"extraData\":null,\"source\":\"worker\"}],\"messages\":[]}],\"Summary\":{\"totalPages\":1,\"totalRecords\":2,\"pageSize\":20}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"transferId\": 79851,\n"
                + "      \"paypointId\": 705,\n"
                + "      \"batchNumber\": \"split_705_gp_11-16-2024\",\n"
                + "      \"batchCurrency\": \"USD\",\n"
                + "      \"batchRecords\": 1,\n"
                + "      \"transferIdentifier\": \"bbcbfed7-e535-45fe-8d62-000000\",\n"
                + "      \"batchId\": 111430,\n"
                + "      \"paypointEntryName\": \"47ae3de37\",\n"
                + "      \"paypointLegalName\": \"Gruzya Outdoor Outfitters LLC\",\n"
                + "      \"paypointDbaName\": \"Gruzya Outdoor Outfitters\",\n"
                + "      \"paypointLogo\": \"https://example.com/logo.png\",\n"
                + "      \"parentOrgName\": \"Pilgrim Planner\",\n"
                + "      \"parentOrgId\": 12345,\n"
                + "      \"parentOrgEntryName\": \"43aebc000\",\n"
                + "      \"parentOrgLogo\": \"https://example.com/parent-logo.png\",\n"
                + "      \"externalPaypointID\": \"ext-12345\",\n"
                + "      \"bankAccount\": {\n"
                + "        \"accountNumber\": \"****1234\",\n"
                + "        \"routingNumber\": \"123456789\"\n"
                + "      },\n"
                + "      \"transferDate\": \"2024-11-17T08:20:07.288+00:00\",\n"
                + "      \"processor\": \"gp\",\n"
                + "      \"transferStatus\": 2,\n"
                + "      \"grossAmount\": 1029,\n"
                + "      \"chargeBackAmount\": 25,\n"
                + "      \"returnedAmount\": 0,\n"
                + "      \"holdAmount\": 0,\n"
                + "      \"releasedAmount\": 0,\n"
                + "      \"billingFeesAmount\": 0,\n"
                + "      \"thirdPartyPaidAmount\": 0,\n"
                + "      \"adjustmentsAmount\": 0,\n"
                + "      \"netTransferAmount\": 1004,\n"
                + "      \"splitAmount\": 650.22,\n"
                + "      \"eventsData\": [\n"
                + "        {\n"
                + "          \"description\": \"Transfer Created\",\n"
                + "          \"eventTime\": \"2024-11-16T08:15:33.4364067Z\",\n"
                + "          \"refData\": null,\n"
                + "          \"extraData\": null,\n"
                + "          \"source\": \"worker\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"messages\": []\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 2,\n"
                + "    \"pageSize\": 20\n"
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
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"VendorNumber\":\"1234\",\"Name1\":\"Herman's Coatings\",\"Name2\":\"Herman's Coating Supply Company, LLC\",\"EIN\":\"123456789\",\"Phone\":\"212-555-1234\",\"Email\":\"example@email.com\",\"RemitEmail\":null,\"Address1\":\"123 Ocean Drive\",\"Address2\":\"Suite 400\",\"City\":\"Bristol\",\"State\":\"GA\",\"Zip\":\"31113\",\"Country\":\"US\",\"Mcc\":\"7777\",\"LocationCode\":\"LOC123\",\"Contacts\":{\"ContactEmail\":\"eric@martinezcoatings.com\",\"ContactName\":\"Eric Martinez\",\"ContactPhone\":\"5555555555\",\"ContactTitle\":\"Owner\"},\"BillingData\":{\"id\":123456,\"accountId\":\"bank-account-001\",\"nickname\":\"Main Checking Account\",\"bankName\":\"Example Bank\",\"routingAccount\":\"123456789\",\"accountNumber\":\"9876543210\",\"typeAccount\":\"Checking\",\"bankAccountHolderName\":\"John Doe\",\"bankAccountHolderType\":\"Business\",\"bankAccountFunction\":2,\"verified\":true,\"status\":1,\"services\":[],\"default\":true},\"PaymentMethod\":null,\"VendorStatus\":1,\"VendorId\":1,\"EnrollmentStatus\":null,\"Summary\":{\"ActiveBills\":2,\"PendingBills\":4,\"InTransitBills\":3,\"PaidBills\":18,\"OverdueBills\":1,\"ApprovedBills\":5,\"DisapprovedBills\":1,\"TotalBills\":34,\"ActiveBillsAmount\":1250.75,\"PendingBillsAmount\":2890.5,\"InTransitBillsAmount\":1675.25,\"PaidBillsAmount\":15420.8,\"OverdueBillsAmount\":425,\"ApprovedBillsAmount\":3240.9,\"DisapprovedBillsAmount\":180,\"TotalBillsAmount\":25083.2},\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"ParentOrgName\":\"PropertyManager Pro\",\"ParentOrgId\":1000,\"CreatedDate\":\"2022-07-01T15:00:01Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"remitAddress1\":\"123 Walnut Street\",\"remitAddress2\":\"Suite 900\",\"remitCity\":\"Miami\",\"remitState\":\"FL\",\"remitZip\":\"31113\",\"remitCountry\":\"US\",\"payeeName1\":\"payeeName1\",\"payeeName2\":\"payeeName2\",\"customField1\":\"\",\"customField2\":\"\",\"customerVendorAccount\":\"123-456\",\"InternalReferenceId\":1000000,\"additionalData\":null,\"externalPaypointID\":\"Paypoint-100\",\"StoredMethods\":[]}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":200,\"totalNetAmount\":77.22,\"totalPages\":1,\"totalRecords\":1}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"VendorNumber\": \"1234\",\n"
                + "      \"Name1\": \"Herman's Coatings\",\n"
                + "      \"Name2\": \"Herman's Coating Supply Company, LLC\",\n"
                + "      \"EIN\": \"123456789\",\n"
                + "      \"Phone\": \"212-555-1234\",\n"
                + "      \"Email\": \"example@email.com\",\n"
                + "      \"RemitEmail\": null,\n"
                + "      \"Address1\": \"123 Ocean Drive\",\n"
                + "      \"Address2\": \"Suite 400\",\n"
                + "      \"City\": \"Bristol\",\n"
                + "      \"State\": \"GA\",\n"
                + "      \"Zip\": \"31113\",\n"
                + "      \"Country\": \"US\",\n"
                + "      \"Mcc\": \"7777\",\n"
                + "      \"LocationCode\": \"LOC123\",\n"
                + "      \"Contacts\": {\n"
                + "        \"ContactEmail\": \"eric@martinezcoatings.com\",\n"
                + "        \"ContactName\": \"Eric Martinez\",\n"
                + "        \"ContactPhone\": \"5555555555\",\n"
                + "        \"ContactTitle\": \"Owner\"\n"
                + "      },\n"
                + "      \"BillingData\": {\n"
                + "        \"id\": 123456,\n"
                + "        \"accountId\": \"bank-account-001\",\n"
                + "        \"nickname\": \"Main Checking Account\",\n"
                + "        \"bankName\": \"Example Bank\",\n"
                + "        \"routingAccount\": \"123456789\",\n"
                + "        \"accountNumber\": \"9876543210\",\n"
                + "        \"typeAccount\": \"Checking\",\n"
                + "        \"bankAccountHolderName\": \"John Doe\",\n"
                + "        \"bankAccountHolderType\": \"Business\",\n"
                + "        \"bankAccountFunction\": 2,\n"
                + "        \"verified\": true,\n"
                + "        \"status\": 1,\n"
                + "        \"services\": [],\n"
                + "        \"default\": true\n"
                + "      },\n"
                + "      \"PaymentMethod\": null,\n"
                + "      \"VendorStatus\": 1,\n"
                + "      \"VendorId\": 1,\n"
                + "      \"EnrollmentStatus\": null,\n"
                + "      \"Summary\": {\n"
                + "        \"ActiveBills\": 2,\n"
                + "        \"PendingBills\": 4,\n"
                + "        \"InTransitBills\": 3,\n"
                + "        \"PaidBills\": 18,\n"
                + "        \"OverdueBills\": 1,\n"
                + "        \"ApprovedBills\": 5,\n"
                + "        \"DisapprovedBills\": 1,\n"
                + "        \"TotalBills\": 34,\n"
                + "        \"ActiveBillsAmount\": 1250.75,\n"
                + "        \"PendingBillsAmount\": 2890.5,\n"
                + "        \"InTransitBillsAmount\": 1675.25,\n"
                + "        \"PaidBillsAmount\": 15420.8,\n"
                + "        \"OverdueBillsAmount\": 425,\n"
                + "        \"ApprovedBillsAmount\": 3240.9,\n"
                + "        \"DisapprovedBillsAmount\": 180,\n"
                + "        \"TotalBillsAmount\": 25083.2\n"
                + "      },\n"
                + "      \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "      \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "      \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "      \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "      \"ParentOrgId\": 1000,\n"
                + "      \"CreatedDate\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"remitAddress1\": \"123 Walnut Street\",\n"
                + "      \"remitAddress2\": \"Suite 900\",\n"
                + "      \"remitCity\": \"Miami\",\n"
                + "      \"remitState\": \"FL\",\n"
                + "      \"remitZip\": \"31113\",\n"
                + "      \"remitCountry\": \"US\",\n"
                + "      \"payeeName1\": \"payeeName1\",\n"
                + "      \"payeeName2\": \"payeeName2\",\n"
                + "      \"customField1\": \"\",\n"
                + "      \"customField2\": \"\",\n"
                + "      \"customerVendorAccount\": \"123-456\",\n"
                + "      \"InternalReferenceId\": 1000000,\n"
                + "      \"additionalData\": null,\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"StoredMethods\": []\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 200,\n"
                + "    \"totalNetAmount\": 77.22,\n"
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
    public void testListVendorsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"VendorNumber\":\"1234\",\"Name1\":\"Herman's Coatings\",\"Name2\":\"Herman's Coating Supply Company, LLC\",\"EIN\":\"123456789\",\"Phone\":\"212-555-1234\",\"Email\":\"example@email.com\",\"RemitEmail\":null,\"Address1\":\"123 Ocean Drive\",\"Address2\":\"Suite 400\",\"City\":\"Bristol\",\"State\":\"GA\",\"Zip\":\"31113\",\"Country\":\"US\",\"Mcc\":\"7777\",\"LocationCode\":\"LOC123\",\"Contacts\":{\"ContactEmail\":\"eric@martinezcoatings.com\",\"ContactName\":\"Eric Martinez\",\"ContactPhone\":\"5555555555\",\"ContactTitle\":\"Owner\"},\"BillingData\":{\"id\":123456,\"accountId\":\"bank-account-001\",\"nickname\":\"Main Checking Account\",\"bankName\":\"Example Bank\",\"routingAccount\":\"123456789\",\"accountNumber\":\"9876543210\",\"typeAccount\":\"Checking\",\"bankAccountHolderName\":\"John Doe\",\"bankAccountHolderType\":\"Business\",\"bankAccountFunction\":2,\"verified\":true,\"status\":1,\"services\":[],\"default\":true},\"PaymentMethod\":null,\"VendorStatus\":1,\"VendorId\":1,\"EnrollmentStatus\":null,\"Summary\":{\"ActiveBills\":2,\"PendingBills\":4,\"InTransitBills\":3,\"PaidBills\":18,\"OverdueBills\":1,\"ApprovedBills\":5,\"DisapprovedBills\":1,\"TotalBills\":34,\"ActiveBillsAmount\":1250.75,\"PendingBillsAmount\":2890.5,\"InTransitBillsAmount\":1675.25,\"PaidBillsAmount\":15420.8,\"OverdueBillsAmount\":425,\"ApprovedBillsAmount\":3240.9,\"DisapprovedBillsAmount\":180,\"TotalBillsAmount\":25083.2},\"PaypointLegalname\":\"Sunshine Services, LLC\",\"PaypointDbaname\":\"Sunshine Gutters\",\"PaypointEntryname\":\"d193cf9a46\",\"ParentOrgName\":\"PropertyManager Pro\",\"ParentOrgId\":1000,\"CreatedDate\":\"2022-07-01T15:00:01Z\",\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"remitAddress1\":\"123 Walnut Street\",\"remitAddress2\":\"Suite 900\",\"remitCity\":\"Miami\",\"remitState\":\"FL\",\"remitZip\":\"31113\",\"remitCountry\":\"US\",\"payeeName1\":\"payeeName1\",\"payeeName2\":\"payeeName2\",\"customField1\":\"\",\"customField2\":\"\",\"customerVendorAccount\":\"123-456\",\"InternalReferenceId\":1000000,\"additionalData\":null,\"externalPaypointID\":\"Paypoint-100\",\"StoredMethods\":[]}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":200,\"totalNetAmount\":77.22,\"totalPages\":1,\"totalRecords\":1}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"VendorNumber\": \"1234\",\n"
                + "      \"Name1\": \"Herman's Coatings\",\n"
                + "      \"Name2\": \"Herman's Coating Supply Company, LLC\",\n"
                + "      \"EIN\": \"123456789\",\n"
                + "      \"Phone\": \"212-555-1234\",\n"
                + "      \"Email\": \"example@email.com\",\n"
                + "      \"RemitEmail\": null,\n"
                + "      \"Address1\": \"123 Ocean Drive\",\n"
                + "      \"Address2\": \"Suite 400\",\n"
                + "      \"City\": \"Bristol\",\n"
                + "      \"State\": \"GA\",\n"
                + "      \"Zip\": \"31113\",\n"
                + "      \"Country\": \"US\",\n"
                + "      \"Mcc\": \"7777\",\n"
                + "      \"LocationCode\": \"LOC123\",\n"
                + "      \"Contacts\": {\n"
                + "        \"ContactEmail\": \"eric@martinezcoatings.com\",\n"
                + "        \"ContactName\": \"Eric Martinez\",\n"
                + "        \"ContactPhone\": \"5555555555\",\n"
                + "        \"ContactTitle\": \"Owner\"\n"
                + "      },\n"
                + "      \"BillingData\": {\n"
                + "        \"id\": 123456,\n"
                + "        \"accountId\": \"bank-account-001\",\n"
                + "        \"nickname\": \"Main Checking Account\",\n"
                + "        \"bankName\": \"Example Bank\",\n"
                + "        \"routingAccount\": \"123456789\",\n"
                + "        \"accountNumber\": \"9876543210\",\n"
                + "        \"typeAccount\": \"Checking\",\n"
                + "        \"bankAccountHolderName\": \"John Doe\",\n"
                + "        \"bankAccountHolderType\": \"Business\",\n"
                + "        \"bankAccountFunction\": 2,\n"
                + "        \"verified\": true,\n"
                + "        \"status\": 1,\n"
                + "        \"services\": [],\n"
                + "        \"default\": true\n"
                + "      },\n"
                + "      \"PaymentMethod\": null,\n"
                + "      \"VendorStatus\": 1,\n"
                + "      \"VendorId\": 1,\n"
                + "      \"EnrollmentStatus\": null,\n"
                + "      \"Summary\": {\n"
                + "        \"ActiveBills\": 2,\n"
                + "        \"PendingBills\": 4,\n"
                + "        \"InTransitBills\": 3,\n"
                + "        \"PaidBills\": 18,\n"
                + "        \"OverdueBills\": 1,\n"
                + "        \"ApprovedBills\": 5,\n"
                + "        \"DisapprovedBills\": 1,\n"
                + "        \"TotalBills\": 34,\n"
                + "        \"ActiveBillsAmount\": 1250.75,\n"
                + "        \"PendingBillsAmount\": 2890.5,\n"
                + "        \"InTransitBillsAmount\": 1675.25,\n"
                + "        \"PaidBillsAmount\": 15420.8,\n"
                + "        \"OverdueBillsAmount\": 425,\n"
                + "        \"ApprovedBillsAmount\": 3240.9,\n"
                + "        \"DisapprovedBillsAmount\": 180,\n"
                + "        \"TotalBillsAmount\": 25083.2\n"
                + "      },\n"
                + "      \"PaypointLegalname\": \"Sunshine Services, LLC\",\n"
                + "      \"PaypointDbaname\": \"Sunshine Gutters\",\n"
                + "      \"PaypointEntryname\": \"d193cf9a46\",\n"
                + "      \"ParentOrgName\": \"PropertyManager Pro\",\n"
                + "      \"ParentOrgId\": 1000,\n"
                + "      \"CreatedDate\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"remitAddress1\": \"123 Walnut Street\",\n"
                + "      \"remitAddress2\": \"Suite 900\",\n"
                + "      \"remitCity\": \"Miami\",\n"
                + "      \"remitState\": \"FL\",\n"
                + "      \"remitZip\": \"31113\",\n"
                + "      \"remitCountry\": \"US\",\n"
                + "      \"payeeName1\": \"payeeName1\",\n"
                + "      \"payeeName2\": \"payeeName2\",\n"
                + "      \"customField1\": \"\",\n"
                + "      \"customField2\": \"\",\n"
                + "      \"customerVendorAccount\": \"123-456\",\n"
                + "      \"InternalReferenceId\": 1000000,\n"
                + "      \"additionalData\": null,\n"
                + "      \"externalPaypointID\": \"Paypoint-100\",\n"
                + "      \"StoredMethods\": []\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 200,\n"
                + "    \"totalNetAmount\": 77.22,\n"
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
    public void testListVcards() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"vcardSent\":true,\"cardToken\":\"vcrd_5Ty8NrBzXjKuqHm9DwElfP\",\"cardNumber\":\"44XX XXXX XXXX 1234\",\"cvc\":\"XXX\",\"expirationDate\":\"2025-12\",\"status\":\"Active\",\"amount\":500,\"currentBalance\":375.25,\"expenseLimit\":100,\"expenseLimitPeriod\":\"monthly\",\"maxNumberOfUses\":10,\"currentNumberOfUses\":3,\"exactAmount\":false,\"mcc\":\"5812\",\"tcc\":\"T01\",\"misc1\":\"Invoice #12345\",\"misc2\":\"Project: Office Supplies\",\"dateCreated\":\"2023-01-15T09:30:00Z\",\"dateModified\":\"2023-02-20T14:15:22Z\",\"associatedVendor\":{\"VendorNumber\":\"V-12345\",\"Name1\":\"Office Supply Co.\",\"EIN\":\"XXXXX6789\",\"Email\":\"billing@officesupply.example.com\",\"VendorId\":1542},\"associatedCustomer\":{\"firstname\":\"Acme\",\"lastname\":\"Corporation\"},\"PaypointDbaname\":\"Global Factory LLC\",\"PaypointLegalname\":\"Global Factory LLC\",\"PaypointEntryname\":\"4872acb376a\",\"externalPaypointID\":\"pay-10\",\"ParentOrgName\":\"SupplyPro\",\"paypointId\":236}],\"Summary\":{\"pageIdentifier\":\"XXXXXXXXXXXXXX\",\"pageSize\":20,\"totalAmount\":2500,\"totalactive\":5,\"totalamounteactive\":2500,\"totalbalanceactive\":1875.25,\"totalPages\":1,\"totalRecords\":5}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"vcardSent\": true,\n"
                + "      \"cardToken\": \"vcrd_5Ty8NrBzXjKuqHm9DwElfP\",\n"
                + "      \"cardNumber\": \"44XX XXXX XXXX 1234\",\n"
                + "      \"cvc\": \"XXX\",\n"
                + "      \"expirationDate\": \"2025-12\",\n"
                + "      \"status\": \"Active\",\n"
                + "      \"amount\": 500,\n"
                + "      \"currentBalance\": 375.25,\n"
                + "      \"expenseLimit\": 100,\n"
                + "      \"expenseLimitPeriod\": \"monthly\",\n"
                + "      \"maxNumberOfUses\": 10,\n"
                + "      \"currentNumberOfUses\": 3,\n"
                + "      \"exactAmount\": false,\n"
                + "      \"mcc\": \"5812\",\n"
                + "      \"tcc\": \"T01\",\n"
                + "      \"misc1\": \"Invoice #12345\",\n"
                + "      \"misc2\": \"Project: Office Supplies\",\n"
                + "      \"dateCreated\": \"2023-01-15T09:30:00Z\",\n"
                + "      \"dateModified\": \"2023-02-20T14:15:22Z\",\n"
                + "      \"associatedVendor\": {\n"
                + "        \"VendorNumber\": \"V-12345\",\n"
                + "        \"Name1\": \"Office Supply Co.\",\n"
                + "        \"EIN\": \"XXXXX6789\",\n"
                + "        \"Email\": \"billing@officesupply.example.com\",\n"
                + "        \"VendorId\": 1542\n"
                + "      },\n"
                + "      \"associatedCustomer\": {\n"
                + "        \"firstname\": \"Acme\",\n"
                + "        \"lastname\": \"Corporation\"\n"
                + "      },\n"
                + "      \"PaypointDbaname\": \"Global Factory LLC\",\n"
                + "      \"PaypointLegalname\": \"Global Factory LLC\",\n"
                + "      \"PaypointEntryname\": \"4872acb376a\",\n"
                + "      \"externalPaypointID\": \"pay-10\",\n"
                + "      \"ParentOrgName\": \"SupplyPro\",\n"
                + "      \"paypointId\": 236\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"XXXXXXXXXXXXXX\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 2500,\n"
                + "    \"totalactive\": 5,\n"
                + "    \"totalamounteactive\": 2500,\n"
                + "    \"totalbalanceactive\": 1875.25,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 5\n"
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
    public void testListVcardsOrg() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"vcardSent\":true,\"cardToken\":\"vcrd_5Ty8NrBzXjKuqHm9DwElfP\",\"cardNumber\":\"44XX XXXX XXXX 1234\",\"cvc\":\"XXX\",\"expirationDate\":\"2025-12\",\"status\":\"Active\",\"amount\":500,\"currentBalance\":375.25,\"expenseLimit\":100,\"expenseLimitPeriod\":\"monthly\",\"maxNumberOfUses\":10,\"currentNumberOfUses\":3,\"exactAmount\":false,\"mcc\":\"5812\",\"tcc\":\"T01\",\"misc1\":\"Invoice #12345\",\"misc2\":\"Project: Office Supplies\",\"dateCreated\":\"2023-01-15T09:30:00Z\",\"dateModified\":\"2023-02-20T14:15:22Z\",\"associatedVendor\":{\"VendorNumber\":\"V-12345\",\"Name1\":\"Office Supply Co.\",\"EIN\":\"XXXXX6789\",\"Email\":\"billing@officesupply.example.com\",\"VendorId\":1542},\"associatedCustomer\":{\"firstname\":\"Acme\",\"lastname\":\"Corporation\"},\"PaypointDbaname\":\"Global Factory LLC\",\"PaypointLegalname\":\"Global Factory LLC\",\"PaypointEntryname\":\"4872acb376a\",\"externalPaypointID\":\"pay-10\",\"ParentOrgName\":\"SupplyPro\",\"paypointId\":236}],\"Summary\":{\"pageIdentifier\":\"XXXXXXXXXXXXXX\",\"pageSize\":20,\"totalAmount\":2500,\"totalactive\":5,\"totalamounteactive\":2500,\"totalbalanceactive\":1875.25,\"totalPages\":1,\"totalRecords\":5}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"vcardSent\": true,\n"
                + "      \"cardToken\": \"vcrd_5Ty8NrBzXjKuqHm9DwElfP\",\n"
                + "      \"cardNumber\": \"44XX XXXX XXXX 1234\",\n"
                + "      \"cvc\": \"XXX\",\n"
                + "      \"expirationDate\": \"2025-12\",\n"
                + "      \"status\": \"Active\",\n"
                + "      \"amount\": 500,\n"
                + "      \"currentBalance\": 375.25,\n"
                + "      \"expenseLimit\": 100,\n"
                + "      \"expenseLimitPeriod\": \"monthly\",\n"
                + "      \"maxNumberOfUses\": 10,\n"
                + "      \"currentNumberOfUses\": 3,\n"
                + "      \"exactAmount\": false,\n"
                + "      \"mcc\": \"5812\",\n"
                + "      \"tcc\": \"T01\",\n"
                + "      \"misc1\": \"Invoice #12345\",\n"
                + "      \"misc2\": \"Project: Office Supplies\",\n"
                + "      \"dateCreated\": \"2023-01-15T09:30:00Z\",\n"
                + "      \"dateModified\": \"2023-02-20T14:15:22Z\",\n"
                + "      \"associatedVendor\": {\n"
                + "        \"VendorNumber\": \"V-12345\",\n"
                + "        \"Name1\": \"Office Supply Co.\",\n"
                + "        \"EIN\": \"XXXXX6789\",\n"
                + "        \"Email\": \"billing@officesupply.example.com\",\n"
                + "        \"VendorId\": 1542\n"
                + "      },\n"
                + "      \"associatedCustomer\": {\n"
                + "        \"firstname\": \"Acme\",\n"
                + "        \"lastname\": \"Corporation\"\n"
                + "      },\n"
                + "      \"PaypointDbaname\": \"Global Factory LLC\",\n"
                + "      \"PaypointLegalname\": \"Global Factory LLC\",\n"
                + "      \"PaypointEntryname\": \"4872acb376a\",\n"
                + "      \"externalPaypointID\": \"pay-10\",\n"
                + "      \"ParentOrgName\": \"SupplyPro\",\n"
                + "      \"paypointId\": 236\n"
                + "    }\n"
                + "  ],\n"
                + "  \"Summary\": {\n"
                + "    \"pageIdentifier\": \"XXXXXXXXXXXXXX\",\n"
                + "    \"pageSize\": 20,\n"
                + "    \"totalAmount\": 2500,\n"
                + "    \"totalactive\": 5,\n"
                + "    \"totalamounteactive\": 2500,\n"
                + "    \"totalbalanceactive\": 1875.25,\n"
                + "    \"totalPages\": 1,\n"
                + "    \"totalRecords\": 5\n"
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
