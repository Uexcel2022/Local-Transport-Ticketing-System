package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.WalletTransaction;
@Repository
public interface WalletTransRepository extends JpaRepository<WalletTransaction,String> {
}
