package siahaan.com.example.belajarci.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import siahaan.com.example.belajarci.entity.Product;

public interface ProductDao extends PagingAndSortingRepository<Product, String>{ }
