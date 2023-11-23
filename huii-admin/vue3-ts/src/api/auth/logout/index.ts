import request from "@/utils/request.ts";

/**
 * logout
 */
export const logout = () => request.post('/logout');