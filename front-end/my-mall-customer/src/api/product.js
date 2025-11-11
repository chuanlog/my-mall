import axios from 'axios';

// 获取商品列表（分页 + 过滤）
export const fetchProducts = async ({
  keyword,
  categoryId,
  status,
  minPrice,
  maxPrice,
  pageSize = 12,
  pageNum = 1
} = {}) => {
  const response = await axios.get('/pms/product/list', {
    params: { keyword, categoryId, status, minPrice, maxPrice, pageSize, pageNum }
  });
  return response.data; // CommonResult<CommonPage<PmsProduct>>
};

// 获取单个商品详情
export const fetchProduct = async (id) => {
  const response = await axios.get(`/pms/product/item/${id}`);
  return response.data; // CommonResult<PmsProduct>
};

// 获取所有分类
export const fetchCategories = async () => {
  const response = await axios.get('/pms/productCategory/listAll');
  return response.data; // CommonResult<List<PmsProductCategory>>
};

// 添加到购物车（预留入口，默认数量1）
export const addToCart = async (productId, quantity = 1) => {
  const response = await axios.post('/oms/omsShoppingCart/add', {
    productId,
    quantity
  });
  return response.data; // CommonResult<Void>
};