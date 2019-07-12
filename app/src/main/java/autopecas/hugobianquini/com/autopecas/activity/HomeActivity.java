package autopecas.hugobianquini.com.autopecas.activity;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import autopecas.hugobianquini.com.autopecas.R;
import autopecas.hugobianquini.com.autopecas.adapter.AdapterEmpresa;
import autopecas.hugobianquini.com.autopecas.helper.ConfiguraçãoFirebase;
import autopecas.hugobianquini.com.autopecas.listener.RecyclerItemClickListener;
import autopecas.hugobianquini.com.autopecas.model.Empresa;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    private RecyclerView recyclerEmpresas;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private AdapterEmpresa adapterEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializarComponentes();
        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguraçãoFirebase.getFirebase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Autopeças Nova Iguaçu");
        setSupportActionBar(toolbar);

        //Configurar RecyclerView

        recyclerEmpresas.setLayoutManager(new LinearLayoutManager(this));
        recyclerEmpresas.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerEmpresas.setAdapter(adapterEmpresa);

        //Recupera Empresas
        recuperarEmpresas();

        //Configurando SearchView
        searchView.setHint("Pesquisar Empresas/Lojas");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEmpresas(newText);
                return true;
            }
        });

        recyclerEmpresas.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerEmpresas, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Empresa empresaSelecionada = empresas.get(position);
                        Intent i = new Intent(HomeActivity.this, ProdutosActivity.class);
                        i.putExtra("empresa", empresaSelecionada);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                })
        );

    }

    private void pesquisarEmpresas(String pesquisa){
        DatabaseReference empresaRef = firebaseRef.child("empresa");
        Query query = empresaRef.orderByChild("nome").startAt(pesquisa).endAt(pesquisa + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empresas.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    empresas.add(ds.getValue(Empresa.class));
                }

                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void recuperarEmpresas(){
        DatabaseReference empresaRef = firebaseRef.child("empresa");
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empresas.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    empresas.add(ds.getValue(Empresa.class));
                }

                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);


        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarComponentes(){
        searchView = findViewById(R.id.materialSearchView);
        recyclerEmpresas = findViewById(R.id.recyclerEmpresas);
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
        startActivity(new Intent(HomeActivity.this, ConfiguracoesUsuarioActivity.class));
    }
}
