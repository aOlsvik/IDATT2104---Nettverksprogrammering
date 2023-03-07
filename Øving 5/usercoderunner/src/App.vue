<template>
  <body>
    <h1>Online Code Compiler and Runner</h1>
    <UserCodeInput @update-message="message = $event" />
    <hr>
    <ExecutedCode :message="message"/>
  </body>
</template>

<script>
import ExecutedCode from './components/ExecutedCode.vue';
import UserCodeInput from './components/UserCodeInput.vue';

export default {
  name: 'App',
  components: {
    UserCodeInput,
    ExecutedCode
  },
  data() {
    return {
      message: "Output will appear here"
    }
  }
}
</script>

<style scoped>
body{
  margin: 0;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
h1{
  font-size: 80px;
  font-weight: bolder;
  color: rgb(24, 136, 24);
}
hr{
  width: 100%;
}
</style>


