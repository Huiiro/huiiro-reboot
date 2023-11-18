import axios from "axios";
import {ElMessage} from "element-plus";

let request = axios.create({
    //baseURL: import.meta.env.VITE_BASE_URI,
    baseURL: 'http://localhost:8080',
    timeout: 5000
});

request.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token !== null && token !== '') {
        config.headers['Authorization'] = 'Bearer ' + token;
    }
    const i18n = localStorage.getItem('i18n');
    config.headers['Accept-Language'] = i18n || 'zh_CN';

    return config;
});

request.interceptors.response.use((response) => {
    let code = response.data.code;
    let message = response.data.message;

    if (code === 0 || code === 200) {
        if (message !== 'ok')
            ElMessage({type: 'success', message});
    } else {
        ElMessage({type: 'error', message});
    }

    return Promise.resolve(response.data);
}, (error) => {
    //let status = error.response.status;
    let message = error.response.data.message;

    ElMessage({type: 'error', message});

    return Promise.reject(error);
});

export default request