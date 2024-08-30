package helen.alves;


import io.restassured.path.json.JsonPath;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class APIScenarios extends BaseTest {

    private static String CONTA_NAME = "Conta " + System.nanoTime();

    @Test
    public void API_1_GetProductList() {
        given()
                .when()
                .get("/productsList")
                .then()
                .statusCode(200)
                .body("html.body", containsString("\"responseCode\": 200"))
                .body("html.body", containsString("\"products\":"));
        ;

    }

    @Test
    public void API_2_ShouldNotPostProductList() {
        String json = given()
                .when()
                .post("/productsList")
                .then()
                .extract().body().asString();

        assertThat(json, containsString("\"responseCode\": 405"));
    }

    @Test
    public void API_3_GetAllBrandsList() {
        given()
                .when()
                .get("/brandsList")
                .then()
                .body("html.body", containsString("\"responseCode\": 200"))
                .body("html.body", containsString("\"brands\":"))
        ;
    }

    @Test
    public void API_4_PutAllBrandsList() {
        given()
                .log().all()
                .when()
                .put("/brandsList")
                .then()
                .body("html.body", containsString("\"responseCode\": 405"))
                .body("html.body", containsString("\"message\": \"This request method is not supported.\""))
        ;
    }

    @Test
    public void API_5_SearchProducts() {
        Map<String, String> params = new HashMap<>();
        params.put("search_product", "top");

        String response = given()
                .log().all()
                .params(params)
                .when()
                .post("/searchProduct")
                .then()
                .log().all()
                .extract().body().asString();

        JsonPath jsonPath = new JsonPath(response);
        assertThat(jsonPath.getInt("responseCode"), equalTo(200));
        assertThat(jsonPath.getString("products.name"), containsString("Top"));
    }

    @Test
    public void API_6_SearchProductsWithoutSearchParameter() {
        given()
                .log().all()
                .when()
                .post("/searchProduct")
                .then()
                .body("html.body", containsString("\"responseCode\": 400"))
                .body("html.body", containsString("\"message\": \"Bad request, search_product parameter is missing in POST request.\""))
        ;
    }

    @Test
    public void API_7_VerifyLoginWithValidCredentials() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "helen@gmail.com");
        params.put("password", "12345");

        given()
                .params(params)
                .when()
                .post("/verifyLogin")
                .then()
                .body("html.body", containsString("\"responseCode\": 200"))
                .body("html.body", containsString("\"message\": \"User exists!\""))
        ;
    }

    @Test
    public void API_8_VerifyLoginWithoutEmail() {
        Map<String, String> params = new HashMap<>();
        params.put("password", "1234");
        String response = given()
                .params(params)
                .when()
                .post("/verifyLogin")
                .then()
                .extract().htmlPath().getString("body");

        //Demonstração de validação transformando em Json
        JsonPath jsonPath = new JsonPath(response);
        assertThat(jsonPath.getInt("responseCode"), equalTo(400));
        assertThat(jsonPath.getString("message"), is("Bad request, email or password parameter is missing in POST request."));
    }

    @Test
    public void API_9_DeleteVerifyLogin() {
        given()
                .when()
                .delete("/verifyLogin")
                .then()
                .body("html.body", containsString("\"responseCode\": 405"))
                .body("html.body", containsString("\"message\": \"This request method is not supported.\""))
        ;
    }

    @Test
    public void API_10_VerifyLoginWithInvalidCredentials() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "helen@gmail.com");
        params.put("password", "11111");

        given()
                .params(params)
                .when()
                .post("/verifyLogin")
                .then()
                .body("html.body", containsString("\"responseCode\": 404"))
                .body("html.body", containsString("\"message\": \"User not found!\""))
        ;
    }

    @Test
    public void API_11_CreateUserAccount() {
        User user = getNewUserInfoForAccount();
        Map<String, String> user_params = convertUserToMap(user);
        given()
                .params(user_params)
                .when()
                .post("/createAccount")
                .then()
                .body("html.body", containsString("\"responseCode\": 201"))
                .body("html.body", containsString("\"message\": \"User created!\""))
        ;
    }


    //error
    @Test
    public void API_12_deleteUserAccount() {
        Map<String, String> delete_user = new HashMap<>();
        delete_user.put("password", "aline123");
        delete_user.put("email", "aline@email.com");

        given()
                .log().all()
                .params(delete_user)
                .when()
                .delete("/deleteAccount")
                .then()
                .log().all()
        ;
    }

    //error
    @Test
    public void API_13_UpdateUserAccount() {
        User user = getNewUserInfoForAccount();
        Map<String, String> user_params = convertUserToMap(user);
        user_params.put("lastname", "Vasconcelos");
                given()
                .params(user_params)
                .when()
                .put("/updateAccount")
                .then()
                .log().all()
        ;
    }


    @Test
    public void API_14_GetUserByEmail() {
        Map<String, String> user_email = new HashMap<>();
        user_email.put("email", CONTA_NAME + "@email.com");
        String response = given()
                .params(user_email)
                .when()
                .get("/getUserDetailByEmail")
                .then()
                .log().all()
                .extract().htmlPath().getString("body");

        JsonPath jsonPath = new JsonPath(response);
        assertThat(jsonPath.getInt("responseCode"), equalTo(200));
        assertThat(jsonPath.getString("user.first_name"), is("Alana"));
        assertThat(jsonPath.getString("user.company"), is("accltda"));
        assertThat(jsonPath.getString("user.address1"), is("rua dos bobos"));

    }

    private User getNewUserInfoForAccount(){
        User user = new User();

        user.setName(CONTA_NAME);
        user.setEmail(CONTA_NAME + "@email.com");
        user.setPassword("12345");
        user.setTitle("Mrs");
        user.setBirth_date("10");
        user.setBirth_month("11");
        user.setBirth_year(2001);
        user.setFirstname("Alana");
        user.setLastname("Alves");
        user.setCompany("accltda");
        user.setAddress1("rua dos bobos");
        user.setAddress2("Casa Amarela");
        user.setCountry("Brasil");
        user.setZipcode("53876590");
        user.setState("Pernambuco");
        user.setCity("Recife");
        user.setMobile_number("819999999999");
        return user;
    }

    // Method to convert User object to Map
    private Map<String, String> convertUserToMap(User user) {
        Map<String, String> userParams = new HashMap<>();
        userParams.put("name", user.getName());
        userParams.put("email", user.getEmail());
        userParams.put("password", user.getPassword());
        userParams.put("title", user.getTitle());
        userParams.put("birth_date", user.getBirth_date());
        userParams.put("birth_month", user.getBirth_month());
        userParams.put("birth_year", String.valueOf(user.getBirth_year()));
        userParams.put("firstname", user.getFirstname());
        userParams.put("lastname", user.getLastname());
        userParams.put("company", user.getCompany());
        userParams.put("address1", user.getAddress1());
        userParams.put("address2", user.getAddress2());
        userParams.put("country", user.getCountry());
        userParams.put("zipcode", user.getZipcode());
        userParams.put("state", user.getState());
        userParams.put("city", user.getCity());
        userParams.put("mobile_number", user.getMobile_number());

        return userParams;
    }

}
