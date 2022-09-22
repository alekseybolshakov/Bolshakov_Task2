package definitions;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import utils.FileReader;

import static constants.Endpoint.CURRENT;
import static constants.JsonPath.ERROR_TYPE;
import static constants.RequestParameter.ACCESS_KEY;
import static constants.RequestParameter.QUERY;

public class StepDefinitions {

    private static final String CITY_TEST_DATA_JSON_PATH_PATTERN =
            "src/test/resources/json_files/%s_test_data.json";
    private static final String ACCESS_KEY_VALUE = "861b5c1ecd28d9e41bab81a2b0237f49";
    private static final String BASE_URI = "http://api.weatherstack.com/";

    private RequestSpecification requestSpecification;

    @Before
    public void requestSpecification(Scenario scenario) {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addParam(ACCESS_KEY, ACCESS_KEY_VALUE)
                .build();
    }

    @Then("I get current weather for {string} city")
    public void positiveTest(String city) {
        Response response = RestAssured
                .given()
                .spec(requestSpecification)
                .param(QUERY, city)
                .when()
                .get(CURRENT);
        String actualJsonText  = response.asString();

        try {
            String expectedJsonText = FileReader.readFileAsString(
                    String.format(CITY_TEST_DATA_JSON_PATH_PATTERN, city));
            JSONAssert.assertEquals(
                    "Received values are not the same as in saved test data json file.\n"
                    , expectedJsonText, actualJsonText, JSONCompareMode.STRICT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Then("I get {string} error message for incorrect {string} city")
    public void incorrectCity(String expectedErrorMessage, String city) {
        RequestSpecification customReqSpec = RestAssured
                .given()
                .spec(requestSpecification)
                .param(QUERY, city);
        verifyErrorMsgWithCustomReqSpec(customReqSpec, expectedErrorMessage);
    }

    @Then("I get {string} error message for incorrect {string} access key")
    public void incorrectAccessKey(String expectedErrorMessage, String accessKey) {
        RequestSpecification customReqSpec = RestAssured
                .given()
                .baseUri(BASE_URI)
                .param(ACCESS_KEY, accessKey);
        verifyErrorMsgWithCustomReqSpec(customReqSpec, expectedErrorMessage);
    }

    private void verifyErrorMsgWithCustomReqSpec(RequestSpecification customRequestSpec, String expectedErrorMessage) {
        Response response = customRequestSpec
                .when()
                .get(CURRENT)
                .then()
                .log().all()
                .extract().response();
        String actualErrorMessage = response.path(ERROR_TYPE);
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Wrong error message");
    }
}
