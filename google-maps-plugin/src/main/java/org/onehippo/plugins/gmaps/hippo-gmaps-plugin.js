function initializeMap(mapDiv, lat, lng, zoom, autocompleteId) {
    var latlng = new google.maps.LatLng(lat, lng);
    var myOptions = {
        zoom: parseInt(zoom),
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(mapDiv, myOptions);
    var markers = new Array();

    createMarker(latlng, markers, map);

    google.maps.event.addListener(map, 'click', function(event) {
        setZoomLevel(map, mapDiv);
        setGeoValues(mapDiv, event.latLng, parseInt(zoom));
        createMarker(event.latLng, markers, map);
    });

    // autocompletion
    var autoInput = $(autocompleteId).find('input');
    autoInput.geo_autocomplete({
        select: function(_event, _ui) {
            if (_ui.item.viewport) map.fitBounds(_ui.item.viewport);
        }
    });
}

function setZoomLevel(map, mapDiv){
    var container = $(mapDiv).parent();
    var zoomIn = container.find('.gmaps-zoom').find('input');

    zoomIn.val(map.getZoom());
    zoomIn.change();
}

function setGeoValues(mapDiv, location) {
    var container = $(mapDiv).parent();
    var lat = container.find('.gmaps-lat').find('input');
    var lng = container.find('.gmaps-lng').find('input');

    lat.val(location.lat());
    lat.change();

    lng.val(location.lng());
    lng.change();
}

function createMarker(location, markers, map) {

    clearAllMarkers(markers);

    var clickedLocation = new google.maps.LatLng(location);
    var marker = new google.maps.Marker({
        position: location,
        map: map,
        title: "Location"
    });

    markers.push(marker);
}

function clearAllMarkers(markers) {
    if (markers) {
        for (var i=0; i<markers.length; i++) {
            markers[i].setMap(null);
        }
    }
}

