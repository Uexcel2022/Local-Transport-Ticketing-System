package uexcel.com.ltts.dto;

import lombok.Getter;
import lombok.Setter;

import javax.crypto.spec.PSource;

@Getter
@Setter
public class FundTransferDto {
    private String payerWallet;
    private  String payeeWallet;
    private String sourceName;
    private  double amount;
}
