package bartkisiel.github.io.HotelBooking.customers;

import bartkisiel.github.io.HotelBooking.util.Utils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "guests")
public class Guest {
    @Transient
    private UUID tempId = UUID.randomUUID();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "First name required.")
    @Column(nullable = false)
    private String firstName;
    @NotBlank(message = "Last name required.")
    @Column(nullable = false)
    private String lastName;
    private boolean underaged;

    public Guest(String firstName, String lastName, boolean underaged) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.underaged = underaged;
    }

    /*
     * empty constructor for Hibernate.
     */
    public Guest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isUnderaged() {
        return underaged;
    }

    public void setUnderaged(boolean underaged) {
        this.underaged = underaged;
    }

    public UUID getTempId() {
        return tempId;
    }

    public String getFormattedFullName() {
        return Utils.capitalizeWords(firstName) + " " + Utils.capitalizeWords(lastName);
    }

    public static Comparator<Guest> compare() {
        return Comparator.comparing(Guest::isUnderaged, Boolean::compareTo)
                .thenComparing(Guest::getFirstName)
                .thenComparing(Guest::getLastName);
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || getClass() != object.getClass()) return false;
        Guest guest = (Guest) object;
        return underaged == guest.underaged &&
                Objects.equals(firstName, guest.lastName) &&
                Objects.equals(guest.firstName, lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, underaged);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", underaged=" + underaged +
                '}';
    }
}
