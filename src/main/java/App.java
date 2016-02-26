import java.util.Map;
import java.util.HashMap;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import java.util.List;

public class App {

  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    /******************************************************
      Students: TODO: Display all restaurants on main page
    *******************************************************/
    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");

      model.put("cuisines", Cuisine.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String cuisineType = request.queryParams("type");
      Cuisine newCuisine = new Cuisine(cuisineType);

      newCuisine.save();
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/restaurants", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String restaurantName = request.queryParams("name");
      String description = request.queryParams("description");
      Restaurant newRestaurant = new Restaurant(restaurantName, description);
      int cuisineId = Integer.parseInt(request.queryParams("cuisineId"));

      newRestaurant.save();
      if (cuisineId > 0) {
        newRestaurant.assignCuisine(cuisineId);
      }

      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clear", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");

      Restaurant.deleteAll();
      Cuisine.deleteAll();

      model.put("cuisines", Cuisine.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/untyped", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      List<Restaurant> restaurants = Cuisine.getUnassignedRestaurants();
      model.put("restaurants", restaurants);

      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      int id = Integer.parseInt(request.params(":id"));
      Cuisine newCuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      List<Restaurant> restaurants = newCuisine.getRestaurants();
      model.put("restaurants", restaurants);
      model.put("id", id);

      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/untyped/restaurants/:id2", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Restaurant newRestaurant = Restaurant.find(Integer.parseInt(request.params(":id2")));
      model.put("restaurant", newRestaurant);
      model.put("cuisines", Cuisine.all());

      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/:id/restaurants/:id2", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Cuisine newCuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      Restaurant newRestaurant = Restaurant.find(Integer.parseInt(request.params(":id2")));
      model.put("restaurant", newRestaurant);
      model.put("currentCuisine", newCuisine);
      model.put("cuisines", Cuisine.all());

      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/restaurants/:id/cuisineUpdate", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      int cuisineId = Integer.parseInt(request.queryParams("cuisineId"));
      Restaurant newRestaurant = Restaurant.find(Integer.parseInt(request.params(":id")));

      if (cuisineId > 0) {
        newRestaurant.assignCuisine(cuisineId);
      } else {
        newRestaurant.clearCuisine();
      }

      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());





    // get("/new-restaurant", (request, reponse) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   model.put("template", "templates/newrestaurant.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    /******************************************************
    STUDENTS:
    TODO: Create page to display information about the selected restaurant
    TODO: Create page to display restaurants by cuisine type
    *******************************************************/

  }
}
