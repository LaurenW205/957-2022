package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class Shooter {
    AHRS ahrs = new AHRS(SerialPort.Port.kMXP); 
    CANSparkMax shooter = new CANSparkMax(0, MotorType.kBrushless);
    RelativeEncoder encoder = shooter.getEncoder();
    SparkMaxPIDController p = shooter.getPIDController();
    DigitalInput breakBeamSensor = new DigitalInput(0);
    int caseNumber = 0;

    public Shooter(){
        //set pid constants
    }

    public int run(int cargo, boolean button){
        switch(caseNumber){
        case 0:

           if(button) 
           caseNumber ++;
        break;

        case 1:
            if(!button)
            caseNumber ++;
        break;

        case 2:
            
        break;

        case 3:
            
        break;
        }
    }
}
