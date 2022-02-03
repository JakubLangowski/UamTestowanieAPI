package endpoints;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.PaginationParams;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class SetsEndpointTest extends ApiTestBase {

    @Test
    void Get_ShouldReturn_Success_ProperResponse() {
        ValidatableResponse response = when().get("sets").then();
        assertOK(response);
    }

    @Test
    void Get_ShouldReturn_Success_WithProperPaginationHeaders() {
        ValidatableResponse response = when().get("sets").then();
        assertPaginationHeaders(response, 500, 500);
    }

    @Test
    void Get_ShouldReturn_0_Sets_IfPageExceedsMaximum() {
        ValidatableResponse response =
                given()
                        .param(PaginationParams.PAGE_SIZE.toString(), 100)
                        .param(PaginationParams.PAGE.toString(), 10000).
                when().get("sets").then();
        assertPaginationHeaders(response, 100, 0);
    }

    @Test
    void Get_ShouldReturn_Success_WithMatchingRequestPaginationHeaders() throws URISyntaxException {
        int pageSize = 5;
        int page = 5;
        Response response =
                given()
                        .param(PaginationParams.PAGE.toString(), page)
                        .param(PaginationParams.PAGE_SIZE.toString(), pageSize)
                        .get("sets");
        assertLinkHeaders(response, page, pageSize);
    }

}
