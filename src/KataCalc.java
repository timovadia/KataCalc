/*
 * Copyright (c) 2021, timovadia. All rights reserved. 
 */

import java.util.Scanner;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;

/**
 * This is implementation of the technical requirement -- console application Calculator.
 *
 * @author tim.berezin
 * @version 1.0
 */
public class KataCalc {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String inputString = scan.nextLine();
        
        inputString = inputString.replace(" ","");

        if (inputString.isEmpty()) {
            System.err.println("throws Exception //т.к. пустая строка");
            System.exit(0);
        } else {
            CalcString calcString = new CalcString();
            System.out.println(calcString.parsString(inputString));
        }
    }
}

class ChkChar {
    public boolean isRomanDigit(char input) {
        return input == 'I' || input == 'V' || input == 'X';
    }
    
    public boolean isOperator(char input) {
        return input == '+' || input == '-' || input == '*' || input == '/';
    }

    public boolean isValidArabNumber(String input) {
        String[] ValuesArabDigits = { "1","2","3","4","5","6","7","8","9","10" };
        return Arrays.asList(ValuesArabDigits).contains(input);
    }

    public boolean isValidRomanNumber(String input) {
        String[] ValuesRomDigits = { "I","II","III","IV","V","VI","VII","VIII","IX","X" };
        return Arrays.asList(ValuesRomDigits).contains(input);
    }
}

class CalcString {
    StringBuffer parsedIntegerOne = new StringBuffer(4);
    StringBuffer parsedIntegerTwo = new StringBuffer(4);
    StringBuilder aggregateStr = new StringBuilder(9);

    char operator = ' ';
    boolean isSecondInt = false;
    boolean isSecondOperator = false;

    ChkChar chkChar = new ChkChar();
    RomNumConverter romNumConverter = new RomNumConverter();

    public String parsString (String inputString) {
        String resCalc = "";

        try {
            for (int i = 0; i < inputString.length(); i++) {
                char strChar = inputString.charAt(i);
                if ((Character.isDigit(strChar) || chkChar.isRomanDigit(strChar)) && !isSecondInt) {
                    parsedIntegerOne.append(strChar);
                    aggregateStr.append(strChar);
                } else if ((Character.isDigit(strChar) || chkChar.isRomanDigit(strChar)) && isSecondInt) {
                    parsedIntegerTwo.append(strChar);
                    aggregateStr.append(strChar);
                }
                if (chkChar.isOperator(strChar) && !isSecondOperator) {
                    operator = strChar;
                    aggregateStr.append(strChar);
                    isSecondInt = true;
                    isSecondOperator = true;
                }
            }
        } catch (NumberFormatException e){
            System.err.println(e.getMessage());
        }

        if (operator == ' ') {
            System.err.println("throws Exception //т.к. строка не является математической операцией");
            System.exit(0);
        } else {
            //все ли числа и оператор распарсены?
            if (!aggregateStr.isEmpty() && !parsedIntegerOne.isEmpty() && !parsedIntegerTwo.isEmpty() && aggregateStr.toString().length() == inputString.length()) {

                //IntegerOne is it arabic?
                if (chkChar.isValidArabNumber(parsedIntegerOne.toString())) {
                    if (chkChar.isValidArabNumber(parsedIntegerTwo.toString())) {
                        //calculate arabic
                        int calc = MathCalc(Integer.parseInt(parsedIntegerOne.toString()), Integer.parseInt(parsedIntegerTwo.toString()), operator);
                        resCalc = Integer.toString(calc);
                    } else if (chkChar.isValidRomanNumber(parsedIntegerTwo.toString())) {
                        System.err.println("throws Exception //т.к. используются одновременно разные системы счисления");
                        System.exit(0);
                    } else {
                        System.err.println("throws Exception //т.к. введены неподходящие числа");
                        System.exit(0);
                    }
                } else if (chkChar.isValidRomanNumber(parsedIntegerOne.toString())) {
                    if (chkChar.isValidRomanNumber(parsedIntegerTwo.toString())) {
                        //calculate roman
                        //convert to arab
                        int a = romNumConverter.convertRomNumToInteger(parsedIntegerOne.toString());
                        int b = romNumConverter.convertRomNumToInteger(parsedIntegerTwo.toString());
                        int calc = MathCalc(a, b, operator);
                        if (calc > 0) {
                            resCalc = romNumConverter.convertIntToRomNum(calc);
                        } else {
                            System.err.println("throws Exception //т.к. в римской системе нет нуля и отрицательных чисел");
                            System.exit(0);
                        }
                    } else if (chkChar.isValidArabNumber(parsedIntegerTwo.toString())) {
                        System.err.println("throws Exception //т.к. используются одновременно разные системы счисления");
                        System.exit(0);
                    } else {
                        System.err.println("throws Exception //т.к. введены неподходящие числа");
                        System.exit(0);
                    }
                } else { //При вводе пользователем неподходящих чисел приложение выбрасывает исключение и завершает свою работу
                    System.err.println("throws Exception //т.к. введены неподходящие числа");
                    System.exit(0);
                }
            } else {
                System.err.println("throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
                System.exit(0);
            }
        }
        return resCalc;
    }

    public int MathCalc (int a, int b, char op) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> 0;
        };
    }
    
    /*
    int MathCalc (int a, int b, char op) {
        int res = switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> 0;
        };
        return res;
    }
     */
}

class RomNumConverter {
    private final Map<String, Integer> numberByNumeral;
    
    public RomNumConverter() {
        Map<String, Integer> numberByNumeral = new LinkedHashMap<>();
        numberByNumeral.put("M", 1000);
        numberByNumeral.put("CM", 900);
        numberByNumeral.put("D", 500);
        numberByNumeral.put("CD", 400);
        numberByNumeral.put("C", 100);
        numberByNumeral.put("XC", 90);
        numberByNumeral.put("L", 50);
        numberByNumeral.put("XL", 40);
        numberByNumeral.put("X", 10);
        numberByNumeral.put("IX", 9);
        numberByNumeral.put("V", 5);
        numberByNumeral.put("IV", 4);
        numberByNumeral.put("I", 1);
        this.numberByNumeral = Collections.unmodifiableMap(numberByNumeral);
    }

    public String convertIntToRomNum(Integer arabicInteger) {
        StringBuilder romanNumeralsBuilder = new StringBuilder();
        int remainder = arabicInteger;
        for (Entry<String, Integer> numeralKeyArabicValue : numberByNumeral.entrySet()) {
            while (remainder >= numeralKeyArabicValue.getValue()) {
                romanNumeralsBuilder.append(numeralKeyArabicValue.getKey());
                remainder -= numeralKeyArabicValue.getValue();
            }
        }
        return romanNumeralsBuilder.toString();
    }

    public Integer convertRomNumToInteger(String romanNumeralsString) {
        Integer total = 0;
        String lastNumeral = "";
        char[] romanNumerals = romanNumeralsString.toUpperCase().toCharArray();
        for (int i = romanNumerals.length - 1; i > -1; i--) {
            String numeral = String.valueOf(romanNumerals[i]);
            total += getIntegerValueFromAdjacentNumerals(numeral, lastNumeral);
            lastNumeral = numeral;
        }
        return total;
    }

    private Integer getIntegerValueFromAdjacentNumerals(String leftNumeral, String rightNumeral) {
        Integer leftNumeralIntegerValue = numberByNumeral.get(leftNumeral);
        int rightNumeralIntegerValue = "".equals(rightNumeral) ? 0 : numberByNumeral.get(rightNumeral);
        return (rightNumeralIntegerValue > leftNumeralIntegerValue) ? -1 * leftNumeralIntegerValue : leftNumeralIntegerValue;
    }
}
