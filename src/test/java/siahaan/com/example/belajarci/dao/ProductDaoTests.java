package siahaan.com.example.belajarci.dao;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import siahaan.com.example.belajarci.entity.Product;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Sql(scripts = { "/mysql/delete-data.sql", "/mysql/sample-product.sql" })
public class ProductDaoTests {

	@Autowired
	private ProductDao productDao;

	@Test
	public void testSave() {
		Product p = new Product();
		p.setCode("T-001");
		p.setName("Test Product 001");
		p.setPrice(new BigDecimal("100000.01"));
		Assert.assertNull(p.getId());
		productDao.save(p);
		Assert.assertNotNull(p.getId());
	}

	@Test
	public void testFindById() {
		Product p = productDao.findById("abc123").orElse(null);
		Assert.assertNotNull(p);
		Assert.assertEquals("P-001", p.getCode());
		Assert.assertEquals("Product 001", p.getName());
		Assert.assertEquals(BigDecimal.valueOf(101000.01), p.getPrice());

		Assert.assertNull(productDao.findById("notexist").orElse(null));
	}
}
