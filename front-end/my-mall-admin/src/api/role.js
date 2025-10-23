import http, { serializeParams } from './http';

// 角色：创建
export const createRole = async (role) => {
  try {
    const response = await http.post('/role/create', role);
    return response.data;
  } catch (error) {
    console.error('创建角色失败:', error);
    throw error;
  }
};

// 角色：更新
export const updateRole = async (id, role) => {
  try {
    const response = await http.post(`/role/update/${id}`, role);
    return response.data;
  } catch (error) {
    console.error('更新角色失败:', error);
    throw error;
  }
};

// 角色：批量删除（支持传单个ID）
export const deleteRoles = async (ids) => {
  try {
    const response = await http.post('/role/delete', null, { params: { ids } });
    return response.data;
  } catch (error) {
    console.error('删除角色失败:', error);
    throw error;
  }
};

// 角色：分页列表（模糊查询）
export const listRoles = async ({ keyword = '', pageSize = 5, pageNum = 1 }) => {
  try {
    const response = await http.get('/role/list', { params: { keyword, pageSize, pageNum } });
    return response.data;
  } catch (error) {
    console.error('查询角色列表失败:', error);
    throw error;
  }
};

// 角色：更新状态
export const updateRoleStatus = async (id, status) => {
  try {
    const response = await http.post(`/role/updateStatus/${id}`, null, { params: { status } });
    return response.data;
  } catch (error) {
    console.error('更新角色状态失败:', error);
    throw error;
  }
};

// 角色：查询关联菜单
export const listRoleMenus = async (roleId) => {
  try {
    const response = await http.get(`/role/listMenu/${roleId}`);
    return response.data;
  } catch (error) {
    console.error('查询角色关联菜单失败:', error);
    throw error;
  }
};

// 角色：查询关联资源
export const listRoleResources = async (roleId) => {
  try {
    const response = await http.get(`/role/listResource/${roleId}`);
    return response.data;
  } catch (error) {
    console.error('查询角色关联资源失败:', error);
    throw error;
  }
};

// 角色：分配菜单（数组序列化）
export const allocRoleMenus = async (roleId, menuIds) => {
  try {
    const response = await http.post('/role/allocMenu', null, {
      params: { roleId, menuIds },
      paramsSerializer: serializeParams
    });
    return response.data;
  } catch (error) {
    console.error('分配角色菜单失败:', error);
    throw error;
  }
};

// 角色：分配资源（数组序列化）
export const allocRoleResources = async (roleId, resourceIds) => {
  try {
    const response = await http.post('/role/allocResource', null, {
      params: { roleId, resourceIds },
      paramsSerializer: serializeParams
    });
    return response.data;
  } catch (error) {
    console.error('分配角色资源失败:', error);
    throw error;
  }
};

// 获取全部角色（用于分配）
export const listAllRoles = async () => {
  try {
    const response = await http.get('/role/listAll');
    return response.data;
  } catch (error) {
    console.error('获取全部角色失败:', error);
    throw error;
  }
};