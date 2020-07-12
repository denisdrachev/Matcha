package matcha.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import matcha.converter.Converter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageElem {

    private int id;
    private int index;
    private String src;

    public ImageElem(int index, String src) {
        this.index = index;
        this.src = src;
    }

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }
}
