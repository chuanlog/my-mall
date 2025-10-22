import React, { useEffect, useState } from 'react';
import { Table, Input, Button, Space, Modal, Form, Switch, Select, Avatar, Popconfirm, message } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import './users.css';
import { 
  listAdmins, getAdminById, updateAdmin, deleteAdmin, 
  updateAdminStatus, getAdminRoles, updateAdminRole, listAllRoles 
} from '../../../Mock/api';
import { uploadAvatarForAdmin, updateAvatarUrlForAdmin } from '../../../Mock/api';

export default function UsersPage() {
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [data, setData] = useState([]);
  const [pageNum, setPageNum] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [total, setTotal] = useState(0);
  const [rolesMap, setRolesMap] = useState({});

  const [editOpen, setEditOpen] = useState(false);
  const [editForm] = Form.useForm();
  const [currentUser, setCurrentUser] = useState(null);
  // 新增：头像相关状态，修复 no-undef
  const [avatarFile, setAvatarFile] = useState(null);
  const [avatarUploading, setAvatarUploading] = useState(false);

  const [roleOpen, setRoleOpen] = useState(false);
  const [roles, setRoles] = useState([]);
  const [selectedRoleIds, setSelectedRoleIds] = useState([]);

  useEffect(() => {
    fetchList();
  }, [pageNum, pageSize]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await listAdmins({ keyword, pageSize, pageNum });
      if (res?.code === 200 && res?.data) {
        const list = res.data.list || res.data.records || [];
        // 补齐分页元数据
        setTotal(res.data.total ?? res.data.totalCount ?? list.length);
        setPageNum(res.data.pageNum ?? pageNum);
        setPageSize(res.data.pageSize ?? pageSize);

        // 并发拉取当前页每个用户的角色
        const rolePairs = await Promise.all(
          list.map(u =>
            getAdminRoles(u.id)
              .then(r => [u.id, r?.data || []])
              .catch(() => [u.id, []])
          )
        );
        const nextMap = {};
        rolePairs.forEach(([id, roles]) => { nextMap[id] = roles; });
        setRolesMap(nextMap);

        // 若列表缺少昵称/邮箱/头像，按需补齐详情后再渲染
        const needDetails = list
          .filter(u => u.nickName == null || u.email == null || !u.icon)
          .map(u => u.id);
        let detailMap = {};
        if (needDetails.length) {
          const detailPairs = await Promise.all(
            needDetails.map(id =>
              getAdminById(id)
                .then(r => [id, r?.data])
                .catch(() => [id, null])
            )
          );
          detailPairs.forEach(([id, d]) => { if (d) detailMap[id] = d; });
        }
        const merged = list.map(u => detailMap[u.id] ? { ...u, ...detailMap[u.id] } : u);
        setData(merged);
      } else {
        message.error(res?.message || '获取用户列表失败');
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

  const handleStatusChange = async (checked, record) => {
    try {
      const res = await updateAdminStatus(record.id, checked ? 1 : 0);
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

  const openEdit = async (record) => {
    try {
      const res = await getAdminById(record.id);
      if (res?.code === 200) {
        setCurrentUser(res.data);
        editForm.setFieldsValue({
          username: res.data.username,
          nickName: res.data.nickName,
          email: res.data.email,
          icon: res.data.icon
        });
        setEditOpen(true);
      } else {
        message.error(res?.message || '获取详情失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const submitEdit = async () => {
    try {
      const values = await editForm.validateFields();
      const res = await updateAdmin(currentUser.id, values);
      if (res?.code === 200) {
        message.success('更新成功');
        setEditOpen(false);
        fetchList();
      } else {
        message.error(res?.message || '更新失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const onAvatarFileChange = (e) => {
    const file = e.target.files?.[0];
    setAvatarFile(file || null);
  };

  const submitUploadAvatar = async () => {
    if (!currentUser?.id) {
      message.warning('请先打开编辑弹窗');
      return;
    }
    if (!avatarFile) {
      message.warning('请选择图片文件');
      return;
    }
    if (avatarFile.size > 5 * 1024 * 1024) {
      message.error('文件大小不能超过5MB');
      return;
    }
    setAvatarUploading(true);
    try {
      const res = await uploadAvatarForAdmin(currentUser.id, avatarFile);
      if (res?.code === 200) {
        message.success('头像上传成功');
        // 将返回的URL写入表单，便于一起保存或直接使用
        editForm.setFieldsValue({ icon: res.data });
        // 立即刷新列表以显示新头像
        fetchList();
      } else {
        message.error(res?.message || '上传失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    } finally {
      setAvatarUploading(false);
    }
  };

  const submitUpdateAvatarUrl = async () => {
    if (!currentUser?.id) {
      message.warning('请先打开编辑弹窗');
      return;
    }
    const url = editForm.getFieldValue('icon');
    if (!url) {
      message.warning('请先在“头像链接”中填入URL');
      return;
    }
    try {
      const res = await updateAvatarUrlForAdmin(currentUser.id, url);
      if (res?.code === 200) {
        message.success('头像URL已更新');
        setEditOpen(false);
        fetchList();
      } else {
        message.error(res?.message || '更新失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const handleDelete = async (record) => {
    try {
      const res = await deleteAdmin(record.id);
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

  const openRoleAssign = async (record) => {
    try {
      const [allRolesRes, adminRolesRes] = await Promise.all([
        listAllRoles(),
        getAdminRoles(record.id)
      ]);
      if (allRolesRes?.code === 200) {
        setRoles(allRolesRes.data || []);
      }
      if (adminRolesRes?.code === 200) {
        setSelectedRoleIds((adminRolesRes.data || []).map(r => r.id));
      }
      setCurrentUser(record);
      setRoleOpen(true);
    } catch (e) {
      message.error('获取角色数据失败');
      console.error(e);
    }
  };

  const submitRoleAssign = async () => {
    try {
      const res = await updateAdminRole(currentUser.id, selectedRoleIds);
      if (res?.code === 200) {
        message.success('分配成功');
        setRoleOpen(false);
        fetchList();
      } else {
        message.error(res?.message || '分配失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
    { title: '用户名', dataIndex: 'username', key: 'username' },
    { title: '昵称', dataIndex: 'nickName', key: 'nickName' },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    { 
      title: '头像', dataIndex: 'icon', key: 'icon', width: 100,
      render: (icon) => <Avatar src={icon} />
    },
    { 
      title: '状态', dataIndex: 'status', key: 'status', width: 120,
      render: (status, record) => (
        <Switch checked={status === 1} onChange={(checked) => handleStatusChange(checked, record)} />
      )
    },
    {
      title: '角色', key: 'roles',
      render: (_, record) => {
        const roles = rolesMap[record.id] || [];
        return roles.length ? roles.map(r => r.name).join(', ') : '—';
      }
    },
    {
      title: '操作', key: 'action', width: 280,
      render: (_, record) => (
        <Space>
          <Button onClick={() => openEdit(record)} type="link">编辑</Button>
          <Button onClick={() => openRoleAssign(record)} type="link">分配角色</Button>
          <Popconfirm title="确认删除该用户？" onConfirm={() => handleDelete(record)}>
            <Button danger type="link">删除</Button>
          </Popconfirm>
        </Space>
      )
    }
  ];

  return (
    <div className="users-page">
      <div className="users-toolbar">
        <Input
          placeholder="输入用户名或姓名"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          style={{ width: 260 }}
        />
        <Button type="primary" icon={<SearchOutlined />} onClick={onSearch}>搜索</Button>
        <Button icon={<PlusOutlined />} disabled>新增（示例未开放）</Button>
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
        title="编辑用户信息"
        open={editOpen}
        onOk={submitEdit}
        onCancel={() => setEditOpen(false)}
        okText="保存"
        cancelText="取消"
      >
        <Form form={editForm} layout="vertical">
          <Form.Item name="username" label="用户名">
            <Input disabled />
          </Form.Item>
          <Form.Item name="nickName" label="昵称">
            <Input placeholder="请输入昵称" />
          </Form.Item>
          <Form.Item name="email" label="邮箱">
            <Input placeholder="请输入邮箱" />
          </Form.Item>
          <Form.Item name="icon" label="头像链接">
            <Input placeholder="请输入头像URL" />
          </Form.Item>

          <div style={{ marginTop: 8 }}>
            <div style={{ marginBottom: 8 }}>
              <Avatar size={64} src={editForm.getFieldValue('icon')} />
            </div>
            <input type="file" accept="image/*" onChange={onAvatarFileChange} />
            <Space style={{ marginTop: 8 }}>
              <Button type="primary" loading={avatarUploading} onClick={submitUploadAvatar}>
                上传并生成头像URL
              </Button>
              <Button onClick={submitUpdateAvatarUrl}>
                使用上方URL更新头像
              </Button>
            </Space>
          </div>
        </Form>
      </Modal>

      <Modal
        title="分配角色"
        open={roleOpen}
        onOk={submitRoleAssign}
        onCancel={() => setRoleOpen(false)}
        okText="确定"
        cancelText="取消"
      >
        <Select
          mode="multiple"
          style={{ width: '100%' }}
          placeholder="选择角色"
          value={selectedRoleIds}
          onChange={setSelectedRoleIds}
          options={(roles || []).map(r => ({ value: r.id, label: r.name }))}
        />
      </Modal>
    </div>
  );
}