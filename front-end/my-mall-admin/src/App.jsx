import './App.css'
import {BrowserRouter, Routes, Route, Navigate} from 'react-router'
import User from './pages/user/user.jsx'
import Home from './pages/home/home.jsx'
import Dashboard from './pages/home/dashboard/dashboard.jsx'
import axios from 'axios'

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
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
