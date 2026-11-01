package com.example.employeemanager.controller;

import com.example.employeemanager.model.Employee;
import com.example.employeemanager.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Display employee registration form and employee list
    @GetMapping({"/", "/employees"})
    public String listEmployees(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees";
    }

    // Save a new employee
    @PostMapping("/employees")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "employees";
        }

        try {
            employeeService.saveEmployee(employee);
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "duplicate", e.getMessage());
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "employees";
        }

        return "redirect:/employees";
    }

    // Display employee edit form
    @GetMapping("/employees/{id}/edit")
    public String editEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "edit-employee";
    }

    // Update employee details
    @PostMapping("/employees/{id}/update")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult result) {

        if (result.hasErrors()) {
            return "edit-employee";
        }

        try {
            employeeService.updateEmployee(id, employee);
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "duplicate", e.getMessage());
            return "edit-employee";
        }

        return "redirect:/employees";
    }

    // Delete an employee
    @PostMapping("/employees/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}