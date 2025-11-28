package com.example.client.runner;

import com.example.client.api.ItemsApi;
import com.example.client.model.CreateItemRequest;
import com.example.client.model.Item;
import com.example.client.model.UpdateItemRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Client runner that demonstrates calling the CRUD API endpoints.
 */
@Component
public class ApiClientRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApiClientRunner.class);

    private final ItemsApi itemsApi;

    public ApiClientRunner(ItemsApi itemsApi) {
        this.itemsApi = itemsApi;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== Starting API Client Demo ===");

        try {
            // 1. Get all items
            logger.info("\n--- GET ALL ITEMS ---");
            List<Item> items = itemsApi.getAllItems();
            logger.info("Retrieved {} items:", items.size());
            items.forEach(item -> logger.info("  - Item {}: {} (${}) - {}", 
                item.getId(), item.getName(), item.getPrice(), item.getDescription()));

            // 2. Get item by ID
            logger.info("\n--- GET ITEM BY ID ---");
            Item item = itemsApi.getItemById(1L);
            logger.info("Retrieved item: {} - {}", item.getName(), item.getDescription());

            // 3. Create new item
            logger.info("\n--- CREATE NEW ITEM ---");
            CreateItemRequest createRequest = new CreateItemRequest();
            createRequest.setName("New Product");
            createRequest.setDescription("A brand new product created via API");
            createRequest.setPrice(49.99);
            Item createdItem = itemsApi.createItem(createRequest);
            logger.info("Created item: ID={}, Name={}", createdItem.getId(), createdItem.getName());

            // 4. Update item
            logger.info("\n--- UPDATE ITEM ---");
            UpdateItemRequest updateRequest = new UpdateItemRequest();
            updateRequest.setName("Updated Product");
            updateRequest.setDescription("Updated description");
            updateRequest.setPrice(59.99);
            Item updatedItem = itemsApi.updateItem(1L, updateRequest);
            logger.info("Updated item: ID={}, Name={}, Price={}", 
                updatedItem.getId(), updatedItem.getName(), updatedItem.getPrice());

            // 5. Delete item
            logger.info("\n--- DELETE ITEM ---");
            itemsApi.deleteItem(1L);
            logger.info("Item deleted successfully");

            logger.info("\n=== API Client Demo Completed Successfully ===");

        } catch (Exception e) {
            logger.error("Error during API calls: {}", e.getMessage());
            throw e;
        }
    }
}
