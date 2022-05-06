package dni.com.retaguardanuvem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class Ajuda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        //Declaramos um actionBar para obter o suport da ActionBar
        ActionBar actionBar = getSupportActionBar();

        //Ativamos o botão de voltar a tela inicial na actionBar//
        assert  actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Altera o nome da actionBar//
        actionBar.setTitle("Ajuda para Escanear");

        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.BarNavigation)));

    }
    //uma class boolean para definir o botao home/back da actionBar//
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}