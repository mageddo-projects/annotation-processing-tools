package com.mageddo.aptools.textblock;

import org.junit.jupiter.api.Test;

import lombok.TextBlock;
import lombok.TextBlocks;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockProcessorTest {

  @Test
  void mustEvaluateTextBlocks(){

    // arrange


    // act

    /*
     Lorem Ipsum is simply dummy text
     of the printing and typesetting
     */
    @TextBlock
    final String text = TextBlocks.lazyInit();


    // assert

    assertEquals("\n" +
        "     Lorem Ipsum is simply dummy text\n" +
        "     of the printing and typesetting\n" +
        "     ", text);
  }

}
