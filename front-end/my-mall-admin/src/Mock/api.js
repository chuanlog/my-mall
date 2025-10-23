import axios from 'axios';

// 配置axios基础URL
const API_BASE_URL = 'http://localhost:8080';
axios.defaults.baseURL = API_BASE_URL;

//用户登录 - 调用UMS后端接口
export const accountLogin = async (username, password) => {
    try {
        console.log('登录中:', username);
        const response = await axios.post('/admin/login', {
            username: username,
            password: password
        });
        console.log('登录响应:', response.data);
        return response.data;
    } catch (error) {
        console.error('登录失败:', error);
        throw error;
    }
}

//用户注册 - 调用UMS后端接口
export const accountRegister = async (username, password) => {
    try {
        console.log('注册中:', username);
        const response = await axios.post('/admin/register', {
            username: username,
            password: password,
            email: `${username}@example.com`, // 临时邮箱
            nickName: username,
            status: 1
        });
        console.log('注册响应:', response.data);
        return response.data;
    } catch (error) {
        console.error('注册失败:', error);
        throw error;
    }
}

//获取当前用户信息
export const getCurrentUserInfo = async () => {
    try {
        const response = await axios.get('/admin/info');
        console.log('用户信息:', response.data);
        return response.data;
    } catch (error) {
        console.error('获取用户信息失败:', error);
        throw error;
    }
}

//用户登出
export const logout = async () => {
    try {
        const response = await axios.post('/admin/logout');
        console.log('登出响应:', response.data);
        return response.data;
    } catch (error) {
        console.error('登出失败:', error);
        throw error;
    }
}

// 用户列表（分页、搜索）
export const listAdmins = async ({ keyword = '', pageSize = 5, pageNum = 1 }) => {
  try {
    const response = await axios.get('/admin/list', {
      params: { keyword, pageSize, pageNum }
    });
    return response.data;
  } catch (error) {
    console.error('获取用户列表失败:', error);
    throw error;
  }
}

// 获取指定用户信息
export const getAdminById = async (id) => {
  try {
    const response = await axios.get(`/admin/${id}`);
    return response.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
}

// 修改指定用户信息
export const updateAdmin = async (id, admin) => {
  try {
    const response = await axios.post(`/admin/update/${id}`, admin);
    return response.data;
  } catch (error) {
    console.error('更新用户失败:', error);
    throw error;
  }
}

// 删除指定用户
export const deleteAdmin = async (id) => {
  try {
    const response = await axios.post(`/admin/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error('删除用户失败:', error);
    throw error;
  }
}

// 修改帐号状态
export const updateAdminStatus = async (id, status) => {
  try {
    const response = await axios.post(`/admin/updateStatus/${id}`, null, {
      params: { status }
    });
    return response.data;
  } catch (error) {
    console.error('更新用户状态失败:', error);
    throw error;
  }
}

// 给用户分配角色
export const updateAdminRole = async (adminId, roleIds) => {
  try {
    const response = await axios.post(`/admin/role/update`, null, {
      params: { adminId, roleIds },
      paramsSerializer: params => {
        const usp = new URLSearchParams();
        Object.entries(params).forEach(([key, value]) => {
          if (Array.isArray(value)) {
            value.forEach(v => usp.append(key, v));
          } else if (value !== undefined && value !== null) {
            usp.append(key, value);
          }
        });
        return usp.toString();
      }
    });
    return response.data;
  } catch (error) {
    console.error('分配角色失败:', error);
    throw error;
  }
}

// 获取指定用户的角色
export const getAdminRoles = async (adminId) => {
  try {
    const response = await axios.get(`/admin/role/${adminId}`);
    return response.data;
  } catch (error) {
    console.error('获取用户角色失败:', error);
    throw error;
  }
}

// 获取全部角色（用于分配）
export const listAllRoles = async () => {
  try {
    const response = await axios.get('/role/listAll');
    return response.data;
  } catch (error) {
    console.error('获取全部角色失败:', error);
    throw error;
  }
}


// 上传当前登录用户头像
export const uploadAvatarForCurrentUser = async (file) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axios.post('/admin/avatar/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  } catch (error) {
    console.error('上传当前用户头像失败:', error);
    throw error;
  }
}

// 上传指定用户头像
export const uploadAvatarForAdmin = async (adminId, file) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axios.post(`/admin/avatar/upload/${adminId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  } catch (error) {
    console.error('上传指定用户头像失败:', error);
    throw error;
  }
}

// 更新指定用户头像URL
export const updateAvatarUrlForAdmin = async (adminId, avatarUrl) => {
  try {
    const response = await axios.post(`/admin/avatar/update/${adminId}`, null, {
      params: { avatarUrl }
    });
    return response.data;
  } catch (error) {
    console.error('更新指定用户头像URL失败:', error);
    throw error;
  }
}


// 资源分类：查询全部
export const listAllResourceCategories = async () => {
  try {
    const response = await axios.get('/resourceCategory/listAll');
    return response.data;
  } catch (error) {
    console.error('获取全部资源分类失败:', error);
    throw error;
  }
}

// 资源分类：创建
export const createResourceCategory = async (category) => {
  try {
    const response = await axios.post('/resourceCategory/create', category);
    return response.data;
  } catch (error) {
    console.error('创建资源分类失败:', error);
    throw error;
  }
}

// 资源分类：更新
export const updateResourceCategory = async (id, category) => {
  try {
    const response = await axios.post(`/resourceCategory/update/${id}`, category);
    return response.data;
  } catch (error) {
    console.error('更新资源分类失败:', error);
    throw error;
  }
}

// 资源分类：删除
export const deleteResourceCategory = async (id) => {
  try {
    const response = await axios.post(`/resourceCategory/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error('删除资源分类失败:', error);
    throw error;
  }
}

// 资源：分页模糊查询
export const listResources = async ({ categoryId, nameKeyword, urlKeyword, pageSize = 5, pageNum = 1 }) => {
  try {
    const response = await axios.get('/resource/list', {
      params: { categoryId, nameKeyword, urlKeyword, pageSize, pageNum }
    });
    return response.data;
  } catch (error) {
    console.error('查询资源列表失败:', error);
    throw error;
  }
}

// 资源：创建
export const createResource = async (resource) => {
  try {
    const response = await axios.post('/resource/create', resource);
    return response.data;
  } catch (error) {
    console.error('创建资源失败:', error);
    throw error;
  }
}

// 资源：更新
export const updateResource = async (id, resource) => {
  try {
    const response = await axios.post(`/resource/update/${id}`, resource);
    return response.data;
  } catch (error) {
    console.error('更新资源失败:', error);
    throw error;
  }
}

// 资源：按ID获取
export const getResourceById = async (id) => {
  try {
    const response = await axios.get(`/resource/${id}`);
    return response.data;
  } catch (error) {
    console.error('获取资源详情失败:', error);
    throw error;
  }
}

// 资源：删除
export const deleteResource = async (id) => {
  try {
    const response = await axios.post(`/resource/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error('删除资源失败:', error);
    throw error;
  }
}

// 资源：查询全部（可用于下拉等）
export const listAllResources = async () => {
  try {
    const response = await axios.get('/resource/listAll');
    return response.data;
  } catch (error) {
    console.error('获取全部资源失败:', error);
    throw error;
  }
}


// 角色：创建
export const createRole = async (role) => {
  try {
    const response = await axios.post('/role/create', role);
    return response.data;
  } catch (error) {
    console.error('创建角色失败:', error);
    throw error;
  }
}

// 角色：更新
export const updateRole = async (id, role) => {
  try {
    const response = await axios.post(`/role/update/${id}`, role);
    return response.data;
  } catch (error) {
    console.error('更新角色失败:', error);
    throw error;
  }
}

// 角色：批量删除（支持传单个ID）
export const deleteRoles = async (ids) => {
  try {
    const response = await axios.post('/role/delete', null, {
      params: { ids }
    });
    return response.data;
  } catch (error) {
    console.error('删除角色失败:', error);
    throw error;
  }
}

// 角色：分页列表（模糊查询）
export const listRoles = async ({ keyword = '', pageSize = 5, pageNum = 1 }) => {
  try {
    const response = await axios.get('/role/list', { params: { keyword, pageSize, pageNum } });
    return response.data;
  } catch (error) {
    console.error('查询角色列表失败:', error);
    throw error;
  }
}

// 角色：更新状态
export const updateRoleStatus = async (id, status) => {
  try {
    const response = await axios.post(`/role/updateStatus/${id}`, null, { params: { status } });
    return response.data;
  } catch (error) {
    console.error('更新角色状态失败:', error);
    throw error;
  }
}

// 角色：查询关联菜单
export const listRoleMenus = async (roleId) => {
  try {
    const response = await axios.get(`/role/listMenu/${roleId}`);
    return response.data;
  } catch (error) {
    console.error('查询角色关联菜单失败:', error);
    throw error;
  }
}

// 角色：查询关联资源
export const listRoleResources = async (roleId) => {
  try {
    const response = await axios.get(`/role/listResource/${roleId}`);
    return response.data;
  } catch (error) {
    console.error('查询角色关联资源失败:', error);
    throw error;
  }
}

// 角色：分配菜单
export const allocRoleMenus = async (roleId, menuIds) => {
  try {
    const response = await axios.post('/role/allocMenu', null, {
      params: { roleId, menuIds },
      paramsSerializer: params => {
        const usp = new URLSearchParams();
        Object.entries(params).forEach(([key, value]) => {
          if (Array.isArray(value)) {
            value.forEach(v => usp.append(key, v));
          } else if (value !== undefined && value !== null) {
            usp.append(key, value);
          }
        });
        return usp.toString();
      }
    });
    return response.data;
  } catch (error) {
    console.error('分配角色菜单失败:', error);
    throw error;
  }
}

// 角色：分配资源
export const allocRoleResources = async (roleId, resourceIds) => {
  try {
    const response = await axios.post('/role/allocResource', null, {
      params: { roleId, resourceIds },
      paramsSerializer: params => {
        const usp = new URLSearchParams();
        Object.entries(params).forEach(([key, value]) => {
          if (Array.isArray(value)) {
            value.forEach(v => usp.append(key, v));
          } else if (value !== undefined && value !== null) {
            usp.append(key, value);
          }
        });
        return usp.toString();
      }
    });
    return response.data;
  } catch (error) {
    console.error('分配角色资源失败:', error);
    throw error;
  }
}