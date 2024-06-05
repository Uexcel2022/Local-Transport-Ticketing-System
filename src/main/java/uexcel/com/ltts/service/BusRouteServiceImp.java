package uexcel.com.ltts.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.entity.Bus;
import uexcel.com.ltts.entity.Route;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Repos;

import java.util.Set;

@Service
public class BusRouteServiceImp implements BusRouteService {
    private final Repos repos;

    public BusRouteServiceImp(Repos repos) {
        this.repos = repos;
    }

    public String addRoute(Route request) {
        repos.getRouteRepository().save(request);
        return "Route added successfully";
    }

    public ResponseEntity<String> addBus(Bus request) {
        Route route = repos.getRouteRepository().findByOriginAndDestination("oshodi","iyanipaja");
        if (route == null) {
            throw new CustomException("Invalid route","400");
        }

        request.setRoute(Set.of(route));
        repos.getBusRepository().save(request);
        return ResponseEntity.ok("Bus added successfully");
    }
}
