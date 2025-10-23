import http from './http';

// 资源分类：查询全部
export const listAllResourceCategories = async () => {
  try {
    const response = await http.get('/resourceCategory/listAll');
    return response.data;
  } catch (error) {
    console.error('获取全部资源分类失败:', error);
    throw error;
  }
};

// 资源分类：创建
export const createResourceCategory = async (category) => {
  try {
    const response = await http.post('/resourceCategory/create', category);
    return response.data;
  } catch (error) {
    console.error('创建资源分类失败:', error);
    throw error;
  }
};

// 资源分类：更新
export const updateResourceCategory = async (id, category) => {
  try {
    const response = await http.post(`/resourceCategory/update/${id}`, category);
    return response.data;
  } catch (error) {
    console.error('更新资源分类失败:', error);
    throw error;
  }
};

// 资源分类：删除
export const deleteResourceCategory = async (id) => {
  try {
    const response = await http.post(`/resourceCategory/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error('删除资源分类失败:', error);
    throw error;
  }
};