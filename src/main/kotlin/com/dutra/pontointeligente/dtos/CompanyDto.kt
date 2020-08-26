package com.dutra.pontointeligente.dtos

import com.dutra.pontointeligente.documents.Company

data class CompanyDto (
    val razaoSocial: String,
    val cnpj: String,
    val id: String?
) {
  companion object Mappers {
    fun toCompanyDto(company: Company): CompanyDto {
      return company.run {
        CompanyDto(
          razaoSocial = razaoSocial,
          cnpj = cnpj,
          id = id
        )
      }
    }
  }
}