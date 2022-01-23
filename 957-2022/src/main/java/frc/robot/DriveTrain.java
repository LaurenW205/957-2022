package frc.robot;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class DriveTrain{

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
}