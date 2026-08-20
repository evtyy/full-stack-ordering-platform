package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * Count how many combo meals belong in category
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * Update combo meal by id
     *
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * Insert new combo meal
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * Paginated query
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * Query a combo meal by id
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * Delete a combo meal by id
     * @param setmealId
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);

    /**
     * Query a combo meal with associated dishes, by id
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * Query combo meals by dynamic conditions
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * Query dish options for given combo meal, by combo meal id
     * @param setmealId
     * @return
     */
    @Select("select sd.dish_id as dishId, sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * Count combo meals matching dynamic conditions
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    @Update("update setmeal set status = #{status} where id = #{id}")
    void startOrStop(Integer status, Long id);
}
