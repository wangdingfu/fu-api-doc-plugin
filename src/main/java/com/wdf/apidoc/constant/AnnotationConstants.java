package com.wdf.apidoc.constant;

/**
 * @author wangdingfu
 * @Descption 注解常量
 * @Date 2022-05-10 21:23:58
 */
public interface AnnotationConstants {

    String CONTROLLER = "org.springframework.stereotype.Controller";
    String REST_CONTROLLER = "org.springframework.web.bind.annotation.RestController";

    String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";
    String GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";
    String PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";
    String POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";
    String DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";
    String PATCH_MAPPING = "org.springframework.web.bind.annotation.PatchMapping";

    String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";
    String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
    String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";


    String VALID_NOT_NULL = "javax.validation.constraints.NotNull";
    String VALID_NOT_BLANK = "org.hibernate.validator.constraints.NotBlank";
    String VALID_NOT_EMPTY = "org.hibernate.validator.constraints.NotEmpty";

    String[] VALID_NOT = new String[]{VALID_NOT_NULL, VALID_NOT_BLANK, VALID_NOT_EMPTY};

    String[] MAPPING = new String[]{REQUEST_MAPPING, GET_MAPPING, PUT_MAPPING, POST_MAPPING, DELETE_MAPPING, PATCH_MAPPING};
}
