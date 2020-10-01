package org.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class WildberriesPage {
    WebDriver driver;

    public WildberriesPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(css = ".c-btn-burger")
    WebElement we_main_button;

    @FindBy(xpath = "//ul[@class='topmenus']//a[text()='Электроника']")
    WebElement we_electronics;

    @FindBy(xpath = "//div[@class='left']//a[text()= 'Ноутбуки и компьютеры']")
    WebElement we_notebooks_and_computers;

    @FindBy(xpath = "//ul[@class='sidemenu']//a[text()='Ноутбуки']")
    WebElement we_notebooks;

    public void goto_notebook_catalog() {
        driver.get("https://www.wildberries.ru/");
        we_main_button.click();
        we_electronics.click();
        we_notebooks_and_computers.click();
        we_notebooks.click();
        assertTrue(driver.getTitle().contains("Купить ноутбуки"));
    }


    @FindBy(xpath = "//*[@id='filterPanelLeft']//input[@name='startN']")
    WebElement we_price_min;

    @FindBy(xpath = "//*[@id='filterPanelLeft']//input[@name='endN']")
    WebElement we_price_max;

    @FindBy(xpath = "//*[@id='filterPanelLeft']//label[contains(text(), '1920x1080')]")
    WebElement we_checkbox_1920_1080;

    @FindBy(xpath = "//*[@id='filterPanelLeft']//div[@class='BrandSearch']")
    WebElement we_brand_search;

    @FindBy(xpath = "//*[@id='filterPanelLeft']")
    WebElement we_panel_left;

    @FindBy(xpath = "//*[@id='filterPanelLeft']//div[@class='BrandSearch']/..//label")
    List<WebElement> lst_brands;


    public void apply_filters() {

        we_checkbox_1920_1080.click();

        for (int i = 0; i < 10; i++) {
            we_price_min.sendKeys(Keys.BACK_SPACE);
        }
        we_price_min.sendKeys("0");
        we_price_min.sendKeys(Keys.ENTER);

        for (int i = 0; i < 10; i++) {
            we_price_max.sendKeys(Keys.BACK_SPACE);
        }
        we_price_max.sendKeys("40000");
        we_price_min.sendKeys(Keys.ENTER);


        assertTrue(lst_brands.size() >= 5, "Найдено менее чем 5 брендов ноутбуков");
        for (Integer i = 0; i < 5; i++) {
            lst_brands.get(i).click();
            WaitForAjax();
        }
    }

    @FindBy(xpath = "//*[@id='price']")
    WebElement we_btn_sort_by_price;

    public void sortByPrice(String findStr) {
        for (int i = 0; i < 2; i++) {
            String href = we_btn_sort_by_price.getAttribute("href");
            we_btn_sort_by_price.click();
            WaitForAjax();
            if (href.contains(findStr)) return;
        }
        fail("Не найден элемент с критерием для сортировки ->" + findStr);
    }


    @FindBy(xpath = "//*[@id='catalog-content']//div[contains(@class,'j-card-item')]")
    List<WebElement> lst_catalog_items;

    public String get_notebook_by_number(Integer min_elements_count_in_catalog, int find_index) {
        Integer size=lst_catalog_items.size();
        assertTrue(size >= min_elements_count_in_catalog, "В каталоге количетсво товара=" + size + " менее чем лимит=" + min_elements_count_in_catalog);
        assertTrue(size >= find_index, "В каталоге количетсво товара=" + size + " это меньше чем индекс поиска=" + find_index);
        String ret = lst_catalog_items.get(find_index).getAttribute("data-catalogercod1s");
        return ret;
    }



    @FindBy(xpath = "//a[@id='comments_reviews_link']")
    WebElement we_comment_link;

    public String get_count_review() {
        String data = "0";
        try {
            data = we_comment_link.getText();
            data = data.replace("отзывов", "");
            data = data.replace(" ", "");
        } catch (Exception e) {
            fail(e.toString());
        }
        return data;
    }

    public void open_catalog_item_by_id(String id) {
        for (Integer i = 0; i < lst_catalog_items.size(); i++) {
            WebElement el = lst_catalog_items.get(i);
            String ret = el.getAttribute("data-catalogercod1s");
            if (id.equals(ret)) {
                el.click();
                WaitForAjax();
                return;
            }
        }
        fail("Не найдена карточка товара по id:" + id);
    }

    public void WaitForAjax() {
        try {
            while (true) {
                Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
                if (ajaxIsComplete) {
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            fail();
        }
    }
}
