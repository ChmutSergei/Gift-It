package by.chmut.giftit.service.impl;

import by.chmut.giftit.criteria.Criteria;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.ItemDao;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;

import java.io.Serializable;
import java.util.List;

public class ItemServiceImpl implements ItemService {

    private DaoFactory factory = DaoFactory.getInstance();

    private ItemDao itemDao = factory.getItemDao();


    @Override
    public boolean create(Item item) throws ServiceException {
        return false;
    }

    @Override
    public Item find(Long id) throws ServiceException {
        return null;
    }

    @Override
    public Item update(Item item) throws ServiceException {
        return null;
    }

    @Override
    public boolean delete(Serializable id) throws ServiceException {
        return false;
    }


    @Override
    public List<Item> find(List<Criteria> searchCriteria) throws ServiceException {
        

        return null;
    }


}
