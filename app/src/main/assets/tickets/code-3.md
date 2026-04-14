---
number: 32
title: Валидатор оплаты автобуса/подорожника
keywords: бизнес-логика, уровни архитектуры, многопоточность, распределённая обработка
---

### Класс-валидатор оплаты автобуса/подорожника — списывает стоимость проезда, не списывает если денег недостаточно, не списывает повторно если проезд уже оплачен, обнуляет билет по окончании дня

```java
@RestController
public class BusController {
  public static final long PRICE = 300;
  private final PassengerRepository passengerRepository;
  @Autowired
  public BusController(PassengerRepository passengerRepository) {
    this.passengerRepository = passengerRepository;
  }

  @PostMapping
  public void pay(long passengerId) {
    User passenger = passengerRepository.getById(passengerId);
    if (passenger.getBalance() < PRICE ) {
      throw new NotEnoughMoneyException();
    }
    if (passenger.getLastPaidDay() != null && passenger.getLastPaidDay() == LocalDateTime.now().toLocalDate()) {
      return; // уже оплачено сегодня
    }

    passenger.setBalance(passenger.getBalance() - PRICE);
    passenger.setLastPaidDay(LocalDateTime.now().toLocalDate());
    passengerRepository.save(passenger); 
  }
}
```