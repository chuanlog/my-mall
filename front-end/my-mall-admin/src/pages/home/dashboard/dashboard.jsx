import React from 'react';
import { Card, Row, Col, Typography } from 'antd';
import './dashboard.css';

const { Title, Paragraph } = Typography;

export default function Dashboard() {
  return (
    <div className="dashboard-container">
      <Title level={2}>欢迎来到商城后台管理系统</Title>
      
      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col xs={24} sm={12} md={8}>
          <Card title="系统概览" bordered={false}>
            <Paragraph>
              这是一个基于Spring Boot和React构建的商城后台管理系统。
            </Paragraph>
          </Card>
        </Col>
        
        <Col xs={24} sm={12} md={8}>
          <Card title="用户管理" bordered={false}>
            <Paragraph>
              管理系统用户、角色和权限分配。
            </Paragraph>
          </Card>
        </Col>
        
        <Col xs={24} sm={12} md={8}>
          <Card title="资源管理" bordered={false}>
            <Paragraph>
              管理系统资源和菜单配置。
            </Paragraph>
          </Card>
        </Col>
      </Row>
      
      <Row gutter={[16, 16]} style={{ marginTop: '16px' }}>
        <Col span={24}>
          <Card title="快速开始" bordered={false}>
            <Paragraph>
              系统已成功集成后端UMS用户管理模块，支持用户注册、登录、权限管理等功能。
            </Paragraph>
            <Paragraph>
              您可以通过左侧菜单导航到不同的功能模块进行管理操作。
            </Paragraph>
          </Card>
        </Col>
      </Row>
    </div>
  );
}