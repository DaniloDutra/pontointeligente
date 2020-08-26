package com.dutra.pontointeligente.controllers

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.documents.Lancamento
import com.dutra.pontointeligente.dtos.LancamentoDto
import com.dutra.pontointeligente.enums.PerfilEnum
import com.dutra.pontointeligente.enums.TipoEnum
import com.dutra.pontointeligente.repositories.LancamentoRepository
import com.dutra.pontointeligente.services.EmployeeService
import com.dutra.pontointeligente.services.LancamentoService
import com.dutra.pontointeligente.utils.SenhaUtils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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
class LancamentoControllerTest(@Autowired val mvc: MockMvc) {

  @MockBean
  private lateinit var lancamentoServiceMock: LancamentoService

  @MockBean
  private lateinit var employeeServiceMock: EmployeeService

  private val urlBase: String = "/api/lancamentos/"
  private val idEmployee: String = "1"
  private val idCompany: String = "2"
  private val idLancamento: String = "1"
  private val tipo: String = TipoEnum.INICIO_TRABALHO.name
  private val data: Date = Date()
  private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

//  @Test
//  @Throws(Exception::class)
//  @WithMockUser
//  fun testCadastrarLancamento() {
//    val lancamento: Lancamento = obterDadosLancamento()
//
//    given<Employee>(employeeServiceMock.buscarPorId(idEmployee))
//        .willReturn(employee())
//    given(lancamentoServiceMock.persistir(lancamento))
//        .willReturn(lancamento.copy(id = idLancamento))
//
//    mvc.perform(MockMvcRequestBuilders.post(urlBase)
//        .content(obterJsonRequisicaoPost())
//        .contentType(MediaType.APPLICATION_JSON)
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk)
//        .andExpect(jsonPath("$.data.tipo").value(tipo))
//        .andExpect(jsonPath("$.data.data").value(dateFormat.format(data)))
//        .andExpect(jsonPath("$.data.employeeId").value(idEmployee))
//        .andExpect(jsonPath("$.erros").isEmpty)
//  }

  @Test
  @Throws(Exception::class)
  @WithMockUser
  fun testCadastrarLancamentoEmployeeIdInvalido() {
    given<Employee>(employeeServiceMock.buscarPorId(idEmployee))
        .willReturn(null)

    mvc.perform(MockMvcRequestBuilders.post(urlBase)
        .content(obterJsonRequisicaoPost())
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
  fun testRemoverLancamento() {
    given<Lancamento>(lancamentoServiceMock.buscarPorId(idLancamento))
        .willReturn(obterDadosLancamento())

    mvc.perform(MockMvcRequestBuilders.delete(urlBase + idLancamento)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk)
  }

  @Throws(JsonProcessingException::class)
  private fun obterJsonRequisicaoPost(): String {
    val lancamentoDto: LancamentoDto = LancamentoDto(
        data = dateFormat.format(data), tipo = tipo, descricao = "Descrição",
        localizacao = "1.234,4.234", employeeId = idEmployee)
    val mapper = ObjectMapper()
    return mapper.writeValueAsString(lancamentoDto)
  }

  private fun obterDadosLancamento(): Lancamento =
      Lancamento(data = data, tipo = TipoEnum.valueOf(tipo), employeeId = idEmployee,
        descricao = "Descrição", localizacao = "1.243,4.345", id = idLancamento)

  private fun employee(): Employee =
      Employee(nome = "Nome", email = "email@email.com",
        senha = SenhaUtils().gerarBcrypt("123456"),
        cpf = "23145699876", perfil = PerfilEnum.ROLE_USUARIO,
          companyId = idCompany, id = idEmployee)
}