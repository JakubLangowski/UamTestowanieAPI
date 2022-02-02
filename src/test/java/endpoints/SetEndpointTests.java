package endpoints;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;

public class SetEndpointTests extends ApiTestBase {

    private ValidatableResponse makeRequest(String setId) {
        return given()
                    .pathParam("setId", setId).
                when()
                    .get("sets/{setId}").then();
    }

    @ParameterizedTest
    @ValueSource(strings = {"PMMQ", "J20", "PM15"})
    void Get_ShouldReturn_Success_ProperResponse(String setId)
    {
        var response = makeRequest(setId);
        assertOK(response);
    }

    @Test
    void Get__ById_ShouldReturn_NotFound_IfCardDontExist()
    {
        var response = makeRequest("abcd");
        assertNotFound(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RNA", "PKLD", "HA2"})
    public void Get_ById_ShouldMatchJsonSchema(String setId) {
        var response = makeRequest(setId);
        assertJsonSchema("set.schema.json", response);
    }

}
