package com.dutra.pontointeligente.repositories

import com.dutra.pontointeligente.documents.Register
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface RegisterRepository : MongoRepository<Register, String> {

  fun findByEmployeeId(employeeId: String, pageable: Pageable): Page<Register>
}