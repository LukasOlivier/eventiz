<script>
  import { post, get } from "$lib/api.js";
  import { onMount } from "svelte";
  
  export let cmsUrl = "";
  export let token = "";


  $: categories = [];

  onMount(async () => {
    try {
      const response = await get(
        `${cmsUrl}categories`,
        token
      );
      categories = response.data.map((category) => ({
        id: category.id,
        type: category.attributes.type,
      }));
    } catch (e) {
      console.error("CMS server is not available: " + e);
    }
  });

   async function sendAnnouncement() {
    const announcement = document.querySelector("#announcement").value;
    const category = document.querySelector("#categories").value;

    if (announcement === "") {
      alert("Please enter an announcement!");
      return;
    }

    if (category === "category") {
      alert("Please select a category!");
      return;
    }

    const data = {
      data: {
        message: announcement,
        category: category,
      },
    };
    const response = await post(
      `${cmsUrl}announcements`,
      data,
      token
    );
    if (response.status !== 200) {
      alert("Something went wrong!");
      return;
    }
    alert("Announcement sent!");
    document.querySelector("#announcement").value = "";
  }
</script>

<section>
  <form action="">
    <div class="flex-row">
      <label for="announcement">Send new announcement:</label>
      <select name="categories" id="categories">
        <option value="category" disabled selected> Category </option>
        {#each categories as category}
          <option value={category.id}>{category.type}</option>
        {/each}
      </select>
    </div>
    <textarea name="announcement" id="announcement" />
  </form>
  <input
    type="submit"
    id="send-announcement"
    value="Publish"
    on:click={sendAnnouncement}
  />
</section>

<style>
  #send-announcement {
    background-color: rgb(21, 94, 179);
    border: none;
    border-radius: 10px;
    padding: 0.5rem;
    margin-top: 2rem;
    width: 100%;
    font-family: "Staatliches", cursive;
    font-size: 1.5rem;
    color: #fff;
  }

  #send-announcement:hover {
    cursor: pointer;
    background-color: rgb(21, 108, 207);
  }

  .flex-row {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  textarea {
    margin-top: 2rem;
    border: none;
    border-bottom: white solid 1px;
    background: none;
    width: 100%;
    color: white;
    height: 100%;
  }

  textarea:focus {
    outline: none;
  }

  select {
    border: none;
    color: white;
    background: none;
    font-family: "Staatliches", cursive;
    font-size: 1rem;
  }

  select option {
    background: none;
    border: none;
    color: black;
  }

  label {
    font-family: "Staatliches", cursive;
    font-size: 1rem;
    color: #fff;
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
    margin-bottom: 5rem;
  }
</style>
