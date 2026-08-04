package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * Authenticate employee login request
     *
     * @param employeeLoginDTO employee login info
     * @return employee entity after successful authentication
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1. Query employee information from database by username
        Employee employee = employeeMapper.getByUsername(username);

        //2. Handle authentication exceptions
        //check whether username exists
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //encrypt plain text password using MD5 before comparison
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //check if password matches
        if (!password.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //check if employee account is disabled
        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3.return authenticated employee info
        return employee;
    }

    /**
     * Create new employee
     * @param employeeDTO employee info submitted from frontend
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //copy matching fields from DTO to Entity
        BeanUtils.copyProperties(employeeDTO, employee);

        //set account status, default 1 = normal, 0 = locked
        employee.setStatus(StatusConstant.ENABLE);

        //set password, default password: MD5 version of 123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //set creation and update time
//        LocalDateTime now = LocalDateTime.now();
//        employee.setCreateTime(now);
//        employee.setUpdateTime(now);
//
//        //note creator ID and editor ID
//        Long id = BaseContext.getCurrentId();
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);

        //save employee info into database
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //configure pagination parameters
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //query paginated employee data
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> result = page.getResult();

        return new PageResult(total, result);
    }

    @Override
    public void startOrStop(Integer status, Long id) {

        //update employee status = ? where id = ? and modification time
        Employee employee = Employee.builder()
                .updateTime(LocalDateTime.now())
                .id(id)
                .status(status)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        //get employee info by employee ID
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void  update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //copy updated employee info from DTO to Entity
        BeanUtils.copyProperties(employeeDTO, employee);

        //update modification time and copy  ID
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        //update employee info in database
        employeeMapper.update(employee);
    }

}
