package uexcel.com.ltts.service;

import uexcel.com.ltts.dto.FundTransferDto;
import uexcel.com.ltts.dto.FundWalletDto;
import uexcel.com.ltts.dto.WalletHistoryDto;

import java.util.List;

public interface WalletService {
    String fundWallet(FundWalletDto fundWalletDto);
    String walletTransfer(FundTransferDto request);
    public List<WalletHistoryDto> transHistory();
}
