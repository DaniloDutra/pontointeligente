package com.dutra.pontointeligente.services.impl

import com.dutra.pontointeligente.documents.Funcionario
import com.dutra.pontointeligente.repositories.FuncionarioRepository
import com.dutra.pontointeligente.services.FuncionarioService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FuncionarioServiceImpl(val funcionarioRepository: FuncionarioRepository) : FuncionarioService {
  override fun persistir(funcionario: Funcionario): Funcionario = funcionarioRepository.save(funcionario)

  override fun buscarPorCpf(cpf: String): Funcionario? = funcionarioRepository.findByCpf(cpf)

  override fun buscarPorEmail(email: String): Funcionario? = funcionarioRepository.findByEmail(email)

  override fun buscarPorId(id: String): Funcionario? = funcionarioRepository.findByIdOrNull(id)
}