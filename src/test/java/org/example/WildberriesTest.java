package org.example;


/*  Test Suite
СЦЕНАРИЙ ДОЛЖЕН ОСУЩЕСТВЛЯТЬ ОПИСАННЫЕ НИЖЕ ДЕЙСТВИЯ:
1. Открыть браузер и развернуть на весь экран.
2. Зайти на www.wildberries.ru
3. Перейти в раздел Электроника - Ноутбуки и компьютеры - ноутбуки
4. расширенный поиск - Задать параметр поиска до 40000 рублей и 1920x1080 разрешение. Сортировка по цене
6. Выбрать не менее 5 любых производителей, среди популярных.
7. Нажать кнопку Подобрать.
8. Проверить, что элементов на странице 20 - вывести соотвествующию ошибку если не так
9. Запомнить второй элемент в списке.
10. Изменить Сортировку на другую (популярность или рейтинг).
11. Найти и нажать по имени запомненного объекта.
12. Вывести цифровое количества отзывов.
13. Закрыть браузер.
ПРИМЕЧАНИЯ:
Предполагается полная свобода в действиях. При оценке в обязательном порядке будут учитываться:
- работоспособность сценария в нескольких браузерах Chrome и любой на выбор;
- гибкость и адаптивность сценария (насколько просто заменить часть исходных данных, например - города);
- «живучесть» сценария (обработка ошибок и исключений);

 */

import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.concurrent.TimeUnit;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WildberriesTest {
    private WebDriver driver;
    private String baseUrl = "https://www.wildberries.ru/";
    private WildberriesPage page;



    @AfterEach
    void dispose(){
        driver.close();
        page=null;
    }

    @Test
    void ChromeTest() {
        System.setProperty("webdriver.chrome.driver", "k:/chromedriver_32_85_0_4183_87.exe");
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(3840, 0));
        driver.manage().window().setSize(new Dimension(1920, 2100));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        page = new WildberriesPage(driver);
        work();
    }
    @Test
    void FirefoxTest() {
        System.setProperty("webdriver.gecko.driver", "k:/geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().window().setPosition(new Point(3840, 0));
        driver.manage().window().setSize(new Dimension(1920, 2100));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        page = new WildberriesPage(driver);
        work();
    }

    void work() {
        String id_item;
        String count_review;
        Integer min_elements_count_in_catalog=15;
        Integer find_element_index=3;

        page.goto_notebook_catalog();

        page.apply_filters();

        page.sortByPrice("pricedown");

        id_item=page.get_notebook_by_number(min_elements_count_in_catalog,find_element_index);

        page.sortByPrice("priceup");

        page.open_catalog_item_by_id(id_item);

        count_review=page.get_count_review();

        System.out.println("На товар с id:" + id_item + " оставлено:" + count_review + " отзывов");
    }


}
