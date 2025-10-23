package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final Map<String, Warehouse> INSTANCES = new HashMap<>();

    private final String name;
    private final Map<UUID, Product> products = new LinkedHashMap<>();
    private final Set<UUID> changedProducts = new HashSet<>();

    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance(String name) {
        return INSTANCES.computeIfAbsent(name, Warehouse::new);
    }
    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        products.put(product.uuid(), product);
    }

    public List<Product> getProducts() {
        return List.copyOf(products.values());
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }
    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Product product = products.get(id);
        if (product == null)
            throw new NoSuchElementException("Product not found with id: " + id);
        product.price(newPrice);
        changedProducts.add(id);
    }

    public List<Product> getChangedProducts() {
        return changedProducts.stream()
                .map(products::get)
                .filter(Objects::nonNull)
                .toList();
    }
    public List<Perishable> expiredProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Perishable per && per.isExpired())
                .map(p -> (Perishable) p)
                .toList();
    }

    public List<Shippable> shippableProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .toList();
    }
    public void remove(UUID id) {
        products.remove(id);
        changedProducts.remove(id);
    }
    public void clearProducts() {
        products.clear();
        changedProducts.clear();
    }
    public boolean isEmpty() {
        return products.isEmpty();
    }
    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        return products.values().stream()
                .collect(Collectors.groupingBy(Product::category));
    }



}





