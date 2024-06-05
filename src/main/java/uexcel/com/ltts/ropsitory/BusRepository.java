package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Bus;
@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
}
