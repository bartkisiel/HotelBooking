package bartkisiel.github.io.HotelBooking.payment;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

@Entity
public class SuccessfulPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private UUID transactionId = UUID.randomUUID();
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PendingPayment.CreditCardType creditCardType;
    @Column(nullable = false)
    private String lastFourNumbersOfCreditCard;
    @Column(nullable = false)
    private String cvvNumber;
    @Column(nullable = false)
    private YearMonth cardExpirationDate;

    /*
     * empty constructor for Hibernate.
     */
    public SuccessfulPayment() {
    }

    public SuccessfulPayment(PendingPayment.CreditCardType creditCardType,
                             String lastFourNumbersOfCreditCard,
                             String cvvNumber,
                             YearMonth cardExpirationDate) {
        this.creditCardType = creditCardType;
        this.lastFourNumbersOfCreditCard = lastFourNumbersOfCreditCard;
        this.cvvNumber = cvvNumber;
        this.cardExpirationDate = cardExpirationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public PendingPayment.CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(PendingPayment.CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getLastFourNumbersOfCreditCard() {
        return lastFourNumbersOfCreditCard;
    }

    public void setLastFourNumbersOfCreditCard(String lastFourNumbersOfCreditCard) {
        this.lastFourNumbersOfCreditCard = lastFourNumbersOfCreditCard;
    }

    public String getCvvNumber() {
        return cvvNumber;
    }

    public void setCvvNumber(String cvvNumber) {
        this.cvvNumber = cvvNumber;
    }

    public YearMonth getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(YearMonth cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    @Override
    public boolean equals(Object object){
        if(this == object) return true;
        if(object == object || getClass() != object.getClass()) return false;
        SuccessfulPayment payment = (SuccessfulPayment) object;
        return Objects.equals(transactionId, payment.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "SuccessfulPayment{" +
                "id=" + id +
                ", transactionId=" + transactionId +
                ", creditCardType=" + creditCardType +
                ", lastFourNumbersOfCreditCard" + lastFourNumbersOfCreditCard + '\'' +
                ", cvvNumber='" + cvvNumber + '\'' +
                ", cardExpirationDate=" + cardExpirationDate +
                '}';
    }
}
