import React, { useEffect, useState } from 'react';
import { Table, Input, Button, Space, Modal, Form, Popconfirm, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { 
  listAllResourceCategories, createResourceCategory, updateResourceCategory, deleteResourceCategory
} from '../../../Mock/api';

export default function ResourceCategoriesPage() {
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
    <div className="resource-categories-page">
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