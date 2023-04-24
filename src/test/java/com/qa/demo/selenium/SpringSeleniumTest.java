package com.qa.demo.selenium;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = { "classpath:cat-schema.sql", "classpath:cat-data.sql" })

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringSeleniumTest {

	public WebDriver driver;

	@LocalServerPort
	private int port;

	private WebDriverWait wait;

	@BeforeEach
	void init() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		this.driver = new ChromeDriver(options);
		this.driver.manage().window().maximize();
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
	}

	@Test
	void testTitle() {
		this.driver.get("http://localhost:" + port);
		WebElement title = this.driver.findElement(By.cssSelector("body > header > h1"));
		Assertions.assertEquals("CATS", title.getText());
	}

	@Test
	void testGetAll() {
		this.driver.get("http://localhost:" + port + "/");

		WebElement card = this.wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div > div")));
		Assertions.assertTrue(card.getText().contains("Mr Bigglesworth"));
	}

	@Test
	void testGet() {
		this.driver.get("http://localhost:" + port + "/");

		WebElement card = this.wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div > div")));
		Assertions.assertTrue(card.getText().contains("Mr Bigglesworth"));
	}

	@Test
	void testCreate() {
		this.driver.get("http://localhost:" + port + "/");

		WebElement name = this.driver.findElement(By.cssSelector("#catName"));
		name.sendKeys("devil");
		WebElement length = this.driver.findElement(By.cssSelector("#catLength"));
		length.sendKeys("21");
		WebElement whiskers = this.driver.findElement(By.cssSelector("#catWhiskers"));
		whiskers.click();
		WebElement evil = this.driver.findElement(By.cssSelector("#catEvil"));
		evil.click();
		WebElement submit = this.driver.findElement(By.cssSelector("#catForm > div.mt-3 > button.btn.btn-success"));
		submit.click();

		WebElement card = this.wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div:nth-child(2)")));
		Assertions.assertTrue(card.getText().contains("devil"));
		Assertions.assertTrue(card.getText().contains("21"));

		WebElement whiskersCheck = this.wait.until(ExpectedConditions
				.elementToBeClickable(By.cssSelector("#output > div:nth-child(2) > div > div > p:nth-child(3)")));
		Assertions.assertEquals("Whiskers: true", whiskersCheck.getText());

		WebElement evilCheck = this.wait.until(ExpectedConditions
				.elementToBeClickable(By.cssSelector("#output > div:nth-child(2) > div > div > p:nth-child(4)")));
		Assertions.assertEquals("Evil: true", evilCheck.getText());
	}

	@Test
	void testUpdate() {
		this.driver.get("http://localhost:" + port + "/");
		WebElement update = this.driver.findElement(By.cssSelector("#output > div > div > div > button:nth-child(5)"));
		update.click();
		WebElement form = this.driver.findElement(By.cssSelector("#updateForm"));

		WebElement name = form.findElement(By.cssSelector("#catName"));
		name.clear();
		name.sendKeys("devil");
		WebElement length = form.findElement(By.cssSelector("#catLength"));
		length.clear();
		length.sendKeys("21");
		WebElement whiskers = form.findElement(By.cssSelector("#catWhiskers"));
		whiskers.click();
		WebElement submit = this.driver.findElement(By.cssSelector("#updateForm > div.mt-3 > button.btn.btn-success"));
		submit.click();

		WebElement card = this.wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div > div")));
		Assertions.assertEquals("", card.getText());

	}

	@Test
	void testDelete() {
		this.driver.get("http://localhost:" + port + "/");
//		WebElement delete = this.driver.findElement(By.cssSelector("#output > div > div > div > button:nth-child(6)"));
//		delete.click();
//		WebElement card = this.wait
//				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div > div")));
//		Dimension size = card.findElement(By.cssSelector("#output > div > div")).getSize();
//		Assertions.assertTrue(size.equals(0));
	}

	@AfterEach
	void tearDown() {
		// this.driver.close();
	}
}
