package restApi.reqressTest;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import restApi.client.UserApiClient;
import restApi.config.ApiConfig;
import restApi.pojo.users.CreateUserRequest;
import restApi.pojo.users.CreateUserResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestUsers {

    private UserApiClient userApiClient;

    @BeforeClass
    public void setUp() {
        ApiConfig.setup(); // Инициализация конфигурации
        userApiClient = new UserApiClient(ApiConfig.getRequestSpec());
    }

    @Test
    public void firstTest() {

        String apiKey = "pub_f3d2f767a6d92da70df78b4163eaf51803eebb3c5d462b5f8b978a52fbf7c095"; // Замените на реальный ключ

        given()
                .baseUri("https://api.reqres.in/")
                .header("x-api-key", apiKey)  // Добавляем заголовок с ключом
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .log().body()
                .body("page", notNullValue())
                .body("data.id", not(hasItem(nullValue())))
                .body("data.first_name", hasItem("George"))
                .statusCode(200);

    }

    @Test
    public void secondTest() {
        String apiKey = "pub_f3d2f767a6d92da70df78b4163eaf51803eebb3c5d462b5f8b978a52fbf7c095"; // Замените на реальный ключ

        Map<String, String> requestData = new HashMap<>();
        requestData.put("email", "chikan.bluth@reqres.in");
        requestData.put("first_name", "anton");
        requestData.put("last_name", "petrov");
        requestData.put("avatar", "https://reqres.in/img/faces/20-image.jpg");

        Response response =  given()
                .baseUri("https://api.reqres.in/")
                .header("x-api-key", apiKey)
                .contentType("application/json")
                .body(requestData)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .extract().response();

        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(requestData.get("first_name"),jsonResponse.get("first_name"));
    }

    @Test
    public void secondTestWithDto() {
        String apiKey = "pub_f3d2f767a6d92da70df78b4163eaf51803eebb3c5d462b5f8b978a52fbf7c095"; // Замените на реальный ключ

        CreateUserRequest requestData = new CreateUserRequest("chikan.bluth@reqres.in",
                "anton",
                "petrov",
                "https://reqres.in/img/faces/20-image.jpg"
        );

        CreateUserResponse response =  given()
                .baseUri("https://api.reqres.in/")
                .header("x-api-key", apiKey)
                .contentType("application/json")
                .body(requestData)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .extract().as(CreateUserResponse.class);

        Assert.assertEquals(response.getFirstName(), requestData.getFirstName());
        Assert.assertEquals(response.getLastName(), requestData.getLastName());
        Assert.assertEquals(response.getEmail(), requestData.getEmail());
        Assert.assertEquals(response.getAvatar(), requestData.getAvatar());

        // Дополнительные проверки
        Assert.assertNotNull(response.getId(), "ID не должен быть null");
        Assert.assertNotNull(response.getCreatedAt(), "createdAt не должен быть null");

        System.out.println("Создан пользователь с ID: " + response.getId());
        System.out.println("Дата создания: " + response.getCreatedAt());
    }

    @Test
    public void createUserTest() {
        // Создаем запрос с параметризованными данными
        CreateUserRequest request = new CreateUserRequest("chikan.bluth@reqres.in",
                "anton",
                "petrov",
                "https://reqres.in/img/faces/20-image.jpg");

        // Выполняем запрос через клиент
        CreateUserResponse response = userApiClient.createUser(request);

        // Валидация
        Assert.assertEquals(response.getFirstName(), request.getFirstName(),
                "First name mismatch");
        Assert.assertEquals(response.getLastName(), request.getLastName(),
                "Last name mismatch");
        Assert.assertEquals(response.getEmail(), request.getEmail(),
                "Email mismatch");
        Assert.assertEquals(response.getAvatar(), request.getAvatar(),
                "Avatar mismatch");

        Assert.assertNotNull(response.getId(), "ID should not be null");
        Assert.assertNotNull(response.getCreatedAt(), "CreatedAt should not be null");

        System.out.printf("User created successfully: %s %s (ID: %s)%n",
                response.getFirstName(), response.getLastName(), response.getId());
    }
}
