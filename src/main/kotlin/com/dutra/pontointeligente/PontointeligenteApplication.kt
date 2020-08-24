package com.dutra.pontointeligente

import com.dutra.pontointeligente.documents.Empresa
import com.dutra.pontointeligente.documents.Funcionario
import com.dutra.pontointeligente.enums.PerfilEnum
import com.dutra.pontointeligente.repositories.EmpresaRepository
import com.dutra.pontointeligente.repositories.FuncionarioRepository
import com.dutra.pontointeligente.repositories.LancamentoRepository
import com.dutra.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class PontointeligenteApplication

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}
