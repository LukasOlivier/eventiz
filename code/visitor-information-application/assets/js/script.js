"use strict";

const STRAPI_URL = "https://eventiz-strapi.lukasolivier.be";
const WEBSOCKET_URL = "ws://eventiz-ws.lukasolivier.be/app/websocket";
const CLOAKROOM_URL = "https://eventiz-laravel.lukasolivier.be/api/cloakroom"
const SENSORS_URL = "https://eventiz-laravel.lukasolivier.be/api/sensors";

init();

function init() {
    setCurrentTime();
    getAnouncement();
    initRacks();
    initSensors();
    initWebsockets();
    initIntervals();
}

function initIntervals() {
    setInterval(() => {
        setCurrentTime();
    }, 1000 * 60);
    setInterval(() => {
        getAnouncement();
    }, 1000 * 60 * 5);
}

async function getAnouncement() {
    console.log("getting annoucements");
    let data = await fetchDataWithURL(`${STRAPI_URL}/api/announcements`);
    renderAnouncements(getMessageFromData(data));
}

function getMessageFromData(data) {
    return data.data[data.data.length - 1].attributes.message;
}

function renderAnouncements(message) {
    document.querySelector("#anouncements p").innerHTML = message;
}

function initWebsockets() {
    const socket = new WebSocket(WEBSOCKET_URL);
    socket.addEventListener("open", () => {
        subscribeToChannel(socket);
    });
    socket.addEventListener("message", (event) => {
        const data = JSON.parse(event.data);
        if (data.event === "App\\Events\\RackUpdate") {
            updateRacks(data);
        } else if (data.event === "App\\Events\\SensorUpdate") {
            updateSensors(data);
        }
    });
}

function subscribeToChannel(socket) {
    const messages = [
        {
            event: "pusher:subscribe",
            data: {
                auth: "",
                channel: "racks"
            },
        },
        {
            event: "pusher:subscribe",
            data: {
                auth: "",
                channel: "sensors"
            },
        }
    ];
    messages.forEach((message) => {
        socket.send(JSON.stringify(message));
    });
}

function setCurrentTime() {
    const now = new Date();
    const minutes = now.getMinutes() < 10 ? `0${now.getMinutes()}` : now.getMinutes();
    const time = `${now.getHours()}:${minutes}`;
    document.querySelector("#time").innerHTML = time;
}

async function initRacks() {
    const data = await fetchDataWithURL(CLOAKROOM_URL);
    const racks = data.occupied_racks;
    const maxRacks = data.max_racks;
    document.querySelector("#racks p").innerHTML = `${racks}/${maxRacks}`;
}

async function initSensors() {
    const data = await fetchDataWithURL(SENSORS_URL);
    setTemp(data[0].value);
    setAir(data[1].value);
}

function updateRacks(data) {
    data = JSON.parse(data.data)[0];
    document.querySelector("#racks p").innerHTML = `${data.occupied_racks}/${data.max_racks}`;
}

function updateSensors(data) {
    data = JSON.parse(data.data);
    data.type === "temperature" ? setTemp(data.value) : setAir(data.value);
}

function setTemp(temp) {
    document.querySelector("#temp p").innerHTML = `${temp}°C`;
}

function setAir(air) {
    document.querySelector("#air p").innerHTML = `${air} PPM`;
}

async function fetchDataWithURL(url) {
    const response = await fetch(url);
    const data = await response.json();
    return data;
}
