package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.dtos.EmpresaDto
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.EmpresaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(val empresaService: EmpresaService) {

  @GetMapping("/cnpj/{cnpj}")
  fun buscarPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<EmpresaDto>> {
    val response: Response<EmpresaDto> = Response<EmpresaDto>()
    val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)

    if (empresa == null) {
      response.erros.add("Empresa não encontrada para o CPNJ: ${cnpj}")
      return ResponseEntity.badRequest().body(response)
    }

    response.data = EmpresaDto.paraEmpresaDto(empresa)
    return ResponseEntity.ok(response)
  }
}