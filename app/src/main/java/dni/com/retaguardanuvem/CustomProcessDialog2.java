package dni.com.retaguardanuvem;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class CustomProcessDialog2 extends Dialog {


    public CustomProcessDialog2(@NonNull Context context){

        super(context);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.rounded);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        View view = LayoutInflater.from(context).inflate(R.layout.carregar_layout, null);
        setContentView(view);


    }

}
