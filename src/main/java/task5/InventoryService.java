package task5;


import java.util.*;
import java.util.stream.Collectors;

public class InventoryService {

    private Map<String, List<Product>> productsMap = new HashMap<>();
     static boolean isInventoryOpen = true;

    public static void setIsInventoryOpenEnabled(boolean isEnabled) {
        isInventoryOpen = isEnabled;
    }

    public void addProduct(Product product) throws NegativePriceException {
        if (!isInventoryOpen) {
            return;  //  Если склад закрыт, то выходим
        }

        if (product.getPrice() < 0) {
            throw new NegativePriceException("Цена не может быть отрицательной");
        }

        productsMap.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
    }

    public List<Product> extractProducts(String category) throws OutOfStockException {
        if (!isInventoryOpen) {
            throw new OutOfStockException("Склад закрыт");
        }

        List<Product> products = productsMap.get(category);
        if (products == null || products.isEmpty()) {
            throw new OutOfStockException("Товары в категории " + category + " отсутствуют");
        }
        return new ArrayList<>(products);
    }

    public List<Product> filterProductsByCategoryAndPrice(String category, int maxPrice) {
       return productsMap.getOrDefault(category, Collections.emptyList()).stream()
                .filter(product -> product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

}
