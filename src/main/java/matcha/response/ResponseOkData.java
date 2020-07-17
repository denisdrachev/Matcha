package matcha.response;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class ResponseOkData implements MyObject {

    private String type;
    private String data;

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }
}
