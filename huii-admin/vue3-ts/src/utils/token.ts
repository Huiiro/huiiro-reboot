import {getNewAccessToken} from "@/api/auth/security";

/**
 * access token
 */
export const setAccessToken = (token: any) => {
    localStorage.setItem("access_token", token);
};

export const getAccessToken = () => {
    return localStorage.getItem("access_token");
};

export const removeAccessToken = () => {
    localStorage.removeItem("access_token");
};

/**
 * refresh token
 */
export const setRefreshToken = (token: string) => {
    if(token) {
        localStorage.setItem("refresh_token", token);
    }
};

export const getRefreshToken = () => {
    return localStorage.getItem("refresh_token");
};

export const removeRefreshToken = () => {
    localStorage.removeItem("refresh_token");
};

/**
 * userInfo
 */
export const setUserInfo = (info: Object) => {
    localStorage.setItem("user_info", JSON.stringify(info));
};

export const getUserInfo = () => JSON.parse(localStorage.getItem("user_info") || 'null');

export const removeUserInfo = () => {
    localStorage.removeItem("user_info");
};

/**
 * refresh access_token
 */
export async function refreshAccessToken() {
    removeAccessToken();
    let refreshToken = getRefreshToken();
    if (refreshToken) {
        const tokenResponse = await getNewAccessToken(refreshToken);
        //@ts-ignore
        if (tokenResponse.code === 0) {
            let accessToken = tokenResponse.data.accessToken;
            setAccessToken(accessToken);
            return accessToken;
        }
    }
    return null;
}