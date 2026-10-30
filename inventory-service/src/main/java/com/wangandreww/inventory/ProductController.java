package com.wangandreww.inventory;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductRepository products;

	public ProductController(ProductRepository products) {
		this.products = products;
	}

	@GetMapping
	public List<ProductResponse> list() {
		return products.findAll(Sort.by("id")).stream().map(ProductResponse::from).toList();
	}

}
