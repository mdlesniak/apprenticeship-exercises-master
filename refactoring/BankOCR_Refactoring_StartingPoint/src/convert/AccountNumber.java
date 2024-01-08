package convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountNumber {
  public String textVersion = "";
  public boolean isValid;
  final static int WIDTH_OF_OCR_NUMERAL = 3;
  final static int NUMBER_OF_DIGITS = 9;

  Map<String, String> stickDigitsMappedToNumerals = new HashMap<String, String>();

  public final static String ZERO = " _ | ||_|";
  public final static String ONE = "     |  |";
  public final static String TWO = " _  _||_ ";
  public final static String THREE = " _  _| _|";
  public final static String FOUR = "   |_|  |";
  public final static String FIVE = " _ |_  _|";
  public final static String SIX = " _ |_ |_|";
  public final static String SEVEN = " _   |  |";
  public final static String EIGHT = " _ |_||_|";
  public final static String NINE = " _ |_| _|";

  public AccountNumber(String ocrAccountNumber) {
    stickDigitsMappedToNumerals.put(ZERO, "0");
    stickDigitsMappedToNumerals.put(ONE, "1");
    stickDigitsMappedToNumerals.put(TWO, "2");
    stickDigitsMappedToNumerals.put(THREE, "3");
    stickDigitsMappedToNumerals.put(FOUR, "4");
    stickDigitsMappedToNumerals.put(FIVE, "5");
    stickDigitsMappedToNumerals.put(SIX, "6");
    stickDigitsMappedToNumerals.put(SEVEN, "7");
    stickDigitsMappedToNumerals.put(EIGHT, "8");
    stickDigitsMappedToNumerals.put(NINE, "9");
    this.textVersion = createAccountNumberFromOcr(ocrAccountNumber);
  }

  private String createAccountNumberFromOcr(String ocrToInterpret) {
    ArrayList<String> accountNumberAsOcrDigits1 = new ArrayList<String>();
    for (int digit1 = 0; digit1 < NUMBER_OF_DIGITS; digit1++) {
    
      int startOfFirstLine = (digit1 * WIDTH_OF_OCR_NUMERAL);
      int startOfSecondLine = startOfFirstLine + (WIDTH_OF_OCR_NUMERAL * NUMBER_OF_DIGITS);
      int startOfThirdLine = startOfSecondLine + (WIDTH_OF_OCR_NUMERAL * NUMBER_OF_DIGITS);
    
      String firstLineOfOcrDigit = ocrToInterpret.substring(startOfFirstLine, (startOfFirstLine + WIDTH_OF_OCR_NUMERAL));
      String secondLineOfOcrDigit = ocrToInterpret.substring(startOfSecondLine, (startOfSecondLine + WIDTH_OF_OCR_NUMERAL));
      String thirdLineOfOcrDigit = ocrToInterpret.substring(startOfThirdLine, (startOfThirdLine + WIDTH_OF_OCR_NUMERAL));
    
      String nextDigit = firstLineOfOcrDigit + secondLineOfOcrDigit
          + thirdLineOfOcrDigit;
      accountNumberAsOcrDigits1.add(nextDigit);
    }
    
    ArrayList<String> accountNumberAsOcrDigits = accountNumberAsOcrDigits1;

    String accountNumber = "";

    final int ACCOUNT_NUMBER_LENGTH = accountNumberAsOcrDigits.size();

    for (int digit = 0; digit < ACCOUNT_NUMBER_LENGTH; digit++) {
      accountNumber = accountNumber
          + (stickDigitsMappedToNumerals.get(accountNumberAsOcrDigits.get(digit)));
    }
    
    int checkSumCalculation = 0;
    int currentDigit;
    
    for (int digit = 0; digit < NUMBER_OF_DIGITS; digit++) {
    
      String thisCharacter = accountNumber.substring(digit, digit + 1);
      currentDigit = Integer.parseInt(thisCharacter);
      checkSumCalculation = checkSumCalculation + ((NUMBER_OF_DIGITS - digit) * currentDigit);
    }

    isValid = ((checkSumCalculation % 11) == 0);
    return accountNumber;
  }
}
