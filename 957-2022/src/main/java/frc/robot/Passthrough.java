package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

public class Passthrough {
    private static Passthrough m_passthrough;
    public CANSparkMax pusher = new CANSparkMax(0, MotorType.kBrushless);
    boolean oldSensor = false;
    int cargo = 0;
    int intakeFlag = 0;
    int time = 0;
    int maxTime = 30;

    public void raiseFlag(){
        intakeFlag = 1;
        time = 0;
    }

    
    public void run(){
        time ++;
        if(intakeFlag == 1){
            pusher.set(.5);
            
            if (time == maxTime){
                intakeFlag = 0;
                time = 0;
                pusher.set(0);
            }
        }
    }

  

    public static synchronized Passthrough getInstance(){
        if (m_passthrough == null)
            m_passthrough = new Passthrough();

        return m_passthrough;
    }
}
