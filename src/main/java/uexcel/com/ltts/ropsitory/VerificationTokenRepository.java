package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.VerificationToken;
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);
}
