package uexcel.com.ltts.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.FundTransferDto;
import uexcel.com.ltts.dto.FundWalletDto;
import uexcel.com.ltts.dto.WalletHistoryDto;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.Wallet;
import uexcel.com.ltts.entity.WalletTransaction;
import uexcel.com.ltts.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletServiceImp implements WalletService {
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
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<WalletHistoryDto> wTHDs = new ArrayList<>();
       List<WalletTransaction> wTHs = repositoryService.getWalletTransRepository()
               .findByWalletId(client.getPhone());

       for(WalletTransaction wTH: wTHs ){
           wTHDs.add(new WalletHistoryDto(wTH.getId(),wTH.getAmount()));
       }
        return wTHDs;
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
