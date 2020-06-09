package matcha.model;

import lombok.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private int id;
    private String login;
    @ToString.Exclude
    private byte[] password;
    @ToString.Exclude
    private String activationCode;
    private String fname;
    private String lname;
    private String email;
    private boolean active;
    private boolean blocked;
    private Date time = Calendar.getInstance().getTime();
    @ToString.Exclude
    private byte[] salt;
    private Integer profileId = null;
}
