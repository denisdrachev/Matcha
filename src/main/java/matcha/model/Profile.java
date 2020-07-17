package matcha.model;

import lombok.*;
import matcha.converter.Converter;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements Serializable {

    private Integer id;
    private Integer age = null;
    private Integer gender = null;
    private List<Integer> preference = new ArrayList<>();
    private String biography;
    private List<String> tags = new ArrayList<>();
    private List<ImageElem> images = new ArrayList<>();
//    @ToString.Exclude
    private List<String> imagesIds;
    private Integer avatar = null;

}
