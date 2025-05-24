package entity;

import main.GamePanel;


public class NPC_mayortadi extends NPC {

    public NPC_mayortadi(GamePanel gp) {
        super(gp);
        name = "Mayor Tadi";
        getImage();
    }

    public void getImage() {
        u1 = setup("/player/police_diem");
        u2 = setup("/player/police_diem");
        r1 = setup("/player/police_diem");
        r2 = setup("/player/police_diem");
        l1 = setup("/player/police_diem");
        l2 = setup("/player/police_diem");
        d1 = setup("/player/police_diem");
        d2 = setup("/player/police_diem");
    }
    

    @Override
    public void setDialogue() {
        dialogues[0] = "Hello there! I'm Mayor Tadi. Welcome to our town!";
        dialogues[1] = "I hope you're enjoying your time on the farm.";
        dialogues[2] = "If you need any help, just ask around town.";
        dialogues[3] = "We have a shipping bin near the center of town where you can sell your produce.";
        dialogues[4] = "Good luck with your farming!";
    }
}