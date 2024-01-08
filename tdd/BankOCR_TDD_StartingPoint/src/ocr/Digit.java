package ocr;


public class Digit {
  private static final String ONE_AS_OCR =  "   " + 
                                            "  |" +  
                                            "  |";

  private static final String TWO_AS_OCR =  " _ " + 
                                            " _|" +  
                                            "|_ ";
  private int digitIndex;
  private DigitAssembler rawInput;

  public Digit(int digitIndex, DigitAssembler rawInput) {
    this.digitIndex = digitIndex;
    this.rawInput = rawInput;
  }

  public int asInt() {
    if (rawInput.assembleDigitAt(digitIndex).equals(ONE_AS_OCR)) return 1;
    if (rawInput.assembleDigitAt(digitIndex).equals(TWO_AS_OCR)) return 2;
    return -1;

  }

}
