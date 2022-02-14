package com.example.decimalmaskbrazil;

public class DecimalBrazilUtils implements DecimalMaskBrazilInterface{

    private DecimalBrazilUtils(){}

    public static int getTextNumber(String textNumber){

        int valueNumber;
        try {
            valueNumber = Integer.parseInt(textNumber);
        } catch (Exception e) {
            valueNumber = 0;
        }
        return (valueNumber < 0) ? 0 : Math.min(valueNumber, MAX_HOUSE);
    }

    public static int getIntMaxHouse(){
        return MAX_HOUSE;
    }

    public static String getStringMaxHouse(){
        return Integer.toString(MAX_HOUSE);
    }
}
