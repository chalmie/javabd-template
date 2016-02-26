import org.sql2o.*;
import java.util.List;

public class Stylist {
  private int id;
  private String name;

  public Stylist (String name) {
    this.name = name;
  }

// Stylist.find(testRestaurant.getStylistId()).getType()

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object otherStylist){
    if (!(otherStylist instanceof Stylist)) {
      return false;
    } else {
      Stylist newStylist = (Stylist) otherStylist;
      return this.getType().equals(newStylist.getType()) &&
        this.getId() == newStylist.getId();
    }
  }

  //CREATE
  public void save() {
    try (Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO stylists(name) VALUES (:name)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .executeUpdate()
      .getKey();
    }
  }

  //READ
  public static List<Stylist> all() {
    String sql = "SELECT id, name FROM cuisine";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  //UPDATE
  public void update(String newName) {
    this.name = newName;
    String sql = "UPDATE stylists SET name = :name WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("name", newName)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
//
  //DELETE
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM stylists WHERE id = :id";
        con.createQuery(sql)
          .addParameter("id", id)
          .executeUpdate();
    }
  }

  public static Stylist find(int id) {
    String sql = "SELECT id, name FROM stylists WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Stylist.class);
    }
  }

  public List<Restaurant> getClients() {
    String sql = "SELECT * FROM clients WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Restaurant.class);
    }
  }

  public static List<Restaurant> getUnassignedRestaurants() {
    String sql = "SELECT * FROM restaurants WHERE stylist_id IS NULL";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .executeAndFetch(Restaurant.class);
    }
  }

  public static void deleteAll(){
  try(Connection con = DB.sql2o.open()) {
    String deleteStylistQuery = "DELETE FROM stylists *;";
    con.createQuery(deleteStylistQuery).executeUpdate();
    }
  }



//   /******************************************************
//     Students:
//     TODO: Create method to get cuisine
//   *******************************************************/
//
}
