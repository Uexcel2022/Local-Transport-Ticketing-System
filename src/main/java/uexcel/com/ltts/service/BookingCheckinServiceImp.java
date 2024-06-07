package uexcel.com.ltts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.TicketInfoDto;
import uexcel.com.ltts.entity.*;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Repos;

import java.time.LocalDate;
import java.util.Map;

@Service
public class BookingCheckinServiceImp implements BookingCheckinService {
    private static final Logger log = LoggerFactory.getLogger(BookingCheckinServiceImp.class);
    private final Repos repos;

    public BookingCheckinServiceImp(Repos repos) {
        this.repos = repos;
    }


    @Transactional
    public TicketInfoDto pressBooking(String routeId) {
        Route route =  repos.getRouteRepository()
                .findById(routeId)
                .orElseThrow(() -> new CustomException("Route not found.","404"));

        Client principal = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Client client = repos.getClientRepository()
                .findById(principal.getId())
                .orElseThrow(() -> new CustomException("Client not found.","404"));

        if(repos.getBookingRepository().existsBookingByRouteAndStatus(route,"valid")){
           throw new CustomException("You have a valid ticket on this route.","400");
        }

        Wallet wallet = repos.getWalletRepository().findByWalletNumber(client.getPhone());

        double rtPrice = route.getPrice();

        if(rtPrice > wallet.getBalance()) {
            throw new CustomException("Insufficient balance.","401");
        }

        double walletBalance = wallet.getBalance()-rtPrice;
        WalletTransaction wTransaction = new WalletTransaction();
        wTransaction.setTransactionType("booking");
        wTransaction.setAmount(rtPrice);
        wTransaction.setSourceAccount(wallet.getClient().getPhone());
        wTransaction.setTransactionDate(LocalDate.now());
        wTransaction.setSourceName(wallet.getClient().getFullName());
        wTransaction.setWallet(wallet);

        wallet.setBalance(walletBalance);
        repos.getWalletTransRepository().save(wTransaction);

        Booking booking = new Booking();
        booking.setClient(client);
        booking.setRoute(route);
        booking.setStatus("valid");
        booking.setDate(LocalDate.now());

        TicketInfoDto ticketInfoDto = new TicketInfoDto();
        ticketInfoDto.setName(client.getFullName());
        ticketInfoDto.setTicketNumber(booking.getTicketNumber());
        ticketInfoDto.setRoute(route.getOrigin() +" - "+ route.getDestination());
        ticketInfoDto.setTicketDate(LocalDate.now());
        ticketInfoDto.setValidity("365 days");

        repos.getBookingRepository().save(booking);
        return ticketInfoDto;
    }

    public String processCheckIn(Map<String,String> request){
           Booking booking = repos.getBookingRepository()
                   .findByTicketNumberAndStatus(request.get("ticketNumber"),"valid");
           if(booking == null) {
               throw new CustomException("The ticked has been used, refunded or validity expired.","400");
           }

            //checking booking validity period
           int booKedDate = booking.getDate().getDayOfYear()+1;
           int date = LocalDate.now().getDayOfYear();
           if(booKedDate < date) {
               booking.setStatus("expired");
               repos.getBookingRepository().save(booking);
               throw  new CustomException("Ticket validity expired.","400");
           }

           //checking the route vs the bus route
           Bus bus = repos.getBusRepository().findBusByBusCode(request.get("busCode"));
           if(bus == null) {
               throw new CustomException("Bus not found.","404");
           }
           boolean isValidRoute = false;
           for(Route route: bus.getRoute()){
               if(route.getId().equals(booking.getRoute().getId())) {
                   isValidRoute = true;
                   break;
               }
           }
           if(!isValidRoute) {
               throw new CustomException("The ticket is not for this route.","400");
           }

           booking.setStatus("used");
           Checkin checkin = new Checkin();
           checkin.setDate(LocalDate.now());
           checkin.setBus(bus);
           checkin.setBooking(booking);
           repos.getCheckingRepository().save(checkin);

           return "check in successful";
    }

}
