package dni.com.retaguardanuvem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class Inicio extends AppCompatActivity {
    //Variavel Timer para apresentação do gif//
    private Timer sTimer;
    //Variavel Delay, que ira definir o tempo da apresentação na Tela//
    private static final long Delay = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //oculta a barra que contém o nome do aplicativo//
        getSupportActionBar().hide();

        //Declaramos que sTimer é o tempo da permanencia da tela//
        sTimer = new Timer();
        //Função para iniciar a apresentação primeiro e depois o aplicativo//
        sTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Inicio.this, MainActivity.class);
                Inicio.this.startActivity(intent);
                Inicio.this.finish();
            }
        }, Delay);
    }

    //Condição de ocultar a barra de noitifcação e de mostrar//
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hideSystemUi();
        }else{
            showSystemUi();
        }
    }
    //Mostra a barra de notificação removendo todos os Sinalizadores//
    //Exceto para aqueles que fazem o contéudo aparecer na barra do sistema//
    private void showSystemUi(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    private void hideSystemUi(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                //Tela Imersiva aderente//
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                //Config para manter estável quando, o contéudo ser redimensionado--//
                //-E quando a barra de notificação oculta aparecer--//
                //-Utilizamos o método LAYOUT_FULLSCREEN para a imagem preencher a tela total//
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                //Esconde a barra de notificação e seus status e barra de navegação//
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        //Modo Tela cheia com corte baseado na posição da camera independete da posição da mesma//
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setNavigationBarColor(this.getResources().getColor(R.color.colorNavigation));
        }
    }
}