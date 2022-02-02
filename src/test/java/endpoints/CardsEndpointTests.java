package endpoints;

import io.restassured.response.ValidatableResponse;
import models.PaginationParams;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class CardsEndpointTests extends ApiTestBase {

    @Test
    void Get_ShouldReturn_Success_ProperResponse() {
        var response = when().get("cards").then();
        assertOK(response);
    }

    @Test
    void Get_ShouldReturn_Success_WithProperPaginationHeaders() {
        var response = when().get("cards").then();
        assertPaginationHeaders(response, 100, 100);
    }

    @Test
    void Get_ShouldReturn_100_CardsIfPageSizeIsGreaterThan100() {
        var response =
                given().param(PaginationParams.PAGE_SIZE.toString(), 120).
                when().get("cards").then();
        assertPaginationHeaders(response, 100, 100);
    }

    @Test
    void Get_ShouldReturn_0_CardsIfPageExceedsMaximum() {
        var response =
                given().param(PaginationParams.PAGE.toString(), 10000).
                        when().get("cards").then();
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
                .get("cards");
        assertLinkHeaders(response, page, pageSize);
    }
}
