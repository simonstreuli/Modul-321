package com.example.server.controller;

import com.example.server.api.ItemsApi;
import com.example.server.model.CreateItemRequest;
import com.example.server.model.Item;
import com.example.server.model.UpdateItemRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller implementing the generated ItemsApi interface.
 * Provides simple logging and returns dummy data for demonstration.
 */
@RestController
public class ItemsController implements ItemsApi {

    private static final Logger logger = LoggerFactory.getLogger(ItemsController.class);

    @Override
    public ResponseEntity<List<Item>> getAllItems() {
        logger.info("GET /api/items - Retrieving all items");
        
        List<Item> items = createDummyItems();
        
        logger.info("Returning {} items", items.size());
        return ResponseEntity.ok(items);
    }

    @Override
    public ResponseEntity<Item> getItemById(Long id) {
        logger.info("GET /api/items/{} - Retrieving item by ID", id);
        
        Item item = createDummyItem(id);
        
        logger.info("Returning item: {}", item.getName());
        return ResponseEntity.ok(item);
    }

    @Override
    public ResponseEntity<Item> createItem(CreateItemRequest createItemRequest) {
        logger.info("POST /api/items - Creating new item: {}", createItemRequest.getName());
        
        Item newItem = new Item();
        newItem.setId(System.currentTimeMillis());
        newItem.setName(createItemRequest.getName());
        newItem.setDescription(createItemRequest.getDescription());
        newItem.setPrice(createItemRequest.getPrice());
        newItem.setCreatedAt(OffsetDateTime.now());
        
        logger.info("Created item with ID: {}", newItem.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @Override
    public ResponseEntity<Item> updateItem(Long id, UpdateItemRequest updateItemRequest) {
        logger.info("PUT /api/items/{} - Updating item", id);
        
        Item updatedItem = new Item();
        updatedItem.setId(id);
        updatedItem.setName(updateItemRequest.getName());
        updatedItem.setDescription(updateItemRequest.getDescription());
        updatedItem.setPrice(updateItemRequest.getPrice());
        updatedItem.setCreatedAt(OffsetDateTime.now());
        
        logger.info("Updated item: {}", updatedItem.getName());
        return ResponseEntity.ok(updatedItem);
    }

    @Override
    public ResponseEntity<Void> deleteItem(Long id) {
        logger.info("DELETE /api/items/{} - Deleting item", id);
        
        logger.info("Item with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    private List<Item> createDummyItems() {
        List<Item> items = new ArrayList<>();
        items.add(createDummyItem(1L));
        items.add(createDummyItem(2L));
        items.add(createDummyItem(3L));
        return items;
    }

    private Item createDummyItem(Long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("Item " + id);
        item.setDescription("This is a description for item " + id);
        item.setPrice(9.99 * id);
        item.setCreatedAt(OffsetDateTime.now());
        return item;
    }
}
