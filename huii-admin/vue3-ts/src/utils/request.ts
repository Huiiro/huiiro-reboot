import axios from "axios";
import {ElMessage} from "element-plus";
import {getAccessToken, refreshAccessToken} from "@/utils/token.ts";
import {useUserStore} from "@/store/modules/user.ts";

let request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 5000
});

request.interceptors.request.use((config) => {
    const token = getAccessToken()
    if (token !== null && token !== '') {
        config.headers['Authorization'] = 'Bearer ' + token;
    }
    const i18n = localStorage.getItem('i18n');
    config.headers['Accept-Language'] = i18n || 'zh_CN';

    return config;
});

request.interceptors.response.use((response) => {

    if (response.data instanceof Blob) {
        //处理blob数据
        return Promise.resolve(response);
    }

    //处理一般数据
    let code = response.data.code;
    let message = response.data.message;

    if (code === 0 || code === 200) {
        if (message !== 'ok')
            ElMessage({type: 'success', message});
    } else {
        ElMessage({type: 'error', message});
    }

    return Promise.resolve(response.data);
}, async (error) => {
    let status = error.response.status;
    let code = error.response.data.code;
    if (status === 401) {
        if (code === 407) {
            //自定义token异常 获取新accessToken
            const newAccessToken = await refreshAccessToken();
            if (newAccessToken) {
                location.reload();
                error.config.headers.Authorization = 'Bearer ' + newAccessToken;
                return axios(error.config);
            } else {
                const userStore = useUserStore();
                userStore.clearLoginInfo();
                localStorage.clear();
                location.reload();
            }
        } else if (code === 401) {
            const userStore = useUserStore();
            userStore.clearLoginInfo();
            localStorage.clear();
            location.reload();
        }
    }
    let message = error.response.data.message;
    ElMessage({type: 'error', message});

    return Promise.reject(error);
});

export default request