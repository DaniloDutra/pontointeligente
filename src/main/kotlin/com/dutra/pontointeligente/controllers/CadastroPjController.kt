package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Company
import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.dtos.CadastroPJDto
import com.dutra.pontointeligente.repositories.EmployeeRepository
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.CompanyService
import com.dutra.pontointeligente.services.EmployeeService
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
class CadastroPjController(val employeeService: EmployeeService,
                           val companyService: CompanyService) {

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
    val companyParaPersistir = CadastroPJDto.toCompany(cadastroPJDto)
    val employeeParaPersistir = CadastroPJDto.toEmployee(cadastroPJDto)

    val company = companyService.peristir(companyParaPersistir)
    val employee = employeeService.persistir(employeeParaPersistir)
    response.data = CadastroPJDto.toCadastroPJDTO(employee, company)

    return ResponseEntity.ok(response)
  }

  private fun validarDadosExistentes(cadastroPJDto: CadastroPJDto, result: BindingResult) {
    val company: Company? = companyService.buscarPorCnpj(cadastroPJDto.cnpj)
    if (company != null) {
      result.addError(ObjectError("company", "Company já existente."))
    }

    val employeeCpf: Employee? = employeeService.buscarPorCpf(cadastroPJDto.cpf)
    if (employeeCpf != null) {
      result.addError(ObjectError("employee", "CPF já existente."))
    }

    val employeeEmail: Employee? = employeeService.buscarPorEmail(cadastroPJDto.email)
    if (employeeEmail != null) {
      result.addError(ObjectError("employee", "Email já existente."))
    }
  }

}