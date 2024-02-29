package app.web.gprojuridico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Estagiario.class, name = "Estagiario"),
})
public class Usuario implements UserDetails {

    @DocumentId
    private String email;
    private String nome;
    private String cpf;
    private String unidadeInstitucional;
    private String senha;
    private Boolean status;
    private String role;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setUnidadeInstitucional(String unidadeInstitucional) {
        this.unidadeInstitucional = unidadeInstitucional;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    // setRoles() não deve existir, pois não é boa prática setar uma lista com uma nova lista

    @Override
    @JsonProperty("role")
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> list = new ArrayList<>();
        list.add(new Role(role));
        return list;
    }

    @Override
    @JsonProperty("senha")
    @JsonIgnore
    public String getPassword() {
        return senha;
    }

    @Override
    @JsonProperty("nome")
    @JsonIgnore
    public String getUsername() {
        return nome;
    }

    @Override
    @JsonProperty("contaNaoExpirada")
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonProperty("contaNaoBloqueada")
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonProperty("credenciaisNaoExpirada")
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty("status")
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
