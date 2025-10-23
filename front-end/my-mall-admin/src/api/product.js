import http, { serializeParams } from './http';

// 商品：分页查询
export const listProducts = async ({ keyword, categoryId, status, minPrice, maxPrice, pageSize = 5, pageNum = 1 }) => {
  const response = await http.get('/pms/product/list', {
    params: { keyword, categoryId, status, minPrice, maxPrice, pageSize, pageNum },
  });
  return response.data;
};

// 商品：查询详情
export const getProductById = async (id) => {
  const response = await http.get(`/pms/product/item/${id}`);
  return response.data;
};

// 商品：创建
export const createProduct = async (product) => {
  const response = await http.post('/pms/product/create', product);
  return response.data;
};

// 商品：更新
export const updateProduct = async (id, product) => {
  const response = await http.post(`/pms/product/update/${id}`, product);
  return response.data;
};

// 商品：批量删除（数组友好）
export const deleteProducts = async (ids) => {
  const qs = serializeParams({ ids });
  const response = await http.post(`/pms/product/delete?${qs}`);
  return response.data;
};

// 商品：更新状态
export const updateProductStatus = async (id, status) => {
  const response = await http.post(`/pms/product/updateStatus/${id}`, null, {
    params: { status },
  });
  return response.data;
};

// 商品：上传图片
export const uploadProductImage = async (id, file) => {
  const formData = new FormData();
  formData.append('file', file);
  const response = await http.post(`/pms/product/image/upload/${id}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

// 商品：更新图片
export const updateProductImage = async (id, imageUrl) => {
  const response = await http.post(`/pms/product/image/update/${id}`, null, {
    params: { imageUrl },
  });
  return response.data;
};

// 分类：用于商品页筛选
export const listAllProductCategories = async () => {
  const response = await http.get('/pms/productCategory/listAll');
  return response.data;
};