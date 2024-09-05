package helen.alves;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@Epic("Testes de API REST usando JUnit e Rest-Assured")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class APIScenariosTest extends BaseTest {

    private static String CONTA_NAME = "Conta " + System.nanoTime();

    @Test
    @Story("Listar Produtos")
    @Description("Este teste recupera a lista de produtos.")
    public void t01_GetProductList() {
        given()
                .filter(new AllureRestAssured())
                .when()
                .get("/productsList")
                .then()
                .log().all()
                .statusCode(200)
                .body("html.body", containsString("\"responseCode\": 200"))
                .body("html.body", containsString("\"products\":"));

    }

    @Test
    @Story("Não Deve Postar na Lista de Produtos")
    @Description("Este teste tenta postar uma lista de produtos.\n" +
            "Valida a resposta 405 - método não permitido.")
    public void t02_ShouldNotPostProductList() {
        String json = given()
                .filter(new AllureRestAssured())
                .when()
                .post("/productsList")
                .then()
                .log().all()
                .extract().body().asString();

        assertThat(json, containsString("\"responseCode\": 405"));
    }

    @Test
    @Story("Listar Marcas")
    @Description("Este teste recupera a lista de marcas.")
    public void t03_GetAllBrandsList() {
        given()
                .filter(new AllureRestAssured())
                .when()
                .get("/brandsList")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 200"))
                .body("html.body", containsString("\"brands\":"))
        ;
    }

    @Test
    @Story("Não Deve Atualizar Lista de Marcas")
    @Description("Este teste tenta atualizar uma lista de marcas.\n" +
            "Valida a resposta 405 - método não permitido.")
    public void t04_PutAllBrandsList() {
        given()
                .filter(new AllureRestAssured())
                .when()
                .put("/brandsList")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 405"))
                .body("html.body", containsString("\"message\": \"This request method is not supported.\""))
        ;
    }

    @Test
    @Story("Buscar Produtos")
    @Description("Este teste busca produtos com a palavra-chave 'top' e valida o a lista dos produtos retornados.")
    public void t05_SearchProducts() {
        Map<String, String> params = new HashMap<>();
        params.put("search_product", "top");

        String response = given()
                .filter(new AllureRestAssured())
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
    @Story("Buscar Produtos Sem Parâmetro")
    @Description("Este teste tenta buscar produtos sem fornecer o parâmetro de busca.\n" +
            "Valida a resposta 400 - requisição inválida.")
    public void t06_SearchProductsWithoutSearchParameter() {
        given()
                .filter(new AllureRestAssured())
                .log().all()
                .when()
                .post("/searchProduct")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 400"))
                .body("html.body", containsString("\"message\": \"Bad request, search_product parameter is missing in POST request.\""))
        ;
    }

    @Test
    @Story("Login com Credenciais Válidas")
    @Description("Este teste verifica a funcionalidade de login com credenciais válidas.")
    public void t07_VerifyLoginWithValidCredentials() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "helen@gmail.com");
        params.put("password", "12345");

        given()
                .filter(new AllureRestAssured())
                .params(params)
                .when()
                .post("/verifyLogin")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 200"))
                .body("html.body", containsString("\"message\": \"User exists!\""))
        ;
    }

    @Test
    @Story("Não Deve Realizar Login sem Email")
    @Description("Este teste tenta realizar o login sem fornecer o email.\n" +
            "Valida uma resposta 400 - requisição inválida.")
    public void t08_VerifyLoginWithoutEmail() {
        Map<String, String> params = new HashMap<>();
        params.put("password", "1234");
        String response = given()
                .filter(new AllureRestAssured())
                .params(params)
                .when()
                .post("/verifyLogin")
                .then()
                .log().all()
                .extract().htmlPath().getString("body");

        //Demonstração de validação transformando em Json
        JsonPath jsonPath = new JsonPath(response);
        assertThat(jsonPath.getInt("responseCode"), equalTo(400));
        assertThat(jsonPath.getString("message"), is("Bad request, email or password parameter is missing in POST request."));
    }

    @Test
    @Story("Não deve excluir Login")
    @Description("Este teste tenta excluir informações de login.\n" +
            "Valida a resposta 405 - método não permitido.")
    public void t09_DeleteVerifyLogin() {
        given()
                .filter(new AllureRestAssured())
                .when()
                .delete("/verifyLogin")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 405"))
                .body("html.body", containsString("\"message\": \"This request method is not supported.\""))
        ;
    }

    @Test
    @Story("Login com Credenciais Inválidas")
    @Description("Este teste verifica a funcionalidade de login com credenciais inválidas.\n" +
            "Valida a resposta 404 com a mensagem 'User not found!'.")
    public void t10_VerifyLoginWithInvalidCredentials() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "helen@gmail.com");
        params.put("password", "11111");

        given()
                .filter(new AllureRestAssured())
                .params(params)
                .when()
                .post("/verifyLogin")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 404"))
                .body("html.body", containsString("\"message\": \"User not found!\""))
        ;
    }

    @Test
    @Story("Criar Conta de Usuário")
    @Description("Este teste cria uma nova conta de usuário com sucesso'")
    public void t11_CreateUserAccount() {
        User user = getNewUserInfoForAccount();
        Map<String, String> user_params = convertUserToMap(user);
        given()
                .filter(new AllureRestAssured())
                .params(user_params)
                .when()
                .post("/createAccount")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 201"))
                .body("html.body", containsString("\"message\": \"User created!\""))
        ;
    }

    //error
    @Test
    @Story("Excluir Conta de Usuário")
    @Description("Este teste deve excluir uma conta de usuário com sucesso")
    public void t12_deleteUserAccount() {
        Map<String, String> delete_user = new HashMap<>();
        delete_user.put("password", "aline123");
        delete_user.put("email", "aline@email.com");

        given()
                .filter(new AllureRestAssured())
                .params(delete_user)
                .when()
                .delete("/deleteAccount")
                .then()
                .log().all()
                .body("html.body", containsString("\"responseCode\": 200"))
                .body("html.body", containsString("\"message\": \"Account deleted!\""))

        ;
    }

    //error
    @Test
    @Story("Atualizar Conta de Usuário")
    @Description("Este teste atualiza as informações de uma conta de usuário com sucesso.")
    public void t13_UpdateUserAccount() {
        User user = getNewUserInfoForAccount();
        Map<String, String> user_params = convertUserToMap(user);
        user_params.put("lastname", "Vasconcelos");
                given()
                    .filter(new AllureRestAssured())
                    .params(user_params)
                    .when()
                    .put("/updateAccount")
                    .then()
                    .log().all()
                    .body("html.body", containsString("\"responseCode\": 200"))
                    .body("html.body", containsString("\"message\": \"User updated!\""))
        ;
    }


    @Test
    @Story("Obter Detalhes do Usuário Através do Email")
    @Description("Este teste recupera os detalhes de um usuário baseado no email com sucesso")
    public void t14_GetUserByEmail() {
        Map<String, String> user_email = new HashMap<>();
        user_email.put("email", CONTA_NAME + "@email.com");
        String response = given()
                .filter(new AllureRestAssured())
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
