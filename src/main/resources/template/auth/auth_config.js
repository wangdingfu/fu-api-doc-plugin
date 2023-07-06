//定义token常量 便于下方使用
const TOKEN_VAR = "ACCESS_TOKEN";
const EXPIRE_TIME = "EXPIRE_TIME";

const currentTime = new Date().getTime();
//从全局变量中获取超时时间
const expireTime = fu.variable(EXPIRE_TIME);
//如果当前时间超过超时时间 则认为已过期
if (currentTime > expireTime) {
    //需要调用接口获取token
    login();
}

/**
 * 登录接口 从服务端获取新的token
 */
function login() {
    //通过fu.doSend('')可以发起http请求 需要指定发起哪一个请求 具体请求在脚本右侧【新增http请求配置】添加
    const result = JSON.parse(fu.doSend('#1'));
    //将接口响应的token保存到全局变量中
    fu.setVariable(TOKEN_VAR, result.data.token);
    //设置过期时间到全局变量中 避免每次请求都调用登录方法
    fu.setVariable(EXPIRE_TIME, currentTime + 3600 * 1000);
}
