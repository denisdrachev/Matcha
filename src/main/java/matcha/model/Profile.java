package matcha.model;

import lombok.*;

import java.io.Serializable;

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
    private String tags;
    private String images;
    private int avatar;
}
