<script>
  import { post } from "$lib/api.js";

  export let apiUrl;
  export let storage;

  let email = "";
  let password = "";
  let error = "";

  async function login(e) {
    e.preventDefault();
    const data = {
      email: email,
      password: password,
    };
    const response = await post(`${apiUrl}login`, data);
    if (response.status === 400) {
      error = "Invalid credentials";
      return;
    }
    document.querySelector("#email").value = "";
    document.querySelector("#password").value = "";
    storage.set(response.data.authorisation.token);
  }
</script>

<form>
  <div>
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" bind:value={email} required />
  </div>
  <div>
    <label for="password">Password:</label>
    <input
      type="password"
      id="password"
      name="password"
      bind:value={password}
      required
    />
  </div>

  <input type="submit" value="Submit" on:click={login} />
  <p id="error">{error}</p>
</form>
<form action="" />

<style>
  #error {
    color: red;
    font-size: 1rem;
    font-family: "Staatliches", cursive;
  }
  input:invalid {
    border: 0.12rem solid rgb(252, 5, 5);
  }
  div {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 70%;
  }
  form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1rem;
  }
  input {
    margin-top: 0.2rem;
    padding: 0;
    width: 100%;
    height: 2rem;
  }
  label {
    font-family: "Staatliches", cursive;
    font-size: 1rem;
    color: #fff;
  }
  input[type="submit"] {
    background-color: rgb(21, 94, 179);
    border: none;
    border-radius: 0.2rem;
    padding: 0.5rem;
    margin-top: 2rem;
    width: 70%;
    font-family: "Staatliches", cursive;
    font-size: 1.5rem;
    color: #fff;
    height: auto;
  }
</style>
