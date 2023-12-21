package models.other;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.response.CustomerResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerById {
    @JsonProperty("return")
    public CustomerResponse myreturn;
}
