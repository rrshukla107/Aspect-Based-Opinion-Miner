package com.rahul.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ReviewsCrawler {
	private static WebDriver webDriver;

	@BeforeAll
	public static void setup() {

		Properties config = new Properties();
		try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
			config.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.setProperty("webdriver.chrome.driver", config.getProperty("chrome_driver_path"));
		webDriver = new ChromeDriver();

	}

	@AfterAll
	public static void cleanUp() {
		webDriver.quit();
	}

	@Test
	void scrapeUserReviews_Interstellar() throws InterruptedException, IOException {

		this.webDriver.get("https://www.rottentomatoes.com/m/interstellar_2014/reviews?type=user");
		File file = new File("interstellar");

		try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
			while (true) {

				List<WebElement> findElements = this.webDriver
						.findElements(By.cssSelector("#movieUserReviewsContent > ul > li "));

				for (WebElement element : findElements) {
					br.write(element.findElement(By.cssSelector(
							"div.audience-reviews__review-wrap > p.audience-reviews__review.js-review-text.clamp.clamp-8.js-clamp"))
							.getText());
					br.flush();
				}

				this.webDriver.findElement(By.cssSelector(
						"#content > div > div > nav:nth-child(7) > button.js-prev-next-paging-next.btn.prev-next-paging__button.prev-next-paging__button-right"))
						.click();

				TimeUnit.SECONDS.sleep(3);
			}
		}

	}

	@Test
	void scrapeGoogleProductReviews_MacbookPro() throws IOException, InterruptedException {
//		this.webDriver.get(
//				"https://www.google.com/shopping/product/17689651069453647396/reviews?rlz=1C1CHBF_enUS862US862&biw=1422&bih=588&output=search&q=macbook+pro&oq=macbook+pro&aqs=products-cc..0l10&prds=paur:ClkAsKraX2ztbQacGyqoLI7OYpEsczZxRRwAmF5LbSm3S6nNpBC6ivaJWGJr7uv4Ty9kKjRxwAnOc79w2GzNV7fXR8bcECEQvzj3RfIsLUTtSjJZy8wfV-fTYRIZAFPVH7021kw-6kSKyZJ3l8Q4e4NN2kvigw&sa=X&ved=0ahUKEwjtxqyQqZbpAhWL4J4KHWi9Ci4QqSQIUw");

//		this.webDriver.get(
//				"https://www.google.com/shopping/product/18381129246155810521/reviews?q=macbook+pro+reviews&rlz=1C1CHBF_enUS862US862&sxsrf=ALeKk03VFD1VHmoX9fHdoiN1LdSoF8V9zw:1588467769044&biw=1422&bih=588&prds=paur:ClkAsKraX6wYOfLqGZ4wgjXhVXOSNNj0EOKELcJ1tDbrtYocn8m09-1wPJrlDrY6_7KFt17GCucC55U54COXNzIx1H1R03mxpUCcJMNEjDTHr33tEGUPzfxCwxIZAFPVH73ltitir38fXYYehT7TBlzEw97n4A&sa=X&ved=0ahUKEwix3N_Vv5bpAhWSv54KHS3fBEsQqSQIWQ");

		this.webDriver.get(
				"https://www.google.com/shopping/product/4540269165564822038/reviews?rlz=1C1CHBF_enUS862US862&biw=1422&bih=588&output=search&q=macbook+pro&oq=macbook+pro&aqs=products-cc..0l10&prds=paur:ClkAsKraX3mpTGL71hPibrrsKeH4yo6BYPaCZHlSZH6z9CFTGmelCXEJK4vqOdq9C3nAuj3TD-HIhF5WeyXOv2YXKRf5S5v6crtgyvNAMfaflWHZINYFIypdGRIZAFPVH71SAXs1nvjZAGcKuQeXT9qDw3t7QA&sa=X&ved=0ahUKEwiirtXbwZbpAhXIHjQIHf_BCX4QqSQIWA");
		File file = new File("macbook_pro");

		Set<String> visited = new HashSet<>();

		try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
			while (true) {

				List<WebElement> findElements = this.webDriver
						.findElements(By.cssSelector("#sh-rol__reviews-cont > div.z6XoBf > div.g1lvWe > div[style]"));

				findElements.addAll(this.webDriver
						.findElements(By.cssSelector("#sh-rol__reviews-cont > div[style] > div._-i2 > div[style]")));

				for (WebElement element : findElements) {
					String id = element.getAttribute("id");
					if (!visited.contains(id)) {
						visited.add(id);
						System.out.println(id);
						br.write(element.getAttribute("textContent"));
						System.out.println(element.getAttribute("textContent"));
						br.newLine();
						br.flush();
					}

				}

				this.webDriver
						.findElement(
								By.cssSelector("#sh-fp__pagination-button-wrapper > button > div.sh-btn__background"))
						.click();

				TimeUnit.SECONDS.sleep(3);
			}
		}

	}
}
