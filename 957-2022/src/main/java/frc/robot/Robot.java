// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
   //Turret m_turret = new Turret();

   int m_timer = 0;
   int m_autoStep = 0;
   int m_autoMode = 0;
   RobotState m_state = RobotState.getInstance();

  int cargoNum = 0;
  public void updateSmartboard() {
    SmartDashboard.putNumber("Cargo", cargoNum);
    
  }

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {
    updateSmartboard();
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {

    System.out.println(m_autoStep);

    switch(m_autoMode){

      case 0:

      switch(m_autoStep){

        case 0:

        m_drivetrain.driveStraight(113, 0, 0.2);

      }

      break;

      case 1: 
      
      switch(m_autoStep) {
      
        case 0:

        if(m_drivetrain.driveStraight(113.2, 0, 0.4));   // Push alliance bot off tarmac
        m_autoStep++;
        m_drivetrain.resetEncoders();
        m_drivetrain.arcadeDrive(0, 0);
        m_state.setState(State.WAITING);
      
        break;

        case 1: 

        if(m_drivetrain.driveStraight(5.5, 0, 0.2)); // Push further for room
        {
        m_autoStep++;
        m_drivetrain.resetEncoders();
        m_drivetrain.arcadeDrive(0, 0);
        m_state.setState(State.WAITING);
        }
        break;

        case 2: 

          if(m_drivetrain.driveStraight(-5.5, 0, 0.2));  // Drive back to give room
          {
          m_autoStep++;
          m_drivetrain.resetEncoders();
          m_drivetrain.arcadeDrive(0, 0);
          m_state.setState(State.WAITING);
          }
        
        break;

        case 3:

          m_drivetrain.turnTo(-137.0, 0, 0.2);    // Rotate bot to face CARGO
          m_timer = m_timer + 20;                 // Timer values subject to change.
          if(m_timer == 500)
          {
            m_drivetrain.resetEncoders();
            m_autoStep++; 
          } 

        break;
      }

      break;

      case 2:
      
       switch(m_autoStep){

        case 0:

          if(m_drivetrain.driveStraight(46, 0, 0.5)){ //drive out of tarmac
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
            m_state.setState(State.INTAKE); //intake cargo
          } 
          
          break;

        case 1:
         
          if(m_drivetrain.driveStraight(24, 0, 0.5)){
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
            m_state.setState(State.SHOOT);

          }

          break;

        case 2:

          m_drivetrain.turnTo(45, 0, 0.5);
          m_timer = m_timer + 20;
          if(m_timer == 500){
            m_drivetrain.resetEncoders();
            m_autoStep++;
          }

          break;

        case 3:

         if(m_drivetrain.driveStraight(120, 0, 0.5)){
           m_autoStep++;
           m_drivetrain.arcadeDrive(0, 0);
           m_state.setState(State.SHOOT);

         }

        break;
       }
   


       case 3:

       switch(m_autoStep){
      
          case 0: //drive to avoid cargo
          if(m_drivetrain.driveStraight(24, 0, 0.2)){
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);

          }
          break;

        case 1: //turn to terminal
          m_drivetrain.turnTo(45, 0, 0.2);
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
        break;

        case 2: //drive to terminal and collect cargo
          if(m_drivetrain.driveStraight(144, 0, 0.2)){
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
            m_state.setState(State.INTAKE); //two state statements in one case?
            m_state.setState(State.PASSTHROUGH);
          }
        break;

        case 3: //reverse to shooting range
          if(m_drivetrain.driveStraight(-108, 0, 0.2)){
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
            m_state.setState(State.SHOOT);
          }
        break;

        case 4: //collect from human player
          if(m_drivetrain.driveStraight(108, 0, 0.2)){
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
            m_state.setState(State.INTAKE);
          }
        break;
        
      }

      break;

      case 4:

      switch(m_autoStep){
        case 0:
          if(m_drivetrain.driveStraight(40, 0, 0.2)){
            m_autoStep ++;
            m_drivetrain.arcadeDrive(0, 0);
            m_state.setState(State.INTAKE);
            m_state.setState(State.SHOOT);
          }
        break;

        case 1:
          m_drivetrain.turnTo(45, 0, 0.2);
          m_autoStep ++;
        break;

        case 2:
         if(m_drivetrain.driveStraight(160, 0, 0.2)){
           m_autoStep ++;
           m_drivetrain.arcadeDrive(0, 0);
         }
        break;

        case 3:
          m_drivetrain.turnTo(-45, 0, 0.2);
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
        break;

        case 4:
          if(m_drivetrain.driveStraight(60, 0, 0.2)){
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
            m_state.setState(State.INTAKE); //collect cargo from terminal
          }
        break;

        case 5:
          if(m_drivetrain.driveStraight(-108, 0, 0.2)){
            m_state.setState(State.SHOOT);
            m_autoStep++;
            m_drivetrain.arcadeDrive(0, 0);
          }
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
