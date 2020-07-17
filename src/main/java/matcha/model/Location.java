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
public class Location implements Serializable {

    private int id;
    private int user;
    private Double x;
    private Double y;
    private Date time = Calendar.getInstance().getTime();

    public void setUser(int user) {
        this.user = user;
    }

    public void setUser(Object user) {
        this.user = (int) user;
    }
}
