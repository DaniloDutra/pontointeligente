package com.dutra.pontointeligente.service

import com.dutra.pontointeligente.documents.Funcionario
import com.dutra.pontointeligente.enums.PerfilEnum
import com.dutra.pontointeligente.repositories.FuncionarioRepository
import com.dutra.pontointeligente.services.FuncionarioService
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
class FuncionarioServiceTest {
  @Autowired
  val funcionarioService: FuncionarioService? = null

  @MockBean
  private lateinit var funcionarioRepository: FuncionarioRepository

  private val email = "jhon.doe@email.com"
  private val cpf = "12345678900"
  private val id = "1"

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    BDDMockito.given(funcionarioRepository.save(Mockito.any(Funcionario::class.java))).willReturn(funcionario())
    BDDMockito.given(funcionarioRepository.findByEmail(email)).willReturn(funcionario())
    BDDMockito.given(funcionarioRepository.findById(id)).willReturn(Optional.of(funcionario()))
    BDDMockito.given(funcionarioRepository.findByCpf(cpf)).willReturn(funcionario())
  }

  private fun funcionario(): Funcionario =
      Funcionario(nome ="Dutra", email = email, cpf = cpf, perfil = PerfilEnum.ROLE_ADMIN,
          senha = SenhaUtils().gerarBcrypt("12345"), id = id, empresaId = id)
  @Test
  fun testPersistirFuncionario() {
    val funcionario: Funcionario? = this.funcionarioService?.persistir(funcionario())
    Assertions.assertNotNull(funcionario)
  }

  @Test
  fun testBuscarFuncionarioPorCpf() {
    val funcionario: Funcionario? = this.funcionarioService?.buscarPorCpf(cpf)
    Assertions.assertNotNull(funcionario)
  }

  @Test
  fun testBuscarFuncionarioPorEmail() {
    val funcionario: Funcionario? = this.funcionarioService?.buscarPorEmail(email)
    Assertions.assertNotNull(funcionario)
  }

  @Test
  fun testBuscarFuncionarioPorId() {
    val funcionario: Funcionario? = this.funcionarioService?.buscarPorEmail(email)
    Assertions.assertNotNull(funcionario)
  }
}