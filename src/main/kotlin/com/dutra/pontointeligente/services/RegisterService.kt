package com.dutra.pontointeligente.services

import com.dutra.pontointeligente.documents.Register
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface RegisterService {

  fun buscarPorEmployeeId(employeeId: String, pageRequest: PageRequest): Page<Register>

  fun buscarPorId(id: String): Register?

  fun persistir(register: Register): Register

  fun remover(id: String)
}