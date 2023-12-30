<script>
  import { post } from "$lib/api.js";

  export let rackData;
  export let rackText;
  export let apiUrl;
  export let jwtToken;

  async function toggleCloakroomStatus() {
    if (rackData.open === 1) {
      await post(`${apiUrl}cloakroom/close`, {}, jwtToken);
    } else {
      await post(`${apiUrl}cloakroom/open`, {}, jwtToken);
    }
  }

  async function editRackAmount() {
    const newAmount = prompt(
      `Enter new amount of max racks, current amount is ${rackData.max_racks}`
    );
    if (newAmount === null) {
      return;
    }
    if (newAmount === "" || isNaN(newAmount) || newAmount < 0) {
      alert("Please enter a valid number");
      return;
    }

    const data = {
      max_racks: newAmount,
    };

    const response = await post(
      `${apiUrl}cloakroom/racks/edit`,
      data,
      jwtToken
    );
    if (response.status === 400) {
      alert("The racks can not be changed while the cloakroom is open");
    } else {
      rackData = response;
    }
  }

  $: {
    rackText =
      rackData.open === 1
        ? `${rackData.occupied_racks} / ${rackData.max_racks}`
        : "Closed";
  }
</script>

<section>
  <div class="flex-row-between">
    <h1>Racks Occupied</h1>
    <button id="edit-racks" on:click={editRackAmount} />
  </div>

  <div class="flex-row-center">
    <h2>{rackText}</h2>
  </div>
</section>
{#if rackData.open === 1}
  <input
    type="button"
    value="Close Cloakroom"
    on:click={toggleCloakroomStatus}
  />
{:else}
  <input
    type="button"
    value="Open Cloakroom"
    class="open"
    on:click={toggleCloakroomStatus}
  />
{/if}

<style>
  #edit-racks {
    background: url("/edit.png") no-repeat center center;
    background-size: contain;
    width: 1rem;
    height: 1rem;
    border: none;
    margin: 0;
    padding: 0;
  }

  #edit-racks:hover {
    cursor: pointer;
  }

  .flex-row-center {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  .flex-row-between {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  input {
    max-width: 100%;
    background-color: #940000;
    border: none;
    border-radius: 10px;
    padding: 0.5rem;
    margin: 0.5rem;
    font-family: "Staatliches", cursive;
    font-size: 1.5rem;
    color: #fff;
  }

  input:hover {
    cursor: pointer;
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

  .open {
    background-color: #009400;
  }

  section {
    flex-grow: 1;
    background-color: #323447;
    border-radius: 10px;
    padding: 1rem;
    margin: 10px;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.75);
    color: white;
    max-width: 100%;
  }
</style>
