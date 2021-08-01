package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import utilities.Log;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PostFullPrice {
    private static final String environment = System.getProperty("environment");

    @Test
    public void fetchFullPrice() {
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

        String matrixReference = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(environment + "/price/get")
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .path("result");
        Log.info(matrixReference);

        String jsonFilePath = "src/test/resources/price_params.json";
        try {
            Map<String, Object> price = new HashMap<String, Object>();
            Map<String, Object> matrix = new HashMap<>();
            matrix.put("token", matrixReference);
            price.put("priceMatrix", new Map[]{matrix});

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(Paths.get(jsonFilePath));

            gson.toJson(price, writer);

            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
