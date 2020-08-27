package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Company
import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.dtos.CadastroPFDto
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.CompanyService
import com.dutra.pontointeligente.services.EmployeeService
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
class CadastroPfController( val companyService: CompanyService,
                            val employeeService: EmployeeService) {

  @PostMapping
  fun cadastrar(@Valid @RequestBody cadastroPFDto: CadastroPFDto,
                result: BindingResult): ResponseEntity<Response<CadastroPFDto>> {
    val response = Response<CadastroPFDto>()

    val company = companyService.searchByCnpj(cadastroPFDto.cnpj)
    validarDadosExistentes(cadastroPFDto, company, result)

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    val buildEmployee = CadastroPFDto.toEmployee(cadastroPFDto, company!!)
    val employee = employeeService.persistir(buildEmployee)

    response.data = CadastroPFDto.toCadastroPFDTO(employee, company)
    return ResponseEntity.ok(response)
  }

  private fun validarDadosExistentes(cadastroPFDto: CadastroPFDto, company: Company?,
                                     result: BindingResult) {
    if (company == null) {
      result.addError(ObjectError("company", "Company não cadastrada."))
    }

    val employeeCpf: Employee? = employeeService.searchByCpf(cadastroPFDto.cpf)
    if (employeeCpf != null) {
      result.addError(ObjectError("employee", "CPF já existente."))
    }

    val employeeEmail: Employee? = employeeService.searchByEmail(cadastroPFDto.email)
    if (employeeEmail != null) {
      result.addError(ObjectError("employee", "Email já existente."))
    }
  }
}