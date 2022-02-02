package endpoints;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class SubTypesEndpointTests extends ApiTestBase {

    @Test
    void Get_SubTypes_ShouldReturn_Success_ProperResponse()
    {
        var response = when().get("subtypes").then();
        assertOK(response);
    }

    @Test
    public void Get_SubTypes_ShouldMatchJsonSchema() {
        var response = when().get("subtypes").then();
        assertJsonSchema("subtypes.schema.json", response);
    }

}
