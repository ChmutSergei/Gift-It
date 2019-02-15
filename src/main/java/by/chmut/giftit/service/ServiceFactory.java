package by.chmut.giftit.service;

import by.chmut.giftit.entity.User;

public class ServiceFactory {

    private static final ServiceFactory INSTANCE = new ServiceFactory();

//    private final PublicationService publicationService= new PublicationServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

//    public PublicationService getPublicationService() {
//        return publicationService;
//    }
}
