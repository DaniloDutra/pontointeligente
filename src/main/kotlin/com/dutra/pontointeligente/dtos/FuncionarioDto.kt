package com.dutra.pontointeligente.dtos

import com.dutra.pontointeligente.documents.Funcionario
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class FuncionarioDto (
    @field:NotEmpty(message = "Nome deve conter entre 3 e 200 caracteres.")
    @field:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
    val nome: String = "",

    @field:NotEmpty(message = "Email não pode ser vazio.")
    @field:Length(min = 5, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
    @field:Email
    val email: String = "",

    val senha: String? = null,
    val valorHora: String? = null,
    val qtdHorasTrabalhadoDia: String? = null,
    val qtdHorasAlmoco: String? = null,
    val id: String? = null
) {
  companion object Mappers {
    fun paraFuncionarioDto(funcionario: Funcionario): FuncionarioDto {
      return funcionario.run {
        FuncionarioDto(
          nome = nome,
          email = email,
          senha = "",
          valorHora = valorHora.toString(),
          qtdHorasTrabalhadoDia = qtdHorasTrabalhoDia.toString(),
          qtdHorasAlmoco = qtdHorasAlmoco.toString(),
          id = id
        )
      }
    }
  }
}