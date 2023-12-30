<script>
  import { onMount, onDestroy } from "svelte";
  import { browser } from "$app/environment";
  import TimeStamps from "../components/TimeStamps.svelte";

  let mapElement;
  let map;

  export let emergencies;

  onMount(async () => {
    if (browser) {
      const leaflet = await import("leaflet");

      let currentLocation = [0, 0];

      if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition((position) => {
          currentLocation = [
            position.coords.latitude,
            position.coords.longitude,
          ];

          map = leaflet.map(mapElement).setView(currentLocation, 17);

          leaflet
            .tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
              attribution:
                '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
            })
            .addTo(map);

          for (const emergency of emergencies) {
            const [lat, lng] = emergency.location.split(",");
            const type = emergency.type.toLowerCase();
            addMarker(lat, lng, type);
          }
        });
      } else {
        document.querySelector("section").innerHTML =
          "Geolocation not available";
      }
    }
  });

  function addMarker(lat, lng, icon) {
    const markerIcon = icon + "-marker.png";
    L.marker([lat, lng], {
      icon: L.icon({
        iconUrl: markerIcon,
        iconSize: [32, 32],
        iconAnchor: [16, 32],
      }),
    }).addTo(map);
  }

  onDestroy(async () => {
    if (map) {
      map.remove();
    }
  });
</script>

<section>
  <div bind:this={mapElement}><p>Please enable location</p></div>
  <TimeStamps {emergencies} />
</section>

<style>
  @import "leaflet/dist/leaflet.css";
  section {
    flex-shrink: 1;
    border-radius: 10px;
    background-color: #323447;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.75);
    margin: 10px 0 10px 10px;
    padding: 0.5rem;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    flex-grow: 5;
    flex-shrink: 5;
  }
  div {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    flex-grow: 2;
  }
  p {
    font-family: "Staatliches", cursive;
    font-size: 1rem;
    color: #fff;
  }
</style>
