package io.github.payabli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.payabli.api.core.ObjectMappers;
import io.github.payabli.api.resources.paymentlink.requests.PayLinkDataBill;
import io.github.payabli.api.resources.paymentlink.requests.PayLinkDataInvoice;
import io.github.payabli.api.resources.paymentlink.requests.PayLinkDataOut;
import io.github.payabli.api.resources.paymentlink.requests.PayLinkUpdateData;
import io.github.payabli.api.resources.paymentlink.requests.RefreshPayLinkFromIdRequest;
import io.github.payabli.api.resources.paymentlink.requests.SendPayLinkFromIdRequest;
import io.github.payabli.api.resources.paymentlink.types.GetPayLinkFromIdResponse;
import io.github.payabli.api.resources.paymentlink.types.PayabliApiResponsePaymentLinks;
import io.github.payabli.api.resources.paymentlink.types.PaymentPageRequestBody;
import io.github.payabli.api.types.ContactElement;
import io.github.payabli.api.types.Element;
import io.github.payabli.api.types.FileContent;
import io.github.payabli.api.types.FileContentFtype;
import io.github.payabli.api.types.HeaderElement;
import io.github.payabli.api.types.InvoiceElement;
import io.github.payabli.api.types.LabelElement;
import io.github.payabli.api.types.MethodElement;
import io.github.payabli.api.types.MethodElementSettings;
import io.github.payabli.api.types.MethodElementSettingsApplePay;
import io.github.payabli.api.types.MethodElementSettingsApplePayButtonStyle;
import io.github.payabli.api.types.MethodElementSettingsApplePayButtonType;
import io.github.payabli.api.types.MethodElementSettingsApplePayLanguage;
import io.github.payabli.api.types.MethodsList;
import io.github.payabli.api.types.NoteElement;
import io.github.payabli.api.types.PageElement;
import io.github.payabli.api.types.PagelinkSetting;
import io.github.payabli.api.types.PayorElement;
import io.github.payabli.api.types.PayorFields;
import io.github.payabli.api.types.PushPayLinkRequest;
import io.github.payabli.api.types.PushPayLinkRequestSms;
import java.util.Arrays;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentLinkWireTest {
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
    public void testAddPayLinkFromInvoice() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink()
                .addPayLinkFromInvoice(
                        23548884,
                        PayLinkDataInvoice.builder()
                                .body(PaymentPageRequestBody.builder()
                                        .contactUs(ContactElement.builder()
                                                .emailLabel("Email")
                                                .enabled(true)
                                                .header("Contact Us")
                                                .order(0)
                                                .paymentIcons(true)
                                                .phoneLabel("Phone")
                                                .build())
                                        .invoices(InvoiceElement.builder()
                                                .enabled(true)
                                                .invoiceLink(LabelElement.builder()
                                                        .enabled(true)
                                                        .label("View Invoice")
                                                        .order(0)
                                                        .build())
                                                .order(0)
                                                .viewInvoiceDetails(LabelElement.builder()
                                                        .enabled(true)
                                                        .label("Invoice Details")
                                                        .order(0)
                                                        .build())
                                                .build())
                                        .logo(Element.builder()
                                                .enabled(true)
                                                .order(0)
                                                .build())
                                        .messageBeforePaying(LabelElement.builder()
                                                .enabled(true)
                                                .label("Please review your payment details")
                                                .order(0)
                                                .build())
                                        .notes(NoteElement.builder()
                                                .enabled(true)
                                                .header("Additional Notes")
                                                .order(0)
                                                .placeholder("Enter any additional notes here")
                                                .value("")
                                                .build())
                                        .page(PageElement.builder()
                                                .description("Complete your payment securely")
                                                .enabled(true)
                                                .header("Payment Page")
                                                .order(0)
                                                .build())
                                        .paymentButton(LabelElement.builder()
                                                .enabled(true)
                                                .label("Pay Now")
                                                .order(0)
                                                .build())
                                        .paymentMethods(MethodElement.builder()
                                                .allMethodsChecked(true)
                                                .enabled(true)
                                                .header("Payment Methods")
                                                .methods(MethodsList.builder()
                                                        .amex(true)
                                                        .applePay(true)
                                                        .discover(true)
                                                        .eCheck(true)
                                                        .mastercard(true)
                                                        .visa(true)
                                                        .build())
                                                .order(0)
                                                .settings(MethodElementSettings.builder()
                                                        .applePay(MethodElementSettingsApplePay.builder()
                                                                .buttonStyle(
                                                                        MethodElementSettingsApplePayButtonStyle.BLACK)
                                                                .buttonType(MethodElementSettingsApplePayButtonType.PAY)
                                                                .language(MethodElementSettingsApplePayLanguage.EN_US)
                                                                .build())
                                                        .build())
                                                .build())
                                        .payor(PayorElement.builder()
                                                .enabled(true)
                                                .fields(Optional.of(Arrays.asList(PayorFields.builder()
                                                        .display(true)
                                                        .fixed(true)
                                                        .identifier(true)
                                                        .label("Full Name")
                                                        .name("fullName")
                                                        .order(0)
                                                        .required(true)
                                                        .validation("^[a-zA-Z ]+$")
                                                        .value("")
                                                        .width(0)
                                                        .build())))
                                                .header("Payor Information")
                                                .order(0)
                                                .build())
                                        .review(HeaderElement.builder()
                                                .enabled(true)
                                                .header("Review Payment")
                                                .order(0)
                                                .build())
                                        .settings(PagelinkSetting.builder()
                                                .color("#000000")
                                                .customCssUrl("https://example.com/custom.css")
                                                .language("en")
                                                .pageLogo(FileContent.builder()
                                                        .fContent(
                                                                "PHN2ZyB2aWV3Qm94PSIwIDAgODAwIDEwMDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiAgPCEtLSBCYWNrZ3JvdW5kIC0tPgogIDxyZWN0IHdpZHRoPSI4MDAiIGhlaWdodD0iMTAwMCIgZmlsbD0id2hpdGUiLz4KICAKICA8IS0tIENvbXBhbnkgSGVhZGVyIC0tPgogIDx0ZXh0IHg9IjQwIiB5PSI2MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjI0IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+R3J1enlhIEFkdmVudHVyZSBPdXRmaXR0ZXJzPC90ZXh0PgogIDxsaW5lIHgxPSI0MCIgeTE9IjgwIiB4Mj0iNzYwIiB5Mj0iODAiIHN0cm9rZT0iIzJjM2U1MCIgc3Ryb2tlLXdpZHRoPSIyIi8+CiAgCiAgPCEtLSBDb21wYW55IERldGFpbHMgLS0+CiAgPHRleHQgeD0iNDAiIHk9IjExMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4xMjMgTW91bnRhaW4gVmlldyBSb2FkPC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSIxMzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+VGJpbGlzaSwgR2VvcmdpYSAwMTA1PC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSIxNTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+VGVsOiArOTk1IDMyIDEyMyA0NTY3PC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSIxNzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+RW1haWw6IGluZm9AZ3J1enlhYWR2ZW50dXJlcy5jb208L3RleHQ+CgogIDwhLS0gSW52b2ljZSBUaXRsZSAtLT4KICA8dGV4dCB4PSI2MDAiIHk9IjExMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjI0IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+SU5WT0lDRTwvdGV4dD4KICA8dGV4dCB4PSI2MDAiIHk9IjE0MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj5EYXRlOiAxMi8xMS8yMDI0PC90ZXh0PgogIDx0ZXh0IHg9IjYwMCIgeT0iMTYwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPkludm9pY2UgIzogR1JaLTIwMjQtMTEyMzwvdGV4dD4KCiAgPCEtLSBCaWxsIFRvIFNlY3Rpb24gLS0+CiAgPHRleHQgeD0iNDAiIHk9IjIyMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE2IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+QklMTCBUTzo8L3RleHQ+CiAgPHJlY3QgeD0iNDAiIHk9IjIzNSIgd2lkdGg9IjMwMCIgaGVpZ2h0PSI4MCIgZmlsbD0iI2Y3ZjlmYSIvPgogIDx0ZXh0IHg9IjUwIiB5PSIyNjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+W0N1c3RvbWVyIE5hbWVdPC90ZXh0PgogIDx0ZXh0IHg9IjUwIiB5PSIyODAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+W0FkZHJlc3MgTGluZSAxXTwvdGV4dD4KICA8dGV4dCB4PSI1MCIgeT0iMzAwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPltDaXR5LCBDb3VudHJ5XTwvdGV4dD4KCiAgPCEtLSBUYWJsZSBIZWFkZXJzIC0tPgogIDxyZWN0IHg9IjQwIiB5PSIzNDAiIHdpZHRoPSI3MjAiIGhlaWdodD0iMzAiIGZpbGw9IiMyYzNlNTAiLz4KICA8dGV4dCB4PSI1MCIgeT0iMzYwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZvbnQtd2VpZ2h0PSJib2xkIiBmaWxsPSJ3aGl0ZSI+RGVzY3JpcHRpb248L3RleHQ+CiAgPHRleHQgeD0iNDUwIiB5PSIzNjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IndoaXRlIj5RdWFudGl0eTwvdGV4dD4KICA8dGV4dCB4PSI1NTAiIHk9IjM2MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0id2hpdGUiPlJhdGU8L3RleHQ+CiAgPHRleHQgeD0iNjgwIiB5PSIzNjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IndoaXRlIj5BbW91bnQ8L3RleHQ+CgogIDwhLS0gVGFibGUgUm93cyAtLT4KICA8cmVjdCB4PSI0MCIgeT0iMzcwIiB3aWR0aD0iNzIwIiBoZWlnaHQ9IjMwIiBmaWxsPSIjZjdmOWZhIi8+CiAgPHRleHQgeD0iNTAiIHk9IjM5MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj5Nb3VudGFpbiBDbGltYmluZyBFcXVpcG1lbnQgUmVudGFsPC90ZXh0PgogIDx0ZXh0IHg9IjQ1MCIgeT0iMzkwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPjE8L3RleHQ+CiAgPHRleHQgeD0iNTUwIiB5PSIzOTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+JDI1MC4wMDwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjM5MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4kMjUwLjAwPC90ZXh0PgoKICA8cmVjdCB4PSI0MCIgeT0iNDAwIiB3aWR0aD0iNzIwIiBoZWlnaHQ9IjMwIiBmaWxsPSJ3aGl0ZSIvPgogIDx0ZXh0IHg9IjUwIiB5PSI0MjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+R3VpZGVkIFRyZWsgUGFja2FnZSAtIDIgRGF5czwvdGV4dD4KICA8dGV4dCB4PSI0NTAiIHk9IjQyMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4xPC90ZXh0PgogIDx0ZXh0IHg9IjU1MCIgeT0iNDIwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPiQ0MDAuMDA8L3RleHQ+CiAgPHRleHQgeD0iNjgwIiB5PSI0MjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+JDQwMC4wMDwvdGV4dD4KCiAgPHJlY3QgeD0iNDAiIHk9IjQzMCIgd2lkdGg9IjcyMCIgaGVpZ2h0PSIzMCIgZmlsbD0iI2Y3ZjlmYSIvPgogIDx0ZXh0IHg9IjUwIiB5PSI0NTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+U2FmZXR5IEVxdWlwbWVudCBQYWNrYWdlPC90ZXh0PgogIDx0ZXh0IHg9IjQ1MCIgeT0iNDUwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPjE8L3RleHQ+CiAgPHRleHQgeD0iNTUwIiB5PSI0NTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+JDE1MC4wMDwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjQ1MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4kMTUwLjAwPC90ZXh0PgoKICA8IS0tIFRvdGFscyAtLT4KICA8bGluZSB4MT0iNDAiIHkxPSI0ODAiIHgyPSI3NjAiIHkyPSI0ODAiIHN0cm9rZT0iIzJjM2U1MCIgc3Ryb2tlLXdpZHRoPSIxIi8+CiAgPHRleHQgeD0iNTUwIiB5PSI1MTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMzNDQ5NWUiPlN1YnRvdGFsOjwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjUxMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4kODAwLjAwPC90ZXh0PgogIDx0ZXh0IHg9IjU1MCIgeT0iNTM1IiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZvbnQtd2VpZ2h0PSJib2xkIiBmaWxsPSIjMzQ0OTVlIj5UYXggKDE4JSk6PC90ZXh0PgogIDx0ZXh0IHg9IjY4MCIgeT0iNTM1IiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPiQxNDQuMDA8L3RleHQ+CiAgPHRleHQgeD0iNTUwIiB5PSI1NzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMyYzNlNTAiPlRvdGFsOjwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjU3MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE2IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+JDk0NC4wMDwvdGV4dD4KCiAgPCEtLSBQYXltZW50IFRlcm1zIC0tPgogIDx0ZXh0IHg9IjQwIiB5PSI2NDAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMyYzNlNTAiPlBheW1lbnQgVGVybXM8L3RleHQ+CiAgPHRleHQgeD0iNDAiIHk9IjY3MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj5QYXltZW50IGlzIGR1ZSB3aXRoaW4gMzAgZGF5czwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iNjkwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPlBsZWFzZSBpbmNsdWRlIGludm9pY2UgbnVtYmVyIG9uIHBheW1lbnQ8L3RleHQ+CgogIDwhLS0gQmFuayBEZXRhaWxzIC0tPgogIDx0ZXh0IHg9IjQwIiB5PSI3MzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMyYzNlNTAiPkJhbmsgRGV0YWlsczwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iNzYwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPkJhbms6IEJhbmsgb2YgR2VvcmdpYTwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iNzgwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPklCQU46IEdFMTIzNDU2Nzg5MDEyMzQ1Njc4PC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSI4MDAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+U1dJRlQ6IEJBR0FHRTIyPC90ZXh0PgoKICA8IS0tIEZvb3RlciAtLT4KICA8bGluZSB4MT0iNDAiIHkxPSI5MDAiIHgyPSI3NjAiIHkyPSI5MDAiIHN0cm9rZT0iIzJjM2U1MCIgc3Ryb2tlLXdpZHRoPSIxIi8+CiAgPHRleHQgeD0iNDAiIHk9IjkzMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjEyIiBmaWxsPSIjN2Y4YzhkIj5UaGFuayB5b3UgZm9yIGNob29zaW5nIEdydXp5YSBBZHZlbnR1cmUgT3V0Zml0dGVyczwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iOTUwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTIiIGZpbGw9IiM3ZjhjOGQiPnd3dy5ncnV6eWFhZHZlbnR1cmVzLmNvbTwvdGV4dD4KPC9zdmc+Cg==")
                                                        .filename("logo.jpg")
                                                        .ftype(FileContentFtype.JPG)
                                                        .furl("")
                                                        .build())
                                                .redirectAfterApprove(true)
                                                .redirectAfterApproveUrl("https://example.com/success")
                                                .build())
                                        .build())
                                .mail2("jo@example.com; ceo@example.com")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"contactUs\": {\n"
                + "    \"emailLabel\": \"Email\",\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Contact Us\",\n"
                + "    \"order\": 0,\n"
                + "    \"paymentIcons\": true,\n"
                + "    \"phoneLabel\": \"Phone\"\n"
                + "  },\n"
                + "  \"invoices\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"invoiceLink\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"label\": \"View Invoice\",\n"
                + "      \"order\": 0\n"
                + "    },\n"
                + "    \"order\": 0,\n"
                + "    \"viewInvoiceDetails\": {\n"
                + "      \"enabled\": true,\n"
                + "      \"label\": \"Invoice Details\",\n"
                + "      \"order\": 0\n"
                + "    }\n"
                + "  },\n"
                + "  \"logo\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"messageBeforePaying\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"label\": \"Please review your payment details\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"notes\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Additional Notes\",\n"
                + "    \"order\": 0,\n"
                + "    \"placeholder\": \"Enter any additional notes here\",\n"
                + "    \"value\": \"\"\n"
                + "  },\n"
                + "  \"page\": {\n"
                + "    \"description\": \"Complete your payment securely\",\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Payment Page\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"paymentButton\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"label\": \"Pay Now\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"paymentMethods\": {\n"
                + "    \"allMethodsChecked\": true,\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Payment Methods\",\n"
                + "    \"methods\": {\n"
                + "      \"amex\": true,\n"
                + "      \"applePay\": true,\n"
                + "      \"discover\": true,\n"
                + "      \"eCheck\": true,\n"
                + "      \"mastercard\": true,\n"
                + "      \"visa\": true\n"
                + "    },\n"
                + "    \"order\": 0,\n"
                + "    \"settings\": {\n"
                + "      \"applePay\": {\n"
                + "        \"buttonStyle\": \"black\",\n"
                + "        \"buttonType\": \"pay\",\n"
                + "        \"language\": \"en-US\"\n"
                + "      }\n"
                + "    }\n"
                + "  },\n"
                + "  \"payor\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"fields\": [\n"
                + "      {\n"
                + "        \"display\": true,\n"
                + "        \"fixed\": true,\n"
                + "        \"identifier\": true,\n"
                + "        \"label\": \"Full Name\",\n"
                + "        \"name\": \"fullName\",\n"
                + "        \"order\": 0,\n"
                + "        \"required\": true,\n"
                + "        \"validation\": \"^[a-zA-Z ]+$\",\n"
                + "        \"value\": \"\",\n"
                + "        \"width\": 0\n"
                + "      }\n"
                + "    ],\n"
                + "    \"header\": \"Payor Information\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"review\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Review Payment\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"settings\": {\n"
                + "    \"color\": \"#000000\",\n"
                + "    \"customCssUrl\": \"https://example.com/custom.css\",\n"
                + "    \"language\": \"en\",\n"
                + "    \"pageLogo\": {\n"
                + "      \"fContent\": \"PHN2ZyB2aWV3Qm94PSIwIDAgODAwIDEwMDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiAgPCEtLSBCYWNrZ3JvdW5kIC0tPgogIDxyZWN0IHdpZHRoPSI4MDAiIGhlaWdodD0iMTAwMCIgZmlsbD0id2hpdGUiLz4KICAKICA8IS0tIENvbXBhbnkgSGVhZGVyIC0tPgogIDx0ZXh0IHg9IjQwIiB5PSI2MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjI0IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+R3J1enlhIEFkdmVudHVyZSBPdXRmaXR0ZXJzPC90ZXh0PgogIDxsaW5lIHgxPSI0MCIgeTE9IjgwIiB4Mj0iNzYwIiB5Mj0iODAiIHN0cm9rZT0iIzJjM2U1MCIgc3Ryb2tlLXdpZHRoPSIyIi8+CiAgCiAgPCEtLSBDb21wYW55IERldGFpbHMgLS0+CiAgPHRleHQgeD0iNDAiIHk9IjExMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4xMjMgTW91bnRhaW4gVmlldyBSb2FkPC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSIxMzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+VGJpbGlzaSwgR2VvcmdpYSAwMTA1PC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSIxNTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+VGVsOiArOTk1IDMyIDEyMyA0NTY3PC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSIxNzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+RW1haWw6IGluZm9AZ3J1enlhYWR2ZW50dXJlcy5jb208L3RleHQ+CgogIDwhLS0gSW52b2ljZSBUaXRsZSAtLT4KICA8dGV4dCB4PSI2MDAiIHk9IjExMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjI0IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+SU5WT0lDRTwvdGV4dD4KICA8dGV4dCB4PSI2MDAiIHk9IjE0MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj5EYXRlOiAxMi8xMS8yMDI0PC90ZXh0PgogIDx0ZXh0IHg9IjYwMCIgeT0iMTYwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPkludm9pY2UgIzogR1JaLTIwMjQtMTEyMzwvdGV4dD4KCiAgPCEtLSBCaWxsIFRvIFNlY3Rpb24gLS0+CiAgPHRleHQgeD0iNDAiIHk9IjIyMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE2IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+QklMTCBUTzo8L3RleHQ+CiAgPHJlY3QgeD0iNDAiIHk9IjIzNSIgd2lkdGg9IjMwMCIgaGVpZ2h0PSI4MCIgZmlsbD0iI2Y3ZjlmYSIvPgogIDx0ZXh0IHg9IjUwIiB5PSIyNjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+W0N1c3RvbWVyIE5hbWVdPC90ZXh0PgogIDx0ZXh0IHg9IjUwIiB5PSIyODAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+W0FkZHJlc3MgTGluZSAxXTwvdGV4dD4KICA8dGV4dCB4PSI1MCIgeT0iMzAwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPltDaXR5LCBDb3VudHJ5XTwvdGV4dD4KCiAgPCEtLSBUYWJsZSBIZWFkZXJzIC0tPgogIDxyZWN0IHg9IjQwIiB5PSIzNDAiIHdpZHRoPSI3MjAiIGhlaWdodD0iMzAiIGZpbGw9IiMyYzNlNTAiLz4KICA8dGV4dCB4PSI1MCIgeT0iMzYwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZvbnQtd2VpZ2h0PSJib2xkIiBmaWxsPSJ3aGl0ZSI+RGVzY3JpcHRpb248L3RleHQ+CiAgPHRleHQgeD0iNDUwIiB5PSIzNjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IndoaXRlIj5RdWFudGl0eTwvdGV4dD4KICA8dGV4dCB4PSI1NTAiIHk9IjM2MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0id2hpdGUiPlJhdGU8L3RleHQ+CiAgPHRleHQgeD0iNjgwIiB5PSIzNjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IndoaXRlIj5BbW91bnQ8L3RleHQ+CgogIDwhLS0gVGFibGUgUm93cyAtLT4KICA8cmVjdCB4PSI0MCIgeT0iMzcwIiB3aWR0aD0iNzIwIiBoZWlnaHQ9IjMwIiBmaWxsPSIjZjdmOWZhIi8+CiAgPHRleHQgeD0iNTAiIHk9IjM5MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj5Nb3VudGFpbiBDbGltYmluZyBFcXVpcG1lbnQgUmVudGFsPC90ZXh0PgogIDx0ZXh0IHg9IjQ1MCIgeT0iMzkwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPjE8L3RleHQ+CiAgPHRleHQgeD0iNTUwIiB5PSIzOTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+JDI1MC4wMDwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjM5MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4kMjUwLjAwPC90ZXh0PgoKICA8cmVjdCB4PSI0MCIgeT0iNDAwIiB3aWR0aD0iNzIwIiBoZWlnaHQ9IjMwIiBmaWxsPSJ3aGl0ZSIvPgogIDx0ZXh0IHg9IjUwIiB5PSI0MjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+R3VpZGVkIFRyZWsgUGFja2FnZSAtIDIgRGF5czwvdGV4dD4KICA8dGV4dCB4PSI0NTAiIHk9IjQyMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4xPC90ZXh0PgogIDx0ZXh0IHg9IjU1MCIgeT0iNDIwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPiQ0MDAuMDA8L3RleHQ+CiAgPHRleHQgeD0iNjgwIiB5PSI0MjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+JDQwMC4wMDwvdGV4dD4KCiAgPHJlY3QgeD0iNDAiIHk9IjQzMCIgd2lkdGg9IjcyMCIgaGVpZ2h0PSIzMCIgZmlsbD0iI2Y3ZjlmYSIvPgogIDx0ZXh0IHg9IjUwIiB5PSI0NTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+U2FmZXR5IEVxdWlwbWVudCBQYWNrYWdlPC90ZXh0PgogIDx0ZXh0IHg9IjQ1MCIgeT0iNDUwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPjE8L3RleHQ+CiAgPHRleHQgeD0iNTUwIiB5PSI0NTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+JDE1MC4wMDwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjQ1MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4kMTUwLjAwPC90ZXh0PgoKICA8IS0tIFRvdGFscyAtLT4KICA8bGluZSB4MT0iNDAiIHkxPSI0ODAiIHgyPSI3NjAiIHkyPSI0ODAiIHN0cm9rZT0iIzJjM2U1MCIgc3Ryb2tlLXdpZHRoPSIxIi8+CiAgPHRleHQgeD0iNTUwIiB5PSI1MTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMzNDQ5NWUiPlN1YnRvdGFsOjwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjUxMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj4kODAwLjAwPC90ZXh0PgogIDx0ZXh0IHg9IjU1MCIgeT0iNTM1IiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZvbnQtd2VpZ2h0PSJib2xkIiBmaWxsPSIjMzQ0OTVlIj5UYXggKDE4JSk6PC90ZXh0PgogIDx0ZXh0IHg9IjY4MCIgeT0iNTM1IiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPiQxNDQuMDA8L3RleHQ+CiAgPHRleHQgeD0iNTUwIiB5PSI1NzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMyYzNlNTAiPlRvdGFsOjwvdGV4dD4KICA8dGV4dCB4PSI2ODAiIHk9IjU3MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE2IiBmb250LXdlaWdodD0iYm9sZCIgZmlsbD0iIzJjM2U1MCI+JDk0NC4wMDwvdGV4dD4KCiAgPCEtLSBQYXltZW50IFRlcm1zIC0tPgogIDx0ZXh0IHg9IjQwIiB5PSI2NDAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMyYzNlNTAiPlBheW1lbnQgVGVybXM8L3RleHQ+CiAgPHRleHQgeD0iNDAiIHk9IjY3MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSIjMzQ0OTVlIj5QYXltZW50IGlzIGR1ZSB3aXRoaW4gMzAgZGF5czwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iNjkwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPlBsZWFzZSBpbmNsdWRlIGludm9pY2UgbnVtYmVyIG9uIHBheW1lbnQ8L3RleHQ+CgogIDwhLS0gQmFuayBEZXRhaWxzIC0tPgogIDx0ZXh0IHg9IjQwIiB5PSI3MzAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IiMyYzNlNTAiPkJhbmsgRGV0YWlsczwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iNzYwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPkJhbms6IEJhbmsgb2YgR2VvcmdpYTwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iNzgwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiMzNDQ5NWUiPklCQU46IEdFMTIzNDU2Nzg5MDEyMzQ1Njc4PC90ZXh0PgogIDx0ZXh0IHg9IjQwIiB5PSI4MDAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzM0NDk1ZSI+U1dJRlQ6IEJBR0FHRTIyPC90ZXh0PgoKICA8IS0tIEZvb3RlciAtLT4KICA8bGluZSB4MT0iNDAiIHkxPSI5MDAiIHgyPSI3NjAiIHkyPSI5MDAiIHN0cm9rZT0iIzJjM2U1MCIgc3Ryb2tlLXdpZHRoPSIxIi8+CiAgPHRleHQgeD0iNDAiIHk9IjkzMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjEyIiBmaWxsPSIjN2Y4YzhkIj5UaGFuayB5b3UgZm9yIGNob29zaW5nIEdydXp5YSBBZHZlbnR1cmUgT3V0Zml0dGVyczwvdGV4dD4KICA8dGV4dCB4PSI0MCIgeT0iOTUwIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTIiIGZpbGw9IiM3ZjhjOGQiPnd3dy5ncnV6eWFhZHZlbnR1cmVzLmNvbTwvdGV4dD4KPC9zdmc+Cg==\",\n"
                + "      \"filename\": \"logo.jpg\",\n"
                + "      \"ftype\": \"jpg\",\n"
                + "      \"furl\": \"\"\n"
                + "    },\n"
                + "    \"redirectAfterApprove\": true,\n"
                + "    \"redirectAfterApproveUrl\": \"https://example.com/success\"\n"
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
                + "  \"responseData\": \"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\n"
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
    public void testAddPayLinkFromBill() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink()
                .addPayLinkFromBill(
                        23548884,
                        PayLinkDataBill.builder()
                                .body(PaymentPageRequestBody.builder()
                                        .contactUs(ContactElement.builder()
                                                .emailLabel("Email")
                                                .enabled(true)
                                                .header("Contact Us")
                                                .order(0)
                                                .paymentIcons(true)
                                                .phoneLabel("Phone")
                                                .build())
                                        .logo(Element.builder()
                                                .enabled(true)
                                                .order(0)
                                                .build())
                                        .messageBeforePaying(LabelElement.builder()
                                                .enabled(true)
                                                .label("Please review your payment details")
                                                .order(0)
                                                .build())
                                        .notes(NoteElement.builder()
                                                .enabled(true)
                                                .header("Additional Notes")
                                                .order(0)
                                                .placeholder("Enter any additional notes here")
                                                .value("")
                                                .build())
                                        .page(PageElement.builder()
                                                .description("Get paid securely")
                                                .enabled(true)
                                                .header("Payment Page")
                                                .order(0)
                                                .build())
                                        .paymentButton(LabelElement.builder()
                                                .enabled(true)
                                                .label("Pay Now")
                                                .order(0)
                                                .build())
                                        .paymentMethods(MethodElement.builder()
                                                .allMethodsChecked(true)
                                                .enabled(true)
                                                .header("Payment Methods")
                                                .methods(MethodsList.builder()
                                                        .amex(true)
                                                        .applePay(true)
                                                        .discover(true)
                                                        .eCheck(true)
                                                        .mastercard(true)
                                                        .visa(true)
                                                        .build())
                                                .order(0)
                                                .build())
                                        .payor(PayorElement.builder()
                                                .enabled(true)
                                                .fields(Optional.of(Arrays.asList(PayorFields.builder()
                                                        .display(true)
                                                        .fixed(true)
                                                        .identifier(true)
                                                        .label("Full Name")
                                                        .name("fullName")
                                                        .order(0)
                                                        .required(true)
                                                        .validation("^[a-zA-Z ]+$")
                                                        .value("")
                                                        .width(0)
                                                        .build())))
                                                .header("Payor Information")
                                                .order(0)
                                                .build())
                                        .review(HeaderElement.builder()
                                                .enabled(true)
                                                .header("Review Payment")
                                                .order(0)
                                                .build())
                                        .settings(PagelinkSetting.builder()
                                                .color("#000000")
                                                .language("en")
                                                .build())
                                        .build())
                                .mail2("jo@example.com; ceo@example.com")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"contactUs\": {\n"
                + "    \"emailLabel\": \"Email\",\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Contact Us\",\n"
                + "    \"order\": 0,\n"
                + "    \"paymentIcons\": true,\n"
                + "    \"phoneLabel\": \"Phone\"\n"
                + "  },\n"
                + "  \"logo\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"messageBeforePaying\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"label\": \"Please review your payment details\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"notes\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Additional Notes\",\n"
                + "    \"order\": 0,\n"
                + "    \"placeholder\": \"Enter any additional notes here\",\n"
                + "    \"value\": \"\"\n"
                + "  },\n"
                + "  \"page\": {\n"
                + "    \"description\": \"Get paid securely\",\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Payment Page\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"paymentButton\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"label\": \"Pay Now\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"paymentMethods\": {\n"
                + "    \"allMethodsChecked\": true,\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Payment Methods\",\n"
                + "    \"methods\": {\n"
                + "      \"amex\": true,\n"
                + "      \"applePay\": true,\n"
                + "      \"discover\": true,\n"
                + "      \"eCheck\": true,\n"
                + "      \"mastercard\": true,\n"
                + "      \"visa\": true\n"
                + "    },\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"payor\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"fields\": [\n"
                + "      {\n"
                + "        \"display\": true,\n"
                + "        \"fixed\": true,\n"
                + "        \"identifier\": true,\n"
                + "        \"label\": \"Full Name\",\n"
                + "        \"name\": \"fullName\",\n"
                + "        \"order\": 0,\n"
                + "        \"required\": true,\n"
                + "        \"validation\": \"^[a-zA-Z ]+$\",\n"
                + "        \"value\": \"\",\n"
                + "        \"width\": 0\n"
                + "      }\n"
                + "    ],\n"
                + "    \"header\": \"Payor Information\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"review\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Review Payment\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"settings\": {\n"
                + "    \"color\": \"#000000\",\n"
                + "    \"language\": \"en\"\n"
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
                + "  \"responseData\": \"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\n"
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
    public void testDeletePayLinkFromId() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink().deletePayLinkFromId("payLinkId");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": \"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\n"
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
    public void testGetPayLinkFromId() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":{\"AdditionalData\":{\"key1\":{\"key\":\"value\"},\"key2\":{\"key\":\"value\"},\"key3\":{\"key\":\"value\"}},\"credentials\":[{}],\"lastAccess\":\"2022-06-30T15:01:00Z\",\"pageContent\":{\"amount\":{\"enabled\":true},\"autopay\":{\"enabled\":true},\"contactUs\":{\"enabled\":true},\"entry\":\"entry\",\"invoices\":{\"enabled\":true},\"logo\":{\"enabled\":true},\"messageBeforePaying\":{\"enabled\":true},\"name\":\"name\",\"notes\":{\"enabled\":true},\"page\":{\"enabled\":true},\"paymentButton\":{\"enabled\":true},\"paymentMethods\":{\"enabled\":true},\"payor\":{\"enabled\":true},\"review\":{\"enabled\":true},\"subdomain\":\"mypage-1\"},\"pageIdentifier\":\"null\",\"pageSettings\":{\"color\":\"color\",\"customCssUrl\":\"customCssUrl\",\"language\":\"language\",\"redirectAfterApprove\":true,\"redirectAfterApproveUrl\":\"redirectAfterApproveUrl\"},\"published\":1,\"receiptContent\":{\"amount\":{\"enabled\":true},\"contactUs\":{\"enabled\":true},\"details\":{\"enabled\":true},\"logo\":{\"enabled\":true},\"messageBeforeButton\":{\"enabled\":true},\"page\":{\"enabled\":true},\"paymentButton\":{\"enabled\":true},\"paymentInformation\":{\"enabled\":true},\"settings\":{\"enabled\":true}},\"subdomain\":\"mypage-1\",\"totalAmount\":1.1,\"validationCode\":\"validationCode\"},\"responseText\":\"Success\"}"));
        GetPayLinkFromIdResponse response = client.paymentLink().getPayLinkFromId("paylinkId");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": {\n"
                + "    \"AdditionalData\": {\n"
                + "      \"key1\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"key2\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"key3\": {\n"
                + "        \"key\": \"value\"\n"
                + "      }\n"
                + "    },\n"
                + "    \"credentials\": [\n"
                + "      {}\n"
                + "    ],\n"
                + "    \"lastAccess\": \"2022-06-30T15:01:00Z\",\n"
                + "    \"pageContent\": {\n"
                + "      \"amount\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"autopay\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"contactUs\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"entry\": \"entry\",\n"
                + "      \"invoices\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"logo\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"messageBeforePaying\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"name\": \"name\",\n"
                + "      \"notes\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"page\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"paymentButton\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"paymentMethods\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"payor\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"review\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"subdomain\": \"mypage-1\"\n"
                + "    },\n"
                + "    \"pageIdentifier\": \"null\",\n"
                + "    \"pageSettings\": {\n"
                + "      \"color\": \"color\",\n"
                + "      \"customCssUrl\": \"customCssUrl\",\n"
                + "      \"language\": \"language\",\n"
                + "      \"redirectAfterApprove\": true,\n"
                + "      \"redirectAfterApproveUrl\": \"redirectAfterApproveUrl\"\n"
                + "    },\n"
                + "    \"published\": 1,\n"
                + "    \"receiptContent\": {\n"
                + "      \"amount\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"contactUs\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"details\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"logo\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"messageBeforeButton\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"page\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"paymentButton\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"paymentInformation\": {\n"
                + "        \"enabled\": true\n"
                + "      },\n"
                + "      \"settings\": {\n"
                + "        \"enabled\": true\n"
                + "      }\n"
                + "    },\n"
                + "    \"subdomain\": \"mypage-1\",\n"
                + "    \"totalAmount\": 1.1,\n"
                + "    \"validationCode\": \"validationCode\"\n"
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
    public void testPushPayLinkFromId() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink()
                .pushPayLinkFromId(
                        "payLinkId",
                        PushPayLinkRequest.sms(PushPayLinkRequestSms.builder().build()));
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"channel\": \"sms\"\n" + "}";
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
                + "  \"responseData\": \"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\n"
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
    public void testRefreshPayLinkFromId() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink()
                .refreshPayLinkFromId(
                        "payLinkId", RefreshPayLinkFromIdRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": \"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\n"
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
    public void testSendPayLinkFromId() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink()
                .sendPayLinkFromId(
                        "payLinkId",
                        SendPayLinkFromIdRequest.builder()
                                .mail2("jo@example.com; ceo@example.com")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"isSuccess\": true,\n"
                + "  \"responseData\": \"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\n"
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
    public void testUpdatePayLinkFromId() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"isSuccess\":true,\"responseData\":\"332-c277b704-1301\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink()
                .updatePayLinkFromId(
                        "332-c277b704-1301",
                        PayLinkUpdateData.builder()
                                .notes(NoteElement.builder()
                                        .enabled(true)
                                        .header("Additional Notes")
                                        .order(0)
                                        .placeholder("Enter any additional notes here")
                                        .value("")
                                        .build())
                                .paymentButton(LabelElement.builder()
                                        .enabled(true)
                                        .label("Pay Now")
                                        .order(0)
                                        .build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"notes\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Additional Notes\",\n"
                + "    \"order\": 0,\n"
                + "    \"placeholder\": \"Enter any additional notes here\",\n"
                + "    \"value\": \"\"\n"
                + "  },\n"
                + "  \"paymentButton\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"label\": \"Pay Now\",\n"
                + "    \"order\": 0\n"
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
                + "  \"responseData\": \"332-c277b704-1301\",\n"
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
    public void testAddPayLinkFromBillLotNumber() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"isSuccess\":true,\"responseData\":\"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\"responseText\":\"Success\"}"));
        PayabliApiResponsePaymentLinks response = client.paymentLink()
                .addPayLinkFromBillLotNumber(
                        "LOT-2024-001",
                        PayLinkDataOut.builder()
                                .entryPoint("billing")
                                .vendorNumber("VENDOR-123")
                                .body(PaymentPageRequestBody.builder()
                                        .contactUs(ContactElement.builder()
                                                .emailLabel("Email")
                                                .enabled(true)
                                                .header("Contact Us")
                                                .order(0)
                                                .paymentIcons(true)
                                                .phoneLabel("Phone")
                                                .build())
                                        .logo(Element.builder()
                                                .enabled(true)
                                                .order(0)
                                                .build())
                                        .messageBeforePaying(LabelElement.builder()
                                                .enabled(true)
                                                .label("Please review your payment details")
                                                .order(0)
                                                .build())
                                        .notes(NoteElement.builder()
                                                .enabled(true)
                                                .header("Additional Notes")
                                                .order(0)
                                                .placeholder("Enter any additional notes here")
                                                .value("")
                                                .build())
                                        .page(PageElement.builder()
                                                .description("Get paid securely")
                                                .enabled(true)
                                                .header("Payment Page")
                                                .order(0)
                                                .build())
                                        .paymentButton(LabelElement.builder()
                                                .enabled(true)
                                                .label("Pay Now")
                                                .order(0)
                                                .build())
                                        .paymentMethods(MethodElement.builder()
                                                .allMethodsChecked(true)
                                                .enabled(true)
                                                .header("Payment Methods")
                                                .methods(MethodsList.builder()
                                                        .amex(true)
                                                        .applePay(true)
                                                        .discover(true)
                                                        .eCheck(true)
                                                        .mastercard(true)
                                                        .visa(true)
                                                        .build())
                                                .order(0)
                                                .build())
                                        .payor(PayorElement.builder()
                                                .enabled(true)
                                                .fields(Optional.of(Arrays.asList(PayorFields.builder()
                                                        .display(true)
                                                        .fixed(true)
                                                        .identifier(true)
                                                        .label("Full Name")
                                                        .name("fullName")
                                                        .order(0)
                                                        .required(true)
                                                        .validation("^[a-zA-Z ]+$")
                                                        .value("")
                                                        .width(0)
                                                        .build())))
                                                .header("Payor Information")
                                                .order(0)
                                                .build())
                                        .review(HeaderElement.builder()
                                                .enabled(true)
                                                .header("Review Payment")
                                                .order(0)
                                                .build())
                                        .settings(PagelinkSetting.builder()
                                                .color("#000000")
                                                .language("en")
                                                .build())
                                        .build())
                                .mail2("customer@example.com; billing@example.com")
                                .amountFixed("true")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"contactUs\": {\n"
                + "    \"emailLabel\": \"Email\",\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Contact Us\",\n"
                + "    \"order\": 0,\n"
                + "    \"paymentIcons\": true,\n"
                + "    \"phoneLabel\": \"Phone\"\n"
                + "  },\n"
                + "  \"logo\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"messageBeforePaying\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"label\": \"Please review your payment details\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"notes\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Additional Notes\",\n"
                + "    \"order\": 0,\n"
                + "    \"placeholder\": \"Enter any additional notes here\",\n"
                + "    \"value\": \"\"\n"
                + "  },\n"
                + "  \"page\": {\n"
                + "    \"description\": \"Get paid securely\",\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Payment Page\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"paymentButton\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"label\": \"Pay Now\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"paymentMethods\": {\n"
                + "    \"allMethodsChecked\": true,\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Payment Methods\",\n"
                + "    \"methods\": {\n"
                + "      \"amex\": true,\n"
                + "      \"applePay\": true,\n"
                + "      \"discover\": true,\n"
                + "      \"eCheck\": true,\n"
                + "      \"mastercard\": true,\n"
                + "      \"visa\": true\n"
                + "    },\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"payor\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"fields\": [\n"
                + "      {\n"
                + "        \"display\": true,\n"
                + "        \"fixed\": true,\n"
                + "        \"identifier\": true,\n"
                + "        \"label\": \"Full Name\",\n"
                + "        \"name\": \"fullName\",\n"
                + "        \"order\": 0,\n"
                + "        \"required\": true,\n"
                + "        \"validation\": \"^[a-zA-Z ]+$\",\n"
                + "        \"value\": \"\",\n"
                + "        \"width\": 0\n"
                + "      }\n"
                + "    ],\n"
                + "    \"header\": \"Payor Information\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"review\": {\n"
                + "    \"enabled\": true,\n"
                + "    \"header\": \"Review Payment\",\n"
                + "    \"order\": 0\n"
                + "  },\n"
                + "  \"settings\": {\n"
                + "    \"color\": \"#000000\",\n"
                + "    \"language\": \"en\"\n"
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
                + "  \"responseData\": \"2325-XXXXXXX-90b1-4598-b6c7-44cdcbf495d7-1234\",\n"
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
