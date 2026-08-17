package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 *  Dish Manager
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "Dish endpoints")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Add new dish
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * Paginated dish query
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Paginated dish query")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("Paginated dish query: {}", dishPageQueryDTO);
        dishPageQueryDTO.setPageSize(100000);
        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }


    /**
     * Enable or disable dish
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        // 1. enable (on sale) 0. disable (discontinued)
        log.info("启用或停用菜品：{}", id);
        dishService.startOrStop(status, id);
        clearRedis("dish_*");
        return Result.success();
    }

    /**
     * Delete dishes by ID
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("Delete dish")
    public Result delete(@RequestParam Long[] ids) {
        log.info("Delete dishes by ID: {}", ids);
        dishService.deleteBatch(ids);

        //clear all dish cache keys matching dish_*
        Set keys = redisTemplate.keys("dish_*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }

        return Result.success();
    }

    /**
     * Get dish by ID with flavors
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get dish by ID")
    public Result<DishVO> getByIdWithFlavor(@PathVariable Long id) {
        log.info("Get dish by ID: {}", id);
        return Result.success(dishService.getByIdWithFlavor(id));
    }

    /**
     * Update dish info
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update dish")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("Update dish: {}", dishDTO);

        //Clear cache for this dish's category
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);

        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * Get dishes by category ID
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }

    /**
     * Clear Redis cache keys matching the given pattern (e.g. "dish_*")
     * @param keys
     */
    private void clearRedis(String keys) {
        Set<String> cacheKeys = redisTemplate.keys(keys);
        redisTemplate.delete(cacheKeys);
    }
}
