<script>
  export let emergencies = [];

  function textFormatter(rawText) {
    switch (rawText) {
      case "FIRE":
        return " A fire took place";
      case "MEDICAL":
        return "A medical emergency took place";
      case "OTHER":
        return "An emergency took place";
      default:
        return "Some emergency took place";
    }
  }

  function timeFormatter(rawTime) {
    const time = rawTime.split("T")[1].split(".")[0];
    const hours = time.split(":")[0];
    const minutes = time.split(":")[1];
    return `${hours}:${minutes}`;
  }
</script>

<section>
  <h2>Recent Events:</h2>

  <ul class="emergency-container">
    {#if emergencies.length === 0}
      <li>No emergencies yet &#128516;</li>
    {/if}
    {#each emergencies.reverse() as emergency}
      <li>
        {timeFormatter(emergency.created_at)} - {textFormatter(emergency.type)}
      </li>
    {/each}
  </ul>
</section>

<style>
  @import url("https://fonts.googleapis.com/css2?family=Lato:wght@300;700&family=Staatliches&display=swap");

  ul {
    overflow: scroll;
    height: 90%;
  }
  section {
    flex-grow: 1;
    margin-left: 1rem;
  }
  h2 {
    font-family: "Staatliches", cursive;
    font-size: 1.5rem;
    color: #fff;
    margin-bottom: 1rem;
  }
  li {
    font-family: "Lato", sans-serif;
    font-size: 1rem;
    color: #fff;
    padding: 0.5rem 0;
  }
  li:hover {
    cursor: pointer;
    background-color: #494a5a;
  }

  li:not(:last-child) {
    border-bottom: 1px solid #e0e0e05e;
  }
</style>
