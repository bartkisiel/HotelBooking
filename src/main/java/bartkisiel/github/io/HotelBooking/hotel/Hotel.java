package bartkisiel.github.io.HotelBooking.hotel;


import bartkisiel.github.io.HotelBooking.locations.Adress;
import bartkisiel.github.io.HotelBooking.room.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Hotel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, name = "hotel_name")
    private String name;
    @Column(nullable = false)
    private Adress address;
    @Column(nullable = false)
    private int stars;
    @Column(nullable = false)
    private String emailAdress;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Room> roomSet;
    // Using constant default values of these is being considered.
    @Column(nullable = false)
    private LocalTime earliestPossibleCheckIn;
    @Column(nullable = false)
    private LocalTime latestPossibleCheckIn;
    @Column(nullable = false)
    private LocalTime possibleCheckOut;
    @Column(nullable = false)
    private LocalTime latestPossibleCheckOut;
    @Column(nullable = false)
    private BigDecimal checkOutFee;
    private final static LocalTime DEFAULT_EARLIEST_POSSIBLE_CHECKIN = LocalTime.of(8, 0);
    private final static LocalTime DEFAULT_LATEST_POSSIBLE_CHECKIN = LocalTime.of(18,0);
    private final static LocalTime DEFAULT_CHECKOUT = LocalTime.of(10,0);
    private final static LocalTime DEFAULT_LATEST_POSSIBLE_CHECKOUT = LocalTime.of(21,59);
    private final static BigDecimal DEFAULT_CHECKOUT_FEE = BigDecimal.valueOf(0.0);

    public Hotel(String name, Adress adress, int stars, String emailAdress) {
        this(name, adress, stars, emailAdress,
                DEFAULT_EARLIEST_POSSIBLE_CHECKIN,
                DEFAULT_LATEST_POSSIBLE_CHECKIN,
                DEFAULT_CHECKOUT,
                DEFAULT_LATEST_POSSIBLE_CHECKOUT,
                DEFAULT_CHECKOUT_FEE);
    }

    public Hotel(String name, Adress adress, int stars, String emailAdress,
                 LocalTime earliestPossibleCheckIn,
                 LocalTime latestPossibleCheckIn,
                 LocalTime possibleCheckOut,
                 LocalTime latestPossibleCheckOut,
                 BigDecimal checkOutFee) {
        this.name = name;
        this.address = adress;
        this.stars = stars;
        this.emailAdress = emailAdress;
        this.roomSet = new HashSet<>();
        this.earliestPossibleCheckIn = earliestPossibleCheckIn;
        this.latestPossibleCheckIn = latestPossibleCheckIn;
        this.possibleCheckOut = possibleCheckOut;
        this.latestPossibleCheckOut = latestPossibleCheckOut;
        this.checkOutFee = checkOutFee;
    }

    /*
     * empty constructor for Hibernate.
     */
    public Hotel() {
    }

    public void addRoom(Room room) {
        roomSet.add(room);
        room.setHotel(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name;
    }

    public Adress getAdress() {
        return address;
    }

    public void setAdress(Adress adress) {
        this.address = adress;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public Set<Room> getRoomSet() {
        return roomSet;
    }

    public void setRoomSet(Set<Room> roomSet) {
        this.roomSet = roomSet;
    }

    public LocalTime getEarliestPossibleCheckIn() {
        return earliestPossibleCheckIn;
    }

    public void setEarliestPossibleCheckIn(LocalTime earliestPossibleCheckIn) {
        this.earliestPossibleCheckIn = earliestPossibleCheckIn;
    }

    public LocalTime getLatestPossibleCheckIn() {
        return latestPossibleCheckIn;
    }

    public void setLatestPossibleCheckIn(LocalTime latestPossibleCheckIn) {
        this.latestPossibleCheckIn = latestPossibleCheckIn;
    }

    public LocalTime getPossibleCheckOut() {
        return possibleCheckOut;
    }

    public void setPossibleCheckOut(LocalTime possibleCheckOut) {
        this.possibleCheckOut = possibleCheckOut;
    }

    public LocalTime getLatestPossibleCheckOut() {
        return latestPossibleCheckOut;
    }

    public void setLatestPossibleCheckOut(LocalTime latestPossibleCheckOut) {
        this.latestPossibleCheckOut = latestPossibleCheckOut;
    }

    public BigDecimal getCheckOutFee() {
        return checkOutFee;
    }

    public void setCheckOutFee(BigDecimal checkOutFee) {
        this.checkOutFee = checkOutFee;
    }

    public static LocalTime getDefaultEarliestPossibleCheckin() {
        return DEFAULT_EARLIEST_POSSIBLE_CHECKIN;
    }

    public static LocalTime getDefaultLatestPossibleCheckin() {
        return DEFAULT_LATEST_POSSIBLE_CHECKIN;
    }

    public static LocalTime getDefaultCheckout() {
        return DEFAULT_CHECKOUT;
    }

    public static LocalTime getDefaultLatestPossibleCheckout() {
        return DEFAULT_LATEST_POSSIBLE_CHECKOUT;
    }

    public static BigDecimal getDefaultCheckoutFee() {
        return DEFAULT_CHECKOUT_FEE;
    }

    //Todo: Method to check if check in time is allowable during the reservation.

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Hotel hotel = (Hotel) object;
        return Objects.equals(address, hotel.address) &&
                Objects.equals(emailAdress, hotel.emailAdress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address,emailAdress);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", adress=" + address +
                ", stars=" + stars +
                ", emailAdress='" + emailAdress + '\'' +
                '}';
    }
}
