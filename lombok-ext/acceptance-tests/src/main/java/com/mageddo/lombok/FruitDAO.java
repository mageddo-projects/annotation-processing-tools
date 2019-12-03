package com.mageddo.lombok;

import lombok.TextBlock;
import lombok.TextBlocks;

public class FruitDAO {

  private final String someField = "";

  public String getFindFruitsSql(){
    /*
    SELECT * FROM FRUITS;
     */
    @TextBlock
    final String sql = TextBlocks.lazyInit();
    return sql;
  }
}
