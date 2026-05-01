package selenium.interfaces;

import selenium.models.FilterSelectionResult;
import org.openqa.selenium.By;
import java.util.List;

/**
 * Действия с фильтрами
 */
public interface FilterActions {

    /**
     * Выбор чекбоксов с возвратом полной информации
     */
    FilterSelectionResult selectCheckboxesWithResult(By searchField, String checkboxTemplate, List<String> items);

    /**
     * Выбор чекбоксов из списка
     */
    List<String> selectCheckboxes(By searchField, String checkboxTemplate, List<String> items);

    /**
     * Выбор радио-кнопок
     */
    void selectRadio(By radioLocator);

    /**
     * Выбор из выпадающего списка
     */
    void selectFromDropdown(By dropdown, String option);
}
