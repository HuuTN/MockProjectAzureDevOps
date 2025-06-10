package com.example.azurewebapp.controller;

import com.example.azurewebapp.model.Product;
import com.example.azurewebapp.repository.ProductRepository;
import com.example.azurewebapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller // Dùng @Controller để trả về view
public class ProductController {

    @Autowired
    private ProductService productService;

    // Endpoint cho trang chủ
    @GetMapping("/")
    public String viewHomePage() {
        // Trả về tên của file home.html (sẽ tạo sau)
        return "home";
    }

    // ... (các phương thức khác giữ nguyên)

    // Endpoint để hiển thị danh sách sản phẩm (ĐÃ CẬP NHẬT)
    @GetMapping("/products-list")
    public String viewProductsList(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int pageNum) {
        // Gọi service để lấy trang dữ liệu
        Page<Product> page = productService.listByPage(pageNum);

        // Lấy danh sách sản phẩm từ đối tượng Page
        List<Product> listProducts = page.getContent();

        // Lấy thông tin phân trang
        long startCount = (long) (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        // Đưa các thuộc tính cần thiết vào Model để Thymeleaf có thể sử dụng
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        return "products-list";
    }

    // Endpoint để hiển thị form tạo mới sản phẩm
    @GetMapping("/products/new")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Tạo sản phẩm mới");
        // Trả về tên của file product-form.html
        return "product-form";
    }

    // Endpoint để lưu sản phẩm (cho cả tạo mới và cập nhật)
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/products-list"; // Chuyển hướng về trang danh sách
    }

    // Endpoint để hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Integer id, Model model) {
        Product product = productService.get(id);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm (ID: " + id + ")");
            return "product-form";
        }
        return "redirect:/products-list";
    }

    // Endpoint để xem chi tiết sản phẩm
    @GetMapping("/products/detail/{id}")
    public String showProductDetail(@PathVariable("id") Integer id, Model model) {
        Product product = productService.get(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product-detail";
        }
        return "redirect:/products-list";
    }

    // Endpoint để xóa sản phẩm
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.delete(id);
        return "redirect:/products-list";
    }
}