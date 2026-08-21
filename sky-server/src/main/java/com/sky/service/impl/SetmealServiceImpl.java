package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Combo meal business logic implementation
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * Add a new combo meal, save association b/w combo meal and its dishes
     *
     * @param setmealDTO
     */
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        setmealDishMapper.insertBatch(setmealDishes);
        log.info("Combo meal-dish associations saved successfully");
        log.info("Combo meal saved successfully");
    }

    /**
     * Paginated query
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        log.info("Paginated query results: {}", page);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * Batch delete combo meals
     *
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        //setmeals on sale cannot be deleted
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal != null && Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        for (Long id : ids) {
            setmealMapper.deleteById(id);
            setmealDishMapper.deleteBySetmealId(id);
        }
    }

    /**
     * Query a combo meal and its associated dish data, by id
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        return null;
    }

    /**
     * Update a combo meal
     *
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * Enable/disable a combo meal for sale
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        setmealMapper.startOrStop(status, id);
    }

    /**
     * Conditional query
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * Query dish options by combo meal id
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> dishItemList = setmealMapper.getDishItemBySetmealId(id);
        for (DishItemVO dishItemVO : dishItemList) {
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dishItemVO.getDishId());
            dishItemVO.setFlavors(flavors);
        }
        return dishItemList;
    }

    @Override
    public SetmealVO getById(Long id) {
        return setmealMapper.getByIdWithDish(id);
    }
}
