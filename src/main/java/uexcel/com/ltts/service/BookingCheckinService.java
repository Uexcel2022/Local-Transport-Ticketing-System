package uexcel.com.ltts.service;

import org.springframework.stereotype.Service;
import uexcel.com.ltts.dto.TicketInfoDto;

import java.util.Map;
public interface BookingCheckinService {
    TicketInfoDto pressBooking(String routeId);
    String processCheckIn(Map<String,String> request);
}
