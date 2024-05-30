package com.makersacademy.acebook.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import com.github.javafaker.Faker;
import com.makersacademy.acebook.Application;
import com.makersacademy.acebook.repository.PostRepository;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, properties="spring.flyway.clean-disabled=false")
@ActiveProfiles("test")
public class PostTest {

	private Post post = new Post("hello");
	@Autowired
	private PostRepository postRepository;

	static WebDriver driver;
	static Faker faker;
	@Autowired Flyway flyway;

	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		driver = new ChromeDriver();
		faker = new Faker();
		flyway.setCleanDisabled(false);
		flyway.clean();
		flyway.migrate();
	}

	@After
	public void tearDown() {
		driver.close();
	}


	public WebDriver getDriver() {
		return driver;
	}

	@Test
	public void signInViewPost() {
		driver.get("http://localhost:8080/login");
		driver.findElement(By.id("username")).sendKeys("johndoe");
		driver.findElement(By.id("password")).sendKeys("password123");
		driver.findElement(By.tagName("button")).click();
		WebElement element = driver.findElement(By.tagName("ul"));
		Assert.assertEquals("This is my first post!\nLikes: 15\nLike", element.getText());
	}

	@Test
	public void signInCreatePostCheckLikesIs0() {
		driver.get("http://localhost:8080/login");
		// Login
		driver.findElement(By.id("username")).sendKeys("johndoe");
		driver.findElement(By.id("password")).sendKeys("password123");
		driver.findElement(By.tagName("button")).click();

		// Create a new post
		driver.findElement(By.id("content")).sendKeys("post test");
		driver.findElement(By.id("content_create")).click();

		// Wait for the post to appear
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5).toMillis());
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(), 'post test')]")));

		// Verify the post content
		WebElement postContentElement = driver.findElement(By.xpath("//li[contains(text(), 'post test')]"));
		String postContent = postContentElement.getText();
		Assert.assertEquals("post test", postContent);

		// Verify the initial like count
		WebElement initialLikesElement = driver.findElement(By.xpath("//li[contains(text(), 'post test')]/following-sibling::li[contains(text(), 'Likes:')]"));
		String initialLikesText = initialLikesElement.getText();
		Assert.assertEquals("Likes: 0", initialLikesText);

		// Find the newly created post's like button
		WebElement likeButton = driver.findElement(By.xpath("//li[contains(text(), 'post test')]/following-sibling::form/button"));
		likeButton.click();

		// Wait for the like count to update
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//li[contains(text(), 'post test')]/following-sibling::li[contains(text(), 'Likes:')]"), "Likes: 1"));

		// Verify the updated like count
		WebElement updatedLikesElement = driver.findElement(By.xpath("//li[contains(text(), 'post test')]/following-sibling::li[contains(text(), 'Likes:')]"));
		String updatedLikesText = updatedLikesElement.getText();
		Assert.assertEquals("Likes: 1", updatedLikesText);

		// Clean up the test post
//		postRepository.deleteTestPost();
	}

	@Test
	public void postHasContent() {
		assertThat(post.getContent(), containsString("hello"));
	}

}

