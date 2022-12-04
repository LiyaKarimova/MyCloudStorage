package com.liyakarimova.commands;

public enum CommandType {
    FILE_MESSAGE,
    FILE_REQUEST,
    LIST_REQUEST, //получить список файлов в директории
    LIST_RESPONSE, //
    PATH_REQUEST, // получить адрес текущей директории
    PATH_RESPONSE,
    PATH_UP_REQUEST,
    PATH_DOWN_REQUEST
}
