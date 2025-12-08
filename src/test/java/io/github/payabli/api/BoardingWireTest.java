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
import io.github.payabli.api.types.ApplicationDataPayInBankData;
import io.github.payabli.api.types.ApplicationDataPayInContactsItem;
import io.github.payabli.api.types.ApplicationDataPayInOwnershipItem;
import io.github.payabli.api.types.ApplicationDataPayInServices;
import io.github.payabli.api.types.ApplicationDataPayInServicesAch;
import io.github.payabli.api.types.ApplicationDataPayInServicesCard;
import io.github.payabli.api.types.ApplicationDetailsRecord;
import io.github.payabli.api.types.ApplicationQueryRecord;
import io.github.payabli.api.types.BoardingLinkQueryRecord;
import io.github.payabli.api.types.OwnType;
import io.github.payabli.api.types.PayabliApiResponse00;
import io.github.payabli.api.types.PayabliApiResponse00Responsedatanonobject;
import io.github.payabli.api.types.QueryBoardingAppsListResponse;
import io.github.payabli.api.types.QueryBoardingLinksResponse;
import io.github.payabli.api.types.SignerDataRequest;
import io.github.payabli.api.types.Whencharged;
import io.github.payabli.api.types.Whendelivered;
import io.github.payabli.api.types.Whenprovided;
import io.github.payabli.api.types.Whenrefunded;
import java.util.Arrays;
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
                                        .acceptAmex(true)
                                        .acceptDiscover(true)
                                        .acceptMastercard(true)
                                        .acceptVisa(true)
                                        .build())
                                .build())
                        .bankData(ApplicationDataPayInBankData.builder().build())
                        .phonenumber("1234567890")
                        .processingRegion("US")
                        .signer(SignerDataRequest.builder()
                                .name("John Smith")
                                .ssn("123456789")
                                .dob("01/01/1976")
                                .phone("555888111")
                                .email("test@email.com")
                                .address("33 North St")
                                .address1("STE 900")
                                .city("Bristol")
                                .country("US")
                                .state("TN")
                                .zip("55555")
                                .signedDocumentReference("https://example.com/signed-document.pdf")
                                .pciAttestation(true)
                                .attestationDate("04/20/2025")
                                .additionalData(
                                        "{\"deviceId\":\"499585-389fj484-3jcj8hj3\",\"session\":\"fifji4-fiu443-fn4843\",\"timeWithCompany\":\"6 Years\"}")
                                .signDate("04/20/2025")
                                .build())
                        .whenCharged(Whencharged.WHEN_SERVICE_PROVIDED)
                        .whenDelivered(Whendelivered.OVER_30_DAYS)
                        .whenProvided(Whenprovided.THIRTY_DAYS_OR_LESS)
                        .whenRefunded(Whenrefunded.THIRTY_DAYS_OR_LESS)
                        .annualRevenue(1000.0)
                        .averageBillSize("500")
                        .averageMonthlyBill("5650")
                        .avgmonthly(1000.0)
                        .baddress("123 Walnut Street")
                        .baddress1("Suite 103")
                        .bcity("New Vegas")
                        .bcountry("US")
                        .binperson(60)
                        .binphone(20)
                        .binweb(20)
                        .bstate("FL")
                        .bsummary("Brick and mortar store that sells office supplies")
                        .btype(OwnType.LIMITED_LIABILITY_COMPANY)
                        .bzip("33000")
                        .contacts(Optional.of(Arrays.asList(ApplicationDataPayInContactsItem.builder()
                                .contactEmail("herman@hermanscoatings.com")
                                .contactName("Herman Martinez")
                                .contactPhone("3055550000")
                                .contactTitle("Owner")
                                .build())))
                        .creditLimit("creditLimit")
                        .dbaName("Sunshine Gutters")
                        .ein("123456789")
                        .faxnumber("1234567890")
                        .highticketamt(1000.0)
                        .legalName("Sunshine Services, LLC")
                        .license("2222222FFG")
                        .licstate("CA")
                        .maddress("123 Walnut Street")
                        .maddress1("STE 900")
                        .mcc("7777")
                        .mcity("Johnson City")
                        .mcountry("US")
                        .mstate("TN")
                        .mzip("37615")
                        .orgId(123L)
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
                        .recipientEmail("josephray@example.com")
                        .recipientEmailNotification(true)
                        .resumable(true)
                        .startdate("01/01/1990")
                        .taxFillName("Sunshine LLC")
                        .templateId(22L)
                        .ticketamt(1000.0)
                        .website("www.example.com")
                        .build()));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"services\": {\n"
                + "    \"ach\": {},\n"
                + "    \"card\": {\n"
                + "      \"acceptAmex\": true,\n"
                + "      \"acceptDiscover\": true,\n"
                + "      \"acceptMastercard\": true,\n"
                + "      \"acceptVisa\": true\n"
                + "    }\n"
                + "  },\n"
                + "  \"annualRevenue\": 1000,\n"
                + "  \"averageBillSize\": \"500\",\n"
                + "  \"averageMonthlyBill\": \"5650\",\n"
                + "  \"avgmonthly\": 1000,\n"
                + "  \"baddress\": \"123 Walnut Street\",\n"
                + "  \"baddress1\": \"Suite 103\",\n"
                + "  \"bankData\": {},\n"
                + "  \"bcity\": \"New Vegas\",\n"
                + "  \"bcountry\": \"US\",\n"
                + "  \"binperson\": 60,\n"
                + "  \"binphone\": 20,\n"
                + "  \"binweb\": 20,\n"
                + "  \"bstate\": \"FL\",\n"
                + "  \"bsummary\": \"Brick and mortar store that sells office supplies\",\n"
                + "  \"btype\": \"Limited Liability Company\",\n"
                + "  \"bzip\": \"33000\",\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"herman@hermanscoatings.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"creditLimit\": \"creditLimit\",\n"
                + "  \"dbaName\": \"Sunshine Gutters\",\n"
                + "  \"ein\": \"123456789\",\n"
                + "  \"faxnumber\": \"1234567890\",\n"
                + "  \"highticketamt\": 1000,\n"
                + "  \"legalName\": \"Sunshine Services, LLC\",\n"
                + "  \"license\": \"2222222FFG\",\n"
                + "  \"licstate\": \"CA\",\n"
                + "  \"maddress\": \"123 Walnut Street\",\n"
                + "  \"maddress1\": \"STE 900\",\n"
                + "  \"mcc\": \"7777\",\n"
                + "  \"mcity\": \"Johnson City\",\n"
                + "  \"mcountry\": \"US\",\n"
                + "  \"mstate\": \"TN\",\n"
                + "  \"mzip\": \"37615\",\n"
                + "  \"orgId\": 123,\n"
                + "  \"ownership\": [\n"
                + "    {\n"
                + "      \"oaddress\": \"33 North St\",\n"
                + "      \"ocity\": \"Any City\",\n"
                + "      \"ocountry\": \"US\",\n"
                + "      \"odriverstate\": \"CA\",\n"
                + "      \"ostate\": \"CA\",\n"
                + "      \"ownerdob\": \"01/01/1990\",\n"
                + "      \"ownerdriver\": \"CA6677778\",\n"
                + "      \"owneremail\": \"test@email.com\",\n"
                + "      \"ownername\": \"John Smith\",\n"
                + "      \"ownerpercent\": 100,\n"
                + "      \"ownerphone1\": \"555888111\",\n"
                + "      \"ownerphone2\": \"555888111\",\n"
                + "      \"ownerssn\": \"123456789\",\n"
                + "      \"ownertitle\": \"CEO\",\n"
                + "      \"ozip\": \"55555\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"phonenumber\": \"1234567890\",\n"
                + "  \"processingRegion\": \"US\",\n"
                + "  \"recipientEmail\": \"josephray@example.com\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"resumable\": true,\n"
                + "  \"signer\": {\n"
                + "    \"address\": \"33 North St\",\n"
                + "    \"address1\": \"STE 900\",\n"
                + "    \"city\": \"Bristol\",\n"
                + "    \"country\": \"US\",\n"
                + "    \"dob\": \"01/01/1976\",\n"
                + "    \"email\": \"test@email.com\",\n"
                + "    \"name\": \"John Smith\",\n"
                + "    \"phone\": \"555888111\",\n"
                + "    \"ssn\": \"123456789\",\n"
                + "    \"state\": \"TN\",\n"
                + "    \"zip\": \"55555\",\n"
                + "    \"pciAttestation\": true,\n"
                + "    \"signedDocumentReference\": \"https://example.com/signed-document.pdf\",\n"
                + "    \"attestationDate\": \"04/20/2025\",\n"
                + "    \"signDate\": \"04/20/2025\",\n"
                + "    \"additionalData\": \"{\\\"deviceId\\\":\\\"499585-389fj484-3jcj8hj3\\\",\\\"session\\\":\\\"fifji4-fiu443-fn4843\\\",\\\"timeWithCompany\\\":\\\"6 Years\\\"}\"\n"
                + "  },\n"
                + "  \"startdate\": \"01/01/1990\",\n"
                + "  \"taxFillName\": \"Sunshine LLC\",\n"
                + "  \"templateId\": 22,\n"
                + "  \"ticketamt\": 1000,\n"
                + "  \"website\": \"www.example.com\",\n"
                + "  \"whenCharged\": \"When Service Provided\",\n"
                + "  \"whenDelivered\": \"Over 30 Days\",\n"
                + "  \"whenProvided\": \"30 Days or Less\",\n"
                + "  \"whenRefunded\": \"30 Days or Less\"\n"
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
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"annualRevenue\":1000,\"averageMonthlyVolume\":1000,\"averageTicketAmount\":1000,\"bAddress1\":\"123 Walnut Street\",\"bAddress2\":\"Suite 103\",\"bankData\":[{\"accountNumber\":\"123123123\",\"bankAccountFunction\":1,\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"bankAccountHolderType\":\"Business\",\"bankName\":\"Test Bank\",\"id\":123,\"nickname\":\"Withdrawal Account\",\"routingAccount\":\"123123123\",\"typeAccount\":\"Checking\",\"accountId\":\"123-456\"}],\"bCity\":\"New Vegas\",\"bCountry\":\"US\",\"bFax\":\"5551234567\",\"binPerson\":60,\"binPhone\":20,\"binWeb\":20,\"boardingLinkId\":91,\"boardingStatus\":123,\"boardingSubStatus\":123,\"bPhone\":\"5551234567\",\"bStartdate\":\"01/01/1990\",\"bState\":\"FL\",\"bSummary\":\"Brick and mortar store that sells office supplies\",\"builderData\":{\"services\":{\"ach\":{\"acceptance\":{\"ccd\":{\"ro\":false,\"value\":\"<string>\"},\"ppd\":{\"ro\":false,\"value\":\"<string>\"},\"web\":{\"ro\":false,\"value\":\"<string>\"}},\"fees\":{\"body\":[{\"columns\":[{\"value\":\"<string>\"}]}],\"header\":{\"columns\":[{\"value\":\"<string>\"}]}},\"price\":{\"body\":[{\"columns\":[{\"value\":\"<string>\"}]}],\"header\":{\"columns\":[{\"value\":\"<string>\"}]}}},\"card\":{\"acceptance\":{\"amex\":{\"ro\":false,\"value\":\"<string>\"},\"discover\":{\"ro\":false,\"value\":\"<string>\"},\"mastercard\":{\"ro\":false,\"value\":\"<string>\"},\"visa\":{\"ro\":false,\"value\":\"<string>\"}},\"fees\":{\"body\":[{\"columns\":[{\"value\":\"<string>\"}]}],\"header\":{\"columns\":[{\"value\":\"<string>\"}]}},\"price\":{\"body\":[{\"columns\":[{\"value\":\"<string>\"}]}],\"header\":{\"columns\":[{\"value\":\"<string>\"}]}}}},\"attributes\":{\"minimumDocuments\":1,\"multipleContacts\":true,\"multipleOwners\":true},\"banking\":{\"depositAccount\":{\"accountNumber\":{\"ro\":false,\"value\":\"<string>\"},\"bankName\":{\"ro\":false,\"value\":\"<string>\"},\"routingAccount\":{\"ro\":false,\"value\":\"<string>\"},\"typeAccount\":{\"ro\":false,\"value\":\"<string>\"}},\"withdrawalAccount\":{\"accountNumber\":{\"ro\":false,\"value\":\"<string>\"},\"bankName\":{\"ro\":false,\"value\":\"<string>\"},\"routingAccount\":{\"ro\":false,\"value\":\"<string>\"},\"typeAccount\":{\"ro\":false,\"value\":\"<string>\"}}},\"business\":{\"address\":{\"baddress\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"baddress1\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"bcity\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"bcountry\":{\"ro\":false,\"value\":\"<string>\"},\"bstate\":{\"ro\":false,\"value\":\"<string>\"},\"bzip\":{\"ro\":false,\"validator\":\"zipcode\",\"value\":\"<string>\"},\"maddress\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"maddress1\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"mcity\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"mcountry\":{\"ro\":false,\"value\":\"<string>\"},\"mstate\":{\"ro\":false,\"value\":\"<string>\"},\"mzip\":{\"ro\":false,\"validator\":\"zipcode\",\"value\":\"<string>\"}},\"details\":{\"btype\":{\"ro\":false,\"value\":\"<string>\"},\"dbaname\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"ein\":{\"ro\":false,\"validator\":\"routing\",\"value\":\"<string>\"},\"faxnumber\":{\"ro\":false,\"validator\":\"phone\",\"value\":\"<string>\"},\"legalname\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"license\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"licstate\":{\"ro\":false,\"value\":\"<string>\"},\"phonenumber\":{\"ro\":false,\"validator\":\"phone\",\"value\":\"<string>\"},\"startdate\":{\"ro\":false,\"value\":\"2021-12-13T05:00:00.000Z\"},\"taxfillname\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"website\":{\"ro\":false,\"validator\":\"url\",\"value\":\"<string>\"}}},\"owners\":{\"contact_list\":{\"contactEmail\":{\"ro\":false,\"validator\":\"email\",\"value\":\"<string>\"},\"contactName\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"contactPhone\":{\"ro\":false,\"validator\":\"phone\",\"value\":\"<string>\"},\"contactTitle\":{\"ro\":false,\"validator\":\"alpha\",\"value\":\"<string>\"}},\"own_list\":{\"oaddress\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"ocity\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"ocountry\":{\"ro\":false,\"value\":\"<string>\"},\"odriverstate\":{\"ro\":false,\"value\":\"<string>\"},\"ostate\":{\"ro\":false,\"value\":\"<string>\"},\"ownerdob\":{\"ro\":false,\"value\":\"2003-06-05T04:00:00.000Z\"},\"ownerdriver\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"owneremail\":{\"ro\":false,\"validator\":\"email\",\"value\":\"<string>\"},\"ownername\":{\"ro\":false,\"validator\":\"alphanumeric\",\"value\":\"<string>\"},\"ownerpercent\":{\"ro\":false,\"validator\":\"number\",\"value\":\"<string>\"},\"ownerphone1\":{\"ro\":false,\"validator\":\"phone\",\"value\":\"<string>\"},\"ownerphone2\":{\"ro\":false,\"validator\":\"phone\",\"value\":\"<string>\"},\"ownerssn\":{\"ro\":false,\"validator\":\"routing\",\"value\":\"<string>\"},\"ownertitle\":{\"ro\":false,\"validator\":\"alpha\",\"value\":\"<string>\"},\"ozip\":{\"ro\":false,\"validator\":\"zipcode\",\"value\":\"<string>\"}}},\"processing\":{\"avgmonthly\":{\"ro\":false,\"validator\":\"numbers\",\"value\":\"<string>\"},\"binperson\":{\"ro\":false,\"value\":\"<string>\"},\"binphone\":{\"ro\":false,\"value\":\"<string>\"},\"binweb\":{\"ro\":false,\"value\":\"<string>\"},\"bsummary\":{\"ro\":false,\"validator\":\"text\",\"value\":\"<string>\"},\"highticketamt\":{\"ro\":false,\"validator\":\"numbers\",\"value\":\"<string>\"},\"mcc\":{\"ro\":false,\"value\":\"<string>\"},\"ticketamt\":{\"ro\":false,\"validator\":\"numbers\",\"value\":\"<string>\"},\"whenCharged\":{\"ro\":false,\"value\":\"<string>\"},\"whenDelivered\":{\"ro\":false,\"value\":\"<string>\"},\"whenProvided\":{\"ro\":false,\"value\":\"<string>\"},\"whenRefunded\":{\"ro\":false,\"value\":\"<string>\"}}},\"bZip\":\"33000\",\"contactData\":[{\"contactEmail\":\"herman@hermanscoatings.com\",\"contactName\":\"Herman Martinez\",\"contactPhone\":\"3055550000\",\"contactTitle\":\"Owner\",\"additionalData\":null}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"dbaName\":\"Sunshine Gutters\",\"documentsRef\":{\"filelist\":[{\"originalName\":\"<string>\",\"zipName\":\"<string>\"}],\"zipfile\":\"zx45.zip\"},\"ein\":\"XXXX6789\",\"externalPaypointId\":\"Paypoint-100\",\"generalEvents\":[{\"description\":\"Created\",\"eventTime\":\"2022-06-17T16:35:21Z\"},{\"description\":\"Updated Status\",\"eventTime\":\"2022-06-17T16:35:22Z\",\"refData\":\"1\"}],\"highTicketAmount\":1000,\"idApplication\":325,\"lastModified\":\"2022-07-01T15:00:01Z\",\"legalName\":\"Sunshine Services, LLC\",\"license\":\"2222222FFG\",\"licenseState\":\"CA\",\"logo\":\"https://mysite.com/my-logo.png\",\"mAddress1\":\"123 Walnut Street\",\"mAddress2\":\"STE 900\",\"mccid\":\"<string>\",\"mCity\":\"TN\",\"mCountry\":\"US\",\"messages\":[{\"content\":\"Requested business license and bank statements.\",\"createdAt\":\"2023-10-30T19:37:20Z\",\"currentApplicationStatus\":3,\"currentApplicationSubStatus\":2,\"id\":261,\"messageType\":1,\"originalApplicationStatus\":-99,\"originalApplicationSubStatus\":0,\"roomId\":6,\"userId\":104,\"userName\":\"admin\"},{\"content\":\"TIN Failed, Need to reach out to merchant\",\"createdAt\":\"2023-10-25T18:41:55Z\",\"currentApplicationStatus\":3,\"currentApplicationSubStatus\":3,\"id\":231,\"messageType\":1,\"originalApplicationStatus\":-99,\"originalApplicationSubStatus\":0,\"roomId\":6,\"userId\":104,\"userName\":\"admin\"},{\"content\":\"Application approved, finalizing agreement.\",\"createdAt\":\"2023-09-09T00:00:00Z\",\"currentApplicationStatus\":7,\"currentApplicationSubStatus\":5,\"id\":3,\"messageType\":1,\"originalApplicationStatus\":-99,\"originalApplicationSubStatus\":0,\"roomId\":6,\"userId\":5,\"userName\":\"admin\"}],\"mState\":\"TN\",\"mZip\":\"37615\",\"orgId\":123,\"orgParentName\":\"PropertyManager Pro\",\"ownerData\":[{\"oaddress\":\"33 North St\",\"ocity\":\"Any City\",\"ocountry\":\"US\",\"odriverstate\":\"CA\",\"ostate\":\"CA\",\"ownerdob\":\"01/01/1990\",\"ownerdriver\":\"CA6677778\",\"owneremail\":\"test@email.com\",\"ownername\":\"John Smith\",\"ownerpercent\":25,\"ownerphone1\":\"555888111\",\"ownerphone2\":\"555888111\",\"ownerssn\":\"123456789\",\"ownertitle\":\"CEO\",\"ozip\":\"55555\",\"additionalData\":null}],\"ownType\":\"Limited Liability Company\",\"pageidentifier\":\"<string>\",\"recipientEmailNotification\":true,\"resumable\":true,\"salesCode\":\"<string>\",\"serviceData\":{\"ach\":{\"acceptCCD\":true,\"acceptPPD\":true,\"acceptWeb\":true},\"card\":{\"acceptAmex\":true,\"acceptDiscover\":true,\"acceptMastercard\":true,\"acceptVisa\":true},\"odp\":{\"allowAch\":true,\"allowChecks\":true,\"allowVCard\":true,\"processing_region\":\"US\",\"processor\":\"tysys\",\"issuerNetworkSettingsId\":\"12345678901234\"}},\"signer\":{\"address\":\"33 North St\",\"address1\":\"STE 900\",\"city\":\"Bristol\",\"country\":\"US\",\"dob\":\"01/01/1976\",\"email\":\"test@email.com\",\"name\":\"John Smith\",\"phone\":\"555888111\",\"ssn\":\"123456789\",\"state\":\"TN\",\"zip\":\"55555\",\"pciAttestation\":true,\"signedDocumentReference\":\"https://example.com/signed-document.pdf\",\"attestationDate\":\"04/20/2025\",\"signDate\":\"04/20/2025\",\"additionalData\":\"{\\\"deviceId\\\":\\\"499585-389fj484-3jcj8hj3\\\",\\\"session\\\":\\\"fifji4-fiu443-fn4843\\\",\\\"timeWithCompany\\\":\\\"6 Years\\\"}\"},\"taxfillname\":\"Sunshine LLC\",\"templateId\":22,\"websiteAddress\":\"www.example.com\",\"whencharged\":\"When Service Provided\",\"whendelivered\":\"Over 30 Days\",\"whenProvided\":\"30 Days or Less\",\"whenrefund\":\"Exchange Only\"}"));
        ApplicationDetailsRecord response = client.boarding().getApplication(352);
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"annualRevenue\": 1000,\n"
                + "  \"averageMonthlyVolume\": 1000,\n"
                + "  \"averageTicketAmount\": 1000,\n"
                + "  \"bAddress1\": \"123 Walnut Street\",\n"
                + "  \"bAddress2\": \"Suite 103\",\n"
                + "  \"bankData\": [\n"
                + "    {\n"
                + "      \"accountNumber\": \"123123123\",\n"
                + "      \"bankAccountFunction\": 1,\n"
                + "      \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "      \"bankAccountHolderType\": \"Business\",\n"
                + "      \"bankName\": \"Test Bank\",\n"
                + "      \"id\": 123,\n"
                + "      \"nickname\": \"Withdrawal Account\",\n"
                + "      \"routingAccount\": \"123123123\",\n"
                + "      \"typeAccount\": \"Checking\",\n"
                + "      \"accountId\": \"123-456\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"bCity\": \"New Vegas\",\n"
                + "  \"bCountry\": \"US\",\n"
                + "  \"bFax\": \"5551234567\",\n"
                + "  \"binPerson\": 60,\n"
                + "  \"binPhone\": 20,\n"
                + "  \"binWeb\": 20,\n"
                + "  \"boardingLinkId\": 91,\n"
                + "  \"boardingStatus\": 123,\n"
                + "  \"boardingSubStatus\": 123,\n"
                + "  \"bPhone\": \"5551234567\",\n"
                + "  \"bStartdate\": \"01/01/1990\",\n"
                + "  \"bState\": \"FL\",\n"
                + "  \"bSummary\": \"Brick and mortar store that sells office supplies\",\n"
                + "  \"builderData\": {\n"
                + "    \"services\": {\n"
                + "      \"ach\": {\n"
                + "        \"acceptance\": {\n"
                + "          \"ccd\": {\n"
                + "            \"ro\": false,\n"
                + "            \"value\": \"<string>\"\n"
                + "          },\n"
                + "          \"ppd\": {\n"
                + "            \"ro\": false,\n"
                + "            \"value\": \"<string>\"\n"
                + "          },\n"
                + "          \"web\": {\n"
                + "            \"ro\": false,\n"
                + "            \"value\": \"<string>\"\n"
                + "          }\n"
                + "        },\n"
                + "        \"fees\": {\n"
                + "          \"body\": [\n"
                + "            {\n"
                + "              \"columns\": [\n"
                + "                {\n"
                + "                  \"value\": \"<string>\"\n"
                + "                }\n"
                + "              ]\n"
                + "            }\n"
                + "          ],\n"
                + "          \"header\": {\n"
                + "            \"columns\": [\n"
                + "              {\n"
                + "                \"value\": \"<string>\"\n"
                + "              }\n"
                + "            ]\n"
                + "          }\n"
                + "        },\n"
                + "        \"price\": {\n"
                + "          \"body\": [\n"
                + "            {\n"
                + "              \"columns\": [\n"
                + "                {\n"
                + "                  \"value\": \"<string>\"\n"
                + "                }\n"
                + "              ]\n"
                + "            }\n"
                + "          ],\n"
                + "          \"header\": {\n"
                + "            \"columns\": [\n"
                + "              {\n"
                + "                \"value\": \"<string>\"\n"
                + "              }\n"
                + "            ]\n"
                + "          }\n"
                + "        }\n"
                + "      },\n"
                + "      \"card\": {\n"
                + "        \"acceptance\": {\n"
                + "          \"amex\": {\n"
                + "            \"ro\": false,\n"
                + "            \"value\": \"<string>\"\n"
                + "          },\n"
                + "          \"discover\": {\n"
                + "            \"ro\": false,\n"
                + "            \"value\": \"<string>\"\n"
                + "          },\n"
                + "          \"mastercard\": {\n"
                + "            \"ro\": false,\n"
                + "            \"value\": \"<string>\"\n"
                + "          },\n"
                + "          \"visa\": {\n"
                + "            \"ro\": false,\n"
                + "            \"value\": \"<string>\"\n"
                + "          }\n"
                + "        },\n"
                + "        \"fees\": {\n"
                + "          \"body\": [\n"
                + "            {\n"
                + "              \"columns\": [\n"
                + "                {\n"
                + "                  \"value\": \"<string>\"\n"
                + "                }\n"
                + "              ]\n"
                + "            }\n"
                + "          ],\n"
                + "          \"header\": {\n"
                + "            \"columns\": [\n"
                + "              {\n"
                + "                \"value\": \"<string>\"\n"
                + "              }\n"
                + "            ]\n"
                + "          }\n"
                + "        },\n"
                + "        \"price\": {\n"
                + "          \"body\": [\n"
                + "            {\n"
                + "              \"columns\": [\n"
                + "                {\n"
                + "                  \"value\": \"<string>\"\n"
                + "                }\n"
                + "              ]\n"
                + "            }\n"
                + "          ],\n"
                + "          \"header\": {\n"
                + "            \"columns\": [\n"
                + "              {\n"
                + "                \"value\": \"<string>\"\n"
                + "              }\n"
                + "            ]\n"
                + "          }\n"
                + "        }\n"
                + "      }\n"
                + "    },\n"
                + "    \"attributes\": {\n"
                + "      \"minimumDocuments\": 1,\n"
                + "      \"multipleContacts\": true,\n"
                + "      \"multipleOwners\": true\n"
                + "    },\n"
                + "    \"banking\": {\n"
                + "      \"depositAccount\": {\n"
                + "        \"accountNumber\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"bankName\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"routingAccount\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"typeAccount\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        }\n"
                + "      },\n"
                + "      \"withdrawalAccount\": {\n"
                + "        \"accountNumber\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"bankName\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"routingAccount\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"typeAccount\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        }\n"
                + "      }\n"
                + "    },\n"
                + "    \"business\": {\n"
                + "      \"address\": {\n"
                + "        \"baddress\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"baddress1\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"bcity\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"bcountry\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"bstate\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"bzip\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"zipcode\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"maddress\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"maddress1\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"mcity\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"mcountry\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"mstate\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"mzip\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"zipcode\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        }\n"
                + "      },\n"
                + "      \"details\": {\n"
                + "        \"btype\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"dbaname\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ein\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"routing\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"faxnumber\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"phone\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"legalname\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"license\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"licstate\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"phonenumber\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"phone\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"startdate\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"2021-12-13T05:00:00.000Z\"\n"
                + "        },\n"
                + "        \"taxfillname\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"website\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"url\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        }\n"
                + "      }\n"
                + "    },\n"
                + "    \"owners\": {\n"
                + "      \"contact_list\": {\n"
                + "        \"contactEmail\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"email\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"contactName\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"contactPhone\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"phone\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"contactTitle\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alpha\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        }\n"
                + "      },\n"
                + "      \"own_list\": {\n"
                + "        \"oaddress\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ocity\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ocountry\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"odriverstate\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ostate\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ownerdob\": {\n"
                + "          \"ro\": false,\n"
                + "          \"value\": \"2003-06-05T04:00:00.000Z\"\n"
                + "        },\n"
                + "        \"ownerdriver\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"owneremail\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"email\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ownername\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alphanumeric\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ownerpercent\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"number\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ownerphone1\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"phone\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ownerphone2\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"phone\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ownerssn\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"routing\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ownertitle\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"alpha\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        },\n"
                + "        \"ozip\": {\n"
                + "          \"ro\": false,\n"
                + "          \"validator\": \"zipcode\",\n"
                + "          \"value\": \"<string>\"\n"
                + "        }\n"
                + "      }\n"
                + "    },\n"
                + "    \"processing\": {\n"
                + "      \"avgmonthly\": {\n"
                + "        \"ro\": false,\n"
                + "        \"validator\": \"numbers\",\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"binperson\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"binphone\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"binweb\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"bsummary\": {\n"
                + "        \"ro\": false,\n"
                + "        \"validator\": \"text\",\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"highticketamt\": {\n"
                + "        \"ro\": false,\n"
                + "        \"validator\": \"numbers\",\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"mcc\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"ticketamt\": {\n"
                + "        \"ro\": false,\n"
                + "        \"validator\": \"numbers\",\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"whenCharged\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"whenDelivered\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"whenProvided\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      },\n"
                + "      \"whenRefunded\": {\n"
                + "        \"ro\": false,\n"
                + "        \"value\": \"<string>\"\n"
                + "      }\n"
                + "    }\n"
                + "  },\n"
                + "  \"bZip\": \"33000\",\n"
                + "  \"contactData\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"herman@hermanscoatings.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\",\n"
                + "      \"additionalData\": null\n"
                + "    }\n"
                + "  ],\n"
                + "  \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"dbaName\": \"Sunshine Gutters\",\n"
                + "  \"documentsRef\": {\n"
                + "    \"filelist\": [\n"
                + "      {\n"
                + "        \"originalName\": \"<string>\",\n"
                + "        \"zipName\": \"<string>\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"zipfile\": \"zx45.zip\"\n"
                + "  },\n"
                + "  \"ein\": \"XXXX6789\",\n"
                + "  \"externalPaypointId\": \"Paypoint-100\",\n"
                + "  \"generalEvents\": [\n"
                + "    {\n"
                + "      \"description\": \"Created\",\n"
                + "      \"eventTime\": \"2022-06-17T16:35:21Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"description\": \"Updated Status\",\n"
                + "      \"eventTime\": \"2022-06-17T16:35:22Z\",\n"
                + "      \"refData\": \"1\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"highTicketAmount\": 1000,\n"
                + "  \"idApplication\": 325,\n"
                + "  \"lastModified\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"legalName\": \"Sunshine Services, LLC\",\n"
                + "  \"license\": \"2222222FFG\",\n"
                + "  \"licenseState\": \"CA\",\n"
                + "  \"logo\": \"https://mysite.com/my-logo.png\",\n"
                + "  \"mAddress1\": \"123 Walnut Street\",\n"
                + "  \"mAddress2\": \"STE 900\",\n"
                + "  \"mccid\": \"<string>\",\n"
                + "  \"mCity\": \"TN\",\n"
                + "  \"mCountry\": \"US\",\n"
                + "  \"messages\": [\n"
                + "    {\n"
                + "      \"content\": \"Requested business license and bank statements.\",\n"
                + "      \"createdAt\": \"2023-10-30T19:37:20Z\",\n"
                + "      \"currentApplicationStatus\": 3,\n"
                + "      \"currentApplicationSubStatus\": 2,\n"
                + "      \"id\": 261,\n"
                + "      \"messageType\": 1,\n"
                + "      \"originalApplicationStatus\": -99,\n"
                + "      \"originalApplicationSubStatus\": 0,\n"
                + "      \"roomId\": 6,\n"
                + "      \"userId\": 104,\n"
                + "      \"userName\": \"admin\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"content\": \"TIN Failed, Need to reach out to merchant\",\n"
                + "      \"createdAt\": \"2023-10-25T18:41:55Z\",\n"
                + "      \"currentApplicationStatus\": 3,\n"
                + "      \"currentApplicationSubStatus\": 3,\n"
                + "      \"id\": 231,\n"
                + "      \"messageType\": 1,\n"
                + "      \"originalApplicationStatus\": -99,\n"
                + "      \"originalApplicationSubStatus\": 0,\n"
                + "      \"roomId\": 6,\n"
                + "      \"userId\": 104,\n"
                + "      \"userName\": \"admin\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"content\": \"Application approved, finalizing agreement.\",\n"
                + "      \"createdAt\": \"2023-09-09T00:00:00Z\",\n"
                + "      \"currentApplicationStatus\": 7,\n"
                + "      \"currentApplicationSubStatus\": 5,\n"
                + "      \"id\": 3,\n"
                + "      \"messageType\": 1,\n"
                + "      \"originalApplicationStatus\": -99,\n"
                + "      \"originalApplicationSubStatus\": 0,\n"
                + "      \"roomId\": 6,\n"
                + "      \"userId\": 5,\n"
                + "      \"userName\": \"admin\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"mState\": \"TN\",\n"
                + "  \"mZip\": \"37615\",\n"
                + "  \"orgId\": 123,\n"
                + "  \"orgParentName\": \"PropertyManager Pro\",\n"
                + "  \"ownerData\": [\n"
                + "    {\n"
                + "      \"oaddress\": \"33 North St\",\n"
                + "      \"ocity\": \"Any City\",\n"
                + "      \"ocountry\": \"US\",\n"
                + "      \"odriverstate\": \"CA\",\n"
                + "      \"ostate\": \"CA\",\n"
                + "      \"ownerdob\": \"01/01/1990\",\n"
                + "      \"ownerdriver\": \"CA6677778\",\n"
                + "      \"owneremail\": \"test@email.com\",\n"
                + "      \"ownername\": \"John Smith\",\n"
                + "      \"ownerpercent\": 25,\n"
                + "      \"ownerphone1\": \"555888111\",\n"
                + "      \"ownerphone2\": \"555888111\",\n"
                + "      \"ownerssn\": \"123456789\",\n"
                + "      \"ownertitle\": \"CEO\",\n"
                + "      \"ozip\": \"55555\",\n"
                + "      \"additionalData\": null\n"
                + "    }\n"
                + "  ],\n"
                + "  \"ownType\": \"Limited Liability Company\",\n"
                + "  \"pageidentifier\": \"<string>\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"resumable\": true,\n"
                + "  \"salesCode\": \"<string>\",\n"
                + "  \"serviceData\": {\n"
                + "    \"ach\": {\n"
                + "      \"acceptCCD\": true,\n"
                + "      \"acceptPPD\": true,\n"
                + "      \"acceptWeb\": true\n"
                + "    },\n"
                + "    \"card\": {\n"
                + "      \"acceptAmex\": true,\n"
                + "      \"acceptDiscover\": true,\n"
                + "      \"acceptMastercard\": true,\n"
                + "      \"acceptVisa\": true\n"
                + "    },\n"
                + "    \"odp\": {\n"
                + "      \"allowAch\": true,\n"
                + "      \"allowChecks\": true,\n"
                + "      \"allowVCard\": true,\n"
                + "      \"processing_region\": \"US\",\n"
                + "      \"processor\": \"tysys\",\n"
                + "      \"issuerNetworkSettingsId\": \"12345678901234\"\n"
                + "    }\n"
                + "  },\n"
                + "  \"signer\": {\n"
                + "    \"address\": \"33 North St\",\n"
                + "    \"address1\": \"STE 900\",\n"
                + "    \"city\": \"Bristol\",\n"
                + "    \"country\": \"US\",\n"
                + "    \"dob\": \"01/01/1976\",\n"
                + "    \"email\": \"test@email.com\",\n"
                + "    \"name\": \"John Smith\",\n"
                + "    \"phone\": \"555888111\",\n"
                + "    \"ssn\": \"123456789\",\n"
                + "    \"state\": \"TN\",\n"
                + "    \"zip\": \"55555\",\n"
                + "    \"pciAttestation\": true,\n"
                + "    \"signedDocumentReference\": \"https://example.com/signed-document.pdf\",\n"
                + "    \"attestationDate\": \"04/20/2025\",\n"
                + "    \"signDate\": \"04/20/2025\",\n"
                + "    \"additionalData\": \"{\\\"deviceId\\\":\\\"499585-389fj484-3jcj8hj3\\\",\\\"session\\\":\\\"fifji4-fiu443-fn4843\\\",\\\"timeWithCompany\\\":\\\"6 Years\\\"}\"\n"
                + "  },\n"
                + "  \"taxfillname\": \"Sunshine LLC\",\n"
                + "  \"templateId\": 22,\n"
                + "  \"websiteAddress\": \"www.example.com\",\n"
                + "  \"whencharged\": \"When Service Provided\",\n"
                + "  \"whendelivered\": \"Over 30 Days\",\n"
                + "  \"whenProvided\": \"30 Days or Less\",\n"
                + "  \"whenrefund\": \"Exchange Only\"\n"
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
    public void testGetApplicationByAuth() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"annualRevenue\":1000,\"averageMonthlyVolume\":1000,\"averageTicketAmount\":1000,\"bAddress1\":\"123 Walnut Street\",\"bAddress2\":\"Suite 103\",\"bankData\":[{\"accountNumber\":\"1XXXXXX3123\",\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"bankAccountHolderType\":\"Personal\",\"bankName\":\"Country Bank\",\"id\":1,\"nickname\":\"Business Checking 1234\",\"routingAccount\":\"123123123\",\"typeAccount\":\"Checking\",\"accountId\":\"123-456\"}],\"bCity\":\"New Vegas\",\"bCountry\":\"US\",\"bFax\":\"5551234567\",\"binPerson\":60,\"binPhone\":20,\"binWeb\":20,\"boardingLinkId\":91,\"boardingStatus\":1,\"boardingSubStatus\":1,\"bPhone\":\"5551234567\",\"bStartdate\":\"01/01/1990\",\"bState\":\"FL\",\"bSummary\":\"Brick and mortar store that sells office supplies\",\"builderData\":{\"attributes\":{\"minimumDocuments\":1,\"multipleContacts\":true,\"multipleOwners\":true}},\"bZip\":\"33000\",\"contactData\":[{\"contactEmail\":\"example@email.com\",\"contactName\":\"Herman Martinez\",\"contactPhone\":\"3055550000\",\"contactTitle\":\"Owner\"}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"dbaName\":\"Sunshine Gutters\",\"documentsRef\":{\"filelist\":[{}],\"zipfile\":\"zx45.zip\"},\"ein\":\"123456789\",\"externalPaypointId\":\"Paypoint-100\",\"generalEvents\":[{\"description\":\"TransferCreated\",\"eventTime\":\"2023-07-05T22:31:06Z\",\"extraData\":{\"key\":\"value\"},\"refData\":\"refData\",\"source\":\"api\"}],\"highTicketAmount\":1000,\"idApplication\":352,\"lastModified\":\"2022-07-01T15:00:01Z\",\"legalName\":\"Sunshine Services, LLC\",\"license\":\"2222222FFG\",\"licenseState\":\"CA\",\"logo\":{\"fContent\":\"TXkgdGVzdCBmaWxlHJ==...\",\"filename\":\"my-doc.pdf\",\"ftype\":\"pdf\",\"furl\":\"https://mysite.com/my-doc.pdf\"},\"mAddress1\":\"123 Walnut Street\",\"mAddress2\":\"STE 900\",\"mccid\":\"mccid\",\"mCity\":\"TN\",\"mCountry\":\"US\",\"mState\":\"TN\",\"mZip\":\"37615\",\"orgId\":123,\"orgParentName\":\"PropertyManager Pro\",\"ownerData\":[{\"oaddress\":\"33 North St\",\"ocity\":\"Any City\",\"ocountry\":\"US\",\"odriverstate\":\"CA\",\"ostate\":\"CA\",\"ownerdob\":\"01/01/1990\",\"ownerdriver\":\"CA6677778\",\"owneremail\":\"example@email.com\",\"ownername\":\"John Smith\",\"ownerpercent\":25,\"ownerphone1\":\"555888111\",\"ownerphone2\":\"555888111\",\"ownerssn\":\"123456789\",\"ownertitle\":\"CEO\",\"ozip\":\"55555\"}],\"ownType\":\"Limited Liability Company\",\"pageidentifier\":\"null\",\"recipientEmailNotification\":true,\"resumable\":false,\"salesCode\":\"salesCode\",\"serviceData\":{\"ach\":{\"acceptCCD\":false,\"acceptPPD\":false,\"acceptWeb\":true},\"card\":{\"acceptAmex\":true,\"acceptDiscover\":false,\"acceptMastercard\":true,\"acceptVisa\":true},\"odp\":{\"allowAch\":true,\"allowChecks\":true,\"allowVCard\":true,\"processing_region\":\"US\",\"processor\":\"tysys\",\"issuerNetworkSettingsId\":\"12345678901234\"}},\"signer\":{\"acceptance\":false,\"address\":\"33 North St\",\"address1\":\"STE 900\",\"city\":\"Bristol\",\"country\":\"US\",\"dob\":\"01/01/1990\",\"email\":\"example@email.com\",\"name\":\"John Smith\",\"phone\":\"555888111\",\"signedDocumentReference\":\"signedDocumentReference\",\"signerUuid\":\"54455d5d-34ff-416c-91e0-5bc87199999\",\"ssn\":\"123456789\",\"state\":\"TN\",\"zip\":\"55555\"},\"taxfillname\":\"Sunshine LLC\",\"templateId\":22,\"websiteAddress\":\"www.example.com\",\"whencharged\":\"When Service Provided\",\"whendelivered\":\"0-7 Days\",\"whenProvided\":\"30 Days or Less\",\"whenrefund\":\"Exchange Only\"}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"annualRevenue\": 1000,\n"
                + "  \"averageMonthlyVolume\": 1000,\n"
                + "  \"averageTicketAmount\": 1000,\n"
                + "  \"bAddress1\": \"123 Walnut Street\",\n"
                + "  \"bAddress2\": \"Suite 103\",\n"
                + "  \"bankData\": [\n"
                + "    {\n"
                + "      \"accountNumber\": \"1XXXXXX3123\",\n"
                + "      \"bankAccountFunction\": 0,\n"
                + "      \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "      \"bankAccountHolderType\": \"Personal\",\n"
                + "      \"bankName\": \"Country Bank\",\n"
                + "      \"id\": 1,\n"
                + "      \"nickname\": \"Business Checking 1234\",\n"
                + "      \"routingAccount\": \"123123123\",\n"
                + "      \"typeAccount\": \"Checking\",\n"
                + "      \"accountId\": \"123-456\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"bCity\": \"New Vegas\",\n"
                + "  \"bCountry\": \"US\",\n"
                + "  \"bFax\": \"5551234567\",\n"
                + "  \"binPerson\": 60,\n"
                + "  \"binPhone\": 20,\n"
                + "  \"binWeb\": 20,\n"
                + "  \"boardingLinkId\": 91,\n"
                + "  \"boardingStatus\": 1,\n"
                + "  \"boardingSubStatus\": 1,\n"
                + "  \"bPhone\": \"5551234567\",\n"
                + "  \"bStartdate\": \"01/01/1990\",\n"
                + "  \"bState\": \"FL\",\n"
                + "  \"bSummary\": \"Brick and mortar store that sells office supplies\",\n"
                + "  \"builderData\": {\n"
                + "    \"attributes\": {\n"
                + "      \"minimumDocuments\": 1,\n"
                + "      \"multipleContacts\": true,\n"
                + "      \"multipleOwners\": true\n"
                + "    }\n"
                + "  },\n"
                + "  \"bZip\": \"33000\",\n"
                + "  \"contactData\": [\n"
                + "    {\n"
                + "      \"contactEmail\": \"example@email.com\",\n"
                + "      \"contactName\": \"Herman Martinez\",\n"
                + "      \"contactPhone\": \"3055550000\",\n"
                + "      \"contactTitle\": \"Owner\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"dbaName\": \"Sunshine Gutters\",\n"
                + "  \"documentsRef\": {\n"
                + "    \"filelist\": [\n"
                + "      {}\n"
                + "    ],\n"
                + "    \"zipfile\": \"zx45.zip\"\n"
                + "  },\n"
                + "  \"ein\": \"123456789\",\n"
                + "  \"externalPaypointId\": \"Paypoint-100\",\n"
                + "  \"generalEvents\": [\n"
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
                + "  \"highTicketAmount\": 1000,\n"
                + "  \"idApplication\": 352,\n"
                + "  \"lastModified\": \"2022-07-01T15:00:01Z\",\n"
                + "  \"legalName\": \"Sunshine Services, LLC\",\n"
                + "  \"license\": \"2222222FFG\",\n"
                + "  \"licenseState\": \"CA\",\n"
                + "  \"logo\": {\n"
                + "    \"fContent\": \"TXkgdGVzdCBmaWxlHJ==...\",\n"
                + "    \"filename\": \"my-doc.pdf\",\n"
                + "    \"ftype\": \"pdf\",\n"
                + "    \"furl\": \"https://mysite.com/my-doc.pdf\"\n"
                + "  },\n"
                + "  \"mAddress1\": \"123 Walnut Street\",\n"
                + "  \"mAddress2\": \"STE 900\",\n"
                + "  \"mccid\": \"mccid\",\n"
                + "  \"mCity\": \"TN\",\n"
                + "  \"mCountry\": \"US\",\n"
                + "  \"mState\": \"TN\",\n"
                + "  \"mZip\": \"37615\",\n"
                + "  \"orgId\": 123,\n"
                + "  \"orgParentName\": \"PropertyManager Pro\",\n"
                + "  \"ownerData\": [\n"
                + "    {\n"
                + "      \"oaddress\": \"33 North St\",\n"
                + "      \"ocity\": \"Any City\",\n"
                + "      \"ocountry\": \"US\",\n"
                + "      \"odriverstate\": \"CA\",\n"
                + "      \"ostate\": \"CA\",\n"
                + "      \"ownerdob\": \"01/01/1990\",\n"
                + "      \"ownerdriver\": \"CA6677778\",\n"
                + "      \"owneremail\": \"example@email.com\",\n"
                + "      \"ownername\": \"John Smith\",\n"
                + "      \"ownerpercent\": 25,\n"
                + "      \"ownerphone1\": \"555888111\",\n"
                + "      \"ownerphone2\": \"555888111\",\n"
                + "      \"ownerssn\": \"123456789\",\n"
                + "      \"ownertitle\": \"CEO\",\n"
                + "      \"ozip\": \"55555\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"ownType\": \"Limited Liability Company\",\n"
                + "  \"pageidentifier\": \"null\",\n"
                + "  \"recipientEmailNotification\": true,\n"
                + "  \"resumable\": false,\n"
                + "  \"salesCode\": \"salesCode\",\n"
                + "  \"serviceData\": {\n"
                + "    \"ach\": {\n"
                + "      \"acceptCCD\": false,\n"
                + "      \"acceptPPD\": false,\n"
                + "      \"acceptWeb\": true\n"
                + "    },\n"
                + "    \"card\": {\n"
                + "      \"acceptAmex\": true,\n"
                + "      \"acceptDiscover\": false,\n"
                + "      \"acceptMastercard\": true,\n"
                + "      \"acceptVisa\": true\n"
                + "    },\n"
                + "    \"odp\": {\n"
                + "      \"allowAch\": true,\n"
                + "      \"allowChecks\": true,\n"
                + "      \"allowVCard\": true,\n"
                + "      \"processing_region\": \"US\",\n"
                + "      \"processor\": \"tysys\",\n"
                + "      \"issuerNetworkSettingsId\": \"12345678901234\"\n"
                + "    }\n"
                + "  },\n"
                + "  \"signer\": {\n"
                + "    \"acceptance\": false,\n"
                + "    \"address\": \"33 North St\",\n"
                + "    \"address1\": \"STE 900\",\n"
                + "    \"city\": \"Bristol\",\n"
                + "    \"country\": \"US\",\n"
                + "    \"dob\": \"01/01/1990\",\n"
                + "    \"email\": \"example@email.com\",\n"
                + "    \"name\": \"John Smith\",\n"
                + "    \"phone\": \"555888111\",\n"
                + "    \"signedDocumentReference\": \"signedDocumentReference\",\n"
                + "    \"signerUuid\": \"54455d5d-34ff-416c-91e0-5bc87199999\",\n"
                + "    \"ssn\": \"123456789\",\n"
                + "    \"state\": \"TN\",\n"
                + "    \"zip\": \"55555\"\n"
                + "  },\n"
                + "  \"taxfillname\": \"Sunshine LLC\",\n"
                + "  \"templateId\": 22,\n"
                + "  \"websiteAddress\": \"www.example.com\",\n"
                + "  \"whencharged\": \"When Service Provided\",\n"
                + "  \"whendelivered\": \"0-7 Days\",\n"
                + "  \"whenProvided\": \"30 Days or Less\",\n"
                + "  \"whenrefund\": \"Exchange Only\"\n"
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
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"Records\":[{\"annualRevenue\":1000,\"averageMonthlyVolume\":1000,\"averageTicketAmount\":1000,\"bAddress1\":\"123 Walnut Street\",\"bAddress2\":\"Suite 103\",\"bankData\":[{\"bankAccountFunction\":0,\"bankAccountHolderName\":\"Gruzya Adventure Outfitters LLC\",\"nickname\":\"Business Checking 1234\"}],\"bCity\":\"New Vegas\",\"bCountry\":\"US\",\"bFax\":\"5551234567\",\"binPerson\":60,\"binPhone\":20,\"binWeb\":20,\"boardingLinkId\":91,\"boardingStatus\":1,\"boardingSubStatus\":1,\"bPhone\":\"5551234567\",\"bStartdate\":\"01/01/1990\",\"bState\":\"FL\",\"bSummary\":\"Brick and mortar store that sells office supplies\",\"bZip\":\"33000\",\"contactData\":[{}],\"createdAt\":\"2022-07-01T15:00:01Z\",\"dbaName\":\"Sunshine Gutters\",\"ein\":\"123456789\",\"externalPaypointId\":\"Paypoint-100\",\"generalEvents\":[{\"description\":\"TransferCreated\",\"eventTime\":\"2023-07-05T22:31:06Z\"}],\"highTicketAmount\":1000,\"idApplication\":352,\"lastModified\":\"2022-07-01T15:00:01Z\",\"legalName\":\"Sunshine Services, LLC\",\"license\":\"2222222FFG\",\"licenseState\":\"CA\",\"mAddress1\":\"123 Walnut Street\",\"mAddress2\":\"STE 900\",\"mccid\":\"mccid\",\"mCity\":\"TN\",\"mCountry\":\"US\",\"mState\":\"TN\",\"mZip\":\"37615\",\"orgId\":123,\"orgParentName\":\"PropertyManager Pro\",\"ownerData\":[{}],\"ownType\":\"Limited Liability Company\",\"pageidentifier\":\"null\",\"recipientEmailNotification\":true,\"resumable\":false,\"salesCode\":\"salesCode\",\"taxfillname\":\"Sunshine LLC\",\"templateId\":22,\"websiteAddress\":\"www.example.com\",\"whencharged\":\"When Service Provided\",\"whendelivered\":\"0-7 Days\",\"whenProvided\":\"30 Days or Less\",\"whenrefund\":\"Exchange Only\"}],\"Summary\":{\"pageIdentifier\":\"null\",\"pageSize\":20,\"totalAmount\":77.22,\"totalNetAmount\":77.22,\"totalPages\":2,\"totalRecords\":2}}"));
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
        String expectedResponseBody = ""
                + "{\n"
                + "  \"Records\": [\n"
                + "    {\n"
                + "      \"annualRevenue\": 1000,\n"
                + "      \"averageMonthlyVolume\": 1000,\n"
                + "      \"averageTicketAmount\": 1000,\n"
                + "      \"bAddress1\": \"123 Walnut Street\",\n"
                + "      \"bAddress2\": \"Suite 103\",\n"
                + "      \"bankData\": [\n"
                + "        {\n"
                + "          \"bankAccountFunction\": 0,\n"
                + "          \"bankAccountHolderName\": \"Gruzya Adventure Outfitters LLC\",\n"
                + "          \"nickname\": \"Business Checking 1234\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"bCity\": \"New Vegas\",\n"
                + "      \"bCountry\": \"US\",\n"
                + "      \"bFax\": \"5551234567\",\n"
                + "      \"binPerson\": 60,\n"
                + "      \"binPhone\": 20,\n"
                + "      \"binWeb\": 20,\n"
                + "      \"boardingLinkId\": 91,\n"
                + "      \"boardingStatus\": 1,\n"
                + "      \"boardingSubStatus\": 1,\n"
                + "      \"bPhone\": \"5551234567\",\n"
                + "      \"bStartdate\": \"01/01/1990\",\n"
                + "      \"bState\": \"FL\",\n"
                + "      \"bSummary\": \"Brick and mortar store that sells office supplies\",\n"
                + "      \"bZip\": \"33000\",\n"
                + "      \"contactData\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"createdAt\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"dbaName\": \"Sunshine Gutters\",\n"
                + "      \"ein\": \"123456789\",\n"
                + "      \"externalPaypointId\": \"Paypoint-100\",\n"
                + "      \"generalEvents\": [\n"
                + "        {\n"
                + "          \"description\": \"TransferCreated\",\n"
                + "          \"eventTime\": \"2023-07-05T22:31:06Z\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"highTicketAmount\": 1000,\n"
                + "      \"idApplication\": 352,\n"
                + "      \"lastModified\": \"2022-07-01T15:00:01Z\",\n"
                + "      \"legalName\": \"Sunshine Services, LLC\",\n"
                + "      \"license\": \"2222222FFG\",\n"
                + "      \"licenseState\": \"CA\",\n"
                + "      \"mAddress1\": \"123 Walnut Street\",\n"
                + "      \"mAddress2\": \"STE 900\",\n"
                + "      \"mccid\": \"mccid\",\n"
                + "      \"mCity\": \"TN\",\n"
                + "      \"mCountry\": \"US\",\n"
                + "      \"mState\": \"TN\",\n"
                + "      \"mZip\": \"37615\",\n"
                + "      \"orgId\": 123,\n"
                + "      \"orgParentName\": \"PropertyManager Pro\",\n"
                + "      \"ownerData\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"ownType\": \"Limited Liability Company\",\n"
                + "      \"pageidentifier\": \"null\",\n"
                + "      \"recipientEmailNotification\": true,\n"
                + "      \"resumable\": false,\n"
                + "      \"salesCode\": \"salesCode\",\n"
                + "      \"taxfillname\": \"Sunshine LLC\",\n"
                + "      \"templateId\": 22,\n"
                + "      \"websiteAddress\": \"www.example.com\",\n"
                + "      \"whencharged\": \"When Service Provided\",\n"
                + "      \"whendelivered\": \"0-7 Days\",\n"
                + "      \"whenProvided\": \"30 Days or Less\",\n"
                + "      \"whenrefund\": \"Exchange Only\"\n"
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
