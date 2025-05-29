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
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator; // Asegúrate de importar esto

/**
 * Servicio que gestiona la lógica de negocio relacionada con las rutas.
 * Incluye operaciones para guardar rutas, convertirlas a DTOs, calcular estadísticas como velocidad máxima,
 * y obtener rutas por usuario o ID.
 * 
 * Este servicio interactúa con las entidades Route, GpsPoint y SystemParameters, y con sus respectivos repositorios.
 * 
 * @author Carlo
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
    private SystemParametersRepo systemParametersRepo;

    @Autowired
    private RouteMapper routeMapper;

    /**
     * Obtiene todas las rutas asociadas a un usuario y las convierte a DTOs simplificados.
     * 
     * @param usuari Usuario del cual se desean obtener las rutas.
     * @return Lista de objetos RouteDtoClear correspondientes a las rutas del usuario.
     */
    public List<RouteDtoClear> getRoutesByUser(User usuari) {
        List<Route> rutes = routeRepository.findByUser(usuari);
        System.out.println(rutes);
        return rutes.stream().map(routeMapper::toDtoClear).collect(Collectors.toList());
    }

    /**
     * Guarda una nueva ruta y sus puntos GPS asociados en la base de datos.
     * 
     * @param dto DTO de la ruta con datos de distancia, velocidad media, tiempo total, usuario, etc.
     */
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
                    dto.setStartDate(route.getStartDate()); // o el getter que uses
                    dto.setTotalTime(route.getTotal_time().toString());
                    dto.setGeneratedBalance(route.getGenerated_balance());
                    dto.setUserEmail(route.getUser().getMail());
                    dto.setValidation_state(route.getValidation_state().toString());
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<RouteUserSpeedDistanceDTO> getAllUserRoutesAsDTO(User user) {
        List<Route> routes = routeRepository.findByUser(user);

        return routes.stream()
                .sorted(Comparator.comparing(Route::getStartDate).reversed()) // Ordenar por fecha descendente
                .map(route -> {
                    RouteUserSpeedDistanceDTO dto = new RouteUserSpeedDistanceDTO();
                    dto.setIdRouteDTO(route.getId_route());
                    dto.setDistance(route.getDistance());
                    dto.setAverageSpeed(route.getAverage_speed());
                    dto.setStartDate(route.getStartDate()); // o el getter que uses
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
        dto.setStartDate(route.getStartDate() != null ? route.getStartDate() : null);
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

    public double calcularVelocitatMaxima(Route route) {
        List<GpsPoint> punts = gpsPointRepository.findByRoute(route);

        System.out.println(punts);

        if (punts == null || punts.size() < 2) {
            return 0.0;
        }

        double velocitatMaxima = 0.0;

        // Ordenar por timestamp por si no viene ordenado
        punts.sort(Comparator.comparing(GpsPoint::getTimestamp));

        for (int i = 1; i < punts.size(); i++) {
            GpsPoint anterior = punts.get(i - 1);
            GpsPoint actual = punts.get(i);

            double distanciaMetres = calcularDistancia(
                    anterior.getLatitud().doubleValue(),
                    anterior.getLongitud().doubleValue(),
                    actual.getLatitud().doubleValue(),
                    actual.getLongitud().doubleValue()
            );

            System.out.println("Punt " + (i - 1) + ": " + anterior.getTimestamp());
            System.out.println("Punt " + i + ": " + actual.getTimestamp());
            System.out.println("Diferència segons: " + Duration.between(anterior.getTimestamp(), actual.getTimestamp()).getSeconds());

            long milisegons = Duration.between(anterior.getTimestamp(), actual.getTimestamp()).toMillis();
            if (milisegons >= 10) {
                double hores = milisegons / 3600000.0; // 1 hora = 3600000 ms
                double velocitat = (distanciaMetres / 1000.0) / hores; // km/h
                velocitatMaxima = Math.max(velocitatMaxima, velocitat);
            }

        }
        System.out.println("vel max" + velocitatMaxima);
        return velocitatMaxima;
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radio de la Tierra en metros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public String obtenirColorVelocitatMaxima(Route route) {
        double vmax = calcularVelocitatMaxima(route);
        double vconfig = systemParametersRepo.findById("default")
                .map(SystemParameters::getAverageValidSpeed)
                .orElse(25.0);

        if (vmax > vconfig + 10) {
            return "danger"; // rojo
        }
        if (vmax >= vconfig) {
            return "warning";    // amarillo
        }
        return "ok";   // blanco
    }
}
