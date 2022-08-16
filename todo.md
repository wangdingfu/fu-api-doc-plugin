# 官网文档

[插件文档地址](https://plugins.jetbrains.com/docs/intellij/welcome.html)

# 20220705

- 参数是否必填支持从`@RequestParam`注解`required`中获取(`已完成`)
- 接口请求类型支持`@RequestMapping`根据`method`属性指定(`已完成`)

# 20220708

- 同一个类中 不同方法同一个入参时，使用@Validated的group指定不同分组时，由于解析过程中使用了缓存  会导致同一个对象只存在一份，无法区分对应方法里的Group(`已完成`)


# 20220718

- 支持针对一个普通java类解析字段(`已完成`)

# 20220722

- 支持对枚举生成接口文档
```
((PsiFieldImpl)((PsiEnumConstantImpl)psiClass.getFields()[0]).getArgumentList().getExpressions()[0].getReference().resolve()).getText()


((PsiBinaryExpressionImpl)((PsiEnumConstantImpl)psiClass.getFields()[0]).getArgumentList().getExpressions()[0]).getOperands()

#解析@link
((PsiJavaCodeReferenceElementImpl)((PsiInlineDocTagImpl)psiDocComment.getDescriptionElements()[4]).getDataElements()[1].getChildren()[0]).getReference().resolve()

psiDocComment.findTagByName("see").getDataElements()[0].getChildren()[0].getReference().resolve()
```

# 1.2.5
1、新增基础配置页面. 支持配置需要过滤的类和属性 支持配置自定义数据
2、工程瘦身. 移除fastJson依赖和HuTools不需要的依赖
3、支持枚举相关配置、校验注解相关配置


# 1.3.0
1、支持多controller生成
