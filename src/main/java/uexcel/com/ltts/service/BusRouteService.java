package uexcel.com.ltts.service;

import org.springframework.stereotype.Service;
import uexcel.com.ltts.dto.BusDto;
import uexcel.com.ltts.dto.ChangeBusRouteDto;
import uexcel.com.ltts.entity.Route;



public interface BusRouteService {
    public String addRoute(Route request);
    String addBus(BusDto request);
}
