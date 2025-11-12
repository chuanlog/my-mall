import React from 'react';
import { Card, Row, Col, Typography, Button } from 'antd';
import { ShoppingCartOutlined, GiftOutlined, StarOutlined, CrownOutlined } from '@ant-design/icons';
import './HomePage.css';

const { Title, Paragraph } = Typography;
import { useNavigate } from 'react-router';

export default function HomePage() {
  const navigate = useNavigate();
  return (
    <div className="homepage">
      <div className="hero-section">
        <div className="hero-content">
          <Title level={1} className="hero-title">
            欢迎来到商城系统
          </Title>
          <Paragraph className="hero-description">
            发现优质商品，享受购物乐趣
          </Paragraph>
          <Button
            type="primary"
            size="large"
            icon={<ShoppingCartOutlined />}
            onClick={() => navigate('/home/products')}
          >
            开始购物
          </Button>
        </div>
      </div>

      <div className="features-section">
        <Title level={2} style={{ textAlign: 'center', marginBottom: '40px' }}>
          为什么选择我们
        </Title>
        <Row gutter={[24, 24]}>
          <Col xs={24} sm={12} md={6}>
            <Card className="feature-card" hoverable>
              <div className="feature-icon">
                <ShoppingCartOutlined />
              </div>
              <Title level={4}>优质商品</Title>
              <Paragraph>
                精选优质商品，品质保证，让您购物无忧
              </Paragraph>
            </Card>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Card className="feature-card" hoverable>
              <div className="feature-icon">
                <GiftOutlined />
              </div>
              <Title level={4}>优惠活动</Title>
              <Paragraph>
                定期推出优惠活动，让您享受更多实惠
              </Paragraph>
            </Card>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Card className="feature-card" hoverable>
              <div className="feature-icon">
                <StarOutlined />
              </div>
              <Title level={4}>优质服务</Title>
              <Paragraph>
                专业的客服团队，为您提供贴心服务
              </Paragraph>
            </Card>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Card className="feature-card" hoverable>
              <div className="feature-icon">
                <CrownOutlined />
              </div>
              <Title level={4}>会员特权</Title>
              <Paragraph>
                成为会员享受更多专属特权和优惠
              </Paragraph>
            </Card>
          </Col>
        </Row>
      </div>

      <div className="cta-section">
        <Card className="cta-card">
          <Title level={3}>准备好开始购物了吗？</Title>
          <Paragraph>
            浏览我们的商品分类，发现您喜欢的商品
          </Paragraph>
          <Button type="primary" size="large" onClick={() => navigate('/home/products')}>
            浏览商品
          </Button>
        </Card>
      </div>
    </div>
  );
}