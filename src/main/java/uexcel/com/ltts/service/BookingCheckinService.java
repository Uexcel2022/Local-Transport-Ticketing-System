package uexcel.com.ltts.service;

import uexcel.com.ltts.dto.BookingHistory;
import uexcel.com.ltts.dto.TicketInfoDto;

import java.util.List;
import java.util.Map;
public interface BookingCheckinService {
    TicketInfoDto pressBooking(String routeId);
    String processCheckIn(Map<String,String> request);
    List<BookingHistory> getBookingHistory();
}
