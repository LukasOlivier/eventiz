<script>
  import { createEventDispatcher } from 'svelte';

  export let sensorName;
  export let value;
  export let unit;


  const dispatch = createEventDispatcher();

  function handleClick() {
    dispatch('buttonClicked', { page: 'stats' });
  }
</script>

<section>
  <div class="flex-row-between">
    <h1>{sensorName}</h1>
    <button on:click={handleClick} class="stats" />
  </div>
  <div class="flex-row">
    {#if value === "" || sensorName === ""}
      <p>Loading data...</p>
    {:else}
      <h2>{value}</h2>
      <p>{unit}</p>
    {/if}
  </div>
</section>

<style>
  .stats {
    background: url("/stats.png");
    background-size: cover;
    background-repeat: no-repeat;
    width: 30px;
    height: 30px;
    border: none;
  }
  .stats:hover {
    cursor: pointer;
  }
  .flex-row-between {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
  .flex-row {
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin-bottom: 2rem;
    width: 100%;
    min-width: 100%;
    align-items: center;
  }

  h1 {
    font-family: "Staatliches", cursive;
    font-size: 1rem;
    color: #fff;
  }

  h2 {
    font-family: "Staatliches", cursive;
    font-size: 5rem;
    margin-top: 1rem;
    text-align: center;
    color: #fff;
  }

  p {
    font-family: "Staatliches", cursive;
    font-size: 1rem;
    color: #fff;
    transform: translate(0.5rem, 1rem);
  }

  section {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: space-between;
    flex-grow: 1;
    background-color: #323447;
    border-radius: 10px;
    padding: 1rem;
    margin: 10px;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.75);
    color: white;
  }
</style>
