/*
 * File: Vector3.java
 * Author: A. Haddox
 * Class: CS 445 - Computer Graphics
 *
 * Assignment: Final Project
 * Date Last Modified: 5/3/2016
 *
 * Purpose: This is a data container for 3 dimensional float vectors
 */
package graphics;

public class Vector3 {
    public float x, y, z;
    
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(double x, double y, double z) {
        this.x = (float)x;
        this.y = (float) y;
        this.z = (float) z;
    }
    
    public Vector3(int x, int y, int z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }
    
    public Vector3(Vector3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }
}
