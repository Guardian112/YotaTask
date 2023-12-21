import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.response.Response;
import models.other.*;
import steps.*;
import models.request.*;
import models.response.*;
import org.testng.annotations.Test;

public class Tests extends BaseTest {
    @Test (description = "Проверка сценария активации абонента по шагам из бизнес-сценария за Админа")
    public void TestAdmin() throws JsonProcessingException {
        // Пункт 1 - Пользователь авторизуется в системе под своим логином/паролем
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse token = API_STEPS.postLogin(loginAdmin, 200); // <-- Тут получаем токен
        System.out.println("Токен: " + token.getToken());

        // Пункт 2 - Пользователь получает список свободных номеров
        PhonesResponse numberList = API_STEPS.getEmptyPhones(token.getToken(), 200);
        System.out.println("Список номеров: " + numberList);

        // Пункт 3 - Пользователь создает нового кастомера
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        IdResponse customerId = API_STEPS.postCreateCustomer(token.getToken(), customer, 200); // <-- Тут получаем id
        System.out.println("Id нового пользователя: " + customerId.getId());

        // Пункт 4 - В промежуток времени до 2х минут кастомер активируется
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Пункт 5 - пользователь проверяет корректность активации кастомера
        CustomerById CustomerById = API_STEPS.getCustomerById(token.getToken(), customerId.getId(), 200);
        System.out.println("Информация о пользователе: " + CustomerById);

        // Пункт 6 - Пользователь проверяет, что кастомер сохранился в старой системе
        Response IdByPhone = API_STEPS.postCustomerByPhone(token.getToken(), 79281326346L, 200);
        FindByPhoneResponse name = new XmlMapper().readValue(IdByPhone.getBody().asString(),FindByPhoneResponse.class);
        System.out.println("Проверка сохранения пользователя (xml): " + name);

        // Пункт 7 - Пользователь admin изменяет кастомеру статус
        StatusRequest stat = new StatusRequest("ACTIVE");
        API_STEPS.postChangeCustomerStatus(stat, token.getToken(), customerId.getId(), 200);
        System.out.println("Статус пользователя изменён на ACTIVE");
        // Пункт 8 - Пользователь проверяет что изменения прошли успешно
        CustomerById NewCustomerById = API_STEPS.getCustomerById(token.getToken(), customerId.getId(), 200);
        System.out.println(NewCustomerById.getMyreturn().getStatus());
    }
    @Test (description = "Проверка сценария активации абонента по шагам из бизнес-сценария за Юзера")
    public void TestUser() throws JsonProcessingException {
        // Пункт 1 - Пользователь авторизуется в системе под своим логином/паролем
        LoginRequest loginAdmin = new LoginRequest("user", "password");
        LoginTokenResponse Token = API_STEPS.postLogin(loginAdmin, 200); // <-- Тут получаем токен
        System.out.println("Токен: " + Token.getToken());

        // Пункт 2 - Пользователь получает список свободных номеров
        PhonesResponse numberList = API_STEPS.getEmptyPhones(Token.getToken(), 200);
        System.out.println("Список номеров: " + numberList);

        // Пункт 3 - Пользователь создает нового кастомера
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        IdResponse customerId = API_STEPS.postCreateCustomer(Token.getToken(), customer, 200); // <-- Тут получаем id
        System.out.println("Id нового пользователя: " + customerId.getId());

        // Пункт 4 - В промежуток времени до 2х минут кастомер активируется
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Пункт 5 - пользователь проверяет корректность активации кастомера
        CustomerById CustomerById = API_STEPS.getCustomerById(Token.getToken(), customerId.getId(), 200);
        System.out.println("Информация о пользователе: " + CustomerById);

        // Пункт 6 - Пользователь проверяет, что кастомер сохранился в старой системе
        Response IdByPhone = API_STEPS.postCustomerByPhone(Token.getToken(), 79281326346L, 200);
        FindByPhoneResponse name = new XmlMapper().readValue(IdByPhone.getBody().asString(),FindByPhoneResponse.class);
        System.out.println("Проверка сохранения пользователя (xml): " + name);

        // Пункт 7 - Пользователь admin изменяет кастомеру статус
        StatusRequest stat = new StatusRequest("ACTIVE");
        API_STEPS.postChangeCustomerStatus(stat, Token.getToken(), customerId.getId(), 401);
        System.out.println("Статус пользователя изменён на ACTIVE");
    }
}