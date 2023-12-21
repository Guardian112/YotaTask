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
    public void ABCAdmin() throws JsonProcessingException {
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 1 - Пользователь авторизуется в системе под своим логином/паролем
        LoginRequest loginAdmin = new LoginRequest("admin", "password");
        LoginTokenResponse token = ADMIN_STEPS.postLoginAdm(loginAdmin); // <-- Тут получаем токен
        System.out.println("Токен: " + token.getToken());
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 2 - Пользователь получает список свободных номеров
        PhonesResponse numberList = ADMIN_STEPS.getEmptyPhonesAdm(token.getToken());
        System.out.println("Список номеров: " + numberList);
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 3 - Пользователь создает нового кастомера
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        IdResponse customerId = ADMIN_STEPS.postCreateCustomerAdm(token.getToken(), customer); // <-- Тут получаем id
        System.out.println("Id нового пользователя: " + customerId.getId());
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 4 - В промежуток времени до 2х минут кастомер активируется
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 5 - пользователь проверяет корректность активации кастомера
        CustomerById CustomerById = ADMIN_STEPS.getCustomerByIdAdm(token.getToken(), customerId.getId());
        System.out.println("Информация о пользователе: " + CustomerById);
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 6 - Пользователь проверяет, что кастомер сохранился в старой системе
        Response IdByPhone = ADMIN_STEPS.postCustomerByPhoneAdm(token.getToken(), 79281326346L);
        FindByPhoneResponse name = new XmlMapper().readValue(IdByPhone.getBody().asString(),FindByPhoneResponse.class);
        System.out.println("Проверка сохранения пользователя (xml): " + name);
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 7 - Пользователь admin изменяет кастомеру статус
        StatusRequest stat = new StatusRequest("ACTIVE");
        ADMIN_STEPS.postChangeCustomerStatusAdm(stat, token.getToken(), customerId.getId());
        System.out.println("Статус пользователя изменён на ACTIVE");
//--------------------------------------------------------------------------------------------------------------------
    }
    @Test (description = "Проверка сценария активации абонента по шагам из бизнес-сценария за Юзера")
    public void ABCUser() throws JsonProcessingException {
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 1 - Пользователь авторизуется в системе под своим логином/паролем
        LoginRequest loginAdmin = new LoginRequest("user", "password");
        LoginTokenResponse Token = USER_STEPS.postLoginUser(loginAdmin); // <-- Тут получаем токен
        System.out.println("Токен: " + Token.getToken());
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 2 - Пользователь получает список свободных номеров
        PhonesResponse numberList = USER_STEPS.getEmptyPhonesUser(Token.getToken());
        System.out.println("Список номеров: " + numberList);
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 3 - Пользователь создает нового кастомера
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        IdResponse customerId = USER_STEPS.postCreateCustomerUser(Token.getToken(), customer); // <-- Тут получаем id
        System.out.println("Id нового пользователя: " + customerId.getId());
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 4 - В промежуток времени до 2х минут кастомер активируется
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 5 - пользователь проверяет корректность активации кастомера
        CustomerById CustomerById = USER_STEPS.getCustomerByIdUser(Token.getToken(), customerId.getId());
        System.out.println("Информация о пользователе: " + CustomerById);
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 6 - Пользователь проверяет, что кастомер сохранился в старой системе
        Response IdByPhone = USER_STEPS.postCustomerByPhoneUser(Token.getToken(), 79281326346L);
        FindByPhoneResponse name = new XmlMapper().readValue(IdByPhone.getBody().asString(),FindByPhoneResponse.class);
        System.out.println("Проверка сохранения пользователя (xml): " + name);
//--------------------------------------------------------------------------------------------------------------------
        // Пункт 7 - Пользователь admin изменяет кастомеру статус
        StatusRequest stat = new StatusRequest("ACTIVE");
        USER_STEPS.postChangeCustomerStatusUser(stat, Token.getToken(), customerId.getId());
        System.out.println("Статус пользователя изменён на ACTIVE");
//--------------------------------------------------------------------------------------------------------------------
    }
}