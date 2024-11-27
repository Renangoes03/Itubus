package com.viajet.itubus.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viajet.itubus.R;
import com.viajet.itubus.activity.model.Recarga;

import java.util.List;

public class RecargaAdapter extends RecyclerView.Adapter<RecargaAdapter.RecargaViewHolder> {

    private final List<Recarga> recargas;

    // Construtor do adaptador
    public RecargaAdapter(List<Recarga> recargas) {
        this.recargas = recargas;
    }

    @NonNull
    @Override
    public RecargaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recarga, parent, false);
        return new RecargaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecargaViewHolder holder, int position) {
        Recarga recarga = recargas.get(position);

        // Configurando os valores com segurança contra valores nulos
        holder.data.setText("Data: " + (recarga.getData() != null ? recarga.getData() : "N/A"));
        holder.horario.setText("Horário: " + (recarga.getHorario() != null ? recarga.getHorario() : "N/A"));

        // Para campos numéricos
        holder.valor.setText("Valor: R$ " + recarga.getValor()); // `double` nunca será nulo
        holder.credito.setText("Crédito: R$ " + recarga.getCredito()); // `double` nunca será nulo
        holder.viagens.setText("Viagens: " + recarga.getQuantidadeViagem()); // `int` nunca será nulo
    }

    @Override
    public int getItemCount() {
        return recargas.size();
    }

    // Classe ViewHolder para vincular os componentes de cada item
    public static class RecargaViewHolder extends RecyclerView.ViewHolder {

        private final TextView data;
        private final TextView horario;
        private final TextView valor;
        private final TextView credito;
        private final TextView viagens;

        public RecargaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializa os componentes do layout do item
            data = itemView.findViewById(R.id.txtData);
            horario = itemView.findViewById(R.id.txtHorario);
            valor = itemView.findViewById(R.id.txtValor);
            credito = itemView.findViewById(R.id.txtCredito);
            viagens = itemView.findViewById(R.id.txtViagens);
        }
    }
}
