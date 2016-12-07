
/*TODO: remove thread.sleep*/
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class SearchKT {

	WebDriver driver;
	List<WebElement> linkURLs;

	public SearchKT(String appURL) {
		this.driver = new FirefoxDriver();
		this.driver.get(appURL);
	}

	private void getLinkElements(String searchtext) {
		this.driver.findElement(By.cssSelector("input[title='Search']")).sendKeys(searchtext + Keys.ENTER);
	}

	private void getLinksFromElements() throws InterruptedException {
		Thread.sleep(1000);
		this.linkURLs = this.driver.findElements(By.cssSelector(".r>a"));
	}

	private void visitURLs() throws InterruptedException {
		String OpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
		for (WebElement url : this.linkURLs) {
			url.sendKeys(OpeninNewTab);
		}
		Thread.sleep(2000);
	}

	private void closeUselessTabs() throws InterruptedException {
		Thread.sleep(10000);
		while (true) {
			Thread.sleep(1000);
			String title = this.driver.getTitle();
			if (title.contains("Access to this site is blocked") || title.contains("Problem loading page")
					|| title.contains("blockpage.cgi")) {
				this.driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
			} else {
				this.driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.PAGE_DOWN);
			}
			if (this.driver.getTitle().contains("- Google Search"))
				break;
		}
	}

	private void nextSearchPage() {
		this.driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.PAGE_DOWN);
		while (!this.driver.getTitle().contains("- Google Search")) {
			this.driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
		}
		this.driver.findElement(By.xpath(".//*[@id='pnnext']/span[2]")).click();
	}

	public static void main(String[] args) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the text to search");
		String consoleInput = scanner.nextLine();
		if (!consoleInput.isEmpty()) {
			SearchKT movieSearch = new SearchKT("http://google.co.in");
			try {
				movieSearch.getLinkElements(consoleInput);
				while (true) {
					movieSearch.getLinksFromElements();
					movieSearch.visitURLs();
					movieSearch.closeUselessTabs();
					System.out.println("Did u get what u need");
					consoleInput = scanner.next();
					if (consoleInput.equalsIgnoreCase("y") || consoleInput.equalsIgnoreCase("yes"))
						break;
					movieSearch.nextSearchPage();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				movieSearch.complete();
				scanner.close();
			}
		}
	}

	private void complete() {
		this.driver.quit();
	}
}
