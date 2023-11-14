package de.unipassau.fim.fsinfo.kdv.data.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "KDV_ShopItem")
public class ShopItem {

  @Id
  @Column(nullable = false)
  private String id;
  private String category;
  @Column(nullable = false)
  private String displayName;
  private Double price;
  private Boolean displayed;

  public ShopItem(String id, String category, String displayName, double price) {
    this.id = id;
    this.category = category;
    this.displayName = displayName;
    this.price = price;
    this.displayed = true;
  }

  @Deprecated
  public ShopItem() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Boolean getDisplayed() {
    return displayed;
  }

  public void setDisplayed(Boolean displayed) {
    this.displayed = displayed;
  }
}
