import axios from 'axios';

// 用户登录
export const userLogin = async (loginData) => {
  try {
    console.log('用户登录中...', loginData);
    const response = await axios.post('/customer/login', loginData);
    console.log('登录响应:', response.data);
    return response.data;
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
};

// 用户注册
export const userRegister = async (registerData) => {
  try {
    console.log('用户注册中...', registerData);
    const response = await axios.post('/customer/register', registerData);
    console.log('注册响应:', response.data);
    return response.data;
  } catch (error) {
    console.error('注册失败:', error);
    throw error;
  }
};

// 刷新token
export const refreshToken = async () => {
  try {
    const response = await axios.get('/customer/refreshToken');
    console.log('刷新token响应:', response.data);
    return response.data;
  } catch (error) {
    console.error('刷新token失败:', error);
    throw error;
  }
};

// 获取当前用户信息
export const getUserInfo = async () => {
  try {
    const response = await axios.get('/customer/info');
    console.log('获取用户信息响应:', response.data);
    return response.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
};

// 用户登出
export const userLogout = async () => {
  try {
    const response = await axios.post('/customer/logout');
    console.log('登出响应:', response.data);
    
    // 清除本地存储
    localStorage.removeItem("token");
    localStorage.removeItem("tokenHead");
    localStorage.removeItem("username");
    
    return response.data;
  } catch (error) {
    console.error('登出失败:', error);
    // 即使API调用失败，也清除本地存储
    localStorage.removeItem("token");
    localStorage.removeItem("tokenHead");
    localStorage.removeItem("username");
    throw error;
  }
};