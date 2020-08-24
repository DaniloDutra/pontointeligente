package com.dutra.pontointeligente.dtos

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.documents.Funcionario
import com.dutra.pontointeligente.enums.PerfilEnum
import com.dutra.pontointeligente.utils.SenhaUtils
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class CadastroPFDto (
    @field:NotEmpty(message = "Nome não pode ser vazio.")
    @field:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
    val nome: String = "",

    @field:NotEmpty(message = "Email não pode ser vazio.")
    @field:Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
    @field:Email(message="Email inválido.")
    val email: String = "",

    @field:NotEmpty(message = "Senha não pode ser vazia.")
    val senha: String = "",

    @field:NotEmpty(message = "CPF não pode ser vazio.")
    @field:CPF(message="CPF inválido")
    val cpf: String = "",

    @field:NotEmpty(message = "CNPJ não pode ser vazio.")
    @field:CNPJ(message="CNPJ inválido.")
    val cnpj: String = "",

    val empresaId: String = "",

    val valorHora: String? = null,
    val qtdHorasTrabalhoDia: String? = null,
    val qtdHorasAlmoco: String? = null,
    val id: String? = null
) {
  companion object Mappers {
    fun paraFuncionario(cadastroPFDto: CadastroPFDto, empresa: Empresa): Funcionario {
      return cadastroPFDto.run {
        Funcionario(
          nome = nome,
          email = email,
          senha = SenhaUtils().gerarBcrypt(senha),
          cpf = cpf,
          perfil = PerfilEnum.ROLE_USUARIO,
          empresaId = empresa.id.toString(),
          valorHora = valorHora?.toDouble(),
          qtdHorasTrabalhoDia = qtdHorasTrabalhoDia?.toFloat(),
          qtdHorasAlmoco = qtdHorasAlmoco?.toFloat(),
          id = id
        )
      }
    }
    fun paraCadastroPFDTO(funcionario: Funcionario, empresa: Empresa): CadastroPFDto {
      return funcionario.run {
        CadastroPFDto(
            nome = nome,
            email = email,
            senha = "",
            cpf = cpf,
            cnpj = empresa.cnpj,
            empresaId = empresa.id.toString(),
            valorHora = valorHora.toString(),
            qtdHorasTrabalhoDia = qtdHorasTrabalhoDia.toString(),
            qtdHorasAlmoco = qtdHorasAlmoco.toString(),
            id = id
        )
      }
    }
  }
}