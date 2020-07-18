package matcha.response;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matcha.converter.Converter;
import matcha.model.MyObject;
import org.json.JSONObject;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseOkData implements MyObject {

    private String type;
    private JSONObject data;

    @Override
    public String toString() {
        return "{\"type\":\"" + type + "\"" +
                ", \"data\":" + data +
                '}';
    }

    //    @Override
//    public String toString() {
//        return Converter.objectToJson(this).get();
//    }
}
