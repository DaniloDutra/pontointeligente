package com.dutra.pontointeligente.dtos

import javax.validation.constraints.NotEmpty

data class LancamentoDto (
    @field:NotEmpty(message = "Data não pode ser vazia.")
    val data: String? = null,

    @field:NotEmpty(message = "Tipo não pode ser vazio.")
    val tipo: String? = null,

    val descricao: String? = null,
    val localizacao: String? = null,
    val employeeId: String? = null,
    var id: String? = null
)