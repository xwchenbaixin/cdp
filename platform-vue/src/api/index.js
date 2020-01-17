/**
 * api接口统一管理
 */

import { get, post } from './http'
//现在，例如我们有这样一个接口，是一个post请求：
//http://www.baiodu.com/api/v1/users/my_address/address_edit_before
//我们可以在api.js中这样封装：
//export const getUserName = data => post('api/v1/users/my_address/address_edit_before', data);
//类似于
/*
  export function getUserName(data){
    return post('api/v1/users/my_address/address_edit_before', data);
  }
 */

// 我们定义了一个apiAddress方法，这个方法有一个参数p，p是我们请求接口时携带的参数对象。而后调用了我们封装的post方法，
// post方法的第一个参数是我们的接口地址，第二个参数是apiAddress的p参数，即请求接口时携带的参数对象。
// 最后通过export导出apiAddress。
// 然后在我们的页面中可以这样调用我们的api接口：
export const getUserName = data => post('/test/getUserName', data);
