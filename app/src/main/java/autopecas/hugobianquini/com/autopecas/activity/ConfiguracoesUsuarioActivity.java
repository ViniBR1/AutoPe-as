package autopecas.hugobianquini.com.autopecas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import autopecas.hugobianquini.com.autopecas.R;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações Usuário");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
