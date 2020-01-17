import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Login from '@/views/login'
import Success from '@/views/success'
import Error from '@/views/error'

Vue.use(Router)

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),

  routes: [

    // {
    //   path: '/',
    //   name: 'HelloWorld',
    //   component: HelloWorld
    // },
    //配置默认的路径，默认显示登录页
    {
      path: '/',
      name:'login',
      component: Login
    },
    //配置登录成功页面，使用时需要使用 path 路径来实现跳转
    {
      path: '/success',
      component: Success
    },

    //配置登录失败页面，使用时需要使用 path 路径来实现跳转
    {
      path: '/error',
      component: Error
    }

  ]
})
