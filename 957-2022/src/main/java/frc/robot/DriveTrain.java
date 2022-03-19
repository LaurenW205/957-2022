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
   

    public AHRS m_navx = new AHRS(Port.kMXP);

    CANSparkMax m_rightNeoMaster = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless); //1
    CANSparkMax m_rightNeoSlave = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless); //2
    RelativeEncoder m_rightEncoder = m_rightNeoMaster.getEncoder();;
    SparkMaxPIDController m_rightController = m_rightNeoMaster.getPIDController();

    CANSparkMax m_leftNeoMaster = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless); //3
    CANSparkMax m_leftNeoSlave = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless); //4
    RelativeEncoder m_leftEncoder = m_leftNeoMaster.getEncoder();
    SparkMaxPIDController m_leftController = m_leftNeoMaster.getPIDController();

    double box1 = 0;
    double box2 = 0;
    double box3 = 0;
    double box4 = 0;

    private static DriveTrain m_drivetrain = null;
    private static final int k_freeCurrentLimit = 40;
    private static final int k_stallCurrentLimit = 40;
    
    public double time = 0;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;

    public DriveTrain(){

        m_navx.reset();
    
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
    double ramp = 0.2;

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
        if (value >= +0.10 ) 
            return value-0.1;
        
        /* Lower deadband */
        if (value <= -0.10)
            return value+0.1;
        
        /* Outside deadband */
        return 0;
    }

    //Drive straight function
    public boolean driveJank(double targetAngle, double feet, double targetSpeed){

        double setpoint = (feet*5.94);

        if(Math.abs(m_leftEncoder.getPosition()-setpoint)<2){

            m_leftNeoMaster.set(0);
            m_rightNeoMaster.set(0);
            return true;

        }

        double speed = 0;

        if(setpoint > 0){

            speed = targetSpeed;
        }else{

            speed = -targetSpeed;
        }

        if(targetAngle-m_navx.getAngle() > 0.5){

            m_rightNeoMaster.set(speed-0.0125);
            m_leftNeoMaster.set(speed+0.0125);

        }else if(targetAngle-m_navx.getAngle() <-0.5){

            m_rightNeoMaster.set(speed+0.0125);
            m_leftNeoMaster.set(speed-0.0125);
            
        }else{

            m_leftNeoMaster.set(speed);
            m_rightNeoMaster.set(speed);
        }

        return false;
    }

    //turning function
    public boolean turnJank(double targetAngle){

        if (m_navx.getAngle() > targetAngle){
            
            m_rightNeoMaster.set(0.1);
            m_leftNeoMaster.set(-0.1);

        }else{

            m_rightNeoMaster.set(-0.1);
            m_leftNeoMaster.set(0.1);
        
        }

        System.out.println(m_navx.getAngle());
        
        if (1 > Math.abs(targetAngle-m_navx.getAngle())){

            m_rightNeoMaster.set(0);
            m_leftNeoMaster.set(0);
           return true;

        }else{

            return false;
        }
    
    }

}