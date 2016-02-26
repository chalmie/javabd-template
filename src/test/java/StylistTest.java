import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;

public class StylistTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
      assertEquals(Stylist.all().size(), 0);
  }

  @Test
  public void save_addsAllInstancesOfStylistToList() {
    Stylist testStylist = new Stylist("Thai");
    Stylist testStylist1 = new Stylist("BBQ");
    testStylist.save();
    testStylist1.save();
    assertEquals(2, Stylist.all().size());
  }

  @Test
  public void update_changesStylistName() {
    Stylist testStylist = new Stylist("Thai");
    testStylist.save();
    testStylist.update("Indian");
    assertEquals("Indian", testStylist.getName());
  }

  @Test
  public void delete_removesStylistFromDatabase() {
    Stylist testStylist = new Stylist("Thai");
    testStylist.save();
    testStylist.delete();
    assertEquals(0, Stylist.all().size());
  }

  @Test
  public void find_findsInstanceOfStylistById() {
    Stylist testStylist = new Stylist("Indian");
    testStylist.save();
    assertEquals(Stylist.find(testStylist.getId()), testStylist);
  }

  @Test
  public void getClients_getAllClientsWithinAStylist() {
    Stylist testStylist = new Stylist("Sandwiches");
    testStylist.save();
    Client testClient = new Client("Bunk", "Sandwich shop");
    Client testClient1 = new Client("Lardo", "Pork-centric sandwich shop");
    testClient.save();
    testClient1.save();
    testClient.assignStylist(testStylist.getId());
    testClient1.assignStylist(testStylist.getId());
    assertEquals(2,testStylist.getClients().size());
  }

  @Test
  public void getUnassignedClients_getAllClientsWithoutAStylist() {
    Stylist testStylist = new Stylist("Sandwiches");
    testStylist.save();
    Client testClient = new Client("Bunk", "Sandwich shop");
    Client testClient1 = new Client("Lardo", "Pork-centric sandwich shop");
    testClient.save();
    testClient1.save();
    testClient.assignStylist(testStylist.getId());
    assertEquals(1,Stylist.getUnassignedClients().size());
  }
}
