package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Bus;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
    boolean existsBusByBusCode(String busCode);
    List<Bus> findByBusCode(String busCode);
    Bus findBusByBusCode(String busCode);
}
