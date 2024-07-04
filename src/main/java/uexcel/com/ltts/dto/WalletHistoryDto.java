package uexcel.com.ltts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class WalletHistoryDto {
    private String transId;
    private String transType;
    private double amount;
    private LocalDate date;

    public WalletHistoryDto(String transId, double amount, LocalDate date,String transType) {
        this.transId = transId;
        this.amount = amount;
        this.date = date;
        this.transType = transType;

    }
}
