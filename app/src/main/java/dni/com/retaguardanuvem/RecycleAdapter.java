package dni.com.retaguardanuvem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    //Chamamos e declaramos as Seguintes Variaveis Context, dados, titulos e images//
    Context context;
    String[] dados;
    String[] titulos;
    int[] images;


    //Apontamos cada Variaveis que ira Armazenar os dados//
    public RecycleAdapter(Context context, String[] dados, String[]titulos, int[]images){
        this.dados = dados;
        this.titulos = titulos;
        this.context = context;
        this.images = images;
    }

    //Chamamos o RecyclerView com o ViewHolder//
    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_desing, parent, false);
        return new ViewHolder(view);

    }

    //Chamamos o RecyclerView para armazenar os dados no diplay espesificando a posição de cada//
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ViewHolder holder, int position) {
        //para cada textView sera implementado um dado e um titulo em cada holde(espaço)//
        holder.DadosView.setText(dados[position]);
        holder.TitulosView.setText(titulos[position]);
        holder.imageView.setImageResource(images[position]);

    }

    //Retorna todos os dados//
    @Override
    public int getItemCount() {
        return dados.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Chamamos e declaramos os TextView e ImageView//
        TextView DadosView;
        TextView TitulosView;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Apontamos cada TextView e ImageView para seu representante na layout//
            DadosView = itemView.findViewById(R.id.id_dados);
            TitulosView = itemView.findViewById(R.id.id_titulo);
            imageView = itemView.findViewById(R.id.id_image);

        }
    }
}
