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
import { logout, getCurrentUserInfo } from "../../Mock/api";
import axios from 'axios';

const { Header, Sider, Content } = Layout;

export default function Home() {
  const [collapsed, setCollapsed] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
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

  const content = (
    <div className="tooltip">
      <p>用户名：{userInfo?.username || localStorage.getItem("username")}</p>
      <p>角色：{userInfo?.roles?.join(', ') || '普通用户'}</p>
      <button onClick={handleLogout} className="tooltip-button">退出登录</button>
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
            <Avatar className="avatar" size={50} icon={<UserOutlined />} />
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