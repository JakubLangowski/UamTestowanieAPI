package endpoints;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class SuperTypesEndpointTests extends ApiTestBase {

    @Test
    void Get_SuperTypes_ShouldReturn_Success_ProperResponse()
    {
        ValidatableResponse response = when().get("supertypes").then();
        assertOK(response);
    }

    @Test
    public void Get_SuperTypes_ShouldMatchJsonSchema() {
        ValidatableResponse response = when().get("supertypes").then();
        assertJsonSchema("supertypes.schema.json", response);
    }

}
