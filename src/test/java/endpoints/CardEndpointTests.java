package endpoints;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.*;

public class CardEndpointTests extends ApiTestBase  {

    private ValidatableResponse makeRequest(String cardId) {
        return given()
                    .pathParam("cardId", cardId).
                when()
                    .get("cards/{cardId}").then();
    }

    private ValidatableResponse makeRequest(int cardId) {
        return given()
                    .pathParam("cardId", cardId).
                when()
                    .get("cards/{cardId}").then();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "22b6b1af-988d-546a-b280-24a1b67b50a1",
            "b79cae48-4768-5080-b44a-b15c1b5cc692",
            "9e01509c-37c2-53e6-8e6e-5ba098d24231"
    })
    void Get_ShouldReturn_Success_ProperResponse(String cardId)
    {
        var response = makeRequest(cardId);
        assertOK(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "4a5b13d9-cce9-4b9f-9a32-27f66db2f46a",
            "b363f265-fbd6-450c-9124-d4e31054c7f1",
            "5d805e06-626e-4713-995d-36390385bf61"
    })
    void Get_ById_ShouldReturn_NotFound_IfCardDontExist(String cardId)
    {
        var response = makeRequest(cardId);
        assertNotFound(response);
    }

    @ParameterizedTest
    @ValueSource(ints = { 100000, -1, 0 })
    void Get_ByMultiverseId_ShouldReturn_NotFound_IfCardDontExist(int cardId)
    {
        var response = makeRequest(cardId);
        assertNotFound(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3388f9c9-f76c-510e-bbf3-812b751637a4",
            "04d1cebd-93bb-5b16-87e2-b228742cab9e"
    })
    public void Get_ById_ShouldMatchJsonSchema(String cardId)
    {
        var response = makeRequest(cardId);
        assertJsonSchema("card.schema.json", response);
    }

    @ParameterizedTest
    @ValueSource(ints = { 97051, 457145 })
    public void Get_ByMultiverseId_ShouldMatchJsonSchema(int cardId)
    {
        var response = makeRequest(cardId);
        assertJsonSchema("card.schema.json", response);
    }
}
