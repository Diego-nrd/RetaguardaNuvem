package dni.com.retaguardanuvem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.Objects;


import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    //Solitações da Camera e Galeria Armazenamento//
    public static final int GALLERY_REQUEST = 103;
    public static final int STORAGE_REQUEST = 101;
    public static final int CAMERA_REQUEST = 100;
    public static final int IMAGE_PICK_CAMERA = 102;


    //Inicializa a Permissão da camera e do Armazenamento do dispositivo//
    private String[] cameraPermissions;
    private String[] storagePermission;

    public EditText cod;
    public EditText nome;
    public EditText desc;
    public ImageView img;
    public Button btn;
    public Button btn2;
    public Button btn3;
    public Button btn4;
    public Button btn5;
    public Button btn6;
    public Button btn7;

    //Chamamos as classes do DataBaseRealtime e Storage, para armazenar imagens e dados//
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    FirebaseDatabase mDatabase;

    //Uma Classe Uri para pega a Uri da Imagem//
    private Uri imageUri;

    //Resultado ao Scanear o código de barra//
    ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Scaneamento Cancelado");
                        Toast.makeText(this, "Scanner Cancelado", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Digite o código no campo Código manualmente ou clique no botão scan", Toast.LENGTH_LONG).show();

                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Scaneamento Cancelado devido a falta de permisão da camera!!");
                        Toast.makeText(MainActivity.this, "Cancelado devido a falta de permissão da camera!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("MainActivity", "Scaneado com Sucesso!");
                    //Captura os dados scaneado//
                    cod.setText(result.getContents());
                    //Convertemos os dados em String e declaramos uma nova variavel(cod)//
                    String codbar = cod.getText().toString().trim();
                    if (codbar.equals("")) {

                    } else {
                        buscaProdut(codbar);
                    }
                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Chamamos os campos e botões da activity e declaramos um novo nome aos campos e botoes//
        cod = findViewById(R.id.id_codigo);
        nome = findViewById(R.id.id_nome);
        desc = findViewById(R.id.id_descricao);
        img = findViewById(R.id.id_img);
        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.delete);
        btn5 = findViewById(R.id.bscan);
        btn6 = findViewById(R.id.sobre);
        btn7 = findViewById(R.id.lupa_bnt);

        //Permissão da camera e do armazenameto//
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Oculta a barra que contém o nome do aplicativo//
        Objects.requireNonNull(getSupportActionBar()).hide();

        //chamamos o Database e Storage com suas instancia//
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();

        //Inicializa o scanner automaticamente//
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(false);
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        options.setCaptureActivity(Scanner.class);
        barcodeLauncher.launch(options);


        //Ação do botão para cadastrar o Produto//
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Capturamos o texto digitado e convertemos em string//
                String codbar = cod.getText().toString();
                String nomep = nome.getText().toString();
                String descp = desc.getText().toString();

                //Verificar se os campos estão vazio caso esteja uma mensagem sera apresentada//
                if (codbar.equals("") | nomep.equals("") | descp.equals("")) {
                    Toast.makeText(MainActivity.this, "Alungs campos estão vazio Revise e Preencha!!", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(MainActivity.this, "Selecione uma foto do produto", Toast.LENGTH_SHORT).show();
                } else {
                    inserirP();
                }
            }
        });

        //Ação do botão de selecionar imagens//
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PegarImagem();
            }
        });

        //Ação do botão de buscar o Produto//
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codbar = cod.getText().toString();
                if (!codbar.isEmpty()) {
                    buscaProdut(codbar);
                } else {
                    Toast.makeText(MainActivity.this, "Campo código está vazio!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Ação do botão para Limpar os campos digitado//
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codbar = cod.getText().toString();
                String nomep  = nome.getText().toString();
                String descp  = desc.getText().toString();
                //caso não tiver nenhum dados digitado//
                if (codbar.isEmpty() & nomep.isEmpty() & descp.isEmpty() & imageUri==null) {
                    Toast.makeText(MainActivity.this, "Não há nenhum campo para ser limpo!", Toast.LENGTH_SHORT).show();
                }else{
                    //um novo IonAlert para perguntar se deseja limpar os campos//
                    new IonAlert(MainActivity.this, IonAlert.WARNING_TYPE)
                        .setTitleText("Atenção")
                        .setContentText("Tem certeza que deseja limpar todos os campos?")
                        .setCancelText("Não")
                        .setConfirmText("Sim")
                        .setConfirmClickListener(new IonAlert.ClickListener() {
                            @Override
                             public void onClick(IonAlert ionAlert) {
                               cod.setText("");
                               nome.setText("");
                               desc.setText("");
                               img.setImageURI(Uri.EMPTY);
                               //um IonAlert em caso de confirmação//
                               ionAlert
                               .setTitleText("Limpo!")
                               .setContentText("Todos campos Limpos com Sucesso!!!")
                               .showCancelButton(false)
                               .setConfirmClickListener(null)
                               .changeAlertType(IonAlert.SUCCESS_TYPE);

                             }
                    }).show();
                }
            }
        });
        //Ação do botão para Deletar o Produto//
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codbar = cod.getText().toString();
                if (!codbar.isEmpty()) {
                    DeleteProdut(codbar);
                } else {
                    Toast.makeText(MainActivity.this, "Não há Nenhum Código Digitado!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Botão Scanner//
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inicializa o Scanner com as configurações//
                ScanOptions options = new ScanOptions()
                        .setOrientationLocked(false)
                        .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                        .setCaptureActivity(Scanner.class);
                barcodeLauncher.launch(options);
            }
        });

        //Botão Sobre//
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Sobre = new Intent(MainActivity.this, Lista.class);
                MainActivity.this.startActivity(Sobre);
            }
        });

        //Botão pesquisar//
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesquisa = cod.getText().toString();
                if (pesquisa.equals("")) {
                    //caso o campo código estiver vazio ira imprimir uma mensagem de erro//
                    Toast.makeText(MainActivity.this, "Não foi possível realizar a pesquisa. O campo código está vazio!!!", Toast.LENGTH_SHORT).show();
                } else {
                    //caso tenha alguma coisa digitada a pesquisa sera realizada//
                    buscanet(pesquisa);
                }
            }
        });
    }

    //Inserir Produto//
    public void inserirP() {
        //Verificação da Conexão de Internet//
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NotActiveNetWork = manager.getActiveNetworkInfo();
        //Verificação se o Dispositivo Não tem Internet Imprime a mensagem //
        if (null == NotActiveNetWork) {
            Toast.makeText(MainActivity.this, "Este Dispositivo Não Está Conectado a Internet, Verifique a sua Conexão e Tente Novamente", Toast.LENGTH_LONG).show();
            onStop();

        } else {
            //Capturamos o texto digitado e convertemos em string//
            String codbar = cod.getText().toString();
            String nomep = nome.getText().toString();
            String descp = desc.getText().toString();

            //Criamos a referencia do banco de Dados chamado Produtos para armazenar os dados //
            mRef = mDatabase.getReference().child("Produtos");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //verifica se o código de barra ja está cadastrado//
                    if (snapshot.child(codbar).exists()) {
                        Toast.makeText(MainActivity.this, "Produto Já Cadastrado!!!", Toast.LENGTH_SHORT).show();
                        //caso o código não estiver cadastrado prossiga//
                    } else {
                        //Ao salvar os dados a tela de save ira iniciar//
                        final CustomProcessDialog dialog = new CustomProcessDialog(MainActivity.this);
                        dialog.show();

                        //Criamos a referencia do banco de Dados chamado produto/image para armazenar as imagnes//
                        StorageReference filepath = mStorage.getReference().child("produto/image").child(imageUri.getLastPathSegment());
                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Caso tenha sucesso em capturar imagem fara o donwlaod da Url da imagem//
                                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        //Uma nova referencia em base do  que for digidado no campo cod sera a chave primaria//
                                        DatabaseReference newPost = mRef.child(codbar);
                                        //Dados a ser inseridos//
                                        newPost.child("cod").setValue(codbar);
                                        newPost.child("nomepro").setValue(nomep);
                                        newPost.child("descpro").setValue(descp);
                                        newPost.child("image").setValue(task.getResult().toString());

                                        //Limpando os dados digitado//
                                        cod.setText("");
                                        nome.setText("");
                                        desc.setText("");
                                        img.setImageURI(Uri.EMPTY);

                                        Toast.makeText(MainActivity.this, "Aguarde...", Toast.LENGTH_SHORT).show();

                                        //Ao concluir o salvamento a tela de salvameto sera encerrada//
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Produto Cadastrado com Sucesso!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 200);
                                    }
                                });
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void buscaProdut(String cod) {
        //Verificação se o Dispositivo Não tem Internet//
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NotActiveNetWork = manager.getActiveNetworkInfo();

        //Caso não tenha Internet Imprima uma mensagem de aviso//
        if (null == NotActiveNetWork) {
            Toast.makeText(MainActivity.this, "Este Dispositivo Não Está Conectado a Internet, Verifique a sua Conexão e Tente Novamente", Toast.LENGTH_LONG).show();
        } else {
            //Referencia do banco de dados da nuvem//
            mRef = FirebaseDatabase.getInstance().getReference("Produtos");

            //Realizaremos a busca no banco de dados com o codigo de barra//
            mRef.child(cod).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    //caso tenha sucesso na busca//
                    if (task.isSuccessful()) {
                        // se existir o codigo buscado carrega seus perspectivos dados//
                        if (task.getResult().exists()) {
                            //Ao realizar a busca a tela de carregamento sera ativada//
                            final CustomProcessDialog2 dialog = new CustomProcessDialog2(MainActivity.this);
                            dialog.show();

                            DataSnapshot dataSnapshot = task.getResult();

                            //Buscamos os dados no banco de dados em base na colunas especificada//
                            String nomep = String.valueOf(dataSnapshot.child("nomepro").getValue());
                            String descp = String.valueOf(dataSnapshot.child("descpro").getValue());
                            String imagep = String.valueOf(dataSnapshot.child("image").getValue());

                            //Carregamos os dados do banco nos campos textView e imageView//
                            nome.setText(nomep);
                            desc.setText(descp);
                            //Chamamos a class Picasso para carregar a imagem no campo ImageView//
                            Picasso.get().load(imagep).into(img);

                            Toast.makeText(MainActivity.this, "Aguarde...", Toast.LENGTH_SHORT).show();

                            //Apos terminar todos os dados serem carregado a tela sera encerrada//
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Dados Carregados com Sucesso!!", Toast.LENGTH_SHORT).show();
                                }
                            }, 2800);

                        } else {
                            Toast.makeText(MainActivity.this, "Produto não cadastrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    public void DeleteProdut(String cod) {
        //Verificação se o Dispositivo Não tem Internet//
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NotActiveNetWork = manager.getActiveNetworkInfo();
        //Caso não tenha Internet Imprima uma mensagem de aviso//
        if (null == NotActiveNetWork) {
            Toast.makeText(MainActivity.this, "Este Dispositivo Não Está Conectado a Internet, Verifique a sua Conexão e Tente Novamente", Toast.LENGTH_LONG).show();
        } else {
            mRef = FirebaseDatabase.getInstance().getReference();
            Query query = mRef.child("Produtos").orderByChild("cod").equalTo(cod);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        //caso o produto existir no sistema ele ira pergunta se deseja excluir//
                        new IonAlert(MainActivity.this, IonAlert.WARNING_TYPE)
                            .setTitleText("Atenção")
                            .setContentText("Tem certeza que deseja deletar este produto?")
                            .setCancelText("Não")
                            .setConfirmText("Sim")
                            .setConfirmClickListener(new IonAlert.ClickListener() {
                            //caso o cliente aperte o sim ira aparecer a mensagem de deletado com sucesso//
                            @Override
                               public void onClick(IonAlert ionAlert) {
                               ionAlert
                               .setTitleText("Deletado!")
                               .setContentText("Produto deletado com sucesso!!")
                               .setConfirmText("Ok")
                               .showCancelButton(false)
                               .setConfirmClickListener(null)
                               .changeAlertType(IonAlert.SUCCESS_TYPE);
                            //um for para deletar o dato desejado pelo codigo //
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    snapshot1.getRef().removeValue();
                                    Toast.makeText(MainActivity.this, "Excluido com Sucesso!!", Toast.LENGTH_SHORT).show();
                                    //Limpa os campos //
                                    nome.setText("");
                                    desc.setText("");
                                    img.setImageURI(Uri.EMPTY);
                                }
                               }
                            })
                            .show();
                    } else {
                        Toast.makeText(MainActivity.this, "Código Não Pertence a Nenhum Produto!!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled", error.toException());
                }
            });
        }
    }

    //Os codigos demais abaixo são da permissão da camera e do armazenamento e Crooped para cortar a imagem//
    //Checar a permissao do armazenamento//
    private boolean checkStoragePermisssion() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public void requestStoragePermission() {
        //Solicitação de Permissao de armazenamento//
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST);

    }

    //Checar a permissao da Camera//
    public boolean checkCameraPermission() {
        //Checa se a permissao da camera esta ativada ou desativada//
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    //Solicitação da Permissao de Camera//
    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST);
    }


    //Pegar a Imagem do Armazenamento
    private void pickFromStorage() {
        //Intent para escolher uma foto da galeria a imagem ira retornar no metodo Main//
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK);
        //Obeter apenas Imagnes//
        galeriaIntent.setType("image/*");
        startActivityForResult(galeriaIntent, GALLERY_REQUEST);
    }

    //Capturar Imagem da Camera do Dispositivo//
    private void pickFromCamera() {
        //Intente para capturar uma foto da camera a imagme ira retornar no metodo mains//
        ContentValues valor = new ContentValues();
        valor.put(MediaStore.Images.Media.TITLE, "Titulo Imagem");
        valor.put(MediaStore.Images.Media.DESCRIPTION, "Imagem Descrição");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valor);

        //Intente para Abrir a camera para Tirar a foto//
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Resultado das permissao permitida/negada//
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    // caso seja permitido retorne true caso seja negada retorne false//
                    boolean cameraAceita = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean galeriaAceita = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAceita && galeriaAceita) {
                        //se ambas permissao foram permitida//
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Permissão Camera & Galeria Requiridos", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    //caso seja permitido retorne true caso negada retorne false//
                    boolean galeriaAceita = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (galeriaAceita) {
                        //galeria permissao aceita//
                        pickFromStorage();
                    } else {
                        Toast.makeText(this, "Permisão Galeria Requerida", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //A imagem escolhida da galeria ou da camera sera recebida aqui//
        if (resultCode == RESULT_OK) {
            //Imagem escolhida//
            //Escolhida da galeria//
            if (requestCode == GALLERY_REQUEST) {
                //Recortar a Imagem//
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle("Recortar")
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setBorderCornerColor(Color.BLUE)
                        .start(this);
                // Image escolhida da camera//
            } else if (requestCode == IMAGE_PICK_CAMERA) {
                //Recortar Image//
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle("Recortar")
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setBorderCornerColor(Color.BLUE)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //Imagem recordade recebida//
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                imageUri = resultUri;
                //Definir Image//
                img.setImageURI(resultUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Criamos uma classe Item que contem o nome da opções e os icones do Dialog//
    public static class Item {
        public final String text;
        public final int icon;

        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    //Criamos a classe para selecionar a Imagem, Tirar da Camera ou da galeria do dispositivo//
    public void PegarImagem() {
        //Declaramos uma final com as opções e os icones de cada opção no Display//
        final Item[] items = {
                new Item("Câmera", R.drawable.ic_camera),
                new Item("Galeria", R.drawable.ic_foto)
        };
        //Criamos uma ListAdapter com nome options e chamamos a activity e o editext//
        ListAdapter options = new ArrayAdapter<Item>(this,
                R.layout.dialog_item,
                R.id.text1,
                items) {
            //Criamos uma View Publica com as posições
            public View getView(int position, View convertView, ViewGroup parent) {
                //Utilizamos a super Class para criar o View//
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(R.id.text1);

                //Colocamos a imagem no textView obs, a position.icon deve ter os valores do top, right e bottom em 0//
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                //Define o Espaço entre a imagem e o nome das opções//
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                return view;
            }
        };
        //Dialog que é responsavél pelas opções//
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Titulo//
        builder.setTitle("Escolha uma Imagem");
        //Icone do titulo do Dialog//
        builder.setIcon(R.drawable.dni3);
        builder.setAdapter(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    //Camera Selecionada Verificar a Permissões//
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        //caso a permissão esteja garantida//
                        pickFromCamera();
                    }
                } else if (i == 1) {
                    //Galeria Selecionada Verificar as Permissões//
                    if (!checkStoragePermisssion()) {
                        requestStoragePermission();
                    } else {
                        //caso a permissão esteja garantida//
                        pickFromStorage();
                    }
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded2);

    }

    //Realiza busca pela internet//
    private void buscanet(String words) {
        //Verifica se o Dispositvo não está conectado a uma rede movel/wifi//
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NotNetworkInfo = manager.getActiveNetworkInfo();
        if (null == NotNetworkInfo) {
            Toast.makeText(MainActivity.this, "Este Dipositivo Não Está Conectado a Internet, Verifique a sua Conexão e Tente Novamente!!", Toast.LENGTH_LONG).show();
        } else {
            //caso o dispositivo esteja conectado a busca ira proseguir//
            try {
                //a busca sera pelo navegador//
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                //busca as palavras digitadas pelo usuario//
                intent.putExtra(SearchManager.QUERY, words);
                //inicia a activity intent//
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Realizando a Pesquisa, Por favor Aguarde.....", Toast.LENGTH_SHORT).show();
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                //caso não tiver o app da google usara o navegador do dispositivo//
                buscaNetCompact(words);
            }
        }
    }

    //Busca pelo navegador do dispositivo, caso não tiver o app do google//
    private void buscaNetCompact(String words) {
        try {
            //Utiliza a URI para busca no google com as palavras digitada do usuario//
            Uri uri = Uri.parse("http://google.com/search?q=" + words);
            //Criar uma nova Intent que ira abrir o navegador do dispositivo//
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //Inicia a nova Intent//
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Encontrou um erro//
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }


    //Condição para ocultar ou mostrar a barra de notificação//
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUi();
        } else {
            showSystemUi();
        }
    }

    //Esconde Barra de Notificação
    private void hideSystemUi() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                //Tela Imersiva aderente//
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        //Config para manter a tela estável quando o contéudo ser redminsionado-//
                        //-e quando a barra de notificação oculta aparecer-//
                        //- usamos o método LAYOUT_FULLSCREEN para a Imagem preencher a tela//
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN

        );
        //Modo Tela cheia com corte com base na posição da camera independente da posição da camera//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setNavigationBarColor(this.getResources().getColor(R.color.colorNavigation));
        }
    }

    //Mostra a Barra de Notificação Removendo todos os Sinalizadores//
    //Exceto para aqueles que fazem contéudo aparecer na barra do Sistemas//
    private void showSystemUi() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }
}