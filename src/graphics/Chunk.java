/*
 * File: Chunk.java
 * Author: J.Galloway, A. Haddox
 * Class: CS 445 - Computer Graphics
 *
 * Assignment: Final Project
 * Date Last Modified: 5/26/2016
 *
 * Purpose: This class holds block information as an aggregate. This class also renders the chunks.
 */

package graphics;

import java.io.IOException;
import util.SimplexNoise;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Chunk {
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    static final int MAX_HEIGHT = 10;
    static final int WATER_HEIGHT = 4;
    
    private Block[][][] Blocks;
    
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    
    private Texture texture;
    
    private int StartX, StartY, StartZ;
    
    private Random r;
    
    /*
     * Method: render
     * Purpose: This method renders the chunk data
     */
    public void render() {
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            
            glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2, GL_FLOAT, 0, 0L);
            
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }
    
    /*
     * Method: rebuildMesh
     * Purpose: This method places junks according to simplex noise generation.
     */
    public void rebuildMesh(float startX, float startY, float startZ) {
        
        long seedtime = System.currentTimeMillis();
        Random seed = new Random(seedtime);

        SimplexNoise noise = new SimplexNoise(5, .1f, seed.nextInt());
        
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);        
        
        for(float x = 0; x < CHUNK_SIZE; x += 1) {
            for(float z = 0; z < CHUNK_SIZE; z += 1) {
                for(float y = 0; y < MAX_HEIGHT; y++) {
                    float i = startX + x *((CHUNK_SIZE - startX) / 640);
                    float j = startY + z *((MAX_HEIGHT - startY) / 480);
                    float k = startZ + z *((CHUNK_SIZE - startZ) / 640);
                    
                    double noiseVal = noise.getNoise(i, j, k);
                    int height = Math.abs((int)(startY + (int)(100 * noiseVal) * CUBE_LENGTH));
                    
                    if(y <= height) {
                        VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH), (float) (y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)), (float) (startZ + z * CUBE_LENGTH)));
                        VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                        VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int)(x)][(int)(y)][(int)(z)]));
                        Blocks[(int)(x)][(int)(y)][(int)(z)].setActive((true));
                    }
                    
                }
            }
        }
        
        for(float x = 0; x < CHUNK_SIZE; x += 1) {
            for(float z = 0; z < CHUNK_SIZE; z += 1) {
                for(float y = 0; y < MAX_HEIGHT; y++) {

                    if(y < WATER_HEIGHT) {
                        if(!Blocks[(int)(x)][(int)(y)][(int)(z)].isActive()) {
                            Blocks[(int)(x)][(int)(y)][(int)(z)] = new Block(Block.BlockType.Water);
                            VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH), (float) (y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)), (float) (startZ + z * CUBE_LENGTH)));
                            VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                            VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int)(x)][(int)(y)][(int)(z)]));
                            Blocks[(int)(x)][(int)(y)][(int)(z)].setActive((true));
                        }
                    }
                }
            }
        }
        
        VertexColorData.flip();
        VertexPositionData.flip();
        VertexTextureData.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    /*
     * Method: createCubeVertexCol
     * Purpose: This method colors the cube vertices.
     */
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        
        for(int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        
        return cubeColors;
    }
    
    /*
     * Method: createTexCube
     * Purpose: This method creates textures the cubes according to their BlockType
     */
    public static float[] createTexCube(float x, float y, Block block) {
        float offset = (1024f/16) / 1024f;
        
        switch(block.getID())
        {     
            case 1://Stone
                return new float[] {
                //Bottom
                x + offset * 4, y + offset * 2,
                x + offset * 3, y + offset * 2,
                x + offset * 3, y + offset * 1,
                x + offset * 4, y + offset * 1,

                //Top
                x + offset * 4, y + offset * 2,
                x + offset * 3, y + offset * 2,
                x + offset * 3, y + offset * 1,
                x + offset * 4, y + offset * 1,

                //Front
                x + offset * 4, y + offset * 2,
                x + offset * 3, y + offset * 2,
                x + offset * 3, y + offset * 1,
                x + offset * 4, y + offset * 1,

                //Back
                x + offset * 4, y + offset * 2,
                x + offset * 3, y + offset * 2,
                x + offset * 3, y + offset * 1,
                x + offset * 4, y + offset * 1,

                //Left
                x + offset * 4, y + offset * 2,
                x + offset * 3, y + offset * 2,
                x + offset * 3, y + offset * 1,
                x + offset * 4, y + offset * 1,

                //Right
                x + offset * 4, y + offset * 2,
                x + offset * 3, y + offset * 2,
                x + offset * 3, y + offset * 1,
                x + offset * 4, y + offset * 1 };
                
            case 2://Dirt
                return new float[] {
                //Bottom
                x + offset * 2, y + offset * 0,
                x + offset * 3, y + offset * 0,
                x + offset * 3, y + offset * 1,
                x + offset * 2, y + offset * 1,

                //Top
                x + offset * 2, y + offset * 0,
                x + offset * 3, y + offset * 0,
                x + offset * 3, y + offset * 1,
                x + offset * 2, y + offset * 1,

                //Front
                x + offset * 2, y + offset * 0,
                x + offset * 3, y + offset * 0,
                x + offset * 3, y + offset * 1,
                x + offset * 2, y + offset * 1,

                //Back
                x + offset * 2, y + offset * 0,
                x + offset * 3, y + offset * 0,
                x + offset * 3, y + offset * 1,
                x + offset * 2, y + offset * 1,

                //Left
                x + offset * 2, y + offset * 0,
                x + offset * 3, y + offset * 0,
                x + offset * 3, y + offset * 1,
                x + offset * 2, y + offset * 1,

                //Right
                x + offset * 2, y + offset * 0,
                x + offset * 3, y + offset * 0,
                x + offset * 3, y + offset * 1,
                x + offset * 2, y + offset * 1 };
                
            case 3://Sand
                return new float[] {
                //Bottom
                x + offset * 2, y + offset * 1,
                x + offset * 3, y + offset * 1,
                x + offset * 3, y + offset * 2,
                x + offset * 2, y + offset * 2,

                //Top
                x + offset * 2, y + offset * 1,
                x + offset * 3, y + offset * 1,
                x + offset * 3, y + offset * 2,
                x + offset * 2, y + offset * 2,

                //Front
                x + offset * 2, y + offset * 1,
                x + offset * 3, y + offset * 1,
                x + offset * 3, y + offset * 2,
                x + offset * 2, y + offset * 2,

                //Back
                x + offset * 2, y + offset * 1,
                x + offset * 3, y + offset * 1,
                x + offset * 3, y + offset * 2,
                x + offset * 2, y + offset * 2,

                //Left
                x + offset * 2, y + offset * 1,
                x + offset * 3, y + offset * 1,
                x + offset * 3, y + offset * 2,
                x + offset * 2, y + offset * 2,

                //Right
                x + offset * 2, y + offset * 1,
                x + offset * 3, y + offset * 1,
                x + offset * 3, y + offset * 2,
                x + offset * 2, y + offset * 2 };
                
            case 4://Grass
                return new float[] {
                //Bottom
                x + offset * 3, y + offset * 10,
                x + offset * 2, y + offset * 10,
                x + offset * 2, y + offset * 9,
                x + offset * 3, y + offset * 9,

                //Top
                x + offset * 3, y + offset * 1,
                x + offset * 2, y + offset * 1,
                x + offset * 2, y + offset * 0,
                x + offset * 3, y + offset * 0,

                //Front
                x + offset * 3, y + offset * 0,
                x + offset * 4, y + offset * 0,
                x + offset * 4, y + offset * 1,
                x + offset * 3, y + offset * 1,

                //Back
                x + offset * 4, y + offset * 1,
                x + offset * 3, y + offset * 1,
                x + offset * 3, y + offset * 0,
                x + offset * 4, y + offset * 0,

                //Left
                x + offset * 3, y + offset * 0,
                x + offset * 4, y + offset * 0,
                x + offset * 4, y + offset * 1,
                x + offset * 3, y + offset * 1,

                //Right
                x + offset * 3, y + offset * 0,
                x + offset * 4, y + offset * 0,
                x + offset * 4, y + offset * 1,
                x + offset * 3, y + offset * 1 };
                
            case 5://Water
                return new float[] {
                //Bottom
                x + offset * 14, y + offset * 12,
                x + offset * 15, y + offset * 12,
                x + offset * 15, y + offset * 13,
                x + offset * 14, y + offset * 13,

                //Top
                x + offset * 14, y + offset * 12,
                x + offset * 15, y + offset * 12,
                x + offset * 15, y + offset * 13,
                x + offset * 14, y + offset * 13,

                //Front
                x + offset * 14, y + offset * 12,
                x + offset * 15, y + offset * 12,
                x + offset * 15, y + offset * 13,
                x + offset * 14, y + offset * 13,

                //Back
                x + offset * 14, y + offset * 12,
                x + offset * 15, y + offset * 12,
                x + offset * 15, y + offset * 13,
                x + offset * 14, y + offset * 13,

                //Left
                x + offset * 14, y + offset * 12,
                x + offset * 15, y + offset * 12,
                x + offset * 15, y + offset * 13,
                x + offset * 14, y + offset * 13,

                //Right
                x + offset * 14, y + offset * 12,
                x + offset * 15, y + offset * 12,
                x + offset * 15, y + offset * 13,
                x + offset * 14, y + offset * 13 };
                
            case 6: 
            default: //Bedrock
                return new float[] {
                //Bottom
                x + offset * 4, y + offset * 2,
                x + offset * 5, y + offset * 2,
                x + offset * 5, y + offset * 3,
                x + offset * 4, y + offset * 3,

                //Top
                x + offset * 4, y + offset * 2,
                x + offset * 5, y + offset * 2,
                x + offset * 5, y + offset * 3,
                x + offset * 4, y + offset * 3,

                //Front
                x + offset * 4, y + offset * 2,
                x + offset * 5, y + offset * 2,
                x + offset * 5, y + offset * 3,
                x + offset * 4, y + offset * 3,

                //Back
                x + offset * 4, y + offset * 2,
                x + offset * 5, y + offset * 2,
                x + offset * 5, y + offset * 3,
                x + offset * 4, y + offset * 3,

                //Left
                x + offset * 4, y + offset * 2,
                x + offset * 5, y + offset * 2,
                x + offset * 5, y + offset * 3,
                x + offset * 4, y + offset * 3,

                //Right
                x + offset * 4, y + offset * 2,
                x + offset * 5, y + offset * 2,
                x + offset * 5, y + offset * 3,
                x + offset * 4, y + offset * 3 };
        }
    }
    
    /*
     * Method: createCube
     * Purpose: This method creates returns an array of cube vertices.
     */
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        
        return new float[] {
            //Top
            x + offset, y+ offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            
            //Bottom
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            
            //Front
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            
            //Back
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            
            //Left
            x - offset,  y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            
            //Right
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z };
    } 

    /*
     * Method: getCubeColor
     * Purpose: This method returns a white cube so 100% of all colors are present
     */
    private float[] getCubeColor(Block block) {
        return new float[] { 1, 1, 1 };
    }
    
    public Chunk(int startX, int startY, int startZ) {
        try
        {
            //texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/terrain.png"));
            texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("textures/tex2.jpg"));
        }
        catch(Exception e)
        {
            System.out.println("Cannot load texture");
        }
        
        r = new Random();
        
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        
        for(int x = 0; x < CHUNK_SIZE; x++) {
            for(int y = 0; y < CHUNK_SIZE; y++) {
                for(int z = 0; z < CHUNK_SIZE; z++) {
                    if(y < 2)
                        Blocks[x][y][z] = new Block(Block.BlockType.Bedrock);
                    else if(Blocks[x][y - 1][z].getBlockType() == Block.BlockType.Water) {
                        Blocks[x][y][z] = new Block(Block.BlockType.Water);
                    }
                    
                    else if(y > 3 && y % 2 == 0) {
                        if(r.nextFloat() > .85f)
                            Blocks[x][y][z] = new Block(Block.BlockType.Sand);
                        else
                            Blocks[x][y][z] = new Block(Block.BlockType.Grass);
                    }
                    
                    else if(y >=2 && y < 8) {
                        if(y >= 6) {
                            if(r.nextFloat() > .3f) {
                                Blocks[x][y][z] = new Block(Block.BlockType.Dirt);
                            }
                            else {
                                Blocks[x][y][z] = new Block(Block.BlockType.Stone);
                            }
                        }
                        else if(r.nextFloat() > .3f) {
                            Blocks[x][y][z] = new Block(Block.BlockType.Dirt);
                        }
                        else {
                            Blocks[x][y][z] = new Block(Block.BlockType.Stone);
                        }
                    }
                    
                    else {
                        if(r.nextFloat() > .85f)
                            Blocks[x][y][z] = new Block(Block.BlockType.Sand);
                        else
                            Blocks[x][y][z] = new Block(Block.BlockType.Grass);
                    }
                }
            }
        }
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        
        rebuildMesh(startX, startY, startZ);
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void setTexture(String path, String ending) throws IOException{
        texture = TextureLoader.getTexture(ending, ResourceLoader.getResourceAsStream(path));
    }
    
    public void rebuildHandles() {
        VBOTextureHandle = glGenBuffers();
    }
}
