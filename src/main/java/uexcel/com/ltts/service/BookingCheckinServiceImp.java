package uexcel.com.ltts.service;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.entity.Booking;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.Route;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Repos;

import java.util.Map;
import java.util.Optional;

@Service
public class BookingCheckinServiceImp implements BookingCheckinService {
    private static final Logger log = LoggerFactory.getLogger(BookingCheckinServiceImp.class);
    private final Repos repos;

    public BookingCheckinServiceImp(Repos repos) {
        this.repos = repos;
    }

    public Booking pressBooking(String routeId) {
        Route route =  repos.getRouteRepository()
                .findById(routeId)
                .orElseThrow(() -> new CustomException("Route not found","404"));


        Client principal = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Client client = repos.getClientRepository()
                .findById(principal.getId())
                .orElseThrow(() -> new CustomException("Client not found","404"));

        Booking booking = new Booking();
        booking.setClient(client);
        booking.setRoute(route);
        return repos.getBookingRepository().save(booking);
    }
}
