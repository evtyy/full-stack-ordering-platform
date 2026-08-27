package com.palette.service;

import com.palette.dto.CategoryDTO;
import com.palette.dto.CategoryPageQueryDTO;
import com.palette.entity.Category;
import com.palette.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * Add a new category
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * Paginated query
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * Delete category by id
     * @param id
     */
    void deleteById(Long id);

    /**
     * Update category
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * Enable/disable category
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * Query categories by type
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
