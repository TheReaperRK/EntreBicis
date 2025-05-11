function initializeMap(routePoints) {
    if (!routePoints || routePoints.length === 0) return;

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 16,
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

    const infoWindow = new google.maps.InfoWindow();
    const markers = [];

    routePoints.forEach(function (point, index) {
        let iconConfig, alwaysVisible = false;

        if (index === 0 || index === routePoints.length - 1) {
            iconConfig = {
                path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                fillColor: '#27C08A',
                scale: 6,
                fillOpacity: 1,
                strokeWeight: 2,
                strokeColor: '#ffffff'
            };
            alwaysVisible = true;
        } else {
            iconConfig = {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 7,
                fillColor: '#27C08A',
                fillOpacity: 1,
                strokeWeight: 2,
                strokeColor: '#ffffff'
            };
        }

        const marker = new google.maps.Marker({
            position: { lat: point.latitud, lng: point.longitud },
            title: index === 0 ? 'Inici' : index === routePoints.length - 1 ? 'Final' : 'Punt ' + (index + 1),
            icon: iconConfig,
            map: alwaysVisible ? map : null // solo los extremos visibles por defecto
        });

        marker.addListener('click', function () {
            infoWindow.setContent(
                `<strong>${marker.getTitle()}</strong><br>Lat: ${point.latitud.toFixed(5)}<br>Lng: ${point.longitud.toFixed(5)}`
            );
            infoWindow.open(map, marker);
        });

        markers.push({ marker, alwaysVisible });
    });

    function updateMarkersVisibility(zoom) {
        markers.forEach(obj => {
            if (obj.alwaysVisible) {
                obj.marker.setMap(map);
            } else {
                obj.marker.setMap(zoom >= 18 ? map : null); // solo mostrar si zoom >= 19
            }
        });
    }

    google.maps.event.addListener(map, 'zoom_changed', function () {
        updateMarkersVisibility(map.getZoom());
    });

    updateMarkersVisibility(map.getZoom());
}
