import request from '@/utils/request.ts';

enum API {
    GET_PUBLIC_KEY = "/auth/v1/security/key"
}

/**
 * 获取公钥
 */
export const getServerPublicKey = () => request.get(API.GET_PUBLIC_KEY);