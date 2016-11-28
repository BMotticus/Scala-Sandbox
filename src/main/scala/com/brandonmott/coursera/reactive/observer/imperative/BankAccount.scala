package com.brandonmott.coursera.reactive.observer.imperative

/**
  * A bank account has [[deposit]] and [[withdraw]] methods, and it maintains a private variable [[balance]].
  * 
  * Every time we change the state of the bank account we need to invoke `publish()`.
  * So add `publish()` once in both [[deposit]] and [[withdraw]] methods
  * 
  * Use `publish()` during `state updates` otherwise nobody would ever know about changes in the bank account.
  *
  * BankAccount created by Brandon Mott on 9/25/16.
  */
class BankAccount extends Publisher {
  private var balance = 0

  /**
    * The [[currentBalance]] is an `accessor` method that provides the `current state` of the variable balance
    * @return current state of the balance
    */
  def currentBalance: Int = balance

  /**
    * The [[deposit]] method adds some amount to the balance
    * @param amount added to [[balance]]
    */
  def deposit(amount: Int): Unit =
    if (amount > 0) {
      balance = balance + amount
      publish()
    }

  /**
    * The [[withdraw]] method's subtracts some amount from the [[balance]]
    * @param amount
    */
  def withdraw(amount: Int): Unit =
    if (0 < amount && amount <= balance) {
      balance = balance - amount
      publish()
    } else throw new Error("insufficient funds")
}
