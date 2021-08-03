package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.annotations.Test;
import specs.PostPriceMatrixFull;
import utilities.Init;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestPriceMatrixFull extends Init {
    private static final String url = "/price/get";

    PostPriceMatrixFull postPriceMatrixFull = new PostPriceMatrixFull();

    @Test()
    public void singleTraveller() {
        postPriceMatrixFull.createRequestSpecification();
        postPriceMatrixFull.createResponseSpecification();

        String result = given()
                .spec(postPriceMatrixFull.requestSpec)
                .when()
                .post(url)
                .then()
                .spec(postPriceMatrixFull.responseSpec)
                .extract()
                .path("result");

        String jsonFilePath = "src/test/resources/price_params.json";
        try {
            Map<String, Object> priceMatrix = new HashMap<String, Object>();
            Map<String, Object> token = new HashMap<>();
            token.put("token", result);
            priceMatrix.put("priceMatrix", new Map[]{token});

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(Paths.get(jsonFilePath));

            gson.toJson(priceMatrix, writer);

            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
