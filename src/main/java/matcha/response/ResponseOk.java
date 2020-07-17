package matcha.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matcha.converter.Converter;
import matcha.model.MyObject;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseOk implements MyObject {

    private String type;
    private String token;
    private String login;

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }
}
