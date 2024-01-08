package ocr;


public class DigitAssembler {
  private static final int DIGIT_WIDTH = 3;
  private static final int STRIPES_PER_DIGIT = 3;
  
  private final String rawOCRInput;
  private int totalCharsInRawInput;

  private int startingOffset;
  private int totalDigits;

  public DigitAssembler(String rawOCRInput) {
    this.rawOCRInput = rawOCRInput;
    totalCharsInRawInput = rawOCRInput.length();
    totalDigits = totalCharsInRawInput / (DIGIT_WIDTH * STRIPES_PER_DIGIT);
  }

  public Digit getDigit(int digitIndex) {
    return new Digit(digitIndex, this);
  }

  public String assembleDigitAt(int digitIndex) {
    startingOffset = digitIndex * DIGIT_WIDTH;
    
    return stripe(0) + stripe(1) + stripe(2);
  }

  private String stripe(int stripeNumber) {
    int startingIndex = startingOffset + (totalDigits * DIGIT_WIDTH * stripeNumber);
    
    return rawOCRInput.substring(startingIndex, startingIndex + DIGIT_WIDTH);
  }

}
