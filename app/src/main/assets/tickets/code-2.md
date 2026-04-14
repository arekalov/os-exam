---
number: 31
title: REST API перевода денег / быстрых платежей
keywords: Конфигурация Spring Boot с профилями dev/prod — профиль dev с БД на сервере aqua/helios и heap 2GB, профиль prod с БД на боевом сервере и heap 16GB
---

### REST API перевода денег / быстрых платежей — Spring MVC REST контроллер перевода денег, проверка что месячные переводы не превышают 100 000 р, гарантия доставки средств


```java
@RestController
public class MoneyController {
  final UserRepository userRepository;
  final TransferRepository transferRepository;
  @Autowired
  public MoneyController(UserRepository userRepository, TransferRepository transferRepository) {
    this.userRepository = userRepository;
    this.transferRepository = transferRepository;
  }
  @PostMapping("/transfer")
  @Transactional
  public void transferMoney( @RequestParam String transferId,@RequestParam long from, @RequestParam long to, @RequestParam long amount) {
    if (transferRepository.existsByTransferId(transferId)) {
        return; // перевод уже обработан
    }
    User sender = userRepository.getById(from);
    User receiver = userRepository.getById(to);
    if (sender.getTransferLimit() < sender. calculateMonthlyTransferredAmount() + amount) {
      throw new TransferLimitExceededException();
    }
    if (sender.getBalance() < amount) {
      throw new NotEnoughMoneyException();
    }
    sender.setBalance(sender.getBalance() - amount);
    receiver.setBalance(receiver.getBalance() + amount);

    userRepository.save(sender);
    userRepository.save(receiver);
  }
}
```