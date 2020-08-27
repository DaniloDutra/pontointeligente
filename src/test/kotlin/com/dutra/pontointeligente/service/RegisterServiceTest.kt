package com.dutra.pontointeligente.service

import com.dutra.pontointeligente.documents.Employee
import com.dutra.pontointeligente.documents.Register
import com.dutra.pontointeligente.enums.TipoEnum
import com.dutra.pontointeligente.repositories.RegisterRepository
import com.dutra.pontointeligente.services.RegisterService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest

class RegisterServiceTest {

  @Autowired
  val registerService: RegisterService? = null

  @MockBean
  private lateinit var registerRepository: RegisterRepository

  private val id = "1"

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    BDDMockito.given<Page<Register>>(registerRepository.findByEmployeeId(id, PageRequest.of(0, 10)))
        .willReturn(PageImpl(ArrayList<Register>()))
    BDDMockito.given(registerRepository.findById(id)).willReturn(Optional.of(register()))
    BDDMockito.given(registerRepository.save(Mockito.any(Register::class.java))).willReturn(register())
  }

  private fun register(): Register =
      Register(Date(), TipoEnum.INICIO_TRABALHO, id)

  @Test
  fun testBuscarRegisterByEmployee() {
    val register: Page<Register>? = this.registerService?.searchByEmployeeId(id, PageRequest.of(0, 10))
    Assertions.assertNotNull(register)
  }

  @Test
  fun testBuscarRegisterById() {
    val register: Register? = this.registerService?.searchById(id)
    Assertions.assertNotNull(register)
  }

  @Test
  fun testPersistirRegister() {
    val register: Register? = this.registerService?.persistir(register())
    Assertions.assertNotNull(register)
  }
}