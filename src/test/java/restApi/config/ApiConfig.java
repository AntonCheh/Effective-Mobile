package restApi.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import restApi.utils.ConfigLoader;

public class ApiConfig {
    private static RequestSpecification requestSpecification;

    @BeforeSuite
    public static void setup() {
        String baseUrl = ConfigLoader.getBaseUrl();
        String apiKey = ConfigLoader.getApiKey();

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("x-api-key", apiKey)
                .setContentType("application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        RestAssured.requestSpecification = requestSpecification;
    }

    public static RequestSpecification getRequestSpec() {
        return requestSpecification;
    }
}
