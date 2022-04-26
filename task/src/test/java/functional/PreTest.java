package functional;

import core.RestServiceBase;
import core.task3.StopOrder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static core.GlobalConstants.*;
import static org.testng.Assert.assertEquals;


public class PreTest extends TestHooks {

    static final Logger LOGGER = LoggerFactory.getLogger(PreTest.class);

    @DataProvider(name = "endpoints")
    public Object[][] dataProvider() {
        return topicsWithoutAuthentication;
    }


    @Test(groups = "functional", dataProvider = "endpoints")
    public void nonRequiredAuthShouldResponseOK(String endpoint) {
        _setTestStep("Prepare test for endpoint: ", endpoint);
        endpoint = BASE_URL + "/" + endpoint;

        _setTestStep("Run for response body");
        RestServiceBase rest = new RestServiceBase();
        ExtractableResponse<Response> response = rest.getResponseBody(endpoint);

        _setTestStep("Validate status code for topics which not required Authorization:");
        LOGGER.info("Status Code for GET: {}", response.statusCode());
        assertEquals(response.statusCode(), REST_SUCCESS_CODE);
    }


    @Test(groups = "functional", dataProvider = "endpoints")
    public void checkResponseBody(String endpoint) {
        _setTestStep("Prepare test for endpoint: ", endpoint);
        endpoint = BASE_URL + "/" + endpoint;

        _setTestStep("Run for response body");
        RestServiceBase rest = new RestServiceBase();
        ExtractableResponse<Response> response = rest.getResponseBody(endpoint);


        LOGGER.info("Response for GET: {}", response.body().asString());
    }


    @Test(groups = "functional")
    public void builderTask3Test() {

        final StopOrder immutableObject = StopOrder.builder()
                .id(1)
                .stop(new BigDecimal("123.456"))
                .description("my new order")
                .build();

        LOGGER.info(immutableObject.toString());

    }

}
