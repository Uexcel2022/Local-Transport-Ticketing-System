package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
 Route findByOriginAndDestination(String from, String to);
}
