package com.dutra.pontointeligente.services.impl

import com.dutra.pontointeligente.documents.Lancamento
import com.dutra.pontointeligente.repositories.LancamentoRepository
import com.dutra.pontointeligente.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LancamentoServiceImpl(val lancamentoRepository: LancamentoRepository) : LancamentoService {
  override fun buscarPorEmployeeId(employeeId: String, pageRequest: PageRequest): Page<Lancamento> =
      lancamentoRepository.findByEmployeeId(employeeId, pageRequest)

  override fun buscarPorId(id: String): Lancamento? = lancamentoRepository.findByIdOrNull(id)

  override fun persistir(lancamento: Lancamento): Lancamento = lancamentoRepository.save(lancamento)

  override fun remover(id: String) = lancamentoRepository.deleteById(id)
}