package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.onesignal.OneSignal;
import com.viajet.itubus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificacaoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);

        // Envia uma notificação ao abrir esta atividade
        enviarNotificacaoAutomatica();
    }

    private void enviarNotificacaoAutomatica() {
    try {
        // Cria o conteúdo da notificação
        JSONObject notificationContent = new JSONObject();
        notificationContent.put("contents", new JSONObject().put("en", "Você abriu a tela de notificações!"));
        notificationContent.put("headings", new JSONObject().put("en", "Notificação Automática"));
        notificationContent.put("included_segments", new JSONArray().put("All")); // Envia para todos os dispositivos

        // Envia a notificação com o handler de resposta
        OneSignal.postNotification(notificationContent, new OneSignal.PostNotificationResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                // Executa o Toast na thread principal
                runOnUiThread(() ->
                    Toast.makeText(NotificacaoActivity.this, "Notificação enviada com sucesso", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onFailure(JSONObject response) {
                // Executa o Toast na thread principal
                runOnUiThread(() ->
                    Toast.makeText(NotificacaoActivity.this, "Erro ao enviar notificação", Toast.LENGTH_SHORT).show()
                );
            }
        });
    } catch (JSONException e) {
        e.printStackTrace();
        Toast.makeText(this, "Erro ao criar conteúdo da notificação", Toast.LENGTH_SHORT).show();
    }
}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Fecha a atividade e retorna para a anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
