package com.tenco;

import com.tenco.view.LibraryView;
import com.tenco.view.LibraryView_self;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        LibraryView libraryView = new LibraryView();
        libraryView.start();
    }
}