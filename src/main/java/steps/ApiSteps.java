package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.other.*;
import models.response.*;
import models.request.*;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

public class ApiSteps {
    @Step ("Авторизация")
    public LoginTokenResponse postLogin(LoginRequest LogReq, Integer statusCode) {
        return given()
                .log().all()
                .baseUri("http://localhost:8080")
                .body(LogReq)
                .contentType(ContentType.JSON)
                .post("/login")
                .then()
                .statusCode(statusCode)
                .extract()
                .response().as(LoginTokenResponse.class);
    }
    @Step ("Получение списка свободных телефонов")
    public PhonesResponse getEmptyPhones(String token, Integer statusCode) {
        return given()
                .log().all()
                .header("authToken", token)
                .baseUri("http://localhost:8080")
                .get("/simcards/getEmptyPhone")
                .then()
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .response().as(PhonesResponse.class);
    }
    @Step ("Создание нового кастомера")
    public IdResponse postCreateCustomer(String token, CustomerRequest CastReq, Integer statusCode) {
        return given()
                .log().all()
                .header("authToken", token)
                .baseUri("http://localhost:8080")
                .body(CastReq)
                .contentType(ContentType.JSON)
                .post("/customer/postCustomer")
                .then()
                .statusCode(statusCode)
                .extract()
                .response().as(IdResponse.class);
    }
    @Step ("Проверка корректности активации кастомера")
    public CustomerById getCustomerById(String token, String customerId, Integer statusCode) {
        return given()
                .log().all()
                .header("authToken", token)
                .baseUri("http://localhost:8080")
                .get("/customer/getCustomerById" + "?customerId=" + customerId)
                .then()
                .statusCode(statusCode)
                .extract()
                .response().as(CustomerById.class);
    }
    @Step ("Проверка что кастомер сохранился в старой системе")
    public Response postCustomerByPhone(String token, Long number, Integer statusCode) {
        return given()
                .log().all()
                .baseUri("http://localhost:8080")
                .body("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns3:Envelope xmlns:ns2=\"soap\" xmlns:ns3=\"http://schemas.xmlsoap.org/soap/envelope\">\n" +
                        "    <ns2:Header>\n" +
                        "        <authToken>" + token + "</authToken>\n" +
                        "    </ns2:Header>\n" +
                        "    <ns2:Body>\n" +
                        "        <phoneNumber>" + number + "</phoneNumber>\n" +
                        "    </ns2:Body>\n" +
                        "</ns3:Envelope>")
                .contentType(ContentType.XML)
                .post("/customer/findByPhoneNumber")
                .then()
                .statusCode(statusCode)
                .extract()
                .response();
    }
    @Step ("Смена статуса кастомера")
    public void postChangeCustomerStatus(StatusRequest StReq, String token, String customerId, Integer statusCode) {
        given()
                .log().all()
                .header("authToken", token)
                .baseUri("http://localhost:8080")
                .body(StReq)
                .contentType(ContentType.JSON)
                .post("/customer/" + customerId + "/changeCustomerStatus")
                .then()
                .statusCode(statusCode)
                .extract()
                .response();
    }
    @Step ("Сравнение")
    public void checkEqualResponse (Object expectedResult, Object actualResult) {
        Assert.assertEquals(expectedResult, actualResult);
    }
}