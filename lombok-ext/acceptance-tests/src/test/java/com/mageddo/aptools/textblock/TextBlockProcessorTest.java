package com.mageddo.aptools.textblock;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import lombok.TextBlock;
import lombok.TextBlocks;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockProcessorTest {

  /**
   * Some desc
   */
  @TextBlock
  private String classFieldDesc;

  @Test
  void mustEvaluateTextBlocks(){

    // arrange
    final String someOtherText = "xyz";

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

  @Test
  void mustEvaluateTextBlocksWhenInAnonymousBlocks() {

    // act
    // assert
    {
      // text a
      @TextBlock
      final String text = TextBlocks.lazyInit();
      assertEquals(" text a", text);
    }
    {
      // text b
      @TextBlock
      final String text = TextBlocks.lazyInit();
      assertEquals(" text b", text);
    }

  }

//  @Test
//  void mustEvaluateTextBlocksInsideLambda() {
//
//    // act
//    final String text = process(() -> {
//      // text a
//      @TextBlock
//      final String block = TextBlocks.lazyInit();
//      return block;
//    });
//
//    // assert
//    assertEquals(" text a", text);
//
//  }

  @Test
  void mustEvaluateTextBlocksOnClassField() {

    // act
    // assert
    assertEquals("\n" +
        "   * Some desc\n" +
        "   ", this.classFieldDesc);

  }

  private String process(Supplier<String> o) {
    return o.get();
  }

}
