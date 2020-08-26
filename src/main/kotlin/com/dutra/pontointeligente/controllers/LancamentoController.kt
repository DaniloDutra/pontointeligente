package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.documents.Lancamento
import com.dutra.pontointeligente.dtos.LancamentoDto
import com.dutra.pontointeligente.enums.TipoEnum
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.EmployeeService
import com.dutra.pontointeligente.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(val lancamentoService: LancamentoService,
                           val employeeService: EmployeeService) {

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  @Value("\${paginacao.qtd_por_pagina}")
  val qtdPorPagina: Int = 15

  @PostMapping
  fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto,
                result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
    val response: Response<LancamentoDto> = Response<LancamentoDto>()
    validarEmployee(lancamentoDto, result)

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    val buildlancamento: Lancamento = converterDtoParaLancamento(lancamentoDto, result)
    val lancamento = lancamentoService.persistir(buildlancamento)
    response.data = converterLancamentoDto(lancamento)
    return ResponseEntity.ok(response)
  }

  @GetMapping("/{id}")
  fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
    val response: Response<LancamentoDto> = Response<LancamentoDto>()
    val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

    if (lancamento == null) {
      response.erros.add("Lançamento não encontrado para o id $id")
      return ResponseEntity.badRequest().body(response)
    }

    response.data = converterLancamentoDto(lancamento)
    return ResponseEntity.ok(response)
  }

  @GetMapping("/employee/{employeeId}")
  fun listarPorEmployeeId(@PathVariable("employeeId") employeeId: String,
                             @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                             @RequestParam(value = "ord", defaultValue = "id") ord: String,
                             @RequestParam(value = "dir", defaultValue = "DESC") dir: String):
      ResponseEntity<Response<Page<LancamentoDto>>> {

    val response: Response<Page<LancamentoDto>> = Response<Page<LancamentoDto>>()

    val pageRequest: PageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
    val lancamentos: Page<Lancamento> =
        lancamentoService.buscarPorEmployeeId(employeeId, pageRequest)

    val lancamentosDto: Page<LancamentoDto> =
        lancamentos.map { converterLancamentoDto(it) }

    response.data = lancamentosDto
    return ResponseEntity.ok(response)
  }

  @PatchMapping("/{id}")
  fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody lancamentoDto: LancamentoDto,
                result: BindingResult): ResponseEntity<Response<LancamentoDto>> {

    val response: Response<LancamentoDto> = Response<LancamentoDto>()
    validarEmployee(lancamentoDto, result)
    lancamentoDto.id = id
    val lancamento: Lancamento = converterDtoParaLancamento(lancamentoDto, result)

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    lancamentoService.persistir(lancamento)
    response.data = converterLancamentoDto(lancamento)
    return ResponseEntity.ok(response)
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  fun remover(@PathVariable("id") id: String): ResponseEntity<Response<String>> {

    val response: Response<String> = Response<String>()
    val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

    if (lancamento == null) {
      response.erros.add("Erro ao remover lançamento. Registro não encontrado para o id $id")
      return ResponseEntity.badRequest().body(response)
    }

    lancamentoService.remover(id)
    return ResponseEntity.ok(Response<String>())
  }

  private fun validarEmployee(lancamentoDto: LancamentoDto, result: BindingResult) {
    if (lancamentoDto.employeeId == null) {
      result.addError(ObjectError("employee",
          "Funcionário não informado."))
      return
    }

    val employee: Employee? = employeeService.buscarPorId(lancamentoDto.employeeId)
    if (employee == null) {
      result.addError(ObjectError("employee",
          "Funcionário não encontrado. ID inexistente."));
    }
  }

  private fun converterLancamentoDto(lancamento: Lancamento): LancamentoDto =
      LancamentoDto(dateFormat.format(lancamento.data), lancamento.tipo.toString(),
          lancamento.descricao, lancamento.localizacao,
          lancamento.employeeId, lancamento.id)

  private fun converterDtoParaLancamento(lancamentoDto: LancamentoDto,
                                         result: BindingResult): Lancamento {
    if (lancamentoDto.id != null) {
      val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDto.id!!)
      if (lanc == null) result.addError(ObjectError("lancamento",
          "Lançamento não encontrado."))
    }

    return Lancamento(dateFormat.parse(lancamentoDto.data), TipoEnum.valueOf(lancamentoDto.tipo!!),
        lancamentoDto.employeeId!!, lancamentoDto.descricao,
        lancamentoDto.localizacao, lancamentoDto.id)
  }
}