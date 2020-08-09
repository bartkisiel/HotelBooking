package bartkisiel.github.io.HotelBooking.reservation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationFlow {
    @Valid
    private Reservation reservation = new Reservation();
    private List<StepDescription> stepDescriptionList = new ArrayList<>();
    private Set<Step> finishedSteps = new HashSet<>();
    private Step currentStep = Step.Dates;

    public ReservationFlow() {
        stepDescriptionList.add(new StepDescription(0, "Dates", "Choose your reservation dates"));
        stepDescriptionList.add(new StepDescription(1, "Guests", "Provide guest details"));
        stepDescriptionList.add(new StepDescription(2, "Extras", "Select optional extras"));
        stepDescriptionList.add(new StepDescription(3, "Meals", "Choose optional meal plans"));
        stepDescriptionList.add(new StepDescription(4, "Review", "Verify your reservation"));
        stepDescriptionList.add(new StepDescription(5, "Payment", "Provide payment details"));
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Step getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }

    public StepDescription getCurrentStepDescription() {
        return stepDescriptionList.get(currentStep.flowStep);
    }

    public void finishStep(Step step) {
        finishedSteps.add(step);
    }

    public void unfinishStep(Step step) {
        finishedSteps.remove(step);
    }

    public boolean isCurrent(Step step) {
        return step == currentStep;
    }

    public boolean isFinished(Step step) {
        return finishedSteps.contains(step);
    }

    public void enterStep(Step step) {
        setCurrentStep(step);
        unfinishStep(step);
    }

    public List<StepDescription> getStepDescriptionList() {
        return stepDescriptionList;
    }

    public static class StepDescription {
        private int flowStep;
        private String title;
        private String description;

        public StepDescription(int flowStep, String title, String description) {
            this.flowStep = flowStep;
            this.title = title;
            this.description = description;
        }

        private int normalizedFlowStep() {
            return flowStep + 1;
        }

        public int getFlowStep() {
            return flowStep;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getFlowwithTitle() {
            return normalizedFlowStep() + ". " + title;
        }

        public String getFlowwithDescription() {
            return normalizedFlowStep() + ". " + description;
        }

        @Override
        public String toString() {
            return "StepDescription{" +
                    "flowStep=" + flowStep +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

}
