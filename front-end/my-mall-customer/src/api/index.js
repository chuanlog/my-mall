import axios from 'axios';

// 设置基础URL
axios.defaults.baseURL = 'http://localhost:8080';

// 请求拦截器
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    const tokenHead = localStorage.getItem('tokenHead');
    
    if (token && tokenHead) {
      config.headers.Authorization = `${tokenHead} ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      // token过期或无效，清除本地存储并跳转到登录页
      localStorage.removeItem('token');
      localStorage.removeItem('tokenHead');
      localStorage.removeItem('username');
      window.location.href = '/user';
    }
    return Promise.reject(error);
  }
);

export default axios;