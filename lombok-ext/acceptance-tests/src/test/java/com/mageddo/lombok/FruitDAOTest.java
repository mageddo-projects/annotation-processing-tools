package com.mageddo.lombok;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FruitDAOTest {

  private final FruitDAO fruitDAO = new FruitDAO();

  @Test
  void mustGenerateTextBlockForFruitSql(){
    // arrange

    // act
    final String sql = fruitDAO.getFindFruitsSql();

    // assert
    assertEquals("\n    SELECT * FROM FRUITS;\n     ", sql);
  }
}
