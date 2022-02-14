package com.example.decimalmaskbrazil;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TextWatcherDecimalBrazil implements TextWatcher, DecimalMaskBrazilInterface{

    private WeakReference<EditText> editTextWeakReference;
    private final Locale locale = new Locale("pt", "BR");
    private DecimalFormatSymbols formatSymbols;
    private DecimalFormat decimalFormat;

    public void setHouse(int house) {
        this.house = Math.max(0, Math.min(house, MAX_HOUSE));
        if(decimalFormat != null) decimalFormat.setMaximumFractionDigits(house);
    }

    private int house;

    public TextWatcherDecimalBrazil(WeakReference<EditText> editTextWeakReference, int house){
        this.editTextWeakReference = editTextWeakReference;
        setHouse(house);
        setupMyKeyListener();
    }

    private void setupMyKeyListener() {
        this.editTextWeakReference.get().setKeyListener(DigitsKeyListener.getInstance(PERMITTED_CHARACTERS+DECIMAL_SEPARATOR));
        this.editTextWeakReference.get().setOnLongClickListener(v -> {
            editTextWeakReference.get().setText("");
            return false;
        });

        formatSymbols = new DecimalFormatSymbols(locale);
        formatSymbols.setDecimalSeparator(CHAR_DECIMAL_SEPARATOR);
        formatSymbols.setGroupingSeparator(CHAR_GROUPING_SEPARATOR);
        decimalFormat = new DecimalFormat(MASK_DECIMAL, formatSymbols);
        decimalFormat.setMaximumFractionDigits(house);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        new Handler(Looper.getMainLooper()).post(()->{
            if (editTextWeakReference.get() == null || s == null || s.toString().isEmpty()) return;

            if (s.toString().equals(DECIMAL_SEPARATOR)) {
                setEditText(s.toString(), true, false);
            } else {
                String originalText = duplicateSeparator(s.toString());

                int lengthHouse = sizeDecimal(originalText);
                if(lengthHouse > house){
                    int dif = lengthHouse - house;
                    originalText = originalText.substring(0, originalText.length() - dif);
                }

                boolean lastComma = lastComma(originalText);
                boolean hasHouse = house != 0;

                if(!hasHouse && lastComma){
                    originalText = originalText.substring(0, originalText.length()-1);
                }

                String text = getStringDecimalNumber(originalText);

                if (!lastComma) text = formatNumber(originalText, text);

                if(text.length() > getMaxLength()){
                    text = text.substring(0, getMaxLength());
                    text = getStringDecimalNumber(text);
                }
                setEditText(text, false, hasHouse && lastComma);
            }
        });
    }

    private void setEditText(String text, boolean isEmpty, boolean lastComma){
        StringBuilder stringBuilder = new StringBuilder(isEmpty ? "0" : "");
        stringBuilder.append(text);

        if(lastComma){
            stringBuilder.append(CHAR_DECIMAL_SEPARATOR);
        }

        editTextWeakReference.get().removeTextChangedListener(this);
        editTextWeakReference.get().setText(stringBuilder.toString());
        editTextWeakReference.get().addTextChangedListener(this);
        editTextWeakReference.get().setSelection(stringBuilder.toString().length());
    }

    private String convertForNumberDefault(String number){
        return number.replaceAll("\\.", "").replace(DECIMAL_SEPARATOR, GROUPING_SEPARATOR);
    }

    private boolean lastComma(String text){
        if(text == null || text.isEmpty()) return false;
        return text.charAt(text.length() - 1) == CHAR_DECIMAL_SEPARATOR;
    }

    private int sizeDecimal(String text){
        if(text == null || text.isEmpty() || !text.contains(DECIMAL_SEPARATOR)){
            return 0;
        }else{
            String result = text.substring(text.indexOf(CHAR_DECIMAL_SEPARATOR));
            return result.length() > 0 ? result.length() - 1 : 0;
        }
    }

    private String duplicateSeparator(String text){
        if(text == null || text.isEmpty()) return text;

        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;

        for (char ch: text.toCharArray()){
            if(ch != CHAR_DECIMAL_SEPARATOR || count == 0){
                stringBuilder.append(ch);
            }

            if(ch == CHAR_DECIMAL_SEPARATOR) count++;
        }

        return stringBuilder.toString();
    }

    private String formatNumber(String numberComma, String decimalPoint){
        if(!numberComma.contains(DECIMAL_SEPARATOR)) return decimalPoint;

        String fractionComma = numberComma.substring(numberComma.indexOf(CHAR_DECIMAL_SEPARATOR));
        String integerDecimal = decimalFormat.format(new BigDecimal(convertForNumberDefault(decimalPoint)).toBigInteger());

        return integerDecimal + fractionComma;
    }

    private String getStringDecimalNumber(String text){

        if(text == null) return "0";

        BigDecimal number = new BigDecimal(convertForNumberDefault(text));
        return decimalFormat.format(number);
    }

    private int getMaxLength(){

        if( editTextWeakReference == null || editTextWeakReference.get() == null || editTextWeakReference.get().getText() == null ) return 0;
        for (InputFilter filter : editTextWeakReference.get().getFilters()) {
            if (filter instanceof InputFilter.LengthFilter) {
                return ((InputFilter.LengthFilter) filter).getMax();
            }
        }

        return 0;
    }
}
