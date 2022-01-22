

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ControlType;


public class DriveTrain{

    CANSparkMax m_rightNeoMaster = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax m_rightNeoSlave = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANEncoder m_rightEncoder = m_rightNeoMaster.getEncoder();
    CANPIDController m_rightController = m_rightNeoMaster.getPIDController();

    CANSparkMax m_leftNeoMaster = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax m_leftNeoSlave = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANEncoder m_leftEncoder = m_rightNeoMaster.getEncoder();
    CANPIDController m_leftController = m_rightNeoMaster.getPIDController();

    private static final int k_freeCurrentLimit = 0;
    private static final int k_stallCurrentLimit = 0;

    private static DriveTrain m_drivetrain = null;

    public DriveTrain(){

        m_rightNeoMaster.restoreFactoryDefaults();
        m_leftNeoMaster.restoreFactoryDefaults();
        m_rightNeoSlave.restoreFactoryDefaults();
        m_leftNeoSlave.restoreFactoryDefaults();
        
    }



    
}