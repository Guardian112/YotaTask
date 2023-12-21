package steps.apiSteps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.other.*;
import models.response.*;
import models.request.*;

import static io.restassured.RestAssured.given;

public class AdminSteps {
    @Step ("Авторизация")
    public LoginTokenResponse postLoginAdm(LoginRequest LogReq) {
        return given()
                .baseUri("http://localhost:8080")
                .body(LogReq)
                .contentType(ContentType.JSON)
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .response().as(LoginTokenResponse.class);
    }
    @Step ("Получение списка свободных телефонов")
    public PhonesResponse getEmptyPhonesAdm(String Token, Integer statusCode) {
        return given()
                .header("authToken", Token)
                .baseUri("http://localhost:8080")
                .get("/simcards/getEmptyPhone")
                .then()
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .response().as(PhonesResponse.class);
    }
    @Step ("Создание нового кастомера")
    public IdResponse postCreateCustomerAdm(String Token, CustomerRequest CastReq) {
        return given()
                .header("authToken", Token)
                .baseUri("http://localhost:8080")
                .body(CastReq)
                .contentType(ContentType.JSON)
                .post("/customer/postCustomer")
                .then()
                .statusCode(200)
                .extract()
                .response().as(IdResponse.class);
    }
    @Step ("Проверка корректности активации кастомера")
    public CustomerById getCustomerByIdAdm(String Token, String customerId) {
        return given()
                .header("authToken", Token)
                .baseUri("http://localhost:8080")
                .get("/customer/getCustomerById" + "?customerId=" + customerId)
                .then()
                .statusCode(200)
                .extract()
                .response().as(CustomerById.class);
    }
    @Step ("Проверка что кастомер сохранился в старой системе")
    public Response postCustomerByPhoneAdm(String Token, Long Number) {
        return given()
                .baseUri("http://localhost:8080")
                .body("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns3:Envelope xmlns:ns2=\"soap\" xmlns:ns3=\"http://schemas.xmlsoap.org/soap/envelope\">\n" +
                        "    <ns2:Header>\n" +
                        "        <authToken>" + Token + "</authToken>\n" +
                        "    </ns2:Header>\n" +
                        "    <ns2:Body>\n" +
                        "        <phoneNumber>" + Number + "</phoneNumber>\n" +
                        "    </ns2:Body>\n" +
                        "</ns3:Envelope>")
                .contentType(ContentType.XML)
                .post("/customer/findByPhoneNumber")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
    @Step ("Смена статуса кастомера")
    public void postChangeCustomerStatusAdm(StatusRequest StReq, String Token, String customerId) {
        given()
                .header("authToken", Token)
                .baseUri("http://localhost:8080")
                .body(StReq)
                .contentType(ContentType.JSON)
                .post("/customer/" + customerId + "/changeCustomerStatus")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}