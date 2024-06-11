package uexcel.com.ltts.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class FundWalletDto {
    private String walletNumber;
    private double amount;
    private String sourceAccount;
    private String transactionType;
    private String  sourceName;
    private String bank;
    private String cCnumber;
    private String cCtype;

}
