package com.metanet.study.mail.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
  private String name;
  private int price;

  public Product(String name, int price) {
    this.name = name;
    this.price = price;
  }

}
