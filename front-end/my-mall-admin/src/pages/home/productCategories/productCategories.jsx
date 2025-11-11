import React, { useEffect, useState } from 'react';
import { Table, Input, Button, Space, Modal, Form, Select, Popconfirm, message, InputNumber, Switch, Upload } from 'antd';
import { SearchOutlined, PlusOutlined, UploadOutlined } from '@ant-design/icons';
import {
  listProductCategories,
  createProductCategory,
  updateProductCategory,
  deleteProductCategories,
  getProductCategoryById,
  updateProductCategoryStatus,
  uploadProductCategoryImage,
  // 移除：updateProductCategoryImage
} from '../../../api/productCategory';

export default function ProductCategoriesPage() {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState([]);

  const [pageSize, setPageSize] = useState(5);
  const [pageNum, setPageNum] = useState(1);
  const [total, setTotal] = useState(0);

  const [keyword, setKeyword] = useState('');

  const [editOpen, setEditOpen] = useState(false);
  const [editForm] = Form.useForm();
  const [currentItem, setCurrentItem] = useState(null);

  const [imageModalOpen, setImageModalOpen] = useState(false);
  const [imageForm] = Form.useForm();
  const [imageFile, setImageFile] = useState(null);

  useEffect(() => {
    fetchList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pageNum, pageSize]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await listProductCategories({ keyword, pageSize, pageNum });
      if (res?.code === 200 && res?.data) {
        const list = res.data.list || res.data.records || [];
        setData(list);
        setTotal(res.data.total ?? res.data.totalCount ?? list.length);
        setPageNum(res.data.pageNum ?? pageNum);
        setPageSize(res.data.pageSize ?? pageSize);
      } else {
        message.error(res?.message || '获取分类列表失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    } finally {
      setLoading(false);
    }
  };

  const onSearch = () => {
    setPageNum(1);
    fetchList();
  };

  const openCreate = () => {
    setCurrentItem(null);
    editForm.resetFields();
    setEditOpen(true);
  };

  const openEdit = async (record) => {
    try {
      const res = await getProductCategoryById(record.id);
      if (res?.code === 200) {
        setCurrentItem(res.data);
        editForm.setFieldsValue({
          name: res.data.name,
          description: res.data.description,
          sort: res.data.sort,
          status: res.data.status ? 1 : 0,
          image: res.data.image
        });
        setEditOpen(true);
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '获取详情失败');
    }
  };

  const submitEdit = async () => {
    const values = await editForm.validateFields();
    const payload = {
      name: values.name,
      description: values.description,
      sort: values.sort,
      status: values.status === 1 ? true : false,
      image: values.image
    };
    try {
      let res;
      if (currentItem?.id) {
        res = await updateProductCategory(currentItem.id, payload);
      } else {
        res = await createProductCategory(payload);
      }
      if (res?.code === 200) {
        message.success('保存成功');
        setEditOpen(false);
        fetchList();
      } else {
        message.error(res?.message || '保存失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const handleDelete = async (id) => {
    const res = await deleteProductCategories([id]);
    if (res?.code === 200) {
      message.success('删除成功');
      fetchList();
    } else {
      message.error(res?.message || '删除失败');
    }
  };

  const handleStatusChange = async (checked, record) => {
    try {
      const res = await updateProductCategoryStatus(record.id, checked ? 1 : 0);
      if (res?.code === 200) {
        message.success('状态已更新');
        fetchList();
      } else {
        message.error(res?.message || '更新失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const openImageModal = (record) => {
    setCurrentItem(record);
    imageForm.setFieldsValue({ imageUrl: record.image });
    setImageFile(null);
    setImageModalOpen(true);
  };

  const submitImage = async () => {
    if (!currentItem?.id) return;
    try {
      // 仅支持上传文件
      if (!imageFile) {
        message.warning('请先选择要上传的图片文件');
        return;
      }
      const res = await uploadProductCategoryImage(currentItem.id, imageFile);
      if (res?.code !== 200) {
        throw new Error(res?.message || '上传失败');
      }
      message.success('图片上传成功');
      setImageModalOpen(false);
      fetchList();
    } catch (e) {
      message.error(e?.response?.data?.message || e.message || '网络错误');
    }
  };

  const columns = [
    {
      title: '图片',
      dataIndex: 'image',
      key: 'image',
      render: (url) => url ? <img alt="category" src={url} style={{ width: 56, height: 56, objectFit: 'cover', borderRadius: 4 }} /> : '—'
    },
    { title: '名称', dataIndex: 'name', key: 'name' },
    { title: '描述', dataIndex: 'description', key: 'description', ellipsis: true },
    { title: '排序', dataIndex: 'sort', key: 'sort' },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (v, record) => (
        <Switch checked={!!v} onChange={(checked) => handleStatusChange(checked, record)} />
      )
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button onClick={() => openEdit(record)}>编辑</Button>
          <Popconfirm title="确认删除该分类？" onConfirm={() => handleDelete(record.id)}>
            <Button danger>删除</Button>
          </Popconfirm>
          <Button icon={<UploadOutlined />} onClick={() => openImageModal(record)}>图片</Button>
        </Space>
      )
    }
  ];

  return (
    <div>
      <div style={{ marginBottom: 12 }}>
        <Space wrap>
          <Input
            placeholder="名称关键字"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            style={{ width: 200 }}
          />
          <Button type="primary" icon={<SearchOutlined />} onClick={onSearch}>搜索</Button>
          <Button icon={<PlusOutlined />} onClick={openCreate}>新增分类</Button>
        </Space>
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
        title={currentItem?.id ? '编辑分类' : '新增分类'}
        open={editOpen}
        onOk={submitEdit}
        onCancel={() => setEditOpen(false)}
        okText="保存"
        cancelText="取消"
        destroyOnClose
      >
        <Form form={editForm} layout="vertical">
          <Form.Item name="name" label="名称" rules={[{ required: true, message: '请输入分类名称' }]}>
            <Input placeholder="请输入分类名称" />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="sort" label="排序" initialValue={0}>
            <InputNumber style={{ width: '100%' }} min={0} />
          </Form.Item>
          <Form.Item name="status" label="状态" initialValue={1}>
            <Select options={[{ label: '上架', value: 1 }, { label: '下架', value: 0 }]} />
          </Form.Item>
          <Form.Item name="image" label="图片URL">
            <Input placeholder="可直接设置图片URL" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="分类图片"
        open={imageModalOpen}
        onOk={submitImage}
        onCancel={() => setImageModalOpen(false)}
        okText="保存"
        cancelText="取消"
      >
        <Form form={imageForm} layout="vertical">
          <Form.Item label="本地上传">
            <Upload
              beforeUpload={() => false}
              onChange={(info) => setImageFile(info.file)}
              maxCount={1}
            >
              <Button icon={<UploadOutlined />}>选择图片</Button>
            </Upload>
          </Form.Item>
          <Form.Item name="imageUrl" label="或填写图片URL">
            <Input placeholder="https://..." />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}