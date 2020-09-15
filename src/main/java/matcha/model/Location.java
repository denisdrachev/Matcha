package matcha.model;

import lombok.*;
import matcha.converter.Converter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {

    private int id;
    private int user;
    @NotNull(message = "Поле location.x не может быть пустым")
    private Double x;
    @NotNull(message = "Поле location.y не может быть пустым")
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
