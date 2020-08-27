package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.documents.Register
import com.dutra.pontointeligente.dtos.RegisterDto
import com.dutra.pontointeligente.enums.TipoEnum
import com.dutra.pontointeligente.response.Response
import com.dutra.pontointeligente.services.EmployeeService
import com.dutra.pontointeligente.services.RegisterService
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
@RequestMapping("/api/registers")
class RegisterController(val registerService: RegisterService,
                           val employeeService: EmployeeService) {

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  @Value("\${paginacao.qtd_por_pagina}")
  val qtdByPagina: Int = 15

  @PostMapping
  fun adicionar(@Valid @RequestBody registerDto: RegisterDto,
                result: BindingResult): ResponseEntity<Response<RegisterDto>> {
    val response: Response<RegisterDto> = Response<RegisterDto>()
    validarEmployee(registerDto, result)

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    val buildregister: Register = converterDtoParaRegister(registerDto, result)
    val register = registerService.persistir(buildregister)
    response.data = converterRegisterDto(register)
    return ResponseEntity.ok(response)
  }

  @GetMapping("/{id}")
  fun listarById(@PathVariable("id") id: String): ResponseEntity<Response<RegisterDto>> {
    val response: Response<RegisterDto> = Response<RegisterDto>()
    val register: Register? = registerService.searchById(id)

    if (register == null) {
      response.erros.add("Lançamento não encontrado para o id $id")
      return ResponseEntity.badRequest().body(response)
    }

    response.data = converterRegisterDto(register)
    return ResponseEntity.ok(response)
  }

  @GetMapping("/employee/{employeeId}")
  fun listarByEmployeeId(@PathVariable("employeeId") employeeId: String,
                             @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                             @RequestParam(value = "ord", defaultValue = "id") ord: String,
                             @RequestParam(value = "dir", defaultValue = "DESC") dir: String):
      ResponseEntity<Response<Page<RegisterDto>>> {

    val response: Response<Page<RegisterDto>> = Response<Page<RegisterDto>>()

    val pageRequest: PageRequest = PageRequest.of(pag, qtdByPagina, Sort.Direction.valueOf(dir), ord)
    val registers: Page<Register> =
        registerService.searchByEmployeeId(employeeId, pageRequest)

    val registersDto: Page<RegisterDto> =
        registers.map { converterRegisterDto(it) }

    response.data = registersDto
    return ResponseEntity.ok(response)
  }

  @PatchMapping("/{id}")
  fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody registerDto: RegisterDto,
                result: BindingResult): ResponseEntity<Response<RegisterDto>> {

    val response: Response<RegisterDto> = Response<RegisterDto>()
    validarEmployee(registerDto, result)
    registerDto.id = id
    val register: Register = converterDtoParaRegister(registerDto, result)

    if (result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    registerService.persistir(register)
    response.data = converterRegisterDto(register)
    return ResponseEntity.ok(response)
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  fun remove(@PathVariable("id") id: String): ResponseEntity<Response<String>> {

    val response: Response<String> = Response<String>()
    val register: Register? = registerService.searchById(id)

    if (register == null) {
      response.erros.add("Erro ao remove lançamento. Registro não encontrado para o id $id")
      return ResponseEntity.badRequest().body(response)
    }

    registerService.remove(id)
    return ResponseEntity.ok(Response<String>())
  }

  private fun validarEmployee(registerDto: RegisterDto, result: BindingResult) {
    if (registerDto.employeeId == null) {
      result.addError(ObjectError("employee",
          "Funcionário não informado."))
      return
    }

    val employee: Employee? = employeeService.searchById(registerDto.employeeId)
    if (employee == null) {
      result.addError(ObjectError("employee",
          "Funcionário não encontrado. ID inexistente."));
    }
  }

  private fun converterRegisterDto(register: Register): RegisterDto =
      RegisterDto(dateFormat.format(register.data), register.type.toString(),
          register.descricao, register.localizacao,
          register.employeeId, register.id)

  private fun converterDtoParaRegister(registerDto: RegisterDto,
                                         result: BindingResult): Register {
    if (registerDto.id != null) {
      val lanc: Register? = registerService.searchById(registerDto.id!!)
      if (lanc == null) result.addError(ObjectError("register",
          "Lançamento não encontrado."))
    }

    return Register(dateFormat.parse(registerDto.data), TipoEnum.valueOf(registerDto.type!!),
        registerDto.employeeId!!, registerDto.descricao,
        registerDto.localizacao, registerDto.id)
  }
}