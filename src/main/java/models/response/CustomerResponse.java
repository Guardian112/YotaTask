package models.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.other.AdditionalParameters;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    public String customerId;
    public String name;
    public String status;
    public Long phone;
    public AdditionalParameters additionalParameters;
    public String pd;
}