package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Checkin;
@Repository
public interface CheckingRepository extends JpaRepository<Checkin,String> {


    Boolean existsCheckinByBookingId(String id);

    Checkin findByBookingId(String id);
}
