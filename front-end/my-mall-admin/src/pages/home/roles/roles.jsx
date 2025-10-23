import React, { useEffect, useState } from 'react';
import { Table, Input, Button, Space, Modal, Form, Switch, Popconfirm, message, Select } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import {
  listRoles, createRole, updateRole, deleteRoles, updateRoleStatus,
  listRoleResources, allocRoleResources,
  listAllResources
} from '../../../api';

export default function RolesPage() {
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [data, setData] = useState([]);
  const [pageNum, setPageNum] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [total, setTotal] = useState(0);

  const [editOpen, setEditOpen] = useState(false);
  const [editForm] = Form.useForm();
  const [currentItem, setCurrentItem] = useState(null);

  const [assignOpen, setAssignOpen] = useState(false);
  const [assignOptions, setAssignOptions] = useState([]);
  const [selectedAssignIds, setSelectedAssignIds] = useState([]);

  useEffect(() => {
    fetchList();
  }, [pageNum, pageSize]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await listRoles({ keyword, pageSize, pageNum });
      if (res?.code === 200 && res?.data) {
        const list = res.data.list || res.data.records || [];
        setData(list);
        setTotal(res.data.total ?? res.data.totalCount ?? list.length);
        setPageNum(res.data.pageNum ?? pageNum);
        setPageSize(res.data.pageSize ?? pageSize);
      } else {
        message.error(res?.message || '获取角色列表失败');
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

  const openEdit = (record) => {
    setCurrentItem(record);
    editForm.setFieldsValue({
      name: record.name,
      description: record.description,
      status: record.status,
    });
    setEditOpen(true);
  };

  const submitEdit = async () => {
    try {
      const values = await editForm.validateFields();
      let res;
      if (currentItem?.id) {
        res = await updateRole(currentItem.id, values);
      } else {
        res = await createRole(values);
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
      const res = await deleteRoles([record.id]);
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

  const handleStatusChange = async (checked, record) => {
    try {
      const res = await updateRoleStatus(record.id, checked ? 1 : 0);
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

  const openAssign = async (record) => {
    setCurrentItem(record);
    setAssignOpen(true);
    setSelectedAssignIds([]);
    try {
      const res = await listAllResources();
      if (res?.code === 200) {
        setAssignOptions((res.data || []).map(r => ({ value: r.id, label: r.name || r.url })));
      }
      const owned = await listRoleResources(record.id);
      if (owned?.code === 200) {
        setSelectedAssignIds((owned.data || []).map(r => r.id));
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '获取分配数据失败');
    }
  };

  const submitAssign = async () => {
    try {
      const res = await allocRoleResources(currentItem.id, selectedAssignIds);
      if (res?.code === 200) {
        message.success('分配成功');
        setAssignOpen(false);
      } else {
        message.error(res?.message || '分配失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
    { title: '名称', dataIndex: 'name', key: 'name' },
    { title: '描述', dataIndex: 'description', key: 'description' },
    {
      title: '状态', dataIndex: 'status', key: 'status', width: 120,
      render: (status, record) => (
        <Switch checked={status === 1} onChange={(checked) => handleStatusChange(checked, record)} />
      )
    },
    {
      title: '操作', key: 'action', width: 320,
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => openEdit(record)}>编辑</Button>
          <Button type="link" onClick={() => openAssign(record)}>分配资源</Button>
          {/* 已移除分配菜单按钮 */}
          <Popconfirm title="确认删除该角色？" onConfirm={() => handleDelete(record)}>
            <Button danger type="link">删除</Button>
          </Popconfirm>
        </Space>
      )
    }
  ];

  return (
    <div className="roles-page">
      <div className="roles-toolbar" style={{ marginBottom: 16 }}>
        <Input
          placeholder="输入角色名称关键词"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          style={{ width: 260 }}
        />
        <Button type="primary" icon={<SearchOutlined />} onClick={onSearch}>搜索</Button>
        <Button icon={<PlusOutlined />} style={{ marginLeft: 8 }} onClick={openCreate}>新增角色</Button>
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
        title={currentItem?.id ? '编辑角色' : '新增角色'}
        open={editOpen}
        onOk={submitEdit}
        onCancel={() => setEditOpen(false)}
        okText="保存"
        cancelText="取消"
      >
        <Form form={editForm} layout="vertical">
          <Form.Item name="name" label="角色名称" rules={[{ required: true, message: '请输入角色名称' }]}>
            <Input placeholder="请输入角色名称" />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input placeholder="请输入描述（可选）" />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select
              placeholder="选择状态"
              options={[
                { value: 1, label: '启用' },
                { value: 0, label: '禁用' },
              ]}
            />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="分配资源"
        open={assignOpen}
        onOk={submitAssign}
        onCancel={() => setAssignOpen(false)}
        okText="确定"
        cancelText="取消"
      >
        <p style={{ marginBottom: 8 }}>
          选择要分配给该角色的资源：
        </p>
        <Select
          mode="multiple"
          style={{ width: '100%' }}
          value={selectedAssignIds}
          onChange={setSelectedAssignIds}
          options={assignOptions}
          placeholder="选择资源"
        />
      </Modal>
    </div>
  );
}