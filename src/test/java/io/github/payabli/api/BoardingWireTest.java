package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.boarding.requests.GetExternalApplicationRequest;
import io.github.payabli.api.resources.boarding.requests.ListApplicationsRequest;
import io.github.payabli.api.resources.boarding.requests.ListBoardingLinksRequest;
import io.github.payabli.api.resources.boarding.requests.RequestAppByAuth;
import io.github.payabli.api.resources.boarding.types.AddApplicationRequest;
import io.github.payabli.api.types.ApplicationData;
import io.github.payabli.api.types.ApplicationDataPayIn;
import io.github.payabli.api.types.ApplicationDataPayInContactsItem;
import io.github.payabli.api.types.ApplicationDataPayInOwnershipItem;
import io.github.payabli.api.types.ApplicationDataPayInServices;
import io.github.payabli.api.types.ApplicationDataPayInServicesAch;
import io.github.payabli.api.types.ApplicationDataPayInServicesCard;
import io.github.payabli.api.types.ApplicationDetailsRecord;
import io.github.payabli.api.types.ApplicationQueryRecord;
import io.github.payabli.api.types.Bank;
import io.github.payabli.api.types.BankAccountHolderType;
import io.github.payabli.api.types.BoardingLinkQueryRecord;
import io.github.payabli.api.types.OwnType;
import io.github.payabli.api.types.PayabliApiResponse00;
import io.github.payabli.api.types.PayabliApiResponse00Responsedatanonobject;
import io.github.payabli.api.types.QueryBoardingAppsListResponse;
import io.github.payabli.api.types.QueryBoardingLinksResponse;
import io.github.payabli.api.types.SignerDataRequest;
import io.github.payabli.api.types.TypeAccount;
import io.github.payabli.api.types.Whencharged;
import io.github.payabli.api.types.Whendelivered;
import io.github.payabli.api.types.Whenprovided;
import io.github.payabli.api.types.Whenrefunded;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardingWireTest {
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
    public void testAddApplication() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3625,\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.boarding()
                .addApplication(AddApplicationRequest.of(ApplicationDataPayIn.builder()
                        .services(ApplicationDataPayInServices.builder()
                                .ach(ApplicationDataPayInServicesAch.builder().build())
                                .card(ApplicationDataPayInServicesCard.builder()
                                        .acceptAmex(Optional.of(true))
                                        .acceptDiscover(Optional.of(true))
                                        .acceptMastercard(Optional.of(true))
                                        .acceptVisa(Optional.of(true))
                                        .build())
                                .build())
                        .phonenumber("1234567890")
                        .processingRegion("US")
                        .signer(SignerDataRequest.builder()
                                .name(Optional.of("John Smith"))
                                .ssn(Optional.of("123456789"))
                                .dob(Optional.of("01/01/1976"))
                                .phone(Optional.of("555888111"))
                                .email(Optional.of("test@email.com"))
                                .address(Optional.of("33 North St"))
                                .address1(Optional.of("STE 900"))
                                .city(Optional.of("Bristol"))
                                .country(Optional.of("US"))
                                .state(Optional.of("TN"))
                                .zip(Optional.of("55555"))
                                .signedDocumentReference(Optional.of("https://example.com/signed-document.pdf"))
                                .pciAttestation(Optional.of(true))
                                .attestationDate(Optional.of("04/20/2025"))
                                .additionalData(Optional.of(new HashMap<String, String>() {
                                    {
                                        put("deviceId", "499585-389fj484-3jcj8hj3");
                                        put("session", "fifji4-fiu443-fn4843");
                                        put("timeWithCompany", "6 Years");
                                    }
                                }))
                                .signDate(Optional.of("04/20/2025"))
                                .build())
                        .whenCharged(Whencharged.WHEN_SERVICE_PROVIDED)
                        .whenDelivered(Whendelivered.OVER_30_DAYS)
                        .whenProvided(Whenprovided.THIRTY_DAYS_OR_LESS)
                        .whenRefunded(Whenrefunded.THIRTY_DAYS_OR_LESS)
                        .annualRevenue(Optional.of(1000.0))
                        .averageBillSize(Optional.of("500"))
                        .averageMonthlyBill(Optional.of("5650"))
                        .avgmonthly(Optional.of(1000.0))
                        .baddress(Optional.of("123 Walnut Street"))
                        .baddress1(Optional.of("Suite 103"))
                        .bankData(Arrays.asList(
                                Bank.builder()
                                        .accountId("123-456")
                                        .nickname("Withdrawal Account")
                                        .bankName("Test Bank")
                                        .routingAccount("123123123")
                                        .accountNumber("123123123")
                                        .typeAccount(TypeAccount.CHECKING)
                                        .bankAccountHolderName("Gruzya Adventure Outfitters LLC")
                                        .bankAccountHolderType(BankAccountHolderType.BUSINESS)
                                        .bankAccountFunction(1)
                                        .build(),
                                Bank.builder()
                                        .accountId("123-456")
                                        .nickname("Deposit Account")
                                        .bankName("Test Bank")
                                        .routingAccount("123123123")
                                        .accountNumber("123123123")
                                        .typeAccount(TypeAccount.CHECKING)
                                        .bankAccountHolderName("Gruzya Adventure Outfitters LLC")
                                        .bankAccountHolderType(BankAccountHolderType.BUSINESS)
                                        .bankAccountFunction(0)
                                        .build()))
                        .bcity(Optional.of("New Vegas"))
                        .bcountry(Optional.of("US"))
                        .binperson(Optional.of(60))
                        .binphone(Optional.of(20))
                        .binweb(Optional.of(20))
                        .bstate(Optional.of("FL"))
                        .bsummary(Optional.of("Brick and mortar store that sells office supplies"))
                        .btype(Optional.of(OwnType.LIMITED_LIABILITY_COMPANY))
                        .bzip(Optional.of("33000"))
                        .contacts(Optional.of(Arrays.asList(ApplicationDataPayInContactsItem.builder()
                                .contactEmail("herman@hermanscoatings.com")
                                .contactName("Herman Martinez")
                                .contactPhone("3055550000")
                                .contactTitle("Owner")
                                .build())))
                        .creditLimit(Optional.of("creditLimit"))
                        .dbaName(Optional.of("Sunshine Gutters"))
                        .ein(Optional.of("123456789"))
                        .faxnumber(Optional.of("1234567890"))
                        .highticketamt(Optional.of(1000.0))
                        .legalName(Optional.of("Sunshine Services, LLC"))
                        .license(Optional.of("2222222FFG"))
                        .licstate(Optional.of("CA"))
                        .maddress(Optional.of("123 Walnut Street"))
                        .maddress1(Optional.of("STE 900"))
                        .mcc(Optional.of("7777"))
                        .mcity(Optional.of("Johnson City"))
                        .mcountry(Optional.of("US"))
                        .mstate(Optional.of("TN"))
                        .mzip(Optional.of("37615"))
                        .orgId(Optional.of(123L))
                        .ownership(Optional.of(Arrays.asList(ApplicationDataPayInOwnershipItem.builder()
                                .ownername("John Smith")
                                .ownertitle("CEO")
                                .ownerpercent(100)
                                .ownerssn("123456789")
                                .ownerdob("01/01/1990")
                                .ownerphone1("555888111")
                                .ownerphone2("555888111")
                                .owneremail("test@email.com")
                                .ownerdriver("CA6677778")
                                .oaddress("33 North St")
                                .ocity("Any City")
                                .ocountry("US")
                                .odriverstate("CA")
                                .ostate("CA")
                                .ozip("55555")
                                .build())))
                        .recipientEmail(Optional.of("josephray@example.com"))
                        .recipientEmailNotification(Optional.of(true))
                        .resumable(Optional.of(true))
                        .startdate(Optional.of("01/01/1990"))
                        .taxFillName(Optional.of("Sunshine LLC"))
                        .templateId(Optional.of(22L))
                        .ticketamt(Optional.of(1000.0))
                        .website(Optional.of("www.example.com"))
                        .build()));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody =
                TestResources.loadResource("/wire-tests/BoardingWireTest_testAddApplication_request.json");
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
    public void testDeleteApplication() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3625,\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.boarding().deleteApplication(352);
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
    public void testGetApplication() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/BoardingWireTest_testGetApplication_response.json")));
        ApplicationDetailsRecord response = client.boarding().getApplication(352);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/BoardingWireTest_testGetApplication_response.json");
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
    public void testGetApplicationByAuth() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/BoardingWireTest_testGetApplicationByAuth_response.json")));
        ApplicationQueryRecord response = client.boarding()
                .getApplicationByAuth(
                        "17E",
                        RequestAppByAuth.builder()
                                .email("admin@email.com")
                                .referenceId("n6UCd1f1ygG7")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody =
                "" + "{\n" + "  \"email\": \"admin@email.com\",\n" + "  \"referenceId\": \"n6UCd1f1ygG7\"\n" + "}";
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/BoardingWireTest_testGetApplicationByAuth_response.json");
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
    public void testGetByIdLinkApplication() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"acceptOauth\":false,\"acceptRegister\":false,\"builderData\":{\"attributes\":{\"minimumDocuments\":1,\"multipleContacts\":true,\"multipleOwners\":true}},\"entryAttributes\":\"entryAttributes\",\"id\":1000000,\"logo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"orgId\":123,\"pageIdentifier:\":\"null\",\"recipientEmailNotification\":true,\"referenceName\":\"payabli-00710\",\"referenceTemplateId\":1830,\"resumable\":false}"));
        BoardingLinkQueryRecord response = client.boarding().getByIdLinkApplication(91);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"acceptOauth\": false,\n"
                + "  \"acceptRegister\": false,\n"
                + "  \"builderData\": {\n"
                + "    \"attributes\": {\n"
                + "      \"minimumDocuments\": 1,\n"
                + "      \"multipleContacts\": true,\n"
                + "      \"multipleOwners\": true\n"
                + "    }\n"
                + "  },\n"
                + "  \"entryAttributes\": \"entryAttributes\",\n"
                + "  \"id\": 1000000,\n"
                + "  \"logo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"orgId\": 123,\n"
                + "  \"pageIdentifier:\": \"null\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"referenceName\": \"payabli-00710\",\n"
                + "  \"referenceTemplateId\": 1830,\n"
                + "  \"resumable\": false\n"
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
    public void testGetByTemplateIdLinkApplication() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"acceptOauth\":false,\"acceptRegister\":false,\"builderData\":{\"attributes\":{\"minimumDocuments\":1,\"multipleContacts\":true,\"multipleOwners\":true}},\"entryAttributes\":\"entryAttributes\",\"id\":1000000,\"logo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"orgId\":123,\"pageIdentifier:\":\"null\",\"recipientEmailNotification\":true,\"referenceName\":\"payabli-00710\",\"referenceTemplateId\":1830,\"resumable\":false}"));
        BoardingLinkQueryRecord response = client.boarding().getByTemplateIdLinkApplication(80.0);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"acceptOauth\": false,\n"
                + "  \"acceptRegister\": false,\n"
                + "  \"builderData\": {\n"
                + "    \"attributes\": {\n"
                + "      \"minimumDocuments\": 1,\n"
                + "      \"multipleContacts\": true,\n"
                + "      \"multipleOwners\": true\n"
                + "    }\n"
                + "  },\n"
                + "  \"entryAttributes\": \"entryAttributes\",\n"
                + "  \"id\": 1000000,\n"
                + "  \"logo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"orgId\": 123,\n"
                + "  \"pageIdentifier:\": \"null\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"referenceName\": \"payabli-00710\",\n"
                + "  \"referenceTemplateId\": 1830,\n"
                + "  \"resumable\": false\n"
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
    public void testGetExternalApplication() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseCode\":1,\"responseData\":{\"appLink\":\"https://boarding-sandbox.payabli.com/boarding/externalapp/load/17E\",\"referenceId\":\"n6UCd1f1ygG7\"},\"responseText\":\"Success\"}"));
        PayabliApiResponse00 response = client.boarding()
                .getExternalApplication(
                        352, "mail2", GetExternalApplicationRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseCode\": 1,\n"
                + "  \"responseData\": {\n"
                + "    \"appLink\": \"https://boarding-sandbox.payabli.com/boarding/externalapp/load/17E\",\n"
                + "    \"referenceId\": \"n6UCd1f1ygG7\"\n"
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
    public void testGetLinkApplication() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"acceptOauth\":false,\"acceptRegister\":false,\"builderData\":{\"attributes\":{\"minimumDocuments\":1,\"multipleContacts\":true,\"multipleOwners\":true}},\"entryAttributes\":\"entryAttributes\",\"id\":1000000,\"logo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"orgId\":123,\"pageIdentifier:\":\"null\",\"recipientEmailNotification\":true,\"referenceName\":\"payabli-00710\",\"referenceTemplateId\":1830,\"resumable\":false}"));
        BoardingLinkQueryRecord response = client.boarding().getLinkApplication("myorgaccountname-00091");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"acceptOauth\": false,\n"
                + "  \"acceptRegister\": false,\n"
                + "  \"builderData\": {\n"
                + "    \"attributes\": {\n"
                + "      \"minimumDocuments\": 1,\n"
                + "      \"multipleContacts\": true,\n"
                + "      \"multipleOwners\": true\n"
                + "    }\n"
                + "  },\n"
                + "  \"entryAttributes\": \"entryAttributes\",\n"
                + "  \"id\": 1000000,\n"
                + "  \"logo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"orgId\": 123,\n"
                + "  \"pageIdentifier:\": \"null\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"referenceName\": \"payabli-00710\",\n"
                + "  \"referenceTemplateId\": 1830,\n"
                + "  \"resumable\": false\n"
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
    public void testListApplications() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(
                        TestResources.loadResource("/wire-tests/BoardingWireTest_testListApplications_response.json")));
        QueryBoardingAppsListResponse response = client.boarding()
                .listApplications(
                        123,
                        ListApplicationsRequest.builder()
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
                TestResources.loadResource("/wire-tests/BoardingWireTest_testListApplications_response.json");
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
    public void testListBoardingLinks() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"AcceptOauth\":false,\"AcceptRegister\":false,\"EntryAttributes\":\"EntryAttributes\",\"Id\":1,\"LastUpdated\":\"2022-07-01T15:00:01Z\",\"OrgParentName\":\"PropertyManager Pro\",\"ReferenceName\":\"payabli-00710\",\"ReferenceTemplateId\":1830,\"TemplateCode\":\"TemplateCode\",\"TemplateName\":\"SMB\"}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
        QueryBoardingLinksResponse response = client.boarding()
                .listBoardingLinks(
                        123,
                        ListBoardingLinksRequest.builder()
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
                + "      \"AcceptOauth\": false,\n"
                + "      \"AcceptRegister\": false,\n"
                + "      \"EntryAttributes\": \"EntryAttributes\",\n"
                + "      \"Id\": 1,\n"
                + "      \"LastUpdated\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"OrgParentName\": \"PropertyManager Pro\",\n"
                + "      \"ReferenceName\": \"payabli-00710\",\n"
                + "      \"ReferenceTemplateId\": 1830,\n"
                + "      \"TemplateCode\": \"TemplateCode\",\n"
                + "      \"TemplateName\": \"SMB\"\n"
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
    public void testUpdateApplication() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseCode\":1,\"responseData\":3625,\"responseText\":\"Success\"}"));
        PayabliApiResponse00Responsedatanonobject response = client.boarding()
                .updateApplication(352, ApplicationData.builder().build());
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
