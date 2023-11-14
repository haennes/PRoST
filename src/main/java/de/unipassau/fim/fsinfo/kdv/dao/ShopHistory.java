package de.unipassau.fim.fsinfo.kdv.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;

@Entity(name = "KDV_ShopHistory")
public class ShopHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String itemId;

  @Column(nullable = false)
  private Double price;

  @Column(nullable = false)
  private Long timestamp;

  public ShopHistory(Long userId, String itemId, Double price) {
    this.userId = userId;
    this.itemId = itemId;
    this.price = price;
    this.timestamp = Instant.now().getEpochSecond();
  }

  /**
   * Do not use to create new entries. Unless Timestamp is added manually
   */
  @Deprecated
  public ShopHistory() {
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public Long getId() {
    return id;
  }
}
