package endpoints;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class SubTypesEndpointTests extends ApiTestBase {

    @Test
    void Get_SubTypes_ShouldReturn_Success_ProperResponse()
    {
        ValidatableResponse response = when().get("subtypes").then();
        assertOK(response);
    }

    @Test
    public void Get_SubTypes_ShouldMatchJsonSchema() {
        ValidatableResponse response = when().get("subtypes").then();
        assertJsonSchema("subtypes.schema.json", response);
    }

}
