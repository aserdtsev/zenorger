<template>
  <div id="app">
<!--    <img alt="Vue logo" src="./assets/logo.png">-->
<!--    <HelloWorld msg="Welcome to Your Vue.js App"/>-->
    <div id="header">Header</div>
    <div id="leftSidebar">Left Sidebar</div>
    <div id="main">Content</div>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'app',
  components: {
    // HelloWorld
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
div {
  margin: 10px;
  border: 1px solid black;
  font-size: 20px;
  height: 80px;
}

#header {
  background-color: #ccc;
}

#leftSidebar {
  background-color: #ddd;
  float: left;
}

#main {
  background-color: #eee;
  height: 200px;
  margin-left: 170px;
}

</style>
