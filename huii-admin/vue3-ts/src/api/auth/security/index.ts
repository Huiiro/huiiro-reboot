import request from '@/utils/request.ts';

enum API {
    GET_PUBLIC_KEY = "/auth/v1/security/key",
    GET_REFRESH_TOKEN = "/auth/v1/security/refresh"
}

/**
 * 获取公钥
 */
export const getServerPublicKey = () => request.get(API.GET_PUBLIC_KEY);

/**
 * 获取 newAccessToken
 * @param refresh refreshToken
 */
export const getNewAccessToken = (refresh: string) =>
    request.get(API.GET_REFRESH_TOKEN, {params: {refresh}});