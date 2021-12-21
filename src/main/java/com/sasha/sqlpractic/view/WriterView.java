package com.sasha.sqlpractic.view;


import com.sasha.sqlpractic.controller.PostController;
import com.sasha.sqlpractic.controller.WriterController;
import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.model.Writer;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WriterView {
    private final String menu = "Выберите тип операции, введя соответсвующий номер";
    private final String menu1 = "Для добавления записи нажмите 1";
    private final String menu2 = "Для изменения существующей записи нажмите 2";
    private final String menu3 = "Для удаления существующей записи нажмите 3";
    private final String menu4 = "Для получения списка записей нажмите 4";
    private final String menu5 = "Для получения записи по id нажмите 5";
    private final String menu6 = "Для выхода в главное меню нажмите 6";
    private final String menu21 = "Для изменения First Name нажмите 1";
    private final String menu211 = "Введите First Name";
    private final String menu22 = "Для изменения Last Name нажмите 2";
    private final String menu221 = "Введите Last Name";
    private final String menu23 = "Для изменения списка Post нажмите 3";
    private final String menu231 = "Введите список id записей Post через запятую";
    private final String menu3151 = "Введите id записи";
    private final String menu32 = "Запись успешно удалена";
    private final String menu61 = "Вы успешно вышли";

    private Scanner scanner = new Scanner(System.in);
    private PostView postView = new PostView();
    private WriterController writerController = new WriterController();
    private PostController postController = new PostController();


    private void startMenu() {
        System.out.println(menu);
        System.out.println(menu1);
        System.out.println(menu2);
        System.out.println(menu3);
        System.out.println(menu4);
        System.out.println(menu5);
        System.out.println(menu6);
    }

    public void choiceMenuWriter() {
        int choice;
        do {
            startMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    newWriter();
                    break;
                case 2:
                    changeWriter();
                    break;
                case 3:
                    System.out.println(menu3151);
                    delWriter(scanner.nextInt());
                    break;
                case 4:
                    getAll();
                    break;
                case 5:
                    System.out.println(menu3151);
                    getById(scanner.nextInt());
                    break;
                case 6:
                    System.out.println(menu61);
                    break;
            }
        }
        while (choice != 6);
    }

    private void getById(Integer id) {
        System.out.println(writerController.getById(id));
    }

    private void getAll() {
        System.out.println(writerController.getAll());
    }

    private void delWriter(Integer id) {
        writerController.delete(id);
        System.out.println(menu32);
    }

    private void changeWriter() {
        System.out.println(menu3151);
        Writer writer = writerController.getById(scanner.nextInt());
        System.out.println(writer);
        System.out.println(menu21);
        System.out.println(menu22);
        System.out.println(menu23);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println(menu211);
                writer.setFirstName(scanner.next());
                break;
            case 2:
                System.out.println(menu221);
                writer.setLastName(scanner.next());
                break;
            case 3:
                postController.getAllPosts().stream().
                        forEach(a -> System.out.println(a));
                System.out.println(menu231);
                writer.setPosts(postIdsToList(scanner.next()));
                break;
        }

        System.out.println(writerController.update(writer));

    }

    private void newWriter() {
        Writer writer = new Writer();
        System.out.println(menu211);
        writer.setFirstName(scanner.next());
        System.out.println(menu221);
        writer.setLastName(scanner.next());
        System.out.println(menu231);
        postView.viewGetAllPosts();
        writer.setPosts(postIdsToList(scanner.next()));
        System.out.println(writerController.newWriter(writer));


    }

    private List<Post> postIdsToList(String posts) {
        return Arrays.stream(posts.split(","))
                .map(a -> postController.getPostById(Integer.valueOf(a)))
                .collect(Collectors.toList());
    }
}
