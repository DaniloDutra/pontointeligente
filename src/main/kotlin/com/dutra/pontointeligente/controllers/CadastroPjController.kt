package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.documents.Funcionario
import com.dutra.pontointeligente.dtos.CadastroPJDto
import com.dutra.pontointeligente.repositories.FuncionarioRepository
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.EmpresaService
import com.dutra.pontointeligente.services.FuncionarioService
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pj")
class CadastroPjController(val funcionarioService: FuncionarioService,
                           val empresaService: EmpresaService) {

  @Transactional
  @PostMapping
  fun cadastrar(@Valid @RequestBody cadastroPJDto: CadastroPJDto,
                result: BindingResult): ResponseEntity<Response<CadastroPJDto>> {
    val response = Response<CadastroPJDto>()

    validarDadosExistentes(cadastroPJDto, result)
    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }
    val empresaParaPersistir = CadastroPJDto.paraEmpresa(cadastroPJDto)
    val funcionarioParaPersistir = CadastroPJDto.paraFuncionario(cadastroPJDto)

    val empresa = empresaService.peristir(empresaParaPersistir)
    val funcionario = funcionarioService.persistir(funcionarioParaPersistir)
    response.data = CadastroPJDto.paraCadastroPJDTO(funcionario, empresa)

    return ResponseEntity.ok(response)
  }

  private fun validarDadosExistentes(cadastroPJDto: CadastroPJDto, result: BindingResult) {
    val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPJDto.cnpj)
    if (empresa != null) {
      result.addError(ObjectError("empresa", "Empresa já existente."))
    }

    val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPJDto.cpf)
    if (funcionarioCpf != null) {
      result.addError(ObjectError("funcionario", "CPF já existente."))
    }

    val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPJDto.email)
    if (funcionarioEmail != null) {
      result.addError(ObjectError("funcionario", "Email já existente."))
    }
  }

}