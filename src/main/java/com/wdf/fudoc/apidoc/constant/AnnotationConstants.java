package com.wdf.fudoc.apidoc.constant;

/**
 * @author wangdingfu
 * @Descption 注解常量
 * @date 2022-05-10 21:23:58
 */
public interface AnnotationConstants {

    String CONTROLLER = "org.springframework.stereotype.Controller";
    String REST_CONTROLLER = "org.springframework.web.bind.annotation.RestController";


    String FEIGN_CLIENT = "org.springframework.cloud.openfeign.FeignClient";

    String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";
    String GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";
    String PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";
    String POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";
    String DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";
    String PATCH_MAPPING = "org.springframework.web.bind.annotation.PatchMapping";



    String HTTP_METHOD = "org.springframework.web.bind.annotation.RequestMethod";

    String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";
    String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
    String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";


    String VALIDATED = "org.springframework.validation.annotation.Validated";
    String VALID = "javax.validation.Valid";

    String VALID_NOT_BLANK = "org.hibernate.validator.constraints.NotBlank";
    String VALID_NOT_EMPTY = "org.hibernate.validator.constraints.NotEmpty";
    String JAVAX_VALID_NOT_NULL = "javax.validation.constraints.NotNull";
    String JAVAX_VALID_NOT_EMPTY = "javax.validation.constraints.NotEmpty";
    String JAVAX_VALID_NOT_BLANK = "javax.validation.constraints.NotBlank";


    String SWAGGER_API = "io.swagger.annotations.Api";
    String SWAGGER_API_OPERATION = "io.swagger.annotations.ApiOperation";
    String SWAGGER_API_MODEL = "io.swagger.annotations.ApiModel";
    String SWAGGER_API_MODEL_PROPERTY = "io.swagger.annotations.ApiModelProperty";
    String SWAGGER_API_PARAM = "io.swagger.annotations.ApiParam";


    String[] VALID_NOT = new String[]{JAVAX_VALID_NOT_NULL, JAVAX_VALID_NOT_EMPTY, JAVAX_VALID_NOT_BLANK, VALID_NOT_BLANK, VALID_NOT_EMPTY};

    String[] MAPPING = new String[]{POST_MAPPING, GET_MAPPING, REQUEST_MAPPING, PUT_MAPPING, DELETE_MAPPING, PATCH_MAPPING};
}
