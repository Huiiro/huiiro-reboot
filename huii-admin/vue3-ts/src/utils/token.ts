
export const setAccessToken = (token: string) => {
    localStorage.setItem("access_token", token);
};

export const setRefreshToken = (token: string) => {
    localStorage.setItem("refresh_token", token);
};

export const setUserInfo = (info: Object) => {
    localStorage.setItem("user_info", JSON.stringify(info));
};

export const getAccessToken = () => {
    return localStorage.getItem("access_token");
};

export const getRefreshToken = () => {
    return localStorage.getItem("refresh_token");
};

export const getUserInfo = () => {
    return localStorage.getItem("user_info");
};