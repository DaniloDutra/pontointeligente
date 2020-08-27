package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Company
import com.dutra.pontointeligente.dtos.CompanyDto
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.CompanyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/companys")
class CompanyController(val companyService: CompanyService) {

  @GetMapping("/cnpj/{cnpj}")
  fun searchByCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<CompanyDto>> {
    val response: Response<CompanyDto> = Response<CompanyDto>()
    val company: Company? = companyService.searchByCnpj(cnpj)

    if (company == null) {
      response.erros.add("Company não encontrada para o CPNJ: ${cnpj}")
      return ResponseEntity.badRequest().body(response)
    }

    response.data = CompanyDto.toCompanyDto(company)
    return ResponseEntity.ok(response)
  }
}