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
 */
export const paramBuilder = (data: any, page: pageParam, date: any) => {
    data.size = page.size;
    data.current = page.current;
    data.total = page.total;
    console.log(date)
    //data.params['beginTime'] = 'TODO';
    //data.params['endTime'] = 'TODO';
    return data;
}
