import './App.css'
import {BrowserRouter, Routes, Route, Navigate} from 'react-router'
import User from './pages/user/user.jsx'
import Home from './pages/home/home.jsx'
import Dashboard from './pages/home/dashboard/dashboard.jsx'
import axios from 'axios'
import UsersPage from './pages/home/users/users.jsx'
import RolesPage from './pages/home/roles/roles.jsx'
import ResourceManagePage from './pages/home/resources/manage.jsx'
import ProductsPage from './pages/home/products/products.jsx'
import ProductCategoriesPage from './pages/home/productCategories/productCategories.jsx'

function App() {
  // 在应用启动时设置axios默认配置
  const token = localStorage.getItem('token');
  const tokenHead = localStorage.getItem('tokenHead');
  if (token && tokenHead) {
    axios.defaults.headers.common['Authorization'] = `${tokenHead}${token}`;
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<User/>} />
        <Route path="/user" element={<User/>} />
        <Route path="/home" element={<Home/>} >
          <Route index element={<Navigate to="dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="users" element={<UsersPage />} />
          {/* 新增：角色管理 */}
          <Route path="resources" element={<ResourceManagePage />} />
          <Route path="resource-categories" element={<ResourceManagePage />} />
          <Route path="roles" element={<RolesPage />} />
          {/* 新增：商品管理与商品分类管理 */}
          <Route path="products" element={<ProductsPage />} />
          <Route path="product-categories" element={<ProductCategoriesPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
