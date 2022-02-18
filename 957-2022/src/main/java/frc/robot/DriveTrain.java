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

    public AHRS m_navx = new AHRS(Port.kMXP);

    CANSparkMax m_rightNeoMaster = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax m_rightNeoSlave = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    RelativeEncoder m_rightEncoder = m_rightNeoMaster.getEncoder();;
    SparkMaxPIDController m_rightController = m_rightNeoMaster.getPIDController();

    CANSparkMax m_leftNeoMaster = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax m_leftNeoSlave = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    RelativeEncoder m_leftEncoder = m_leftNeoMaster.getEncoder();
    SparkMaxPIDController m_leftController = m_leftNeoMaster.getPIDController();

    double box1 = 0;
    double box2 = 0;
    double box3 = 0;
    double box4 = 0;

    private static DriveTrain m_drivetrain = null;
    private static final int k_freeCurrentLimit = 40;
    private static final int k_stallCurrentLimit = 40;

    private static final int deviceID = 1;
    
    public double time = 0;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;

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
        m_rightController.setOutputRange(-0.35,0.35);
        m_rightController.setSmartMotionMaxAccel(1500, 0);
        m_rightController.setSmartMotionMaxVelocity(2000, 0);

        m_leftController.setP(5e-5);
        m_leftController.setI(1e-6);
        m_leftController.setD(0);
        m_leftController.setIZone(0);
        m_leftController.setFF(0.000156);
        m_leftController.setOutputRange(-0.35,0.35);
        m_leftController.setSmartMotionMaxAccel(1500, 0);
        m_leftController.setSmartMotionMaxVelocity(2000, 0);

        maxVel = 1200; 
        maxAcc = 1500;

        int smartMotionSlot = 0;
        m_rightController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        m_rightController.setSmartMotionMinOutputVelocity(0, smartMotionSlot);
        m_rightController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    
        m_leftController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        m_leftController.setSmartMotionMinOutputVelocity(0, smartMotionSlot);
        m_leftController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        
    }

    public void resetPID(){

        m_rightController.setIAccum(0);
        m_leftController.setIAccum(0);
    }

    public void revSucks(){

        int smartMotionSlot = 0;

        m_rightNeoMaster.restoreFactoryDefaults();

        m_rightNeoSlave.follow(m_rightNeoMaster);

        m_rightNeoMaster.setIdleMode(IdleMode.kCoast);
        m_rightEncoder = m_rightNeoMaster.getEncoder();
        m_rightController = m_rightNeoMaster.getPIDController();

        m_rightNeoMaster.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);
        m_rightNeoSlave.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);

        m_rightController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        m_rightController.setSmartMotionMinOutputVelocity(0, smartMotionSlot);
        m_rightController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);

        m_rightController.setP(5e-5);
        m_rightController.setI(1e-6);
        m_rightController.setD(0);
        m_rightController.setIZone(0);
        m_rightController.setFF(0.000156);
        m_rightController.setOutputRange(-1,1);
        m_rightController.setSmartMotionMaxAccel(1500, 0);
        m_rightController.setSmartMotionMaxVelocity(2000, 0);
    }

    public void gyroStuff(){

       box4 = box3;
       box3 = box2;
       box2 = box1;
       box1 = m_leftEncoder.getPosition();
      
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

        m_rightNeoMaster.setInverted(true);
        m_rightNeoSlave.setInverted(true);
    
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

        m_leftNeoMaster.setInverted(true);
        //m_rightNeoSlave.setInverted(true);
        m_rightNeoMaster.follow(m_leftNeoMaster,true);

        
        int smartMotionSlot = 0;
        double turnOffset = 0;

        if(targetHeading-m_navx.getAngle()> 1 ){
            turnOffset = 0.05;
        }else if(targetHeading-m_navx.getAngle() < -1){
            turnOffset = -0.05;
        }

        
        //m_rightController.setSmartMotionAllowedClosedLoopError(0.25, smartMotionSlot);
        m_leftController.setSmartMotionAllowedClosedLoopError(0.25, smartMotionSlot);
        m_leftController.setReference(-inches*5.94, CANSparkMax.ControlType.kSmartMotion, 0, 0);
       // m_rightController.setReference(inches*5.94, CANSparkMax.ControlType.kSmartMotion, 0, 0);
        
        if (Math.abs(inches*5.94+m_rightEncoder.getPosition())< 2){

            //m_leftNeoMaster.set(0);
            //m_rightNeoMaster.set(0);
            return true;
        }else{

            //m_leftNeoMaster.set(0.2 + turnOffset);
            ///m_rightNeoMaster.set(0.2 + -turnOffset);
            return false;
        }

    }

    public boolean driveJank(double targetAngle, double feet){

        double setpoint = (feet*5.94);

        if(Math.abs(m_leftEncoder.getPosition()-setpoint)<2){

            m_leftNeoMaster.set(0);
            m_rightNeoMaster.set(0);
            return true;

        }

        double speed = 0;

        if(setpoint > 0){

            speed = 0.25;
        }else{

            speed = -0.25;
        }

        if(targetAngle-m_navx.getAngle() > 0.5){

            m_rightNeoMaster.set(speed-0.05);
            m_leftNeoMaster.set(speed+0.05);

        }else if(targetAngle-m_navx.getAngle() <-0.5){

            m_rightNeoMaster.set(speed+0.05);
            m_leftNeoMaster.set(speed-0.05);
            
        }else{

            m_leftNeoMaster.set(speed);
            m_rightNeoMaster.set(speed);
        }

        return false;
    }
        
    public boolean turnTo(double targetAngle){

        System.out.println(m_navx.getAngle());

        m_leftNeoMaster.setInverted(false);
        m_rightNeoSlave.setInverted(false);
        m_rightNeoMaster.follow(m_leftNeoMaster, false);

        int smartMotionSlot = 0;

        //double litteE = targetAngle + (m_navx.getAngle());
        double average = ((-m_rightEncoder.getPosition())+ (m_leftEncoder.getPosition()))/2;
        double setpoint = ((targetAngle/360*2*5.94)*3.4);

        m_leftController.setReference(-setpoint, CANSparkMax.ControlType.kSmartMotion, 0, 0);
        m_leftController.setSmartMotionAllowedClosedLoopError(0.1, smartMotionSlot);

        System.out.println(time);

        if (Math.abs(-m_leftEncoder.getPosition()-setpoint) < 2){
            time += 0.02;
            if (time > 1){
               return true;
            }else{
                return false;
            }
        
        }else{
            time = 0;
            return false;
        

        }
    
    
    }

    public boolean turnJank(double targetAngle){

        if (m_navx.getAngle() > targetAngle){
            
            m_rightNeoMaster.set(0.15);
            m_leftNeoMaster.set(-0.15);

        }else{

            m_rightNeoMaster.set(-0.15);
            m_leftNeoMaster.set(0.15);
        
        }

        if (1 > Math.abs(targetAngle-m_navx.getAngle())){

           return true;

        }else{

            return false;
        }
    
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