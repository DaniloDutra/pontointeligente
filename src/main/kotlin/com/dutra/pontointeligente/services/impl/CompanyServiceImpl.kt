package com.dutra.pontointeligente.services.impl

import com.dutra.pontointeligente.documents.Company
import com.dutra.pontointeligente.repositories.CompanyRepository
import com.dutra.pontointeligente.services.CompanyService
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(val companyRepository: CompanyRepository) : CompanyService {
  override fun buscarPorCnpj(cnpj: String): Company? = companyRepository.findByCnpj(cnpj)

  override fun peristir(company: Company): Company = companyRepository.save(company)
}