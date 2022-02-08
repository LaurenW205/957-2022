package frc.robot;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Intake {
    DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 0);
    CANSparkMax intakeMotor = new CANSparkMax(0, MotorType.kBrushless);
    DigitalInput sensor = new DigitalInput(0);
    int var = 5;
    boolean lastCycle = false;

    Rect r;
    RedVision g = new RedVision();
    
    int centerX = 0;
    int centerY = 0;

    public int getX(int target) {
        r = Imgproc.boundingRect(g.filterContoursOutput().get(target));
        centerX = r.x + (r.width / 2);
        return centerX;
    }

    public int getY(int target) {
        r = Imgproc.boundingRect(g.filterContoursOutput().get(target));
        centerY = r.y + (r.height / 2);
        return centerY;
    }


    public Intake(){

        new Thread(() -> {
            CameraServer.startAutomaticCapture();
            CvSink sink;
            CvSource outputStream = CameraServer.putVideo("Blur", 320, 240); // Remove this for competitions
            
            Mat mat = new Mat();
            sink = CameraServer.getVideo();
      
      
            while(!Thread.interrupted()) {
      
              if (sink.grabFrame(mat) == 0) {
                continue;
              }
              
              g.process(mat);
              outputStream.putFrame(g.hsvThresholdOutput);  // Remove this for competition (expensive operation)
      
            }
          }).start();

    }

    public void extendCyl() {
        doubleSolenoid.set(Value.kForward);
        intakeMotor.set(1);
    }

    public void retractCyl() {
        doubleSolenoid.set(Value.kReverse);
        intakeMotor.set(0);
    }

    public int run(int cargoNum, boolean button, boolean rev) 
    {
        if(rev)
        {
            intakeMotor.set(-1);
            cargoNum = 0;
        }
        else
        {
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

    }

        boolean nowCycle = sensor.get();

        if (nowCycle == false) {
            if (lastCycle == true)
                cargoNum++;
        }

        if (cargoNum >= 2 && var != 0)      // Retract cylinder if have maximum cargo
            var = 5;

        lastCycle = nowCycle;

        return cargoNum;
    }
}
