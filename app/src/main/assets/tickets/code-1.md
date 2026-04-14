---
number: 30
title: Сервис купли-продажи почки
keywords: бизнес-логика, уровни архитектуры, многопоточность, распределённая обработка
---

### Написать класс, реализующий транзакцию, которая получает Почку, Seller и Buyer и совершает акт купли-продажи. Причем продавец не может суммарно продать, а покупатель купить более 2 почек.

```java
class KidneyService {
  @Autowired
  private KidneyRepository kidneyRepo;
  @Autowired
  private SellerRepository sellerRepo;
  @Autowired
  private BuyerRepository buyerRepo;

  @Transactional
  public void kidneyTrading(Kidney kidney, Seller s, Buyer b) {
    Seller seller = sellerRepo.findSellerById(s.getId());
    Buyer buyer = buyerRepo.findSellerById(b.getId());
    List<Kidney> sellerSoldKidneys = seller.getSoldKidneys();
    List<Kidney> buyerBoughtKidneys = buyer.getBoughtKidneys();
    if (sellerSoldKidneys.size() >= 2 || buyerBoughtKidneys.size() >= 2)
      throw new KidneyLimitExceededException("Too much stuff"); 
    sellerSoldKidneys.add(kidney);
    buyerBoughtKidneys.add(kidney);
    kidney.setOwner(buyer);

    kidneyRepo.save(kidney);
    sellerRepo.save(seller);
    buyerRepo.save(buyer);
  }
}
```