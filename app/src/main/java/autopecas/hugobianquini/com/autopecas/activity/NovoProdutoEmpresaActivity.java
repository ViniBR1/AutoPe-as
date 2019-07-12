package autopecas.hugobianquini.com.autopecas.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import autopecas.hugobianquini.com.autopecas.R;
import autopecas.hugobianquini.com.autopecas.helper.ConfiguraçãoFirebase;
import autopecas.hugobianquini.com.autopecas.helper.UsuarioFirebase;
import autopecas.hugobianquini.com.autopecas.model.Produto;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao, editProdutoPreco;
    private ImageView imagePerfilProduto;
    private FirebaseAuth autenticacao;
    private String idUsuarioLogado;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String urlImagemSelecionada = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);

        inicializarComponentes();
        storageReference = ConfiguraçãoFirebase.getFirebaseStorage();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagePerfilProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);


                }
            }
        });

    }

    public void validarDadosProduto(View view){

        String nome = editProdutoNome.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();
        String preco = editProdutoPreco.getText().toString();



        if ((!nome.isEmpty())){
            if (!descricao.isEmpty()){
                if (!preco.isEmpty()){


                    Produto produto = new Produto();
                    produto.setIdUsuario(idUsuarioLogado);
                    produto.setUrlImagem(urlImagemSelecionada);
                    produto.setNome(nome);
                    produto.setDescricao(descricao);
                    produto.setPreco(Double.parseDouble(preco));
                    produto.salvar();
                    finish();
                    exibirMensagem("Produto Registrado Com Sucesso");

                }else{ exibirMensagem("Digite o preço para o Produto");}

            }else{ exibirMensagem("Digite uma descrição para o Produto");}
        }else {
            exibirMensagem("Digite um nome para o Produto");
        }


    }

    private void exibirMensagem(String texto){
        Toast.makeText(NovoProdutoEmpresaActivity.this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                localImagem);
                        break;
                }

                if (imagem != null){
                    imagePerfilProduto.setImageBitmap(imagem);
                    //fazendo upload da imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    StorageReference imagemRef = storageReference.child("imagens").child("empresas").child(idUsuarioLogado + "jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NovoProdutoEmpresaActivity.this, "Erro ao fazer Upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            urlImagemSelecionada = String.valueOf(taskSnapshot.getDownloadUrl());
                            Toast.makeText(NovoProdutoEmpresaActivity.this, "Sucesso ao fazer Upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }catch (Exception e) {
                e.printStackTrace();
            }


        }}

    private void inicializarComponentes(){

        imagePerfilProduto = findViewById(R.id.imagePerfilProduto);
        editProdutoNome = findViewById(R.id.editProdutoNome);
        editProdutoDescricao = findViewById(R.id.editProdutoDescrição);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);

    }

}
