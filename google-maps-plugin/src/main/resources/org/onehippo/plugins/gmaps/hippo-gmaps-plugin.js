function initializeMap(mapDiv, lat, lng, zoom, autocompleteId) {
    var latlng = new google.maps.LatLng(lat, lng);
    var myOptions = {
        zoom:parseInt(zoom),
        center:latlng,
        mapTypeId:google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(mapDiv, myOptions);
    var markers = new Array();

    createMarker(latlng, markers, map);
    //Autocomplete via GoogleJS api
    var input = document.getElementById('searchTextField');
    var autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.bindTo('bounds', map);

    google.maps.event.addListener(map, 'click', function (event) {
        setZoomLevel(mapDiv, map);
        setGeoValues(mapDiv, event.latLng);
        createMarker(event.latLng, markers, map);
    });

    google.maps.event.addListener(map, 'zoom_changed', function() {
        var zoomLevel = map.getZoom();
        setZoomLevelChanged(mapDiv, zoomLevel);
    });

    google.maps.event.addListener(autocomplete, 'place_changed', function () {
        var place = autocomplete.getPlace();
        var address = '';
        if (place.address_components) {
            address = [(place.address_components[0] &&
                place.address_components[0].short_name || ''),
                (place.address_components[1] &&
                    place.address_components[1].short_name || ''),
                (place.address_components[2] &&
                    place.address_components[2].short_name || '')
            ].join(' ');
        }
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(parseInt(17));
        }
        setZoomLevel(mapDiv, map);
        setGeoValues(mapDiv, map.getCenter());
        createMarker(map.getCenter(), markers, map);

    });
}

function setZoomLevel(mapDiv, mapInstance) {
    setZoomLevelChanged(mapDiv, mapInstance.getZoom());
}

function setZoomLevelChanged(mapDiv, newZoom) {
    var container = $(mapDiv).parent();
    var zoomIn = container.find('.gmaps-zoom').find('input');
    zoomIn.val(newZoom);
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
        position:location,
        map:map,
        title:"Location",
        icon:'resources/org.onehippo.plugins.gmaps.GmapsPlugin/images/beachflag.png',
        animation:google.maps.Animation.DROP
    });
    markers.push(marker);
}

function clearAllMarkers(markers) {
    if (markers) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }
    }
}

