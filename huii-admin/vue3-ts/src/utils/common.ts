/**
 * 通用方法封装
 */

export interface pageParam {
    size: number
    current: number
    total: number
}


/**
 * 请求参数封装
 * @param data 实体参数
 * @param page 分页参数
 * @param date 时间参数
 * @param args 其他参数
 */
export const paramBuilder = (data: any, page: pageParam, date: any,
                             ...args: (Record<string, any> | Map<string, any>)[]) => {
    data.size = page.size;
    data.current = page.current;
    data.total = page.total;
    data.params = {};
    if (date) {
        data.params['beginTime'] = formatDate(date[0]);
        data.params['endTime'] = formatDate(date[1]);
    }
    args.forEach((arg, index) => {
        if (arg !== null && arg !== undefined) {
            if (arg instanceof Map) {
                arg.forEach((value, key) => {
                    data.params[key] = value;
                });
            } else {
                Object.entries(arg).forEach(([key, value]) => {
                    data.params[key] = value;
                });
            }
        }
    })
    return data;
}

function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}