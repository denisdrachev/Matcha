package matcha.model;

import lombok.*;
import matcha.converter.Converter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, MyObject{

    private int id;
    private String login;
    @ToString.Exclude
    private String password;
    @ToString.Exclude
    private byte[] passwordBytes;
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
    @ToString.Exclude
    private Location location;

//    public void setPassword(byte[] password) {
//        this.password = password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password.getBytes();
//    }
}
