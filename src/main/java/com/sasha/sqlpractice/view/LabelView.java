package com.sasha.sqlpractice.view;

import com.sasha.sqlpractice.controller.LabelController;
import com.sasha.sqlpractice.model.Label;

import java.util.Scanner;

public class LabelView {
    private final Scanner scanner = new Scanner(System.in);
    private final String menu = "Выберите тип операции, введя соответсвующий номер";
    private final String menu1 = "Для добавления записи нажмите 1";
    private final String menu2 = "Для изменения имени в существующей записи нажмите 2";
    private final String menu3 = "Для удаления существующего имени нажмите 3";
    private final String menu4 = "Для получения списка имен нажмите 4";
    private final String menu5 = "Для получения имени по id нажмите 5";
    private final String menu6 = "Для выхода в главное меню нажмите 6";
    private final String choiceMenu1 = "Введите имя";
    private final String choiceMenu2 = "Введите id имени";
    private final String choiceMenu3 = "Введите новое имя";
    private final String choiceMenu4 = "Введите id имени, котрое вы хотите удалить";
    private final String choiceMenu5 = "Введите id имени, которое вы хотите найти";
    private final String choiceMenu6 = "Вы успешно вышли";
    private final LabelController labelController = new LabelController();


    private void startMenu() {
        System.out.println(menu);
        System.out.println(menu1);
        System.out.println(menu2);
        System.out.println(menu3);
        System.out.println(menu4);
        System.out.println(menu5);
        System.out.println(menu6);
    }


    public void choiceMenuLabel() {
        int choice;
        do {
            startMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(choiceMenu1);
                    viewNewLabel(scanner.next());
                    break;
                case 2:
                    viewAllLabel();
                    System.out.println(choiceMenu2);
                    int id = scanner.nextInt();
                    System.out.println(choiceMenu3);
                    viewUpdateLabel(id, scanner.next());
                    break;
                case 3:
                    System.out.println(choiceMenu4);
                    viewAllLabel();
                    viewDeleteLabel(scanner.nextInt());
                    break;
                case 4:
                    viewAllLabel();
                    break;
                case 5:
                    System.out.println(choiceMenu5);
                    viewGetById(scanner.nextInt());
                    break;
                case 6:
                    System.out.println(choiceMenu6);
                    break;
            }
        } while (choice != 6);
    }

    public void viewGetById(Integer id) {
        System.out.println("Ваша запись найдена: " + labelController.getById(id));
    }

    private void viewDeleteLabel(Integer id) {
        labelController.deleteById(id);
        System.out.println("Ваша запись успешно удалена");
    }

    private void viewUpdateLabel(Integer id, String name) {
        System.out.println("Ваша запись обновлена: " + labelController.updateLabel (new Label(id, name)));
    }

    public void viewAllLabel() {
        System.out.println(labelController.getAll());
    }

    private void viewNewLabel(String name) {
        System.out.println("Ваша запись создана:  " +labelController.saveNewLabel(name).toString());
    }


}
