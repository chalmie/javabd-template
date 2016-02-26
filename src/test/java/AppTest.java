import org.fluentlenium.adapter.FluentTest;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
      return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Add a cuisine to categorize restaurants");
  }

  @Test
  public void cuisineIsCreated() {
    goTo("http://localhost:4567/");
    fill("#type").with("Thai");
    submit("#cuisineBtn");
    assertThat(pageSource()).contains("Thai");
  }

  @Test
  public void restaurantWithCuisineIsCreated() {
    Cuisine testCuisine = new Cuisine("Thai");
    testCuisine.save();
    String clickstring = "#" + testCuisine.getId();
    goTo("http://localhost:4567/");
    fill("#name").with("Pok Pok");
    click(clickstring);
    fill("#description").with("5 hour wait");
    submit("#restaurantBtn");
    click("a", withText("Thai"));
    assertThat(pageSource()).contains("Pok Pok");
  }

  @Test
  public void restaurantWithoutCuisineIsCreated() {
    Restaurant testRestaurant = new Restaurant("Lardo", "Heavy Sandwiches");
    testRestaurant.save();
    goTo("http://localhost:4567/cuisines/untyped");
    assertThat(pageSource()).contains("Lardo");
  }

  @Test
  public void restaurantsPageDisplaysRestaurant() {
    Restaurant testRestaurant = new Restaurant("Lardo", "Heavy Sandwiches");
    testRestaurant.save();
    goTo("http://localhost:4567/cuisines/untyped");
    click("a", withText("Lardo"));
    assertThat(pageSource()).contains("Heavy Sandwiches");
  }

  @Test
  public void cuisineUpdateReturnsToRoot() {
    Restaurant testRestaurant = new Restaurant("Lardo", "Heavy Sandwiches");
    testRestaurant.save();
    Cuisine testCuisine = new Cuisine("Thai");
    testCuisine.save();
    String clickstring = "#" + testCuisine.getId();
    String restaurantPath = String.format("http://localhost:4567/cuisines/untyped/restaurants/%d", testRestaurant.getId());
    goTo(restaurantPath);
    click(clickstring);
    submit("#updateCuisineBtn");
    assertThat(pageSource()).contains("Add a cuisine to categorize restaurants");
  }

  @Test
  public void cuisineUpdateReturnsToRoot() {
    Restaurant testRestaurant = new Restaurant("Lardo", "Heavy Sandwiches");
    testRestaurant.save();
    Cuisine testCuisine = new Cuisine("Thai");
    testCuisine.save();
    String clickstring = "#" + testCuisine.getId();
    String restaurantPath = String.format("http://localhost:4567/cuisines/untyped/restaurants/%d", testRestaurant.getId());
    String cuisineAddedPath = String.format("http://localhost:4567/cuisines/%d/restaurants/%d", testCuisine.getId(), testRestaurant.getId());
    goTo(restaurantPath);
    click(clickstring);
    submit("#updateCuisineBtn");
    goTo(cuisineAddedPath);
    assertThat(pageSource()).contains("Thai");
  }


}
