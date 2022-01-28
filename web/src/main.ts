import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
import { message } from 'ant-design-vue';

axios.defaults.baseURL = process.env.VUE_APP_SERVER;
console.log('start:' +process.env.VUE_APP_SERVER );
console.log('環境：', process.env.NODE_ENV);
console.log('服務端：', process.env.VUE_APP_SERVER);
/**
 * axios攔截器
 */
axios.interceptors.request.use(function (config) {
  console.log('請求參數：', config);
  const token = store.state.user.token;
  if (Tool.isNotEmpty(token)) {
    config.headers.token = token;
    console.log("請求header增加token:", token);
  }
  return config;
}, error => {
  return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
  console.log('返回結果：', response);
  return response;
}, error => {
  console.log('返回錯誤：', error);
  const response = error.response;
  const status = response.status;
  if (status === 401) {
    console.log("未登入，跳到首頁");
    store.commit("setUser", {});
    message.error("未登入或登入超時");
    router.push('/');
  }
  return Promise.reject(error);
});

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');


const icons: any = Icons;
for (const i in icons) {
  app.component(i, icons[i]);
}
console.log('end');

