package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.MouseHandler;
import main.UtilityTool;

public class Entity {

    public Random rand = new Random();

    GamePanel gp;

    // Object Attributes
    public int coinValue;
    public boolean deadObj = false;
    public int objIndex;
    public int enchantLevel = 0;
    public ArrayList<Integer> enchantIndex = new ArrayList<Integer>();
    

    // States
    public int worldX, worldY, screenX, screenY, speed, defaultSpeed;
    public boolean collision = false;
    public boolean collisionOn = false;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY, solidAreaDefaultWidth, solidAreaDefaultHeight;
    public String direction = "down";
    public boolean standing = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean inFight = false;
    public boolean showNames = false;
    public boolean reachedGoal = false;
    public boolean knockBack = false;


    // Character Attributes
    public int type;
    public int playerType = 1, enemyType = 2, npcType = 3, objectType = 4;
    public String name;
    public int maxLife;
    public int life;
    public double maxSp;
    public double sp;
    public int actionLockCounter = 0;
    public int level = 1;
    public static int taskLevel;
    public int subType;
    
    // Item
    public int objDetailedType;
    public int weaponAttackSize;
    
    public static ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 45;
    public int price =0;
    public String description = "" ;

    // Images
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, left5, right1, right2, right3, right4, downCracked1, downCracked2, downCracked3;
    public BufferedImage[] dyingUp = new BufferedImage[5], dyingDown = new BufferedImage[5], dyingLeft = new BufferedImage[5], dyingRight = new BufferedImage[5];
    public BufferedImage[] up = new BufferedImage[4], down = new BufferedImage[4], left = new BufferedImage[4], right = new BufferedImage[4];
    public BufferedImage[] attackUp = new BufferedImage[16], attackDown = new BufferedImage[16], attackLeft = new BufferedImage[16], attackRight = new BufferedImage[16];
    public BufferedImage[] auraUp = new BufferedImage[4], auraDown = new BufferedImage[4], auraLeft = new BufferedImage[4], auraRight = new BufferedImage[4];
    public BufferedImage[] auraSwordUp = new BufferedImage[16], auraSwordDown = new BufferedImage[16], auraSwordLeft = new BufferedImage[16], auraSwordRight = new BufferedImage[16];
    public BufferedImage image, deadImage;
    public BufferedImage hpBarImage, emptyBarImage;

    // Counter
    public int stepCounter = 0;
    public int stepType = 0;
    public int enemySoundCounter = 0;
    public int invincibleCounter = 0;
    public boolean invincible = false;
    public int spriteNum = 1, spriteCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    public int damageCounter = 0;
    public int damageTimeOut = 20;
    public boolean newBorn = false;
    public int bornCounter = 0;
    public int objectCounter = 0;
    public int knockBackCounter = 0;

    // NPC
    public int npcActionCounter = 0;
    public static String dialogues[] = new String[20];

    // Enemy
    public int wolfID;
    
    public int drawX, drawY;

    public Entity(GamePanel gp) {
        this.gp = gp;

        getImages();
    }

    public void getImages() {
        hpBarImage = setup("/UI/HpBarEnemy", gp.tileSize, gp.tileSize / 8);
        emptyBarImage = setup("/UI/emptyBar2", gp.tileSize, gp.tileSize / 8);
    }

    public void setAction() {
    }

    public void speak() {
    }

    public void damageReaction() {
    }
    
    public void changeDirection() {
    }
    
    public boolean increaseWeapon(Entity entity) { 
        return false;
    }

    public void checkCollision() {
        // CHECK TILE COLLISION
        collisionOn = false;
        gp.collisionChecker.checkTile(this);

        // CHECK ENEMY COLLISION
        gp.collisionChecker.checkEntity(this, gp.enemy);

        // CHECK NPC COLLISION
        gp.collisionChecker.checkEntity(this, gp.npc);

        // CHECK PLAYER COLLISION
        if (type != playerType) {
            gp.collisionChecker.checkPlayer(this);
        }
    }

    public void update() {
        
        if(dying && name == "Satellite") {
            solidArea.x = 50;
            solidArea.y = 60;
            solidArea.width = 100;
            solidArea.height = 205;

            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
        }
        
        if(knockBack == true) {
            
            checkCollision();
            
            if(!collisionOn) {
                int newWorldX = worldX;
                int newWorldY = worldY;

                switch (gp.player.direction) {
                    case "upleft":
                        newWorldY -= speed;
                        newWorldX -= speed;
                        break;
                    case "upright":
                        newWorldY -= speed;
                        newWorldX += speed;
                        break;
                    case "downleft":
                        newWorldY += speed;
                        newWorldX -= speed;
                        break;
                    case "downright":
                        newWorldY += speed;
                        newWorldX += speed;
                        break;
                    case "up":
                        newWorldY -= speed;
                        break;
                    case "down":
                        newWorldY += speed;
                        break;
                    case "left":
                        newWorldX -= speed;
                        break;
                    case "right":
                        newWorldX += speed;
                        break;
                }

                if (newWorldX > 0 && newWorldX < (gp.maxWorldCol - 1) * gp.tileSize &&
                        newWorldY > 0 && newWorldY < (gp.maxWorldRow - 2) * gp.tileSize) {
                    worldX = newWorldX;
                    worldY = newWorldY;
                    if(type == enemyType && name == "Wolf") {
                        System.out.println("girdi ");
                        if (newWorldX > gp.aSetter.wolfBoundary.x && newWorldX < gp.aSetter.wolfBoundary.x + gp.aSetter.wolfBoundary.width &&
                                newWorldY > gp.aSetter.wolfBoundary.y && newWorldY < gp.aSetter.wolfBoundary.y + gp.aSetter.wolfBoundary.height) {
                            worldX = newWorldX;
                            worldY = newWorldY;
                        }
                    }else {
                        /* 
                        worldX = newWorldX;
                        worldY = newWorldY;
                        */
                    }
                }
            }
            
            knockBackCounter++;
            if(knockBackCounter == 8) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            
        }else {
            setAction();
            checkCollision();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn && !standing) {
                int newWorldX = worldX;
                int newWorldY = worldY;

                switch (direction) {
                    case "upleft":
                        newWorldY -= speed;
                        newWorldX -= speed;
                        break;
                    case "upright":
                        newWorldY -= speed;
                        newWorldX += speed;
                        break;
                    case "downleft":
                        newWorldY += speed;
                        newWorldX -= speed;
                        break;
                    case "downright":
                        newWorldY += speed;
                        newWorldX += speed;
                        break;
                    case "up":
                        newWorldY -= speed;
                        break;
                    case "down":
                        newWorldY += speed;
                        break;
                    case "left":
                        newWorldX -= speed;
                        break;
                    case "right":
                        newWorldX += speed;
                        break;
                }

                if(type == enemyType && name == "Wolf") {
                    if (newWorldX > gp.aSetter.wolfBoundary.x && newWorldX < gp.aSetter.wolfBoundary.x + gp.aSetter.wolfBoundary.width &&
                            newWorldY > gp.aSetter.wolfBoundary.y && newWorldY < gp.aSetter.wolfBoundary.y + gp.aSetter.wolfBoundary.height) {
                        worldX = newWorldX;
                        worldY = newWorldY;
                    }else {
                        changeDirection();
                    }
                }else {
                    /* 
                    worldX = newWorldX;
                    worldY = newWorldY;
                    */
                }
            }
        }
        
        // Active showNames when mouse over entity or entity is near player
        int mouseOverX = gp.player.worldX - gp.player.screenX + gp.mouseH.mouseOverX;
        int mouseOverY = gp.player.worldY - gp.player.screenY + gp.mouseH.mouseOverY;
        
        int enemyLeft = worldX + solidArea.x;
        int enemyRight = worldX + solidArea.x + solidArea.width;
        int enemyTop = worldY + solidArea.y;
        int enemyBottom = worldY + solidArea.y + solidArea.height;
        
        int xDiff = Math.abs(gp.player.worldX - worldX) / gp.tileSize;
        int yDiff = Math.abs(gp.player.worldY - worldY) / gp.tileSize;
        
        if(mouseOverX >= enemyLeft && mouseOverY >= enemyTop &&
           mouseOverX <= enemyRight && mouseOverY <= enemyBottom) {
            showNames = true;
        }else if(xDiff + yDiff <= 5) {
            showNames = true;
        }else {
            showNames = false;
        }
        
        if(type == npcType) {
            if(name == "Abulbul") {
                animationCharacter(5);
            }else {
                animationCharacter(10);
            }
        }

        if (!standing) {
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 3) {
                    spriteNum = 1;
                } else {
                    spriteNum++;
                }
                spriteCounter = 0;
            }
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter == damageTimeOut) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (type == enemyType) {
            if (inFight && gp.collisionChecker.checkFightAreaForEnemy(gp.player, this)) {
                standing = true;
            } else {
                standing = false;
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        drawX = worldX - gp.player.worldX + gp.player.screenX;
        drawY = worldY - gp.player.worldY + gp.player.screenY;

        /*
         * when player moves to the edges of the map,
         * in the if below was thinking screen moves but at the edges screen doesn't
         * move
         * so we added the missing parts here
         */

        int missingLeft;
        int missingRight;
        int missingTop;
        int missingBottom;

        if (gp.isPlayerAtLeftEdge)
            missingLeft = Math.abs(gp.player.defaultScreenX - gp.player.worldX);
        else
            missingLeft = 0;

        if (gp.isPlayerAtTopEdge)
            missingTop = Math.abs(gp.player.defaultScreenY - gp.player.worldY);
        else
            missingTop = 0;

        if (gp.isPlayerAtRightEdge)
            missingRight = Math.abs(gp.player.defaultScreenX - gp.player.worldX);
        else
            missingRight = 0;

        if (gp.isPlayerAtBottomEdge)
            missingBottom = Math.abs(gp.player.defaultScreenY - gp.player.worldY);
        else
            missingBottom = 0;

        if (worldX > gp.player.worldX - gp.player.defaultScreenX - missingRight - 96 && // is entity's location
                                                                                                 // more than screenX
                worldX < gp.player.worldX + gp.player.defaultScreenX + missingLeft + 96 && // is entity's
                                                                                                    // location less
                                                                                                    // than screenX
                worldY > gp.player.worldY - gp.player.defaultScreenY - missingBottom - 96 && // is entity's
                                                                                                      // location more
                                                                                                      // than screenY
                worldY < gp.player.worldY + gp.player.defaultScreenY + missingTop + 96) { // is entity's
                                                                                                   // location less than
                                                                                                   // screenY

            switch (direction) {
                case "up":
                case "upleft":
                case "upright":
                    if (spriteNum == 1)
                        image = up1;
                    if (spriteNum == 2)
                        image = up2;
                    if (spriteNum == 3)
                        image = up3;
                    break;
                case "down":
                case "downleft":
                case "downright":
                    if (spriteNum == 1)
                        image = down1;
                    if (spriteNum == 2)
                        image = down2;
                    if (spriteNum == 3)
                        image = down3;
                    if (spriteNum == 4)
                        image = down4;
                    break;
                case "left":
                    if (spriteNum == 1)
                        image = left1;
                    if (spriteNum == 2)
                        image = left2;
                    if (spriteNum == 3)
                        image = left3;
                    break;
                case "right":
                    if (spriteNum == 1)
                        image = right1;
                    if (spriteNum == 2)
                        image = right2;
                    if (spriteNum == 3)
                        image = right3;
                    break;
            }

            // if enemy is new born animate
            if (newBorn) {
                bornAnimation(g2);
            }

            // if object not collected remove it
            /*
             * if (type == objectType) {
             * objectCounter++;
             * if (objectCounter == 300) {
             * deadObj = true;
             * }
             * }
             */

            // Enemy fill Hp
            damageCounter++;
             
            if (type == enemyType && damageCounter == 360 && !dying) {
                if (life != maxLife) {
                    life++;
                    damageCounter = 0;
                }
            }

            // Enemy Label
            if (type == enemyType && showNames && name != "Satellite") {
                
                g2.setFont(new Font("Courier New", Font.BOLD, 12));
                g2.setColor(Color.green);
                g2.drawString("Lv " + level, screenX - 14, screenY - 20);
                g2.setColor(Color.red);
                g2.drawString(name, screenX + 25, screenY - 20);
            }

            // Enemy Hp bar
            if (type == enemyType && hpBarOn == true && !dying) {

                double oneScale;
               
                if(name == "Satellite") oneScale = (double) gp.tileSize * 4 / maxLife;
                else oneScale = (double) gp.tileSize / maxLife;
                
                
                double hpBarValue = oneScale * life;
                double maxBar = oneScale * maxLife;
                
   

                g2.drawImage(emptyBarImage, screenX, screenY - 10, (int) maxBar, gp.tileSize / 8, null);
                
                if(name == "Satellite") g2.drawImage(hpBarImage, screenX + 10, screenY - 10, Math.abs((int) hpBarValue - 20), gp.tileSize / 8,null);
                else g2.drawImage(hpBarImage, screenX + 3, screenY - 10, Math.abs((int) hpBarValue - 3), gp.tileSize / 8,null);
                

                hpBarCounter++;
                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            // Set entity transparent after damage
            if (invincible && !dying) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4F);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            }

            if (deadObj && name != "Satellite") {
                dyingAnimation(g2);
            }

            // NPC LABEL
            if (type == npcType) {
                g2.setFont(new Font("Courier New", Font.BOLD, 13));
                
                switch(name) {
                    case "Abulbul":
                        g2.setColor(new Color(0, 0, 189)); 
                        g2.drawString(name, screenX + 25, screenY);
                        break;
                    case "Blacksmith":
                        g2.setColor(new Color(0, 0, 189)); 
                        g2.drawString(name, screenX + 10, screenY - 10);
                        break;
                    case "Merchant":
                        g2.setColor(new Color(169, 103, 245)); 
                        g2.drawString(name, screenX + 15, screenY + 10);
                        break;
                }
            }

            // Object Label
            if (type == objectType) {
                g2.setFont(new Font("Courier New", Font.BOLD, 12));

                switch (name) {
                    case "Dolunay":
                        g2.setFont(new Font("Gill Sans Extrabold", Font.BOLD, 12));
                        g2.setColor(Color.cyan);
                        g2.drawString("Dolunay Sword", screenX - 8, screenY);
                        break;
                        
                    case "Su Perisi":
                        g2.setColor(Color.YELLOW);
                        g2.drawString("Su Perisi", screenX - 10, screenY - 10);
                        break;
                        
                    case "Kırmızı Demir Pala":
                        g2.setColor(Color.cyan);
                        g2.drawString("Kırmızı Demir Pala", screenX - 25, screenY - 10);
                        break;
                        
                    case "Geniş Kılıç":
                        g2.setColor(Color.cyan);
                        g2.drawString("Geniş Kılıç", screenX - 25, screenY - 10);
                        break;
                        
                    default:
                        break;
                }
            }
            
            if(name == "Satellite" && life <= 0) {
                System.out.println(life);
                dying = true;
                image = downCracked1;
            }


            g2.drawImage(image, null, screenX, screenY);

            changeAlpha(g2, 1F);

            // Reset transparency
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int increaseAmount = 5;
        if (dyingCounter < increaseAmount) {
            changeAlpha(g2, 1f);
        } else if (dyingCounter < increaseAmount * 2) {
            changeAlpha(g2, 0.9f);
        } else if (dyingCounter < increaseAmount * 3) {
            changeAlpha(g2, 0.8f);
        } else if (dyingCounter < increaseAmount * 4) {
            changeAlpha(g2, 0.7f);
        } else if (dyingCounter < increaseAmount * 5) {
            changeAlpha(g2, 0.6f);
        } else if (dyingCounter < increaseAmount * 6) {
            changeAlpha(g2, 0.5f);
        } else if (dyingCounter < increaseAmount * 7) {
            changeAlpha(g2, 0.4f);
        } else if (dyingCounter < increaseAmount * 8) {
            changeAlpha(g2, 0.3f);
        } else if (dyingCounter < increaseAmount * 9) {
            changeAlpha(g2, 0.2f);
        } else if (dyingCounter < increaseAmount * 10) {
            changeAlpha(g2, 0.1f);
        } else {
            changeAlpha(g2, 0f);
            if (type == objectType) {
                gp.obj[objIndex] = null;
            }
        }
    }

    public void bornAnimation(Graphics2D g2) {
        bornCounter++;
        int increaseAmount = 3;
        if (bornCounter < increaseAmount) {
            changeAlpha(g2, 0f);
        } else if (bornCounter < increaseAmount * 2) {
            changeAlpha(g2, 0.1f);
        } else if (bornCounter < increaseAmount * 3) {
            changeAlpha(g2, 0.2f);
        } else if (bornCounter < increaseAmount * 4) {
            changeAlpha(g2, 0.3f);
        } else if (bornCounter < increaseAmount * 5) {
            changeAlpha(g2, 0.4f);
        } else if (bornCounter < increaseAmount * 6) {
            changeAlpha(g2, 0.5f);
        } else if (bornCounter < increaseAmount * 7) {
            changeAlpha(g2, 0.6f);
        } else if (bornCounter < increaseAmount * 8) {
            changeAlpha(g2, 0.7f);
        } else if (bornCounter < increaseAmount * 9) {
            changeAlpha(g2, 0.8f);
        } else if (bornCounter < increaseAmount * 10) {
            changeAlpha(g2, 0.9f);
        } else {
            changeAlpha(g2, 1f);
            newBorn = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath, int width, int height) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }
    
    public void animationCharacter(int spriteTime) {
        spriteCounter++;
        if (spriteCounter > spriteTime) {
            if (spriteNum == 4) {
                spriteNum = 1;
            } else {
                spriteNum++;
            }
            spriteCounter = 0;
        }
    }

    public void searchPath(int goalCol, int goalRow) {
        
        if(type == playerType) {
            solidArea.x = 9;
            solidArea.y = 20;
            solidArea.width = 30;
            solidArea.height = 28;
        }

        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pathFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

        if (gp.pathFinder.search(goalCol, goalRow, this) && !standing) { // it returns true when found a way to go
            if(reachedGoal) {
                onPath = false;
                gp.player.mouseH.pressed = false;
                reachedGoal = false;
            }else {
                // Next worldX & worldY
                int nextX = gp.pathFinder.pathList.get(0).col * gp.tileSize;
                int nextY = gp.pathFinder.pathList.get(0).row * gp.tileSize;
    
                // Entity's solidArea Position
                /*
                 * int entityLeftX = (int)(worldX / gp.tileSize) * gp.tileSize;
                 * int entityRightX = entityLeftX;
                 * int entityTopY = (int)(worldY / gp.tileSize) * gp.tileSize;
                 * int entityBottomY = entityTopY;
                 */
    
                int entityLeftX = worldX + solidArea.x;
                int entityRightX = worldX + solidArea.x + solidArea.width;
                int entityTopY = worldY + solidArea.y;
                int entityBottomY = worldY + solidArea.y + solidArea.height;
    
                // System.out.println("nextX:" + nextX + " nextY:" + nextY + " entityLeftX: " +
                // entityLeftX + " entityRightX: " + entityRightX+ " entityTopY:" + entityTopY +
                // " entityBottomY:" + entityBottomY);
    
                if (entityTopY > nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize) {
                    direction = "up";
                } else if (entityTopY < nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize) {
                    direction = "down";
                } else if (entityTopY >= nextY && entityBottomY < nextY + gp.tileSize) {
                    // Left or Right
                    if (entityLeftX > nextX) {
                        direction = "left";
                    }
                    if (entityLeftX < nextX) {
                        direction = "right";
                    }
                } else if (entityTopY > nextY && entityLeftX > nextX) {
                    // up or left
                    direction = "up";
                    checkCollision();
                    if (collisionOn) {
    
                        direction = "left";
                    }
                } else if (entityTopY > nextY && entityLeftX < nextX) {
                    // up or right
                    direction = "up";
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                } else if (entityTopY < nextY && entityLeftX > nextX) {
                    // down or left
                    direction = "down";
    
                    checkCollision();
                    if (collisionOn) {
                        direction = "left";
                    }
                } else if (entityTopY < nextY && entityLeftX < nextX) {
                    // down or right
                    direction = "down";
    
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                }
    
                /*
                 * // If reaches the goal, stop searching
                 * int nextCol = gp.pathFinder.pathList.get(0).col;
                 * int nextRow = gp.pathFinder.pathList.get(0).row;
                 * 
                 * if(nextCol == goalCol && nextRow == goalRow) {
                 * onPath = false;
                 * if(gp.player.mouseH.pressed && type == playerType) {
                 * gp.player.mouseH.pressed = false;
                 * }
                 * if(type == npcType) {
                 * //standing = true; // stop NPC when it reaches to goal
                 * }
                 * }
                 */
            }
        }
        
        if(type == playerType) {
            solidArea.x = solidAreaDefaultX;
            solidArea.y = solidAreaDefaultY;
            solidArea.width = solidAreaDefaultWidth;
            solidArea.height = solidAreaDefaultHeight;
        }
    }
}
