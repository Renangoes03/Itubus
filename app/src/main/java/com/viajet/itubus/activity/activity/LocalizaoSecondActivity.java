package com.viajet.itubus.activity.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.viajet.itubus.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.ArrayList;
import java.util.List;

public class LocalizaoSecondActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        MapView mapView = findViewById(R.id.mapview);
        Context context = this;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        // Configuração inicial do Osmdroid
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));

        // Configuração do mapa
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Configurar ponto específico no mapa
        IMapController mapController = mapView.getController();
        GeoPoint startPoint = new GeoPoint(-23.265891, -47.300541);
        mapController.setZoom(15);
        mapController.setCenter(startPoint);

        //Criando uma lista dos marcadores no mapa
        List<OverlayItem> items = new ArrayList<>();
        items.add(new OverlayItem("Passageiro", "Localização do usuário", new GeoPoint(-23.265891, -47.300541)));
        items.add(new OverlayItem("Ônibus", "Localização do ônibus", new GeoPoint(-23.260470, -47.304511)));
        items.add(new OverlayItem("Ponto", "Ponto de espera do ônibus", new GeoPoint(-23.267221, -47.299694)));

        //Criando os ícones dos marcadores
        Drawable busIcon = ContextCompat.getDrawable(this, R.drawable.ic_bus_locate);
        Drawable userIcon = ContextCompat.getDrawable(this, R.drawable.ic_people);
        Drawable busPointIcon = ContextCompat.getDrawable(this, R.drawable.ic_bus_point);

        //Colocando os ícones nos marcadores
        items.get(1).setMarker(busIcon);
        items.get(0).setMarker(userIcon);
        items.get(2).setMarker(busPointIcon);

        //Configurando a ação ao pressionar ou tocar um marcador
        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> gestureListener =
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        Toast.makeText(context, "Clicou no: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true; // Indica que o evento foi tratado
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        Toast.makeText(context, "Segurou no: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true; // Indica que o evento foi tratado
                    }
                };

        ItemizedIconOverlay<OverlayItem> itemizedOverlay = new ItemizedIconOverlay<>(
                this,
                items,
                gestureListener
        );

        mapView.getOverlays().add(itemizedOverlay);
        mapView.invalidate(); // Atualize o mapa

    }
}
