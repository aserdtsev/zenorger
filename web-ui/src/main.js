import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'

import App from './App.vue'

Vue.use(BootstrapVue)
Vue.config.productionTip = false

const axios = require('axios');

new Vue({
  render: h => h(App),
  data() {
    return {
      info: null
    };
  },
  mounted() {
    axios
        .get('https://api.coindesk.com/v1/bpi/currentprice.json')
        .then(response => (this.info = response));
  }
}).$mount('#app')
