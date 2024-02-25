package de.unipassau.fim.fsinfo.kdv.service;

import de.unipassau.fim.fsinfo.kdv.data.dao.ShopHistoryEntry;
import de.unipassau.fim.fsinfo.kdv.data.dao.ShopItem;
import de.unipassau.fim.fsinfo.kdv.data.dao.User;
import de.unipassau.fim.fsinfo.kdv.data.repositories.ShopHistoryRepository;
import de.unipassau.fim.fsinfo.kdv.data.repositories.ShopItemRepository;
import de.unipassau.fim.fsinfo.kdv.data.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

  @Autowired
  ShopItemRepository itemRepository;
  @Autowired
  ShopHistoryRepository historyRepository;
  @Autowired
  UserRepository userRepository;

  public boolean consume(String itemId, String userId, int amount) {
    Optional<ShopItem> itemO = itemRepository.findById(itemId);
    Optional<User> userO = userRepository.findById(userId);

    if (userO.isEmpty() || itemO.isEmpty()) {
      return false;
    }

    User user = userO.get();
    ShopItem item = itemO.get();

    if (!item.getEnabled() || !user.getEnabled()) {
      return false;
    }

    // Create single entries for every item
    for (int i = 0; i < amount; i++) {
      user.setBalance(user.getBalance() - item.getPrice());

      userRepository.save(user);
      historyRepository.save(new ShopHistoryEntry(user.getId(), item.getId(), item.getPrice()));
    }

    return true;
  }

}