package models.request;
//import com.fasterxml.jackson.annotation.JsonProperty; @JsonProperty("first_name")
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
        private String login;
        private String password;

}
