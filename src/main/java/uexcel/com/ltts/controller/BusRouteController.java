package uexcel.com.ltts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uexcel.com.ltts.entity.Route;
import uexcel.com.ltts.service.BusRouteService;

@RestController
@RequestMapping("/api/v1")
public class BusRouteController {
    private final BusRouteService busRouteService;

    public BusRouteController(BusRouteService busRouteService) {
        this.busRouteService = busRouteService;
    }

    @PostMapping("add-route")
    public ResponseEntity<String> addRoute(@RequestBody Route request) {
      return ResponseEntity.ok().body(busRouteService.addRoute(request));
    }

}
