package com.liutao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liutao.reggie.common.R;
import com.liutao.reggie.entity.Employee;
import com.liutao.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/employee")
@ResponseBody
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request 登录成功后要把员工的id存到session一份表示登录成功，获取当前登录用户可以直接getSession获取
     * @param employee 前端传回的数据有两个key：username和password 要与employee实体类的属性对应上
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        1.  将页面提交的密码password进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//        2.  根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(wrapper);

//        3.  如果每页查询到则返回登录失败结果
        if(one == null){
            return R.error("登录失败");
        }

//        4.  密码比对，如果不一致则返回登录失败结果
        if (! one.getPassword().equals(password)){
            return R.error("密码错误");
        }

//        5.  查看员工状态，如果为已禁用状态，则返回员工已禁用结果

        if(one.getStatus() == 0){
            return R.error("账户已禁用");
        }

//        6.  登录成功将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", one.getId());

        return R.success(one);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功!");
    }

    /**
     * 新增员工
     * @param employee: 前端返回的是JOSN数据
     * @return
     */
    /**
     * 新增员工
     * @param employee， 新增员工的username不能与表里的重复
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 员工信息的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name ={}", page, pageSize, name);

        //1、构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //2、构造条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>();
        //2、1 添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //2、2 添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);

        //3、执行查询
        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());
        Long empId =  (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息");
        Employee byId = employeeService.getById(id);
        if (byId != null) {
            return R.success(byId);
        }
        return R.error("没有查询到对应员工信息");
    }
}
