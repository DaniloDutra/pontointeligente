package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.dtos.EmployeeDto
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.EmployeeService
import com.dutra.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/employees")
class EmployeeController(val employeeService: EmployeeService) {

  @PatchMapping("/{id}")
  fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody employeeDto: EmployeeDto,
                result: BindingResult): ResponseEntity<Response<EmployeeDto>> {

    val response: Response<EmployeeDto> = Response<EmployeeDto>()
    val employee: Employee? = employeeService.searchById(id)

    if (employee == null) {
      result.addError(ObjectError("employee", "Funcionário não encontrado."))
    }

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    val funcAtualizar: Employee = atualizarDadosEmployee(employee!!, employeeDto)
    employeeService.persistir(funcAtualizar)
    response.data = EmployeeDto.toEmployeeDto(funcAtualizar)

    return ResponseEntity.ok(response)
  }

  fun atualizarDadosEmployee(employee: Employee,
                                employeeDto: EmployeeDto): Employee{
    var senha: String
    if (employeeDto.senha == null) {
      senha = employee.senha
    } else {
      senha = SenhaUtils().gerarBcrypt(employeeDto.senha)
    }

    return Employee(
      nome = employeeDto.nome,
      email = employee.email,
      senha = senha,
      cpf = employee.cpf,
      perfil = employee.perfil,
      companyId = employee.companyId,
      valorHora = employeeDto.valorHora?.toDouble(),
      qtdHorasTrabalhoDia = employeeDto.qtdHorasTrabalhadoDia?.toFloat(),
      qtdHorasAlmoco = employeeDto.qtdHorasAlmoco?.toFloat(),
      id = employee.id)
  }
}