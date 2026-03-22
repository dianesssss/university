package ru.msu.cmc.webprac.dao;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class BaseDaoTest extends AbstractTestNGSpringContextTests {
}