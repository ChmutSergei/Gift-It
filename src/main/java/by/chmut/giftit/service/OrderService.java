package by.chmut.giftit.service;

import by.chmut.giftit.entity.Item;

import java.util.List;

public interface OrderService {

    List<Item> findPaidItems(long userId) throws ServiceException;
}
