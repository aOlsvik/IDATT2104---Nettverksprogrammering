<template>
  <div class="container">
    <form @submit.prevent="submit()">
      <label for="code-input">Write your Python code here</label>
      <textarea name="code-input" id="code-input" placeholder="Your code" v-model="userCodeInput"></textarea>
      <button id="submit" type="submit">Compile and run code!</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

  export default {
    data() {
      return {
        userCodeInput: ""
      }
    }, 
    methods: {
      async submit() {
        console.log(this.userCodeInput)
        const data = {
          code: this.userCodeInput
        }
        await axios.post("http://localhost:8000/compile-and-run", data)
        .then(response => {
          console.log(response.data)
          this.$emit("update-message", response.data.code)
        })
        .catch(error => {
          console.error(error)
        })
      }
    }
  }
</script>

<style scoped>
.container {
  width: 100%;
  display: flex;
  justify-content: center;
}
form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 75%;
}
form label {
  font-size: 50px;
}
form textarea{
  width: 100%;
  min-height: 100px;
  resize: vertical;
}

form button {
  margin: 20px 0;
}
</style>