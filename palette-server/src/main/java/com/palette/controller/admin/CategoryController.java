package com.palette.controller.admin;

import com.palette.dto.CategoryDTO;
import com.palette.dto.CategoryPageQueryDTO;
import com.palette.entity.Category;
import com.palette.result.PageResult;
import com.palette.result.Result;
import com.palette.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category management
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Category endpoints")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Add category
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("Add category: {}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * Paginated category query
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Paginated category query")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("Paginated query: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete category by id
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("Delete category")
    public Result<String> deleteById(Long id){
        log.info("Delete category: {}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * Update category
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update category")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * Enable/disable category
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Enable/disable category")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * Get categories by type
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Get categories by type")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
