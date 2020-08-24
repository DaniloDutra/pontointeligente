package com.dutra.pontointeligente.services

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.repositories.EmpresaRepository

interface EmpresaService {

  fun buscarPorCnpj(cnpj: String): Empresa?

  fun peristir(empresa: Empresa): Empresa
}