package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import utilities.Init;

import java.util.HashMap;
import java.util.Map;

public class GetStatic extends Init {
    private static final String uri = System.getProperty("environment");

    public RequestSpecification requestSpec;
    public ResponseSpecification responseSpec;

    public void createRequestSpecification() {
        Map<String,String> jsonBody = new HashMap<>();
        jsonBody.put("domain", "WAPP");
        jsonBody.put("existingDate", "2020-08-01");
        jsonBody.put("newDate", "2020-08-03");

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .addQueryParams(jsonBody)
                .build();
    }

    public void createResponseSpecification() {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

}
