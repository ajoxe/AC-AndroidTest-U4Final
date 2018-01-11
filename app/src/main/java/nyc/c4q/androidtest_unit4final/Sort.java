package nyc.c4q.androidtest_unit4final;

import java.util.List;

/**
 * Created by justiceo on 1/7/18.
 */

public class Sort {

    /**
     * Sorts a list using the selection sort algorithm.
     * See lecture on sorting: https://github.com/C4Q/AC-Android/tree/v2/DSA/sorting
     *
     * When `isAscending` is true, the list is sorted in ascending alphabetical order from a to z,
     * otherwise it is sorted in descending order from z to a.
     * @param list
     * @param isAscending
     */
    public static void selectionSort(List<String> list, boolean isAscending) {
        if (isAscending){
            selectionSortAscending(list);
        } else {
            selectionSortDescending(list);
        }
        // TODO: Implement selection sort.
        // You may not use Collections.sort or its equivalent
        // You may not implement another sorting algorithm that is not "selection sort"
        // Tip: Try a version without ordering first.
    }
    public static void selectionSortAscending(List<String> list){
        for (int n = list.size(); n > 0; n--) {
            String lastName = list.get(0);
            int index = list.indexOf(lastName);
            for (int i = 0; i < n; i++) {
                if (list.get(i).compareTo(lastName) > 0) {
                    lastName = list.get(i);
                    index = list.indexOf(lastName);
                }
            }
            list.set(index, list.set(n - 1, list.get(index)));
        }
    }

    public static void selectionSortDescending(List<String> list){
        for (int n = list.size(); n > 0; n--) {
            String firstName = list.get(0);
            int index = list.indexOf(firstName);
            for (int i = 0; i < n; i++) {
                if (list.get(i).compareTo(firstName) < 0) {
                    firstName = list.get(i);
                    index = list.indexOf(firstName);
                }
            }
            list.set(index, list.set(n - 1, list.get(index)));
        }
    }


}
