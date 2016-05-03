/*
 * File: Project.java
 * Author: A. Haddox
 * Class: CS 445 - Computer Graphics
 *
 * Assignment: Final Project
 * Date Last Modified: 5/3/2016
 *
 * Purpose: This is the driver for the project.
 */
package core;

import graphics.Viewport;

public class Project {
    /*
     * Method: main
     * Purpose: Creates the viewport and starts the game
     */
    public static void main(String[] args) {
        Viewport viewport = new Viewport();
        viewport.start();
    }
}
