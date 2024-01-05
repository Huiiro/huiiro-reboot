import request from '@/utils/request.ts';

/**
 * 获取服务器集合
 */
export const getServerInfo = () => request.get("/system/server/info");