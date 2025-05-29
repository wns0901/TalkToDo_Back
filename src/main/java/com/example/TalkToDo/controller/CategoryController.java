package com.example.TalkToDo.controller;

import com.example.TalkToDo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 사용자의 카테고리 목록 조회
    @GetMapping("/user/{userId}")
    public List<String> getUserCategories(@PathVariable Long userId) {
        return categoryService.getUserCategories(userId);
    }

    // 사용자의 카테고리 추가
    @PostMapping("/user/{userId}")
    public String addUserCategory(
            @PathVariable Long userId,
            @RequestBody String category) {
        return categoryService.addUserCategory(userId, category);
    }

    // 사용자의 카테고리 삭제
    @DeleteMapping("/user/{userId}/{category}")
    public ResponseEntity<?> deleteUserCategory(
            @PathVariable Long userId,
            @PathVariable String category) {
        categoryService.deleteUserCategory(userId, category);
        return ResponseEntity.ok().build();
    }
} 