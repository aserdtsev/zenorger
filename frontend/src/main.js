import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import App from './App.vue'

Vue.use(VueAxios, axios);
Vue.config.productionTip = false;

new Vue({
  render: h => h(App)
}).$mount('#app');

export function jsonCopy(src) {
  return JSON.parse(JSON.stringify(src));
}

export function equals(left, right) {
  return JSON.stringify(left) === JSON.stringify(right);
}

export function createUuid() {
  return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
      (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
  )
}

