const TOKEN_VAR = "ACCESS_TOKEN";
const EXPIRE_VAR = "ACCESS_TOKEN_EXPIRES";

/**
 * 定义填充鉴权数据方法
 */
function paddingAuthData() {
    const jsonResult = fudoc.result;
    // 将 接口响应的token 写入环境变量
    fudoc.env.put(TOKEN_VAR, jsonResult.data.token);
    fudoc.env.put(EXPIRE_VAR, 3600);
}



// 获取环境变量里的 ACCESS_TOKEN
const accessToken = fudoc.env.get(TOKEN_VAR);

// 获取环境变量里的 ACCESS_TOKEN_EXPIRES
const accessTokenExpires = fudoc.env.get(EXPIRE_VAR);

// 如 ACCESS_TOKEN 没有值，或 ACCESS_TOKEN_EXPIRES 已过期，则执行发送登录接口请求
if (!accessToken || (accessTokenExpires && new Date(accessTokenExpires) <= new Date())) {
    paddingAuthData();
}