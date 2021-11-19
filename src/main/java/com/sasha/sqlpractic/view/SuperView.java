package com.sasha.sqlpractic.view;

import java.util.Scanner;

public class SuperView {
    private final String menu = "С чем вы хотите работать";
    private final String menu1 = "Для работы с com.sasha.sqlpractice.model.Label нажмите 1";
    private final String menu2 = "Для работы с com.sasha.sqlpractice.model.Post нажмите 2";
    private final String menu3 = "Для работы с com.sasha.sqlpractice.model.Writer нажмите 3";
    private final String menu4 = "Для выходы нажмите 4";
    private final String menu41 = "Вы успешно вышли";
    private final Scanner scanner = new Scanner(System.in);
    LabelView labelView = new LabelView();
    PostView postView = new PostView();
    WriterView writerView = new WriterView();

    private void startMenu() {
        System.out.println(menu);
        System.out.println(menu1);
        System.out.println(menu2);
        System.out.println(menu3);
        System.out.println(menu4);
    }

    public void choiceMenu() {
        int choice;
        do {
            startMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    labelView.choiceMenuLabel();
                    break;
                case 2:
                    postView.choiceMenuPost();
                    break;
                case 3:
                    writerView.choiceMenuWriter();
                    break;
                case 4:
                    System.out.println(menu41);
                    break;
            }

        } while (choice != 4);

    }
}
