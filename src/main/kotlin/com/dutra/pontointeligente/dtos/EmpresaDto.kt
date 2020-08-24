package com.dutra.pontointeligente.dtos

import com.dutra.pontointeligente.documents.Empresa

data class EmpresaDto (
    val razaoSocial: String,
    val cnpj: String,
    val id: String?
) {
  companion object Mappers {
    fun paraEmpresaDto(empresa: Empresa): EmpresaDto {
      return empresa.run {
        EmpresaDto(
          razaoSocial = razaoSocial,
          cnpj = cnpj,
          id = id
        )
      }
    }
  }
}