import http from './http';

// 资源：分页模糊查询
export const listResources = async ({ categoryId, nameKeyword, urlKeyword, pageSize = 5, pageNum = 1 }) => {
  try {
    const response = await http.get('/resource/list', {
      params: { categoryId, nameKeyword, urlKeyword, pageSize, pageNum }
    });
    return response.data;
  } catch (error) {
    console.error('查询资源列表失败:', error);
    throw error;
  }
};

// 资源：创建
export const createResource = async (resource) => {
  try {
    const response = await http.post('/resource/create', resource);
    return response.data;
  } catch (error) {
    console.error('创建资源失败:', error);
    throw error;
  }
};

// 资源：更新
export const updateResource = async (id, resource) => {
  try {
    const response = await http.post(`/resource/update/${id}`, resource);
    return response.data;
  } catch (error) {
    console.error('更新资源失败:', error);
    throw error;
  }
};

// 资源：按ID获取
export const getResourceById = async (id) => {
  try {
    const response = await http.get(`/resource/${id}`);
    return response.data;
  } catch (error) {
    console.error('获取资源详情失败:', error);
    throw error;
  }
};

// 资源：删除
export const deleteResource = async (id) => {
  try {
    const response = await http.post(`/resource/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error('删除资源失败:', error);
    throw error;
  }
};

// 资源：查询全部（可用于下拉等）
export const listAllResources = async () => {
  try {
    const response = await http.get('/resource/listAll');
    return response.data;
  } catch (error) {
    console.error('获取全部资源失败:', error);
    throw error;
  }
};