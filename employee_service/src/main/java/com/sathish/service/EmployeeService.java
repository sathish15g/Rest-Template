package com.sathish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sathish.employee.model.Employee;
import com.sathish.employee.repo.EmployeeRepo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;

	public List<Employee> getAllEmployees() {

		return employeeRepo.findAll();
	}

	public Employee getEmployeeById(Long id) {
		
		return employeeRepo.findOne(id);
	}

	public void createEmployee(Employee employee) {
	
		employeeRepo.save(employee);
	}

	public void deleteById(Long id) {

		employeeRepo.delete(id);
	}

}
