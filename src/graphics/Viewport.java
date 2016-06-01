/*
 * File: Viewport.java
 * Author: A. Haddox
 * Class: CS 445 - Computer Graphics
 *
 * Assignment: Final Project
 * Date Last Modified: 6/1/2016
 *
 * Purpose: This class creates and initiates the GL window and camera.
 */
package graphics;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;

public class Viewport {
    private CameraControl cc;
    private DisplayMode displayMode;
    
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    
    /*
     * Method: start
     * Purpose: This method creates the viewport window and starts the main game loop
     */
    public void start() {
        try
        {
            createWindow();
            initGL();
            cc = new CameraControl(0, 0, 0);
            cc.gameLoop();
        }
        catch(Exception e)
        {
            
        }
    }
    
    /*
     * Method: createWindow
     * Purpose: This method sets the height and width of the viewport
     (          by checking the available display modes
     */
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for(int i = 0; 9 < d.length; i++) {
            if(d[i].getWidth() == 640 && d[i].getHeight() == 480
                                      && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Final Project");
        Display.create();
    }
    
    /*
     * Method: initGL
     * Purpose: This method sets the background color of the 3D space and creates
                a perspective view.
     */
    private void initGL() {
        glClearColor(0.0f, 0.70f, 0.95f, 0.0f); //Sky Blue-ish
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100, (float)displayMode.getWidth()/(float)displayMode.getHeight(), 0.1f, 300f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
    }
    
    /*
     * Method: initLightArrays
     * Purpose: This method sets up the light buffers
     */
    private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(0.0f).put(0.0f).put(1.0f).flip();
        
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
    }
    
    /*
     * Method: drawCube
     * Purpose: This method draws a cube according to a specified size and position.
     */
    public static void drawCube(float size, Vector3 position) throws Exception{
        glTranslatef(position.x, position.y, position.z);
        glBegin(GL_QUADS);
            //Top
            glColor3f(0, 0, 0);
            glVertex3f(size, size, -size);
            glVertex3f(-size, size, -size);
            glVertex3f(-size, size, size);
            glVertex3f(size, size, size);

            //Bottom
            glColor3f(1, 1, 1);
            glVertex3f(size, -size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(-size, -size, -size);
            glVertex3f(size, -size, -size);
            
            //Front
            glColor3f(0, 0, 1);
            glVertex3f(size, size, size);
            glVertex3f(-size, size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(size, -size, size);
            
            //Back
            glColor3f(0, 1, 0);
            glVertex3f(size, -size, -size);
            glVertex3f(-size,-size,-size);
            glVertex3f(-size, size, -size);
            glVertex3f(size, size, -size);
            
            //Left
            glColor3f(1, 0, 0);
            glVertex3f(-size, size, size);
            glVertex3f(-size, size, -size);
            glVertex3f(-size, -size, -size);
            glVertex3f(-size, -size, size);
            
            //Right
            glColor3f(1, 0, 1);
            glVertex3f(size, size, -size);
            glVertex3f(size, size, size);
            glVertex3f(size, -size, size);
            glVertex3f(size, -size, -size);
        glEnd();
        
        //Top
        glBegin(GL_LINE_LOOP);
            glColor3f(0, 0, 0);
            glVertex3f(size, size, -size);
            glVertex3f(-size, size, -size);
            glVertex3f(-size, size, size);
            glVertex3f(size, size, size);
        glEnd();
        
        //Bottom
        glBegin(GL_LINE_LOOP);
            glVertex3f(size, -size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(-size, -size, -size);
            glVertex3f(size, -size, -size);
        glEnd();
        
        //Front
        glBegin(GL_LINE_LOOP);
            glVertex3f(size, size, size);
            glVertex3f(-size, size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(size, -size, size);
        glEnd();
        
        //Back
        glBegin(GL_LINE_LOOP);
            glVertex3f(size, -size, -size);
            glVertex3f(-size,-size,-size);
            glVertex3f(-size, size, -size);
            glVertex3f(size, size, -size);
        glEnd();
        
        //Left
        glBegin(GL_LINE_LOOP);
            glVertex3f(-size, size, size);
            glVertex3f(-size, size, -size);
            glVertex3f(-size, -size, -size);
            glVertex3f(-size, -size, size);
        glEnd();
        
        //Right
        glBegin(GL_LINE_LOOP);
            glVertex3f(size, size, -size);
            glVertex3f(size, size, size);
            glVertex3f(size, -size, size);
            glVertex3f(size, -size, -size);
        glEnd();
    }
}
