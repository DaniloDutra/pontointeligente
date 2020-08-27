package com.dutra.pontointeligente.documents

import com.dutra.pontointeligente.enums.TipoEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Register (
  val data: Date,
  val type: TipoEnum,
  val employeeId: String,
  val descricao: String? = "",
  val localizacao: String? = "",
  @Id val id: String? = null
)
