package dni.com.retaguardanuvem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.Objects;

public class Scanner extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    //Declarando os Elementos ToggleButton e demais Elementos//
    private ToggleButton flash;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ViewfinderView viewfinderView;
    private ToggleButton back;
    private Button ajuda;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        //Configurações da camera como foco e sena do scanner//
        CameraSettings mCameraSettings = new CameraSettings();
        mCameraSettings.setFocusMode(CameraSettings.FocusMode.CONTINUOUS);
        mCameraSettings.setAutoFocusEnabled(true);
        mCameraSettings.setBarcodeSceneModeEnabled(true);

        //Oculta a barra que contém o nome do aplicativo//
        Objects.requireNonNull(getSupportActionBar()).hide();//



        //Chama o scanner na activity scanner//
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setCameraSettings(mCameraSettings);


        //Chama o flash e o botão volta na activity scanner//
        flash = findViewById(R.id.Flash);
        back = findViewById(R.id.back);
        ajuda = findViewById(R.id.Questoes);


        //chama a tela customizavel na activity custom_barcode//
        viewfinderView = findViewById(R.id.zxing_viewfinder_view);


        //Verifica se o dispositivo tem flash//
        if(!hasFlash()){
            flash.setVisibility(View.GONE);
        }

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setShowMissingCameraPermissionDialog(false);
        capture.decode();

        changeMaskColor(null);
        changeLaserVisibility(true);

        //Botão de ligar e desligar o flash do dispositivo//
        flash.setOnClickListener(view -> {
            if(flash.isChecked()){
                barcodeScannerView.setTorchOn();
            }else{
                barcodeScannerView.setTorchOff();
            }
        });

        //Botão responsavel por volta a tela de inicio//
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Botão para entra na activity ajuda//
        ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent ajuda = new Intent(Scanner.this, Ajuda.class);
              Scanner.this.startActivity(ajuda);
            }
        });

    }


    @Override
    protected void onResume() {
        capture.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        capture.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        capture.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public boolean hasFlash(){
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    //mascara que ficara em volta da tela do scanner//
    public void  changeMaskColor(View view){
        int color = getColor(R.color.fundo);
        viewfinderView.setMaskColor(color);
    }

    //Habilita o laser do scanner//
    public void changeLaserVisibility(Boolean visible){
        viewfinderView.setLaserVisibility(visible);
    }

    //botao que entra na tela ajuda//
    public void Ajuda(View view){

    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //Condição de Alterar a cor da barra de status e da barra de navegação//

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            getWindow().setNavigationBarColor(this.getResources().getColor(R.color.BarNavigation));
        }

    }
}