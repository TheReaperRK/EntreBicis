/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.logic;

import cat.copernic.backend.entity.DTO.GpsPointDTO;
import cat.copernic.backend.entity.DTO.RouteDTO;
import cat.copernic.backend.entity.GpsPoint;
import cat.copernic.backend.entity.Route;
import cat.copernic.backend.entity.User;
import java.sql.Time;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlo
 */
@Service
public class mapToRoute {

    public Route mapToRoute(RouteDTO dto, User user) {
        Route route = new Route();
        route.setStartDate(dto.getStartDate());
        route.setEnd_date(dto.getEndDate());
        route.setDistance(dto.getDistance());
        route.setAverage_speed(dto.getAverageSpeed());
        route.setTotal_time(Time.valueOf(dto.getTotalTime()));
        route.setGenerated_balance(dto.getGeneratedBalance());
        route.setUser(user);
        return route;
    }

    public List<GpsPoint> mapToGpsPoints(List<GpsPointDTO> dtoList, Route route) {
        return dtoList.stream().map(dto -> {
            GpsPoint p = new GpsPoint();
            p.setLatitud(dto.getLatitud());
            p.setLongitud(dto.getLongitud());
            p.setTimestamp(dto.getTimestamp());
            p.setRoute(route);
            return p;
        }).toList();
    }
}
