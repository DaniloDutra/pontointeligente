package com.dutra.pontointeligente.services.impl

import com.dutra.pontointeligente.documents.Register
import com.dutra.pontointeligente.repositories.RegisterRepository
import com.dutra.pontointeligente.services.RegisterService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RegisterServiceImpl(val registerRepository: RegisterRepository) : RegisterService {
  override fun searchByEmployeeId(employeeId: String, pageRequest: PageRequest): Page<Register> =
      registerRepository.findByEmployeeId(employeeId, pageRequest)

  override fun searchById(id: String): Register? = registerRepository.findByIdOrNull(id)

  override fun persistir(register: Register): Register = registerRepository.save(register)

  override fun remove(id: String) = registerRepository.deleteById(id)
}