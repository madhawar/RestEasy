package tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import specs.PostPriceMatrixFullViaToken;
import utilities.DataPOJO;
import utilities.ExcelData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TestFullPriceMatrixViaToken {
    private static final String url = "/price/get";

    @DataProvider(name ="ExcelData")
    public Object[][] excelDP() throws IOException {
        ExcelData excelData = new ExcelData();
        return excelData.getData("src/test/resources/price_params.xlsx","test");
    }

    @DataProvider(name ="JSON")
    public Object[][] jsonDP() throws FileNotFoundException {
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

    PostPriceMatrixFullViaToken postPriceMatrixFullViaToken = new PostPriceMatrixFullViaToken();

    @Test(dataProvider = "JSON")
    public void singleTraveller(DataPOJO jsonParams) {
        postPriceMatrixFullViaToken.createRequestSpecification(jsonParams.getToken());
        postPriceMatrixFullViaToken.createResponseSpecification();

        given()
                .spec(postPriceMatrixFullViaToken.requestSpec)
                .log().all()
                .when()
                .post(url)
                .then()
                .spec(postPriceMatrixFullViaToken.responseSpec)
                .log().all();
//                .extract()
//                .path("result.startDate" );

//        Assert.assertEquals(startDate, "2020-07-20");
    }

}
