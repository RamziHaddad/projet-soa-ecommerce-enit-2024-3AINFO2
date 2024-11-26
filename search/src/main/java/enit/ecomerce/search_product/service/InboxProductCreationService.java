package enit.ecomerce.search_product.service;

import enit.ecomerce.search_product.product.ProductEntity;
import enit.ecomerce.search_product.repository.ProducteEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InboxProductCreationService {
    @Autowired
    private ProducteEntityRepository productEntityRepository;
    public void treatInbox(List<ProductEntity> inboxProducts) {
                    //the method should  check for un indexed products and index them and 
                    //updates the inbox data base
            }
        }
  

