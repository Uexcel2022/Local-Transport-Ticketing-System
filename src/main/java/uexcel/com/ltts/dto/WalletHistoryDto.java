package uexcel.com.ltts.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WalletHistoryDto {
    private String transId;
    private double amount;

    public WalletHistoryDto(String transId, double amount) {
        this.transId = transId;
        this.amount = amount;
    }
}
