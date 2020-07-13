package bartkisiel.github.io.HotelBooking.features;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Extra {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigDecimal perDayPrice;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    public enum Type {
        Basic, Premium, Luxury
    }

    public enum Category {
        General, Food
    }

    public Extra(String description, BigDecimal perDayPrice, Type type, Category category) {
        this.description = description;
        this.perDayPrice = perDayPrice;
        this.type = type;
        this.category = category;
    }

    /*
     * empty constructor for Hibernate.
     */
    public Extra() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPerDayPrice() {
        return perDayPrice;
    }

    public void setPerDayPrice(BigDecimal perDayPrice) {
        this.perDayPrice = perDayPrice;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal totalPrice(int days) {
        return perDayPrice.multiply(BigDecimal.valueOf(days));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extra extra = (Extra) o;
        return id == extra.id &&
                Objects.equals(description, extra.description) &&
                Objects.equals(perDayPrice, extra.perDayPrice) &&
                type == extra.type &&
                category == extra.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, type, category);
    }

    @Override
    public String toString() {
        return "Extra{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", perDayPrice=" + perDayPrice +
                ", type=" + type +
                ", category=" + category +
                '}';
    }
}
