package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Funcionario
import com.dutra.pontointeligente.dtos.FuncionarioDto
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.FuncionarioService
import com.dutra.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/funcionarios")
class FuncionarioController(val funcionarioService: FuncionarioService) {

  @PatchMapping("/{id}")
  fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody funcionarioDto: FuncionarioDto,
                result: BindingResult): ResponseEntity<Response<FuncionarioDto>> {

    val response: Response<FuncionarioDto> = Response<FuncionarioDto>()
    val funcionario: Funcionario? = funcionarioService.buscarPorId(id)

    if (funcionario == null) {
      result.addError(ObjectError("funcionario", "Funcionário não encontrado."))
    }

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    val funcAtualizar: Funcionario = atualizarDadosFuncionario(funcionario!!, funcionarioDto)
    funcionarioService.persistir(funcAtualizar)
    response.data = FuncionarioDto.paraFuncionarioDto(funcAtualizar)

    return ResponseEntity.ok(response)
  }

  fun atualizarDadosFuncionario(funcionario: Funcionario,
                                funcionarioDto: FuncionarioDto): Funcionario{
    var senha: String
    if (funcionarioDto.senha == null) {
      senha = funcionario.senha
    } else {
      senha = SenhaUtils().gerarBcrypt(funcionarioDto.senha)
    }

    return Funcionario(
      nome = funcionarioDto.nome,
      email = funcionario.email,
      senha = senha,
      cpf = funcionario.cpf,
      perfil = funcionario.perfil,
      empresaId = funcionario.empresaId,
      valorHora = funcionarioDto.valorHora?.toDouble(),
      qtdHorasTrabalhoDia = funcionarioDto.qtdHorasTrabalhadoDia?.toFloat(),
      qtdHorasAlmoco = funcionarioDto.qtdHorasAlmoco?.toFloat(),
      id = funcionario.id)
  }
}