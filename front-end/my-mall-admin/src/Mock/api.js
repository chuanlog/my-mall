import axios from 'axios';

// 配置axios基础URL
const API_BASE_URL = 'http://localhost:8080';
axios.defaults.baseURL = API_BASE_URL;

//用户登录 - 调用UMS后端接口
export const accountLogin = async (username, password) => {
    try {
        console.log('登录中:', username);
        const response = await axios.post('/admin/login', {
            username: username,
            password: password
        });
        console.log('登录响应:', response.data);
        return response.data;
    } catch (error) {
        console.error('登录失败:', error);
        throw error;
    }
}

//用户注册 - 调用UMS后端接口
export const accountRegister = async (username, password) => {
    try {
        console.log('注册中:', username);
        const response = await axios.post('/admin/register', {
            username: username,
            password: password,
            email: `${username}@example.com`, // 临时邮箱
            nickName: username,
            status: 1
        });
        console.log('注册响应:', response.data);
        return response.data;
    } catch (error) {
        console.error('注册失败:', error);
        throw error;
    }
}

//获取当前用户信息
export const getCurrentUserInfo = async () => {
    try {
        const response = await axios.get('/admin/info');
        console.log('用户信息:', response.data);
        return response.data;
    } catch (error) {
        console.error('获取用户信息失败:', error);
        throw error;
    }
}

//用户登出
export const logout = async () => {
    try {
        const response = await axios.post('/admin/logout');
        console.log('登出响应:', response.data);
        return response.data;
    } catch (error) {
        console.error('登出失败:', error);
        throw error;
    }
}


// 获取全部课程
export const getAllCourse = async (userId,token) => {
  try {
    const response = await axios.get(`/api/course/${userId}`,{
      headers: {
        token: token
      }
    });
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error('获取课程失败:', error);
    return null;
  }
};

//提交课程修改
export const editCourse = async (courseData,token) => {
  try {
    const response = await axios.put('/api/course', courseData,{
      headers: {
        token: token
      }
    });
    console.log('修改成功', response.data);
    return response.data;
  } catch (error) {
    console.error('修改课程失败:', error);
    throw error;
  }
};

//删除课程api
export const deleteCourse = async (courseId,token) => {
  try{
    console.log(token);
    console.log(courseId);
    const response = await axios.delete(`/api/course/${courseId}`,{
      headers: {
        token: token
      }
    });
    console.log(response.data);
    return response.data;
  }catch(error){
    console.error('删除失败:',error);
  }
}

//添加课程api
export const addCourse = async (courseName,courseType,credits,id,preRequisiteCourseIds,totalHours,token) => {
  try{
    //courseName,courseType,credits,id,preRequisiteCourseIds,totalHours
    console.log("信息："+courseName,courseType,credits,id,preRequisiteCourseIds,totalHours);
    console.log("token"+token);
    const response = await axios.post('/api/course', {
      courseName: courseName,
      courseType: courseType,
      credits: credits,
      id: id,
      preRequisiteCourseIds: preRequisiteCourseIds,
      totalHours: totalHours
    },
    {
      headers: {
        token: token
      }
    }
    );
    console.log(response.data);
    return response.data;
  }catch(error){
    console.error('添加失败:',error);
  }
}

//生成培养计划api
export const generatePlan = async (creditsOfCommonElectiveCourses,creditsOfProfessionalElectiveCourses,userId,token) => {
  try{
    const response = await axios.post('/api/plan/planninng', {
      creditsOfCommonElectiveCourses:creditsOfCommonElectiveCourses,
      creditsOfProfessionalElectiveCourses:creditsOfProfessionalElectiveCourses,
      userId:userId
    },{
      headers: {
        token: token
      }
    })
    console.log(response.data);
    return response.data;
  }catch(error){
    console.error('生成失败:',error);
  }
  
}

// 用户列表（分页、搜索）
export const listAdmins = async ({ keyword = '', pageSize = 5, pageNum = 1 }) => {
  try {
    const response = await axios.get('/admin/list', {
      params: { keyword, pageSize, pageNum }
    });
    return response.data;
  } catch (error) {
    console.error('获取用户列表失败:', error);
    throw error;
  }
}

// 获取指定用户信息
export const getAdminById = async (id) => {
  try {
    const response = await axios.get(`/admin/${id}`);
    return response.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
}

// 修改指定用户信息
export const updateAdmin = async (id, admin) => {
  try {
    const response = await axios.post(`/admin/update/${id}`, admin);
    return response.data;
  } catch (error) {
    console.error('更新用户失败:', error);
    throw error;
  }
}

// 删除指定用户
export const deleteAdmin = async (id) => {
  try {
    const response = await axios.post(`/admin/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error('删除用户失败:', error);
    throw error;
  }
}

// 修改帐号状态
export const updateAdminStatus = async (id, status) => {
  try {
    const response = await axios.post(`/admin/updateStatus/${id}`, null, {
      params: { status }
    });
    return response.data;
  } catch (error) {
    console.error('更新用户状态失败:', error);
    throw error;
  }
}

// 给用户分配角色
export const updateAdminRole = async (adminId, roleIds) => {
  try {
    const response = await axios.post(`/admin/role/update`, null, {
      params: { adminId, roleIds }
    });
    return response.data;
  } catch (error) {
    console.error('分配角色失败:', error);
    throw error;
  }
}

// 获取指定用户的角色
export const getAdminRoles = async (adminId) => {
  try {
    const response = await axios.get(`/admin/role/${adminId}`);
  return response.data;
  } catch (error) {
    console.error('获取用户角色失败:', error);
    throw error;
  }
}

// 获取全部角色（用于分配）
export const listAllRoles = async () => {
  try {
    const response = await axios.get('/role/listAll');
    return response.data;
  } catch (error) {
    console.error('获取全部角色失败:', error);
    throw error;
  }
}


// 上传当前登录用户头像
export const uploadAvatarForCurrentUser = async (file) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axios.post('/admin/avatar/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  } catch (error) {
    console.error('上传当前用户头像失败:', error);
    throw error;
  }
}

// 上传指定用户头像
export const uploadAvatarForAdmin = async (adminId, file) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axios.post(`/admin/avatar/upload/${adminId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  } catch (error) {
    console.error('上传指定用户头像失败:', error);
    throw error;
  }
}

// 更新指定用户头像URL
export const updateAvatarUrlForAdmin = async (adminId, avatarUrl) => {
  try {
    const response = await axios.post(`/admin/avatar/update/${adminId}`, null, {
      params: { avatarUrl }
    });
    return response.data;
  } catch (error) {
    console.error('更新指定用户头像URL失败:', error);
    throw error;
  }
}