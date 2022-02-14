package com.example.decimalmaskbrazil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private View viewRoot;
    private EditText editText;
    private EditText editTextNormal;
    private TextWatcherDecimalBrazil textWatcherDecimalBrazil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewRoot = getWindow().getDecorView();
        setConfigure();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    private void setConfigure() {

        editText = viewRoot.findViewById(R.id.decimalNumber);
        editTextNormal = viewRoot.findViewById(R.id.decimalQualquer);
        textWatcherDecimalBrazil = new TextWatcherDecimalBrazil(new WeakReference<>(editText), 4);
        editText.addTextChangedListener(textWatcherDecimalBrazil);

        editTextNormal.setHint("n° casas decimais (máx #)".replace("#", DecimalBrazilUtils.getStringMaxHouse()));
        editTextNormal.addTextChangedListener(new TextWatcher() {
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
                        editText.setText(editText.getText());
                    } else {
                        int valueNumber = DecimalBrazilUtils.getTextNumber(s.toString());
                        String textNumber = Integer.toString(valueNumber);

                        editTextNormal.removeTextChangedListener(this);
                        editTextNormal.setText(textNumber);
                        editTextNormal.addTextChangedListener(this);
                        editTextNormal.setSelection(textNumber.length());

                        textWatcherDecimalBrazil.setHouse(valueNumber);
                        editText.setText(editText.getText());
                    }
                });
            }
        });
    }


}