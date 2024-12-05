

 package com.viajet.itubus.activity.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.viajet.itubus.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


public class LocalizaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        MapView mapView = findViewById(R.id.mapview);


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
        GeoPoint startPoint = new GeoPoint(-23.259282, -47.310374);
        mapView.getController().setCenter(startPoint);
        mapView.getController().setZoom(13);

    }


}
/**
package com.viajet.itubus.activity.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.viajet.itubus.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class LocalizaoActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private Marker userMarker;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        // Configuração inicial do Osmdroid
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));

        // Inicializar mapa
        mapView = findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Solicitar permissão de localização
        solicitarPermissaoLocalizacao();

        // Adicionar marcadores fixos
        adicionarMarcadoresFixos(this);
    }

    private void solicitarPermissaoLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Mostrar explicação antes de solicitar permissão
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permissão Necessária")
                        .setMessage("Este aplicativo precisa de acesso à sua localização para mostrar sua posição no mapa.")
                        .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1))
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            } else {
                // Solicita a permissão diretamente
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            iniciarLocalizacao(this); // Permissão já concedida
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarLocalizacao(this); // Inicia a localização se a permissão foi concedida
            } else {
                Toast.makeText(this, "Permissão de localização negada. O recurso não funcionará corretamente.", Toast.LENGTH_LONG).show();
            }
        }
    }

  private void iniciarLocalizacao(Context context) {
        Drawable userIcon = ContextCompat.getDrawable(this, R.drawable.ic_people);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Atualizar marcador
                    if (userMarker != null) {
                        mapView.getOverlays().remove(userMarker);
                    }
                    userMarker = new Marker(mapView);
                    userMarker.setPosition(new GeoPoint(latitude, longitude));
                    userMarker.setTitle("Localização do usuário");
                    userMarker.setIcon(userIcon);
                    mapView.getOverlays().add(userMarker);
                    mapView.getController().setCenter(new GeoPoint(latitude, longitude));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    private void adicionarMarcadoresFixos(Context context) {
        List<OverlayItem> items = new ArrayList<>();
        Drawable busIcon = ContextCompat.getDrawable(this, R.drawable.ic_bus_locate);
        Drawable busPointIcon = ContextCompat.getDrawable(this, R.drawable.ic_bus_point);

        // Adicionando marcadores fixos
        items.add(new OverlayItem("Ônibus", "Localização do ônibus", new GeoPoint(-23.280576, -47.290167)));
        items.add(new OverlayItem("Ponto", "Ponto de espera do ônibus", new GeoPoint(-23.289342, -47.295933)));

        items.get(0).setMarker(busIcon);
        items.get(1).setMarker(busPointIcon);

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

        ItemizedIconOverlay<OverlayItem> itemizedOverlay = new ItemizedIconOverlay<>(this, items, gestureListener);
        mapView.getOverlays().add(itemizedOverlay);
        mapView.invalidate(); // Atualize o mapa
    }
}

 */
