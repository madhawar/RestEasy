package tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.DataPOJO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PostFullPriceMatrix {
    private static final String environment = System.getProperty("environment");

    @DataProvider(name ="JSON")
    public Object[][] priceMatrix() throws FileNotFoundException {
        JsonElement jsonData = new JsonParser().parse(new FileReader("src/test/resources/price_params.json"));
        JsonElement dataSet = jsonData.getAsJsonObject().get("priceMatrix");
        List<DataPOJO> testData = new Gson().fromJson(dataSet, new TypeToken<List<DataPOJO>>() {}.getType());
        Object[][] returnValue = new Object[testData.size()][1];
        int index = 0;
        for (Object[] each : returnValue) {
            each[0] = testData.get(index++);
        }
        return returnValue;
    }

    @Test(dataProvider = "JSON")
    public void fetchFullPrice(DataPOJO priceMatrix) {
        Map<String,Object> jsonBody = new HashMap<String,Object>();
        jsonBody.put("token", priceMatrix.getToken());

        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(environment + "/price/get")
                .then()
                .statusCode(200)
                .log()
                .all();
    }
}
