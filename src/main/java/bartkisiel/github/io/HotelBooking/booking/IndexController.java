package bartkisiel.github.io.HotelBooking.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class IndexController {

    @GetMapping("/")
    public String getSearchPage() {
        return "/hotel/search";
    }

    @GetMapping("/greeting")
    public @ResponseBody
    ResponseEntity<String> getHelloWorld() {
        return new ResponseEntity<String>("Hello World", HttpStatus.OK);
    }

}
