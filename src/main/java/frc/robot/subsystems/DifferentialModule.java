package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.shuffleboard.ShuffleboardUI;
import frc.robot.utils.ModuleConstants;

public class DifferentialModule {

  private static double[] graph = new double[4];

  // Creates variables for motors and absolute encoders
  private final CANSparkMax m_driveMotor;
  private final CANSparkMax m_driveMotorFollower;

  private final ProfiledPIDController drivePIDController;
  private final SimpleMotorFeedforward driveFeedForward;

  private final double DRIVE_GEAR_RATIO;
  private final double WHEEL_DIAMETER;

  /**
   * Constructs a SwerveModule with a drive motor, turning motor, and absolute
   * turning encoder.
   * 
   * @param m - a ModuleConstants object that contains all constants relevant for
   *          creating a swerve module.
   *          Look at ModuleConstants.java for what variables are contained
   */
  public DifferentialModule(ModuleConstants m) {
    // Creates TalonFX objects
    m_driveMotor = new CANSparkMax (m.driveMotorChannel, MotorType.kBrushless);
    m_driveMotorFollower = new CANSparkMax (m.driveMotorFollowerChannel, MotorType.kBrushless);
    m_driveMotorFollower.follow(m_driveMotor);
    m_driveMotor.setInverted(m.inverted);

    // Creates other variables
    this.DRIVE_GEAR_RATIO = m.DRIVE_GEAR_RATIO;
    this.WHEEL_DIAMETER = m.WHEEL_DIAMETER;

    // ~2 Seconds delay per swerve module
    Timer.delay(2.3);

    // Sets motor speeds to 0
    m_driveMotor.set(0);

    // Creates PID Controllers
    this.drivePIDController = new ProfiledPIDController(
        m.DRIVE_KP,
        m.DRIVE_KI,
        m.DRIVE_KD,
        new TrapezoidProfile.Constraints(m.DriveMaxAngularVelocity, m.DriveMaxAngularAcceleration));

    this.driveFeedForward = new SimpleMotorFeedforward(
        m.DRIVE_FEEDFORWARD_KS,
        m.DRIVE_FEEDFORWARD_KV);

    // Sets up shuffleboard
    setupShuffleboard(m.driveMotorChannel);
  }

  /**
   * TODO: figure out how this calculation works and make it more clear instead of
   * having it all happen on one line
   * *custom function
   * 
   * @return The current velocity of the drive motor (meters per second)
   */
  private double getDriveWheelVelocity() {
    double driveMotorRotationsPerSecond = m_driveMotor.getEncoder().getVelocity() / 60.0; // RPM to RPS
    double driveWheelMetersPerSecond = (driveMotorRotationsPerSecond / DRIVE_GEAR_RATIO)
        * (WHEEL_DIAMETER * Math.PI);
    graph[m_driveMotor.getDeviceId() == 2 ? 1 : 3] = driveWheelMetersPerSecond;
    return driveWheelMetersPerSecond;
  }

  /**
   * *custom function
   * 
   * @return The current state of the module.
   */
  public double getModuleState() {
    return getDriveWheelVelocity();
  }

  /** resets drive motor position */
  public void resetDriveMotorPosition() {
    m_driveMotor.getEncoder().setPosition(0);
  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState Desired state with speed and angle.
   */
  public void setDesiredState(double speedMetersPerSecond) {
    // Calculate the drive output from the drive PID controller then set drive
    // motor.
    double driveOutput = drivePIDController.calculate(getDriveWheelVelocity(),
        speedMetersPerSecond);
    double driveFeedforwardOutput = driveFeedForward.calculate(speedMetersPerSecond);
    m_driveMotor.setVoltage(driveOutput + driveFeedforwardOutput);

    graph[m_driveMotor.getDeviceId() == 2 ? 0 : 2] = speedMetersPerSecond;

    SmartDashboard.putNumberArray("drive", graph);
  }

  public void stop() {
    m_driveMotor.setVoltage(0);
  }

  // SHUFFLEBOARD STUFF

  private void setupShuffleboard(double driveMotorChannel) {
    ShuffleboardUI.Test.addSlider("Drive " + driveMotorChannel, m_driveMotor.get(), -1, 1).subscribe(m_driveMotor::set);
  }
}
