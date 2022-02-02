package endpoints;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.HeaderLinks;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.net.URISyntaxException;
import java.util.Objects;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public abstract class ApiTestBase {

    @BeforeAll
    public static void setup() {
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> {
                    ObjectMapper om = new ObjectMapper().findAndRegisterModules();
                    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    return om;
                }
        ));

        RestAssured.baseURI = "https://api.magicthegathering.io/v1/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    public void assertLinkHeaders(Response response, Integer page, Integer pageSize) throws URISyntaxException {

        int total = Integer.parseInt(response.getHeader("total-count"));
        var resultHeaderLinks = response.headers().getValue("link");

        var lastPage = Math.ceil(total / (float)pageSize);
        var expectedLinks = new HeaderLinks();
        if (page > 1) {
            expectedLinks.setFirst(true);
            expectedLinks.setPrev(true);
        }
        if (page < lastPage) {
            expectedLinks.setNext(true);
            expectedLinks.setLast(true);
        }

        var headerLinks = new HeaderLinks();

        for (var resultHeaderLink: resultHeaderLinks.split(",")) {
            var headerLink = resultHeaderLink.trim().split(";");
            var url = headerLink[0].subSequence(1, headerLink[0].length() - 1).toString();
            var rel = headerLink[1].subSequence(6, headerLink[1].length() - 1).toString();

            var builder = new URIBuilder(url);
            var queryPageParam = 0;
            for (var param: builder.getQueryParams()){
                if (Objects.equals(param.getName(), "page")) {
                    queryPageParam = Integer.parseInt(param.getValue());
                }
            }

            switch (rel) {
                case "first" -> {
                    headerLinks.setFirst(true);
                    headerLinks.setFirstPage(queryPageParam);
                }
                case "last" -> {
                    headerLinks.setLast(true);
                    headerLinks.setLastPage(queryPageParam);
                }
                case "next" -> {
                    headerLinks.setNext(true);
                    headerLinks.setNextPage(queryPageParam);
                }
                case "prev" -> {
                    headerLinks.setPrev(true);
                    headerLinks.setPrevPage(queryPageParam);
                }
            }
        }

        Assertions.assertEquals(expectedLinks.isFirst(), headerLinks.isFirst());
        if (expectedLinks.isFirst()) Assertions.assertEquals(1, headerLinks.getFirstPage());
        Assertions.assertEquals(expectedLinks.isLast(), headerLinks.isLast());
        if (expectedLinks.isLast()) Assertions.assertEquals(lastPage, headerLinks.getLastPage());
        Assertions.assertEquals(expectedLinks.isNext(), headerLinks.isNext());
        if (expectedLinks.isNext()) Assertions.assertEquals(page + 1, headerLinks.getNextPage());
        Assertions.assertEquals(expectedLinks.isPrev(), headerLinks.isPrev());
        if (expectedLinks.isPrev()) Assertions.assertEquals(page - 1, headerLinks.getPrevPage());
    }


    public void assertJsonSchema(String schema, ValidatableResponse response){
        response.assertThat().body(matchesJsonSchemaInClasspath(schema));
    }

    public void assertOK(ValidatableResponse response){
        response
            .assertThat().statusCode(200)
            .assertThat().contentType(ContentType.JSON);
    }

    public void assertNotFound(ValidatableResponse response){
        response
            .assertThat().statusCode(404)
            .assertThat().contentType(ContentType.JSON)
            .assertThat().body("status", is(404))
            .assertThat().body("error", is("Not Found"));
    }

    public void assertPaginationHeaders(ValidatableResponse response, Integer pageSize, Integer count) {
        response
                .assertThat().header("page-size", is(pageSize.toString()))
                .assertThat().header("count", is(count.toString()))
                .assertThat().header("total-count", notNullValue());
    }

}
