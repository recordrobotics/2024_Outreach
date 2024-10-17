package frc.robot.control;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.utils.DriveCommandData;
import frc.robot.utils.SimpleMath;
import java.lang.Math;

public class Xbox extends AbstractControl {

    private Joystick joystick;
    private XboxController drivebox;
    private double speed_level = 0.8;

    public void Joystick(int joystickPort) {
		joystick = new Joystick(0);
	}

    public Xbox(int driveboxID, int notesboxID) {
        // Sets up xbox controllers
        drivebox = new XboxController(1);
    }

    @Override
    public DriveCommandData getDriveCommandData() {
        // Gets information needed to drive
        DriveCommandData driveCommandData = new DriveCommandData(
                -(getXY().getFirst()*0+getAB().getFirst()) * getDirectionalSpeedLevel(),
                (getXY().getSecond()*0+getAB().getSecond()) * getDirectionalSpeedLevel(),
                (-getSpin()*isJoyOn()+getSpinJoy()) * getSpinSpeedLevel(),
                false);

        // Returns
        return driveCommandData;
    }

    public int isJoyOn() {
        if(Math.abs(getSpinJoy())<=0.1) {
            return 1;
        } else {
            return 0;
        }
    }

    public Pair<Double, Double> getXY() {
        double X = SimpleMath.ApplyThresholdAndSensitivity(drivebox.getRawAxis(0), Constants.Control.XBOX_X_THRESHOLD,
                Constants.Control.XBOX_DIRECTIONAL_SENSITIVITY);
        double Y = SimpleMath.ApplyThresholdAndSensitivity(drivebox.getRawAxis(1), Constants.Control.XBOX_X_THRESHOLD,
                Constants.Control.XBOX_DIRECTIONAL_SENSITIVITY);
         return super.OrientXY(new Pair<Double, Double>(X, Y));
    }

   public Pair<Double, Double> getAB() {
        double A = SimpleMath.ApplyThresholdAndSensitivity(joystick.getX(), Constants.Control.JOYSTICK_X_THRESHOLD, Constants.Control.JOSYSTICK_DIRECTIONAL_SENSITIVITY);
        double B = SimpleMath.ApplyThresholdAndSensitivity(joystick.getY(), Constants.Control.JOYSTICK_Y_THRESHOLD, Constants.Control.JOSYSTICK_DIRECTIONAL_SENSITIVITY);
        return super.OrientXY(new Pair<Double, Double>(A, B));
    }

    public Double getSpin() {
        return SimpleMath.ApplyThresholdAndSensitivity(-drivebox.getRawAxis(4),
                Constants.Control.XBOX_SPIN_ROT_THRESHOLD, Constants.Control.XBOX_SPIN_ROT_SENSITIVITY);
    }

    public Double getSpinJoy() {
        return SimpleMath.ApplyThresholdAndSensitivity(-SimpleMath.Remap(joystick.getTwist(), -1.0, 1.0, -1.0, 1.0), Constants.Control.JOYSTICK_SPIN_THRESHOLD, Constants.Control.JOYSTICK_SPIN_SENSITIVITY);
    }

    public Double getDirectionalSpeedLevel() {
        return speed_level;
    }

    public Double getSpinSpeedLevel() {
        return 1 * speed_level; // 3.14
    }

    @Override
    public Boolean getPoseReset() {
        return drivebox.getRawButtonPressed(7);
    }

    @Override
    public Boolean getKillAuto() {
        return drivebox.getRawButton(8);
    }

    @Override
    public Boolean getShoot() {
        return drivebox.getRightTriggerAxis() > 0.3;
    }

    @Override
    public Boolean getReverse() {
        return drivebox.getRawButton(6);
    }

    @Override
    public Boolean getLoad() {
        return drivebox.getRawButton(5);
    }
}