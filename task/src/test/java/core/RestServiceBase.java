package core;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static core.GlobalConstants.*;
import static io.restassured.RestAssured.given;

public class RestServiceBase {

    static final Logger LOGGER = LoggerFactory.getLogger(RestServiceBase.class);
    @Getter @Setter
    protected static ExtractableResponse<Response> response;

    public ExtractableResponse<Response> getResponseBody(String URL) {
        response = getExtractableResponseBody(URL);
        return response;
    }

    public ExtractableResponse<Response> getResponseBodyWithTimeoutInSeconds(String endpoint, Map<String, String> params) {
        try {
            Awaitility.await().atMost(RESPONSE_TIMEOUT, TimeUnit.SECONDS).until(() -> this.getResponseBodyByParams(endpoint, params).statusCode() == REST_SUCCESS_CODE);
        } catch (Exception ex) {
            LOGGER.error("{} | too long waiting?: {}", FAILED_RESPONSE_MESSAGE, RESPONSE_TIMEOUT);
            ex.printStackTrace();
        }
        return response;
    }


    public ExtractableResponse<Response> getResponseBodyByParams(String endpoint, Map<String, String> params) {
        response = getExtractableResponseBodyWithParams(endpoint, params);
        return response;
    }


    public ExtractableResponse<Response> getExtractableResponseBodyWithParams(String endpoint, Map<String, String> params) {
        return given()
                .queryParams(params)
                .when()
                .get(endpoint)
                .then()
                .extract();
    }


    public ExtractableResponse<Response> getExtractableResponseBody(String endpoint) {
        return given()
                .when()
                .get(endpoint)
                .then()
                .extract();
    }


    // TODO: 2022-04-24
//    protected ExtractableResponse<Response> postRequest(String URL, String requestBody) {
//        return given()
//                .when()
//                .post(URL)
//                .then()
//                .extract();
//    }

}
