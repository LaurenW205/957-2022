package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ShuffleBoard {

    public void updateSmartboard(int cargo, int auto) 
      {
        SmartDashboard.putNumber("Cargo", cargo);
        SmartDashboard.putNumber("Auto", auto);
        
      }

    public String getAlly1()
      {
        return SmartDashboard.getString("Ally 1", "???");
      }
    
    public String getAlly2()
      {
        return SmartDashboard.getString("Ally 2", "???");
      }

    
}
