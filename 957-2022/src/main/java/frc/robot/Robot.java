// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.automodes.JankAuto;
import frc.robot.automodes.leftcargosupernear;
import frc.robot.automodes.lefttwocargonear;
import frc.robot.automodes.midthreecargo;
import frc.robot.automodes.midtwocargofar;
import frc.robot.automodes.righttwocargonear;


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
   midtwocargofar m2cf = new midtwocargofar();
   righttwocargonear r2cn = new righttwocargonear();
   lefttwocargonear l2cn = new lefttwocargonear();
   midthreecargo m3c = new midthreecargo();
   leftcargosupernear leftsup = new leftcargosupernear();
   
   int m_timer = 0;
   int m_autoStep = 0;
   String m_autoMode;
   int cargoNum = 0;
   int oldPOV = 0;
   int manualStep = 0;
   int caseNumber = 0;
   double speedMod = 0;

   
    //controller
    final int k_RevIntake = 3;      // X?
    final int k_Intake = 2;         // B,2
    final int k_MoveCargo = 4;      // Y,4
    final int k_CargoChange = 0;    //d pad, up and down
 
    //joystick
    final int k_Shooter = 1;        // TRIGGER
    final int k_FarShooter = 12;    // button 12
    final int k_CloseShooter = 11;  //button 11
    final int k_Puke2 = 9;
    final int k_DriveDirection = 7; //button 7
    final int k_SpeedDial = 3;// flip switch, axis 3
 
    Shooter m_Shooter = new Shooter();
    Turret2 m_Turret = new Turret2();
    Intake m_Intake = new Intake();
    Climbing m_Climbing = new Climbing(5);
    //Bling m_Bling = new Bling();

  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();
    CameraServer.startAutomaticCapture();

    //m_Bling.connect();

  }

  @Override
  public void robotPeriodic() {
    sb.updateSmartboard(cargoNum, m_drivetrain);
    sb.updateAuto();
    String ally_1 = sb.getAlly1();
    String ally_2 = sb.getAlly2();

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

    //m_Bling.tick("955", "997");
    //m_Bling.tick(ally_1, ally_2);
    
  }

  @Override
  public void autonomousInit() {

    m_Shooter.speed = 2650;
    m_autoMode = sb.updateAuto();

    l2cn.reset();
    m2cf.reset();
    r2cn.reset();
    m3c.reset();
    leftsup.reset();

    
    // set the auto to 1

    m_drivetrain.setIdleMode(IdleMode.kBrake);
    m_drivetrain.resetEncoders();
    m_drivetrain.m_navx.reset();
    // ja1.reset();
    cargoNum = 1; 
  }

  @Override
  public void autonomousPeriodic() {

    if (m_autoMode == "No Auto"){

    }else if (m_autoMode == "Auto 1"){
      l2cn.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if (m_autoMode == "Auto 2"){
      m2cf.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if (m_autoMode == "Auto 3"){
      r2cn.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if (m_autoMode == "Auto 4"){
      m3c.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode == "Auto 5"){
      leftsup.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }

    //thc1.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    cargoNum = m_Intake.run(cargoNum, m_controller.getRawButton(k_Intake), m_controller.getRawButton(k_RevIntake));    
    cargoNum = m_Shooter.run(cargoNum, m_joystick.getRawButton(k_Shooter), m_joystick.getRawButton(k_FarShooter),m_joystick.getRawButton(k_CloseShooter), m_joystick.getRawButton(k_Puke2)); 
    Passthrough.getInstance().run(cargoNum, m_controller.getRawButton(k_MoveCargo), m_controller.getRawButton(k_RevIntake));

    
  }
    
  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {

    if(m_joystick.getRawAxis(k_SpeedDial) < 0.5){
      speedMod = 0.5;
    }else{
      speedMod = m_joystick.getRawAxis(3);
    }

    //switches bot orientation
    switch(caseNumber){
      case 0:
        m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1)*speedMod, -m_joystick.getRawAxis(2));
        if(m_joystick.getRawButton(k_DriveDirection)){
          caseNumber ++;
        }
      break;

      case 1:
        m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1)*speedMod, -m_joystick.getRawAxis(2));
        if(!m_joystick.getRawButton(k_DriveDirection)){
          caseNumber ++;
        }
      break;

      case 2:
      m_drivetrain.arcadeDrive(-m_joystick.getRawAxis(1)*speedMod, -m_joystick.getRawAxis(2));
      if(m_joystick.getRawButton(k_DriveDirection)){
        caseNumber ++;
      }
      break;

      case 3:
      m_drivetrain.arcadeDrive(-m_joystick.getRawAxis(1)*speedMod, -m_joystick.getRawAxis(2));
      if(!m_joystick.getRawButton(k_DriveDirection)){
        caseNumber = 0;
      }
      break;
    }

    cargoNum = m_Intake.run(cargoNum, m_controller.getRawButton(k_Intake), m_controller.getRawButton(k_RevIntake));    
    cargoNum = m_Shooter.run(cargoNum, m_joystick.getRawButton(k_Shooter), m_joystick.getRawButton(k_FarShooter), m_joystick.getRawButton(k_CloseShooter), m_joystick.getRawButton(k_Puke2)); 
    Passthrough.getInstance().run(cargoNum, m_controller.getRawButton(k_MoveCargo), m_controller.getRawButton(k_RevIntake));

    double buttonUp = m_controller.getRawAxis(2); //left trigger
    double buttonDown = m_controller.getRawAxis(3); //right trigger
    m_Climbing.manualControls(buttonUp, buttonDown);

    m_Turret.manualOverride(-m_controller.getRawAxis(0), -m_controller.getRawAxis(1), 0, m_drivetrain.m_navx.getAngle());
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
 
    boolean intakeForward = m_controller.getRawButton(5); //left button
    boolean intakeBackward = m_controller.getRawButton(6); //right button

    boolean intakeCylExtend = m_controller.getRawButton(7); // face buttons left
    boolean intakeCylRetract = m_controller.getRawButton(8); // right
    
    boolean ptForward = m_controller.getRawButton(3); //x
    boolean ptBackward = m_controller.getRawButton(4); //y
    
    boolean shooterForward = m_controller.getRawButton(1); //a
    boolean shooterBackward =  m_controller.getRawButton(2); //b

    double x_axis = m_controller.getRawAxis(0); //left stick
    double y_axis = -m_controller.getRawAxis(1);
    

    if(intakeForward){
       m_Intake.intakeMotor_1.set(0.1);
      }else if(intakeBackward){
        m_Intake.intakeMotor_1.set(-0.1);
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
       Passthrough.getInstance().pusher.set(0.1);
      }else if(ptBackward){
        Passthrough.getInstance().pusher.set(-0.1);
      }else{
        Passthrough.getInstance().pusher.set(0);
      }
    

    if(shooterForward){
        // m_Shooter.shooter.set(0.1);
      }else if(shooterBackward){
        // m_Shooter.shooter.set(-0.1);
      }else{
        // m_Shooter.shooter.set(0);
      }
    
      m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1), m_joystick.getRawAxis(2));

    m_Climbing.manualControls(buttonUp, buttonDown);
    System.out.println(m_Climbing.m_rightMotor.getEncoder().getPosition());
    
   //  m_Turret.manualOverride(-x_axis, y_axis, 0);
  }
}
