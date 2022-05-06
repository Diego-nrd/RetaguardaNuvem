package dni.com.retaguardanuvem;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Lista extends AppCompatActivity {

    //Chamamos e declaramos RecyclerView RecycleAdapter e RecyclerView.Layout//
    RecyclerView recyclerView;
    RecycleAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    //Declaramos uma variavel dados em String para armazenar os textos, cada virgula é um novo campos no layout//
    String[] dados = {"Desenvolvido por Diego Sacramento da Silva.\n" +
            "\n" +
            " Atenção é Proibida a Venda ou a Utilização Comercial, este Software não tem Garantia Este Software foi Desenvolvido afins de Estudos, não para a Comercialização.\n" +
            "\n" +
            " Todas as Lincenças de Código e de Imagens, logo etc, Foram Declarados abaixo. Caso o seu código ou Imagen não foi Declarada Por favor entre em Contado.\n" +
            "\n" +
            "\t\t\tInstagram:@diego.scaletta",
            //Linceça Dni //
            "Dni Autocom Informática 2022, Utilização de Imagens  - Automação e Segurança eletrônica - Todos os direitos reservados",

            //Licença Image-Crop//
            "Copyright 2016, Arthur Teplitzki, 2013, Edmodo, Inc.\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:\n" +
            "\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n"+
            "Unless required by applicable law or agreed to in writing,software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.",

            //Licença Zxing Scanner//
            "Copyright (C) 2012-201 ZXing authors, Journey Mobile\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License.\n" +
            "You may obtain a copy of the License at\n" +
            "\n" +
            "    http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            "See the License for the specific language governing permissions and limitations under the License.",

            //Licença SpinKit//
            "Copyright © 2016 ybq \n" +
            "Permission is hereby granted, free of charge, \n" +
            "to any person obtaining a copy of this software and associated documentation files (the “Software”),\n" +
            "to deal in the Software without restriction, \n" +
            "including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,\n" +
            "and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\n",

            //Licença IonAlert//
            "Copyright 2020 ionalert\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at\n" +
            "\n" +
            " http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and\n" +
            "limitations under the License.",

            //Licença FireBase//
            "Copyright [2022] [Fire Base, google.com]\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License.\n" +
            "You may obtain a copy of the License at\n" +
            "\n" +
            "    http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and\n" +
            "limitations under the License.",
            //Lincença android-gif-drawable//
            "Copyright (c) 2013 - present Karol Wrótniak, Droids on Roids LLC\n" +
            "\n" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal\n" +
            "in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
            "copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included in\n" +
            "all copies or substantial portions of the Software."};

    //Declaramos uma variavel titulos em String para armazenar os textos, cada virgula é uma novo titulo no layout//
    String[] titulos = {"Sobre","Dni Autocom Informática","Image Cropper", "Zxing", "Android-SpinKit", "Ionalert", "Fire Base", "Android-gifs-Drawable"};

    //Declaramos uma nova variavel image int, para armazenar as fotos, cada virgula representa um campo no layout//
    int[] image = {R.drawable.ic_sobre, R.drawable.ic_copyright, R.drawable.ic_copyright, R.drawable.ic_copyright, R.drawable.ic_copyright, R.drawable.ic_copyright, R.drawable.ic_copyright, R.drawable.ic_copyright};


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        //Declaramos um actionBar para obter o suporte da ActionBar//
        ActionBar actionBar = getSupportActionBar();

        //Ativamos o botão de voltar a tela inicial na actionBar//
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Altera a o nome da actionbar//
        actionBar.setTitle("Sobre e Licenças");

        //Altera a cor da actionbar//
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.BarNavigation)));

        //Chamamos e apontamos a recyclerView//
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleAdapter(this, dados, titulos, image);
        recyclerView.setAdapter(adapter);
    }

    //uma class bollean para definir o icone do botão home/back da actionBar//
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Altera a cor do NavigationBar(Barra de Navegação)//
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            getWindow().setNavigationBarColor(this.getResources().getColor(R.color.black));
        }

    }
}
