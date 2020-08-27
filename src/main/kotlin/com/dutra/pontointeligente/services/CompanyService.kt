package com.dutra.pontointeligente.services

import com.dutra.pontointeligente.documents.Company
import com.dutra.pontointeligente.repositories.CompanyRepository

interface CompanyService {

  fun searchByCnpj(cnpj: String): Company?

  fun peristir(company: Company): Company
}