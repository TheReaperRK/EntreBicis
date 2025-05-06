/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.logic;

import cat.copernic.backend.entity.DTO.GpsPointDTO;
import cat.copernic.backend.entity.DTO.RouteDTO;
import cat.copernic.backend.entity.DTO.RouteDtoClear;
import cat.copernic.backend.entity.DTO.RouteUserSpeedDistanceDTO;
import cat.copernic.backend.entity.GpsPoint;
import cat.copernic.backend.entity.Route;
import cat.copernic.backend.entity.SystemParameters;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.State;
import cat.copernic.backend.entity.enums.Validation;
import cat.copernic.backend.entity.mappers.RouteMapper;
import cat.copernic.backend.repository.GpsPointRepo;
import cat.copernic.backend.repository.RouteRepo;
import cat.copernic.backend.repository.SystemParametersRepo;
import cat.copernic.backend.repository.UserRepo;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator; // Asegúrate de importar esto

/**
 *
 * @author carlo
 */
@Service
public class RouteService {

    @Autowired
    private RouteRepo routeRepository;

    @Autowired
    private GpsPointRepo gpsPointRepository;

    @Autowired
    private SystemParametersRepo spRepo;

    @Autowired
    private UserRepo usuariRepository;

    @Autowired
    private RouteMapper routeMapper;

    public List<RouteDtoClear> getRoutesByUser(User usuari) {
        List<Route> rutes = routeRepository.findByUser(usuari);
        System.out.println(rutes);
        return rutes.stream().map(routeMapper::toDtoClear).collect(Collectors.toList());
    }

    public void saveRoute(RouteDTO dto) {
        User usuari = usuariRepository.findByMail(dto.getUserEmail());

        Route route = new Route();
        route.setStartDate(dto.getStartDate());
        route.setEnd_date(dto.getEndDate());
        route.setDistance(dto.getDistance());
        route.setAverage_speed(dto.getAverageSpeed());
        route.setTotal_time(Time.valueOf(dto.getTotalTime()));
        route.setGenerated_balance((calculateBalance(dto.getDistance())).intValue());
        route.setUser(usuari);
        route.setValidation_state(Validation.NOT_CHECKED); // Por defecto no validada
        route.setState(State.ENDED);

        routeRepository.save(route);

        List<GpsPoint> gpsPoints = dto.getGpsPoints().stream().map(gpsDto -> {
            GpsPoint point = new GpsPoint();
            point.setLatitud(gpsDto.getLatitud());
            point.setLongitud(gpsDto.getLongitud());
            point.setTimestamp(gpsDto.getTimestamp());
            point.setRoute(route);
            return point;
        }).collect(Collectors.toList());

        gpsPointRepository.saveAll(gpsPoints);
    }

    public void savePoints(List<GpsPointDTO> gpsPoints) {
        List<GpsPoint> entities = gpsPoints.stream().map(dto -> {
            GpsPoint point = new GpsPoint();
            point.setLatitud(dto.getLatitud());
            point.setLongitud(dto.getLongitud());
            point.setTimestamp(dto.getTimestamp());
            return point;
        }).collect(Collectors.toList());

        gpsPointRepository.saveAll(entities);
    }

    public Route getLastRoute(String userEmail) {
        System.out.println(routeRepository.findFirstByUserMailOrderByStartDateDesc(userEmail));
        return routeRepository.findFirstByUserMailOrderByStartDateDesc(userEmail);
    }

    public Double calculateBalance(double distance) {
        List<SystemParameters> temp = spRepo.findAll();

        SystemParameters sp = temp.get(0);

        double pointsXKm = sp.getPointsPerKilometer();

        return distance * pointsXKm;
    }

    public List<RouteUserSpeedDistanceDTO> getAllRoutesAsDTO() {
        List<Route> routes = routeRepository.findAll();

        return routes.stream()
                .sorted(Comparator.comparing(Route::getStartDate).reversed()) // Ordenar por fecha descendente
                .map(route -> {
                    RouteUserSpeedDistanceDTO dto = new RouteUserSpeedDistanceDTO();
                    dto.setIdRouteDTO(route.getId_route());
                    dto.setDistance(route.getDistance());
                    dto.setAverageSpeed(route.getAverage_speed());
                    dto.setTotalTime(route.getTotal_time().toString());
                    dto.setGeneratedBalance(route.getGenerated_balance());
                    dto.setUserEmail(route.getUser().getMail());
                    dto.setValidation_state(route.getValidation_state().toString());
                    return dto;
                }).collect(Collectors.toList());
    }

    public Route getRouteById(Long id) {
        return routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Ruta no trobada"));
    }

    public RouteDTO convertToDto(Route route) {
        RouteDTO dto = new RouteDTO();

        dto.setIdRouteDTO(route.getId_route());
        dto.setDistance(route.getDistance());
        dto.setAverageSpeed(route.getAverage_speed());
        dto.setTotalTime(route.getTotal_time().toString());
        dto.setGeneratedBalance(route.getGenerated_balance());
        dto.setUserEmail(route.getUser().getMail());

        // Mapear puntos GPS
        List<GpsPointDTO> gpsPoints = gpsPointRepository.findByRoute(route).stream().map(point -> {
            GpsPointDTO dtoPoint = new GpsPointDTO();
            dtoPoint.setLatitud(point.getLatitud());
            dtoPoint.setLongitud(point.getLongitud());
            dtoPoint.setTimestamp(point.getTimestamp());
            return dtoPoint;
        }).toList();

        dto.setGpsPoints(gpsPoints);

        return dto;
    }

}
