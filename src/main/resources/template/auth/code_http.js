//调用配置好的http接口 需要指定调用的是哪个http请求 并返回响应结果(字符串格式)
//如果响应结果是json格式 可使用JSON.parse()将响应内容转为json对象
const result = fu.doSend('#1');
