package com.example.patterns_banking.models;

import com.example.patterns_banking.models.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String number;

  @Enumerated(EnumType.STRING)
  private AccountType type;

  private BigDecimal balance;

  private Boolean isActive = true;

  private Account(Builder builder) {
    this.id = builder.id;
    this.number = builder.number;
    this.type = builder.type;
    this.balance = builder.balance;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Long id;
    private String number;
    private AccountType type;
    private BigDecimal balance;

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder number(String number) {
      this.number = number;
      return this;
    }

    public Builder type(AccountType type) {
      this.type = type;
      return this;
    }

    public Builder balance(BigDecimal balance) {
      this.balance = balance;
      return this;
    }

    public Account build() {
      return new Account(this);
    }
  }
}
