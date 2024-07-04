package uexcel.com.ltts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.FundTransferDto;
import uexcel.com.ltts.dto.FundWalletDto;
import uexcel.com.ltts.dto.WalletHistoryDto;
import uexcel.com.ltts.entity.*;
import uexcel.com.ltts.exception.CustomException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalletServiceImp implements WalletService {
    private static final Logger log = LoggerFactory.getLogger(WalletServiceImp.class);
    private final RepositoryService repositoryService;

    public WalletServiceImp(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Transactional
    public String fundWallet(FundWalletDto request){
        Wallet wallet = isExist(request.getWalletNumber());

        if(request.getAmount() > 0){
            double bal = wallet.getBalance()+request.getAmount();
            wallet.setBalance(bal);
            WalletTransaction wt = new WalletTransaction();
            wt.setWallet(wallet);
            wt.setSourceAccount(request.getSourceAccount());
            wt.setSourceName(request.getSourceName());
            wt.setCCnumber(request.getCCtype());
            wt.setCCnumber(request.getCCnumber());
            wt.setTransactionType(request.getTransactionType());
            wt.setAmount(request.getAmount());
            wt.setBank(request.getBank());
            repositoryService.getWalletTransRepository().save(wt);
            return "Transaction successful.";
        }
            throw new CustomException("Negative or Zero amount entered.","400");
    }

    @Transactional
    public String walletTransfer(FundTransferDto request){
        Wallet payerWallet = isExist(request.getPayerWallet());
        Wallet payeeWallet = isExist(request.getPayeeWallet());
        if(request.getAmount() > 0) {
            request.setSourceName(payerWallet.getClient().getFullName());
            payerWallet.setBalance(payerWallet.getBalance() - request.getAmount());
            payeeWallet.setBalance(payeeWallet.getBalance() + request.getAmount());

            walletTransferDebit(request, new WalletTransaction(),payerWallet);

            walletTransferCredit(request,new WalletTransaction(), payeeWallet);

            return "Transaction successful";
        }
            throw new CustomException("Negative or Zero amount entered.","400");

    }

    public List<WalletHistoryDto> transHistory(){
        LocalDate dateFrom = LocalDate.now().minusDays(31);
        log.info(dateFrom.toString());
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<WalletHistoryDto> wTHDs = new ArrayList<>();
        Wallet wallet = repositoryService.getWalletRepository().findByWalletNumber(client.getPhone());
        if(wallet == null){
            throw new CustomException("Wallet not found.","400");
        }
       List<WalletTransaction> wTHs = repositoryService.getWalletTransRepository()
               .findByWalletIdAndTransactionDateIsAfter(wallet.getId(),dateFrom);

       for(WalletTransaction wTH: wTHs ){
           wTHDs.add(new WalletHistoryDto(wTH.getId(),wTH.getAmount(),
                   wTH.getTransactionDate(),wTH.getTransactionType()));
       }
        return wTHDs;
    }

    @Override
    @Transactional
    public String cancelBooking(String ticketNo){

        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = repositoryService.getBookingRepository().findByTicketNumberAndStatus(ticketNo,"valid");
        if(booking == null){
            throw new CustomException("The ticked is no longer valid.","400");
        }

        Checkin checkin = repositoryService.getCheckingRepository().findByBookingId(booking.getId());
        if(checkin != null){
            booking.setStatus("used");
            repositoryService.getBookingRepository().save(booking);
            return"The ticked has already been used.:400";
        }

        if(booking.getDate().plusDays(365).isBefore(LocalDate.now())){
            booking.setStatus("expired");
            repositoryService.getBookingRepository().save(booking);
            return "Ticked has expired.:400.";
        }

            Wallet wallet = repositoryService.getWalletRepository().findByWalletNumber(client.getPhone());
            wallet.setBalance(wallet.getBalance() + booking.getRoute().getPrice());

            WalletTransaction wt = new WalletTransaction();
            wt.setWallet(wallet);
            wt.setSourceAccount(client.getPhone());
            wt.setSourceName(client.getFullName());
            wt.setBank("company");
            wt.setTransactionType("refund-booking");
            wt.setAmount(booking.getRoute().getPrice());
            booking.setStatus("refunded");
            repositoryService.getBookingRepository().save(booking);
            repositoryService.getWalletTransRepository().save(wt);

            return "Refund successful.";

    }


    private Wallet isExist(String walletNumber){
        Wallet wallet = repositoryService.getWalletRepository()
                .findByWalletNumber(walletNumber);
        if(wallet==null){
            throw new CustomException("Invalid wallet number", "400");
        }
        return wallet;
    }


    private  void walletTransferDebit(FundTransferDto request, WalletTransaction wt,Wallet wallet){
        wt.setSourceAccount(request.getPayerWallet());
        wt.setSourceName(request.getSourceName());
        wt.setTransactionType("wallet transfer");
        wt.setAmount(-request.getAmount());
        wt.setWallet(wallet);
        wt.setBank("Wallet");
        repositoryService.getWalletTransRepository().save(wt);
    }

    private  void walletTransferCredit(FundTransferDto request, WalletTransaction wt, Wallet wallet){
        wt.setSourceAccount(request.getPayerWallet());
        wt.setSourceName(request.getSourceName());
        wt.setTransactionType("wallet transfer");
        wt.setAmount(request.getAmount());
        wt.setWallet(wallet);
        wt.setBank("Wallet");
        repositoryService.getWalletTransRepository().save(wt);
    }

}
