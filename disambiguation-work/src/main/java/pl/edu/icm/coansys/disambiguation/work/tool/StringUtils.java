package pl.edu.icm.coansys.disambiguation.work.tool;

import java.util.Map;

import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */
public abstract class StringUtils {
    
    private StringUtils() {
        throw new IllegalStateException();
    }
    
    /**
     * Returns the trailing integer from the given string or null if the string does not
     * end with number
     * Example:
     * Alice has got a cat 12 - will return 12 (the position of '1')
     * Alice has got a black cat - will return null (no trailing number in the string) 
     */
    public static Integer getTrailingInteger(String str) {
        int positionOfTrailingInteger = getPositionOfTrailingInteger(str);
        if (positionOfTrailingInteger == -1) {
            // string does not end in digits
            return null;
        }
        return Integer.parseInt(str.substring(positionOfTrailingInteger));
    }

    
    /**
     * Returns the position of the first digit in the trailing number of the given string or
     * -1 if the string does not end with number
     * Example:
     * Alice has got a cat 12 - will return 20 (the position of '1')
     * Alice has got a black cat - will return -1 (no trailing number in the string) 
     */
    public static int getPositionOfTrailingInteger(String str) {
        int pos;
        for (pos=str.length()-1; pos>=0; pos--) {
            char c = str.charAt(pos);
            if (!Character.isDigit(c)) break;
        }
        
        if (pos==str.length()-1) {
            return -1;
        }
        
        return pos + 1;
    }
    
    /**
     * XIV - true,
     * MC - true,
     * Mc - true,
     * MRA - false
     */
    public static boolean isRomanNumber(String value) {
        return value.toUpperCase().matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
    }
    
    /**
     * Converts roman number to decimal.
     *  
     * @throws IllegalArgumentException if the number is not a valid roman number, see: {@link StringUtils#isRomanNumber(String)} 
     */
    public static int romanToDecimal(String romanNumber) {
        Preconditions.checkArgument(isRomanNumber(romanNumber));
        
        int decimal = 0;
        int lastNumber = 0;
        String romanNumeral = romanNumber.toUpperCase();
        /* operation to be performed on upper cases even if user enters roman values in lower case chars */
        for (int x = romanNumeral.length() - 1; x >= 0 ; x--) {
            char convertToDecimal = romanNumeral.charAt(x);

            switch (convertToDecimal) {
                case 'M':
                    decimal = processDecimal(1000, lastNumber, decimal);
                    lastNumber = 1000;
                    break;

                case 'D':
                    decimal = processDecimal(500, lastNumber, decimal);
                    lastNumber = 500;
                    break;

                case 'C':
                    decimal = processDecimal(100, lastNumber, decimal);
                    lastNumber = 100;
                    break;

                case 'L':
                    decimal = processDecimal(50, lastNumber, decimal);
                    lastNumber = 50;
                    break;

                case 'X':
                    decimal = processDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;

                case 'V':
                    decimal = processDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;

                case 'I':
                    decimal = processDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;
            }
        }
        return decimal;
    }

    /**
     * If the trailing part of the value is roman number then replaces it with decimal number and
     * returns the changed value, otherwise returns the passed value
     */
    public static String replaceLastRomanNumberToDecimal(String value) {
        if (value==null || !value.contains(" ")) {
            return value;
        }
        String number = value.substring(value.lastIndexOf(" ")).trim();
        if (isRomanNumber(number)) {
            int decimalNumber = romanToDecimal(number);
            return value.substring(0, value.lastIndexOf(" ")+1) + decimalNumber;
        }
        
        return value;
    }
    
    static Map<String, String> wordToDecimal = Maps.newHashMap(); 
    static {
        wordToDecimal.put("ONE", "1");
        wordToDecimal.put("TWO", "2");
        wordToDecimal.put("THREE", "3");
        wordToDecimal.put("FOUR", "4");
        wordToDecimal.put("FIVE", "5");
        wordToDecimal.put("SIX", "6");
        wordToDecimal.put("SEVEN", "7");
        wordToDecimal.put("EIGHT", "8");
        wordToDecimal.put("NINE", "9");
        wordToDecimal.put("TEN", "10");
     }
    
    /**
     * If the trailing part of the value is a string denoting number (one, two, three... ten)
     * then it is replaced with an appropriate number
     */
    public static String replaceLastWordNumberToDecimal(String value) {
        
        if (value==null || !value.contains(" ")) {
            return value;
        }
        String number = value.substring(value.lastIndexOf(" ")).trim().toUpperCase();
        if (isEngWordNumber(number)) {
            return value.substring(0, value.lastIndexOf(" ")+1) + wordToDecimal.get(number);
        }
        
        return value;
    }
    
    public static boolean isEngWordNumber(String value) {
        return value.toUpperCase().matches("ONE|TWO|THREE|FOUR|FIVE|SIX|SEVEN|EIGHT|NINE|TEN");
    }
    
    /** 
     * 1. Removes all characters that are not: letters, digits, spaces and dots, then removes diacritics and lower-cases <br/>
     * 2. Replaces one or more white spaces with one space <br/>
     * 3. Replaces dots with spaces <br/>
     * 4. Removes diacritics <br/>
     * @see DiacriticsRemover#removeDiacritics(String, boolean)
     * 
     * */
    public static String normalize(String title) {
        title = title.replaceAll("[^\\W\\d\\s\\.]", "");
        title = title.replaceAll("\\s+", " ");
        title = title.replaceAll("\\.", " ");
        title = DiacriticsRemover.removeDiacritics(title);
        title = title.toLowerCase();
        return title;
    }
    


    
    //******************** PRIVATE ********************

    private static int processDecimal(int decimal, int lastNumber, int lastDecimal) {
        if (lastNumber > decimal) {
            return lastDecimal - decimal;
        } else {
            return lastDecimal + decimal;
        }
    }

    
}
