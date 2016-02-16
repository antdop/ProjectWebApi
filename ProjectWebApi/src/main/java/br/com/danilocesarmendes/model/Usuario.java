package br.com.danilocesarmendes.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "public", name = "usuario")
public class Usuario implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "public.seq_usuario", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_usuario")
    private Integer id;
    
    @Column(name="nome", length = 500, nullable = false)
    private String nome;
    
    @Column(name="email", length = 100, nullable = false)
    private String email;
    
    @Column(name="senha", length = 100, nullable = false)
    private String senha;
    
    @Column(name="token", length = 100)
    private String token;
    
    @Column(name="ativo")
    private Boolean ativo;

    public Usuario() {
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}