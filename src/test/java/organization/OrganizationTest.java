package organization;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganizationTest extends BaseTest {

    private String fakeName;
    private String fakeWebsite;

    private static String firstTeamId;
    private static String secondTeamId;

    @BeforeEach
    public void beforeEach() {
        fakeName = faker.name().name();
        fakeWebsite = faker.internet().url();
    }

    public void deleteTeam(String teamId) {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + teamId)
                .then()
                .statusCode(200);
    }

    @Test
    @Order(1)
    public void createFirstTeam() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Pierwszy zespół")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("displayName")).isEqualTo("Pierwszy zespół");

        firstTeamId = json.getString("id");
        deleteTeam(firstTeamId);
    }

    @Test
    @Order(2)
    public void checkIfNameIsValid() {

        JSONObject team = new JSONObject();
        team.put("name", fakeName);
        team.put("website", fakeWebsite);

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Drugi zespół")
                .body(team.toString())
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("displayName")).isEqualTo("Drugi zespół");
        Assertions.assertThat(json.getString("name")).containsPattern("[a-z_\\d]{3,}");

        secondTeamId = json.getString("id");
        deleteTeam(secondTeamId);
    }

    @Test
    @Order(3)
    public void checkIfNameIsUnique() {
        JSONObject team = new JSONObject();
        team.put("name", fakeName);
        team.put("website", fakeWebsite);

        Response responseOne = given()
                .spec(reqSpec)
                .queryParam("displayName", "Trzeci zespół")
                .body(team.toString())
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonOne = responseOne.jsonPath();
        Assertions.assertThat(jsonOne.getString("displayName")).isEqualTo("Trzeci zespół");

        Response responseTwo = given()
                .spec(reqSpec)
                .queryParam("displayName", "Czwarty zespół")
                .body(team.toString())
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonTwo = responseTwo.jsonPath();
        Assertions.assertThat(jsonTwo.getString("displayName")).isEqualTo("Czwarty zespół");
        Assertions.assertThat(jsonOne.getString("name")).isNotEqualTo(jsonTwo.getString("name"));

        firstTeamId = jsonOne.getString("id");
        secondTeamId = jsonTwo.getString("id");

        deleteTeam(firstTeamId);
        deleteTeam(secondTeamId);
    }

    @Test
    @Order(4)
    public void checkWebsite() {

        JSONObject team = new JSONObject();
        team.put("name", fakeName);
        team.put("website", fakeWebsite);

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Piąty zespół")
                .body(team.toString())
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("displayName")).isEqualTo("Piąty zespół");
        if (!json.getString("website").startsWith("http://")) {
            Assertions.assertThat(json.getString("website")).startsWith("https://");
        } else {
            Assertions.assertThat(json.getString("website")).startsWith("http://");
        }

        firstTeamId = json.getString("id");
        deleteTeam(firstTeamId);
    }


}
