import http, { serializeParams } from './http';

// 用户列表（分页、搜索）
export const listAdmins = async ({ keyword = '', pageSize = 5, pageNum = 1 }) => {
  try {
    const response = await http.get('/admin/list', { params: { keyword, pageSize, pageNum } });
    return response.data;
  } catch (error) {
    console.error('获取用户列表失败:', error);
    throw error;
  }
};

// 获取指定用户信息
export const getAdminById = async (id) => {
  try {
    const response = await http.get(`/admin/${id}`);
    return response.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
};

// 修改指定用户信息
export const updateAdmin = async (id, admin) => {
  try {
    const response = await http.post(`/admin/update/${id}`, admin);
    return response.data;
  } catch (error) {
    console.error('更新用户失败:', error);
    throw error;
  }
};

// 删除指定用户
export const deleteAdmin = async (id) => {
  try {
    const response = await http.post(`/admin/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error('删除用户失败:', error);
    throw error;
  }
};

// 修改帐号状态
export const updateAdminStatus = async (id, status) => {
  try {
    const response = await http.post(`/admin/updateStatus/${id}`, null, { params: { status } });
    return response.data;
  } catch (error) {
    console.error('更新用户状态失败:', error);
    throw error;
  }
};

// 给用户分配角色（数组序列化）
export const updateAdminRole = async (adminId, roleIds) => {
  try {
    const response = await http.post('/admin/role/update', null, {
      params: { adminId, roleIds },
      paramsSerializer: serializeParams
    });
    return response.data;
  } catch (error) {
    console.error('分配角色失败:', error);
    throw error;
  }
};

// 获取指定用户的角色
export const getAdminRoles = async (adminId) => {
  try {
    const response = await http.get(`/admin/role/${adminId}`);
    return response.data;
  } catch (error) {
    console.error('获取用户角色失败:', error);
    throw error;
  }
};

// 上传当前登录用户头像
export const uploadAvatarForCurrentUser = async (file) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    const response = await http.post('/admin/avatar/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  } catch (error) {
    console.error('上传当前用户头像失败:', error);
    throw error;
  }
};

// 上传指定用户头像
export const uploadAvatarForAdmin = async (adminId, file) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    const response = await http.post(`/admin/avatar/upload/${adminId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  } catch (error) {
    console.error('上传指定用户头像失败:', error);
    throw error;
  }
};

// 更新指定用户头像URL
export const updateAvatarUrlForAdmin = async (adminId, avatarUrl) => {
  try {
    const response = await http.post(`/admin/avatar/update/${adminId}`, null, {
      params: { avatarUrl }
    });
    return response.data;
  } catch (error) {
    console.error('更新指定用户头像URL失败:', error);
    throw error;
  }
};