import React from "react";
import './user.css'
import {useState,useEffect} from 'react'
import { accountLogin,accountRegister } from "../../Mock/api";
import { useNavigate } from "react-router";
import axios from 'axios';

export default function User() {
    const [isSignedIn, setIsSignedIn] = useState(true);
    const [account, setAccount] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const renderForm = (isSignedIn) => {
      if (isSignedIn) {
        return (
          <form className="content" onSubmit={(e)=>{e.preventDefault()}}>
            <input 
              type="text" 
              onChange={(e) => setAccount(e.target.value)} 
              value={account} 
              placeholder="请输入用户名" 
              disabled={loading}
            />
            <input 
              type="password" 
              onChange={(e) => setPassword(e.target.value)} 
              value={password} 
              placeholder="请输入密码" 
              disabled={loading}
            />
            <button 
              className="submit-btn" 
              onClick={()=>userLogin(account,password)}
              disabled={loading}
            >
              {loading ? '登录中...' : 'Sign In'}
            </button>
          </form>
        )
      }
      else{
        return(
          <form className="content" onSubmit={(e)=>{e.preventDefault()}}>
            <input 
              type="text" 
              onChange={(e) => setAccount(e.target.value)} 
              value={account} 
              placeholder="请输入用户名" 
              disabled={loading}
            />
            <input 
              type="password" 
              onChange={(e) => setPassword(e.target.value)} 
              value={password} 
              placeholder="请输入密码" 
              disabled={loading}
            />
            <button 
              className="submit-btn"
              onClick={()=>userRegister(account,password)}
              disabled={loading}
            >
              {loading ? '注册中...' : 'Sign Up'}
            </button>
          </form>
        )
      }
    }

    const clear=()=>{
      setAccount("");
      setPassword("");
    }

    const userLogin = async (username, password) => {
      if (!username || !password) {
        alert("请输入用户名和密码");
        return;
      }
      
      setLoading(true);
      try {
        const response = await accountLogin(username, password);
        console.log('登录响应:', response);
        
        if(response.code === 200){
          // 登录成功，后端返回的数据结构
          const data = response.data;
          const token = data.token;
          const tokenHead = data.tokenHead;
          
          // 存储token信息
          localStorage.setItem('token', token);
          localStorage.setItem('tokenHead', tokenHead);
          localStorage.setItem('username', username);
          
          // 设置axios默认请求头
          axios.defaults.headers.common['Authorization'] = `${tokenHead}${token}`;
          
          clear();
          alert("登录成功");
          navigate('/home');
        }
        else{
          alert("登录失败：" + (response.message || "用户名或密码错误"));
        }
      } catch (error) {
        console.error('登录错误:', error);
        alert("登录失败：" + (error.response?.data?.message || error.message || "网络错误"));
      } finally {
        setLoading(false);
      }
    }
    
    const userRegister = async(username, password) => {
      if (!username || !password) {
        alert("请输入用户名和密码");
        return;
      }
      
      setLoading(true);
      try {
        const response = await accountRegister(username, password);
        console.log('注册响应:', response);
        
        if(response.code === 200){
          // 注册成功
          alert("注册成功，请继续登录");
          setIsSignedIn(true);
          clear();
        }
        else{
          alert("注册失败：" + (response.message || "注册失败"));
        }
      } catch (error) {
        console.error('注册错误:', error);
        alert("注册失败：" + (error.response?.data?.message || error.message || "网络错误"));
      } finally {
        setLoading(false);
      }
    }
    
    useEffect(()=>{
      const token = localStorage.getItem('token');
      const tokenHead = localStorage.getItem('tokenHead');
      if(token && tokenHead){
        // 设置axios默认请求头
        axios.defaults.headers.common['Authorization'] = `${tokenHead}${token}`;
        alert("已登录,为你跳转到主页");
        navigate('/home')
      }
    }, [navigate])
    
  return (
    <div className="container">
      <div className="login-card">
        <div className="header">商城后台管理系统</div>
        <div className="tabs">
          <button onClick={()=>setIsSignedIn(true)} className="login-btn btn"
            style={{borderBottom: isSignedIn === true ? "2px solid #ce93d8" : "2px solid #151b23",
              color: isSignedIn === true ? "#ce93d8" : "#3b4a5a",
              textShadow: isSignedIn === true ? "0 0 8px #ce93d8, 0 0 16px #ce93d8" : "none"
            }}
          >Sign In</button>
          <button onClick={()=>setIsSignedIn(false)}className="sinup-btn btn"
            style={{borderBottom: isSignedIn === false ? "2px solid #ce93d8" : "2px solid #151b23",
              color: isSignedIn === false ? "#ce93d8" : "#3b4a5a",
              textShadow: isSignedIn === false ? "0 0 8px #ce93d8, 0 0 16px #ce93d8" : "none"
            }}
          >Sign Up</button>
        </div>
        <div className="form-container">
          <label className="label-1">UserName</label>
          <label className="label-2">PassWord</label>
          {renderForm(isSignedIn)}
        </div>
      </div>
    </div>
  );
}