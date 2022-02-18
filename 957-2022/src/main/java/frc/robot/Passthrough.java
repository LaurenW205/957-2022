package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Passthrough {
    private static Passthrough m_passthrough;
    public CANSparkMax pusher = new CANSparkMax(0, MotorType.kBrushless);
    boolean oldSensor = false;
    public boolean cargoDown = true;
    int cargo = 0;
    int intakeFlag = 0;
    int time = 0;
    int maxTime = 30;
    int state = 0;

    public void raiseFlag(){
        intakeFlag = 1;
        time = 0;
    }

    
    public void run(int cargo, boolean button){
        time ++;
        if (cargo == 1)  // If we have 1 cargo, we may allow the driver to move the cargo
        {   
            switch(state)
            {
            case 0:         // Check for button press
                if (button && intakeFlag == 0)
                state++;
            break;

            case 1:         // Check for button to be unpressed, if so raise flag to move cargo    
                if (!button)
                {
                    time = 0;
                    if (cargoDown)   // If cargo is down, push up and set cargo to being up
                    {
                      intakeFlag = 1;
                      cargoDown = false;
                    }
                    else        // If cargo is up, push down and set cargo to being down
                    {
                      intakeFlag = -1;
                      cargoDown = true;
                    }
                }
                state++;
            break;

            case 2:         // Ignore any button presses until flag lowers
                if (intakeFlag == 0)
                  state++;
            break;

            }
        }
        else            // If we have 2 cargo, we reset any process that was ongoing in the case statements
          state = 0;
            
        if(intakeFlag != 0){
            pusher.set(.5*intakeFlag);
            
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
