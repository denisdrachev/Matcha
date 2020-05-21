package matcha.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {

    private int id;
    private int profile;
    private String location;
}
