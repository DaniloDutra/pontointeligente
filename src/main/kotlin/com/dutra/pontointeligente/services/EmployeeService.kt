package com.dutra.pontointeligente.services

import com.dutra.pontointeligente.documents.Employee

interface EmployeeService {

  fun persistir(employee: Employee): Employee

  fun buscarPorCpf(cpf: String): Employee?

  fun buscarPorEmail(email: String): Employee?

  fun buscarPorId(id: String): Employee?
}