package endpoints;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class FormatEndpointTests extends ApiTestBase {

    @Test
    void Get_Formats_ShouldReturn_Success_ProperResponse()
    {
        ValidatableResponse response = when().get("formats").then();
        assertOK(response);
    }

    @Test
    public void Get_Formats_ShouldMatchJsonSchema() {
        ValidatableResponse response = when().get("formats").then();
        assertJsonSchema("formats.schema.json", response);
    }
}
