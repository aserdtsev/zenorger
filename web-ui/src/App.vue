<template>
    <div id="app" class="container">
        <!--    <img alt="Vue logo" src="./assets/logo.png">-->
        <!--    <HelloWorld msg="Welcome to Your Vue.js App"/>-->
        <div class="row text-center">
            <div id="header" class="col-sm-12">Zenorger</div>
        </div>
        <div class="row">
            <div id="nav" class="col-sm-4">
                <div>Inbox</div>
                <div>Contexts</div>
                <ul>
                    <li>Home</li>
                    <li>Work</li>
                </ul>
            </div>
            <div id="main" class="col-sm-8">{{ info }}</div>
        </div>
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
