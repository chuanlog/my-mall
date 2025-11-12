import React, {useState} from "react";
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UploadOutlined,
  UserOutlined,
  VideoCameraOutlined,
  HomeOutlined,
  ShoppingCartOutlined
} from '@ant-design/icons';
import { Button, Layout, Menu, theme,Popover,Avatar } from 'antd';
import './home.css'
import { useNavigate,Outlet, useLocation } from "react-router";
const { Header, Sider, Content } = Layout;
export default function Home() {
  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  const navigate = useNavigate();
  const location = useLocation();
  const username=localStorage.getItem("username");

  const handleLogout = () => {
    const confirmed = window.confirm('确定要退出登录吗？');
  if (confirmed) {
    
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("userid");
    alert("退出成功")
    navigate("/user");
  }
  else{return;}
  }

  const content=(
    <div className="tooltip">
      <p>用户名：{username}</p>
      <button onClick={()=>{handleLogout()}} className="tooltip-button">退出登录</button>
    </div>
  )
  return (
    <Layout className="layout">
      <Sider trigger={null} collapsible collapsed={collapsed}>
        <div className="demo-logo-vertical" />
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          onClick={({ key }) => navigate(key)}
          items={[
            { key: '/home', icon: <HomeOutlined />, label: '首页' },
            { key: '/home/products', icon: <ShoppingCartOutlined />, label: '商品' },
            { key: '/home/profile', icon: <UserOutlined />, label: '个人中心' }
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
          <Avatar  className="avatar" size={50} icon={<UserOutlined />} />
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