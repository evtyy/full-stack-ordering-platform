package com.palette.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.palette.constant.MessageConstant;
import com.palette.constant.StatusConstant;
import com.palette.dto.CategoryDTO;
import com.palette.dto.CategoryPageQueryDTO;
import com.palette.entity.Category;
import com.palette.exception.DeletionNotAllowedException;
import com.palette.mapper.CategoryMapper;
import com.palette.mapper.DishMapper;
import com.palette.mapper.SetmealMapper;
import com.palette.result.PageResult;
import com.palette.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category business layer
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Add a new category
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //Copy properties
        BeanUtils.copyProperties(categoryDTO, category);

        //Category status defaults to disabled (0)
        category.setStatus(StatusConstant.DISABLE);

        //Set creation time, update time, creator, and updater
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * Paginated query
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        //The next SQL statement will be paginated, automatically adding the LIMIT keyword
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * Delete category by id
     * @param id
     */
    public void deleteById(Long id) {
        //Check whether the current category is associated with any dishes; if so, throw a business exception
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //There are dishes under the current category, cannot delete
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_LINKED_TO_DISH);
        }

        //Check whether the current category is associated with any combo meals; if so, throw a business exception
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //There are combo meals under the current category, cannot delete
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_LINKED_TO_SETMEAL);
        }

        //Delete category data
        categoryMapper.deleteById(id);
    }

    /**
     * Update category
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //Set update time and updater
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * Enable/disable category
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * Query categories by type
     * @param type
     * @return
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
