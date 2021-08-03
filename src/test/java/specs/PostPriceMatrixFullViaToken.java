package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.HashMap;
import java.util.Map;

public class PostPriceMatrixFullViaToken {
    private static final String uri = System.getProperty("environment");

    public RequestSpecification requestSpec;
    public ResponseSpecification responseSpec;

    public void createResponseSpecification() {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public void createRequestSpecification(String token) {
        Map<String,Object> jsonBody = new HashMap<String,Object>();
        jsonBody.put("token", token);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .setBody(jsonBody)
                .build();
    }
}
