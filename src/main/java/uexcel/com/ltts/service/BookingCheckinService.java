package uexcel.com.ltts.service;

import uexcel.com.ltts.dto.TicketInfoDto;

public interface BookingCheckinService {
    TicketInfoDto pressBooking(String routeId);
}
