package matcha.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    private int id;
    private int index;
    private String src;
    private int userId;
    private boolean avatar;
}
