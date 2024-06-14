package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.WalletTransaction;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WalletTransRepository extends JpaRepository<WalletTransaction,String> {

//    @Query(value = "SELECT p FROM WalletTransaction p WHERE p.wallet.client.phone=:phone")
    List<WalletTransaction> findByWalletIdAndTransactionDateIsAfter(String walletId,LocalDate dateFrom);
}
