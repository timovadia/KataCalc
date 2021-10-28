import java.util.Scanner;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;

public class KataCalc {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        String inputString = scan.nextLine();
        inputString = inputString.replace(" ","");

        ChkChar aChar = new ChkChar();
        char fst = inputString.charAt(0);
        if (aChar.isRomanDigit(fst)) {
            RomanNumber romanNumber = new RomanNumber();
            System.out.println(romanNumber.parsString(inputString));
        } else if (aChar.isArabDigit(fst)) {
            ArabNumber arabNumber = new ArabNumber();
            System.out.println(arabNumber.parsString(inputString));
        } else {
            System.err.println("throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
    }
}

class ChkChar {

    public boolean isRomanDigit(char input){
        return input == 'I' || input == 'V' || input == 'X';
    }

    public boolean isArabDigit(char input){
        return Character.isDigit(input);
    }

    public boolean isOperator(char input){
        return input == '+' || input == '-' || input == '*' || input == '/';
    }

    public boolean isValidNumber(String input){
        int num = Integer.parseInt(input);
        return num > 0 && num < 11;
    }

    public boolean isValidRoman(String input){
        String[] ValuesRomDigits = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};
        return Arrays.asList(ValuesRomDigits).contains(input);
    }
}

class RomanNumber {

    StringBuffer parsedIntegerOne = new StringBuffer(4);
    StringBuffer parsedIntegerTwo = new StringBuffer(4);
    char operator = ' ';
    boolean isSecondInt = false;
    ChkChar chkChar = new ChkChar();
    RomNumConverter romNumConverter = new RomNumConverter();

    public String parsString (String inputString) {

        String resCalc = "";

        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (chkChar.isRomanDigit(c) && !isSecondInt) {
                parsedIntegerOne.append(c);
            } else if (chkChar.isRomanDigit(c) && isSecondInt) {
                parsedIntegerTwo.append(c);
            }
            if (!Character.isDigit(c) && chkChar.isOperator(c)) {
                operator = c;
                isSecondInt = true;
            }
        }
        if (chkChar.isValidRoman(parsedIntegerOne.toString()) && chkChar.isValidRoman(parsedIntegerTwo.toString())) {
            //convert to arab
            int a = romNumConverter.convertRomNumToInteger(parsedIntegerOne.toString());
            int b = romNumConverter.convertRomNumToInteger(parsedIntegerTwo.toString());
            ArabNumber arabNumber = new ArabNumber();
            int calc = arabNumber.MathCalc(a, b, operator);
            resCalc = romNumConverter.convertIntToRomNum(calc);

        }
        return resCalc;
    }
}

class ArabNumber {

    StringBuffer parsedIntegerOne = new StringBuffer(2);
    StringBuffer parsedIntegerTwo = new StringBuffer(2);
    StringBuilder aggregateStr = new StringBuilder(9);

    char operator = ' ';
    boolean isSecondInt = false;
    boolean isSecondOperator = false;
    ChkChar chkChar = new ChkChar();

    public String parsString (String inputString) {

        String resCalc = "";

        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (Character.isDigit(c) && !isSecondInt) {
                parsedIntegerOne.append(c);
                aggregateStr.append(c);
            } else if (Character.isDigit(c) && isSecondInt) {
                parsedIntegerTwo.append(c);
                aggregateStr.append(c);
            }
            if (!Character.isDigit(c) && chkChar.isOperator(c) && !isSecondOperator) {
                operator = c;
                aggregateStr.append(c);
                isSecondInt = true;
                isSecondOperator = true;
            }
        }
        if (operator == ' ') {
            System.err.println("throws Exception //т.к. строка не является математической операцией");
            System.exit(0);
        } else {
            if (!aggregateStr.isEmpty() && !parsedIntegerOne.isEmpty() && !parsedIntegerTwo.isEmpty() && aggregateStr.toString().length() == inputString.length()) {
                if (chkChar.isValidNumber(parsedIntegerOne.toString()) && chkChar.isValidNumber(parsedIntegerTwo.toString())) {
                    int calc = MathCalc(Integer.parseInt(parsedIntegerOne.toString()), Integer.parseInt(parsedIntegerTwo.toString()), operator);
                    resCalc = Integer.toString(calc);
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
        return rightNumeralIntegerValue > leftNumeralIntegerValue ?
                -1 * leftNumeralIntegerValue : leftNumeralIntegerValue;
    }
}