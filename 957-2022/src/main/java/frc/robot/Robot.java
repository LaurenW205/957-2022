// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.RobotState.State;

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

   int m_autoStep = 0;
   int m_autoMode = 0;
   RobotState m_state = RobotState.getInstance();

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

      case 3:

      switch(m_autoMode){

        case 0:
        m_drivetrain.driveStraight(24, 0, 0.2);//drive foward to avoid cargo
        m_drivetrain.turnTo(45, 0, 0.2); //turn towards terminal
        m_drivetrain.driveStraight(144, 0, 0.2);//drive towards terminal
        m_state.setState(State.INTAKE);
        m_drivetrain.driveStraight(-108, 0, 0.2);//reverse to shooting range
        m_state.setState(State.PASSTHROUGH);
        m_state.setState(State.SHOOT);
        m_state.setState(State.INTAKE);
        m_drivetrain.driveStraight(-108, 0, 0.2); //drive forward to collect from human player
        break;

      }

      break;
































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
