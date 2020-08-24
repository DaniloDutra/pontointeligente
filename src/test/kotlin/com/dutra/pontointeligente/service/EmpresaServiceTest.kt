package com.dutra.pontointeligente.service

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.repositories.EmpresaRepository
import com.dutra.pontointeligente.services.EmpresaService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class EmpresaServiceTest {

  @Autowired
  val empresaService: EmpresaService? = null

  @MockBean
  private val empresaRepository: EmpresaRepository? = null

  private val CNPJ = "51463645000100"

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    BDDMockito.given(empresaRepository?.findByCnpj(CNPJ)).willReturn(empresa())
    BDDMockito.given(empresaRepository?.save(empresa())).willReturn(empresa())
  }

  fun empresa(): Empresa = Empresa("Dutra S.A", CNPJ, "1")

  @Test
  fun testBuscarEmpresaPorCnpj() {
    val empresa: Empresa? = empresaService?.buscarPorCnpj(CNPJ)
    Assertions.assertNotNull(empresa)
  }

  @Test
  fun testSalvarEmpresa() {
    val empresa: Empresa? = empresaService?.peristir(empresa())
    Assertions.assertNotNull(empresa)
  }
}