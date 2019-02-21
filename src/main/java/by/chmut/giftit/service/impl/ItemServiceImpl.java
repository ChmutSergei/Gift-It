package by.chmut.giftit.service.impl;

import by.chmut.giftit.criteria.Criteria;
import by.chmut.giftit.criteria.SearchCriteria;
import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Bitmap;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static by.chmut.giftit.constant.AttributeName.*;

public class ItemServiceImpl implements ItemService {

    private DaoFactory factory = DaoFactory.getInstance();

    private ItemDao itemDao = factory.getItemDao();
    private BitmapDao bitmapDao = factory.getBitmapDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public boolean createItem(Map<String, Object> itemParameters) throws ServiceException {
        Item item = setParameters(itemParameters);
        boolean result;
        try {
            manager.beginTransaction(itemDao, bitmapDao);
            List<Bitmap> bitmaps = bitmapDao.findAll();
            updateItemFilter(bitmaps, item);
            result = itemDao.create(item);
            for (Bitmap bitmap : bitmaps) {
                bitmapDao.update(bitmap);
            }
            manager.endTransaction(itemDao);
        } catch (DaoException e) {
            //rollback;
            throw new ServiceException(e);
        }
        return result;
    }

    private void updateItemFilter(List<Bitmap> bitmaps, Item item) {
        String parameterPrice = selectOnValue(item.getCost());
        for (Bitmap bitmap : bitmaps) {
            int[] data = bitmap.getData();
            int[] newData = new int[data.length + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            if (bitmap.getName().equals(parameterPrice) || bitmap.getName().equals(item.getType())) {
                newData[newData.length - 1] = 1;
            }
            bitmap.setData(newData);
        }
    }

    private String selectOnValue(BigDecimal price) {
        if (price.compareTo(LOW_BORDER_PRICE) <= 0) {
            return LOW_PARAMETER_NAME;
        } else {
            if (price.compareTo(MEDIUM_BORDER_PRICE) <= 0) {
                return MEDIUM_PARAMETER_NAME;
            } else {
                if (price.compareTo(HIGH_BORDER_PRICE) <= 0) {
                    return HIGH_PARAMETER_NAME;
                }
            }
        }
        return null;
    }

    private Item setParameters(Map<String, Object> itemParameters) {
        Item item = new Item();
        item.setItemName((String) itemParameters.get(ITEM_NAME_PARAMETER_NAME));
        item.setType(((String[]) itemParameters.get(TYPE_PARAMETER_NAME))[0]);
        item.setDescription((String) itemParameters.get(DESCRIPTION_PARAMETER_NAME));
        String[] active = (String[]) itemParameters.get(ACTIVE_PARAMETER_NAME);
        item.setActive(ACTIVE_PARAMETER_NAME.equals(active[0]));
        item.setCost(new BigDecimal((String) itemParameters.get(PRICE_PARAMETER_NAME)));
        item.setImage(new File((String) itemParameters.get(ITEM_IMAGE_PARAMETER_NAME)));
        return item;
    }

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
        List<Criteria> criteriaList = removeNullable(searchCriteria);
        if (criteriaList.isEmpty()) {
            return Collections.emptyList();
        }
        int criteriaCount = criteriaList.size();
        List<Item> result = null;
        switch (criteriaCount) {
            case 1:
                if (criteriaList.get(0).getGroupSearchName().equals(SearchCriteria.Type.class.getSimpleName())) {
                    try {
                        result = findByType(criteriaList);
                    } catch (DaoException exception) {
                        throw new ServiceException(exception);
                    }
                } else {
                    try {
                        result = findByPrice(criteriaList);
                    } catch (DaoException exception) {
                        throw new ServiceException(exception);
                    }
                }
                break;
            case 2:
                try {
                    result = findByTypeAndPrice(criteriaList);
                } catch (DaoException exception) {
                    throw new ServiceException(exception);
                }
                break;
            default:
                throw new ServiceException("Not supported operation - when find item");
        }
        return result;
    }

    private List<Item> findByType(List<Criteria> criteriaList) throws DaoException {
        List<Item> items = new ArrayList<>();
        for (String type : criteriaList.get(0).getCriteria()) {
            items.addAll(itemDao.findByType(type));
        }
        return items;
    }

    private List<Item> findByPrice(List<Criteria> criteriaList) throws DaoException {
        List<String> criteriaPriceList = criteriaList.get(0).getCriteria();
        String price = criteriaPriceList.get(criteriaList.size() - 1);
        List<Item> items = new ArrayList<>(itemDao.findByPrice(price));
        return items;
    }

    private List<Item> findByTypeAndPrice(List<Criteria> criteriaList) throws DaoException {
        List<Item> items = new ArrayList<>();
        Criteria criteriaType = getCriteriaOnName(criteriaList, SearchCriteria.Type.class.getSimpleName());
        Criteria criteriaPrice = getCriteriaOnName(criteriaList, SearchCriteria.Price.class.getSimpleName());
        List<String> criteriaPriceList = criteriaPrice.getCriteria();
        String price = criteriaPriceList.get(criteriaList.size() - 1);
        for (String type : criteriaType.getCriteria()) {
            items.addAll(itemDao.findByTypeAndPrice(type, price));
        }
        return items;
    }

    private Criteria getCriteriaOnName(List<Criteria> criteriaList, String name) {
        for (Criteria criteria : criteriaList) {
            if (criteria.getGroupSearchName().equals(name)) {
                return criteria;
            }
        }
        return null;
    }

    private List<Criteria> removeNullable(List<Criteria> searchCriteria) {
        List<Criteria> result = new ArrayList<>();
        List<String> params = new ArrayList<>();
        for (Criteria criteria : searchCriteria) {
            params = criteria.getCriteria().stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (!params.isEmpty()) {
                criteria.setCriteria(params);
                result.add(criteria);
            }
        }
        return result;
    }

}
