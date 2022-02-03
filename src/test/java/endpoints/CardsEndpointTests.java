package endpoints;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.PaginationParams;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class CardsEndpointTests extends ApiTestBase {

    @Test
    void Get_ShouldReturn_Success_ProperResponse() {
        ValidatableResponse response = when().get("cards").then();
        assertOK(response);
    }

    @Test
    void Get_ShouldReturn_Success_WithProperPaginationHeaders() {
        ValidatableResponse response = when().get("cards").then();
        assertPaginationHeaders(response, 100, 100);
    }

    @Test
    void Get_ShouldReturn_100_CardsIfPageSizeIsGreaterThan100() {
        ValidatableResponse response =
                given().param(PaginationParams.PAGE_SIZE.toString(), 120).
                when().get("cards").then();
        assertPaginationHeaders(response, 100, 100);
    }

    @Test
    void Get_ShouldReturn_0_CardsIfPageExceedsMaximum() {
        ValidatableResponse response =
                given().param(PaginationParams.PAGE.toString(), 10000).
                        when().get("cards").then();
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
                .get("cards");
        assertLinkHeaders(response, page, pageSize);
    }
}
