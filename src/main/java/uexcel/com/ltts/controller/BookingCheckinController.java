package uexcel.com.ltts.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uexcel.com.ltts.dto.TicketInfoDto;
import uexcel.com.ltts.service.BookingCheckinService;

import java.util.Map;

@Controller
@RequestMapping("api/v1")
public class BookingCheckinController {
    private final BookingCheckinService bookingCheckinService;
    public BookingCheckinController(BookingCheckinService bookingCheckinService) {
        this.bookingCheckinService = bookingCheckinService;
    }

    @GetMapping("booking")
    public ResponseEntity<TicketInfoDto> processBooking(@PathParam("routeId") String routeId){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingCheckinService.pressBooking(routeId));
    }

    @PostMapping("checkin")
    public ResponseEntity<String> processBooking(@RequestBody Map<String,String> request){
        return ResponseEntity.ok()
                .body(bookingCheckinService.processCheckIn(request));
    }
}
