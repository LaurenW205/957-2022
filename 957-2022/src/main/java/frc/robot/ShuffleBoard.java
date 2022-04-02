package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.automodes.buddyleft;
import frc.robot.automodes.buddyright;

public class ShuffleBoard {
  boolean autoModeSet = false;
  SendableChooser<String> m_chooser = new SendableChooser<>();
  buddyleft bl = new buddyleft();
  
  public double sbLeftAngle = 0;
  
  // Run when shuffleboard is first initialized
  public ShuffleBoard(){

    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(1);//off

    // Add options
    m_chooser.setDefaultOption("Nothing", "No Auto");
    //m_chooser.addOption("Left 2 Cargo Near", "Auto 1");
    //m_chooser.addOption("Mid 2 Cargo Far", "Auto 2");
    //m_chooser.addOption("Right 2 Cargo Near", "Auto 3");
    //m_chooser.addOption("Mid 3 Cargo", "Auto 4");
    //m_chooser.addOption("Left 2 Cargo Super Near", "Auto 5");
    //m_chooser.addOption("1 Cargo Auto", "Auto 6");
    m_chooser.addOption("Buddy Right", "Auto 7");
    m_chooser.addOption("Buddy Left", "Auto 8");
    m_chooser.addOption("Single Cargo", "Auto 9");
    m_chooser.addOption("Test Auto", "Auto 10");
    m_chooser.addOption("Low Buddy Right", "Auto 11");
    m_chooser.addOption("Low Buddy left", "Auto 12");

    // Put chooser on dashboard
    SmartDashboard.putData(m_chooser);

    SmartDashboard.putString("Ally 1", "0");
    SmartDashboard.putString("Ally 2", "0");

    SmartDashboard.putBoolean("Slow?", true);

    


  }

  public void updateSmartboard(int cargo, DriveTrain d) 
    {
      SmartDashboard.putNumber("Cargo", cargo);
     // SmartDashboard.putString("Auto", m_chooser.getSelected());
      SmartDashboard.putNumber("Bot Angle", d.m_navx.getAngle());
      SmartDashboard.putData("Gyro Angle", d.m_navx);
    }

  public String updateAuto(){
    return m_chooser.getSelected();
  }
    
  public String getAlly1()
    {
      return SmartDashboard.getString("Ally 1", "???");
    }
  
  public String getAlly2()
    {
      return SmartDashboard.getString("Ally 2", "???");
    }
  
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("datatable");
    NetworkTableEntry txEntry = table.getEntry("tx");

  public double getTargetX(){
    double TargetX = txEntry.getDouble(160);
    if (TargetX == -9000)
      TargetX = 160;
    return TargetX;
  }

  public Boolean fastOrSlow(){
      if (DriveTrain.getInstance().coefficent == 1){
        return SmartDashboard.putBoolean("Slow?", false);
      }else{
        return SmartDashboard.putBoolean("Slow?", true);
    }
  }
}
