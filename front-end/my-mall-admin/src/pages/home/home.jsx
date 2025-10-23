import React, {useState, useEffect} from "react";
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  DashboardOutlined,
} from '@ant-design/icons';
import { Button, Layout, Menu, theme, Popover, Avatar } from 'antd';
import './home.css'
import { useNavigate, Outlet } from "react-router";
import { logout, getCurrentUserInfo } from "../../api";
import axios from 'axios';
import { message } from 'antd';
import { uploadAvatarForCurrentUser } from '../../api';

const { Header, Sider, Content } = Layout;

export default function Home() {
  const [collapsed, setCollapsed] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [avatarFile, setAvatarFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  const navigate = useNavigate();

  useEffect(() => {
    // 检查登录状态
    const token = localStorage.getItem('token');
    const tokenHead = localStorage.getItem('tokenHead');
    
    if (!token || !tokenHead) {
      alert("请先登录");
      navigate('/user');
      return;
    }

    // 设置axios默认请求头
    axios.defaults.headers.common['Authorization'] = `${tokenHead}${token}`;

    // 获取用户信息
    fetchUserInfo();
  }, [navigate]);

  const fetchUserInfo = async () => {
    try {
      const response = await getCurrentUserInfo();
      if (response.code === 200) {
        setUserInfo(response.data);
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
      // 如果获取用户信息失败，可能是token过期，跳转到登录页
      if (error.response?.status === 401) {
        localStorage.removeItem("token");
        localStorage.removeItem("tokenHead");
        localStorage.removeItem("username");
        alert("登录已过期，请重新登录");
        navigate("/user");
      }
    }
  };

  const handleLogout = async () => {
    const confirmed = window.confirm('确定要退出登录吗？');
    if (confirmed) {
      try {
        await logout();
      } catch (error) {
        console.error('登出失败:', error);
      } finally {
        // 无论登出接口是否成功，都清除本地存储
        localStorage.removeItem("token");
        localStorage.removeItem("tokenHead");
        localStorage.removeItem("username");
        delete axios.defaults.headers.common['Authorization'];
        alert("退出成功");
        navigate("/user");
      }
    }
  }

  const handleFileChange = (e) => {
    const file = e.target.files?.[0];
    setAvatarFile(file || null);
  };

  const handleUploadAvatar = async () => {
    if (!avatarFile) {
      message.warning('请先选择图片文件');
      return;
    }
    if (avatarFile.size > 5 * 1024 * 1024) {
      message.error('文件大小不能超过5MB');
      return;
    }
    setUploading(true);
    try {
      const res = await uploadAvatarForCurrentUser(avatarFile);
      if (res?.code === 200) {
        message.success('头像上传成功');
        setUserInfo(prev => ({ ...(prev || {}), icon: res.data }));
        setAvatarFile(null);
      } else {
        message.error(res?.message || '上传失败');
      }
    } catch (e) {
      message.error(e?.response?.data?.message || '网络错误');
    } finally {
      setUploading(false);
    }
  };

  const content = (
    <div className="tooltip">
      <p>用户名：{userInfo?.username || localStorage.getItem("username")}</p>
      <p>角色：{userInfo?.roles?.join(', ') || '普通用户'}</p>
      <div style={{ marginTop: 8 }}>
        <div style={{ marginBottom: 8 }}>
          <Avatar size={64} src={userInfo?.icon} icon={<UserOutlined />} />
        </div>
        <input type="file" accept="image/*" onChange={handleFileChange} />
        <Button
          type="primary"
          style={{ marginTop: 8 }}
          loading={uploading}
          onClick={handleUploadAvatar}
        >
          上传头像
        </Button>
      </div>
      <button onClick={handleLogout} className="tooltip-button" style={{ marginTop: 8 }}>退出登录</button>
    </div>
  )

  return (
    <Layout className="layout">
      <Sider trigger={null} collapsible collapsed={collapsed}>
        <div className="demo-logo-vertical" />
        <Menu
          theme="dark"
          mode="inline"
          defaultSelectedKeys={['/home/dashboard']}
          onClick={({ key }) => navigate(key)}
          items={[
            {
              key: '/home/dashboard',
              icon: <DashboardOutlined />,
              label: '仪表盘',
            },
            {
              key: '/home/users',
              icon: <UserOutlined />,
              label: '用户管理',
            },
            {
              key: '/home/resources',
              icon: <DashboardOutlined />,
              label: '资源管理',
            },
            {
              key: '/home/roles',
              icon: <UserOutlined />,
              label: '角色管理',
            },
          ]}
        />
      </Sider>
      <Layout>
        <Header className="header" style={{ padding: 0, background: colorBgContainer }}>
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{
              fontSize: '16px',
              width: 64,
              height: 64,
            }}
          />
          <Popover content={content} placement="leftTop" trigger="hover">
            <Avatar className="avatar" size={50} src={userInfo?.icon} icon={<UserOutlined />} />
          </Popover>
        </Header>
        <Content
          style={{
            margin: '24px 16px',
            padding: 24,
            minHeight: 280,
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
          }}
        >
          <div
            style={{
              padding: 15,
              textAlign: 'center',
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
              width: '100%',
              height: '100%',
            }}
          >
            <Outlet />
          </div>
        </Content>
      </Layout>
    </Layout>
  );
}