// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.automodes.JankAuto;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 *
 */

public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
   
   DriveTrain m_drivetrain = DriveTrain.getInstance();
   Joystick m_joystick = new Joystick(0);
   Joystick m_controller = new Joystick(1);
   //ShuffleBoard sb = new ShuffleBoard();
   
   int m_timer = 0;
   int m_autoStep = 0;
   int m_autoMode = 0;
   int cargoNum = 0;
   int oldPOV = 0;
   
   // Button ports
   final int k_MoveCargo = 0;
   final int k_RevIntake = 3;   //joystick button 3
   final int k_Intake = 4;      //joystick button 4
   final int k_Turret = 0;      //controller A
   final int k_Climber = 3;     //controller Y
   final int k_CargoChange = 0; //controller d pad
   final int k_Shooter = 1;     //controller B

   //Shooter m_Shooter = new Shooter();
   //Turret2 m_Turret = new Turret2();
   //Intake m_Intake = new Intake();

   JankAuto ja1 = new JankAuto();

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {

    //sb.updateSmartboard(cargoNum, m_autoMode);
    // Next three lines are for testing; can be deleted for competition
    //String ally_1 = sb.getAlly1();
    //String ally_2 = sb.getAlly2();
    //System.out.println("xoxo to: Team " + ally_1 + " & Team " + ally_2);

    if (m_controller.getPOV()==180 && oldPOV != 180){
      cargoNum--;
    }

    if (m_controller.getPOV()==0 && oldPOV !=0){
      cargoNum++;
    }

    if(cargoNum > 2){
      cargoNum = 2;
    }
    
    if(cargoNum < 0){
      cargoNum = 0;
    }
    oldPOV = m_controller.getPOV();
    
  }

  @Override
  public void autonomousInit() {

    m_drivetrain.setIdleMode(IdleMode.kBrake);
    m_drivetrain.resetEncoders();
    m_drivetrain.m_navx.reset();
    ja1.reset();
  }

  @Override
  public void autonomousPeriodic() {

    ja1.run(m_drivetrain);
  }
    
  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {

    m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1), m_joystick.getRawAxis(2));
    //m_Turret.run(m_controller.getRawButton(k_Turret));
    //cargoNum = m_Intake.run(cargoNum, m_joystick.getRawButton(k_Intake), m_joystick.getRawButton(k_RevIntake));    
    //cargoNum = m_Shooter.run(cargoNum, m_controller.getRawButton(k_Shooter)); 
    //Passthrough.getInstance().run(cargoNum, m_controller.getRawButton(k_MoveCargo));
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
