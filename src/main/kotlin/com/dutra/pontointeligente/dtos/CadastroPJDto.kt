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

data class CadastroPJDto (
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

    @field:NotEmpty(message = "Razão social não pode ser vazio.")
    @field:Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres.")
    val razaoSocial: String = "",

    val id: String? = null
) {
  companion object Mappers {
    fun paraEmpresa(cadastroPJDto: CadastroPJDto): Empresa {
      return cadastroPJDto.run {
        Empresa(
          razaoSocial = razaoSocial,
          cnpj = cnpj
        )
      }
    }
    fun paraFuncionario(cadastroPJDto: CadastroPJDto): Funcionario {
      return cadastroPJDto.run {
        Funcionario(
          nome = nome,
          email = email,
          senha = SenhaUtils().gerarBcrypt(senha),
          cpf = cpf,
          perfil = PerfilEnum.ROLE_ADMIN,
          empresaId = id.toString()
        )
      }
    }
    fun paraCadastroPJDTO(funcionario: Funcionario, empresa: Empresa): CadastroPJDto {
      return funcionario.run {
        CadastroPJDto(
          nome = nome,
          email = email,
          senha = "",
          cpf = cpf,
          id = id,
          cnpj = empresa.cnpj,
          razaoSocial = empresa.razaoSocial
        )
      }
    }
  }
}