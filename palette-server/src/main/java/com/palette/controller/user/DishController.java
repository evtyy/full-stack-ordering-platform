package com.palette.controller.user;

import com.palette.constant.StatusConstant;
import com.palette.entity.Dish;
import com.palette.result.Result;
import com.palette.service.DishService;
import com.palette.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "Client-side dish browsing interfaces")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Query dishes by category id
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query dishes by category id")
    public Result<List<DishVO>> list(Long categoryId) {
        // Check whether dish data exists in redis
        String key = "dish_" + categoryId;
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            // If it exists, return the data directly without querying the database
            return Result.success(list);
        }

        // Does not exist in redis, query the database first
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//Query dishes that are currently on sale

        list = dishService.listWithFlavor(dish);

        // Put the queried data into the cache
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }

}
