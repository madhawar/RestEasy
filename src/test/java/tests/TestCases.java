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
        Map<String,String> requestParams = new HashMap<String,String>();
        requestParams.put("DOMAIN", "WAPP");
        requestParams.put("FLOW", "CREATE_FLOW");
        requestParams.put("PRICE_DATE", "2020-08-18");
        requestParams.put("CHANNEL", "OFFLINE");
        requestParams.put("REQUESTED_FLOW", "CREATE_FLOW");
        requestParams.put("COUNTRY_LIST", "DEFAULT");
        requestParams.put("QUOTED_METHOD", "ONLINE");
        requestParams.put("FROM_LOCATION", "UK1");
        requestParams.put("CANCELLATION_COVER", "5000");
        requestParams.put("BAGGAGE_COVER", "500");
        requestParams.put("TRIP_EXCESS", "50");
        requestParams.put("FCDO_COVER", "4");
        requestParams.put("TDE_COVER", "Y");
        requestParams.put("GADGET_COVER", "1");
        requestParams.put("KEY", "ACTIVITY");
        requestParams.put("API_TYPE", "1");
        requestParams.put("FACTOR_POPUP", "N");
        requestParams.put("CALLBACKURL", "https://redmine.interserv.co.uk?price/notify");

        given()
                .contentType(ContentType.JSON)
                .body(requestParams)
                .when()
                .post("/price/wapp")
                .then()
                .statusCode(200)
                .log()
                .all();
    }

}
