package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ShuffleBoard {
  boolean autoModeSet = false;

    public void updateSmartboard(int cargo, int auto, DriveTrain d, Turret2 t) 
      {
        SmartDashboard.putNumber("Cargo", cargo);
        SmartDashboard.putNumber("Auto", auto);
        Shuffleboard.getTab("Smart Dashboard").add(d.m_navx);
      }

    public String getAlly1()
      {
        return SmartDashboard.getString("Ally 1", "???");
      }
    
    public String getAlly2()
      {
        return SmartDashboard.getString("Ally 2", "???");
      }

    public int setAuto()
    {
      if(SmartDashboard.getString("Auto", "no") != "no" && !autoModeSet) // If there is value to use and autoMode has not already been set,
      {  
        autoModeSet = true;                                               // Then prevent auto from being set again
        return Integer.parseInt(SmartDashboard.getString("Auto", "no"));  // And return the value entered
      }
      else
        return 0;                                                         // Otherwise, return 0 (0 should not be assigned an auto)
    }

    
}
