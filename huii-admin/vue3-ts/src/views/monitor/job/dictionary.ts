export const jobGroupOptions = [
    {value: 'DEFAULT', label: '默认'},
];

export const jobStatusOptions = [
    {value: '1', label: '执行', type: 'success'},
    {value: '0', label: '暂停', type: 'danger'},
];

export const jobConcurrentStatusOptions = [
    {value: '1', label: '允许'},
    {value: '0', label: '禁止'},
];

export const jobMisfirePolicyOptions = [
    {value: 1, label: '立即执行'},
    {value: 2, label: '单次执行'},
    {value: 3, label: '稍后执行'},
];

export const jobLogStatusOptions = [
    {value: '1', label: '成功', type: 'success'},
    {value: '0', label: '失败', type: 'danger'},
];