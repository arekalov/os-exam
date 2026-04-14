---
number: 33
title: Спам-бот Telegram
keywords: бизнес-логика, уровни архитектуры, многопоточность, распределённая обработка
---

### Спам-бот Telegram — REST контроллер отправки сообщений конкретному пользователю, случайному пулу пользователей и всему сообществу


```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/spam")
public class SpamController {
    private final TelegramService telegramService; // умеет отправлять сообщения по chatId
    private final UserRepository userRepository;

    @Autowired
    public SpamController(TelegramService telegramService,
                          UserRepository userRepository) {
        this.telegramService = telegramService;
        this.userRepository = userRepository;
    }

    // 1. Отправка конкретному пользователю
    @PostMapping("/user")
    public void sendToUser(@RequestParam Long userId,
                           @RequestParam String message) {

        User user = userRepository.getById(userId);
        telegramService.sendMessage(user.getChatId(), message);
    }

    // 2. Отправка случайному пулу пользователей
    @PostMapping("/random")
    public void sendToRandom(@RequestParam int count,
                             @RequestParam String message) {

        List<User> users = userRepository.findRandomUsers(count);

        for (User user : users) {
            telegramService.sendMessage(user.getChatId(), message);
        }
    }

    // 3. Рассылка всем пользователям
    @PostMapping("/broadcast")
    public void broadcast(@RequestParam String message) {

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            telegramService.sendMessage(user.getChatId(), message);
        }
    }
}
```