package com.dutra.pontointeligente.services.impl

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.repositories.EmpresaRepository
import com.dutra.pontointeligente.services.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository) : EmpresaService {
  override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)

  override fun peristir(empresa: Empresa): Empresa = empresaRepository.save(empresa)
}