package frc.robot;

// WPILib imports
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
// Local imports
import frc.robot.commands.manual.*;
import frc.robot.control.*;
import frc.robot.shuffleboard.ShuffleboardUI;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here
  private final Drivetrain _drivetrain;
  private final Channel _channel;
  private final Shooter _shooter;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // Init subsystems
    _drivetrain = new Drivetrain();
    _channel = new Channel();
    _shooter = new Shooter();

    // Sets up Control scheme chooser
    ShuffleboardUI.Overview.addControls(new Xbox(0, 1));

    // Bindings and Teleop
    configureButtonBindings();
  }

  public void teleopInit() {
    // Sets default command for manual drive. It is the only one right now
    _drivetrain.setDefaultCommand(new ManualDrive(_drivetrain));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new Trigger(() -> ShuffleboardUI.Overview.getControl().getReverse())
        .toggleOnTrue(new Reverse(_shooter, _channel));
    new Trigger(() -> ShuffleboardUI.Overview.getControl().getShoot())
        .toggleOnTrue(new Shoot(_channel, _shooter));
    new Trigger(() -> ShuffleboardUI.Overview.getControl().getLoad())
        .toggleOnTrue(new ChannelLoad(_channel, _shooter));
  }

  public void testPeriodic() {
    ShuffleboardUI.Test.testPeriodic();
  }
}
