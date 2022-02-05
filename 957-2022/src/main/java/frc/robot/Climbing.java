package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;

public class Climbing {

    CANSparkMax climbingMotor;
    SparkMaxPIDController pidController;
    int CanID = -1;
    double MaxExtension = -1;

    public Climbing(int canID, double maxExtension){
        CanID = canID;
        MaxExtension = maxExtension;
        climbingMotor = new CANSparkMax(canID, MotorType.kBrushless);
        climbingMotor.restoreFactoryDefaults();
        pidController = climbingMotor.getPIDController();


        pidController.setP(5e-5);
        pidController.setI(1e-6);
        pidController.setD(0);
        pidController.setIZone(0);
        pidController.setFF(0.000156);
        pidController.setOutputRange(1, -1);
        pidController.setSmartMotionMaxVelocity(2000, 0);
        pidController.setSmartMotionMaxAccel(1500, 0);
        pidController.setReference(maxExtension, CANSparkMax.ControlType.kSmartMotion);

    }

    public void RetractArm() {
        pidController.setReference(0, CANSparkMax.ControlType.kVelocity);
    }

    public void ExtendArm() {
        pidController.setReference(MaxExtension, CANSparkMax.ControlType.kVelocity);
    }

}

