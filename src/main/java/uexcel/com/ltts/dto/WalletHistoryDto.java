package uexcel.com.ltts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class WalletHistoryDto {
    private String transId;
    private double amount;
    private LocalDate date;

    public WalletHistoryDto(String transId, double amount, LocalDate date) {
        this.transId = transId;
        this.amount = amount;
        this.date = date;

    }
}
