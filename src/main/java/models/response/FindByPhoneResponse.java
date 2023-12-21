package models.response;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.other.Header;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindByPhoneResponse {
    @JacksonXmlProperty(localName = "Header")
    public Header header;
    @JacksonXmlProperty(localName = "Body")
    public BodyResponse bodyResponse;
}
