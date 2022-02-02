package endpoints;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class TypesEndpointTests extends ApiTestBase {

    @Test
    void Get_Types_ShouldReturn_Success_ProperResponse()
    {
        var response = when().get("types").then();
        assertOK(response);
    }

    @Test
    public void Get_Types_ShouldMatchJsonSchema() {
        var response = when().get("types").then();
        assertJsonSchema("types.schema.json", response);
    }

}
