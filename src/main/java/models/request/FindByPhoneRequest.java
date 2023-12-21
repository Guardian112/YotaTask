package models.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.other.Header;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindByPhoneRequest {
    public Header header;
    public BodyRequest body;
}
