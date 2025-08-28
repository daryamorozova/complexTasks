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
    public void testAddProductWhenInventoryOpen() throws InventoryService.NegativePriceException, OutOfStockException {
        Product product = new Product("Laptop", 1000, "Electronics");
        inventoryService.addProduct(product);

        List<Product> products = inventoryService.extractProducts("Electronics");
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testAddProductWhenInventoryClosed() throws InventoryService.NegativePriceException {
        InventoryService.setIsInventoryOpenEnabled(false);
        Product product = new Product("Laptop", 1000, "Electronics");

        inventoryService.addProduct(product);

        assertThrows(OutOfStockException.class, () -> {
            inventoryService.extractProducts("Electronics");
        });
    }

    @Test
    public void testExtractProductsWhenCategoryExists() throws OutOfStockException, InventoryService.NegativePriceException {
        Product product = new Product("Laptop", 1000, "Electronics");
        inventoryService.addProduct(product);

        List<Product> products = inventoryService.extractProducts("Electronics");
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testExtractProductsWhenCategoryDoesNotExist() {
        assertThrows(OutOfStockException.class, () -> {
            inventoryService.extractProducts("NonExistentCategory");
        });
    }

    @Test
    public void testFilterProductsByCategoryAndPrice() throws InventoryService.NegativePriceException {
        inventoryService.addProduct(new Product("Laptop", 1000, "Electronics"));
        inventoryService.addProduct(new Product("Phone", 500, "Electronics"));
        inventoryService.addProduct(new Product("TV", 1500, "Electronics"));

        List<Product> filteredProducts = inventoryService.filterProductsByCategoryAndPrice("Electronics", 1000);
        assertEquals(2, filteredProducts.size());
        assertTrue(filteredProducts.stream().anyMatch(p -> p.getName().equals("Laptop")));
        assertTrue(filteredProducts.stream().anyMatch(p -> p.getName().equals("Phone")));
    }

    @Test
    public void testAddProductWithNegativePrice() {
        assertThrows(InventoryService.NegativePriceException.class, () -> {
            inventoryService.addProduct(new Product("Faulty Product", -100, "Misc"));
        });
    }

}
