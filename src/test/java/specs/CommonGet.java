package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import utilities.Init;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;

public class CommonGet extends Init {
    private static final String uri = System.getProperty("environment");

    public RequestSpecification requestSpec;
    public ResponseSpecification responseSpec;

    // Static Tree API-WAPP
    // Location Tree API-WAPP
    // FCO Countries All API-WAPP
    // Price Change Logs API
    public void createRequestSpecification_1(String qpDomain) {
        Map<String,String> jsonBody = new HashMap<>();
        jsonBody.put("domain", qpDomain);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .addQueryParams(jsonBody)
                .build();
    }

    // FCO Countries API-WAPP
    // Price Static Tree API WAPP
    // fco-static-version-expire -DO NOT USE FOR AUTOMATION
    public void createRequestSpecification_2(String qpDomain, String qpRequestDate) {
        Map<String,String> jsonBody = new HashMap<>();
        jsonBody.put("domain", qpDomain);
        jsonBody.put("requestDate", qpRequestDate);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .addQueryParams(jsonBody)
                .build();
    }

    // Price Status API
    public void createRequestSpecification_3(String qpDomain, String qpExistingDate, String qpNewDate) {
        Map<String,String> jsonBody = new HashMap<>();
        jsonBody.put("domain", qpDomain);
        jsonBody.put("existingDate", qpExistingDate);
        jsonBody.put("newDate", qpNewDate);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .addQueryParams(jsonBody)
                .build();
    }

    // factor popup
    public void createRequestSpecification_4(String qpToken, String qpFcdoCover, String qpGadgetCover, String qpTripExcess) {
        Map<String,String> jsonBody = new HashMap<>();
        jsonBody.put("token", qpToken);
        jsonBody.put("FCDO_COVER", qpFcdoCover);
        jsonBody.put("GADGET_COVER", qpGadgetCover);
        jsonBody.put("TRIP_EXCESS", qpTripExcess);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .addQueryParams(jsonBody)
                .build();
    }

    // factor popup all
    public void createRequestSpecification_5(String qpToken) {
        Map<String,String> jsonBody = new HashMap<>();
        jsonBody.put("token", qpToken);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .addQueryParams(jsonBody)
                .build();
    }

    public void createResponseSpecification_1(String parameter) {
        if (!parameter.equals("")) {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody(parameter + ".size()", greaterThan(0))
                .build();
        }
        else {
            responseSpec = new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .expectContentType(ContentType.JSON)
                    .build();
        }
    }

}
