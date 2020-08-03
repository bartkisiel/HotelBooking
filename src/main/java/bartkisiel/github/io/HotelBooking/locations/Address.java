package bartkisiel.github.io.HotelBooking.locations;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
public class Address {
    private String name;
    @Column(nullable = false, name = "street_adress_1")
    @NotBlank(message = "Street adress must not be empty!")
    private String streetAdress1;
    @Column(name = "street_adress_2")
    private String streetAdress2;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Country country;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    @Embedded
    @Valid
    private Postcode postcode;

    public Address(String name, String streetAdress1, String streetAdress2, Country country, String city, Postcode postcode) {
        this.name = name;
        this.streetAdress1 = streetAdress1;
        this.streetAdress2 = streetAdress2;
        this.country = country;
        this.city = city;
        this.postcode = postcode;
    }

    /*
     * empty constructor for Hibernate.
     */
    public Address() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAdress1() {
        return streetAdress1;
    }

    public void setStreetAdress1(String streetAdress1) {
        this.streetAdress1 = streetAdress1;
    }

    public String getStreetAdress2() {
        return streetAdress2;
    }

    public void setStreetAdress2(String streetAdress2) {
        this.streetAdress2 = streetAdress2;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Postcode getPostcode() {
        return postcode;
    }

    public void setPostcode(Postcode postcode) {
        this.postcode = postcode;
    }

    //Todo: Method to check if adresses are equal.

    @Override
    public int hashCode() {
        return Objects.hash(name, streetAdress1, streetAdress2, country, city, postcode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", streetAdress1='" + streetAdress1 + '\'' +
                ", streetAdress2='" + streetAdress2 +'\'' +
                ", country='" + country +
                ", city ='" + city + '\'' +
                ", postcode=" + postcode +
                '}';
    }
}
