package com.dutra.pontointeligente.service

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.enums.PerfilEnum
import com.dutra.pontointeligente.repositories.EmployeeRepository
import com.dutra.pontointeligente.services.EmployeeService
import com.dutra.pontointeligente.utils.SenhaUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@SpringBootTest
class EmployeeServiceTest {
  @Autowired
  val employeeService: EmployeeService? = null

  @MockBean
  private lateinit var employeeRepository: EmployeeRepository

  private val email = "jhon.doe@email.com"
  private val cpf = "12345678900"
  private val id = "1"

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    BDDMockito.given(employeeRepository.save(Mockito.any(Employee::class.java))).willReturn(employee())
    BDDMockito.given(employeeRepository.findByEmail(email)).willReturn(employee())
    BDDMockito.given(employeeRepository.findById(id)).willReturn(Optional.of(employee()))
    BDDMockito.given(employeeRepository.findByCpf(cpf)).willReturn(employee())
  }

  private fun employee(): Employee =
      Employee(nome ="Dutra", email = email, cpf = cpf, perfil = PerfilEnum.ROLE_ADMIN,
          senha = SenhaUtils().gerarBcrypt("12345"), id = id, companyId = id)
  @Test
  fun testPersistirEmployee() {
    val employee: Employee? = this.employeeService?.persistir(employee())
    Assertions.assertNotNull(employee)
  }

  @Test
  fun testBuscarEmployeePorCpf() {
    val employee: Employee? = this.employeeService?.buscarPorCpf(cpf)
    Assertions.assertNotNull(employee)
  }

  @Test
  fun testBuscarEmployeePorEmail() {
    val employee: Employee? = this.employeeService?.buscarPorEmail(email)
    Assertions.assertNotNull(employee)
  }

  @Test
  fun testBuscarEmployeePorId() {
    val employee: Employee? = this.employeeService?.buscarPorEmail(email)
    Assertions.assertNotNull(employee)
  }
}