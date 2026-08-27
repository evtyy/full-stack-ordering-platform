package com.palette.service;

import com.palette.dto.EmployeeDTO;
import com.palette.dto.EmployeeLoginDTO;
import com.palette.dto.EmployeePageQueryDTO;
import com.palette.entity.Employee;
import com.palette.result.PageResult;

public interface EmployeeService {

    /**
     * Employee login
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * Add new employee
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * Paginated query
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * Enable/disable employee account
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * Get employee by id
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * Edit employee information
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
