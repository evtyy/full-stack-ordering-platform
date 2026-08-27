package com.palette.mapper;

import com.palette.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * Batch insert flavors
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * Delete flavors by dish ID
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * Get flavors by dish ID
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    /**
     * Batch delete flavors associated with dish by dish IDs
     * @param dishIds
     */
    void deleteByDishIds(Long[] dishIds);
}
