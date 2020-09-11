package bartkisiel.github.io.HotelBooking.reservation;

import bartkisiel.github.io.HotelBooking.customers.Guest;
import bartkisiel.github.io.HotelBooking.exceptions.NotFoundException;
import bartkisiel.github.io.HotelBooking.features.Extra;
import bartkisiel.github.io.HotelBooking.features.ExtraRepository;
import bartkisiel.github.io.HotelBooking.room.Room;
import bartkisiel.github.io.HotelBooking.room.RoomRepository;
import bartkisiel.github.io.HotelBooking.util.TimeProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@SessionAttributes("reservationFlow")
public class ReservationController {
    private RoomRepository roomRepository;
    private ExtraRepository extraRepository;
    private TimeProvider timeProvider;

    public ReservationController(RoomRepository roomRepository, ExtraRepository extraRepository, TimeProvider timeProvider) {
        this.roomRepository = roomRepository;
        this.extraRepository = extraRepository;
        this.timeProvider = timeProvider;
    }

    @ModelAttribute("reservationFlow")
    public ReservationFlow getReservationFlow() {
        return new ReservationFlow();
    }

    @GetMapping("/reservation")
    public String getDateForm(@RequestParam(value = "roomId") Integer roomId,
                              @ModelAttribute("reservationFlow") ReservationFlow reservationFlow) throws NotFoundException {
        reservationFlow.enterStep(Step.Dates);

        Optional<Room> possibleRoom = roomRepository.findById(roomId);
        if(!possibleRoom.isPresent()) {
            throw new NotFoundException();
        }

        possibleRoom.get().setReservation(reservationFlow.getReservation());
        return "reservation/dates";
    }

    @PostMapping("/reservation/dates")
    public String dates(@Valid @ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
            reservationFlow.enterStep(Step.Dates);

            if(bindingResult.hasErrors()) {
                return "reservation/dates";
            }

            Optional<ReservationDates.ReservationValidationError> validationError = reservationFlow
                    .getReservation()
                    .getReservationDates()
                    .validation(timeProvider.localDate());

            if(validationError.isPresent()) {
                bindingResult.rejectValue("reservation.dates", validationError.get().getCode(),
                        validationError.get().getReason());
                return "reservation/dates";
            }

            reservationFlow.finishStep(Step.Dates);
            redirectAttributes.addFlashAttribute("reservationFlow", reservationFlow);
            return "redirect:/reservation/guests";
    }

    @PostMapping(value = "/reservation/dates", params = "cancel")
    public String cancelDates(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @PostMapping(value = "/reservation/dates", params = "prices")
    public String roomCostFragment(@ModelAttribute("reservationFlow") ReservationFlow reservationFlow) {
        return "reservation/fragments :: roomCosts";
    }

    @GetMapping("/reservation/guests")
    public String getGuestForm(@ModelAttribute("reservationFlow") ReservationFlow reservationFlow, Model model) {
        reservationFlow.enterStep(Step.Guests);
        model.addAttribute("guest", new Guest());
        return "reservation/guests";
    }

    @PostMapping(value = "/reservation/guests", params = "back")
    public String fromGuestBackToDates(@ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                                       RedirectAttributes ra) {
        reservationFlow.enterStep(Step.Guests);
        ra.addFlashAttribute("reservationFlow", reservationFlow);
        return "redirect:/reservation?roomId=" + reservationFlow.getReservation().getRoom().getId();
    }

    @PostMapping(value = "/reservation/guests", params = "addGuest")
    public String postAddGuest(@Valid @ModelAttribute("guest") Guest guest,
                               BindingResult bindingResult,
                               @ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                               Model model) {
        reservationFlow.enterStep(Step.Guests);

        if(bindingResult.hasErrors()) {
            return "reservation/guests";
        }
        if(reservationFlow.getReservation().getGuests().contains(guest)) {
            bindingResult.reject("exists", "A guest with this name already exists");
            return "reservation/guests";
        }
        if(reservationFlow.getReservation().roomIsFull()){
            bindingResult.reject("guestLimitExceeded", "This room has the maximum number of guests");
            return "reservation/guests";
        }

        reservationFlow.getReservation().addGuest(guest);
        model.addAttribute("guest", new Guest());

        return "reservation/guests";
    }

    @PostMapping(value = "/reservation/guests", params="removeGuest")
    public String postRemoveGuest(@RequestParam("removeGuest")UUID guestId,
                                  @ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                                  Model model) {
        reservationFlow.enterStep(Step.Guests);
        reservationFlow.getReservation().deleteGuestById(guestId);
        model.addAttribute("guest", new Guest());
        return "reservation/guests";
    }

    @PostMapping(value = "/reservation/guests")
    public String postGuestToExtras(@ModelAttribute(binding = false) Guest guest,
                                    Errors errors,
                                    @ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                                    RedirectAttributes redirectAttributes) {
        reservationFlow.enterStep(Step.Guests);

        if(!reservationFlow.getReservation().hasGuests()) {
            errors.reject("guests.noneExist", "There must be at least one guest");
            return "reservation/guests";
        }
        if(!reservationFlow.getReservation().hasAdultGuest()) {
            errors.reject("guests.noAdults", "There must be at least one adult guest");
            return "reservation/guests";
        }
        redirectAttributes.addFlashAttribute("reservationFlow", reservationFlow);

        reservationFlow.finishStep(Step.Guests);
        return "redirect:/reservation/extras";
    }

    @GetMapping("/reservation/extras")
    public String getGeneralExtrasForm(@ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                                       Model model) {
        reservationFlow.setCurrentStep(Step.Extras);
        List<Extra> generalExtras = extraRepository.findAllByTypeAndCategory(
                reservationFlow.getReservation().getPriceForExtraTypeOfRoom(), Extra.Category.General
        );
        model.addAttribute("extras", generalExtras);
        return "reservation/extras";
    }

    @PostMapping(value = "/reservation/extras", params = "back")
    public String fromGeneralExtrasBackToGuest(@ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                                               RedirectAttributes ra) {
        reservationFlow.setCurrentStep(Step.Guests);
        ra.addFlashAttribute("reservationFlow", reservationFlow);
        return "redirect:/reservation/guests";
    }

    @PostMapping(value = "/reservation/extras")
    public String submitGeneralExtras(@ModelAttribute("reservationFlow") ReservationFlow reservationFlow,
                                      RedirectAttributes ra) {
        reservationFlow.setCurrentStep(Step.Extras);
        ra.addFlashAttribute("reservationFlow", reservationFlow);
        reservationFlow.finishStep(Step.Extras);
        return "redirect:/reservation/meals";
    }

    @PostMapping(value = "/reservation/extras", params = "add")
    public String submitGeneralExtrasAjax(@ModelAttribute("reservationFlow") ReservationFlow reservationFlow) {
        return "reservation/fragments :: quickSummary";
    }



}
