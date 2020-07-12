package matcha.model;

import lombok.*;
import matcha.converter.Converter;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class Image implements Serializable {

    private int index;
    private String src;
    private boolean main;

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }
}
