package ocr;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExtractDigitsFromRawOCRTest {
  private String rawOCRInput;
  private DigitAssembler rawInput;

  @Before
  public void init() {
    rawOCRInput = "    _  _     _  _  _  _  _ " + 
                  "  | _| _||_||_ |_   ||_||_|" +  
                  "  ||_  _|  | _||_|  ||_| _|";
    rawInput = new DigitAssembler(rawOCRInput);
  }
  
  @Test
  public void canParse_FirstDigit_FromRawOCRDigits() throws Exception {
    Digit one = rawInput.getDigit(0);
    assertEquals(1, one.asInt());
  }

  @Test
  public void canParse_SecondDigit_FromOCRDigits() throws Exception {
    Digit two = rawInput.getDigit(1);
    assertEquals(2, two.asInt());
  }
  
  @Test
  public void canParse_SecondDigit_From_TwoOCRDigits() throws Exception {
    Digit one = rawInput.getDigit(0);
    assertEquals(1, one.asInt());
    
    Digit two = rawInput.getDigit(1);
    assertEquals(2, two.asInt());
  }
}

