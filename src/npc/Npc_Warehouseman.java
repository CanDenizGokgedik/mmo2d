package npc;

import main.GamePanel;
import entity.Entity;

public class Npc_Warehouseman  extends Entity {
    
    GamePanel gp;

    public Npc_Warehouseman(GamePanel gp) {
        super(gp);
        this.gp = gp;

        direction = "down";
        speed = 0;
        defaultSpeed = speed;
        type = npcType;
        level = 100;
        name = "Depocu";
        
        solidArea.x = 10;
        solidArea.y = 2;
        solidArea.width = 60;
        solidArea.height = 90;
        
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        getNpcImage();
    }


    public void getNpcImage() {
        down1 = setup("/npc/blacksmith1", 96, 96);
        down2 = setup("/npc/blacksmith2", 96, 96);
        down3 = setup("/npc/blacksmith3", 96, 96);
        down4 = setup("/npc/blacksmith4", 96, 96);
    }

}
