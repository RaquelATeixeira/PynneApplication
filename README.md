# 🏦 Bank Aggregator - Banking Data Aggregation System

> **Intelligent banking data aggregation system** that unifies information from multiple banks through a robust and scalable hexagonal architecture.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple.svg)](https://alistair.cockburn.us/hexagonal-architecture/)

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Dependency Injection](#-dependency-injection)
- [Tests](#-test)
- [Build layout](#-build-layout)
- [Running the App](#-running-the-app)

## 🎯 Overview

Hello Par, welcome to my challange resolution! Hope u like it.
Here I'm going to explain my line of thought and also my choices to acomplish the requirements.

## 🏗 Architecture

I choose Hexagonal architecture (Ports & Adapters) because it was the most natural way to make heterogeneous integrations look the same without leaking their details into the app.

I used these layers to make Bank1 and Bank2 look the same logically. I didn’t try to make Bank1 look like Bank2, or the other way around. Instead, I defined our own port (BankClient) and mapped both banks to it.

#### **1. Domain:** plain models (AccountBalance, Transaction). No framework.
  - Bank1 has no currency → we set "UNKNOWN"; Bank2 provides currency.
  - Bank1: type is int (1/2) → map to "CREDIT"/"DEBIT"; text → description.
  - Bank2: type is enum → map to the same strings; text stays in description.
#### **2. Port:** interface BankClient = the abstraction the app uses to talk to “any bank”.
  - long accountId because your vendor code uses primitive long (not String).
  - fromDate/toDate included because Bank2 requires a date range; Bank1 can ignore or accept it (we call the 3-arg variant).
  - Return types are our domain objects, not vendor ones.
  - ```
    public interface BankClient {
        String bankId();
        List<AccountBalance> getBalances(long accountId); 
        List<Transaction> getTransactions(long accountId, Date fromDate, Date toDate);
    }
    ```
#### **3. Adapters:** one per bank (Bank1Adapter, Bank2Adapter). Each translates from the vendor’s native API to our domain models.
- Bank1Adapter calls Bank1AccountSource.getAccountBalance(long) and getTransactions(long, Date, Date) and maps.
- Bank2Adapter calls Bank2AccountSource.getBalance(long) and getTransactions(long, Date, Date) and maps.
- Upstream differences are contained inside adapters.
Controllers/services don’t need to know about those differences
#### **4. Application:** AggregationService orchestrates calls across all BankClient implementations.
- AggregationService gets all BankClients.
- No vendor imports here; it depends only on the port.
#### **5 . Web:** BankController exposes REST and only calls the service (never vendors).
- It only calls AggregationService. It never sees vendor classes.


## Dependency Injection
* Adapters are Spring @Components implementing BankClient
* AggregationService is @Service that receives List<BankClient> by constructor → DI.
* BankController receives the service by constructor → DI.

This means that adding a Bank3 only requires creating a Bank3Adapter that implements BankClient. Spring wires it automatically, and no other layer needs to change.

## Tests

I divided the tests into three segments:

* Unit tests for business logic.
* Controller tests to validate the REST contract.
* Contract tests for Adapter to ensure each adapter properly translates vendor data into the unified domain model.
* Architecture tests, that protects the intended hexagonal architecture.
  - As a bonus, I used ArchUnit to check the structural rules of the codebase.

## Build layout

I chose multi-module Maven layout (vendors / core / adapters / web) instead of keeping everything in one big module, because enforces architecture at build time, not just by convention.
Why? The challenge requires strict isolation, If you put everything in a single module, the compiler will let you import com.bank1.* anywhere. The only safeguard would be discipline or ArchUnit rules.

```
bank-aggregator/ (parent pom)
├─ vendors/   ← “black box” for com.bank1 / com.bank2 (I didn't change the code)
├─ core/      ← domain, ports, application (pure logic)
├─ adapters/  ← Bank1Adapter, Bank2Adapter (the only place that imports vendors)
└─ web/       ← Spring Boot app (controllers + main)
```

## 🚀 Running the App

### Prerequisites
- Java 21+
- Maven 3.9+

### Build & Test
```bash
mvn clean install
```

### Run
```bash
cd web
mvn spring-boot:run
```

### Rest Endpoints
```bash
/api/customers/{id}/balances
/api/customers/{id}/transactions?from=...&to=...
```
You can use parameter from as 2025-01-01T00:00:00 and 2025-02-01T00:00:00Z as parameter to