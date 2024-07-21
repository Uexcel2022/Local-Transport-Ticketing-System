package uexcel.com.ltts.service;

import uexcel.com.ltts.dto.BusDto;
import uexcel.com.ltts.entity.Route;

import java.util.List;


public interface BusRouteService {
    public String addRoute(Route request);
    String addBus(BusDto request);

    Route findRouteByOriginDestination(String origin, String destination);

    List<Route> findAllRoutes();
}
