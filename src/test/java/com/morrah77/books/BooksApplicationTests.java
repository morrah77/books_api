package com.morrah77.books;

import org.apache.catalina.core.ApplicationContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BooksApplicationTests {

	@Test
	public void contextLoads() {
		Assert.assertTrue(true);
	}

}
