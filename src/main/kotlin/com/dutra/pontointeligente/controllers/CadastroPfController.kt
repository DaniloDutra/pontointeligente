package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.documents.Funcionario
import com.dutra.pontointeligente.dtos.CadastroPFDto
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.EmpresaService
import com.dutra.pontointeligente.services.FuncionarioService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pf")
class CadastroPfController( val empresaService: EmpresaService,
                            val funcionarioService: FuncionarioService) {

  @PostMapping
  fun cadastrar(@Valid @RequestBody cadastroPFDto: CadastroPFDto,
                result: BindingResult): ResponseEntity<Response<CadastroPFDto>> {
    val response = Response<CadastroPFDto>()

    val empresa = empresaService.buscarPorCnpj(cadastroPFDto.cnpj)
    validarDadosExistentes(cadastroPFDto, empresa, result)

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    val buildFuncionario = CadastroPFDto.paraFuncionario(cadastroPFDto, empresa!!)
    val funcionario = funcionarioService.persistir(buildFuncionario)

    response.data = CadastroPFDto.paraCadastroPFDTO(funcionario, empresa)
    return ResponseEntity.ok(response)
  }

  private fun validarDadosExistentes(cadastroPFDto: CadastroPFDto, empresa: Empresa?,
                                     result: BindingResult) {
    if (empresa == null) {
      result.addError(ObjectError("empresa", "Empresa não cadastrada."))
    }

    val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPFDto.cpf)
    if (funcionarioCpf != null) {
      result.addError(ObjectError("funcionario", "CPF já existente."))
    }

    val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPFDto.email)
    if (funcionarioEmail != null) {
      result.addError(ObjectError("funcionario", "Email já existente."))
    }
  }
}