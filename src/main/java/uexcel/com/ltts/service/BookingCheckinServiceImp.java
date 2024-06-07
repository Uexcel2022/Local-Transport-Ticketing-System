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

//    public String processCheckIn(Map<String,String> request){
//
//    }
}
