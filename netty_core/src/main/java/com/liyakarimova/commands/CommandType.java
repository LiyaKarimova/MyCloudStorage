package com.liyakarimova.commands;

public enum CommandType {
    FILE_MESSAGE, // отправляю файл, запиши его
    FILE_REQUEST, // удачно записал файл
    LIST_REQUEST, //получить список файлов в директории
    LIST_RESPONSE, // список файлов
    PATH_REQUEST, // получить адрес текущей директории
    PATH_RESPONSE, // адрес директории
    PATH_UP_REQUEST, // подняться на директорию выше
    PATH_IN_REQUEST // провалится в директорию
}
