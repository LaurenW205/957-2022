package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;


import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class DriveTrain{

    //values for MiniPID subject to change
    MiniPID m_auxLoop = new MiniPID(0.03, 0, 0.15);
    MiniPID m_driveLoop = new MiniPID(0.015, 0.0001, 0.02);

    AHRS m_navx = new AHRS(Port.kMXP);

    CANSparkMax m_rightNeoMaster = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax m_rightNeoSlave = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    RelativeEncoder m_rightEncoder = m_rightNeoMaster.getEncoder();
    SparkMaxPIDController m_rightController = m_rightNeoMaster.getPIDController();

    CANSparkMax m_leftNeoMaster = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax m_leftNeoSlave = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    RelativeEncoder m_leftEncoder = m_rightNeoMaster.getEncoder();
    SparkMaxPIDController m_leftController = m_leftNeoMaster.getPIDController();

    private static DriveTrain m_drivetrain = null;
    private static final int k_freeCurrentLimit = 40;
    private static final int k_stallCurrentLimit = 40;

    public DriveTrain(){

        m_navx.reset();
        m_auxLoop.setOutputLimits(-0.2, 0.2);

        m_rightNeoMaster.restoreFactoryDefaults();
        m_leftNeoMaster.restoreFactoryDefaults();
        m_rightNeoSlave.restoreFactoryDefaults();
        m_leftNeoSlave.restoreFactoryDefaults();

        m_rightNeoMaster.setInverted(true);
        m_rightNeoSlave.setInverted(true);

        m_rightNeoSlave.follow(m_rightNeoMaster);
        m_leftNeoSlave.follow(m_leftNeoMaster);

        m_rightNeoMaster.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);
        m_rightNeoSlave.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);
        m_leftNeoMaster.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);
        m_leftNeoSlave.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);

        m_rightController.setP(5e-5);
        m_rightController.setI(1e-6);
        m_rightController.setD(0);
        m_rightController.setIZone(0);
        m_rightController.setFF(0.000156);
        m_rightController.setOutputRange(-1,1);
        m_rightController.setSmartMotionMaxAccel(1500, 0);
        m_rightController.setSmartMotionMaxVelocity(2000, 0);

        m_leftController.setP(5e-5);
        m_leftController.setI(1e-6);
        m_leftController.setD(0);
        m_leftController.setIZone(0);
        m_leftController.setFF(0.000156);
        m_leftController.setOutputRange(-1,1);
        m_leftController.setSmartMotionMaxAccel(1500, 0);
        m_leftController.setSmartMotionMaxVelocity(2000, 0);
    }

    public void resetEncoders(){
        m_rightEncoder.setPosition(0);
        m_leftEncoder.setPosition(0);
    }

    public void setIdleMode(IdleMode mode){
        m_rightNeoMaster.setIdleMode(mode);
        m_rightNeoSlave.setIdleMode(mode);
        m_leftNeoMaster.setIdleMode(mode);
        m_leftNeoSlave.setIdleMode(mode);
    }

    public static DriveTrain getInstance(){
        if(m_drivetrain == null)
        m_drivetrain = new DriveTrain();

        return m_drivetrain;
    }

    double outputT = 0;
    double outputD = 0;
    double ramp = 0.1;

    public void arcadeDrive(double speed, double turn){
    
        turn = deadband(turn/2);
        speed = deadband(speed);

        outputD = outputD + (outputD - speed) * -ramp;
        outputT = outputT + (outputT - turn) * -ramp;
        
        m_rightNeoMaster.set(outputD+turn);
        m_leftNeoMaster.set(outputD-turn);

    }

    double deadband(double value) {
        /* Upper deadband */
        if (value >= +0.20 ) 
            return value-0.2;
        
        /* Lower deadband */
        if (value <= -0.20)
            return value+0.2;
        
        /* Outside deadband */
        return 0;
    }


    public boolean driveStraight(double inches, double targetHeading, double speed){

        m_driveLoop.setOutputLimits(-speed, speed);

        double setpoint = inches*0.333;

        if(Math.abs(inches+m_rightEncoder.getPosition()) > 10){
            m_driveLoop.resetI();
        }
        double output = m_driveLoop.getOutput(-m_rightEncoder.getPosition(), setpoint);
        double turn = 0;
        if(m_navx.getAngle() > +0.1){
            turn = 0.1;
        }
        if(m_navx.getAngle() < -0.1){
            turn = -0.1;
        }
        m_leftNeoMaster.set(-turn-output);
        m_rightNeoMaster.set(-turn-output);
  
        if (Math.abs(inches+m_rightEncoder.getPosition()/0.333) < 1){
            return true;
        }else{
            return false;
        }
    }

    public void turnTo(double inches, double targetAngle, double speed){

        double setpoint = inches*0.333;

        double margin = (setpoint- targetAngle)/30;
       
        if(margin < -0.5){
            speed = -0.5;
        }
        if(margin > 0.5){
            speed = 0.5;
        }

       //return targetAngle//
    }

    public double target(double targetLocation){
        
        double currentLocation = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        SmartDashboard.putNumber("limelight", currentLocation);
        double output = m_auxLoop.getOutput(currentLocation, targetLocation);
        m_rightNeoMaster.set(output);
        m_leftNeoMaster.set(-output);
        return currentLocation;

    }


}