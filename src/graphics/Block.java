
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
    
    public void setPosition(Vector3 pos) {
        position = new Vector3(pos);
    }
    
    public void setPosition(float x, float y, float z) {
        position = new Vector3(x, y, z);
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean act) {
        active = act;
    }
    
    public int getID() {
        return type.getID();
    }
}
