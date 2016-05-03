/*
 * File: CameraControl.java
 * Author: J. Galloway
 * Class: CS 445 - Computer Graphics
 *
 * Assignment: Final Project
 * Date Last Modified: 5/3/2016
 *
 * Purpose: This class defines the first person view camera for the application
 */
package graphics;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

public class CameraControl {
    private Vector3f position = null;
    private Vector3f lPosition = null;
    private float ylook = 0.0f;
    private float xlook = 0.0f;
    private Vector3 me;
    
    public CameraControl (float x, float y, float z)
    {
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
    }
    
    /*
     * Method: ylook
     * Purpose: Adjusts the yaw (up/down) rotation of the camera
     */
    public void ylook(float adjust)
    {
        ylook += adjust;
    }
    
    /*
     * Method: xlook
     * Purpose: Adjusts the pitch (left/right) rotation of the camera
     */
    public void xlook(float adjust)
    {
        xlook += adjust;
    }
    
    /*
     * Method: walkForward
     * Purpose: Moves the camera in its forward vector direction
     */
    public void walkForward(float dist)
    {
        position.x -= dist * (float)Math.sin(Math.toRadians(ylook));
        position.z += dist * (float)Math.cos(Math.toRadians(ylook));
    }
    
    /*
     * Method: walkBackward
     * Purpose: Moves the camera opposite to its forward vector direction
     */
    public void walkBackward(float dist)
    {
        position.x += dist * (float)Math.sin(Math.toRadians(ylook));
        position.z -= dist * (float)Math.cos(Math.toRadians(ylook));
    }
    
    /*
     * Method: strafeL
     * Purpose: Moves the camera 90 deg. left of its forward vector direction
     */
    public void strafeL(float dist)
    {
        position.x -= dist * (float)Math.sin(Math.toRadians(ylook-90));
        position.z += dist * (float)Math.cos(Math.toRadians(ylook-90));
    }
    
    /*
     * Method: strafeR
     * Purpose: Moves the camera 90 deg. right of its forward vector direction
     */
    public void strafeR(float dist)
    {
        position.x -= dist * (float)Math.sin(Math.toRadians(ylook+90));
        position.z += dist * (float)Math.cos(Math.toRadians(ylook+90));
    }
    
    /*
     * Method: floatUp
     * Purpose: Moves the camera in the direction of its up vector
     */
    public void floatUp(float dist)
    {
        position.y -= dist;
    }
    
    /*
     * Method: dropDown
     * Purpose: Moves the camera in the opposite direction of its up vector
     */
    public void dropDown(float dist)
    {
        position.y += dist;
    }
    
    /*
     * Method: cameraPos
     * Purpose: Positions the camera and rotates it in the 3D space according to
                its pitch, yaw, and position
     */
    public void cameraPos()
    {
        glRotatef(xlook, 1.0f, 0.0f, 0.0f);
        glRotatef(ylook, 0.0f, 1.0f, 0.0f);
        glTranslatef(position.x, position.y, position.z);
    }
    
    /*
     * Method: gameLoop
     * Purpose: Handles user input to position the camera and renders objects in the
                3D space
     */
    public void gameLoop() {
        CameraControl camera = new CameraControl(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        float mouseSensitivity = 0.20f;
        float movementSpeed = 0.35f;
        
        Mouse.setGrabbed(true);
        
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            lastTime = time;
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            
            camera.ylook(dx * mouseSensitivity);
            camera.xlook(-dy* mouseSensitivity); //Negative dy to have mouse pushed up rotate the camera up
            
            if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                camera.walkForward(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)|| Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                camera.walkBackward(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                camera.strafeL(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                camera.strafeR(movementSpeed);
            }
            
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.floatUp(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
                camera.dropDown(movementSpeed);
            }
            
            glLoadIdentity();
            camera.cameraPos();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            render();
            
            Display.update();
            Display.sync(60);
        }
        
        Display.destroy();
    }
    
    /*
     * Method: render
     * Purpose: Handles rendering objects in 3D space
     */
    private void render() {
        try
        {
            Viewport.drawCube(2.0f, new Vector3(5, 0, 5)/*Vector3 rotation?*/);
        }
        catch(Exception e)
        {
            
        }
    }
}