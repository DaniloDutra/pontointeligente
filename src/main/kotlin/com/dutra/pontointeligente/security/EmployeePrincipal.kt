package com.dutra.pontointeligente.security

import com.dutra.pontointeligente.documents.Employee
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class EmployeePrincipal(val employee: Employee) : UserDetails {
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    val authorities = mutableListOf<GrantedAuthority>()
    authorities.add(SimpleGrantedAuthority(employee.perfil.toString()))
    return authorities
  }

  override fun isEnabled(): Boolean = true

  override fun getUsername(): String = employee.email

  override fun isCredentialsNonExpired(): Boolean = true

  override fun getPassword(): String = employee.senha

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true
}