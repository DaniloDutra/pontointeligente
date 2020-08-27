package com.dutra.pontointeligente.service

import com.dutra.pontointeligente.documents.Company
import com.dutra.pontointeligente.repositories.CompanyRepository
import com.dutra.pontointeligente.services.CompanyService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class CompanyServiceTest {

  @Autowired
  val companyService: CompanyService? = null

  @MockBean
  private val companyRepository: CompanyRepository? = null

  private val CNPJ = "51463645000100"

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    BDDMockito.given(companyRepository?.findByCnpj(CNPJ)).willReturn(company())
    BDDMockito.given(companyRepository?.save(company())).willReturn(company())
  }

  fun company(): Company = Company("Dutra S.A", CNPJ, "1")

  @Test
  fun testBuscarCompanyByCnpj() {
    val company: Company? = companyService?.searchByCnpj(CNPJ)
    Assertions.assertNotNull(company)
  }

  @Test
  fun testSalvarCompany() {
    val company: Company? = companyService?.peristir(company())
    Assertions.assertNotNull(company)
  }
}