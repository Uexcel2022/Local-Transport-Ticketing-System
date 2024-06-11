package uexcel.com.ltts.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.BusDto;
import uexcel.com.ltts.entity.Bus;
import uexcel.com.ltts.entity.Route;
import uexcel.com.ltts.exception.CustomException;

import java.util.List;


@Service
public class BusRouteServiceImp implements BusRouteService {
    private final RepositoryService repositoryService;

    public BusRouteServiceImp(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Transactional
    public String addRoute(Route request) {
            if(repositoryService.getRouteRepository()
                    .existsRouteByOriginAndDestination(request.getOrigin(), request.getDestination())){

                return "Existing Routes: " + request.getOrigin() + "-" + request.getDestination();
            }

        if(repositoryService.getRouteRepository()
                .existsRouteByOriginAndDestination(request.getDestination(), request.getOrigin())){;

            return "Existing Routes: " + request.getDestination() + "-" + request.getOrigin();
        }

        Route routeInversion = new Route();
        routeInversion.setOrigin(request.getDestination());
        routeInversion.setDestination(request.getOrigin());
        routeInversion.setPrice(request.getPrice());

        repositoryService.getRouteRepository().save(request);
        repositoryService.getRouteRepository().save(routeInversion);

        return "Route added successfully.";
    }

    @Transactional
    public String addBus(BusDto request) {

        List<Route> routes = isExist(request.getOrigin(),request.getDestination());

        if(repositoryService.getBusRepository().existsBusByBusCode(request.getBusCode())){
            throw new CustomException("Bus is existing.","400");
        };

        Bus bus = new Bus();
        bus.setBusName(request.getBusName());
        bus.setBusCode(request.getBusCode());
        bus.setStartDate(request.getStartDate());
        bus.setRoute(routes);

        repositoryService.getBusRepository().save(bus);

        return "Bus added successfully.";
    }



    private List<Route> isExist(String origin, String destination) {
        Route routeTo = repositoryService.getRouteRepository().findByOriginAndDestination(origin,destination);

        if (routeTo == null) {
            throw new CustomException("Invalid route","400");
        }

        Route routeFro = repositoryService.getRouteRepository().findByOriginAndDestination(destination,origin);

        if (routeFro == null) {
            throw new CustomException("Invalid route","400");
        }

        return List.of(routeTo,routeFro);

    }
}
