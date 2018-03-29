package com.sathish.employee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sathish.employee.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

}
