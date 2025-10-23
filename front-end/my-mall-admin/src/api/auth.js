import http from './http';

// 用户登录 - 调用UMS后端接口
export const accountLogin = async (username, password) => {
  try {
    const response = await http.post('/admin/login', { username, password });
    return response.data;
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
};

// 用户注册 - 调用UMS后端接口
export const accountRegister = async (username, password) => {
  try {
    const response = await http.post('/admin/register', {
      username,
      password,
      email: `${username}@example.com`,
      nickName: username,
      status: 1
    });
    return response.data;
  } catch (error) {
    console.error('注册失败:', error);
    throw error;
  }
};

// 获取当前用户信息
export const getCurrentUserInfo = async () => {
  try {
    const response = await http.get('/admin/info');
    return response.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
};

// 用户登出
export const logout = async () => {
  try {
    const response = await http.post('/admin/logout');
    return response.data;
  } catch (error) {
    console.error('登出失败:', error);
    throw error;
  }
};