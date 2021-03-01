package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static final String BASE_URL = "https://api.trello.com/1";
    protected static final String BOARDS = "boards";
    protected static final String LISTS = "lists";
    protected static final String CARDS = "cards";
    protected static final String ORGANIZATIONS = "organizations";

    protected static final String KEY = "850d5ebf1bb7287b29885a89f2e51190";
    protected static final String TOKEN = "468d925f343825589639734de5b3dac105a7b87b44706c2aa5f4b81251f2767f";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;
    protected static Faker faker;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder=new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        reqSpec = reqBuilder.build();
        faker = new Faker();
    }
}
