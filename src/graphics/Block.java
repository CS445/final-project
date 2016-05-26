/*
 * File: Block.java
 * Author: A. Haddox
 * Class: CS 445 - Computer Graphics
 *
 * Assignment: Final Project
 * Date Last Modified: 5/20/2016
 *
 * Purpose: This class holds block information.
 */
package graphics;

public class Block {
    private boolean active;
    private BlockType type;
    private Vector3 position;
    
    
    public enum BlockType {
        Bedrock(0),
        Stone(1),
        Dirt(2),
        Sand(3),
        Grass(4),
        Water(5);
        
        private int BlockID;
        
        BlockType(int i) {
            BlockID = i;
        }
        
        public int getID() {
            return BlockID;
        }
        
        public void setID(int i) {
            BlockID =i;
        }
    }
    
    public Block(BlockType type) {
        this.type = type;
    }
    
    /*
     * Method: setPosition
     * Purpose: This method sets the position vector
     */
    public void setPosition(Vector3 pos) {
        position = new Vector3(pos);
    }
    
    /*
     * Method: setPosition
     * Purpose: This method sets the position vector
     */
    public void setPosition(float x, float y, float z) {
        position = new Vector3(x, y, z);
    }
    
    /*
     * Method: isActive
     * Purpose: This method returns the active state of the block
     */
    public boolean isActive() {
        return active;
    }
    
    /*
     * Method: setActive
     * Purpose: This method sets the block active/inactive
     */
    public void setActive(boolean act) {
        active = act;
    }
    
    /*
     * Method: getID
     * Purpose: This method returns the BlockType ID
     */
    public int getID() {
        return type.getID();
    }
    
    public BlockType getBlockType() {
        return type;
    }
}
