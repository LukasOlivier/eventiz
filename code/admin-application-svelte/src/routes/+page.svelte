<script>
  import LeafletMap from "$lib/LeafletMap.svelte";
  import { onMount } from "svelte";
  import Echo from "laravel-echo";
  import Pusher from "pusher-js"; // Although we don't use this directly, it is a dependency of laravel-echo
  import { browser } from "$app/environment";
  import SensorField from "../components/SensorField.svelte";
  import RackField from "../components/RackField.svelte";
  import AnnouncementField from "../components/AnnouncementField.svelte";
  import Login from "../components/Login.svelte";
  import { get } from "$lib/api.js";
  import { writable } from "svelte/store";
  import Charts from "../components/Charts.svelte";
  import EmergencyButton from "../components/EmergencyButton.svelte";

  import {
    PUBLIC_LARAVEL_URL,
    PUBLIC_ECHO_WSHOST,
    PUBLIC_ECHO_WSPORT,
    PUBLIC_ECHO_CLUSTER,
    PUBLIC_ECHO_KEY,
    PUBLIC_ECHO_FORCE_TLS,
    PUBLIC_ECHO_DISABLE_STATS,
    PUBLIC_ECHO_BROADCAST_DRIVER,
    PUBLIC_STRAPI_URL,
    PUBLIC_STRAPI_TOKEN,
  } from "$env/static/public";
  

  export const jwtStorage = writable(null);

  let temperature = "";
  let airQuality = "";
  let rackData = "";
  let emergencies = [];
  let jwtToken = null;
  let currentPage = "dashboard";
  export const temperatureData = writable([]);
  export const airQualityData = writable([]);

  jwtStorage.subscribe((value) => {
    jwtToken = value;
  });

  function parseBool(string) {
    return string === "true";
  }

  function registerWebSockets() {
    window.Echo = new Echo({
      broadcaster: PUBLIC_ECHO_BROADCAST_DRIVER,
      key: PUBLIC_ECHO_KEY,
      wsHost: PUBLIC_ECHO_WSHOST,
      wssHost: PUBLIC_ECHO_WSHOST,
      wsPort: parseInt(PUBLIC_ECHO_WSPORT),
      wssPort: parseInt(PUBLIC_ECHO_WSPORT),
      disableStats: parseBool(PUBLIC_ECHO_DISABLE_STATS),
      forceTLS: parseBool(PUBLIC_ECHO_FORCE_TLS),
      cluster: PUBLIC_ECHO_CLUSTER,
    });

    window.Echo.channel("sensors").listen("SensorUpdate", (e) => {
      if (e.type === "temperature") {
        temperature = e.value;
        temperatureData.update((data) => [e, ...data]);
      } else {
        airQuality = e.value;
        airQualityData.update((data) => [e, ...data]);
      }
    });

    window.Echo.channel("racks").listen("RackUpdate", (e) => {
      const rackDataFromWebSocket = e[0];
      rackData = rackDataFromWebSocket;
    });

    window.Echo.channel("emergencies").listen("EmergencyUpdate", (e) => {
      emergencies = emergencies.concat(e);
    });
  }

  async function fetchData() {
    emergencies = await get(`${PUBLIC_LARAVEL_URL}emergencies`);

    const sensorData = await get(`${PUBLIC_LARAVEL_URL}sensors`);

    rackData = await get(`${PUBLIC_LARAVEL_URL}cloakroom`);

    temperature = sensorData[0].value;
    airQuality = sensorData[1].value;

    const newTemperatureData = await get(
      `${PUBLIC_LARAVEL_URL}sensors/statistics?type=temperature`
    );

    temperatureData.update((data) => newTemperatureData.reverse().slice(0, 10));

    const newAirQualityData = await get(
      `${PUBLIC_LARAVEL_URL}sensors/statistics?type=air_quality`
    );
    airQualityData.update((data) => newAirQualityData.reverse().slice(0, 10));
  }

  onMount(async () => {
    fetchData();

    if (browser) {
      registerWebSockets();
    }
  });

  function handleButtonClick(event) {
    const { page } = event.detail;
    currentPage = page;
  }
</script>

{#if jwtToken == null}
  <h1 class="page-title">EventIz - Admin</h1>
  <Login apiUrl={PUBLIC_LARAVEL_URL} storage={jwtStorage} />
{:else if currentPage === "dashboard"}
  <h1 class="page-title">EventIz - Admin</h1>

  {#if temperature === "" || airQuality === "" || rackData === ""}
    <div id="loading">
      <img src="/loading.gif" alt="loading" />
      <p>Getting info...</p>
    </div>
  {:else}
    <section id="dashboard">
      <div class="flex-row">
        <section class="sensor-container">
          <section class="top-row">
            <SensorField
              sensorName="Temperature"
              unit="°C"
              value={temperature}
              on:buttonClicked={handleButtonClick}
            />
            <SensorField
              sensorName="Air Quality"
              unit="PPM"
              value={airQuality}
              on:buttonClicked={handleButtonClick}
            />
          </section>
          <RackField {rackData} apiUrl={PUBLIC_LARAVEL_URL} {jwtToken} />
        </section>
        <LeafletMap {emergencies} />
      </div>
      <div id="bottom-row" class="flex-row">
        <AnnouncementField cmsUrl={PUBLIC_STRAPI_URL} token={PUBLIC_STRAPI_TOKEN} />
        <EmergencyButton apiUrl={PUBLIC_LARAVEL_URL}/>
      </div>
    </section>
  {/if}
{:else if currentPage === "stats"}
  <Charts
    {airQualityData}
    {temperatureData}
    on:buttonClicked={handleButtonClick}
  />
{/if}

<style>
  #bottom-row{
    height: 20rem;
  }
  #dashboard {
    width: 80%;
    margin: auto;
  }

  #loading {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 60vh;
    color: white;
    font-size: 3rem;
    gap: 2rem;
    font-family: "Staatliches", cursive;
  }

  #loading img {
    width: 10rem;
    height: 10rem;
  }
  .page-title {
    margin-top: 2rem;
    margin-bottom: 3rem;
    font-family: "Staatliches", cursive;
    font-size: 4rem;
    color: #fff;
    width: 100%;
    text-align: center;
  }
  @media (max-width: 60rem) {
    .flex-row {
      display: flex;
      flex-direction: column;
      justify-content: center;
      margin-bottom: 3rem;
      height: 100vh;
    }
  }
  @media (min-width: 60rem) {
    .flex-row {
      display: flex;
      flex-direction: row;
      justify-content: center;
      margin-bottom: 3rem;
      height: 50vh;
    }
  }

  .sensor-container {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    flex-grow: 1;
    flex-shrink: 5;
  }

  .top-row {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    flex-grow: 1;
  }
</style>
