package bartkisiel.github.io.HotelBooking.features;

import bartkisiel.github.io.HotelBooking.customers.Guest;
import bartkisiel.github.io.HotelBooking.reservation.Reservation;
import bartkisiel.github.io.HotelBooking.util.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "meal_plans")
public class MealPlan implements Serializable {
    public static final double UNDERAGE_DISCOUNT = 0.50;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private UUID mealPlanId = UUID.randomUUID();
    @OneToOne
    private Guest guest;
    @OneToOne
    private Reservation reservation;
    @ManyToMany
    private List<Extra> foodExtras;
    @ElementCollection
    private List<DietPreference> dietPreferenceList;

    /*
     * empty constructor for Hibernate.
     */
    public MealPlan() {
    }

    public MealPlan(Guest guest, Reservation reservation, List<Extra> foodExtras, List<DietPreference> dietPreferenceList) {
        this.guest = guest;
        this.reservation = reservation;
        setFoodExtras(foodExtras);
        this.dietPreferenceList = dietPreferenceList;
    }

    public MealPlan(Guest guest, Reservation reservation) {
        this.guest = guest;
        this.reservation = reservation;
    }

    public int getId() {
        return id;
    }

    public UUID getMealPlanId() {
        return mealPlanId;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<Extra> getFoodExtras() {
        return foodExtras;
    }

    public void setFoodExtras(List<Extra> foodExtras) {
        boolean containsInvalidCategory = foodExtras.stream()
                .anyMatch(e -> e.getCategory() != Extra.Category.Food);
        if(containsInvalidCategory) {
            throw new IllegalArgumentException("Contains invalid food category.");
        }
        this.foodExtras = foodExtras;
    }

    public List<DietPreference> getDietPreferenceList() {
        return dietPreferenceList;
    }

    public void setDietPreferenceList(List<DietPreference> dietPreferenceList) {
        this.dietPreferenceList = dietPreferenceList;
    }

    public boolean isEmpty() {
        return foodExtras.isEmpty() && dietPreferenceList.isEmpty();
    }

    public boolean hasInvalidDietaryPreference() {
        return dietPreferenceList.stream()
                .filter(d -> d == DietPreference.Vegan || d == DietPreference.Vegetarian)
                .count() == 2;
    }

    public BigDecimal getTotalMealPlanCosts() {
        return foodExtras.stream()
                .map(this::calculateExtraCosts)
                .reduce(BigDecimal.ZERO, BigDecimal::add, BigDecimal::add);
    }

    public BigDecimal calculateExtraCosts(Extra extra) throws IllegalArgumentException {
        if(extra.getCategory() != Extra.Category.Food) {
            throw new IllegalArgumentException("Contains invalid extra category.");
        }
        BigDecimal cost = extra.totalPrice(reservation.getReservationDates().totalDays());
        if(guest.isUnderaged()) {
            BigDecimal discount = cost.multiply(BigDecimal.valueOf(UNDERAGE_DISCOUNT));
            return cost.subtract(discount);
        }
        return cost;
    }

    public boolean hasExtras() {
        return !foodExtras.isEmpty();
    }

    public boolean hasDietPreferences() {
        return !dietPreferenceList.isEmpty();
    }

    public String toFoodExtraCsv() {
        return Utils.toCsv(foodExtras, Extra::getDescription);
    }

    public String toDietRequirementsCsv() {
        return Utils.toCsv(dietPreferenceList, DietPreference::getDescription);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MealPlan mealPlan = (MealPlan) object;
        return Objects.equals(mealPlanId, mealPlan.mealPlanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealPlanId);
    }


}
