import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Random;
import java.text.SimpleDateFormat;

class CarDealership {
  ArrayList<Car> cars;
  // new salesteam/accountingSystem objects created
  SalesTeam salesTeam = new SalesTeam();
  AccountingSystem accountingSystem = new AccountingSystem();
  // Filters
  boolean electricFilter = false;
  boolean priceFilter = false;
  double priceMin = 0;
  double priceMax = 0;
  boolean AWDFilter = false;

  // created new instance viariable to help aid buyCar and returnCar
  Car removedCar;
  Car boughtCar;
  int randomNumber;
  String salesPerson;
  int randomMonth;
  int randomDay;
  int RETDay;
  Calendar date;
  String type;
  double salePrice;
  String realDate;

  public CarDealership() {
    cars = new ArrayList<Car>();
  }

  public void addCars(ArrayList<Car> newCars) {
    cars.addAll(newCars);
    newCars.clear();
  }

  // public Car buyCar(int index)
  // {
  // if (index < 0 || index > cars.size()-1)
  // return null;

  // return (Car) cars.remove(index);
  // }

  // public void returnCar(Car car) {
  // if (car != null)
  // cars.add(car);
  // }

  public void displayInventory() {
    System.out.println("");

    for (int i = 0; i < cars.size() - 1; i++) {
      Car car = cars.get(i);

      if (priceFilter && (car.price < priceMin || car.price > priceMax))
        continue;

      if (electricFilter && !car.power.equals("ELECTRIC_MOTOR"))
        continue;

      if (AWDFilter && !car.AWD.equals("AWD"))
        continue;

      System.out.println("" + i + " " + car.display());
    }
    System.out.println("");
  }

  public void filtersClear() {
    electricFilter = false;
    priceFilter = false;
    AWDFilter = false;
  }

  public void filterByPrice(double min, double max) {
    priceFilter = true;
    priceMin = min;
    priceMax = max;
  }

  public void filterByElectric() {
    electricFilter = true;
  }

  public void filterByAWD() {
    AWDFilter = true;
  }

  public void sortByPrice() {
    Collections.sort(cars);
  }

  private class SafetyRatingComparator implements Comparator<Car> {
    public int compare(Car a, Car b) {
      if (a.safetyRating < b.safetyRating)
        return 1;
      else if (a.safetyRating > b.safetyRating)
        return -1;
      else
        return 0;
    }
  }

  public void sortBySafetyRating() {
    Collections.sort(cars, new SafetyRatingComparator());
  }

  private class RangeComparator implements Comparator<Car> {
    public int compare(Car a, Car b) {
      if (a.maxRange < b.maxRange)
        return 1;
      else if (a.maxRange > b.maxRange)
        return -1;
      else
        return 0;
    }
  }

  public void sortByMaxRange() {
    Collections.sort(cars, new RangeComparator());
  }

  /**
   * 
   * @param VIN buys a car from the arraylist of transactions, displays it, then
   *            removing that transaction out of the list
   *///
  public String buyCar(int VIN) throws IllegalArgumentException {
    if(cars.isEmpty() == true)
    {
      throw new IllegalArgumentException("List of cars are empty");
    }
    for (int i = 0; i < cars.size(); i++) {

      if (cars.get(i).getVIN() == VIN) {
        this.removedCar = cars.remove(i);

      }

    }
    Random random = new Random();
    this.salesPerson = salesTeam.randomSalesPerson();
    this.randomMonth = random.nextInt(12) + 1;
    this.randomDay = random.nextInt(31) + 1;
    this.date = new GregorianCalendar(2019, randomMonth, randomDay);
    // SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy"); //here i
    // tried to format the date but the problem
    // this.realDate = sdf.format(date.getTime()); //was that i had to make the date
    // a String which would of messed up my code
    this.type = "BUY";
    this.salePrice = removedCar.getPrice();
    return accountingSystem.add(this.date, this.removedCar, this.salesPerson, this.type, this.salePrice);
    

  }

  /**
   * 
   * @param transaction this gets a bought transaction with the help of id, then
   *                    return that car back to the car list
   *///
  public void returnCar(int transaction) {
    
    Transaction trans = accountingSystem.getTransaction(transaction);
    Random random = new Random();
    RETDay = (int) (Math.random() * 30) + this.date.get(Calendar.DAY_OF_MONTH); // random day above the previous day
    Calendar retDate = new GregorianCalendar(2019, this.date.get(Calendar.MONTH), RETDay);
    // SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy"); //here i
    // tried to format the date but the problem
    // realDate = sdf.format(retDate.getTime()); //was that i had to make the date a
    // String which would of messed up my code
    this.type = "BUY";
    accountingSystem.add(retDate, this.removedCar, this.salesPerson, "RET", this.salePrice);
    cars.add(this.removedCar);
    System.out.println(trans.display());
    
  
    }
   

  

  // this is to help with the simulator which prints all sales
  public void printSales() {
    accountingSystem.allTransaction();
  }

  // this is to help with the simulator with prints the sales team
  public String printTeam() {
    return salesTeam.allSalesPerson();
  }

  // this is to help with the simulator which prints the top sales person
  public String printTopSales() {
    return accountingSystem.topSales();
  }

  /**
   * 
   * @param month this is to help with the simulator which prints the trasaction
   *              of a certain month
   *///
  public String printMonthSales(int month) {
    return accountingSystem.monthSales(month);
  }

  // this is to help with the simulator which prints out the final Sales stats
  public String printStats() {
    return accountingSystem.STATS();
  }

}
