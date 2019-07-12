package autopecas.hugobianquini.com.autopecas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import autopecas.hugobianquini.com.autopecas.R;
import autopecas.hugobianquini.com.autopecas.model.Produto;

/**
 * Created by Jamilton
 */

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    private List<Produto> produtos;
    private Context context;

    public AdapterProduto(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Produto produto = produtos.get(i);
        holder.nome.setText(produto.getNome());
        holder.descricao.setText(produto.getDescricao());
        holder.valor.setText("R$ " + produto.getPreco());

        //Carregar Imagem
        String urlImagem = produto.getUrlImagem();

        Picasso.get().load(urlImagem).into(holder.imageProduto);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProduto;
        TextView nome;
        TextView descricao;
        TextView valor;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageProduto = itemView.findViewById(R.id.imageProduto);
            nome = itemView.findViewById(R.id.textNomeRefeicao);
            descricao = itemView.findViewById(R.id.textDescricaoRefeicao);
            valor = itemView.findViewById(R.id.textPreco);
        }
    }
}
