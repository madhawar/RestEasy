package tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.DataPOJO;
import utilities.ExcelData;
import utilities.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestCases {
    private static final String environment = System.getProperty("environment");

    @DataProvider(name ="Excel")
    public Object[][] excelDP() throws IOException {
        ExcelData excelData = new ExcelData();
        return excelData.getData("src/test/resources/price_params.xlsx","wapp");
    }

    @DataProvider(name ="JSON")
    public Object[][] priceParams() throws FileNotFoundException {
        JsonElement jsonData = new JsonParser().parse(new FileReader("src/test/resources/price-params.json"));
        JsonElement dataSet = jsonData.getAsJsonObject().get("priceParams");
        List<DataPOJO> testData = new Gson().fromJson(dataSet, new TypeToken<List<DataPOJO>>() {}.getType());
        Object[][] returnValue = new Object[testData.size()][1];
        int index = 0;
        for (Object[] each : returnValue) {
            each[0] = testData.get(index++);
        }
        return returnValue;
    }

    @Test()
    public void testGet() {
        String url = environment + "/price-status?existingDate=2020-08-01&newDate=2020-08-03&domain=WAPP";

        long telemetry = given()
                .contentType(ContentType.JSON)
                .when()
                .get(url)
                .timeIn(TimeUnit.MILLISECONDS);
        Log.info("Response time: " + String.valueOf(telemetry));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .assertThat()
                .statusCode(200)
                .log()
                .all();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .assertThat()
                .body("result.startDate", equalTo("2020-07-20"));
    }

    @Test
    public void testPost() {
        Map<String,Object> jsonBodyUsingMap = new HashMap<String,Object>();
        jsonBodyUsingMap.put("DOMAIN", "WAPP");
        jsonBodyUsingMap.put("FLOW", "CREATE_FLOW");
        jsonBodyUsingMap.put("PRICE_DATE", "2020-08-18");
        jsonBodyUsingMap.put("CHANNEL", "OFFLINE");
        jsonBodyUsingMap.put("REQUESTED_FLOW", "CREATE_FLOW");
        jsonBodyUsingMap.put("COUNTRY_LIST", "DEFAULT");
        jsonBodyUsingMap.put("QUOTED_METHOD", "ONLINE");
        jsonBodyUsingMap.put("FROM_LOCATION", "UK1");
        jsonBodyUsingMap.put("CANCELLATION_COVER", "5000");
        jsonBodyUsingMap.put("BAGGAGE_COVER", "500");
        jsonBodyUsingMap.put("TRIP_EXCESS", "50");
        jsonBodyUsingMap.put("FCDO_COVER", "4");
        jsonBodyUsingMap.put("TDE_COVER", "Y");
        jsonBodyUsingMap.put("GADGET_COVER", "1");
        jsonBodyUsingMap.put("KEY", "ACTIVITY");
        jsonBodyUsingMap.put("API_TYPE", "1");
        jsonBodyUsingMap.put("FACTOR_POPUP", "N");
        jsonBodyUsingMap.put("CALLBACKURL", "https://redmine.interserv.co.uk?price/notify");

        Map<String,Object> propertyMap = new HashMap<String,Object>();
        propertyMap.put("ID", "");
        propertyMap.put("AGE", "");
        propertyMap.put("MEDICAL_CONDITIONS", "");
        propertyMap.put("SURGERY_COUNT", "");
        propertyMap.put("FTE", "");
        propertyMap.put("SURGERY_OTHER_SELECTED", "");
        propertyMap.put("SCREENING_STATUS", "");

        Map<String,String> screeningMap = new HashMap<>();
        screeningMap.put("SCREENING_ID", "");
        screeningMap.put("SCORE", "");
        screeningMap.put("IS_WS", "");
        screeningMap.put("IS_AMT", "");
        screeningMap.put("REGION_ID", "");

        propertyMap.put("SCREENING", screeningMap);
        jsonBodyUsingMap.put("PROPERTY", propertyMap);

        Gson gson = new Gson();
        JsonObject requestBody = gson.toJsonTree(jsonBodyUsingMap).getAsJsonObject();

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log()
                .all()
                .when()
                .post(environment + "/price/get")
                .then()
                .statusCode(200)
                .log()
                .all();
    }

}
