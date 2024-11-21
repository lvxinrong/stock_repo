/**
 * 跳转到详情页面
 */
export function goToDetail(router, ts_code) {
    if (!ts_code) {
        console.error("ID 参数不能为空");
        return;
    }
    router.push({ name: "Detail", params: { ts_code } });
}