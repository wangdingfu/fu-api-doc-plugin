
//定义token常量 便于下方使用
const TOKEN_VAR = "ACCESS_TOKEN";

//fu为全局上下文对象 通过fu.result可以获取鉴权接口的响应结果
const jsonResult = fu.result;
//将鉴权接口响应接口中的token设置到全局变量中（如果当前鉴权配置在a项目下 那么这个变量会设置到a项目的全局变量中）
fu.setVariable(TOKEN_VAR, jsonResult.data.token);
//设置当前鉴权有效时间为3600秒 3600秒之后会重新发起鉴权接口 并调用当前脚本
fu.setExpireTime(3600);