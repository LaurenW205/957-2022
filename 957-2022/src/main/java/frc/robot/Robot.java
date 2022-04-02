// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.automodes.NothingAuto;
import frc.robot.automodes.buddyleft;
import frc.robot.automodes.buddyright;
import frc.robot.automodes.leftcargosupernear;
import frc.robot.automodes.lefttwocargonear;
import frc.robot.automodes.lowbuddyleft;
import frc.robot.automodes.lowbuddyright;
import frc.robot.automodes.midthreecargo;
import frc.robot.automodes.midtwocargofar;
import frc.robot.automodes.righttwocargonear;
import frc.robot.automodes.singlecargo;
import frc.robot.automodes.testauto;


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
   //midtwocargofar m2cf = new midtwocargofar();
   //righttwocargonear r2cn = new righttwocargonear();
   //lefttwocargonear l2cn = new lefttwocargonear();
   //midthreecargo m3c = new midthreecargo();
   //leftcargosupernear leftsup = new leftcargosupernear();
   NothingAuto na = new NothingAuto();
   buddyright br = new buddyright();
   buddyleft bl =  new buddyleft();
   singlecargo sc = new singlecargo();
   testauto ta = new testauto();
   Relay relay = new Relay(3);
   MiniPID pid = new MiniPID(.125/7, 0.005, 0);
   lowbuddyleft lbl = new lowbuddyleft();
   lowbuddyright lbr = new lowbuddyright();

   //:) sus 
   
   int m_timer = 0;
   int m_autoStep = 0;
   String m_autoMode;
   int cargoNum = 0;
   int oldPOV = 0;
   int manualStep = 0;
   int caseNumber = 0;
   int switchSpeed = 0;
   double speedMod = 0;
   int lastPriority = 0;
   int turretSwitch = 0;
   
    //controller red
    //final int k_MoveCargo = 4;        // y , 4
    final int k_CargoChange = 0;        // d pad, up and down
    final int k_PukeController = 1;     // a, 1
    final int k_TurretMode = 9;         // down on left stick, 9
    final int k_LeftBumper = 6;        // axis 2, bumper left
    final int k_RightBumper = 5;       // axis 3, bumper right
    final int k_ReverseIntake = 3;      // x , 3
    //left trigger up climb
    //right trigger down climb
    final int k_SendAlliances = 8; //start
 
    //joystick (drive controller)
    final int k_Shooter = 3;            // right trigger , axis 3
    final int k_FarShooter = 1;         // a , 1
    final int k_CloseShooter = 5;       // left bumper , 5
    final int k_DriveDirection = 9;     // down on left stick, 9
    final int k_PukeJoystick = 6;       // right bumper , 6
    final int k_Intake = 10;            // down on right stick , 10
    final int k_ForceShoot = 2;         // left trigger , axis 2
    final int k_Vision = 4;             // y, 4
    //switch drivemode by pressing anywhere on D pad


    
    // Switches axis for controller
    int switchAxis = 4;
 
    Shooter m_Shooter = new Shooter();
    Turret2 m_Turret = new Turret2();
    Intake m_Intake = new Intake();
    Climbing m_Climbing = new Climbing(5);
    Bling m_Bling = new Bling();

  @Override
  public void robotInit() {
    //CameraServer.startAutomaticCapture();
    //CameraServer.startAutomaticCapture();

    m_Bling.connect();

    pid.setOutputLimits(0.4);


  }

  @Override
  public void robotPeriodic() {
   System.out.println(sb.getTargetX());
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

    if(m_controller.getRawButton(k_SendAlliances)){
      m_Bling.sendAlliance(ally_1, ally_2);
    }

    m_Bling.tick(ally_2, ally_2);

    
  }

  @Override
  public void autonomousInit() {

    m_Shooter.speed = 2650;

    //l2cn.reset();
    //m2cf.reset();
    //r2cn.reset();
    //m3c.reset();
    //leftsup.reset();
    na.reset();
    br.reset();
    bl.reset();
    sc.reset();
    ta.reset();
    lbl.reset();
    lbr.reset();

    
    // set the auto to 1

    m_drivetrain.setIdleMode(IdleMode.kBrake);
    m_drivetrain.resetEncoders();
    m_drivetrain.m_navx.reset();
    // ja1.reset();
    cargoNum = 1; 
    Passthrough.getInstance().raiseFlag(1);
    Passthrough.getInstance().intakeSensor = false;
  }

  @Override
  public void autonomousPeriodic() {

    if (m_autoMode == "No Auto"){
    }else if (m_autoMode == "Auto 1"){
      //l2cn.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if (m_autoMode == "Auto 2"){
      //m2cf.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if (m_autoMode == "Auto 3"){
      //r2cn.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if (m_autoMode == "Auto 4"){
      //m3c.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode == "Auto 5"){
      //leftsup.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode == "Auto 6"){
      na.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode == "Auto 7"){
      br.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode == "Auto 8"){
      bl.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode ==  "Auto 9"){
      sc.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode ==  "Auto 10"){
      ta.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode ==  "Auto 11"){
      lbr.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }else if(m_autoMode ==  "Auto 12"){
      lbl.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    }

    //thc1.run(m_drivetrain, m_Shooter, m_Intake, m_Turret, cargoNum);
    cargoNum = m_Intake.run(cargoNum, m_controller.getRawButton(k_Intake));    
    cargoNum = m_Shooter.run(cargoNum, m_joystick.getRawAxis(k_Shooter) > .5, true, m_controller.getRawButton(k_PukeController)); 
    Passthrough.getInstance().run(cargoNum);

    
  }
    
  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {

    relay.set(Value.kForward);
    int priority = 0;
   // setting priority for different functions
    if(m_controller.getRawButton(k_ReverseIntake)){
      priority = 1;
    }
    if(m_joystick.getRawAxis(k_ForceShoot) > .5){
      priority = 2;
    }

    //sets mode of shooter to puke, near, far and controller puke
    m_Shooter.modeSetting(m_joystick.getRawButton(k_FarShooter),
      m_joystick.getRawButton(k_CloseShooter), 
      m_joystick.getRawButton(k_PukeJoystick),
      m_controller.getRawButton(k_PukeController));
      

    //case statement for priority and choosing which systems to run
    switch(priority){

      case 0: //automatic functions
       cargoNum = m_Intake.run(cargoNum, m_joystick.getRawButton(k_Intake));
       cargoNum =  m_Shooter.run(cargoNum, m_joystick.getRawAxis(k_Shooter) > .5,
        false, m_controller.getRawButton(k_PukeController));
        Passthrough.getInstance().run(cargoNum);

      break;

      case 1: //reverses intake
        m_Intake.reverse(cargoNum);
      break;  
      
      case 2: //force shoots
        m_Shooter.forceShoot();
      break;
    }
    // PRIORITY CLEAN UP

    //resets automatic functions when switching to priority function
    if(priority == 1 && lastPriority == 0){
      m_Shooter.p.setReference(0, ControlType.kVelocity);
      m_Intake.retractCyl();
      m_Intake.var = 0;
      m_Shooter.caseNumber = 0;
      Passthrough.getInstance().intakeFlag = 0;
      m_drivetrain.setCoefficient(1);
    }

    //resets automatic functions when switching to priority function
    if(priority == 2 && lastPriority == 0){
      m_Intake.retractCyl();
      m_Intake.var = 0;
      m_Shooter.caseNumber = 0;
      Passthrough.getInstance().intakeFlag = 0;
      m_drivetrain.setCoefficient(1);
    }

    //stops passthrough motor when switching to automatic functions
    if(priority != 1 && lastPriority == 1){
      Passthrough.getInstance().pusher.set(0);
    }

    //stops shooter and passthrough motor when switching to automatic functions
    if(priority !=2 && lastPriority ==2){
      Passthrough.getInstance().pusher.set(0);
      m_Shooter.p.setReference(0, ControlType.kVelocity);
    }
    
    //checks when priotity is switched
    lastPriority = priority;   
   


    //switches bot orientation
    if (m_controller.getRawButton(k_Vision)){
      double target = sb.getTargetX();
      if (target == -9000)
        target = 160;
      double speed = pid.getOutput(target*0.45, 160*.45);
      m_drivetrain.arcadeDrive(0, speed);

    }
    else {
      pid.reset();
      switch(caseNumber){
        case 0:
          m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1), -m_joystick.getRawAxis(switchAxis));
          if(m_joystick.getRawButton(k_DriveDirection)){
            caseNumber ++;
          }
        break;
  
        case 1:
          m_drivetrain.arcadeDrive(m_joystick.getRawAxis(1), -m_joystick.getRawAxis(switchAxis));
          if(!m_joystick.getRawButton(k_DriveDirection)){
            caseNumber ++;
          }
        break;
  
        case 2:
        m_drivetrain.arcadeDrive(-m_joystick.getRawAxis(1), -m_joystick.getRawAxis(switchAxis));
        if(m_joystick.getRawButton(k_DriveDirection)){
          caseNumber ++;
        }
        break;
  
        case 3:
        m_drivetrain.arcadeDrive(-m_joystick.getRawAxis(1), -m_joystick.getRawAxis(switchAxis));
        if(!m_joystick.getRawButton(k_DriveDirection)){
          caseNumber = 0;
        }
        break;
      }
    }

   

    double buttonUp = m_controller.getRawAxis(2); //left trigger
    double buttonDown = m_controller.getRawAxis(3); //right trigger
    m_Climbing.manualControls(buttonUp, buttonDown);
     
    switch(turretSwitch){ //switches from manual turret mode to automatic turret mode
      case 0: //automatic
        m_Turret.run(m_controller.getRawButton(k_RightBumper),m_controller.getRawButton(k_LeftBumper));
        if(m_controller.getRawButton(k_TurretMode))
          turretSwitch++;
      break;

      case 1: //automatic
        m_Turret.run(m_controller.getRawButton(k_RightBumper) ,m_controller.getRawButton(k_LeftBumper));
        if(!m_controller.getRawButton(k_TurretMode))
          turretSwitch++;
      break;

      case 2: //manual
        m_Turret.manualOverride(-m_controller.getRawAxis(0), -m_controller.getRawAxis(1), 0, m_drivetrain.m_navx.getAngle());
        if(m_controller.getRawButton(k_TurretMode))
          turretSwitch ++;
      break;

      case 3: //manual
        m_Turret.manualOverride(-m_controller.getRawAxis(0), -m_controller.getRawAxis(1), 0, m_drivetrain.m_navx.getAngle());
        if(!m_controller.getRawButton(k_TurretMode))
          turretSwitch = 0;
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
    relay.set(Value.kForward);


  }
}
