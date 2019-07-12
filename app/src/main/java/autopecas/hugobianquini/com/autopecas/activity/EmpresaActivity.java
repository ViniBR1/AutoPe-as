package autopecas.hugobianquini.com.autopecas.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import autopecas.hugobianquini.com.autopecas.R;
import autopecas.hugobianquini.com.autopecas.adapter.AdapterProduto;
import autopecas.hugobianquini.com.autopecas.helper.ConfiguraçãoFirebase;
import autopecas.hugobianquini.com.autopecas.helper.UsuarioFirebase;
import autopecas.hugobianquini.com.autopecas.listener.RecyclerItemClickListener;
import autopecas.hugobianquini.com.autopecas.model.Empresa;
import autopecas.hugobianquini.com.autopecas.model.Produto;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;
    private AlertDialog.Builder dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        inicializarComponentes();
        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguraçãoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Autopeças NI - Empresa");
        setSupportActionBar(toolbar);

        //Configurar RecyclerView

        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutos.setAdapter(adapterProduto);

        //Recupera Produtos para Empresa
        recuperarProdutos();

        //Adicionar evento de clique no RecyclerView
        recyclerProdutos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerProdutos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        dialog = new AlertDialog.Builder(EmpresaActivity.this);
                        dialog.setTitle("Excluir Produto");
                        dialog.setMessage("Deseja realmente excluir este Produto?");
                        dialog.setCancelable(false);
                        dialog.setIcon(android.R.drawable.ic_delete);
                        dialog.setNegativeButton("Não",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       
                                    }
                                });

                        dialog.setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Produto produtoSelecionado = produtos.get(position);
                                        produtoSelecionado.remover();
                                        Toast.makeText(EmpresaActivity.this, "Produto Excluído", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        dialog.create();
                        dialog.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));

    }

    private void recuperarProdutos() {
        DatabaseReference produtosRef = firebaseRef.child("produtos").child(idUsuarioLogado);
        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));
                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair :
                deslogarUsuario();
                break;
            case R.id.menuConfigurações :
                abrirConfigurações();
                break;
            case R.id.menuNovoProduto :
                abrirNovoProduto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deslogarUsuario(){
        try {
            autenticacao.signOut();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirConfigurações(){
        startActivity(new Intent(EmpresaActivity.this, ConfiguracoesEmpresaActivity.class));
    }

    private void abrirNovoProduto(){
        startActivity(new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class));
    }

    private void inicializarComponentes(){
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
    }

}
