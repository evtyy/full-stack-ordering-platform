package com.palette.service;

import com.palette.dto.SetmealDTO;
import com.palette.dto.SetmealPageQueryDTO;
import com.palette.entity.Setmeal;
import com.palette.result.PageResult;
import com.palette.vo.DishItemVO;
import com.palette.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * Add a new combo meal, and also save the association between the combo meal and its dishes
     *
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * Paginated query
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * Batch delete combo meals
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * Query a combo meal and its associated dish data by id
     *
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * Update combo meal
     *
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * Enable/disable sale of a combo meal
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * Conditional query
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * Query dish options by id
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    SetmealVO getById(Long id);
}
