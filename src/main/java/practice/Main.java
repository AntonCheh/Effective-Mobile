package practice;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class Main {
    /*
    TODO:
     - реализовать методы класса CoolNumbers
     - посчитать время поиска введимого номера в консоль в каждой из структуры данных
     - проанализоровать полученные данные
     */

    public static void main(String[] args) {
        CoolNumbers coolNumbers = new CoolNumbers();
        List<String> coolNumbersList = coolNumbers.generateCoolNumbers();
        TreeSet<String> coolNumbersTreeSet = new TreeSet<>(coolNumbersList);
        HashSet<String> coolNumbersHashSet = new HashSet<>(coolNumbersList);

        String searchNumber = "В248СС138";

        long startTime = System.nanoTime();
        boolean linearSearchResult = coolNumbers.bruteForceSearchInList(coolNumbersList, searchNumber);
        long endTime = System.nanoTime();
        long linearSearchTime = endTime - startTime;

        startTime = System.nanoTime();
        boolean binarySearchResult = coolNumbers.binarySearchInList(coolNumbersList, searchNumber);
        endTime = System.nanoTime();
        long binarySearchTime = endTime - startTime;

        startTime = System.nanoTime();
        boolean hashSetSearchResult = coolNumbers.searchInHashSet(coolNumbersHashSet, searchNumber);
        endTime = System.nanoTime();
        long hashSetSearchTime = endTime - startTime;

        startTime = System.nanoTime();
        boolean treeSetSearchResult = coolNumbers.searchInTreeSet(coolNumbersTreeSet, searchNumber);
        endTime = System.nanoTime();
        long treeSetSearchTime = endTime - startTime;

        System.out.println("Поиск перебором: " + (linearSearchResult ? "найден" : "не найден") + " - время поиска " + linearSearchTime + " нс");
        System.out.println("Бинарный поиск: " + (binarySearchResult ? "найден" : "не найден") + " - время поиска " + binarySearchTime + " нс");
        System.out.println("Поиск в HashSet: " + (hashSetSearchResult ? "найден" : "не найден") + " - время поиска " + hashSetSearchTime + " нс");
        System.out.println("Поиск в TreeSet: " + (treeSetSearchResult ? "найден" : "не найден") + " - время поиска " + treeSetSearchTime + " нс");



    }
}


