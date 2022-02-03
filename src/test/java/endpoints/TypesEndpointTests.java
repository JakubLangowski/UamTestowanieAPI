package endpoints;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class TypesEndpointTests extends ApiTestBase {

    @Test
    void Get_Types_ShouldReturn_Success_ProperResponse()
    {
        ValidatableResponse response = when().get("types").then();
        assertOK(response);
    }

    @Test
    public void Get_Types_ShouldMatchJsonSchema() {
        ValidatableResponse response = when().get("types").then();
        assertJsonSchema("types.schema.json", response);
    }

}
