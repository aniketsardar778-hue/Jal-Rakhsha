const map = L.map("mapContainer").setView([22.5726, 88.3639], 10);
L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap contributors"
}).addTo(map);

let marker = null;

function setMapMarker(lat, lng, text) {
    map.setView([lat, lng], 15);
    if (marker) map.removeLayer(marker);
    marker = L.marker([lat, lng]).addTo(map).bindPopup(text).openPopup();
}

document.getElementById("startBtn").addEventListener("click", () => {
    document.getElementById("assessment").scrollIntoView({ behavior: "smooth" });
});

document.getElementById("locationBtn").addEventListener("click", () => {
    const status = document.getElementById("gpsStatus");
    if (!navigator.geolocation) {
        status.textContent = "Geolocation is not supported by this browser.";
        return;
    }
    status.textContent = "Getting your current location...";
    navigator.geolocation.getCurrentPosition(
        async position => {
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;
            setMapMarker(lat, lng, "Your GPS Location");
            try {
                const location = await selectNearestStoredLocation(lat, lng);
                status.textContent = `GPS detected. Assessment location selected: ${location.locationName}, ${location.district}, ${location.state}`;
            } catch (e) {
                status.textContent = e.message;
            }
        },
        error => {
            status.textContent = `Unable to access location: ${error.message}`;
        },
        { enableHighAccuracy: true, timeout: 15000, maximumAge: 60000 }
    );
});

map.on("click", async e => {
    setMapMarker(e.latlng.lat, e.latlng.lng, "Selected Map Point");
    try {
        const location = await selectNearestStoredLocation(e.latlng.lat, e.latlng.lng);
        document.getElementById("gpsStatus").textContent = `Nearest stored assessment location selected: ${location.locationName}, ${location.district}, ${location.state}`;
    } catch (err) {
        document.getElementById("gpsStatus").textContent = err.message;
    }
});
