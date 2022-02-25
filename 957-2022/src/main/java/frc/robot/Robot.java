// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
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
   ShuffleBoard sb = new ShuffleBoard();
   
   int m_timer = 0;
   int m_autoStep = 0;
   int m_autoMode = 0;
   int cargoNum = 0;
   int oldPOV = 0;
   int manualStep = 0;
   
   // Button ports
   final int k_MoveCargo = 0;
   final int k_RevIntake = 3;   //joystick button 3
   final int k_Intake = 4;      //joystick button 4
   final int k_Turret = 0;      //controller A
   final int k_Climber = 3;     //controller Y
   final int k_CargoChange = 0; //controller d pad
   final int k_Shooter = 1;     //controller B
   final int k_ManualSwitch = 3; //controller X


   Shooter m_Shooter = new Shooter();
   Turret2 m_Turret = new Turret2();
   Intake m_Intake = new Intake();
   Climbing m_Climbing = new Climbing(5);
   Bling m_Bling = new Bling();

   JankAuto ja1 = new JankAuto();

  @Override
  public void robotInit() {
    m_Bling.connect();
  }

  @Override
  public void robotPeriodic() {

    //sb.updateSmartboard(cargoNum, m_autoMode);
    // Next three lines are for testing; can be deleted for competition
    String ally_1 = sb.getAlly1();
    String ally_2 = sb.getAlly2();
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

    m_Bling.tick(ally_1, ally_2);
    
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

    double buttonUp = m_controller.getRawAxis(2);
    double buttonDown = m_controller.getRawAxis(3);
    
    m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1), m_joystick.getRawAxis(2));
    
    cargoNum = m_Intake.run(cargoNum, m_joystick.getRawButton(k_Intake), m_joystick.getRawButton(k_RevIntake));    
    cargoNum = m_Shooter.run(cargoNum, m_controller.getRawButton(k_Shooter)); 
    Passthrough.getInstance().run(cargoNum, m_controller.getRawButton(k_MoveCargo));

    if(buttonUp> .75){
       m_Climbing.ExtendArm();
    }

    if(buttonDown> .75){
      m_Climbing.RetractArm();
    }

    switch(manualStep){
      case 0:
        m_Turret.run(m_controller.getRawButton(k_Turret));
        if(m_controller.getRawButton(k_Turret))
          manualStep++;
      break;

      case 1:
        m_Turret.run(m_controller.getRawButton(k_Turret));
        if(!m_controller.getRawButton(k_Turret)){
          manualStep++;
        }
      break;

      case 2:
        m_Turret.manualOverride(m_controller.getRawAxis(0), m_controller.getRawAxis(1), 0);
        if(m_controller.getRawButton(k_Turret)){
          manualStep++;
        }
      break;

      case 3:
        m_Turret.manualOverride(m_controller.getRawAxis(0), m_controller.getRawAxis(1), 0);
        if(!m_controller.getRawButton(k_Turret)){
          manualStep = 0;
        }
      break;
    }


  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {

    double buttonUp = m_controller.getRawAxis(2); //left trigger
    double buttonDown = m_controller.getRawAxis(3); //right trigger

    boolean intakeForward = m_controller.getRawButton(4); //left button
    boolean intakeBackward = m_controller.getRawButton(5); //right button

    boolean intakeCylExtend = m_controller.getRawButton(6);
    boolean intakeCylRetract = m_controller.getRawButton(7);
    
    boolean ptForward = m_controller.getRawButton(2); //x
    boolean ptBackward = m_controller.getRawButton(3); //y
    
    boolean shooterForward = m_controller.getRawButton(0); //a
    boolean shooterBackward =  m_controller.getRawButton(1); //b

    double x_axis = m_controller.getRawAxis(0); //left stick
    double y_axis = m_controller.getRawAxis(1);
    

    if(intakeForward){
       m_Intake.intakeMotor_1.set(0.5);
      }else if(intakeBackward){
        m_Intake.intakeMotor_1.set(-0.5);
      }else{
        m_Intake.intakeMotor_1.set(0);
      }


    if(intakeCylExtend){
      m_Intake.extendCyl();
    }
    if(intakeCylRetract){
      m_Intake.retractCyl();
    }


    if(ptForward){
       Passthrough.getInstance().pusher.set(0.5);
      }else if(ptBackward){
        Passthrough.getInstance().pusher.set(-0.5);
      }else{
        Passthrough.getInstance().pusher.set(0);
      }
    

    if(shooterForward){
        m_Shooter.shooter.set(0.5);
      }else if(shooterBackward){
        m_Shooter.shooter.set(-0.5);
      }else{
        m_Shooter.shooter.set(0);
      }



    m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1), m_joystick.getRawAxis(2));

    m_Climbing.manualControls(buttonUp, buttonDown);
    
    m_Turret.manualOverride(x_axis, y_axis, 0);
  }
}
