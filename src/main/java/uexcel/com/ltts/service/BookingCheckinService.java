package uexcel.com.ltts.service;

import uexcel.com.ltts.entity.Booking;

import java.util.Map;

public interface BookingCheckinService {
    Booking pressBooking(String client, String routeId);
}
