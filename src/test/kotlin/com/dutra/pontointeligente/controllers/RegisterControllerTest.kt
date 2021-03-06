package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.documents.Register
import com.dutra.pontointeligente.dtos.RegisterDto
import com.dutra.pontointeligente.enums.PerfilEnum
import com.dutra.pontointeligente.enums.TipoEnum
import com.dutra.pontointeligente.services.EmployeeService
import com.dutra.pontointeligente.services.RegisterService
import com.dutra.pontointeligente.utils.SenhaUtils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest(@Autowired val mvc: MockMvc) {


  @MockkBean
  private lateinit var registerMockkBean: RegisterService

  @MockkBean
  private lateinit var employeeMockkBean: EmployeeService

  private val urlBase: String = "/api/registers/"
  private val idEmployee: String = "1"
  private val idCompany: String = "2"
  private val idRegister: String = "1"
  private val type: String = TipoEnum.INICIO_TRABALHO.name
  private val data: Date = Date()
  private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

//  @Test
//  @Throws(Exception::class)
//  @WithMockUser
//  fun testCreateRegister() {
//    val register: Register = getDadosRegister()
//
//    every { employeeMockkBean.searchById(idEmployee) } returns employee()
//    every { registerMockkBean.persistir(register) } returns register
//
//    mvc.perform(MockMvcRequestBuilders.post(urlBase)
//        .content(getJsonRequestPost())
//        .contentType(MediaType.APPLICATION_JSON)
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk)
//        .andExpect(jsonPath("$.data.type").value(type))
//        .andExpect(jsonPath("$.data.data").value(dateFormat.format(data)))
//        .andExpect(jsonPath("$.data.employeeId").value(idEmployee))
//        .andExpect(jsonPath("$.erros").isEmpty)
//  }

  @Test
  @Throws(Exception::class)
  @WithMockUser
  fun testCreateRegisterEmployeeIdInvalido() {
    every { employeeMockkBean.searchById(idEmployee) } returns null

    mvc.perform(MockMvcRequestBuilders.post(urlBase)
        .content(getJsonRequestPost())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.erros")
            .value("Funcionário não encontrado. ID inexistente."))
        .andExpect(jsonPath("$.data").isEmpty)
  }

  @Test
  @Throws(Exception::class)
  @WithMockUser(username = "admin@admin.com", roles = arrayOf("ADMIN"))
  fun testRemoveRegister() {
    every { registerMockkBean.searchById(idRegister) } returns getDadosRegister()
    every { registerMockkBean.remove(idRegister) } returns Unit

    mvc.perform(MockMvcRequestBuilders.delete(urlBase + idRegister)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk)
  }

  @Throws(JsonProcessingException::class)
  private fun getJsonRequestPost(): String {
    val registerDto: RegisterDto = RegisterDto(
        data = dateFormat.format(data), type = type, descricao = "Descrição",
        localizacao = "1.234,4.234", employeeId = idEmployee)
    val mapper = ObjectMapper()
    return mapper.writeValueAsString(registerDto)
  }

  private fun getDadosRegister(): Register =
      Register(data = data, type = TipoEnum.valueOf(type), employeeId = idEmployee,
        descricao = "Descrição", localizacao = "1.243,4.345", id = idRegister)

  private fun employee(): Employee =
      Employee(nome = "Nome", email = "email@email.com",
        senha = SenhaUtils().gerarBcrypt("123456"),
        cpf = "23145699876", perfil = PerfilEnum.ROLE_USUARIO,
          companyId = idCompany, id = idEmployee)
}