package com.palette.service;

import com.palette.dto.DishDTO;
import com.palette.dto.DishPageQueryDTO;
import com.palette.entity.Dish;
import com.palette.result.PageResult;
import com.palette.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * Add dish with flavors
     *
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * Paginated dish query
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * Delete dishes in batch
     *
     * @param ids
     */
    void deleteBatch(Long[] ids);

    /**
     * Get dish and its flavors by ID
     *
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * Update dish info and its flavors by ID
     *
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * Discontinue or put dish on sale
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * Get dishes by category ID
     *
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * Query dishes and flavors by condition
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
