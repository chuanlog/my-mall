import React from "react";
import './user.css'
import {useState,useEffect} from 'react'
import { userLogin, userRegister } from "../../api/user";
import { useNavigate } from "react-router";

export default function User() {
    const [isSignedIn, setIsSignedIn] = useState(true);
    const [account, setAccount] = useState("");
    const [password, setPassword] = useState("");
    const [nickName, setNickName] = useState("");
    const [email, setEmail] = useState("");
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
            />
            <input 
              type="password" 
              onChange={(e) => setPassword(e.target.value)} 
              value={password} 
              placeholder="请输入密码" 
            />
            <button className="submit-btn" onClick={()=>handleLogin(account,password)}>登录</button>
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
            />
            <input 
              type="password" 
              onChange={(e) => setPassword(e.target.value)} 
              value={password} 
              placeholder="请输入密码" 
            />
            <input 
              type="text" 
              onChange={(e) => setNickName(e.target.value)} 
              value={nickName} 
              placeholder="请输入昵称（可选）" 
            />
            <input 
              type="email" 
              onChange={(e) => setEmail(e.target.value)} 
              value={email} 
              placeholder="请输入邮箱（可选）" 
            />
            <button className="submit-btn" onClick={()=>handleRegister(account,password,nickName,email)}>注册</button>
          </form>
        )
      }
    }

    const clear=()=>{
      setAccount("");
      setPassword("");
      setNickName("");
      setEmail("");
    }

    const handleLogin = async (username, password) => {
      if (!username || !password) {
        alert("请输入用户名和密码");
        return;
      }
      
      try {
        const response = await userLogin({ username, password });
        console.log('登录响应:', response);
        
        if (response.code === 200) {
          // 登录成功
          const { token, tokenHead } = response.data;
          localStorage.setItem('token', token);
          localStorage.setItem('tokenHead', tokenHead);
          localStorage.setItem('username', username);
          
          clear();
          alert("登录成功");
          navigate('/home');
        } else {
          alert("登录失败：" + (response.message || "用户名或密码错误"));
        }
      } catch (error) {
        console.error('登录错误:', error);
        alert("登录失败：网络错误或服务器异常");
      }
    }

    const handleRegister = async(username, password, nickName, email) => {
      if (!username || !password) {
        alert("请输入用户名和密码");
        return;
      }
      
      try {
        const registerData = { username, password };
        if (nickName) registerData.nickName = nickName;
        if (email) registerData.email = email;
        
        const response = await userRegister(registerData);
        console.log('注册响应:', response);
        
        if (response.code === 200) {
          // 注册成功
          alert("注册成功，请继续登录");
          setIsSignedIn(true);
          clear();
        } else {
          alert("注册失败：" + (response.message || "用户名已存在"));
        }
      } catch (error) {
        console.error('注册错误:', error);
        alert("注册失败：网络错误或服务器异常");
      }
    }

    useEffect(()=>{
      if(localStorage.getItem('token')){
        alert("已登录,为你跳转到主页");
        navigate('/home')
      }
    }, [navigate])

  return (
    <div className="container">
      <div className="login-card">
        <div className="header">商城系统</div>
        <div className="tabs">
          <button onClick={()=>setIsSignedIn(true)} className="login-btn btn"
            style={{borderBottom: isSignedIn === true ? "2px solid #ce93d8" : "2px solid #151b23",
              color: isSignedIn === true ? "#ce93d8" : "#3b4a5a",
              textShadow: isSignedIn === true ? "0 0 8px #ce93d8, 0 0 16px #ce93d8" : "none"
            }}
          >登录</button>
          <button onClick={()=>setIsSignedIn(false)}className="sinup-btn btn"
            style={{borderBottom: isSignedIn === false ? "2px solid #ce93d8" : "2px solid #151b23",
              color: isSignedIn === false ? "#ce93d8" : "#3b4a5a",
              textShadow: isSignedIn === false ? "0 0 8px #ce93d8, 0 0 16px #ce93d8" : "none"
            }}
          >注册</button>
        </div>
        <div className="form-container">
          <label className="label-1">用户名</label>
          <label className="label-2">密码</label>
          {renderForm(isSignedIn)}
        </div>
      </div>
    </div>
  );
}