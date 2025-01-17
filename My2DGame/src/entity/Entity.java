/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import my2dgame.GamePanel;
import my2dgame.UtilityTool;

/**
 *
 * @author manhc
 */
public class Entity {

    GamePanel gp;
    public int worldX, worldY;
    public int speed;
    //Mô tả dữ liệu hình ảnh có thể truy cập được, sử dụng để lưu trữ tập tin hình ảnh
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public String direction = "down";

    //Để thay đổi hình ảnh liên tục với FPS 60
    public int spireCounter = 0;
    public int spireNum = 1;

    //Vùng va chạm nhân vật với map
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    //Attack Area
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    //Va chạm với đồ vật
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    public boolean invincible = false;
    boolean attacking = false;
    public int invincibleCounter = 0;
    String dialogue[] = new String[20];
    int dialogueIndex = 0;
    
    public boolean alive = true;
    public boolean dying = false;
    int dyingCounter = 0;
    boolean hpBarOn = false;
    int hpBarCounter = 0;
    public int shotAvaiableCounter = 0;
    
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    
    //Character state
    public int maxLife;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public int maxMana;
    public int mana;
    public int ammo;
    public Projectile projectile;
    
    //Item attributes
    public int value;
    public int attackValue;
    public int defensevalue;
    public String description = "";
    public int useCost;
    
    //Type
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {

    }
    
    public void damageReaction(){
    
    }
    
    public void speak(){
        if (dialogue[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogue[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
        
    }
    
    public void use(Entity entity){
    
    }
    
    public void checkDrop(){
    
    }
    public void dropItem(Entity droppedItem){
        for (int i = 0; i < gp.obj.length; i++) {
            if(gp.obj[i] == null){
                gp.obj[i] = droppedItem;
                gp.obj[i].worldX = worldX; //The dead monster's worldX
                gp.obj[i].worldY = worldY; //The dead monster's worldY
                break;
            }
        }
    }
    
    public Color getParticleColor(){
        Color color = null;
        return color;
    }
    
    public int getParticleSize(){
        int size = 0; //0 pixels
        return size;
    }
    
    public int getParticleSpeed(){
        int speed = 0;
        return speed;
    }
    
    public int getParticleMaxlife(){
        int maxLife = 0;
        return maxLife;
    }
    
    public void generateParticle(Entity generator, Entity target){
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxlife();
        
        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }

    public void update() {
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEtity(this, gp.npc);
        gp.cChecker.checkEtity(this, gp.monster);
        gp.cChecker.checkEtity(this, gp.iTile);
        boolean contactPlayer= gp.cChecker.checkPlayer(this);
        
        if(this.type == type_monster && contactPlayer == true){
            damagePlayer(attack);
        }

        //If collision is false, player can move
        if (collisionOn == false) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        //Với FPS 60 thì sẽ có 6 lần thay đổi hình ảnh trong một giây với điều kiện ở dưới
        //Cứ mỗi 0,1 giây sẽ thay đổi ảnh liên tục khiến chuyển động mượt hơn
        spireCounter++;
        if (spireCounter > 10) {
            if (spireNum == 1) {
                spireNum = 2;
            } else if (spireNum == 2) {
                spireNum = 1;
            }
            spireCounter = 0;
        }
        
        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 40){
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if(shotAvaiableCounter < 30){
            shotAvaiableCounter++;
        }

    }
    
    public void damagePlayer(int attack){
        if(gp.player.invincible == false){
                //we can give damage
                gp.playSE(6);
                
                int damage = attack - gp.player.defense;
                if(damage < 0){ damage = 0;}
                
                gp.player.life -= damage;
                gp.player.invincible = true;
            }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //kiểm tra ranh giới vẽ, chỉ vẽ trong phạm vi trên màn hình thấy được
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    if (spireNum == 1) {
                        image = up1;
                    }
                    if (spireNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spireNum == 1) {
                        image = down1;
                    }
                    if (spireNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spireNum == 1) {
                        image = left1;
                    }
                    if (spireNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spireNum == 1) {
                        image = right1;
                    }
                    if (spireNum == 2) {
                        image = right2;
                    }
                    break;
            }
            
            //Monster HP bar
            if(type == 2 && hpBarOn == true){
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;
                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1, screenY - 16, gp.tileSize+2, 12);
                
                g2.setColor(new Color(255,0,30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
                
                hpBarCounter++;
                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }
            
            if(invincible == true){
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if(dying == true){
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);
            //Reset alpha
            changeAlpha(g2, 1f);
        }
    }
    
    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;
        int i = 5;
        if(dyingCounter <= i){ changeAlpha(g2, 0f);}
        if(dyingCounter > i && dyingCounter <= i*2){ changeAlpha(g2, 1f);}
        if(dyingCounter > i*2 && dyingCounter <= i*3){ changeAlpha(g2, 0f);}
        if(dyingCounter > i*3 && dyingCounter <= i*4){ changeAlpha(g2, 1f);}
        if(dyingCounter > i*4 && dyingCounter <= i*5){ changeAlpha(g2, 0f);}
        if(dyingCounter > i*5 && dyingCounter <= i*6){ changeAlpha(g2, 1f);}
        if(dyingCounter > i*6 && dyingCounter <= i*7){ changeAlpha(g2, 0f);}
        if(dyingCounter > i*7 && dyingCounter <= i*8){ changeAlpha(g2, 1f);}
        if(dyingCounter > i*8){
            
            alive = false;
        }
        
    }
    
    public void changeAlpha(Graphics2D g2, float alphaValue){
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

}
