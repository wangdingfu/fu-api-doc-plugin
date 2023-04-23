
// 定义发送登录接口请求方法
function sendLoginRequest() {
    // 获取环境里的 前置URL
    const baseUrl = pm.environment.get("BASE_URL");

    const tenantId = pm.environment.get("TENANT_ID");

    // 登录用户名，这里从环境变量 LOGIN_USERNAME 获取，也可以写死（但是不建议）
    const username = pm.environment.get("LOGIN_USERNAME");

    // 登录用户名，这里从环境变量 LOGIN_PASSWORD 获取，也可以写死（但是不建议）
    const password = pm.environment.get("LOGIN_PASSWORD");

    // 构造一个 POST x-www-form-urlencoded 格式请求。这里需要改成你们实际登录接口的请求参数。
    const loginRequest = {
        url: baseUrl + "/blade-auth/oauth/token",
        method: "POST",
        header: {
            "Authorization":"Basic c3dvcmQ6c3dvcmRfc2VjcmV0"
        },
        body: {
            mode: 'formdata', // 此处为 formdata
            // 此处为 formdata
            formdata: [
                { key: "tenantId", value: tenantId },
                { key: "username", value: username },
                { key: "password", value: password },
            ]
        }

    };

    // 发送请求。
    // pm.sendrequest 参考文档: https://www.apifox.cn/help/app/scripts/api-references/pm-reference/#pm-sendrequest
    pm.sendRequest(loginRequest, function (err, res) {
        if (err) {
            console.log(err);
        } else {
            // 读取接口返回的 json 数据。
            // 如果你的 token 信息是存放在 cookie 的，可以使用 res.cookies.get('token') 方式获取。
            // cookies 参考文档：https://www.apifox.cn/help/app/scripts/api-references/pm-reference/#pm-cookies

            const jsonData = res.json();
            // 将 accessToken 写入环境变量 ACCESS_TOKEN
            pm.environment.set("ACCESS_TOKEN", jsonData.token_type + ' ' + jsonData.access_token);
            // 将 accessTokenExpires 过期时间写入环境变量 ACCESS_TOKEN_EXPIRES
            pm.environment.set(
                "ACCESS_TOKEN_EXPIRES",
                new Date().getTime()+(jsonData.expires_in)*1000
            );
        }
    });
}

// 获取环境变量里的 ACCESS_TOKEN
const accessToken = pm.environment.get("ACCESS_TOKEN");

// 获取环境变量里的 ACCESS_TOKEN_EXPIRES
const accessTokenExpires = pm.environment.get("ACCESS_TOKEN_EXPIRES");

// 如 ACCESS_TOKEN 没有值，或 ACCESS_TOKEN_EXPIRES 已过期，则执行发送登录接口请求
if (
    !accessToken ||
    (accessTokenExpires && new Date(accessTokenExpires) <= new Date())
) {
    sendLoginRequest();
}