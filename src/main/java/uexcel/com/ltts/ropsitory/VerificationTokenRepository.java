package uexcel.com.ltts.ropsitory;

import org.antlr.v4.runtime.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.VerificationToken;

import java.util.Date;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);
    VerificationToken findByClientId(String clientId);
}
