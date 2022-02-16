package com.example.decimalmaskbrazil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.decimalmaskbrazil.databinding.ActivityMainBinding;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binder;
    private TextWatcherDecimalBrazil textWatcherDecimalBrazil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

        setConfigure();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    private void setConfigure() {

        textWatcherDecimalBrazil = new TextWatcherDecimalBrazil(new WeakReference<>( binder.decimalNumber), 0);
        binder.decimalNumber.addTextChangedListener(textWatcherDecimalBrazil);

        binder.decimalQualquer.setHint("n° casas decimais (máx #)".replace("#", DecimalBrazilUtils.getStringMaxHouse()));
        binder.decimalQualquer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                new Handler(Looper.getMainLooper()).post(()->{
                    if(s == null) {
                        return;
                    } else if (s.toString().isEmpty()) {
                        textWatcherDecimalBrazil.setHouse(0);
                        binder.decimalNumber.setText( binder.decimalNumber.getText());
                    } else {
                        int valueNumber = DecimalBrazilUtils.getTextNumber(s.toString());
                        String textNumber = Integer.toString(valueNumber);

                        binder.decimalQualquer.removeTextChangedListener(this);
                        binder.decimalQualquer.setText(textNumber);
                        binder.decimalQualquer.addTextChangedListener(this);
                        binder.decimalQualquer.setSelection(textNumber.length());

                        textWatcherDecimalBrazil.setHouse(valueNumber);
                        binder.decimalNumber.setText( binder.decimalNumber.getText());
                    }
                });
            }
        });
    }


}