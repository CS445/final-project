/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author antzy_000
 */
public class Viewport {
    private final int FRAMERATE = 60;
    public void start() {
        try
        {
            createWindow();
            initGL();
            render();
        }
        catch(Exception e)
        {
            
        }
    }
    
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Final Project");
        Display.create();
    }
    
    private void initGL() {
        glClearColor(0.0f, 0.70f, 0.95f, 0.0f); //Sky Blue-ish
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        //glOrtho(0, 640, 0, 480, 1, -1);
        GLU.gluPerspective(100.0f, 640/480, 0.1f, 300.0f); //Needs a rewrite. Check 3D Viewing pdf
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    private void render() {
        while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            try
            {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                
                drawCube(2.0f, new Vector3(1, 1, -10)/*Vector3 rotation?*/);
                
                //handleInput();
                
                
                Display.update();
                Display.sync(FRAMERATE);
            }
            catch(Exception e)
            {
                
            }
        }
        
        Display.destroy();
    }
    
    private void drawCube(float size, Vector3 position) throws Exception{
        glTranslatef(position.x, position.y, position.z);
        glBegin(GL_QUADS);
            //Top
            glColor3f(0, 0, 0);
            
            glVertex3f(size, size, -size);
            glVertex3f(-size, size, -size);
            glVertex3f(-size, size, size);
            glVertex3f(size, size, size);
            
            //Bottom
            glVertex3f(size, -size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(-size, -size, -size);
            glVertex3f(size, size, -size);
            
            //Front
            glVertex3f(size, size, size);
            glVertex3f(-size, size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(size, -size, size);
            
            //Back
            glVertex3f(size, -size, -size);
            glVertex3f(-size,-size,-size);
            glVertex3f(-size, size, -size);
            glVertex3f(size, size, -size);
            
            //Left
            glVertex3f(-size, size, size);
            glVertex3f(-size, size, -size);
            glVertex3f(-size, -size, -size);
            glVertex3f(-size, -size, size);
            
            //Right
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
            glColor3f(0, 0, 0);
            glVertex3f(size, -size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(-size, -size, -size);
            glVertex3f(size, size, -size);
        glEnd();
        
        //Front
        glBegin(GL_LINE_LOOP);
            glColor3f(0, 0, 0);
            glVertex3f(size, size, size);
            glVertex3f(-size, size, size);
            glVertex3f(-size, -size, size);
            glVertex3f(size, -size, size);
        glEnd();
        
        //Back
        glBegin(GL_LINE_LOOP);
            glColor3f(0, 0, 0);
            glVertex3f(size, -size, -size);
            glVertex3f(-size,-size,-size);
            glVertex3f(-size, size, -size);
            glVertex3f(size, size, -size);
        glEnd();
        
        //Left
        glBegin(GL_LINE_LOOP);
            glColor3f(0, 0, 0);
            glVertex3f(-size, size, size);
            glVertex3f(-size, size, -size);
            glVertex3f(-size, -size, -size);
            glVertex3f(-size, -size, size);
        glEnd();
        
        //Right
        glBegin(GL_LINE_LOOP);
            glColor3f(0, 0, 0);
            glVertex3f(size, size, -size);
            glVertex3f(size, size, size);
            glVertex3f(size, -size, size);
            glVertex3f(size, -size, -size);
        glEnd();
    }
}
