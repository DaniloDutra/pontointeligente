package com.dutra.pontointeligente.repositories

import com.dutra.pontointeligente.documents.Company
import org.springframework.data.mongodb.repository.MongoRepository

interface CompanyRepository : MongoRepository<Company, String> {

  fun findByCnpj(Cnpj: String): Company?
}