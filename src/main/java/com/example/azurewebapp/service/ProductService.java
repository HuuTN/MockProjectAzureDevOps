package com.example.azurewebapp.service;

import com.example.azurewebapp.model.Product;
import com.example.azurewebapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.PageRequest; // Import PageRequest
import org.springframework.data.domain.Pageable; // Import Pageable
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
  @Autowired
  private ProductRepository repo;

  // Kích thước mỗi trang
  public static final int PRODUCTS_PER_PAGE = 10;

  // Phương thức mới để lấy danh sách theo trang
  public Page<Product> listByPage(int pageNum) {
    // Pageable là đối tượng chứa thông tin phân trang.
    // PageRequest.of(page, size) - page ở đây là zero-based (bắt đầu từ 0)
    // nên ta cần trừ 1 từ pageNum mà người dùng truyền vào (bắt đầu từ 1).
    Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

    return repo.findAll(pageable);
  }

  // Giữ lại các phương thức cũ
  public void save(Product product) {
    repo.save(product);
  }

  public Product get(Integer id) {
    Optional<Product> result = repo.findById(id);
    return result.orElse(null);
  }

  public void delete(Integer id) {
    repo.deleteById(id);
  }
}