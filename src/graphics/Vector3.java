/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

/**
 *
 * @author antzy_000
 */
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
