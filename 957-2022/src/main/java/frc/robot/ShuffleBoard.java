package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleBoard {
  boolean autoModeSet = false;
  SendableChooser<String> m_chooser = new SendableChooser<>();

  // Run when shuffleboard is first initialized
  public ShuffleBoard(){

    // Add options
    m_chooser.setDefaultOption("Nothing", "No Auto");
    m_chooser.addOption("Set Auto 1", "Auto 1");
    m_chooser.addOption("Set Auto 2", "Auto 2");
    m_chooser.addOption("Set Auto 3", "Auto 3");

    // Put chooser on dashboard
    SmartDashboard.putData(m_chooser);

    SmartDashboard.putString("Ally 1", "0");
    SmartDashboard.putString("Ally 2", "0");
  }

  public void updateSmartboard(int cargo, DriveTrain d) 
    {
      SmartDashboard.putNumber("Cargo", cargo);
      SmartDashboard.putString("Auto", m_chooser.getSelected());
      SmartDashboard.putNumber("Bot Angle", d.m_navx.getAngle());
      SmartDashboard.putData("Gyro Angle", d.m_navx);
    }

  public String updateAuto(){
    System.out.println(m_chooser.getSelected()); // Testing
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
}
