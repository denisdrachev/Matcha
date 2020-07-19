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
public class Location implements Serializable {

    private int id;
    private int user;
    private Double x;
    private Double y;
    @ToString.Exclude
    private boolean active = false;
    private Date time = Calendar.getInstance().getTime();

    public void setUser(int user) {
        this.user = user;
    }

    public void setUser(Object user) {
        this.user = (int) user;
    }

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }
}
