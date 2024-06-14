package uexcel.com.ltts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uexcel.com.ltts.dto.FundTransferDto;
import uexcel.com.ltts.dto.FundWalletDto;
import uexcel.com.ltts.dto.WalletHistoryDto;
import uexcel.com.ltts.service.WalletService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/v1")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("fund-wallet")
    public ResponseEntity<String> fundWallet(@RequestBody FundWalletDto request){
      return ResponseEntity.ok().body(walletService.fundWallet(request));
    }

    @PutMapping("fund-transfer")
    public ResponseEntity<String> walletTransfer(@RequestBody FundTransferDto request){
        return ResponseEntity.ok(walletService.walletTransfer(request));

    }

    @GetMapping("trans-history")
    public ResponseEntity<List<WalletHistoryDto>> getTransHistory(){
       return ResponseEntity.ok().body(walletService.transHistory());
    }
     @PostMapping("cancel-ticket")
    public ResponseEntity<String> cancelBooking(@RequestBody Map<String,String> ticketNo){
         String resp = walletService.cancelBooking(ticketNo.get("ticketNumber"));
         String[] rsp = resp.split(":");
         if(rsp.length == 2){
             return ResponseEntity.status(400).body(rsp[0]);
         }
        return ResponseEntity.ok().body(resp);
    }
}
