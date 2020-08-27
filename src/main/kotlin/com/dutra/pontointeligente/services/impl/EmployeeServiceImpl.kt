package com.dutra.pontointeligente.services.impl

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.repositories.EmployeeRepository
import com.dutra.pontointeligente.services.EmployeeService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl(val employeeRepository: EmployeeRepository) : EmployeeService {
  override fun persistir(employee: Employee): Employee = employeeRepository.save(employee)

  override fun searchByCpf(cpf: String): Employee? = employeeRepository.findByCpf(cpf)

  override fun searchByEmail(email: String): Employee? = employeeRepository.findByEmail(email)

  override fun searchById(id: String): Employee? = employeeRepository.findByIdOrNull(id)
}