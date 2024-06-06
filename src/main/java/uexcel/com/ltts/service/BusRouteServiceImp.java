package uexcel.com.ltts.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.BusDto;
import uexcel.com.ltts.dto.ChangeBusRouteDto;
import uexcel.com.ltts.entity.Bus;
import uexcel.com.ltts.entity.Route;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Repos;

import java.util.List;
import java.util.Set;

@Service
public class BusRouteServiceImp implements BusRouteService {
    private final Repos repos;

    public BusRouteServiceImp(Repos repos) {
        this.repos = repos;
    }

    @Transactional
    public String addRoute(Route request) {
            if(repos.getRouteRepository()
                    .existsRouteByOriginAndDestination(request.getOrigin(), request.getDestination())){

                return "Existing Routes: " + request.getOrigin() + "-" + request.getDestination();
            }

        if(repos.getRouteRepository()
                .existsRouteByOriginAndDestination(request.getDestination(), request.getOrigin())){;

            return "Existing Routes: " + request.getDestination() + "-" + request.getOrigin();
        }

        Route routeInversion = new Route();
        routeInversion.setOrigin(request.getDestination());
        routeInversion.setDestination(request.getOrigin());
        routeInversion.setPrice(request.getPrice());

        repos.getRouteRepository().save(request);
        repos.getRouteRepository().save(routeInversion);

        return "Route added successfully.";
    }

    @Transactional
    public String addBus(BusDto request) {

        List<Route> routes = isExist(request.getOrigin(),request.getDestination());

        if(repos.getBusRepository().existsBusByBusCode(request.getBusCode())){
            throw new CustomException("Bus is existing.","400");
        };

        Bus bus = new Bus();
        bus.setBusName(request.getBusName());
        bus.setBusCode(request.getBusCode());
        bus.setStartDate(request.getStartDate());
        bus.setRoute(routes);

        repos.getBusRepository().save(bus);

        return "Bus added successfully.";
    }

    public String updateBusRoute(ChangeBusRouteDto request) {

        List<Bus> buses = repos.getBusRepository().findByBusCode(request.getBusCode());
        for (Bus bus : buses) {
            for(Route route : bus.getRoute()){
                if((route.getOrigin().equals(request.getOrigin())&&
                        route.getDestination().equals(request.getDestination()))||
                        (route.getOrigin().equals(request.getDestination())&&
                        route.getDestination().equals(request.getOrigin()))
                ){
                    throw new CustomException("The bus route exist","400");
                }
            }
        }

//        Route toAddRouteTo = repos.getRouteRepository().findByOriginAndDestination(request
//                .getOrigin(),request.getDestination());
//        if(toAddRouteTo == null){
//            throw new CustomException("Invalid route","400");
//        }
//
//        Route toAddRouteFro = repos.getRouteRepository().findByOriginAndDestination(
//                request.getDestination(),request.getOrigin());
//
//        if(toAddRouteFro == null){
//            throw new CustomException("Invalid route","400");
//        }


        List<Route> routes = isExist(request.getOrigin(),request.getDestination());
        List<Bus> bus =  repos.getBusRepository().findByBusCode(request.getBusCode());

        Bus toUpdateBus = bus.get(0);
        toUpdateBus.setRoute(routes);
        repos.getBusRepository().save(toUpdateBus);

       return "Route updated successfully.";
    }

    private List<Route> isExist(String origin, String destination) {
        Route routeTo = repos.getRouteRepository().findByOriginAndDestination(origin,destination);

        if (routeTo == null) {
            throw new CustomException("Invalid route","400");
        }

        Route routeFro = repos.getRouteRepository().findByOriginAndDestination(destination,origin);

        if (routeFro == null) {
            throw new CustomException("Invalid route","400");
        }

        return List.of(routeTo,routeFro);

    }
}
