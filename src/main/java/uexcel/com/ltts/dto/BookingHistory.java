package uexcel.com.ltts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class BookingHistory {
    private String bookingId;
    private String route;
    private LocalDate date;
    private LocalDate expiryDate;
}
