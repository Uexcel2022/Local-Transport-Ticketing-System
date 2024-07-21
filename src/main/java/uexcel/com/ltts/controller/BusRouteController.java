package uexcel.com.ltts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uexcel.com.ltts.dto.BusDto;
import uexcel.com.ltts.entity.Route;
import uexcel.com.ltts.service.BusRouteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BusRouteController {
    private final BusRouteService busRouteService;

    public BusRouteController(BusRouteService busRouteService) {
        this.busRouteService = busRouteService;
    }

    @PostMapping("add-route")
    public ResponseEntity<String> addRoute(@RequestBody Route request) {
      return ResponseEntity.status(HttpStatus.CREATED).body(busRouteService.addRoute(request));
    }

    @GetMapping("route")
    public ResponseEntity<Route> findRoute(@RequestParam("origin") String origin,
                                           @RequestParam("destination") String destination ) {
        return ResponseEntity.ok(busRouteService.findRouteByOriginDestination(origin,destination));
    }

    @GetMapping("all-route")
    public ResponseEntity<List<Route>> findAllRoute() {
        return ResponseEntity.ok(busRouteService.findAllRoutes());
    }

    @PostMapping("add-bus")
    public ResponseEntity<String> addBus(@RequestBody BusDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(busRouteService.addBus(request));
    }

//    @PutMapping("update-bus-route")
//    public ResponseEntity<String> updateBusRoute(@RequestBody ChangeBusRouteDto request) {
//        return ResponseEntity.ok().body(busRouteService.updateBusRoute(request));
//    }

}
