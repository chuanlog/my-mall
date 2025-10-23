import http, { serializeParams } from './http';

// 分类：分页查询
export const listProductCategories = async ({ keyword, pageSize = 5, pageNum = 1 }) => {
  const response = await http.get('/pms/productCategory/list', {
    params: { keyword, pageSize, pageNum },
  });
  return response.data;
};

// 分类：查询所有（给商品页下拉）
export const listAllProductCategories = async () => {
  const response = await http.get('/pms/productCategory/listAll');
  return response.data;
};

// 分类：查询详情
export const getProductCategoryById = async (id) => {
  const response = await http.get(`/pms/productCategory/item/${id}`);
  return response.data;
};

// 分类：创建
export const createProductCategory = async (category) => {
  const response = await http.post('/pms/productCategory/create', category);
  return response.data;
};

// 分类：更新
export const updateProductCategory = async (id, category) => {
  const response = await http.post(`/pms/productCategory/update/${id}`, category);
  return response.data;
};

// 分类：批量删除（数组友好）
export const deleteProductCategories = async (ids) => {
  const qs = serializeParams({ ids });
  const response = await http.post(`/pms/productCategory/delete?${qs}`);
  return response.data;
};

// 分类：更新状态
export const updateProductCategoryStatus = async (id, status) => {
  const response = await http.post(`/pms/productCategory/updateStatus/${id}`, null, {
    params: { status },
  });
  return response.data;
};

// 分类：上传图片
export const uploadProductCategoryImage = async (id, file) => {
  const formData = new FormData();
  formData.append('file', file);
  const response = await http.post(`/pms/productCategory/image/upload/${id}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

// 分类：更新图片
export const updateProductCategoryImage = async (id, imageUrl) => {
  const response = await http.post(`/pms/productCategory/image/update/${id}`, null, {
    params: { imageUrl },
  });
  return response.data;
};