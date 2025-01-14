/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my2dgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author manhc
 */
public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    //Debug
    public boolean showDebugText = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); //Trả về số nguyên keyCode được liên kết với key trong sự kiện này

        //Title state
        if (gp.gameState == gp.titleState) {
            titltState(code);
        }

        //Play state
        else if (gp.gameState == gp.playState) {
            playState(code);
            
        } //Pause state
        else if (gp.gameState == gp.pauseState) {
            pauseState(code);
            
        } //Dialogue state
        else if (gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }
        //Character State
        else if (gp.gameState == gp.characterState) {
            characterState(code);  
        }
        //Options State
        else if (gp.gameState == gp.optionsState) {
            optionsState(code);  
        }
    }
    
    public void titltState(int code){
        if (gp.ui.titleScreenState == 0) {
                if (code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 2) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) {
                        gp.ui.titleScreenState = 1;
                    }
                    if (gp.ui.commandNum == 1) {
                        //add later
                    }
                    if (gp.ui.commandNum == 2) {
                        System.exit(0);
                    }
                }
            }
            else if (gp.ui.titleScreenState == 1) {
                if (code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 3;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 3) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) {
                        System.out.println("Do some fighter specific stuff!");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                    if (gp.ui.commandNum == 1) {
                        System.out.println("Do some thier specific stuff!");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                    if (gp.ui.commandNum == 2) {
                        System.out.println("Do some sorcerer specific stuff!");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                    if (gp.ui.commandNum == 3) {
                        gp.ui.titleScreenState = 0;
                    }
                }
            }
    }
    public void playState(int code){
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }
            if(code == KeyEvent.VK_C){
                gp.gameState = gp.characterState;
            }
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if (code == KeyEvent.VK_F) {
                shotKeyPressed = true;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.optionsState;
            }

            //Debug
            if (code == KeyEvent.VK_T) {
                if (showDebugText == false) {
                    showDebugText = true;
                } else if (showDebugText == true) {
                    showDebugText = false;
                }
            }
            if (code == KeyEvent.VK_R){
                
                gp.tileM.loadMap("/maps/worldV2.txt");
            }
    }
    public void pauseState(int code){
        if (code == KeyEvent.VK_P) {
                gp.gameState = gp.playState;

            }
    }
    public void dialogueState(int code){
        if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;

            }
    }
    public void characterState(int code){
        if (code == KeyEvent.VK_C) {
                gp.gameState = gp.playState;
                
            }
        if (code == KeyEvent.VK_W) {
                if(gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                gp.playSE(9);
                }
            }
        if (code == KeyEvent.VK_A) {
                if(gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                gp.playSE(9);
                }
            }
        if (code == KeyEvent.VK_S) {
                if(gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(9);
                }
            }
        if (code == KeyEvent.VK_D) {
                if(gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(9);
                }
            }
        if (code == KeyEvent.VK_ENTER) {
                gp.player.selectItem();
            }
    }
    
    public void optionsState(int code){
        
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        
        int maxComandNum = 0;
        switch(gp.ui.subState){
            case 0: maxComandNum = 5; break;
            case 3: maxComandNum = 1; break;
        }
        
        if (code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    gp.playSE(9);
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = maxComandNum;
                    }
                }
        if (code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    gp.playSE(9);
                    if (gp.ui.commandNum > maxComandNum) {
                        gp.ui.commandNum = 0;
                    }
                }
        if (code == KeyEvent.VK_A) {
                    if(gp.ui.subState == 0){
                        if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0){
                            gp.music.volumeScale--;
                            gp.music.checkVolume();
                            gp.playSE(9);
                        }
                        if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0){
                            gp.se.volumeScale--;
                            gp.playSE(9);
                        }
                    }
                }
        if (code == KeyEvent.VK_D) {
                    if(gp.ui.subState == 0){
                        if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5){
                            gp.music.volumeScale++;
                            gp.music.checkVolume();
                            gp.playSE(9);
                        }
                        if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5){
                            gp.se.volumeScale++;
                            gp.playSE(9);
                        }
                    }
                }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
                shotKeyPressed = false;
            }
    }

}
