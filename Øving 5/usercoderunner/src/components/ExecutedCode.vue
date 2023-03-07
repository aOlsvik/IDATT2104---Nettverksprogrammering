<template>
    <div class="container">
        <h2>Your code:</h2>
        <div class="compiled-code">
            <p>{{ message }}</p>
        </div>
    </div>
</template>

<script>
    export default {
        props:{
            message: {
                type:String
            }
        },
        computed: {
            displayMessage() {
                
            } 
        }

}
</script>

<style scoped>
.container {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}
.compiled-code {
    width: 75%;
    background-color: white;
}
p{
    white-space: pre-wrap;
    margin-left:5px;
    font-size: 20px;
}
</style>
  
