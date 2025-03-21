
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Processor {

    public List<Car> cars;

    public Processor(List<Car> cars) {
        this.cars = cars;
    }

    public void processCars() {
        for (Car c : cars) {
            // Generating car info
            // Generare informații masina
            String cI = "Car: " + c.getBrand() + ", HorsePower: " + c.getHorsePower() + ", Color: " + c.getColor();
            System.out.println(cI);

            // Calculating average price of car functions
            // Calcularea pretului mediu al functiilor masinii
            double t = 0;
            for (CarFunction carFunction : c.getCarFunctions()) {
                t += carFunction.getPrice();
            }
            double avg = t / c.getCarFunctions().size();
            System.out.println("Average Price: " + avg);

            // Categorizing the average price of car functions
            // Clasificarea pretului mediu al functiilor masinii
            String cat;
            if (avg >= 900) {
                cat = "Expensive";
            } else if (avg >= 750) {
                cat = "Affordable";
            } else if (avg >= 500) {
                cat = "Cheap";
            } else {
                cat = "Promo";
            }
            System.out.println("Price Category: " + cat);
        }
    }

    // Metoda 1: Returnează o listă cu toate marcile de masini cu litere mici, avand culoarea "grey".
    public List<String> methodOne() {
        // Implementarea aici
        return cars.stream()
                .filter(car -> car.getColor().equals("grey"))
                .map(Car::getBrand)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    // Metoda 2: Returnează o listă cu marcile de masini care au pretul mediu al functiilor masinii mai mare sau egală cu 900, cu majuscule.
    public List<String> methodTwo() {
        // Implementarea aici
        return cars.stream()
                .filter(car -> car.getCarFunctions()
                        .stream()
                        .mapToDouble(CarFunction::getPrice)
                        .average()
                        .orElse(0) >= 900)
                .map(car -> car .getBrand().toUpperCase())
                .collect(Collectors.toList());
    }

    // Metoda 3: Returnează puterea medie a masinilor cu o culoare "red" si pretul mediu al functiilor masinii peste 600. Aruncă CarException dacă nu există astfel de masini.
    public double methodThree() {
        // Implementarea aici
        return cars.stream()
                .filter(car -> car.getColor().equals("red"))
                .filter(car -> car.getCarFunctions()
                        .stream()
                        .mapToDouble(CarFunction::getPrice)
                        .average()
                        .orElse(0) >= 600)
                .mapToInt(Car::getHorsePower)
                .average()
                .orElseThrow(CarException::new);
    }

    // Metodă de ajutor pentru methodFour pentru a obține toate numele distincte de functii ale masinii
    private Set<String> getAllCarFunctionNames() {
        Set<String> allSubjectNames = new HashSet<>();
        for (Car car : cars) {
            for (CarFunction carFunction : car.getCarFunctions()) {
                allSubjectNames.add(carFunction.getName());
            }
        }
        return allSubjectNames;
    }

    // Metoda 4: Returnează un dicționar (map) în care cheile sunt nume de functii ale masinii, iar valorile sunt liste de masini sortate dupa pretul functiei respective a masinii.
    // Lista ar trebui să includă numai masinile cu un pret peste 500 pentru functia respectiva a masinii.
    // Sugestie: Puteți utiliza metoda getPriceForCarFunction(String carFunctionName) din clasa Car care returnează pretul unei functii de masina după numele acesteia.
    public Map<String, List<Car>> methodFour() {
        Set<String> allCarFunctionNames = getAllCarFunctionNames();
        // Implementarea aici
        return getAllCarFunctionNames().stream()
                .collect(Collectors.toMap(
                        functionName -> functionName, // Key = numele funcției
                        functionName -> cars.stream()
                                .filter(car -> car.getPriceForCarFunction(functionName)
                                        .orElse(0.0) > 500) // Filtrare: doar mașinile cu preț > 500 pentru funcția respectivă
                                .sorted(Comparator.comparing(car -> car.getPriceForCarFunction(functionName).orElse(0.0))) // Sortare după prețul funcției
                                .collect(Collectors.toList()) // Colectare într-o listă
                ));
    }

    // Metoda 5: Tipărește marca masinilor care au un pret sub 500 pentru orice functie a masinii și returnează o listă cu aceste masini.
    public List<Car> methodFive() {
        // Implementarea aici
        return cars.stream()
                .filter(car -> car.getCarFunctions().stream()
                        .anyMatch(carFunction -> carFunction.getPrice() < 500))
                .peek(car -> System.out.println(car.getBrand()))
                .collect(Collectors.toList());
    }

    // Metoda 6: Returnează un dicționar (map) în care cheile sunt masini și valorile sunt preturile medii pentru functiile masinii.
    public Map<Car, Double> methodSix() {
        // Implementarea aici
        return cars.stream()
                .collect(Collectors.toMap(car->car,car -> car.getCarFunctions()
                        .stream()
                        .mapToDouble(CarFunction::getPrice)
                        .average()
                        .orElse(0.0))
                );
    }
}
