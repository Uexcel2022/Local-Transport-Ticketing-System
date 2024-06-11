package uexcel.com.ltts.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TicketInfoDto {
    private String name;
    private String ticketNumber;
    private String route;
    private double amount;
    private LocalDate ticketDate;
    private String validity;
}
