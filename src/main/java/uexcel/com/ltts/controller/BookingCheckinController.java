package uexcel.com.ltts.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uexcel.com.ltts.entity.Booking;
import uexcel.com.ltts.service.BookingCheckinService;

@Controller
@RequestMapping("api/v1")
public class BookingCheckinController {
    private final BookingCheckinService bookingCheckinService;
    public BookingCheckinController(BookingCheckinService bookingCheckinService) {
        this.bookingCheckinService = bookingCheckinService;
    }

    @GetMapping("booking/{clientId}/{routeId}")
    public ResponseEntity<Booking> processBooking(@PathVariable String clientId, @PathVariable String routeId){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingCheckinService.pressBooking(clientId,routeId));

    }
}
