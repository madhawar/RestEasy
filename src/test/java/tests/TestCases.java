package tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestCases<result> {
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
    public void testPostFull() {
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

        long telemetry = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(environment + "/price/wapp")
                .timeIn(TimeUnit.MILLISECONDS);
                Log.info("Response time: " + String.valueOf(telemetry));

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .log()
                .all()
                .when()
                .post(environment + "/price/wapp")
                .then()
                .statusCode(200)
                .log()
                .all();

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(environment + "/price/wapp")
                .then()
                .assertThat()
                .body("status", equalTo("success"));
    }

}
