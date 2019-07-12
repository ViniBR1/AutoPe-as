package autopecas.hugobianquini.com.autopecas.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import autopecas.hugobianquini.com.autopecas.helper.ConfiguraçãoFirebase;

import static autopecas.hugobianquini.com.autopecas.helper.UsuarioFirebase.getIdUsuario;

public class Empresa implements Serializable {
    private String idUsuario;
    private String urlImagem;
    private String nome;

    private String endereco;
    private String numero;
    private String email;

    public Empresa() {
    }
    public void salvar(){
        DatabaseReference firebaseRef = ConfiguraçãoFirebase.getFirebase();
        DatabaseReference empresaRef = firebaseRef.child("empresa").child(getIdUsuario());
        empresaRef.setValue(this);
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
