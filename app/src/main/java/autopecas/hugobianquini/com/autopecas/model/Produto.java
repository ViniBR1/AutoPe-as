package autopecas.hugobianquini.com.autopecas.model;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import autopecas.hugobianquini.com.autopecas.helper.ConfiguraçãoFirebase;

public class Produto{

    private String idUsuario;
    private String nome;
    private String descricao;
    private Double preco;
    private String idProduto;
    private String urlImagem;


    public Produto() {
        DatabaseReference firebaseref = ConfiguraçãoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseref.child("produtos");
        setIdProduto(produtoRef.push().getKey());
    }

    public void salvar(){
        DatabaseReference firebaseref = ConfiguraçãoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseref.child("produtos").child(getIdUsuario()).child(getIdProduto());
        produtoRef.setValue(this);
    }

    public void remover(){
        DatabaseReference firebaseref = ConfiguraçãoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseref.child("produtos").child(getIdUsuario()).child(getIdProduto());
        produtoRef.removeValue();
    }



    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
