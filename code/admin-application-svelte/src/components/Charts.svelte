<script>
  import { onMount } from "svelte";
  import { createEventDispatcher } from "svelte";
  import Chart from "chart.js/auto";

  export let temperatureData = [];
  export let airQualityData = [];

  let temperatureChart = null;
  let airQualityChart = null;

  let chartData = {
    temperature: [],

    airQuality: [],

    averageTemperature: 0,
    averageAirQuality: 0,
  };

  
  onMount(() => {
    const unsubscribeTemperature = temperatureData.subscribe(
      ($temperatureData) => {
        
        chartData.temperature = $temperatureData;
        chartData.averageTemperature = $temperatureData.pop().value;

        generateCharts();
      }
    );

    const unsubscribeAirQuality = airQualityData.subscribe(
      ($airQualityData) => {
        chartData.airQuality = $airQualityData;
        chartData.averageAirQuality = $airQualityData.pop().value;
        generateCharts();
      }
    );

    return () => {
      unsubscribeTemperature();
      unsubscribeAirQuality();
    };
  });
  function generateCharts() {
    const filteredTemperatureData = removeAverageFromData(chartData.temperature);
    const filteredAirQualityData = removeAverageFromData(chartData.airQuality);
    updateChart(
      "Temperature",
      filteredTemperatureData,
      "line-chart-temperature",
      temperatureChart
    );
    updateChart(
      "Air Quality",
      filteredAirQualityData,
      "line-chart-air_quality",
      airQualityChart
    );
  }

  function removeAverageFromData(data){
      return data.filter((value) => value.type !== "average");
    }

  function updateChart(type, data, canvasId, existingChart) {
    const ctx = document.querySelector(`#${canvasId}`);

    const dates = data.map((value) => value.timestamp).reverse();
    const values = data.map((value) => value.value).reverse();


    if (existingChart !== null) {
      existingChart.data.labels = dates;
      existingChart.data.datasets[0].data = values;
      existingChart.update();
    } else {
      const chart = new Chart(ctx, {
        type: "line",
        data: {
          labels: dates,
          datasets: [
            {
              label: type,
              data: values,
              fill: false,
              borderColor: "rgb(75, 192, 192)",
              tension: 0.1,
              backgroundColor: [
                "rgba(255, 99, 132, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(255, 206, 86, 0.2)",
              ],
            },
          ],
        },
        options: {
          scales: {
            x: {
              ticks: {
                color: "white",
              },
            },
            y: {
              ticks: {
                color: "white",
              },
            },
          },
          plugins: {
            legend: {
              labels: {
                color: "white",
              },
            },
          },
        },
      });

      if (type === "Temperature") {
        temperatureChart = chart;
      } else {
        airQualityChart = chart;
      }
    }
  }

  const dispatch = createEventDispatcher();

  function handleClick() {
    dispatch("buttonClicked", { page: "dashboard" });
  }
</script>

<body>
  <button id="go-back-btn" on:click={handleClick}>⬅️ Back to dashboard</button
    >
  <header>
    <h1>Eventiz Statistics</h1>
  </header>

  <div id="average-values">
    <p>Average temperature: {chartData.averageTemperature}°C</p>
    <p>Average air quality: {chartData.averageAirQuality}</p>
  </div>

  <section id="stats">
    <div style="width: 800px;"><canvas id="line-chart-temperature" /></div>
    <div style="width: 800px;"><canvas id="line-chart-air_quality" /></div>
  </section>
</body>

<style>

  #average-values {
    display: flex;
    flex-direction: row;
    justify-content: center;
    color: white;
    font-family: 'Staatliches', cursive;
    font-size: 1.5rem;
    padding: 4rem;
    background: rgb(21, 94, 179);
    gap: 40%;

  }
  #go-back-btn {
    font-family: "Staatliches", cursive;
    font-size: 1.5rem;
    color: #fff;
    text-decoration: none;
    border: none;
    background: none;
    position: fixed;
    top: 2rem;
    left: 2rem;
  }

  #go-back-btn:hover {
    cursor: pointer;
    text-decoration: underline;
  }
  header{
    text-align: center;
  }

  body {
    width: 100%;
  }

  #stats {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }

  header {
    display: flex;
    flex-direction: row;
    justify-content: center;
    gap: 30%;
    align-items: center;
    padding: 2rem 2rem;
    background: rgb(21, 94, 179);
    width: 100%;
  }

  h1 {
    font-family: "Staatliches", cursive;
    font-size: 3rem;
    color: #fff;
  }
</style>
