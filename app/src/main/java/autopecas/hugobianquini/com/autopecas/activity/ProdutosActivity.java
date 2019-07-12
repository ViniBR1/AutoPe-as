package autopecas.hugobianquini.com.autopecas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import autopecas.hugobianquini.com.autopecas.R;
import autopecas.hugobianquini.com.autopecas.adapter.AdapterProduto;
import autopecas.hugobianquini.com.autopecas.helper.ConfiguraçãoFirebase;
import autopecas.hugobianquini.com.autopecas.model.Empresa;
import autopecas.hugobianquini.com.autopecas.model.Produto;

public class ProdutosActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutosEmpresa;
    private ImageView imageEmpresa;
    private TextView textEmpresaProdutos;
    private Empresa empresaSelecionada;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        inicializarComponentes();
        firebaseRef = ConfiguraçãoFirebase.getFirebase();

        //Recuperar empresa selecionada
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            empresaSelecionada = (Empresa)bundle.getSerializable("empresa");
            idEmpresa = empresaSelecionada.getIdUsuario();//id da empresa
            textEmpresaProdutos.setText(empresaSelecionada.getNome());
            String url = empresaSelecionada.getUrlImagem();

            if(url != ""){
                Picasso.get().load(url).into(imageEmpresa);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lista de Produtos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurar RecyclerView

        recyclerProdutosEmpresa.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutosEmpresa.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutosEmpresa.setAdapter(adapterProduto);

        //Recupera Produtos para Empresa
        recuperarProdutos();
    }

    private void inicializarComponentes(){
        recyclerProdutosEmpresa = findViewById(R.id.recyclerProdutosEmpresa);
        imageEmpresa = findViewById(R.id.imageEmpresa);
        textEmpresaProdutos = findViewById(R.id.textNomeEmpresaProdutos);
    }

    private void recuperarProdutos() {
        DatabaseReference produtosRef = firebaseRef.child("produtos").child(idEmpresa);
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

}
