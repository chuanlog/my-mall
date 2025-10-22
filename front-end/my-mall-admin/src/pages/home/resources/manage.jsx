import React, { useEffect, useState } from 'react';
import { Table, Input, Button, Space, Modal, Form, Select, Popconfirm, message, Tabs } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import { 
  listResources, createResource, updateResource, deleteResource, getResourceById,
  listAllResourceCategories, createResourceCategory, updateResourceCategory, deleteResourceCategory
} from '../../../Mock/api';
import { useLocation } from 'react-router';

function ResourceListTab() {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState([]);
  const [pageNum, setPageNum] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [total, setTotal] = useState(0);

  const [categories, setCategories] = useState([]);
  const [filters, setFilters] = useState({
    categoryId: undefined,
    nameKeyword: '',
    urlKeyword: ''
  });

  const [editOpen, setEditOpen] = useState(false);
  const [editForm] = Form.useForm();
  const [currentItem, setCurrentItem] = useState(null);

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    fetchList();
  }, [pageNum, pageSize, filters]);

  const fetchCategories = async () => {
    try {
      const res = await listAllResourceCategories();
      if (res?.code === 200) {
        setCategories(res.data || []);
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '获取资源分类失败');
    }
  };

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await listResources({
        categoryId: filters.categoryId,
        nameKeyword: filters.nameKeyword,
        urlKeyword: filters.urlKeyword,
        pageSize,
        pageNum
      });
      if (res?.code === 200 && res?.data) {
        const list = res.data.list || res.data.records || [];
        setData(list);
        setTotal(res.data.total ?? res.data.totalCount ?? list.length);
        setPageNum(res.data.pageNum ?? pageNum);
        setPageSize(res.data.pageSize ?? pageSize);
      } else {
        message.error(res?.message || '获取资源列表失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    } finally {
      setLoading(false);
    }
  };

  const openCreate = () => {
    setCurrentItem(null);
    editForm.resetFields();
    setEditOpen(true);
  };

  const openEdit = async (record) => {
    try {
      const res = await getResourceById(record.id);
      if (res?.code === 200) {
        setCurrentItem(res.data);
        editForm.setFieldsValue({
          name: res.data.name,
          url: res.data.url,
          categoryId: res.data.categoryId,
          description: res.data.description
        });
        setEditOpen(true);
      } else {
        message.error(res?.message || '获取资源详情失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const submitEdit = async () => {
    try {
      const values = await editForm.validateFields();
      let res;
      if (currentItem?.id) {
        res = await updateResource(currentItem.id, values);
      } else {
        res = await createResource(values);
      }
      if (res?.code === 200) {
        message.success(currentItem?.id ? '更新成功' : '创建成功');
        setEditOpen(false);
        fetchList();
      } else {
        message.error(res?.message || '操作失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const handleDelete = async (record) => {
    try {
      const res = await deleteResource(record.id);
      if (res?.code === 200) {
        message.success('删除成功');
        fetchList();
      } else {
        message.error(res?.message || '删除失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
    { title: '名称', dataIndex: 'name', key: 'name' },
    { title: 'URL', dataIndex: 'url', key: 'url' },
    { 
      title: '分类', dataIndex: 'categoryId', key: 'categoryId',
      render: (cid) => categories.find(c => c.id === cid)?.name || cid
    },
    { title: '描述', dataIndex: 'description', key: 'description' },
    {
      title: '操作', key: 'action', width: 220,
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => openEdit(record)}>编辑</Button>
          <Popconfirm title="确认删除该资源？" onConfirm={() => handleDelete(record)}>
            <Button danger type="link">删除</Button>
          </Popconfirm>
        </Space>
      )
    }
  ];

  return (
    <div>
      <div className="toolbar" style={{ marginBottom: 16 }}>
        <Select
          placeholder="选择分类"
          allowClear
          style={{ width: 220, marginRight: 8 }}
          value={filters.categoryId}
          onChange={(v) => setFilters(prev => ({ ...prev, categoryId: v }))}
          options={categories.map(c => ({ value: c.id, label: c.name }))}
        />
        <Input
          placeholder="名称关键字"
          value={filters.nameKeyword}
          onChange={(e) => setFilters(prev => ({ ...prev, nameKeyword: e.target.value }))}
          style={{ width: 200, marginRight: 8 }}
        />
        <Input
          placeholder="URL关键字"
          value={filters.urlKeyword}
          onChange={(e) => setFilters(prev => ({ ...prev, urlKeyword: e.target.value }))}
          style={{ width: 200, marginRight: 8 }}
        />
        <Button type="primary" icon={<SearchOutlined />} onClick={fetchList}>搜索</Button>
        <Button icon={<PlusOutlined />} style={{ marginLeft: 8 }} onClick={openCreate}>新增资源</Button>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        columns={columns}
        dataSource={data}
        pagination={{
          current: pageNum,
          pageSize,
          total,
          showSizeChanger: true,
          pageSizeOptions: ['5', '10', '20'],
          onChange: (p, s) => { setPageNum(p); setPageSize(s); }
        }}
      />

      <Modal
        title={currentItem?.id ? '编辑资源' : '新增资源'}
        open={editOpen}
        onOk={submitEdit}
        onCancel={() => setEditOpen(false)}
        okText="保存"
        cancelText="取消"
      >
        <Form form={editForm} layout="vertical">
          <Form.Item name="name" label="名称" rules={[{ required: true, message: '请输入资源名称' }]}>
            <Input placeholder="请输入资源名称" />
          </Form.Item>
          <Form.Item name="url" label="URL" rules={[{ required: true, message: '请输入资源URL' }]}>
            <Input placeholder="请输入资源URL" />
          </Form.Item>
          <Form.Item name="categoryId" label="分类" rules={[{ required: true, message: '请选择资源分类' }]}>
            <Select
              placeholder="选择分类"
              options={categories.map(c => ({ value: c.id, label: c.name }))}
            />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input placeholder="请输入描述" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

function ResourceCategoriesTab() {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState([]);

  const [editOpen, setEditOpen] = useState(false);
  const [editForm] = Form.useForm();
  const [currentItem, setCurrentItem] = useState(null);

  useEffect(() => {
    fetchList();
  }, []);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await listAllResourceCategories();
      if (res?.code === 200) {
        setData(res.data || []);
      } else {
        message.error(res?.message || '获取资源分类失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    } finally {
      setLoading(false);
    }
  };

  const openCreate = () => {
    setCurrentItem(null);
    editForm.resetFields();
    setEditOpen(true);
  };

  const openEdit = (record) => {
    setCurrentItem(record);
    editForm.setFieldsValue({
      name: record.name,
      sort: record.sort
    });
    setEditOpen(true);
  };

  const submitEdit = async () => {
    try {
      const values = await editForm.validateFields();
      let res;
      if (currentItem?.id) {
        res = await updateResourceCategory(currentItem.id, values);
      } else {
        res = await createResourceCategory(values);
      }
      if (res?.code === 200) {
        message.success(currentItem?.id ? '更新成功' : '创建成功');
        setEditOpen(false);
        fetchList();
      } else {
        message.error(res?.message || '操作失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const handleDelete = async (record) => {
    try {
      const res = await deleteResourceCategory(record.id);
      if (res?.code === 200) {
        message.success('删除成功');
        fetchList();
      } else {
        message.error(res?.message || '删除失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
    { title: '名称', dataIndex: 'name', key: 'name' },
    { title: '排序', dataIndex: 'sort', key: 'sort' },
    {
      title: '操作', key: 'action', width: 220,
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => openEdit(record)}>编辑</Button>
          <Popconfirm title="确认删除该分类？" onConfirm={() => handleDelete(record)}>
            <Button danger type="link">删除</Button>
          </Popconfirm>
        </Space>
      )
    }
  ];

  return (
    <div>
      <div className="toolbar" style={{ marginBottom: 16 }}>
        <Button icon={<PlusOutlined />} onClick={openCreate}>新增分类</Button>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        columns={columns}
        dataSource={data}
        pagination={false}
      />

      <Modal
        title={currentItem?.id ? '编辑分类' : '新增分类'}
        open={editOpen}
        onOk={submitEdit}
        onCancel={() => setEditOpen(false)}
        okText="保存"
        cancelText="取消"
      >
        <Form form={editForm} layout="vertical">
          <Form.Item name="name" label="名称" rules={[{ required: true, message: '请输入分类名称' }]}>
            <Input placeholder="请输入分类名称" />
          </Form.Item>
          <Form.Item name="sort" label="排序">
            <Input placeholder="请输入排序值（可选）" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

export default function ResourceManagePage() {
  const location = useLocation();
  const initialKey = location.pathname.includes('resource-categories') ? 'categories' : 'list';
  return (
    <Tabs defaultActiveKey={initialKey} items={[
      { key: 'list', label: '资源列表', children: <ResourceListTab /> },
      { key: 'categories', label: '资源分类', children: <ResourceCategoriesTab /> },
    ]} />
  );
}