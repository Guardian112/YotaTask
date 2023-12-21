import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.response.Response;
import models.other.*;
import steps.BaseTest;
import models.request.*;
import models.response.*;
import org.testng.annotations.Test;

public class Tests extends BaseTest {
    @Test(description = "Пользователь авторизуется в системе под своим логином/паролем")
    public void Login() {
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse Token = ADMIN_STEPS.postLoginAdm(loginAdmin); // <-- Тут получаем токен
        System.out.println(Token.getToken());
    }

    @Test(description = "Пользователь получает список свободных номеров")
    public void EmptyPhones() {
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse Token = ADMIN_STEPS.postLoginAdm(loginAdmin); // <-- Тут получаем токен
        PhonesResponse numberList = ADMIN_STEPS.getEmptyPhonesAdm(Token.getToken(), 200);
        System.out.println(numberList);
    }

    // Этот надо повторять до успеха

    @Test(description = "Пользователь создает нового кастомера")
    public void CreateCustomer() {
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse Token = ADMIN_STEPS.postLoginAdm(loginAdmin); // <-- Тут получаем токен
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        IdResponse customerId = ADMIN_STEPS.postCreateCustomerAdm(Token.getToken(), customer); // <-- Тут получаем id
        System.out.println(customerId.getId());
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(description = "Пользователь проверяет корректность активации кастомера")
    public void CustomerById() {
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse Token = ADMIN_STEPS.postLoginAdm(loginAdmin); // <-- Тут получаем токен
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        IdResponse customerId = ADMIN_STEPS.postCreateCustomerAdm(Token.getToken(), customer); //<-- Тут получаем id
        CustomerById CustomerById = ADMIN_STEPS.getCustomerByIdAdm(Token.getToken(), customerId.getId());
        System.out.println(CustomerById);
    }

    @Test(description = "Пользователь проверяет, что кастомер сохранился в старой системе")
    public void CustomerByPhone() throws JsonProcessingException {
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse Token = ADMIN_STEPS.postLoginAdm(loginAdmin); // <-- Тут получаем токен
        Response IdByPhone = ADMIN_STEPS.postCustomerByPhoneAdm(Token.getToken(), 79281326346L);
        FindByPhoneResponse name = new XmlMapper().readValue(IdByPhone.getBody().asString(),FindByPhoneResponse.class);
        System.out.println(name);
    }

    @Test(description = "Пользователь меняет кастомеру статус")
    public void ChangeCustomerStatus() {
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse Token = ADMIN_STEPS.postLoginAdm(loginAdmin); // <-- Тут получаем токен
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        StatusRequest stat = new StatusRequest("ACTIVE");
        IdResponse customerId = ADMIN_STEPS.postCreateCustomerAdm(Token.getToken(), customer); //<-- Тут получаем id
        ADMIN_STEPS.postChangeCustomerStatusAdm(stat, Token.getToken(), customerId.getId());
    }
}