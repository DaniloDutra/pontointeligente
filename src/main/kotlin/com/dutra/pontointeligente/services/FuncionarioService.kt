package com.dutra.pontointeligente.services

import com.dutra.pontointeligente.documents.Funcionario

interface FuncionarioService {

  fun persistir(funcionario: Funcionario): Funcionario

  fun buscarPorCpf(cpf: String): Funcionario?

  fun buscarPorEmail(email: String): Funcionario?

  fun buscarPorId(id: String): Funcionario?
}