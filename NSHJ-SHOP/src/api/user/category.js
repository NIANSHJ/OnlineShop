import request from '@/utils/user_request'

//分类列表查询服务
const categoryList = () => {
    return new Promise((resolve) => {
        request.get('/category').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//分类添加服务
const addCategory = (categoryData) => {
    return new Promise((resolve) => {
        request.post('/category', categoryData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//分类修改服务
const updateCategory = (categoryData) => {
    return new Promise((resolve) => {
        request.put('/category', categoryData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//分类删除服务
const deleteCategory = (id) => {
    return new Promise((resolve) => {
        request.delete('/category', { params: { id: id } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    categoryList, addCategory, updateCategory, deleteCategory
};







