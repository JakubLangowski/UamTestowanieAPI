package endpoints;

import models.PaginationParams;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class SetsEndpointTest extends ApiTestBase {

    @Test
    void Get_ShouldReturn_Success_ProperResponse() {
        var response = when().get("sets").then();
        assertOK(response);
    }

    @Test
    void Get_ShouldReturn_Success_WithProperPaginationHeaders() {
        var response = when().get("sets").then();
        assertPaginationHeaders(response, 500, 500);
    }

    @Test
    void Get_ShouldReturn_0_Sets_IfPageExceedsMaximum() {
        var response =
                given()
                        .param(PaginationParams.PAGE_SIZE.toString(), 100)
                        .param(PaginationParams.PAGE.toString(), 10000).
                when().get("sets").then();
        assertPaginationHeaders(response, 100, 0);
    }

    @Test
    void Get_ShouldReturn_Success_WithMatchingRequestPaginationHeaders() throws URISyntaxException {
        var pageSize = 5;
        var page = 5;
        var response =
                given()
                        .param(PaginationParams.PAGE.toString(), page)
                        .param(PaginationParams.PAGE_SIZE.toString(), pageSize)
                        .get("sets");
        assertLinkHeaders(response, page, pageSize);
    }

}
