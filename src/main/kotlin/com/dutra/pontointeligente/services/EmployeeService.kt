package com.dutra.pontointeligente.services

import com.dutra.pontointeligente.documents.Employee

interface EmployeeService {

  fun persistir(employee: Employee): Employee

  fun searchByCpf(cpf: String): Employee?

  fun searchByEmail(email: String): Employee?

  fun searchById(id: String): Employee?
}