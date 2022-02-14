package com.example.decimalmaskbrazil;

public interface DecimalMaskBrazilInterface {

    String MASK_DECIMAL = "#,###,###.########";
    String  PERMITTED_CHARACTERS = "0123456789";
    String DECIMAL_SEPARATOR = ",";
    String GROUPING_SEPARATOR = ".";
    char CHAR_GROUPING_SEPARATOR = '.';
    char CHAR_DECIMAL_SEPARATOR = ',';
    int MAX_HOUSE = 8;
    int MAX_LENGTH = 13;
}
