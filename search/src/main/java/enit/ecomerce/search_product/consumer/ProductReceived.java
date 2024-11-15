package enit.ecomerce.search_product.consumer;

 
public record ProductReceived(
    String id,
    String name,
    String description,
    Float price,
    String category
) {}
