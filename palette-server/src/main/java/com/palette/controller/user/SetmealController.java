package com.palette.controller.user;

import com.palette.constant.StatusConstant;
import com.palette.entity.Setmeal;
import com.palette.result.Result;
import com.palette.service.SetmealService;
import com.palette.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "Client-side combo meal browsing interfaces")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * Conditional query
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    @ApiOperation("Query combo meals by category id")
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * Query the list of dishes included in a combo meal by combo meal id
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("Query the list of dishes included in a combo meal by combo meal id")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
