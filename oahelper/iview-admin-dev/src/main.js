import Vue from 'vue';
import iView from 'iview';
import {router} from './router/index';
import {appRouter} from './router/router';
import store from './store';
import App from './app.vue';
import '@/locale';
import 'iview/dist/styles/iview.css';
import VueI18n from 'vue-i18n';
import util from './libs/util';
import axios from 'axios'
import Qs from 'qs'

Vue.prototype.$axiosInstance = axios.create({
  transformRequest: [function (data) {
    data = Qs.stringify(data,{arrayFormat: 'brackets'});
    return data
  }],
  headers: { 'Content-Type': 'application/x-www-form-urlencoded'},
  timeout:30000
});


// // 拦截响应response，并做一些错误处理
Vue.prototype.$axiosInstance.interceptors.response.use(function (response) {
  const data = response.data
  if(data.httpCode===401){
    //未登录重定向 删除所有cookie
    var keys=document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
      for (var i = keys.length; i--;)
        document.cookie=keys[i]+'=0;expires=' + new Date( 0).toUTCString()
    }
    window.location.href="/";
  }else if(data.httpCode===200){
    return response;
  }
  Vue.prototype.$Notice.error({
    desc: data.msg,
  });

}, (err) => { // 这里是返回状态码不为200时候的错误处理
  Vue.prototype.$Notice.error({
    title: '服务器调用接口错误，返回状态不正常',
  });
  return Promise.reject(err);
});

Vue.prototype.$uploadFileRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}


//
Vue.use(VueI18n);
Vue.use(iView);

let vmapp = new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App),
    data: {
        currentPageName: ''
    },
    mounted () {
        this.currentPageName = this.$route.name;
        // 显示打开的页面的列表
        this.$store.commit('setOpenedList');
        this.$store.commit('initCachepage');
        // 权限菜单过滤相关
        this.$store.commit('updateMenulist');
        // iview-admin检查更新
        //util.checkUpdate(this);
    },
    created () {
        let tagsList = [];
        appRouter.map((item) => {
            if (item.children.length <= 1) {
                tagsList.push(item.children[0]);
            } else {
                tagsList.push(...item.children);
            }
        });
        this.$store.commit('setTagsList', tagsList);
    }
});
export default vmapp;