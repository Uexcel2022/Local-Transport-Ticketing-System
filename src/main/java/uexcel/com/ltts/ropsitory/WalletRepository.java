package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Wallet;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {

    @Query(nativeQuery = true,value = "SELECT * FROM wallet WHERE wallet_number=:phone")
    Wallet findByWalletNumber(String phone);
}
