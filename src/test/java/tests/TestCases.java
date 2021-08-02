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
import java.util.*;
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
    public void testPostPartial() {
        int min = 1000;
        int max = 9999;
        int num = (int) (Math.random()*(max-min+1)+min);

        List<Map<String, Object>> screeningList = new ArrayList<>();
        Map<String,Object> screeningMap = new HashMap<String, Object>();
        screeningMap.put("SCREENING_ID", 1);
        screeningMap.put("SCORE", 6);
        screeningMap.put("IS_WS", "Y");
        screeningMap.put("IS_AMT", "Y");
        screeningMap.put("REGION_ID", 1);
        screeningList.add(screeningMap);
        Map<String,Object> screeningMap2 = new HashMap<String, Object>();
        screeningMap2.put("SCREENING_ID", 12);
        screeningMap2.put("SCORE", 6);
        screeningMap2.put("IS_WS", "N");
        screeningMap2.put("IS_AMT", "N");
        screeningMap2.put("REGION_ID", 6);
        screeningList.add(screeningMap2);
        Map<String,Object> screeningMap3 = new HashMap<String, Object>();
        screeningMap3.put("SCREENING_ID", 13);
        screeningMap3.put("SCORE", 6);
        screeningMap3.put("IS_WS", "N");
        screeningMap3.put("IS_AMT", "N");
        screeningMap3.put("REGION_ID", 5);
        screeningList.add(screeningMap3);
        Map<String,Object> screeningMap4 = new HashMap<String, Object>();
        screeningMap4.put("SCREENING_ID", 14);
        screeningMap4.put("SCORE", 6);
        screeningMap4.put("IS_WS", "N");
        screeningMap4.put("IS_AMT", "N");
        screeningMap4.put("REGION_ID", 4);
        screeningList.add(screeningMap4);
        Map<String,Object> screeningMap5 = new HashMap<String, Object>();
        screeningMap5.put("SCREENING_ID", 15);
        screeningMap5.put("SCORE", 6);
        screeningMap5.put("IS_WS", "N");
        screeningMap5.put("IS_AMT", "N");
        screeningMap5.put("REGION_ID", 3);
        screeningList.add(screeningMap5);
        Map<String,Object> screeningMap6 = new HashMap<String, Object>();
        screeningMap6.put("SCREENING_ID", 16);
        screeningMap6.put("SCORE", 6);
        screeningMap6.put("IS_WS", "N");
        screeningMap6.put("IS_AMT", "N");
        screeningMap6.put("REGION_ID", 2);
        screeningList.add(screeningMap6);
        Map<String,Object> screeningMap7 = new HashMap<String, Object>();
        screeningMap7.put("SCREENING_ID", 17);
        screeningMap7.put("SCORE", 6);
        screeningMap7.put("IS_WS", "N");
        screeningMap7.put("IS_AMT", "N");
        screeningMap7.put("REGION_ID", 1);
        screeningList.add(screeningMap7);

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
        jsonBody.put("PRICE_DATE", "2021-04-11");
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
//        jsonBody.put("API_TYPE", "1");
        jsonBody.put("FACTOR_POPUP", "Y");
//        jsonBody.put("CALLBACKURL", "https://redmine.interserv.co.uk?price/notify");
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
    }

}
