//定义token常量 便于下方使用
const TOKEN_VAR = "ACCESS_TOKEN";
const EXPIRE_TIME = "EXPIRE_TIME";

var currentTime = new Date().getTime();
//从全局变量中获取超时时间
var expireTime = fu.variable(EXPIRE_TIME);
//如果当前时间超过超时时间 则认为已过期
if (currentTime > expireTime) {
    //需要调用接口获取token
    login();
}

/**
 * 登录接口 从服务端获取新的token
 */
function login() {
    //fu为全局上下文对象 通过fu.doRequest()可以调用配置好的http接口 并返回响应结果
    const result = JSON.parse(fu.doRequest());
    //将鉴权接口响应接口中的token设置到全局变量中（如果当前鉴权配置在a项目下 那么这个变量会设置到a项目的全局变量中）
    fu.setVariable(TOKEN_VAR, result.data.token);
    //设置当前鉴权有效时间为3600秒 3600秒之后会重新发起鉴权接口 并调用当前脚本
    fu.setVariable(EXPIRE_TIME, currentTime + result.data.expireTime);
}
