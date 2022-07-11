# 20220705

- 参数是否必填支持从`@RequestParam`注解`required`中获取
- 接口请求类型支持`@RequestMapping`根据`method`属性指定

# 20220708

- 同一个类中 不同方法同一个入参时，使用@Validated的group指定不同分组时，由于解析过程中使用了缓存  会导致同一个对象只存在一份，无法区分对应方法里的Group
