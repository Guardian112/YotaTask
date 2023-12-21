import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.response.Response;
import models.other.*;
import steps.*;
import models.request.*;
import models.response.*;
import org.testng.annotations.Test;

public class UserTests extends BaseTest {
    @Test (description = "Проверка сценария активации абонента по шагам из бизнес-сценария за Юзера")
    public void TestUser() throws JsonProcessingException, InterruptedException {
        // Пункт 1 - Пользователь авторизуется в системе под своим логином/паролем
        LoginRequest loginAdmin = new LoginRequest("user", "password");
        LoginTokenResponse Token = API_STEPS.postLogin(loginAdmin, 200); // <-- Тут получаем токен
        System.out.println("Токен: " + Token.getToken() + "\n");

        // Пункт 2 - Пользователь получает список свободных номеров
        PhonesResponse numberList = API_STEPS.getEmptyPhones(Token.getToken(), 200);
        System.out.println("Список номеров: " + numberList + "\n");

        // Пункт 3 - Пользователь создает нового кастомера
        AdditionalParameters addPar = new AdditionalParameters("string"); // <-- Тут определяем доп. параметры
        CustomerRequest customer = new CustomerRequest("sdfsdf", 79281326346L, addPar);
        IdResponse customerId = API_STEPS.postCreateCustomer(Token.getToken(), customer, 200); // <-- Тут получаем id
        System.out.println("Id нового пользователя: " + customerId.getId() + "\n");

        // Пункт 4 - В промежуток времени до 2х минут кастомер активируется
        Thread.sleep(120000);

        // Пункт 5 - пользователь проверяет корректность активации кастомера
        CustomerById CustomerById = API_STEPS.getCustomerById(Token.getToken(), customerId.getId(), 200);
        API_STEPS.checkEqualResponse("ACTIVE", CustomerById.getMyreturn().getStatus());
        System.out.println("Информация о пользователе: " + CustomerById + "\n");

        // Пункт 6 - Пользователь проверяет, что кастомер сохранился в старой системе
        Response IdByPhone = API_STEPS.postCustomerByPhone(Token.getToken(), 79281326346L, 200);
        FindByPhoneResponse name = new XmlMapper().readValue(IdByPhone.getBody().asString(),FindByPhoneResponse.class);
        System.out.println("Проверка сохранения пользователя (xml): " + name + "\n");

        // Пункт 7 - Пользователь admin изменяет кастомеру статус
        StatusRequest stat = new StatusRequest("BOSS");
        API_STEPS.postChangeCustomerStatus(stat, Token.getToken(), customerId.getId(), 401);
        System.out.println("Статус пользователя изменён на BOSS");
    }
}
