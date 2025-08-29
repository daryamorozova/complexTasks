package task5;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceTest {

    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        inventoryService = new InventoryService();
        InventoryService.setIsInventoryOpenEnabled(true);
    }

    @Test
    public void testAddProductWhenInventoryOpen() throws NegativePriceException, OutOfStockException {
        Product product = new Product("Ноутбук", 1000, "Электроника");
        inventoryService.addProduct(product);

        List<Product> products = inventoryService.extractProducts("Электроника");
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }


    @Test
    public void testAddProductWhenInventoryClosed() throws NegativePriceException, OutOfStockException {
        InventoryService.setIsInventoryOpenEnabled(false);
        Product product = new Product("Ноутбук", 1000, "Электроника");
        inventoryService.addProduct(product);

        assertThrows(OutOfStockException.class, () -> inventoryService.extractProducts("Электроника"));
    }

    @Test
    public void testExtractProductsWhenCategoryExists() throws NegativePriceException, OutOfStockException {
        Product product = new Product("Ноутбук", 1000, "Электроника");
        inventoryService.addProduct(product);

        List<Product> products = inventoryService.extractProducts("Электроника");
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testExtractProductsWhenCategoryDoesNotExist(){
        assertThrows(OutOfStockException.class, () -> inventoryService.extractProducts("Несуществующая категория"));
    }

    @Test
    public void testFilterProductsByCategoryAndPrice() throws NegativePriceException {
        inventoryService.addProduct(new Product("Ноутбук", 1000, "Электроника"));
        inventoryService.addProduct(new Product("Телефон", 500, "Электроника"));
        inventoryService.addProduct(new Product("Пиджак", 1500, "Одежда"));

        List<Product> filteredProducts = inventoryService.filterProductsByCategoryAndPrice("Электроника", 500);
        assertEquals(1, filteredProducts.size());
        assertTrue(filteredProducts.stream().anyMatch(product -> product.getName().equals("Телефон")));

    }

    @Test
    public void testAddProductWithNegativePrice(){
        assertThrows(NegativePriceException.class, () -> {
            inventoryService.addProduct(new Product("Неправильный продукт", -100, "Одежда"));
        });
    }




}
