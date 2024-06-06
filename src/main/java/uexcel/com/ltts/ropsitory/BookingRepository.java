package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
}
