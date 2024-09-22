package com.viajet.itubus.activity.activity;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.viajet.itubus.databinding.ActivityLocateBinding;

import com.viajet.itubus.R;

public class LocateActivity extends AppCompatActivity
    implements OnMapReadyCallback{

    private GoogleMap mMap;

    private AppBarConfiguration appBarConfiguration;
    private ActivityLocateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLocateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_locate);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Mudar exibição do mapa
        mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );

        // Add a marker in Sydney and move the camera
        LatLng ibirapuera = new LatLng(-23.587097, -46.657635);
        //-23.587097, -46.657635

        mMap.addMarker(
                new MarkerOptions()
                        .position( ibirapuera )
                        .title("Ibirapuera")
                        /*.icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                        )*/
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.botao_onibus))
        );
        mMap.moveCamera(//2.0 até 21.0
                CameraUpdateFactory.newLatLngZoom(ibirapuera,18)
        );

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_locate);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}