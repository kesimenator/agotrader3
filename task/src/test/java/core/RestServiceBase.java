package core;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import lombok.Getter;
import lombok.Setter;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public ExtractableResponse<Response> getResponseWithTimeoutInSeconds(String endpoint, Map<String, String> params) {
        try {
            Awaitility.await().atMost(RESPONSE_TIMEOUT, TimeUnit.SECONDS).until(() -> this.getExtractableResponseWithParams(endpoint, params).statusCode() == REST_SUCCESS_CODE);
        } catch (Exception ex) {
            LOGGER.error("{} | too long waiting?: {}", FAILED_RESPONSE_MESSAGE, RESPONSE_TIMEOUT);
            ex.printStackTrace();
        }
        assert response != null;
        return response;
    }


//    public ExtractableResponse<Response> getResponseBodyByParams(String endpoint, Map<String, String> params) {
//        response = getExtractableResponseBodyWithParams(endpoint, params);
//        return response;
//    }


    public ExtractableResponse<Response> getExtractableResponseWithParams(String endpoint, Map<String, String> params) {
        response = given()
                .queryParams(params)
                .when()
                .get(endpoint)
                .then()
                .extract();
        return response;
    }


    public ResponseBodyExtractionOptions getExtractableResponseBodyWithParams(String endpoint, Map<String, String> params) {
        return getExtractableResponseWithParams(endpoint, params).body();
    }


    public ExtractableResponse<Response> getExtractableResponseBody(String endpoint) {
        return given()
                .when()
                .get(endpoint)
                .then()
                .extract();
    }


    //            LinkedList<Bucket> deserializedData =
//                    given()
//                            .queryParams(paramsMap)
//                            .when()
//                            .get(ENDPOINT_TRADE_BUCKETED)
//                            .then()
//                            .extract()
//                            .body()
//                            .as(new TypeRef<LinkedList<Bucket>>() {
//                            });


    // TODO: 2022-04-24
//    protected ExtractableResponse<Response> postRequest(String URL, String requestBody) {
//        return given()
//                .when()
//                .post(URL)
//                .then()
//                .extract();
//    }

}
