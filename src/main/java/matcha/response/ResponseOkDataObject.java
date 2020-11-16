package matcha.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matcha.model.MyObject;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseOkDataObject implements MyObject, Response {

    private String type;
    private Object data;

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
