package com.mageddo.aptools.textblock;

import org.junit.jupiter.api.Test;

import lombok.TextBlock;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockProcessorTest {

  @Test
  void mustEvaluateTextBlocks(){

    // arrange


    // act

    /*
     * Lorem Ipsum is simply dummy text
     *  of the printing and typesetting
     */
    @TextBlock
    final String text = "";


    // assert

    assertEquals("", text);
  }

}
