package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Joystick;

public class Climbing {

    CANSparkMax m_leftMotor;
    public CANSparkMax m_rightMotor;
    SparkMaxPIDController pidController;
    int CanID = -1;
    double MaxExtension = -1;

    public Climbing(double maxExtension){

        MaxExtension = maxExtension; 
        m_leftMotor = new CANSparkMax(5, MotorType.kBrushless);
        m_rightMotor = new CANSparkMax(8, MotorType.kBrushless);
        m_leftMotor.restoreFactoryDefaults();
        m_rightMotor.restoreFactoryDefaults();
        pidController = m_leftMotor.getPIDController();


        pidController.setP(5e-5);
        pidController.setI(1e-6);
        pidController.setD(0);
        pidController.setIZone(0);
        pidController.setFF(0.000156);
        pidController.setOutputRange(-0.2, 0.2);
        pidController.setSmartMotionMaxVelocity(2000, 0);
        pidController.setSmartMotionMaxAccel(1500, 0);
        pidController.setReference(maxExtension, CANSparkMax.ControlType.kSmartMotion);

        m_leftMotor.setIdleMode(IdleMode.kBrake);
        m_rightMotor.setIdleMode(IdleMode.kBrake);



        m_leftMotor.follow(m_rightMotor, true);
    }

    public void RetractArm() {
        pidController.setReference(0, CANSparkMax.ControlType.kSmartMotion);
    }

    public void ExtendArm() {
        pidController.setReference(MaxExtension, CANSparkMax.ControlType.kSmartMotion);
    }

    public void manualControls(double buttonUp, double buttonDown){
        if(buttonUp > 0.5){
            m_rightMotor.set(0.5);
        }else if(buttonDown > 0.5){
            m_rightMotor.set(-0.5);
        }else{
            m_rightMotor.set(0);
        }
    }
}

