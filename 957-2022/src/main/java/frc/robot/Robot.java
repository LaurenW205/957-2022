// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
   
   DriveTrain m_drivetrain = DriveTrain.getInstance();
   //Turret m_turret = new Turret();

   int m_autoStep = 0;
   int m_autoMode = 0;

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {

    System.out.println(m_autoStep);

    switch(m_autoMode){

      case 0:

      switch(m_autoMode){

        case 0:

        m_drivetrain.driveStraight(125, 0, 0.2);

      }

      break;
      case 2:

      switch(m_autoStep){
       
       
        case 0:
         m_drivetrain.driveStraight(46, 0, 0.5);
         m_autoStep ++;
        break;

        case 1:
         //intake cargo
         m_autoStep ++;
        break;

        case 2:
         m_drivetrain.driveStraight(24, 0, 0.5);
         m_autoStep ++;
        break;

        case 3:
         //m_turret.run(true);
         m_autoStep ++;
        break;

        case 4:
        //shoot
         m_autoStep ++;
        break;

        case 5:
         m_drivetrain.turnTo(45);
         m_autoStep ++;
        break;




      }
    }
    
  }
    
  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
