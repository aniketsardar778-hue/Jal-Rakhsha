// ================================
// SMOOTH SCROLL
// ================================

document.getElementById("startBtn").addEventListener("click", () => {
    document.getElementById("assessment").scrollIntoView({
        behavior: "smooth"
    });
});

// ================================
// LEAFLET MAP
// ================================

const map = L.map("mapContainer").setView([22.5726, 88.3639], 10);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap"
}).addTo(map);

let marker;

// ================================
// GPS LOCATION
// ================================

document.getElementById("locationBtn").addEventListener("click", () => {

    if (navigator.geolocation) {

        navigator.geolocation.getCurrentPosition(position => {

            const lat = position.coords.latitude;
            const lng = position.coords.longitude;

            map.setView([lat, lng], 15);

            if (marker) {
                map.removeLayer(marker);
            }

            marker = L.marker([lat, lng])
                .addTo(map)
                .bindPopup("Your Location")
                .openPopup();

            // Let script1.js match this GPS point to a known
            // location and fill the assessment form's dropdown.
            if (typeof window.handleLocationFound === "function") {
                window.handleLocationFound(lat, lng);
            }

        }, (err) => {
            alert("Unable to access location: " + (err && err.message ? err.message : "unknown error"));
        });

    } else {
        alert("Geolocation not supported.");
    }

});

// ================================
// CLICK ON MAP
// ================================

map.on("click", function (e) {

    if (marker) {
        map.removeLayer(marker);
    }

    marker = L.marker(e.latlng)
        .addTo(map)
        .bindPopup(
            "Selected Location<br>" +
            e.latlng.lat.toFixed(5) +
            ", " +
            e.latlng.lng.toFixed(5)
        )
        .openPopup();

    // Let script1.js match this point to a known location and
    // fill the assessment form's dropdown.
    if (typeof window.handleLocationFound === "function") {
        window.handleLocationFound(e.latlng.lat, e.latlng.lng);
    }

});
