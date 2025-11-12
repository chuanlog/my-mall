import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router'
import User from './pages/user/user.jsx'
import Home from './pages/home/home.jsx'
import HomePage from './pages/home/Home/HomePage.jsx'
import ProductsPage from './pages/home/Products/ProductsPage.jsx'
import axios from 'axios'

function App() {
  const token = localStorage.getItem('token');
  const tokenHead = localStorage.getItem('tokenHead');
  
  // 设置axios默认配置
  if (token && tokenHead) {
    axios.defaults.headers.common['Authorization'] = `${tokenHead} ${token}`;
  }

  // 设置基础URL
  axios.defaults.baseURL = 'http://localhost:8080';

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<User/>} />
        <Route path="/user" element={<User/>} />
        <Route path="/home" element={<Home/>} >
          <Route index element={<HomePage />} />
          <Route path="products" element={<ProductsPage />} />
          <Route path="profile" element={<div>个人中心页面（待开发）</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
