package bartkisiel.github.io.HotelBooking.locations;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Embeddable
public class Postcode {
    @Column(nullable = false)
    @Pattern(regexp = "[0-9]{4}", message = "Postcode must contain four digits!")
    @NotBlank(message = "Postcode must not be empty!")
    private String code;

    public Postcode(String code) {
        this.code = code;
    }

    /*
     * empty constructor for Hibernate.
     */
    public Postcode() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode(){
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Postcode{" +
                "code'" + code + '\'' +
                '}';
    }
}
