package matcha.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matcha.converter.Converter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseOkData {

    private String type;
    private String data;

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }
}
