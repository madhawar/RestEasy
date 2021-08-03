package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostPriceMatrixFull {
    private static final String uri = System.getProperty("environment");

    public RequestSpecification requestSpec;
    public ResponseSpecification responseSpec;

    public void createResponseSpecification() {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public void createRequestSpecification() {
        int min = 1000;
        int max = 9999;
        int num = (int) (Math.random()*(max-min+1)+min);

        List<Map<String, Object>> screeningList = new ArrayList<>();
        Map<String,Object> screeningMap = new HashMap<String, Object>();
        screeningMap.put("SCREENING_ID", 1);
        screeningMap.put("SCORE", 2);
        screeningMap.put("IS_WS", "Y");
        screeningMap.put("IS_AMT", "Y");
        screeningMap.put("REGION_ID", 1);
        screeningList.add(screeningMap);

        List<Map<String, Object>> propertyList = new ArrayList<>();
        Map<String,Object> propertyMap = new HashMap<String, Object>();
        propertyMap.put("ID", num);
        propertyMap.put("AGE", 80);
        propertyMap.put("MEDICAL_CONDITIONS", "TB|Coldsore");
        propertyMap.put("SURGERY_COUNT", 1);
        propertyMap.put("FTE", "N");
        propertyMap.put("SURGERY_OTHER_SELECTED", "Y");
        propertyMap.put("SCREENING_STATUS", "ACCEPTED");
        propertyMap.put("SCREENING", screeningList);
        propertyList.add(propertyMap);

        Map<String,Object> jsonBody = new HashMap<String,Object>();
        jsonBody.put("DOMAIN", "WAPP");
        jsonBody.put("FLOW", "CREATE_FLOW");
        jsonBody.put("PRICE_DATE", "2020-08-18");
        jsonBody.put("CHANNEL", "OFFLINE");
        jsonBody.put("REQUESTED_FLOW", "CREATE_FLOW");
        jsonBody.put("COUNTRY_LIST", "DEFAULT");
        jsonBody.put("QUOTED_METHOD", "ONLINE");
        jsonBody.put("FROM_LOCATION", "UK1");
        jsonBody.put("CANCELLATION_COVER", "5000");
        jsonBody.put("BAGGAGE_COVER", "500");
        jsonBody.put("TRIP_EXCESS", "50");
        jsonBody.put("FCDO_COVER", "4");
        jsonBody.put("TDE_COVER", "Y");
        jsonBody.put("GADGET_COVER", "1");
        jsonBody.put("KEY", "ACTIVITY");
        jsonBody.put("API_TYPE", "1");
        jsonBody.put("FACTOR_POPUP", "N");
        jsonBody.put("CALLBACKURL", "https://redmine.interserv.co.uk?price/notify");
        jsonBody.put("PROPERTY", propertyList);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .setBody(jsonBody)
                .build();
    }
}
