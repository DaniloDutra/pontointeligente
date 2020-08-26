package com.dutra.pontointeligente.repositories

import com.dutra.pontointeligente.documents.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface LancamentoRepository : MongoRepository<Lancamento, String> {

  fun findByEmployeeId(employeeId: String, pageable: Pageable): Page<Lancamento>
}