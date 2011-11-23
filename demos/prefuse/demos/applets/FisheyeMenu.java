package prefuse.demos.applets;

import prefuse.util.ui.JPrefuseApplet;


public class FisheyeMenu extends JPrefuseApplet {
    
    public void init() {
        prefuse.demos.FisheyeMenu fm = prefuse.demos.FisheyeMenu.demo();
        this.getContentPane().add(fm);
        fm.getVisualization().run("init");
    }
    
} // end of class FisheyeMenu
