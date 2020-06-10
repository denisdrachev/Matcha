package matcha.model;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements Serializable {

    private int id;
    private int age;
    private int gender;
    private int preference;
    private String biography;
    private List<String> tags;
    private List<String> images;
    private int avatar;
}
