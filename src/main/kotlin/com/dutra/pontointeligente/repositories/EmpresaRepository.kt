package com.dutra.pontointeligente.repositories

import com.dutra.pontointeligente.documents.Empresa
import org.springframework.data.mongodb.repository.MongoRepository

interface EmpresaRepository : MongoRepository<Empresa, String> {

  fun findByCnpj(Cnpj: String): Empresa?
}