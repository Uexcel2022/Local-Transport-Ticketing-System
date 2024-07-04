package uexcel.com.ltts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.ValidTicketsDto;
import uexcel.com.ltts.dto.TicketInfoDto;
import uexcel.com.ltts.entity.*;
import uexcel.com.ltts.exception.CustomException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookingCheckinServiceImp implements BookingCheckinService {
    private static final Logger log = LoggerFactory.getLogger(BookingCheckinServiceImp.class);
    private final RepositoryService repositoryService;

    public BookingCheckinServiceImp(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }


    @Transactional
    public TicketInfoDto pressBooking(String routeId) {
        Route route =  repositoryService.getRouteRepository()
                .findById(routeId)
                .orElseThrow(() -> new CustomException("Route not found.","404"));

        Client principal = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Client client = repositoryService.getClientRepository()
                .findById(principal.getId())
                .orElseThrow(() -> new CustomException("Client not found.","404"));

        if(repositoryService.getBookingRepository().existsBookingByRouteAndStatus(route,"valid")){
           throw new CustomException("You have a valid ticket on this route.","400");
        }

        Wallet wallet = repositoryService.getWalletRepository().findByWalletNumber(client.getPhone());

        double rtPrice = route.getPrice();

        if(rtPrice > wallet.getBalance()) {
            throw new CustomException("Insufficient balance.","402");
        }

        double walletBalance = wallet.getBalance()-rtPrice;
        WalletTransaction wTransaction = new WalletTransaction();
        wTransaction.setTransactionType("booking");
        wTransaction.setBank("wallet");
        wTransaction.setAmount(-rtPrice);
        wTransaction.setSourceAccount(wallet.getClient().getPhone());
        wTransaction.setTransactionDate(LocalDate.now());
        wTransaction.setSourceName(wallet.getClient().getFullName());
        wTransaction.setWallet(wallet);

        wallet.setBalance(walletBalance);
        repositoryService.getWalletTransRepository().save(wTransaction);

        Booking booking = new Booking();
        booking.setClient(client);
        booking.setRoute(route);
        booking.setStatus("valid");
        booking.setDate(LocalDate.now());

        TicketInfoDto info = getTicketInfo(client,booking,route);

        repositoryService.getBookingRepository().save(booking);
        return info;
    }

    public List<ValidTicketsDto> getValidTickets() {
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ValidTicketsDto> bHs = new ArrayList<>();

        List<Booking> bookings =repositoryService.getBookingRepository().findByClientId(client.getId());
        for (Booking booking:bookings){
            if(booking.getStatus().equals("valid")) {
                ValidTicketsDto bH = new ValidTicketsDto();
                bH.setBookingId(booking.getId());
                bH.setTickNo(booking.getTicketNumber());
                bH.setRoute(booking.getRoute().getOrigin() + "-" + booking.getRoute().getDestination());
                bH.setDate(booking.getDate());
                bH.setExpiryDate(booking.getDate().plusDays(365));
                bH.setStatus(booking.getStatus());
                bHs.add(bH);
            }
        }
        return bHs;
    }

    public String processCheckIn(Map<String,String> request){
           Booking booking = repositoryService.getBookingRepository()
                   .findByTicketNumberAndStatus(request.get("ticketNumber"),"valid");
           if(booking == null) {
               throw new CustomException("The ticked has been used, refunded or validity expired.","400");
           }

        if (repositoryService.getCheckingRepository().existsCheckinByBookingId(booking.getId())) {
            throw new CustomException("The ticket has been used.","400");
        }

            //checking booking validity period
           LocalDate validityPeriod = booking.getDate().plusDays(365);
           if(validityPeriod.isBefore(LocalDate.now())) {
               booking.setStatus("expired");
               repositoryService.getBookingRepository().save(booking);
               throw  new CustomException("Ticket has expired.","400");
           }

           //checking the route vs the bus route
           Bus bus = repositoryService.getBusRepository().findBusByBusCode(request.get("busCode"));
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
           repositoryService.getCheckingRepository().save(checkin);

           return "check in successful";
    }

    private TicketInfoDto getTicketInfo(Client client,Booking booking,Route route){
        TicketInfoDto ticketInfoDto = new TicketInfoDto();
        ticketInfoDto.setTicketId(booking.getId());
        ticketInfoDto.setName(client.getFullName());
        ticketInfoDto.setTicketNumber(booking.getTicketNumber());
        ticketInfoDto.setRoute(route.getOrigin() +" - "+ route.getDestination());
        ticketInfoDto.setTicketDate(LocalDate.now());
        ticketInfoDto.setAmount(route.getPrice());
        ticketInfoDto.setValidity("365 days");
        return ticketInfoDto;
    }

    private static LocalDate getExpiryDate(LocalDate date){
       return  date.plusDays(365);

    }

}
