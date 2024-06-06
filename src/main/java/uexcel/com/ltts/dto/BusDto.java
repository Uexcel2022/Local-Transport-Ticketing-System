package uexcel.com.ltts.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class BusDto {
    private String busName;
    private String busCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String origin;
    private String destination;
}
