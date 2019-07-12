package autopecas.hugobianquini.com.autopecas.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import autopecas.hugobianquini.com.autopecas.R;
import autopecas.hugobianquini.com.autopecas.helper.ConfiguraçãoFirebase;
import autopecas.hugobianquini.com.autopecas.helper.UsuarioFirebase;


public class AutenticacaoActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button buttonAcesso, buttonCadastro;
    private CheckBox checkBox;
    private TextView textView;
    private TextView textView2;
    private RadioGroup radioGroup;
    private RadioButton radioButton;


    private FirebaseAuth autenticacao;
    private static final String ARQUIVO_PREFERENCIA = "ArqPreferência";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);


        inicializaComponentes();


        buttonAcesso.setVisibility(View.VISIBLE);
        buttonCadastro.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);


        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticacao();


        //verificarUsuariologado();

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String state = String.valueOf(checkBox.isChecked());
                editor.putString("valor", state);
                editor.commit();

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()){
                    if (!senha.isEmpty()){

                        buttonAcesso.setVisibility(View.GONE);
                        buttonCadastro.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);

                        textView2.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.VISIBLE);

                    }}else {
                    Toast.makeText(AutenticacaoActivity.this, "Insira seu Email e Senha para realizar o Cadastro", Toast.LENGTH_LONG).show();
                }


            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(final RadioGroup radioGroup, int i) {




                                                      String email = campoEmail.getText().toString();
                                                      String senha = campoSenha.getText().toString();




                                                      autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<AuthResult> task) {
                                                              if (task.isSuccessful()) {

                                                                  Toast.makeText(AutenticacaoActivity.this, "Usuário Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();
                                                                  String tipoUsuario = getTipoUsuario();
                                                                  UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);
                                                                  abrirTelaPrincipal(tipoUsuario);

                                                              }else{
                                                                  String erroExcessao = "";
                                                                  try {
                                                                      throw task.getException();
                                                                  }catch (FirebaseAuthWeakPasswordException e) {
                                                                      erroExcessao = "Digite uma senha mais forte";
                                                                  }catch (FirebaseAuthInvalidCredentialsException e) {
                                                                      erroExcessao = "Digite um endereço de Email válido";
                                                                  }catch (FirebaseAuthUserCollisionException e){
                                                                      erroExcessao = "Este email já foi cadastrado";
                                                                  } catch (Exception e) {
                                                                      erroExcessao = "ao cadastrar usuário: " + e.getMessage();
                                                                      e.printStackTrace();
                                                                  }

                                                                  Toast.makeText(AutenticacaoActivity.this, erroExcessao, Toast.LENGTH_SHORT).show();
                                                                  buttonAcesso.setVisibility(View.VISIBLE);
                                                                  buttonCadastro.setVisibility(View.VISIBLE);
                                                                  textView.setVisibility(View.VISIBLE);

                                                                  int opcaoEscolhida = radioGroup.getCheckedRadioButtonId();
                                                                  radioButton = findViewById(opcaoEscolhida);
                                                                  radioButton.setChecked(false);

                                                                  textView2.setVisibility(View.GONE);
                                                                  radioGroup.setVisibility(View.GONE);



                                                              }
                                                          }
                                                      });



                                                  }
                                              }
        );



        buttonAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String state = String.valueOf(checkBox.isChecked());
                editor.putString("valor", state);
                editor.commit();


                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()){
                    if (!senha.isEmpty()){

                        autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AutenticacaoActivity.this, "Login Efetuado com Sucesso", Toast.LENGTH_SHORT).show();
                                    String tipoUsuario = task.getResult().getUser().getDisplayName();
                                    abrirTelaPrincipal(tipoUsuario);

                                }else {
                                    Toast.makeText(AutenticacaoActivity.this, "Erro ao fazer Login", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }}
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        if (sharedPreferences.contains("valor")){
            String stateRecuperado = sharedPreferences.getString("valor", "true");
            if (stateRecuperado.equals("true")){
                checkBox.setChecked(true);
            }else {
                checkBox.setChecked(false);
            }
        }

        if (checkBox.isChecked()){
            verificarUsuariologado();
        }

    }


    private void verificarUsuariologado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null){
            String tipoUsuario = usuarioAtual.getDisplayName();
            abrirTelaPrincipal(tipoUsuario);
        }
    }

    private String getTipoUsuario(){
        int opcaoEscolhida = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(opcaoEscolhida);
        String tipoUser = radioButton.getText().toString();
        if (tipoUser.equals("Empresa")){
            String tipoUsuario = "E";
            return tipoUsuario;
        }else {
            String tipoUsuario = "U";
            return tipoUsuario;
        }


    }


    private void abrirTelaPrincipal (String tipoUsuario) {
        if (tipoUsuario.equals("E")){
            Intent intent = new Intent(AutenticacaoActivity.this, EmpresaActivity.class);
            startActivity(intent);
        }else{
            startActivity(new Intent(AutenticacaoActivity.this, HomeActivity.class));
        }}

    private void inicializaComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        buttonAcesso = findViewById(R.id.buttonAcesso);
        buttonCadastro = findViewById(R.id.buttonCadastro);
        checkBox = findViewById(R.id.checkBoxManter);
        textView = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);
        radioGroup = findViewById(R.id.radioGroup);

    }




}
