package com.viajet.itubus;

import android.app.Application;
import com.onesignal.OneSignal;

public class NotificacaoApplication extends Application {

    private static final String ONESIGNAL_APP_ID = "f860910d-654c-4141-a0a3-8330ac1e457a";

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializa o OneSignal
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // Solicita permissão para notificações push
        OneSignal.promptForPushNotifications();

        // Configurações adicionais para o usuário
        String externalId = "JEaNXy7JpURH4P9FtEXFNJeOto12"; // ID externo para identificar o usuário
        OneSignal.setExternalUserId(externalId);
        OneSignal.setEmail("renangoessantos9@gmail.com");
    }
}
