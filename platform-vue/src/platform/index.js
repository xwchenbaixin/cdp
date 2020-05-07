// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui'
import store from './store'
import router from './router'

import './icons'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)


export default {
  router: router.routes,
  store: store,
  locale: []
}
