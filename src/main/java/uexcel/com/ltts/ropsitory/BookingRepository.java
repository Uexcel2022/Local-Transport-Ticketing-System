package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Booking;
import uexcel.com.ltts.entity.Route;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    boolean existsBookingByRouteAndStatus(Route route, String status);

    Booking findByTicketNumberAndStatus(String ticketNumber,String status);
}
