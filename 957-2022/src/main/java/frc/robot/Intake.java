package frc.robot;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Intake {
    DoubleSolenoid doubleSolenoid = new DoubleSolenoid(15, PneumaticsModuleType.CTREPCM, 0, 1);
    CANSparkMax intakeMotor_1 = new CANSparkMax(7, MotorType.kBrushless);
    DigitalInput sensor = new DigitalInput(0);
    double timer = 0;

    public int var = 5;
    boolean lastCycle = false;

    public void extendCyl() {
        doubleSolenoid.set(Value.kForward);
        intakeMotor_1.set(-.4);
    }

    public void retractCyl() {
        doubleSolenoid.set(Value.kReverse);
        intakeMotor_1.set(0);
    }

    public int run(int cargoNum, boolean button){

        Passthrough.getInstance().intakeSensor = sensor.get();

            switch (var) {
                case 0:                 // Wait for button press
                    if (button)
                        var++;
                break;

                case 1:
                    if (!button)        // Wait for button to be unpressed
                        var = 2;
                break;

                case 2:
                    extendCyl();        // Extend cylinder
                    var = 3;
                break;

                case 3:
                    if (button)         // Wait for button press
                        var = 4;
                break;

                case 4:
                    if (!button)        // Wait for button unpress
                        var = 5;
                break;

                case 5:
                    retractCyl();       // Retract arm and return to beginning of cycle
                    var = 0;
                break;
        }
    
        boolean nowCycle = sensor.get();
        if (nowCycle == false) {
            if (lastCycle == true && timer > 0.1){
                Passthrough.getInstance().raiseFlag(cargoNum);
                timer = 0;
                cargoNum++; 
            }
        }


        timer = timer + .02;
        if (cargoNum >= 2 && var != 0)      // Retract cylinder if have maximum cargo
            var = 5;

        lastCycle = nowCycle;

        return cargoNum;
    }

    public void reverse(int cargoNum){
        if(var !=0){ //resets arm to reverse cargo
            retractCyl();
            var = 0;
        }
        Passthrough.getInstance().pusher.set(-0.25);
        cargoNum = 0;
    }


}
