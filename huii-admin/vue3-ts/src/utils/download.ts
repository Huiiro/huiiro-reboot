import {ElMessage} from "element-plus";

export function downloadExport(res: any) {
    //获取下载请求头
    let name = res.headers['content-disposition'] || res.headers['Content-Disposition'];

    if (name != null || undefined) {
        let nameIndex = name.lastIndexOf("=");
        name = name.substring(nameIndex + 1, name.length);
        name = decodeURIComponent(name);

        const blob = new Blob([res.data]);
        const link = document.createElement('a');

        link.download = name;
        link.style.display = 'none';
        link.href = URL.createObjectURL(blob);

        document.body.appendChild(link);
        link.click();

        URL.revokeObjectURL(link.href);
        document.body.removeChild(link);
        ElMessage({type: 'success', message: '文件导出成功'});
    } else {
        //异常处理
        const file = new FileReader();
        file.readAsText(res.data, 'utf-8');
        file.onload = function () {
            //@ts-ignore
            const result = JSON.parse(file.result);
            ElMessage({type: 'error', message: result.message});
        }
    }
}

export async function download(fileUrl: any, fileName: any) {
    const response = await fetch(fileUrl);
    const blob = await response.blob();
    const link = document.createElement("a");
    link.style.display = "none";
    document.body.appendChild(link);
    const url = window.URL.createObjectURL(blob);
    link.href = url;
    link.setAttribute("download", fileName);
    link.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(link);
}