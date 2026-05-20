Feature: [MVIDEO] Проверка поиска товаров на сайте М.Видео

  Background:
    Given I open Mvideo website
    And I open catalog
    And I hover over electronics section

  Scenario Outline: Поиск ноутбуков с фильтрацией по цене и брендам
    When I navigate to "<laptopCategory>" section
    And I set price range from "<priceMin>" to "<priceMax>"
    And I select brands "<brands>"
    Then I should see at least "<minCount>" products
    And all products should contain selected brands
    And all prices should be within range "<priceMin>" to "<priceMax>"
    When I get first product name
    And I search for this product
    Then search results should contain the product
    And I verify all assertions

    Examples:
      | laptopCategory | priceMin | priceMax | brands                   | minCount |
      | Ноутбуки       | 15000    | 100000   | HP, Lenovo, Huawei       | 10       |
      | Ноутбуки       | 15000    | 100000   | Samsung, Honor, Apple    | 50       |
      | Ноутбуки       | 15000    | 100000   | Asus, Packard Bell, Sony | 150      |