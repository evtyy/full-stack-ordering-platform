package com.palette.controller.user;

import com.palette.entity.Category;
import com.palette.result.Result;
import com.palette.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "Client-side category interfaces")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Query categories
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query categories")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
