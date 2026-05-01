package practice;

import java.util.*;

public class CoolNumbers {

    private static final String[] LETTERS = {"А", "В", "Е", "К", "М", "Н", "О", "Р", "С", "Т", "У", "Х"};
    private static final String[] NUMBERS = new String[10];
    private static final int MAX_REGION = 199;
    private static final int MIN_REGION = 1;
    private static final int MIN_COUNT = 2_000_000;

    static {
        for (int i = 0; i < NUMBERS.length; i++) {
            NUMBERS[i] = String.valueOf(i);
        }
    }

    public static List<String> generateCoolNumbers() {
        List<String> coolNumbers = new ArrayList<>();
        Random random = new Random();

        while (coolNumbers.size() < MIN_COUNT) {
            String letter1 = LETTERS[random.nextInt(LETTERS.length)];
            String letter2 = LETTERS[random.nextInt(LETTERS.length)];
            String letter3 = LETTERS[random.nextInt(LETTERS.length)];
            String number1 = NUMBERS[random.nextInt(NUMBERS.length)];
            String number2 = NUMBERS[random.nextInt(NUMBERS.length)];
            String number3 = NUMBERS[random.nextInt(NUMBERS.length)];
            int region = MIN_REGION + random.nextInt(MAX_REGION);
            String coolNumber = letter1 + number1 + number2 + number3 + letter2 + letter3 + region;
            coolNumbers.add(coolNumber);
        }
        return coolNumbers;
    }

    public static boolean bruteForceSearchInList (List<String> list, String number) {
        for (String coolNumber : list) {
            if (coolNumber.equals(number)) {
                return true;
            }
        }
        return false;
    }

    public static boolean binarySearchInList(List<String> sortedList, String number) {

        int left = 0;
        int right = sortedList.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            String midNumber = sortedList.get(mid);
            int compareResult = midNumber.compareTo(number);
            if (compareResult == 0) {
                return true;
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return false;
    }

    public static boolean searchInHashSet(HashSet<String> hashSet, String number) {
        return hashSet.contains(number);
    }

    public static boolean searchInTreeSet(TreeSet<String> treeSet, String number) {
        return treeSet.contains(number);
    }
}







