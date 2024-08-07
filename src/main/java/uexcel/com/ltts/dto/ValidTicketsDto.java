package uexcel.com.ltts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class ValidTicketsDto {
    private String bookingId;
    private String tickNo;
    private String route;
    private LocalDate date;
    private String status;
    private LocalDate expiryDate;
}
