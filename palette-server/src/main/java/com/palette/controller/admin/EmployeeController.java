package com.palette.controller.admin;

import com.palette.constant.JwtClaimsConstant;
import com.palette.dto.EmployeeDTO;
import com.palette.dto.EmployeeLoginDTO;
import com.palette.dto.EmployeePageQueryDTO;
import com.palette.entity.Employee;
import com.palette.properties.JwtProperties;
import com.palette.result.PageResult;
import com.palette.result.Result;
import com.palette.service.EmployeeService;
import com.palette.utils.JwtUtil;
import com.palette.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Employee management
 */
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "Employee Management")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Login
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("Employee login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("Employee login: {}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //After successful login, generate a JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * New Employee
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "New Employee")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("New employee: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * Paginated employee query
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "Paginated employee query")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("Paginated employee query, params: {}", employeePageQueryDTO);
        PageResult result = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(result);
    }

    /**
     * Enable/disable employee account
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "Enable/disable employee account")
    public Result startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("Enable/disable employee account: {},{}", status,id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * Get employee by ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get employee by ID")
    public Result<Employee> getById(@PathVariable("id") Long id) {
        log.info("Get employee by ID: {}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * Edit employee info
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Edit employee info")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Edit employee info: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * Logout
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Employee logout")
    public Result<String> logout() {
        return Result.success();
    }


}
