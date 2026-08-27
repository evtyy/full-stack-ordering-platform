package com.palette.controller.admin;

import com.palette.dto.SetmealDTO;
import com.palette.dto.SetmealPageQueryDTO;
import com.palette.result.PageResult;
import com.palette.result.Result;
import com.palette.service.SetmealService;
import com.palette.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "Combo meal related interfaces")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * Add a new combo meal
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("New combo meal: {}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * Paginated query
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("Paginated query of combo meal list, request params: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete combo meal
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("Batch delete combo meals")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("Delete combo meals, ids: {}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Query combo meal by id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Query combo meal by id")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("Query combo meal by id, id: {}", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * Update combo meal
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update combo meal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("Update combo meal, request params: {}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * Enable or disable combo meal
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Enable or disable combo meal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("Enable or disable combo meal, status: {}, id: {}", status, id);
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}