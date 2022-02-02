package endpoints;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class FormatEndpointTests extends ApiTestBase {

    @Test
    void Get_Formats_ShouldReturn_Success_ProperResponse()
    {
        var response = when().get("formats").then();
        assertOK(response);
    }

    @Test
    public void Get_Formats_ShouldMatchJsonSchema() {
        var response = when().get("formats").then();
        assertJsonSchema("formats.schema.json", response);
    }
}
