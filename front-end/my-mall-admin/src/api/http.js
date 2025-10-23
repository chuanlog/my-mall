import axios from 'axios';

export const API_BASE_URL = 'http://localhost:8080';

const http = axios.create({
  baseURL: API_BASE_URL
});

// 统一的数组友好序列化器（与现有用法一致）
export const serializeParams = (params) => {
  const usp = new URLSearchParams();
  Object.entries(params || {}).forEach(([key, value]) => {
    if (Array.isArray(value)) {
      value.forEach(v => usp.append(key, v));
    } else if (value !== undefined && value !== null) {
      usp.append(key, value);
    }
  });
  return usp.toString();
};

export default http;