/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/reactjs.jsx to edit this template
 */
function initializeMap(routePoints) {
    if (!routePoints || routePoints.length === 0) return;

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: { lat: routePoints[0].latitud, lng: routePoints[0].longitud }
    });

    var routePath = new google.maps.Polyline({
        path: routePoints.map(function (point) {
            return { lat: point.latitud, lng: point.longitud };
        }),
        geodesic: true,
        strokeColor: '#27C08A',
        strokeOpacity: 1.0,
        strokeWeight: 4
    });

    routePath.setMap(map);

    routePoints.forEach(function (point, index) {
        let iconConfig;

        if (index === 0) {
                        // Primer punto (INICIO - Bandera de salida)
                        iconConfig = {
                            path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                            fillColor: '#27C08A',
                            scale: 6,
                            fillOpacity: 1,
                            strokeWeight: 2,
                            strokeColor: '#ffffff'
                        };
                    } else if (index === routePoints.length - 1) {
                        // Último punto (FINAL - Bandera de meta)
                        iconConfig = {
                            path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                            fillColor: '#27C08A',
                            scale: 6,
                            fillOpacity: 1,
                            strokeWeight: 2,
                            strokeColor: '#ffffff'
                        };
                    } else {
                        // Puntos intermedios (círculo verde)
                        iconConfig = {
                            path: google.maps.SymbolPath.CIRCLE,
                            scale: 7,
                            fillColor: '#27C08A',
                            fillOpacity: 1,
                            strokeWeight: 2,
                            strokeColor: '#ffffff'
                        };
                    }

        new google.maps.Marker({
            position: { lat: point.latitud, lng: point.longitud },
            map: map,
            title: index === 0 ? 'Inici' : index === routePoints.length - 1 ? 'Final' : 'Punt ' + (index + 1),
            icon: iconConfig
        });
    });
}

