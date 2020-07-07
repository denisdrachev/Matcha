package matcha.model;

import lombok.Data;

import java.util.Base64;

@Data
public class ImageElem {

    private int index;
    private Base64 src;
}
