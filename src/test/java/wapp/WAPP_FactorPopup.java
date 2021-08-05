package wapp;

import org.testng.annotations.Test;
import specs.CommonGet;

import static io.restassured.RestAssured.given;

public class WAPP_FactorPopup {
    private static final String url = "/factor/popup";

    CommonGet commonGet = new CommonGet();

    @Test()
    public void status200AndContentJSON() {
        String qpToken = "3634";
        String qpFcdoCover= "4";
        String qpGadgetCover= "1";
        String qpTripExcess= "50";
        String parameter = "";

        commonGet.createRequestSpecification_4(qpToken, qpFcdoCover, qpGadgetCover, qpTripExcess);
        commonGet.createResponseSpecification_1(parameter);

        given()
                .spec(commonGet.requestSpec).log().uri()
                .when()
                .get(url)
                .then()
                .spec(commonGet.responseSpec).log().body();
    }
}
