package com.dutra.pontointeligente.documents

import com.dutra.pontointeligente.enums.PerfilEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Employee (
  val nome: String,
  val email: String,
  val senha: String,
  val cpf: String,
  val perfil: PerfilEnum,
  val companyId: String,
  val valorHora: Double? = 0.0,
  val qtdHorasTrabalhoDia: Float? = 0.0f,
  val qtdHorasAlmoco: Float? = 0.0f,
  @Id val id: String? = null
)