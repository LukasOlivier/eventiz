<script>
  import { post, get } from "$lib/api.js";
  import { onMount } from "svelte";

  export let apiUrl = "";

  onMount(async () => {});

  async function sendAlarmTrigger() {
    try {
      const response = await post(`${apiUrl}alarm`);
      if (response.status !== 200) {
        alert("Something went wrong!");
        return;
      }
      alert("Alarm triggered!");
    } catch (error) {
      alert("Something went wrong!");
    }
  }
</script>

<button id="alarm" on:click={sendAlarmTrigger}>Trigger alarm</button>

<style>
  #alarm {
    background-color: #ff0000;
    color: white;
    border: none;
    border-radius: 10px;
    padding: 1rem;
    font-size: 1.5rem;
    font-family: "Staatliches", cursive;
    cursor: pointer;
    flex-grow: 1;
    margin-bottom: 5rem;
    margin-top: 0.5rem;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.75);
    transition:
      background-color 0.3s,
      box-shadow 0.3s;
  }

  #alarm:hover {
    /* Hover effect styles */
    background-color: #cc0000;
  }

  #alarm:active {
    /* Button press effect styles */
    background-color: #aa0000;
    box-shadow: inset 0px 0px 10px 0px rgba(0, 0, 0, 0.75);
  }
</style>
