package com.dutra.pontointeligente.security

import com.dutra.pontointeligente.services.EmployeeService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class EmployeeDetailsService(val employeeService: EmployeeService) : UserDetailsService {
  override fun loadUserByUsername(username: String?): UserDetails {
    if (username != null) {
      val employee = employeeService.searchByEmail(username)
      if (employee != null) {
        return EmployeePrincipal(employee)
      }
    }
    throw UsernameNotFoundException(username)
  }
}