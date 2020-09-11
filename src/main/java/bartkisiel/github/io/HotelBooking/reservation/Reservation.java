package bartkisiel.github.io.HotelBooking.reservation;

import bartkisiel.github.io.HotelBooking.customers.Guest;
import bartkisiel.github.io.HotelBooking.features.Extra;
import bartkisiel.github.io.HotelBooking.features.MealPlan;
import bartkisiel.github.io.HotelBooking.payment.SuccessfulPayment;
import bartkisiel.github.io.HotelBooking.room.Room;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "uuid")
    private UUID reservationId = UUID.randomUUID();
    @OneToOne(mappedBy = "reservation")
    private Room room;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "reservation_guests",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id", referencedColumnName = "id")
    )
    private Set<Guest> guests = new HashSet<>();
    @Embedded
    @Valid
    private ReservationDates reservationDates = new ReservationDates();
    @ManyToMany
    @JoinTable(
            name = "reservation_general_extras",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "general_extra_id", referencedColumnName = "id")
    )
    private Set<Extra> generalExtras = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<MealPlan> mealPlanList = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private SuccessfulPayment successfulPayment;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /*
     * empty constructor for Hibernate.
     */
    public Reservation() {
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) { this.room = room; }

    public Set<Guest> getGuests() {
        return Collections.unmodifiableSet(guests);
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }

    public ReservationDates getReservationDates() {
        return reservationDates;
    }

    public void setReservationDates(ReservationDates reservationDates) {
        this.reservationDates = reservationDates;
    }

    public Set<Extra> getGeneralExtras() {
        return generalExtras;
    }

    public void setGeneralExtras(Set<Extra> generalExtras) {
        boolean containsCategoriesError = generalExtras.stream().anyMatch(extra -> extra.getCategory() != Extra.Category.General);
        if(containsCategoriesError) {
            throw new IllegalArgumentException("Contains invalid category.");
        }
        this.generalExtras = generalExtras;
    }

    public List<MealPlan> getMealPlanList() {
        return mealPlanList;
    }

    public void setMealPlanList(List<MealPlan> mealPlanList) {
        this.mealPlanList = mealPlanList;
    }

    public SuccessfulPayment getSuccessfullPayment() {
        return successfulPayment;
    }

    public void setSuccessfullPayment(SuccessfulPayment successfullPayment) {
        createdAt = LocalDateTime.now();
        this.successfulPayment = successfullPayment;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void addGuest(Guest guest) {
        if(!roomIsFull()) {
            guests.add(guest);
        }
    }

    public boolean roomIsFull() {
        return guests.size() >= room.getBeds();
    }

    public boolean hasGuests() {
        return !guests.isEmpty();
    }

    public boolean hasAdultGuest() {
        return guests.stream().anyMatch(guest -> !guest.isUnderaged());
    }

    public void deleteGuests() {
        guests.clear();
    }

    public boolean deleteGuestById(UUID guestId) {
        return guests.removeIf(guest -> guest.getTempId().equals(guestId));
    }

    public Extra.Type getPriceForExtraTypeOfRoom() {
        switch(room.getRoomtype()) {
            case Loft:
            case Apartament:
            case Penthouse:
                return Extra.Type.Premium;
            default:
                return Extra.Type.Basic;
        }
    }

    public BigDecimal calculateChargeForLateCheckOut() {
        return reservationDates.isDelayedCheckOutTime() ? getLateCheckoutFee() : BigDecimal.ZERO;
    }

    public BigDecimal getLateCheckoutFee() {
        switch (room.getRoomtype()) {
            case Apartament:
            case Penthouse:
                return BigDecimal.ZERO;
            default:
                return room.getHotel().getCheckOutFee();
        }
    }

    public BigDecimal getTotalCosts() {
        int days = reservationDates.totalDays();
        if(days == 0) {
            return BigDecimal.ZERO;
        }
        return room.getPricePerNight().multiply(BigDecimal.valueOf(days));
    }

    public BigDecimal getTotalCostsWithLateCheckOutFee() {
        return getTotalCosts().add(getLateCheckoutFee());
    }

    public void createMealPlan() {
        mealPlanList = guests.stream()
                .map(guest -> new MealPlan(guest, this))
                .sorted(Comparator.comparing(MealPlan::getGuest, Guest.compare()))
                .collect(Collectors.toList());
    }

    public boolean hasEmptyMealPlans() {
        return mealPlanList.stream()
                .map(MealPlan::getTotalMealPlanCosts)
                .reduce(BigDecimal.ZERO, BigDecimal::add, BigDecimal::add)
                .equals(BigDecimal.ZERO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || getClass() != object.getClass()) return false;
        Reservation reservation = (Reservation) object;
        return Objects.equals(reservationId, reservation.reservationId);
    }
}
