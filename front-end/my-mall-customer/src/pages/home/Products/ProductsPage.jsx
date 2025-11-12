import React, { useEffect, useState } from 'react';
import { Row, Col, Card, Input, Select, Pagination, Tag, Button, Empty, message, Spin } from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';
import { fetchProducts, fetchCategories, addToCart } from '../../../api/product';
import './ProductsPage.css';

const { Meta } = Card;
const { Search } = Input;
const { Option } = Select;

export default function ProductsPage() {
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [products, setProducts] = useState([]);
  const [total, setTotal] = useState(0);
  const [filters, setFilters] = useState({
    keyword: '',
    categoryId: undefined,
    pageSize: 12,
    pageNum: 1,
    status: 1
  });

  useEffect(() => {
    const loadCategories = async () => {
      try {
        const res = await fetchCategories();
        if (res.code === 200) {
          setCategories(res.data || []);
        } else {
          message.error(res.message || '获取分类失败');
        }
      } catch (e) {
        message.error('获取分类失败', e);
      }
    };
    loadCategories();
  }, []);

  useEffect(() => {
    const loadProducts = async () => {
      setLoading(true);
      try {
        const res = await fetchProducts(filters);
        if (res.code === 200) {
          const page = res.data || {};
          setProducts(page.list || []);
          setTotal(page.total || 0);
        } else {
          message.error(res.message || '获取商品列表失败');
        }
      } catch (e) {
        message.error('获取商品列表失败', e);
      } finally {
        setLoading(false);
      }
    };
    loadProducts();
  }, [filters]);

  const handleSearch = (value) => {
    setFilters(prev => ({ ...prev, keyword: value, pageNum: 1 }));
  };

  const handleCategoryChange = (value) => {
    setFilters(prev => ({ ...prev, categoryId: value || undefined, pageNum: 1 }));
  };

  const handlePageChange = (page, pageSize) => {
    setFilters(prev => ({ ...prev, pageNum: page, pageSize }));
  };

  const handleAddToCart = async (productId) => {
    try {
      const res = await addToCart(productId, 1);
      if (res.code === 200) {
        message.success('已添加到购物车');
      } else {
        message.error(res.message || '添加到购物车失败');
      }
    } catch (e) {
      message.error('添加到购物车失败', e);
    }
  };

  const renderTags = (tagsStr) => {
    if (!tagsStr) return null;
    try {
      const arr = JSON.parse(tagsStr);
      if (Array.isArray(arr)) {
        return arr.map((t, idx) => <Tag key={idx} color="blue">{t}</Tag>);
      }
      return null;
    } catch {
      return null;
    }
  };

  return (
    <div className="products-page">
      <div className="filters-bar">
        <Search
          placeholder="搜索商品名称"
          allowClear
          enterButton="搜索"
          onSearch={handleSearch}
          className="filter-search"
        />
        <Select
          allowClear
          placeholder="选择分类"
          className="filter-select"
          onChange={handleCategoryChange}
          value={filters.categoryId}
        >
          {categories.map(cat => (
            <Option key={cat.id} value={cat.id}>{cat.name}</Option>
          ))}
        </Select>
      </div>

      {loading ? (
        <div className="loading-wrap"><Spin /></div>
      ) : products.length === 0 ? (
        <Empty description="暂无商品" />
      ) : (
        <Row gutter={[16, 16]}>
          {products.map(p => (
            <Col xs={24} sm={12} md={8} lg={6} key={p.id}>
              <Card
                hoverable
                cover={
                  p.image ? (
                    <img alt={p.name} src={p.image} className="product-image" />
                  ) : (
                    <div className="product-image placeholder">暂无图片</div>
                  )
                }
                actions={[
                  <Button
                    type="primary"
                    icon={<ShoppingCartOutlined />}
                    onClick={() => handleAddToCart(p.id)}
                  >
                    添加到购物车
                  </Button>
                ]}
              >
                <Meta
                  title={p.name}
                  description={
                    <div className="product-meta">
                      <div className="product-price">￥{p.price}</div>
                      <div className="product-tags">{renderTags(p.tags)}</div>
                      <div className="product-desc">{p.description}</div>
                    </div>
                  }
                />
              </Card>
            </Col>
          ))}
        </Row>
      )}

      <div className="pagination-bar">
        <Pagination
          current={filters.pageNum}
          pageSize={filters.pageSize}
          total={Number(total) || 0}
          onChange={handlePageChange}
          showSizeChanger
          showTotal={(t) => `共 ${t} 条`}
        />
      </div>
    </div>
  );
}